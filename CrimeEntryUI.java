import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;


public class CrimeEntryUI extends JFrame {
    private JTextField caseIdField, crimeTypeField, locationField, dateField;
    private JTextArea descriptionArea;
    private JButton saveBtn, cancelBtn;

    public CrimeEntryUI() {
        setTitle("Crime Entry");
        setSize(600, 500);
        setExtendedState(MAXIMIZED_BOTH);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);


        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(232, 237, 243));
        add(mainPanel);




        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel heading = new JLabel("Enter Crime Details", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 32));
        heading.setForeground(new Color(0, 70, 140));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(heading, gbc);


        gbc.gridwidth = 1;
        gbc.gridy++;


        mainPanel.add(new JLabel("Case ID:"), gbc);
        caseIdField = new JTextField(24);
        caseIdField.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridx = 1;
        mainPanel.add(caseIdField, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Crime Type:"), gbc);
        crimeTypeField = new JTextField(24);
        crimeTypeField.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridx = 1;
        mainPanel.add(crimeTypeField, gbc);


        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Location:"), gbc);
        locationField = new JTextField(24);
        locationField.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridx = 1;
        mainPanel.add(locationField, gbc);


        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Date:"), gbc);
        dateField = new JTextField(24);
        dateField.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridx = 1;
        mainPanel.add(dateField, gbc);


        gbc.gridx = 0;
        gbc.gridy++;
        mainPanel.add(new JLabel("Status:"), gbc);
        descriptionArea = new JTextArea(4, 2);
        descriptionArea.setFont(new Font("Arial", Font.PLAIN, 20));
        JScrollPane scrollPane = new JScrollPane(descriptionArea);
        gbc.gridx = 1;
        mainPanel.add(scrollPane, gbc);


        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel();
        saveBtn = new JButton("Save");
        cancelBtn = new JButton("Cancel");
        saveBtn.setFont(new Font("Arial", Font.BOLD, 20));
        cancelBtn.setFont(new Font("Arial", Font.BOLD, 20));
        buttonPanel.add(saveBtn);
        buttonPanel.add(cancelBtn);
        mainPanel.add(buttonPanel, gbc);


        saveBtn.addActionListener(e -> {
            String caseId = caseIdField.getText().trim();
            String crimeType = crimeTypeField.getText().trim();
            String location = locationField.getText().trim();
            String date = dateField.getText().trim();
            String status = descriptionArea.getText().trim();

            if(caseId.isEmpty() || crimeType.isEmpty() || location.isEmpty() || date.isEmpty() || status.isEmpty()) {
                JOptionPane.showMessageDialog(this, "Please fill all fields!");
                return;
            }

            boolean saved = CrimeDatabaseUtil.addCase(caseId, crimeType, status);
            // If your DB has fields for location and date, modify addCase method to accept them

            if(saved) {
                JOptionPane.showMessageDialog(this, "Crime record saved successfully!");
                // Optional: clear fields after saving
                caseIdField.setText("");
                crimeTypeField.setText("");
                locationField.setText("");
                dateField.setText("");
                descriptionArea.setText("");
            } else {
                JOptionPane.showMessageDialog(this, "Failed to save! Case ID may already exist.");
            }
        });


    }



}
