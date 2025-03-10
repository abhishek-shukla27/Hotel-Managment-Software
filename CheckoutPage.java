import java.awt.Button;
import java.awt.Choice;
import java.awt.Desktop;
import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.print.Doc;
import javax.print.DocFlavor;
import javax.print.DocPrintJob;
import javax.print.PrintServiceLookup;
import javax.print.SimpleDoc;
import javax.print.attribute.HashPrintRequestAttributeSet;

public class CheckoutPage extends Frame {

    private Choice customerChoice;
    private Label billLabel;
    private static final int SINGLE_ROOM_RATE = 1000;
    private static final int DOUBLE_ROOM_RATE = 2000;
    private static final int SUITE_ROOM_RATE = 4000;

    public CheckoutPage() {
        setTitle("Customer Checkout");
        setSize(500, 300);
        setLayout(new GridLayout(6, 2, 10, 10));
        setLocationRelativeTo(null);

        Label selectLabel = new Label("Select Customer:");
        customerChoice = new Choice();
        loadCustomers();

        Label billTextLabel = new Label("Total Bill:");
        billLabel = new Label("Rs. 0");

        Button calculateButton = new Button("Calculate Bill");
        Button checkoutButton = new Button("Confirm Checkout");
        Button generateBillButton = new Button("Generate Bill");

        calculateButton.addActionListener(e -> calculateBill());
        checkoutButton.addActionListener(e -> {
            if (customerChoice.getSelectedItem() != null) {
                confirmCheckout(customerChoice.getSelectedItem());
            }
        });
        generateBillButton.addActionListener(e -> generateBill());

        add(selectLabel);
        add(customerChoice);
        add(billTextLabel);
        add(billLabel);
        add(calculateButton);
        add(checkoutButton);
        add(generateBillButton);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });

        setVisible(true);
    }

    private void loadCustomers() {
        String dbURL = "jdbc:sqlite:hotel_checkIn.db";
        String query = "SELECT name FROM Customer_CheckIn";

        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                customerChoice.add(rs.getString("name"));
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void calculateBill() {
        String selectedCustomer = customerChoice.getSelectedItem();
        if (selectedCustomer == null) {
            billLabel.setText("No customer selected!");
            return;
        }

        String dbURL = "jdbc:sqlite:hotel_checkIn.db";
        String query = "SELECT check_in_date, check_in_time, room_type FROM Customer_CheckIn WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(query)) {

            pstmt.setString(1, selectedCustomer);
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                String checkInDateStr = rs.getString("check_in_date");
                String checkInTimeStr = rs.getString("check_in_time");
                String roomType = rs.getString("room_type");

                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("d-MM-yyyy HH:mm:ss");
                LocalDateTime checkInDateTime = LocalDateTime.parse(checkInDateStr + " " + checkInTimeStr, formatter);
                LocalDateTime checkOutDateTime = LocalDateTime.now();

                long daysStayed = Duration.between(checkInDateTime, checkOutDateTime).toDays();
                if (Duration.between(checkInDateTime, checkOutDateTime).toHours() % 24 > 0) {
                    daysStayed += 1;
                }

                int roomRate = switch (roomType.toLowerCase()) {
                    case "single" -> SINGLE_ROOM_RATE;
                    case "double" -> DOUBLE_ROOM_RATE;
                    case "suite" -> SUITE_ROOM_RATE;
                    default -> SINGLE_ROOM_RATE;
                };

                long totalBill = daysStayed * roomRate;
                billLabel.setText("Rs. " + totalBill + " (" + daysStayed + " days)");
            } else {
                billLabel.setText("No check-in record found!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            billLabel.setText("Error fetching data!");
        }
    }

    private void confirmCheckout(String customerName) {
        String dbURL = "jdbc:sqlite:hotel_checkIn.db";
        String deleteQuery = "DELETE FROM Customer_CheckIn WHERE name = ?";

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {

            pstmt.setString(1, customerName);
            int rowsAffected = pstmt.executeUpdate();

            if (rowsAffected > 0) {
                billLabel.setText("Checkout Successful!");
                customerChoice.remove(customerName);
            } else {
                billLabel.setText("Checkout Failed!");
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void generateBill() {
        try {
            File billFile = new File("CustomerBill.txt");
            FileWriter writer = new FileWriter(billFile);
            writer.write("--- Hotel Bill Receipt ---\n");
            writer.write("Customer Name: " + customerChoice.getSelectedItem() + "\n");
            writer.write("Total Amount: " + billLabel.getText() + "\n");
            writer.write("---------------------------\n");
            writer.close();
            Desktop.getDesktop().print(billFile);
            printBill(billFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void printBill(File billFile) {
        try {
            DocPrintJob job = PrintServiceLookup.lookupDefaultPrintService().createPrintJob();
            FileInputStream fis = new FileInputStream(billFile);
            Doc doc = new SimpleDoc(fis, DocFlavor.INPUT_STREAM.AUTOSENSE, null);
            job.print(doc, new HashPrintRequestAttributeSet());
            fis.close();
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        new CheckoutPage();
    }
}
