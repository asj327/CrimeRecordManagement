import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * CRMSApplication is the main entry point for the GUI.
 * It handles the login process and launches the appropriate dashboard upon success.
 */
public class CRMSApplication {

    /**
     * Entry method for launching the login GUI.
     */
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            // First, prompt for the sensitive MySQL connection credentials.
            // These are needed to instantiate the UserAuthenticator.
            String dbUser = "root";
            String dbPass = getPasswordFromUser("Enter MySQL Database Password", "Database Connection Setup");

            if (dbPass != null) {
                // If credentials are provided, launch the login window.
                new LoginApp(dbUser, dbPass);
            } else {
                JOptionPane.showMessageDialog(null, "Application canceled by user.", "Exit", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    /**
     * Utility method to get the database password securely via a GUI prompt.
     */
    private static String getPasswordFromUser(String message, String title) {
        JPasswordField passField = new JPasswordField();

        // Pre-fill with a common username to guide the user
        JTextField userField = new JTextField("root");

        JPanel panel = new JPanel(new GridLayout(0, 1));
        panel.add(new JLabel("DB Username (root):"));
        panel.add(userField);
        panel.add(new JLabel(message + ":"));
        panel.add(passField);

        int result = JOptionPane.showConfirmDialog(null, panel, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            // NOTE: We only return the password as the username is fixed for now
            return new String(passField.getPassword());
        }
        return null;
    }

    /**
     * The main login window frame.
     */
    private static class LoginApp extends JFrame implements ActionListener {
        private final UserAuthenticator authenticator;
        private final JTextField userField;
        private final JPasswordField passField;

        public LoginApp(String dbUser, String dbPass) {
            // Initialize the secure authenticator with the database credentials
            this.authenticator = new UserAuthenticator(dbUser, dbPass);

            setTitle("CRMS Login");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(400, 250);
            setLocationRelativeTo(null);
            setLayout(new GridLayout(4, 2, 10, 10));

            // Initialize UI Components
            userField = new JTextField(20);
            passField = new JPasswordField(20);
            JButton loginButton = new JButton("Login");
            loginButton.addActionListener(this);

            // Add Components to the Frame
            add(new JLabel("Username:"));
            add(userField);
            add(new JLabel("Password:"));
            add(passField);
            add(new JLabel()); // Empty space for layout
            add(loginButton);

            setVisible(true);
        }

        @Override
        public void actionPerformed(ActionEvent e) {
            String username = userField.getText();
            String password = new String(passField.getPassword());

            // Attempt authentication using the secure core
            UserAuthenticator.UserData userData = authenticator.authenticateUser(username, password);

            if (userData != null) {
                // SUCCESS: User is authenticated and active
                JOptionPane.showMessageDialog(this, "Login Successful! Role: " + userData.getUserRole(), "Success", JOptionPane.INFORMATION_MESSAGE);

                // Close login and open dashboard
                new DashboardPanel(userData.getUsername(), userData.getUserRole());
                dispose();
            } else {
                // FAILURE
                JOptionPane.showMessageDialog(this, "Invalid Username, Password, or Inactive Account.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Temporary placeholder for the post-login dashboard.
     */
    private static class DashboardPanel extends JFrame {
        public DashboardPanel(String username, String userRole) {
            setTitle("CRMS Dashboard - " + userRole);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(600, 400);
            setLocationRelativeTo(null);
            setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));

            JLabel welcomeLabel = new JLabel("Welcome, " + username + "!");
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 18));

            JLabel roleLabel = new JLabel("Access Level: " + userRole);
            roleLabel.setFont(new Font("Arial", Font.ITALIC, 14));

            add(welcomeLabel);
            add(roleLabel);
            add(new JLabel("This is your role-specific dashboard."));

            setVisible(true);
        }
    }
}