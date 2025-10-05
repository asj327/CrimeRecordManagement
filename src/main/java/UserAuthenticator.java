
import java.sql.*;
import org.mindrot.jbcrypt.BCrypt;

public class UserAuthenticator {

    // Final database name, matching your setup
    private static final String DB_URL = "jdbc:mysql://localhost:3306/crimeRecords?useSSL=false&serverTimezone=UTC";

    // Instance variables to hold credentials passed from the main application
    private final String dbUser;
    private final String dbPass;

    /**
     * Constructor: Initializes the Authenticator with the necessary
     * MySQL credentials (which are provided by the user in the login UI).
     */
    public UserAuthenticator(String dbUser, String dbPass) {
        this.dbUser = dbUser;
        this.dbPass = dbPass;
    }

    /**
     * Checks credentials against the database using HASHED passwords and checks access.
     *
     * @param username The username entered by the user.
     * @param password The raw password entered by the user.
     * @return UserData object if authenticated and active, null otherwise.
     */
    public UserData authenticateUser(String username, String password) {
        // We retrieve both the hash AND the is_active status in one query.
        String sql = "SELECT password_hash, user_role, is_active FROM users WHERE username = ?";

        try (Connection conn = DriverManager.getConnection(DB_URL, this.dbUser, this.dbPass);
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, username);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password_hash");
                    boolean isActive = rs.getBoolean("is_active");
                    String userRole = rs.getString("user_role");

                    // 1. Check if the password matches the hash
                    boolean passwordMatches = BCrypt.checkpw(password, storedHash);

                    // 2. Check if the user is active
                    if (passwordMatches && isActive) {
                        // Success! Return the user data
                        return new UserData(username, userRole);
                    }
                }
            }

        } catch (SQLException ex) {
            System.err.println("Authentication Database Error: " + ex.getMessage());
        }
        return null; // Login failed or user is inactive
    }

    public String hashPassword(String plainPassword) {
        // Generate a salt and hash the password securely
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt());
    }

    /**
     * Public helper class to safely return authenticated user data (username and role).
     */
    public static class UserData {
        private final String username;
        private final String userRole;

        public UserData(String username, String userRole) {
            this.username = username;
            this.userRole = userRole;
        }

        public String getUsername() {
            return username;
        }

        public String getUserRole() {
            return userRole;
        }
    }
}