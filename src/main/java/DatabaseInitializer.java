import javax.swing.*;
import java.awt.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

    /**
     * Initializes the MySQL database for the Crime Management System (CRMS).
     * This program uses a GUI prompt to ask for MySQL root credentials
     * before attempting to create the 'crimeRecords' database and its tables.
     */
    public class DatabaseInitializer {

        // IMPORTANT: Connection URLs remain static, but credentials are provided at runtime.
        private static final String DB_URL_ROOT = "jdbc:mysql://localhost:3306/?useSSL=false&serverTimezone=UTC";
        private static final String DB_URL_CRMS = "jdbc:mysql://localhost:3306/crimeRecords?useSSL=false&serverTimezone=UTC";

        // Credentials will be stored here after successful input from the GUI
        private static String mysqlUser = "";
        private static String mysqlPass = "";

        public static void main(String[] args) {
            SwingUtilities.invokeLater(() -> {
                // Step 1: Get credentials from the user via GUI
                if (getCredentialsFromUser()) {
                    // Step 2: Create the database
                    if (createDatabase()) {
                        // Step 3: Create the tables within the database
                        createTables();
                    }
                } else {
                    System.out.println("❌ Database initialization cancelled by user.");
                }
            });
        }

        /**
         * Shows a GUI dialog to securely capture the MySQL username and password.
         * @return true if credentials were provided, false otherwise (e.g., if user cancels).
         */
        private static boolean getCredentialsFromUser() {
            // Use a JPanel to combine the username and password fields
            JPanel panel = new JPanel(new GridLayout(0, 1));

            JTextField userField = new JTextField("root"); // Pre-fill with default root username
            JPasswordField passField = new JPasswordField();

            panel.add(new JLabel("MySQL Username (e.g., root):"));
            panel.add(userField);
            panel.add(new JLabel("MySQL Password:"));
            panel.add(passField);

            int result = JOptionPane.showConfirmDialog(null, panel,
                    "Enter MySQL Root Credentials", JOptionPane.OK_CANCEL_OPTION,
                    JOptionPane.PLAIN_MESSAGE);

            if (result == JOptionPane.OK_OPTION) {
                mysqlUser = userField.getText();
                mysqlPass = new String(passField.getPassword());
                return true;
            }
            return false;
        }

        /**
         * Connects to the root MySQL server and executes the CREATE DATABASE command.
         * Uses the credentials provided by the user.
         */
        private static boolean createDatabase() {
            System.out.println("Attempting to connect to MySQL server to create 'crimeRecords'...");
            try (Connection conn = DriverManager.getConnection(DB_URL_ROOT, mysqlUser, mysqlPass);
                 Statement stmt = conn.createStatement()) {

                // SQL to create the database if it doesn't exist
                String sql = "CREATE DATABASE IF NOT EXISTS crimeRecords";
                stmt.executeUpdate(sql);
                System.out.println("✅ Database 'crimeRecords' is ready.");
                return true;

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,
                        "Failed to connect to MySQL server or create database.\nError: " + ex.getMessage() +
                                "\nPlease ensure the server is running and your credentials are correct.",
                        "Database Connection Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("❌ Failed to connect to MySQL or create database. Error: " + ex.getMessage());
                return false;
            }
        }

        /**
         * Connects to the new 'crimeRecords' database and creates the 'users' and 'records' tables.
         * Uses the credentials provided by the user.
         */
        private static void createTables() {
            System.out.println("Connecting to 'crimeRecords' to initialize tables...");
            try (Connection conn = DriverManager.getConnection(DB_URL_CRMS, mysqlUser, mysqlPass);
                 Statement stmt = conn.createStatement()) {

                // 1. CREATE 'users' table (Focus on security and access control)
                String createUsersTable = """
                CREATE TABLE IF NOT EXISTS users (
                    id INT AUTO_INCREMENT PRIMARY KEY,
                    username VARCHAR(50) NOT NULL UNIQUE,
                    password_hash VARCHAR(60) NOT NULL,
                    user_role VARCHAR(20) NOT NULL DEFAULT 'OFFICER',
                    is_active BOOLEAN NOT NULL DEFAULT TRUE,
                    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
                )
                """;
                stmt.executeUpdate(createUsersTable);
                System.out.println("✅ Table 'users' created/verified.");

                // 2. CREATE 'records' table (Placeholder for future crime data)
                String createRecordsTable = """
                CREATE TABLE IF NOT EXISTS records (
                    record_id INT AUTO_INCREMENT PRIMARY KEY,
                    crime_type VARCHAR(100),
                    location VARCHAR(255),
                    date_reported DATE,
                    status VARCHAR(50) DEFAULT 'Open',
                    officer_id INT,
                    FOREIGN KEY (officer_id) REFERENCES users(id)
                )
                """;
                stmt.executeUpdate(createRecordsTable);
                System.out.println("✅ Table 'records' created/verified.");

            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(null,
                        "Failed to create tables in crimeRecords database.\nError: " + ex.getMessage(),
                        "Table Creation Error", JOptionPane.ERROR_MESSAGE);
                System.err.println("❌ Failed to create tables. Error: " + ex.getMessage());
            }
        }
    }


