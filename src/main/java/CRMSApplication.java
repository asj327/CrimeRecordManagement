import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingConstants;
import java.awt.event.*;

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
        private final JButton loginButton;
        private final JLabel messageLabel;

        public LoginApp(String dbUser, String dbPass) {
            // Initialize the secure authenticator with the database credentials
            this.authenticator = new UserAuthenticator(dbUser, dbPass);

            setTitle("Login Module");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setExtendedState(JFrame.MAXIMIZED_BOTH);
            setLocationRelativeTo(null);

            JPanel mainPanel = new JPanel(new GridBagLayout());
            mainPanel.setBackground(new Color(173, 216, 230)); // light blue
            add(mainPanel);

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.insets = new Insets(15, 15, 15, 15);
            gbc.fill = GridBagConstraints.HORIZONTAL;


            JLabel heading = new JLabel("Welcome to Crime Record Management System", SwingConstants.CENTER);
            heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
            heading.setForeground(new Color(0, 0, 128));
            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            mainPanel.add(heading, gbc);

            JLabel userLabel = new JLabel("Username:");
            userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            userField = new JTextField(18);
            userField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            gbc.gridwidth = 1;
            gbc.gridy = 1;
            gbc.gridx = 0;
            mainPanel.add(userLabel, gbc);
            gbc.gridx = 1;
            mainPanel.add(userField, gbc);

            // Password label & field
            JLabel passLabel = new JLabel("Password:");
            passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
            passField = new JPasswordField(18);
            passField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
            gbc.gridy = 2;
            gbc.gridx = 0;
            mainPanel.add(passLabel, gbc);
            gbc.gridx = 1;
            mainPanel.add(passField, gbc);

            loginButton = new JButton("Login");
            loginButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
            loginButton.setBackground(new Color(0, 102, 204));
            loginButton.setForeground(Color.WHITE);
            loginButton.setFocusPainted(false);
            gbc.gridy = 3;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            mainPanel.add(loginButton, gbc);


            messageLabel = new JLabel("", SwingConstants.CENTER);
            messageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
            messageLabel.setForeground(Color.RED);
            gbc.gridy = 4;
            mainPanel.add(messageLabel, gbc);




            loginButton.addActionListener(this);
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
                DashboardRouter.routeToDashboard(userData.getUsername(), userData.getUserRole());
                dispose();
            } else {
                // FAILURE
                JOptionPane.showMessageDialog(this, "Invalid Username, Password, or Inactive Account.", "Login Failed", JOptionPane.ERROR_MESSAGE);
            }
        }
    }



}
