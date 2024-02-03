import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class RegistrationForm extends JDialog { // transform to J dialog
    private JTextField tfName;
    private JTextField tfEmail;
    private JTextField tfPhone;
    private JTextField tfAddress;
    private JButton btnRegister;
    private JButton btnCancel;
    private JPanel registrationPanel;
    private JTextField tfgender;


    //Constructor that will display this dialog
    public RegistrationForm(JFrame parent){
        super (parent);
        setTitle("Registration Form");
        setContentPane(registrationPanel);
        setMinimumSize(new Dimension(1200, 700));
        setModal(true);
        setLocationRelativeTo(parent);

        //Buttons
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        btnRegister.addActionListener(e -> registerUser());
        btnCancel.addActionListener(e -> dispose());
        setVisible(true);
    }

    private void registerUser() {
        String Name = tfName.getText();
        String EmailAddress = tfEmail.getText();
        String ContactNumber = tfPhone.getText();
        String LocalRegisteredAddress = tfAddress.getText();
        String Gender = tfgender.getText();

        if (Name.isEmpty() || Gender.isEmpty() || EmailAddress.isEmpty() || ContactNumber.isEmpty() || LocalRegisteredAddress.isEmpty()){
            JOptionPane.showMessageDialog(this, "Please enter all fields", "try again",JOptionPane.ERROR_MESSAGE);
            return;
        }
        //add user to database
       user = addUserToDatabase(Name, Gender, EmailAddress, ContactNumber, LocalRegisteredAddress);
        if (user != null){//not-equal-to operator or not existed will
            // returns true if the operands don't have the same value
            dispose();
        }
        else { //if existing user
            JOptionPane.showMessageDialog(this, "Failed to register a new user", "Try again", JOptionPane.ERROR_MESSAGE);
        }
    }
public User user; //Database
    private User addUserToDatabase(String Name, String Gender, String EmailAddress, String ContactNumber, String LocalRegisteredAddress )
    {
        User user = null;
        final String DB_URL = "jdbc:mysql://localhost:3306/attendance?serverTimezone=UTC"; //link for sql database
        final String USERNAME = "root";
        final String PASSWORD = "";

        try {//try connect to database
            Connection conn = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            Statement stmt = conn.createStatement();

            String sql = "INSERT INTO users (Name, Gender, EmailAddress, ContactNumber, LocalRegisteredAddress)" + "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, Name);
            preparedStatement.setString(2, Gender);
            preparedStatement.setString(3, EmailAddress);
            preparedStatement.setString(4, ContactNumber);
            preparedStatement.setString(5, LocalRegisteredAddress);


            //insert row into the table
            int addedRows = preparedStatement.executeUpdate();
            if (addedRows > 0) {
                user = new User();
                user.Name = Name;
                user.Gender = Name;
                user.EmailAddress = EmailAddress;
                user.ContactNumber = ContactNumber;
                user.LocalRegisteredAddress = LocalRegisteredAddress;

            }

            stmt.close();
            conn.close();

        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        return user;
    }
    //main method
    public static void main (String [] args)
    {
        RegistrationForm RegForm = new RegistrationForm(null);
        User user = RegForm.user;
        if (user != null){
            System.out.println("Succesful registration of: " + user.Name);
        }
        else{
            System.out.println("Registration cancelled");
        }
    }
}
