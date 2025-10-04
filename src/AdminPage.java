import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Combined CRMS System: Contains the Officer Login UI and the Admin Control Panel.
 * To run this code, you must have the MySQL server running and the JDBC Connector
 * JAR file correctly included in your classpath.
 */
public class AdminPage {

    // --- JDBC CONNECTION DETAILS (BACKEND CONFIG) ---
    private static final String DB_URL = "jdbc:mysql://localhost:3306/basic_login_db?useSSL=false&serverTimezone=UTC";
    private static final String USER = "root";       // Your MySQL username
    private static final String PASS = "root"; // Your MySQL password (REPLACE THIS)

    // ====================================================================
    // 1. OFFICER LOGIN APPLICATION (Simulates the Officer's interface)
    // ====================================================================

    /**
     * Inner class for the Admin Control Panel Window (Admin UI).
     */
    static class AdminControlPanel extends JFrame {
        private final JPanel mainPanel;

        public AdminControlPanel() {
            setTitle("CRMS Admin Control Panel - Access Management");
            setBounds(300, 100, 550, 600);
            setDefaultCloseOperation(DISPOSE_ON_CLOSE);

            mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS)); // Vertical stacking

            JLabel titleLabel = new JLabel("Officer Access Control");
            titleLabel.setFont(new Font("Arial", Font.BOLD, 20));
            titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

            mainPanel.add(Box.createVerticalStrut(20));
            mainPanel.add(titleLabel);
            mainPanel.add(Box.createVerticalStrut(10));

            JScrollPane scrollPane = new JScrollPane(mainPanel);
            add(scrollPane, BorderLayout.CENTER);

            loadOfficerList(); // Populate the list of officers
        }

        /**
         * Fetches all user records from the database.
         * @return A list of arrays: [id, username, is_active]
         */
        private List<Object[]> getAllOfficers() {
            List<Object[]> officers = new ArrayList<>();
            String sql = "SELECT id, username, is_active FROM users ORDER BY username";

            try (Connection conn = DriverManager.getConnection(DB_URL, USER, PASS);
                 Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(sql)) {

                while (rs.next()) {
                    officers.add(new Object[]{
                            rs.getInt("id"),
                            rs.getString("username"),
                            rs.getBoolean("is_active")
                    });
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Database error while fetching officers: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
            return officers;
        }

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
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.LEFT));
            panel.setBorder(BorderFactory.createEtchedBorder());
            panel.setMaximumSize(new Dimension(500, 40));

            JLabel nameLabel = new JLabel("User: " + username);
            nameLabel.setPreferredSize(new Dimension(150, 30));

            JLabel statusLabel = new JLabel(isActive ? "STATUS: GRANTED" : "STATUS: REVOKED");
            statusLabel.setForeground(isActive ? Color.BLUE.darker() : Color.RED.darker());
            statusLabel.setPreferredSize(new Dimension(150, 30));

            JButton toggleButton = new JButton(isActive ? "REVOKE ACCESS" : "GRANT ACCESS");

            // Toggle action listener
            toggleButton.addActionListener(e -> toggleAccessStatus(id, !isActive));

            panel.add(nameLabel);
            panel.add(statusLabel);
            panel.add(toggleButton);

            return panel;
        }

        /**
         * BACKEND: Updates the 'is_active' status for a specific user ID.
         */
        private void toggleAccessStatus(int id, boolean newStatus) {
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
                    JOptionPane.showMessageDialog(this, "Failed to update status for user ID: " + id);
                }

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this,
                        "Database error during status update: " + ex.getMessage(),
                        "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    // ====================================================================
    // 3. MAIN APPLICATION STARTUP
    // ====================================================================

    /**
     * The main entry point for the system.
     * It launches a small window allowing the user to choose to open the
     * Officer Login screen or the Admin Control Panel.
     */
    static void main(String[] args) {
        // Ensure all UI creation is on the Event Dispatch Thread (EDT)
        SwingUtilities.invokeLater(() -> {
            JFrame launchFrame = new JFrame("CRMS Launcher");
            launchFrame.setBounds(500, 300, 300, 150);
            launchFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            launchFrame.setLayout(new FlowLayout());

            JLabel prompt = new JLabel("Select Interface:");
            launchFrame.add(prompt);


            JButton adminButton = new JButton("Admin Panel");
            adminButton.addActionListener(e -> new AdminControlPanel());
            launchFrame.add(adminButton);

            launchFrame.setVisible(true);
        });
    }
}