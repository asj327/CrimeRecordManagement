
import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.mindrot.jbcrypt.BCrypt;

/**
 * Tool to securely create the first ADMIN user in the 'crimeRecords' database.
 * This tool hashes the password using BCrypt before insertion.
 * REQUIRES: The jBCrypt library (set up via pom.xml).
 */
public class AdminAccountCreator {

    private static final String DB_URL_CRMS = "jdbc:mysql://localhost:3306/crimeRecords?useSSL=false&serverTimezone=UTC";
    private static String mysqlUser = "";
    private static String mysqlPass = "";

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            if (getCredentialsFromUser()) {
                createNewAdminUser();
            } else {
                System.out.println("❌ Account creation cancelled.");
            }
        });
    }

    /**
     * Shows a GUI dialog to securely capture the MySQL username and password (used to connect).
     */
    private static boolean getCredentialsFromUser() {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        JTextField userField = new JTextField("root");
        JPasswordField passField = new JPasswordField();

        panel.add(new JLabel("MySQL Database Username:"));
        panel.add(userField);
        panel.add(new JLabel("MySQL Database Password:"));
        panel.add(passField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Enter MySQL DB Credentials (to insert user)", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            mysqlUser = userField.getText();
            mysqlPass = new String(passField.getPassword());
            return true;
        }
        return false;
    }

    /**
     * Prompts for new ADMIN user credentials and performs the secure insertion.
     */
    private static void createNewAdminUser() {
        JPanel panel = new JPanel(new GridLayout(0, 1));

        JTextField adminUserField = new JTextField("admin");
        JPasswordField adminPassField = new JPasswordField();

        panel.add(new JLabel("New ADMIN Username:"));
        panel.add(adminUserField);
        panel.add(new JLabel("New ADMIN Password:"));
        panel.add(adminPassField);

        int result = JOptionPane.showConfirmDialog(null, panel,
                "Create New ADMIN Account", JOptionPane.OK_CANCEL_OPTION,
                JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            String adminUsername = adminUserField.getText();
            String adminPassword = new String(adminPassField.getPassword());

            if (adminUsername.isEmpty() || adminPassword.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username and Password cannot be empty!", "Input Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // --- CORE SECURITY STEP: HASHING ---
            String hashed_password = BCrypt.hashpw(adminPassword, BCrypt.gensalt());

            String sql = "INSERT INTO users (username, password_hash, user_role, is_active) VALUES (?, ?, 'ADMIN', TRUE)";

            try (Connection conn = DriverManager.getConnection(DB_URL_CRMS, mysqlUser, mysqlPass);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setString(1, adminUsername);
                pstmt.setString(2, hashed_password);

                int rows = pstmt.executeUpdate();
                if (rows > 0) {
                    JOptionPane.showMessageDialog(null,
                            "✅ SUCCESS: New ADMIN user '" + adminUsername + "' created securely.",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    System.out.println("✅ Successfully inserted first ADMIN user into 'users' table.");
                } else {
                    JOptionPane.showMessageDialog(null, "Failed to insert user.", "Error", JOptionPane.ERROR_MESSAGE);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,
                        "Failed to insert user. Possible duplicate username or DB error.\nError: " + ex.getMessage(),
                        "Database Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("❌ Database insertion failed. Error: " + ex.getMessage());
            }
        }
    }
}
