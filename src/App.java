import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import javax.swing.table.DefaultTableModel;


public class App {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginUI().setVisible(true);
        });
    }
}

class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public LoginUI() {
        setTitle("Crime Record Management System - Login");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 400);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(173, 216, 230));
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Crime Record Management System", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 24));
        heading.setForeground(new Color(0, 0, 128));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Username:"), gbc);

        usernameField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Password:"), gbc);

        passwordField = new JPasswordField(20);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);

        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 16));
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(loginButton, gbc);

        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setForeground(Color.RED);
        gbc.gridy = 4;
        mainPanel.add(messageLabel, gbc);

        loginButton.addActionListener(e -> checkLogin());

        // Allow Enter key to login
        getRootPane().setDefaultButton(loginButton);
    }

    private void checkLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (user.equals("admin") && pass.equals("admin123")) {
            messageLabel.setText("Login Successful!");
            messageLabel.setForeground(new Color(0, 128, 0));
            SwingUtilities.invokeLater(() -> {
                new DashboardUI().setVisible(true);
                dispose();
            });
        } else {
            messageLabel.setText("Invalid Credentials");
            messageLabel.setForeground(Color.RED);
        }
    }
}

class DashboardUI extends JFrame {
    public DashboardUI() {
        setTitle("Crime Record Management - Dashboard");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(2, 3, 20, 20));
        panel.setBackground(new Color(240, 248, 255));
        panel.setBorder(BorderFactory.createEmptyBorder(100, 100, 100, 100));

        JButton insertBtn = createButton("Insert Record");
        JButton deleteBtn = createButton("Delete Record");
        JButton updateBtn = createButton("Update Record");
        JButton displayBtn = createButton("Display Records");
        JButton searchBtn = createButton("Search Record");
        JButton exitBtn = createButton("Exit");

        panel.add(insertBtn);
        panel.add(deleteBtn);
        panel.add(updateBtn);
        panel.add(displayBtn);
        panel.add(searchBtn);
        panel.add(exitBtn);

        add(panel);

        // Action listeners
        insertBtn.addActionListener(e -> new CrimeEntryUI().setVisible(true));
        deleteBtn.addActionListener(e -> new DeleteRecordUI().setVisible(true));
        updateBtn.addActionListener(e -> new UpdateRecordUI().setVisible(true));
        displayBtn.addActionListener(e -> new DisplayRecordsUI().setVisible(true));
        searchBtn.addActionListener(e -> new SearchRecordUI().setVisible(true));
        exitBtn.addActionListener(e -> System.exit(0));
    }

    private JButton createButton(String text) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 16));
        button.setFocusPainted(false);
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(200, 80));
        return button;
    }
}

class CrimeEntryUI extends JFrame {
    private JTextField caseIdField, crimeTypeField, locationField, dateField;
    private JTextArea descriptionArea;
    private CrimeRecordDAO crimeDAO;

    public CrimeEntryUI() {
        crimeDAO = new CrimeRecordDAO();
        setTitle("Add New Crime Record");
        setSize(600, 500);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(240, 248, 255));
        add(mainPanel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        JLabel heading = new JLabel("Enter Crime Details", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 24));
        heading.setForeground(new Color(0, 70, 140));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(heading, gbc);

        gbc.gridwidth = 1;

        // Case ID
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Case ID:"), gbc);
        caseIdField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(caseIdField, gbc);

        // Crime Type
        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Crime Type:"), gbc);
        crimeTypeField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(crimeTypeField, gbc);

        // Location
        gbc.gridy = 3;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Location:"), gbc);
        locationField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(locationField, gbc);

        // Date
        gbc.gridy = 4;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Date (dd-MM-yyyy):"), gbc);
        dateField = new JTextField(20);
        gbc.gridx = 1;
        mainPanel.add(dateField, gbc);

        // Description
        gbc.gridy = 5;
        gbc.gridx = 0;
        mainPanel.add(new JLabel("Description:"), gbc);
        descriptionArea = new JTextArea(4, 20);
        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 1;
        mainPanel.add(scrollPane, gbc);

