import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class CheckInPage extends Frame {

    private Label clockLabel;
    private TextField checkInTimeField;

    public CheckInPage() {
        // Frame Title
        setTitle("Hotel Blue Queen-Check In");
        
        // Frame Size and Layout
        setSize(600, 500);
        setLayout(new GridLayout(8, 2, 10, 10));
        setLocationRelativeTo(null);

        // Creating components for Check-In form
        Label nameLabel = new Label("Name:");
        TextField nameField = new TextField(20);

        Label addressLabel = new Label("Address:");
        TextField addressField = new TextField(20);

        Label phoneLabel = new Label("Phone Number:");
        TextField phoneField = new TextField(15);

        Label checkInDateLabel = new Label("Check-In Date:");
        TextField checkInDateField = new TextField(10);

        Label checkInTimeLabel = new Label("Check-In Time:");
        checkInTimeField = new TextField(10);
        autoFillCheckInTime();  // Automatically fill with the current time

        Label roomTypeLabel = new Label("Room Type:");
        Choice roomTypeChoice = new Choice();
        roomTypeChoice.add("Single");
        roomTypeChoice.add("Double");
        roomTypeChoice.add("Suite");

        // Clock label to show current date and time
        clockLabel = new Label("", Label.CENTER);
        clockLabel.setFont(new Font("Arial", Font.BOLD, 14));
        startClock();

        Button submitButton = new Button("Submit");
        Button backButton = new Button("Back");

        // Adding action listener for submit button
        submitButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String name = nameField.getText();
                String address = addressField.getText();
                String phone = phoneField.getText();
                String checkInDate = checkInDateField.getText();
                String checkInTime = checkInTimeField.getText();
                String roomType = roomTypeChoice.getSelectedItem();

                if(name.isEmpty()||address.isEmpty()||phone.isEmpty()||checkInDate.isEmpty()||checkInTime.isEmpty())
                {
                    showMessageDialog("Please fill all fields");
                    return;
                }   
            
                saveToDatabase(name,address,phone,checkInDate,checkInTime,roomType);
                // Clear the form fields after submission
                nameField.setText("");
                addressField.setText("");
                phoneField.setText("");
                checkInDateField.setText("");
                checkInTimeField.setText("");
                roomTypeChoice.select(0);
            }
        });

        // Adding action listener for back button
        backButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                dispose(); // Close Check-In window
                new WelcomePage().setVisible(true); // Reopen the main welcome page
            }
        });

        
        add(clockLabel); // Adding the clock label at the top
        add(new Label("")); // Empty cell for layout adjustment
        add(nameLabel);
        add(nameField);
        add(addressLabel);
        add(addressField);
        add(phoneLabel);
        add(phoneField);
        add(checkInDateLabel);
        add(checkInDateField);
        add(checkInTimeLabel);
        add(checkInTimeField);
        add(roomTypeLabel);
        add(roomTypeChoice);
        add(submitButton);
        add(backButton);

        // Adding window close event
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                dispose();
            }
        });
        
        setVisible(true);
    }

    // Method to start the clock and update the clockLabel every second
    private void startClock() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SimpleDateFormat sdf = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
                String currentTime = sdf.format(new Date());
                
                // Updating the clockLabel with current date and time
                clockLabel.setText(currentTime);
            }
        }, 0, 1000);
    }

    // Method to automatically fill the Check-In Time field with the current time
    private void autoFillCheckInTime() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        String currentTime = timeFormat.format(new Date());
        checkInTimeField.setText(currentTime);
    }

    private void showMessageDialog(String message) {
        Dialog dialog = new Dialog(this, "Message", true);
        dialog.setLayout(new FlowLayout());
        Label msgLabel = new Label(message);
        Button okButton = new Button("OK");
        okButton.addActionListener(e -> dialog.dispose());
        dialog.add(msgLabel);
        dialog.add(okButton);
        dialog.setSize(300, 150);
        dialog.setLocationRelativeTo(this);
        dialog.setVisible(true);
    }

    private void saveToDatabase(String name, String address, String phone, String checkInDate, String checkInTime, String roomType) {
        String dbURL = "jdbc:sqlite:hotel_checkIn.db";
        String insertQuery = "INSERT INTO Customer_CheckIn  (name, address, phone, check_in_date, check_in_time, room_type) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = DriverManager.getConnection(dbURL);
             PreparedStatement pstmt = conn.prepareStatement(insertQuery)) {

            pstmt.setString(1, name);
            pstmt.setString(2, address);
            pstmt.setString(3, phone);
            pstmt.setString(4, checkInDate);
            pstmt.setString(5, checkInTime);
            pstmt.setString(6, roomType);

            pstmt.executeUpdate();
            showMessageDialog("Check-In details saved successfully!");

        } catch (Exception ex) {
            ex.printStackTrace();
            showMessageDialog("Error saving to database: " + ex.getMessage());
        }
    }

    public static void main(String[] args) {
        new CheckInPage();
    }
}
