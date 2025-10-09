import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Admin Control Panel Only: Manages user access status in the database.
 * The database name is 'admin' and the table name for officers is 'users'.
 * To run this code, you must have the MySQL server running and the JDBC Connector
 * JAR file correctly included in your classpath.
 */
public class  AdminPage {

    // --- JDBC CONNECTION DETAILS (BACKEND CONFIG) ---
    // NOTE: If you are using XAMPP or a different MySQL setup, verify the port is 3306.
    private static final String DB_URL = "jdbc:mysql://localhost:3306/admin?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";
    // *** IMPORTANT: Ensure this password matches your MySQL 'root' user password. ***
    // *** If your root user has no password, use an empty string: "" ***
    private static final String PASS = "root"; // <-- CHANGE THIS IF YOUR PASSWORD ISN'T 'root'

    // ====================================================================
    // 1. ADMIN CONTROL PANEL (Your primary interface)
    // ====================================================================

    /**
     * Inner class for the Admin Control Panel Window (Admin UI).
     */
    static class AdminControlPanel extends JFrame {
        private final JPanel mainPanel;
        private final JButton backButton;
        private final JButton addUserButton;

        public AdminControlPanel() {
            this.setTitle("CRMS Admin Control Panel - Access Management");
            this.setBounds(300, 100, 550, 650); // Increased height for new buttons
            this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            this.mainPanel = new JPanel();
            this.mainPanel.setLayout(new BoxLayout(this.mainPanel, BoxLayout.Y_AXIS));

            // --- Header Panel (North) ---
            JPanel headerPanel = new JPanel(new BorderLayout());
            headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            this.backButton = new JButton("<- Back to Dashboard");
            this.backButton.setFocusPainted(false);
            this.backButton.addActionListener(e -> navigateBackToDashboard());

            JLabel titleLabel = new JLabel("Officer Access Control", SwingConstants.CENTER);
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));

            headerPanel.add(this.backButton, BorderLayout.WEST);
            headerPanel.add(titleLabel, BorderLayout.CENTER);

            // --- Management Bar (Below Header) ---
            JPanel managementBar = new JPanel(new FlowLayout(FlowLayout.RIGHT));
            managementBar.setBorder(BorderFactory.createEmptyBorder(0, 10, 10, 10));

            this.addUserButton = new JButton("Add New User");
            this.addUserButton.setFont(new Font("Arial", Font.BOLD, 14));
            this.addUserButton.setBackground(new Color(50, 150, 50)); // Green color
            this.addUserButton.setForeground(Color.WHITE);
            this.addUserButton.addActionListener(e -> showAddUserDialog()); // New: Action listener for Add User

            managementBar.add(this.addUserButton);

            // Add Header and Management Bar to the frame
            this.getContentPane().setLayout(new BorderLayout());
            JPanel northPanel = new JPanel(new BorderLayout());
            northPanel.add(headerPanel, BorderLayout.NORTH);
            northPanel.add(managementBar, BorderLayout.CENTER);
            this.getContentPane().add(northPanel, BorderLayout.NORTH);

            // --- Officer List Panel (Center) ---
            JScrollPane scrollPane = new JScrollPane(this.mainPanel);
            scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
            this.getContentPane().add(scrollPane, BorderLayout.CENTER);

