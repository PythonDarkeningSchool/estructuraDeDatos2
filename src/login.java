import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Hashtable;

public class login {
    private JTabbedPane helpTab;
    private JPanel loginPanel;
    private JTextField userIDInputField;
    private JButton enterButton;
    private JButton clearButton;
    private JTextArea helpTextArea;

    private static JFrame frame = new JFrame("login");


    public login() {
        // Listeners
        enterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String userID = userIDInputField.getText();
                Hashtable users = getUsers();

                if (users.get(userID) == "true") {
                    // dispose the current frame
                    frame.dispose();
                    // call to control frame
                    control administration = new control();
                    administration.createFrame();
                }
                else if (users.get(userID) == "false") {
                    JOptionPane.showMessageDialog(frame, "The user entered is not active", "User inactive", JOptionPane.WARNING_MESSAGE);
                    userIDInputField.setText("");
                    userIDInputField.requestFocus();
                }
                else {
                    JOptionPane.showMessageDialog(frame, "The user entered does not exists", "Invalid user", JOptionPane.ERROR_MESSAGE);
                    userIDInputField.setText("");
                    userIDInputField.requestFocus();
                }
            }
        });
        clearButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                userIDInputField.setText("");
                userIDInputField.requestFocus();
            }
        });
        helpTab.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                helpTextArea.selectAll();
                StringBuffer text = new StringBuffer("Use the following credentials:\n\n");
                text.append("admin => simulate a valid user to enter in the system\n");
                text.append("user  => simulate a invalid user to enter in the system");
                helpTextArea.replaceSelection(text.toString());
                helpTextArea.setEditable(false);


            }
        });
    }

    // Custom functions
    private Hashtable getUsers(){
        // Create the Hashtable
        Hashtable<String, String> container=new Hashtable<>();
        // Insert some users
        container.put("admin", "true");
        container.put("user", "false");

        return container;
    }


    // main function
    public static void main(String[] args) {
        //JFrame frame = new JFrame("login");
        frame.setContentPane(new login().loginPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.pack();
        frame.setVisible(true);
    }
}
