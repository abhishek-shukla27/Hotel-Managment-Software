import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

public class login extends JFrame implements ActionListener{

    private
    JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;

    public login(){
         
        setTitle("Login Page");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600,500);
        setLayout(new GridLayout(15,2,2,2));
        setLocationRelativeTo(null);
        JLabel usernameLabel=new JLabel("Username:");
        usernameField=new JTextField();
        JLabel passwordLabel=new JLabel("Password:");
        passwordField=new JPasswordField();

        loginButton=new JButton("Login");
        loginButton.addActionListener(this);

        add(usernameLabel);
        add(usernameField);
        add(passwordLabel);
        add(passwordField);
        add(new JLabel());
        add(loginButton);
        setVisible(true);        
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        if(e.getSource()==loginButton){
            String username=usernameField.getText();
            String password= new String(passwordField.getPassword());

            if(isValidLogin(username,password)){
                JOptionPane.showMessageDialog(this,"Login Succesful");
                dispose();
                WelcomePage Welcome=new WelcomePage();
                Welcome.setVisible(true);
            }
            else{
                JOptionPane.showMessageDialog(this, "Invalid username or password","Login Error",JOptionPane.ERROR_MESSAGE);
                usernameField.setText("");
                passwordField.setText("");
            }
        }
    }  
    private boolean isValidLogin(String username,String password){
        return username.equals("abcd") && password.equals("1234");
    } 

    public static void main(String[] args) {
        new login();
    }
}