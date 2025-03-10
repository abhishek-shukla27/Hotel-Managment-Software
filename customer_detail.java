import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class customer_detail extends Frame {

     @SuppressWarnings("unused")
    public customer_detail(){
        setTitle("Hotel Blue Queen-Check Out");
        setSize(600,500);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);

        Label headerLabel=new Label("Customer Check-In Details",Label.CENTER);
        headerLabel.setFont(new Font("Arial",Font.BOLD,16));
        add(headerLabel, BorderLayout.NORTH);

        TextArea displayArea = new TextArea();
        displayArea.setEditable(false);
        add(displayArea, BorderLayout.CENTER);

        Button fetchButton = new Button("Show the details of Customers");
        add(fetchButton, BorderLayout.NORTH);


        fetchButton.addActionListener(e -> {
            // Fetch Data from Database and Display
            List<String> customerDetails = fetchDataFromDatabase();
            if (customerDetails.isEmpty()) {
                displayArea.setText("No customer data found.");
            } else {
                StringBuilder sb = new StringBuilder();
                for (String detail : customerDetails) {
                    sb.append(detail).append("\n\n");
                }
                displayArea.setText(sb.toString());
            }
        });

        addWindowListener(new WindowAdapter() {
            public void closingWindow(WindowEvent e){
                dispose();
            }
        });
        setVisible(true);
    }

    private List<String> fetchDataFromDatabase() {
        String dbURL = "jdbc:sqlite:hotel_checkIn.db";
        String selectQuery = "SELECT * FROM Customer_CheckIn";

        List<String> customerDetails = new ArrayList<>();

        try (Connection conn = DriverManager.getConnection(dbURL);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectQuery)) {

            while (rs.next()) {
                // Fetching individual fields
                int id = rs.getInt("customerId");
                String name = rs.getString("name");
                String address = rs.getString("address");
                String phone = rs.getString("phone");
                String checkInDate = rs.getString("check_in_date");
                String checkInTime = rs.getString("check_in_time");
                String roomType = rs.getString("room_type");

                // Formatting the data
                String detail = String.format(
                    "ID: %d\nName: %s\nAddress: %s\nPhone: %s\nCheck-In Date: %s\nCheck-In Time: %s\nRoom Type: %s",
                    id, name, address, phone, checkInDate, checkInTime, roomType
                );

                customerDetails.add(detail);
            }

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return customerDetails;
    }
    
    public static void main(String[] args) {
        new customer_detail();
    }
}
