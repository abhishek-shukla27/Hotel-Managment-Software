import java.awt.*;
import javax.swing.*;

public class WelcomePage {
    private JFrame frame;

    public WelcomePage() {
        frame = new JFrame("Hotel Management System");
        frame.setSize(600, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(44, 62, 80));

        JLabel titleLabel = new JLabel("Welcome to Hotel Management System", JLabel.CENTER);
        titleLabel.setFont(new Font("SansSerif", Font.BOLD, 22));
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setBorder(BorderFactory.createEmptyBorder(20, 10, 20, 10));
        frame.add(titleLabel, BorderLayout.NORTH);

        JPanel buttonPanel = new JPanel(new GridLayout(4, 1, 15, 15));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(30, 50, 30, 50));
        buttonPanel.setBackground(new Color(44, 62, 80));

        JButton checkInButton = createStyledButton("Check-In");
        JButton checkOutButton = createStyledButton("Check-Out");
        JButton aboutUsButton = createStyledButton("About Us");
        JButton staffDataButton = createStyledButton("Staff & Employee Data");

        buttonPanel.add(checkInButton);
        buttonPanel.add(checkOutButton);
        buttonPanel.add(aboutUsButton);
        buttonPanel.add(staffDataButton);

        frame.add(buttonPanel, BorderLayout.CENTER);

        checkInButton.addActionListener(e -> {
            frame.dispose();
            new CheckInPage(); // Open Check-In Page
        });

        checkOutButton.addActionListener(e -> {
            frame.dispose();
            new CheckoutPage(); // Open Check-Out Page
        });

        aboutUsButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Hotel XYZ - Best in Hospitality", "About Us", JOptionPane.INFORMATION_MESSAGE);
        });

        staffDataButton.addActionListener(e -> {
            JOptionPane.showMessageDialog(frame, "Staff Data: Confidential", "Staff & Employee Data", JOptionPane.INFORMATION_MESSAGE);
        });

        frame.setVisible(true);
    }

    private JButton createStyledButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("SansSerif", Font.BOLD, 16));
        button.setForeground(Color.WHITE);
        button.setBackground(new Color(52, 152, 219));
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
        button.setCursor(new Cursor(Cursor.HAND_CURSOR));
        return button;
    }

    public static void main(String[] args) {
        new WelcomePage();
    }

    public void setVisible(boolean b) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setVisible'");
    }
}