            this.loadOfficerList();
            this.setVisible(true);
        }

        /**
         * Fetches all user records from the database.
         * @return A list of arrays: [id, username, is_active]
         */
        private List<Object[]> getAllOfficers() {
            List<Object[]> officers = new ArrayList<>();
            // QUERY: Selects from the 'users' table, which is confirmed to be the correct table name.
            String sql = "SELECT id, username, is_active FROM users ORDER BY username";

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                // *** CRITICAL DEBUGGING POINT ***
                // The code below is correct for processing results. If it fails, the issue is:
                // 1. Database connection failed (see catch block below).
                // 2. The 'users' table is empty (run 'SELECT * FROM users' in your SQL client).
                // 3. The 'users' table is missing 'id', 'username', or 'is_active' columns.
                while (rs.next()) {
                    officers.add(new Object[]{
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getBoolean("is_active")
                    });
                }
            } catch (SQLException ex) {
                // This block executes if the connection fails or the SQL query is invalid.
                // Check your MySQL server status and JDBC connector JAR in your project's classpath.
                JOptionPane.showMessageDialog(this,
                        "Database error while fetching officers: " + ex.getMessage() +
                                "\nCHECK: MySQL server is running, database 'admin' exists, and password is correct.",
                        "Database Connection Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace(); // Print the full stack trace for detailed error info
            }
            return officers;
        }
        private void showAddUserDialog() {
            JTextField usernameField = new JTextField(15);
            JPasswordField passwordField = new JPasswordField(15);

            JPanel myPanel = new JPanel(new GridLayout(0, 2, 10, 10));
            myPanel.add(new JLabel("Username:"));
            myPanel.add(usernameField);
            myPanel.add(new JLabel("Password:"));
            myPanel.add(passwordField);

            int result = JOptionPane.showConfirmDialog(this, myPanel,
                    "Add New Officer", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                String username = usernameField.getText().trim();
                String password = new String(passwordField.getPassword());
                if (!username.isEmpty() && !password.isEmpty()) {
                    addNewUser(username, password);
                } else {
                    JOptionPane.showMessageDialog(this, "Username and Password cannot be empty.", "Input Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
        private void addNewUser(String username, String password) {
            // is_active is set to 1 (true) by default for a new user
            String sql = "INSERT INTO users (username, password, is_active) VALUES (?, ?, 1)";

            try (
                    Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    PreparedStatement pstmt = conn.prepareStatement(sql);
            ) {
                pstmt.setString(1, username);
                pstmt.setString(2, password); // NOTE: Use HASHED passwords in a real application!

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "User " + username + " added successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadOfficerList(); // Refresh the UI
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to add user.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                // Check for duplicate key error (if username is unique)
                if (ex.getSQLState().startsWith("23")) { // SQLState for Integrity Constraint Violation
                    JOptionPane.showMessageDialog(this, "Error: Username already exists.", "Database Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(this, "Database error during user addition: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                    ex.printStackTrace();
                }
            }
        }


        private void navigateBackToDashboard() {
            // Close the current Officer Access Control window
            this.dispose();

            // Re-open the Admin Dashboard
            SwingUtilities.invokeLater(() -> new AdminDashboardUI().setVisible(true));
        }

        // --- Remaining methods (getAllOfficers, loadOfficerList, etc.) go here ---

        // [Existing database methods are assumed to be here, unchanged]



            /**
             * Clears and re-renders the list of officers with toggle buttons.
             */
        private void loadOfficerList() {
            // Clear all components added after the title (index 2 onwards)
            Component[] components = mainPanel.getComponents();
            for (int i = components.length - 1; i >= 2; i--) {
                mainPanel.remove(components[i]);
            }

            List<Object[]> officers = getAllOfficers();

            if (officers.isEmpty()) {
                mainPanel.add(new JLabel("No officers found in the database."));
            }

            for (Object[] officer : officers) {
                int id = (int) officer[0];
                String username = (String) officer[1];
                boolean isActive = (boolean) officer[2];

                JPanel officerRow = createOfficerRow(id, username, isActive);
                mainPanel.add(officerRow);
                mainPanel.add(Box.createVerticalStrut(5));
            }

            mainPanel.revalidate();
            mainPanel.repaint();
        }

        /**
         * Creates a single row component for one officer with a toggle button.
         */
        private JPanel createOfficerRow(int id, String username, boolean isActive) {
            // Changed FlowLayout to put components on the left/center/right
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
            panel.setBorder(BorderFactory.createEtchedBorder());
            panel.setMaximumSize(new Dimension(500, 40));

            JLabel nameLabel = new JLabel("User: " + username);
            nameLabel.setPreferredSize(new Dimension(130, 30));

            JLabel statusLabel = new JLabel(isActive ? "STATUS: GRANTED" : "STATUS: REVOKED");
            statusLabel.setForeground(isActive ? Color.BLUE.darker() : Color.RED.darker());
            statusLabel.setPreferredSize(new Dimension(140, 30));

            JButton toggleButton = new JButton(isActive ? "REVOKE" : "GRANT");
            toggleButton.setPreferredSize(new Dimension(90, 25));

            JButton deleteButton = new JButton("DELETE"); // New: Delete button
            deleteButton.setPreferredSize(new Dimension(80, 25));
            deleteButton.setBackground(Color.RED.darker());
            deleteButton.setForeground(Color.WHITE);

            // Toggle action listener
            toggleButton.addActionListener(e -> toggleAccessStatus(id, !isActive));

            // New: Delete action listener
            deleteButton.addActionListener(e -> confirmAndDeleteUser(id, username));

            panel.add(nameLabel);
            panel.add(statusLabel);
            panel.add(toggleButton);
            panel.add(deleteButton); // Added delete button

            return panel;
        }

        private void confirmAndDeleteUser(int id, String username) {
            int result = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to PERMANENTLY delete user: " + username + "?",
                    "Confirm Deletion", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);

            if (result == JOptionPane.YES_OPTION) {
                deleteUser(id);
            }
        }

        private void deleteUser(int id) {
            String sql = "DELETE FROM users WHERE id = ?";

            try (
                    Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                    PreparedStatement pstmt = conn.prepareStatement(sql);
            ) {
                pstmt.setInt(1, id);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    JOptionPane.showMessageDialog(this, "User deleted successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
                    loadOfficerList(); // Refresh the UI
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to delete user. User ID not found.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Database error during user deletion: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }

        /**
         * BACKEND: Updates the 'is_active' status for a specific user ID.
         */
        private void toggleAccessStatus(int id, boolean newStatus) {
            // UPDATE: Correctly targets the 'users' table.
            String sql = "UPDATE users SET is_active = ? WHERE id = ?";

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 PreparedStatement pstmt = conn.prepareStatement(sql)) {

                pstmt.setBoolean(1, newStatus);
                pstmt.setInt(2, id);

                int rowsAffected = pstmt.executeUpdate();

                if (rowsAffected > 0) {
                    System.out.println("Status updated for ID: " + id + ". New status: " + newStatus);
                    loadOfficerList(); // Refresh the UI
                } else {
                    JOptionPane.showMessageDialog(this, "Failed to update status for user ID: " + id + ". User ID may not exist.");
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Database error during status update: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
                ex.printStackTrace();
            }
        }
    }


    // ====================================================================
    // 2. MAIN APPLICATION STARTUP
    // ====================================================================

    /**
     * The main entry point for the system.
     * It now directly launches the Admin Control Panel.
     */
    public static void main(String[] args) {
        // Ensure all UI creation is on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(AdminControlPanel::new);
    }
}