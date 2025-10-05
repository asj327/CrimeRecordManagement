import java.sql.*;

public class CrimeDatabaseUtil {

    private static final String URL = "jdbc:mysql://localhost:3306/crimeDB";
    private static final String USER = "root";
    private static final String PASSWORD = "password";


    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }


    public static boolean deleteCase(String caseId) {
        String sql = "DELETE FROM cases WHERE case_id = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, caseId);
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static boolean updateCaseStatus(String caseId, String status) {
        String sql = "UPDATE cases SET status = ? WHERE case_id = ?";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, status);
            pst.setString(2, caseId);
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }


    public static ResultSet searchCaseById(String caseId) {
        String sql = "SELECT * FROM cases WHERE case_id = ?";
        try {
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, caseId);
            return pst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static ResultSet searchCaseByName(String name) {
        String sql = "SELECT * FROM cases WHERE name LIKE ?";
        try {
            Connection con = getConnection();
            PreparedStatement pst = con.prepareStatement(sql);
            pst.setString(1, "%" + name + "%");
            return pst.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }


    public static boolean addCase(String caseId, String name, String status) {
        String sql = "INSERT INTO cases(case_id, name, status) VALUES (?, ?, ?)";
        try (Connection con = getConnection();
             PreparedStatement pst = con.prepareStatement(sql)) {
            pst.setString(1, caseId);
            pst.setString(2, name);
            pst.setString(3, status);
            int rows = pst.executeUpdate();
            return rows > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
}
