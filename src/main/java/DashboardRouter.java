import javax.swing.*;
import java.awt.*;

/**
 * DashboardRouter is responsible for checking the authenticated user's role
 * and launching the corresponding dashboard UI. The placeholder UIs are
 * defined internally as private nested classes.
 */
public class DashboardRouter {

    /**
     * Examines the user's role and routes the application flow.
     * * @param username The authenticated user's username.
     * @param userRole The role string of the authenticated user (e.g., "ADMIN", "OFFICER").
     */
    public static void routeToDashboard(String username, String userRole) {
        // Ensure the role is uppercase for reliable checking
        String role = userRole.toUpperCase();

        // --- START ROLE-BASED ROUTING LOGIC ---

        if (role.equals("ADMIN")) {
            // PATH 1: Admin Dashboard
            new AdminPagePlaceholder(username, role).setVisible(true);

            // TODO FOR ADMIN TEAM: Replace 'new AdminPagePlaceholder(...)' with the final Admin Dashboard class

        } else if (role.equals("OFFICER") || role.equals("USER")) {
            // PATH 2: Officer/User Dashboard
            new OfficerPagePlaceholder(username, role).setVisible(true);

            // TODO FOR OFFICER TEAM: Replace 'new OfficerPagePlaceholder(...)' with the final Officer Dashboard class

        } else {
            JOptionPane.showMessageDialog(null, "Login successful, but role is unrecognized: " + role, "Routing Error", JOptionPane.ERROR_MESSAGE);
        }
        // --- END ROLE-BASED ROUTING LOGIC ---
    }

    // =================================================================
    // INBUILT PLACEHOLDER WINDOWS temporary
    // =================================================================

    /**
     * Internal Placeholder UI for the ADMIN Dashboard.
     */
    private static class AdminPagePlaceholder extends JFrame {
        public AdminPagePlaceholder(String username, String userRole) {
            setTitle("CRMS Dashboard - " + userRole + " (ADMIN VIEW)");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(800, 500);
            setLocationRelativeTo(null);
            setLayout(new FlowLayout(FlowLayout.CENTER, 30, 30));

            JLabel welcomeLabel = new JLabel("Welcome, Administrative User: " + username);
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 22));
            welcomeLabel.setForeground(Color.BLUE.darker());

            JLabel roleLabel = new JLabel("Access Level: " + userRole + " - Full System Control");
            roleLabel.setFont(new Font("Arial", Font.ITALIC, 16));

            add(welcomeLabel);
            add(roleLabel);
            add(new JLabel("--- This is the dedicated ADMIN PAGE placeholder (Inbuilt in Router). ---"));
        }
    }

    /**
     * Internal Placeholder UI for the OFFICER/USER Dashboard.
     */
    private static class OfficerPagePlaceholder extends JFrame {
        public OfficerPagePlaceholder(String username, String userRole) {
            setTitle("CRMS Dashboard - " + userRole + " (OFFICER VIEW)");
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setSize(700, 450);
            setLocationRelativeTo(null);
            setLayout(new FlowLayout(FlowLayout.CENTER, 25, 25));

            JLabel welcomeLabel = new JLabel("Welcome, Officer: " + username);
            welcomeLabel.setFont(new Font("Arial", Font.BOLD, 20));
            welcomeLabel.setForeground(Color.RED.darker());

            JLabel roleLabel = new JLabel("Access Level: " + userRole + " - Standard Operations");
            roleLabel.setFont(new Font("Arial", Font.ITALIC, 15));

            add(welcomeLabel);
            add(roleLabel);
            add(new JLabel("--- This is the dedicated OFFICER/USER PAGE placeholder (Inbuilt in Router). ---"));
        }
    }
}