        // Buttons
        gbc.gridy = 6;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel();
        JButton saveBtn = new JButton("Save");
        JButton cancelBtn = new JButton("Cancel");
        saveBtn.setFont(new Font("Arial", Font.BOLD, 14));
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 14));
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        mainPanel.add(buttonPanel, gbc);

        // Action listeners
        saveBtn.addActionListener(e -> saveCrimeRecord());
        cancelBtn.addActionListener(e -> dispose());
    }

    private void saveCrimeRecord() {
        try {
            String caseId = caseIdField.getText().trim();
            String crimeType = crimeTypeField.getText().trim();
            String location = locationField.getText().trim();
            String dateStr = dateField.getText().trim();
            String description = descriptionArea.getText().trim();

            if (caseId.isEmpty() || crimeType.isEmpty() || location.isEmpty() || dateStr.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all required fields!", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Parse date
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
            Date crimeDate = sdf.parse(dateStr);

            CrimeRecord crime = new CrimeRecord(caseId, crimeType, location, crimeDate, description, "Open");

            if (crimeDAO.insertCrimeRecord(crime)) {
                JOptionPane.showMessageDialog(this, "Crime record added successfully!");
                clearFields();
            } else {
                JOptionPane.showMessageDialog(this, "Failed to add record!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Please enter date in dd-MM-yyyy format!", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void clearFields() {
        caseIdField.setText("");
        crimeTypeField.setText("");
        locationField.setText("");
        dateField.setText("");
        descriptionArea.setText("");
    }
}


class DisplayRecordsUI extends JFrame {

    public DisplayRecordsUI() {
        setTitle("Display Crime Records");
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Table model
        DefaultTableModel model = new DefaultTableModel(
                new String[]{"Case ID", "Crime Type", "Date", "Location", "Status"}, 0);
        JTable table = new JTable(model);
        add(new JScrollPane(table), BorderLayout.CENTER);

        try {

            CrimeRecordDAO dao = new CrimeRecordDAO();
            List<CrimeRecord> records = dao.getAllCrimeRecords();

            // Populate the table
            for (CrimeRecord crime : records) {
                model.addRow(new Object[]{
                        crime.getCaseId(),
                        crime.getCrimeType(),
                        crime.getDate(),
                        crime.getLocation(),
                        crime.getStatus()
                });
            }

        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Error fetching data!");
        }
    }
}



class SearchRecordUI extends JFrame {
    public SearchRecordUI() {
        setTitle("Search Crime Record");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBackground(new Color(240, 248, 255));
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 0;
        gbc.gridx = 0;
        panel.add(new JLabel("Case ID:"), gbc);

        JTextField caseIdField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(caseIdField, gbc);

        JButton searchBtn = new JButton("Search");
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(searchBtn, gbc);

        CrimeRecordDAO dao = new CrimeRecordDAO();
        searchBtn.addActionListener(e -> {
            String caseId = caseIdField.getText().trim();
            if (caseId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Case ID!");
                return;
            }

            CrimeRecord record = dao.searchCrimeRecord(caseId);
            if (record != null) {
                StringBuilder sb = new StringBuilder();
                sb.append("Record Found:\n\n");
                sb.append("Case ID: ").append(record.getCaseId()).append("\n");
                sb.append("Crime Type: ").append(record.getCrimeType()).append("\n");
                sb.append("Location: ").append(record.getLocation()).append("\n");
                sb.append("Date: ").append(record.getDate()).append("\n");
                sb.append("Status: ").append(record.getStatus()).append("\n");
                sb.append("Description: ").append(record.getDescription()).append("\n");

                JTextArea resultArea = new JTextArea(sb.toString(), 10, 40);
                resultArea.setEditable(false);
                JScrollPane scrollPane = new JScrollPane(resultArea);
                JOptionPane.showMessageDialog(this, scrollPane, "Search Result", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(this, "No record found with Case ID: " + caseId, "Not Found", JOptionPane.WARNING_MESSAGE);
            }
        });
    }
}

class DeleteRecordUI extends JFrame {
    public DeleteRecordUI() {
        setTitle("Delete Crime Record");
        setSize(400, 200);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 0;
        gbc.gridx = 0;
        panel.add(new JLabel("Case ID:"), gbc);

        JTextField caseIdField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(caseIdField, gbc);

        JButton deleteBtn = new JButton("Delete");
        gbc.gridy = 1;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(deleteBtn, gbc);

        CrimeRecordDAO dao = new CrimeRecordDAO();
        deleteBtn.addActionListener(e -> {
            String caseId = caseIdField.getText().trim();
            if (caseId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Case ID!");
                return;
            }

            int result = JOptionPane.showConfirmDialog(this,
                    "Are you sure you want to delete record with Case ID: " + caseId + "?",
                    "Confirm Delete", JOptionPane.YES_NO_OPTION);

            if (result == JOptionPane.YES_OPTION) {
                if (dao.deleteCrimeRecord(caseId)) {
                    JOptionPane.showMessageDialog(this, "Record deleted successfully!");
                    caseIdField.setText("");
                } else {
                    JOptionPane.showMessageDialog(this, "Record not found or deletion failed!");
                }
            }
        });
    }
}

class UpdateRecordUI extends JFrame {
    public UpdateRecordUI() {
        setTitle("Update Crime Record Status");
        setSize(500, 300);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        add(panel);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridy = 0;
        gbc.gridx = 0;
        panel.add(new JLabel("Case ID:"), gbc);

        JTextField caseIdField = new JTextField(20);
        gbc.gridx = 1;
        panel.add(caseIdField, gbc);

        gbc.gridy = 1;
        gbc.gridx = 0;
        panel.add(new JLabel("New Status:"), gbc);

        String[] statusOptions = {"Open", "Under Investigation", "Closed", "Pending"};
        JComboBox<String> statusCombo = new JComboBox<>(statusOptions);
        gbc.gridx = 1;
        panel.add(statusCombo, gbc);

        JButton updateBtn = new JButton("Update");
        gbc.gridy = 2;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        panel.add(updateBtn, gbc);

        CrimeRecordDAO dao = new CrimeRecordDAO();
        updateBtn.addActionListener(e -> {
            String caseId = caseIdField.getText().trim();
            String newStatus = (String) statusCombo.getSelectedItem();

            if (caseId.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please enter Case ID!");
                return;
            }

            if (dao.updateCrimeRecord(caseId, newStatus)) {
                JOptionPane.showMessageDialog(this, "Record updated successfully!");
                caseIdField.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Record not found or update failed!");
            }
        });
    }
}