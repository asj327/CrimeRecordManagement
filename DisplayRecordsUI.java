import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class DisplayRecordsUI extends JFrame {
    public DisplayRecordsUI() {
        setTitle("Display Crime Records");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        DefaultTableModel model = new DefaultTableModel(new String[]{"Case ID", " Crime Type", "Date", "Location","Status"}, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        try {
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/crime_db", "root", "password");//name of db
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT * FROM crime_records");//name of db table

            while (rs.next()) {
                model.addRow(new Object[]{
                        rs.getInt("case_id"),
                        rs.getString("crime_type"),//name of db colm
                        rs.getString("crime_date"),
                        rs.getString("location"),
                        rs.getString("status"),
                });
            }

            conn.close();
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data!");
        }
    }
}
