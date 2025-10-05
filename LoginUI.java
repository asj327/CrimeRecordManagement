import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.SwingConstants;
import javax.swing.*;            
import java.awt.event.*;        // ActionListener, ActionEvent
import java.sql.ResultSet;
import java.sql.SQLException;


public class LoginUI extends JFrame {
    private JTextField usernameField;
    private JPasswordField passwordField;
    private JButton loginButton;
    private JLabel messageLabel;

    public LoginUI() {
        setTitle("Login Module");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);


        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(173, 216, 230)); // light blue
        add(mainPanel);



        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 15, 15, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;


        JLabel heading = new JLabel("Welcome to Crime Record Management System", SwingConstants.CENTER);
        heading.setFont(new Font("Segoe UI", Font.BOLD, 28));
        heading.setForeground(new Color(0, 0, 128));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        mainPanel.add(heading, gbc);


        JLabel userLabel = new JLabel("Username:");
        userLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        usernameField = new JTextField(18);
        usernameField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridwidth = 1;
        gbc.gridy = 1;
        gbc.gridx = 0;
        mainPanel.add(userLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(usernameField, gbc);

        // Password label & field
        JLabel passLabel = new JLabel("Password:");
        passLabel.setFont(new Font("Segoe UI", Font.PLAIN, 20));
        passwordField = new JPasswordField(18);
        passwordField.setFont(new Font("Segoe UI", Font.PLAIN, 18));
        gbc.gridy = 2;
        gbc.gridx = 0;
        mainPanel.add(passLabel, gbc);
        gbc.gridx = 1;
        mainPanel.add(passwordField, gbc);


        loginButton = new JButton("Login");
        loginButton.setFont(new Font("Segoe UI", Font.BOLD, 20));
        loginButton.setBackground(new Color(0, 102, 204));
        loginButton.setForeground(Color.WHITE);
        loginButton.setFocusPainted(false);
        gbc.gridy = 3;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        mainPanel.add(loginButton, gbc);


        messageLabel = new JLabel("", SwingConstants.CENTER);
        messageLabel.setFont(new Font("Segoe UI", Font.ITALIC, 16));
        messageLabel.setForeground(Color.RED);
        gbc.gridy = 4;
        mainPanel.add(messageLabel, gbc);


        loginButton.addActionListener(e -> checkLogin());
    }

    private void checkLogin() {
        String user = usernameField.getText();
        String pass = new String(passwordField.getPassword());

        if (user.equals("admin") && pass.equals("admin123")) {
            messageLabel.setText("✅ Login Successful!");
            messageLabel.setForeground(new Color(0, 128, 0));

            SwingUtilities.invokeLater(() -> {
                new DashboardUI().setVisible(true);
                dispose();
            });

        } else {
            messageLabel.setText("❌ Invalid Credentials");
            messageLabel.setForeground(Color.RED);
        }
    }


    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new LoginUI().setVisible(true));
    }
}
class DashboardUI extends JFrame implements ActionListener {

    JButton insertBtn, deleteBtn, updateBtn, displayBtn, searchBtn;
    public DashboardUI() {
        setTitle("Dashboard - Crime Record Management System");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setLocationRelativeTo(null);

        JPanel panel = new JPanel(new GridLayout(1, 5, 5, 10));
        panel.setBackground(new Color(250, 249, 249));
        panel.setBorder(BorderFactory.createEmptyBorder(300, 100, 300, 100));


        // Buttons
         insertBtn = createButton("Insert Record", "icons/add.png");
         deleteBtn = createButton("Delete Record", "icons/delete.png");
         updateBtn = createButton("Update Record", "icons/update.png");
         displayBtn = createButton("Display Records", "icons/display.png");
         searchBtn = createButton("Search Record", "icons/search.png");

        panel.add(insertBtn);
        panel.add(deleteBtn);
        panel.add(updateBtn);
        panel.add(displayBtn);
        panel.add(searchBtn);


       add(panel);
        insertBtn.addActionListener(e -> {
            new CrimeEntryUI().setVisible(true);
        });

        displayBtn.addActionListener(e ->{
            new DisplayRecordsUI().setVisible(true);
        });


        deleteBtn.addActionListener(this);
        updateBtn.addActionListener(this);
        searchBtn.addActionListener(this);
        displayBtn.addActionListener(this);

        deleteBtn.addActionListener(this);


    }
        @Override
        public void actionPerformed (ActionEvent e){


            if (e.getSource() == deleteBtn) {
                openFrame1();
            }

            else if (e.getSource()== updateBtn){
                openFrame2();
            }
            else if (e.getSource()== searchBtn) {
                openFrame3();
            }


        }



    void openFrame1() {

        JFrame frame1 = new JFrame("Frame 1");
        frame1.setSize(600, 400);
        frame1.setLocationRelativeTo(null);
        frame1.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame1.setExtendedState(MAXIMIZED_BOTH);


        JPanel p = new JPanel(new GridBagLayout());
        p.setBackground(new Color(232, 237, 243));
        frame1.add(p);

        JTextField id;
        JButton Delete;

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel heading = new JLabel("Enter Details", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 32));
        heading.setForeground(new Color(0, 70, 140));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        p.add(heading, gbc);


        gbc.gridwidth = 1;
        gbc.gridy++;

        gbc.gridx = 0;
        gbc.gridy++;
        p.add(new JLabel("Case ID:"), gbc);
        id = new JTextField(24);
        id.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridx = 1;
        p.add(id, gbc);

        gbc.gridx = 0;
        gbc.gridy++;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel();
        Delete = new JButton("Delete");
        Delete.setFont(new Font("Arial", Font.BOLD, 20));
        buttonPanel.add(Delete);
        p.add(buttonPanel, gbc);
        Delete.addActionListener(e -> {
            String caseId = id.getText().trim();
            if(caseId.isEmpty()) {
                JOptionPane.showMessageDialog(frame1, "Please enter Case ID!");
                return;
            }
            boolean deleted = CrimeDatabaseUtil.deleteCase(caseId);
            if(deleted) {
                JOptionPane.showMessageDialog(frame1, "Deleted Successfully!");
            } else {
                JOptionPane.showMessageDialog(frame1, "Case ID not found!");
            }
        });


        frame1.setVisible(true);
    }

    void openFrame2() {

        JFrame frame2 = new JFrame();
        frame2.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame2.setSize(600, 400);
        frame2.setLocationRelativeTo(null);
        frame2.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel q = new JPanel(new GridBagLayout());
        q.setBackground(new Color(232, 237, 243));
        frame2.add(q);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel heading = new JLabel("Update case status", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 32));
        heading.setForeground(new Color(0, 70, 140));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        q.add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        q.add(new JLabel("Case ID:"), gbc);

        JTextField id = new JTextField(24);
        id.setFont(new Font("Arial", Font.PLAIN, 24));
        gbc.gridx = 1;
        q.add(id, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        JLabel statusLabel = new JLabel("Status:");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        q.add(statusLabel, gbc);

        String[] crimes = {"under investigation", "case closed", "dispute"};
        JComboBox<String> status = new JComboBox<>(crimes);
        status.setEditable(true);
        status.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        q.add(status, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel();
        JButton Update = new JButton("update");
        Update.setFont(new Font("Arial", Font.BOLD, 20));
        buttonPanel.add(Update);
        q.add(buttonPanel, gbc);

        Update.addActionListener(e -> {
            String caseId = id.getText().trim();
            String newStatus = (String) status.getSelectedItem();
            if(caseId.isEmpty() || newStatus.isEmpty()) {
                JOptionPane.showMessageDialog(frame2, "Please fill all fields!");
                return;
            }
            boolean updated = CrimeDatabaseUtil.updateCaseStatus(caseId, newStatus);
            if(updated) {
                JOptionPane.showMessageDialog(frame2, "Updated Successfully!");
            } else {
                JOptionPane.showMessageDialog(frame2, "Case ID not found!");
            }
        });


        frame2.setVisible(true);
    }

    void openFrame3() {
        JFrame frame3 = new JFrame();
        frame3.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame3.setSize(600, 400);
        frame3.setLocationRelativeTo(null);
        frame3.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel r = new JPanel(new GridBagLayout());
        r.setBackground(new Color(232, 237, 243));
        frame3.add(r);

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(15, 10, 15, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.WEST;

        JLabel heading = new JLabel("Search case", SwingConstants.CENTER);
        heading.setFont(new Font("Arial", Font.BOLD, 32));
        heading.setForeground(new Color(0, 70, 140));
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 2;
        r.add(heading, gbc);

        gbc.gridwidth = 1;
        gbc.gridy++;
        gbc.gridx = 0;
        JLabel statusLabel = new JLabel("Search by:");
        statusLabel.setFont(new Font("Arial", Font.PLAIN, 18));
        r.add(statusLabel, gbc);

        String[] search = {"Select", "Case ID", "Name"};
        JComboBox<String> label = new JComboBox<>(search);
        label.setFont(new Font("Arial", Font.PLAIN, 16));
        gbc.gridx = 1;
        r.add(label, gbc);

        gbc.gridy++;
        gbc.gridx = 0;
        gbc.gridwidth = 2;
        gbc.anchor = GridBagConstraints.CENTER;
        JPanel buttonPanel = new JPanel();
        JButton submit = new JButton("SUBMIT");
        submit.setFont(new Font("Arial", Font.BOLD, 20));
        buttonPanel.add(submit);
        r.add(buttonPanel, gbc);

        label.addActionListener(e -> {
            Object selected = label.getSelectedItem();
            r.removeAll();

            gbc.gridx = 0;
            gbc.gridy = 0;
            gbc.gridwidth = 2;
            r.add(heading, gbc);

            gbc.gridwidth = 1;
            gbc.gridy = 1;
            gbc.gridx = 0;
            r.add(statusLabel, gbc);
            gbc.gridx = 1;
            r.add(label, gbc);

            if ("Case ID".equals(selected)) {
                JLabel l1 = new JLabel("Case ID:");
                l1.setFont(new Font("Arial", Font.PLAIN, 18));
                gbc.gridy = 2;
                gbc.gridx = 0;
                r.add(l1, gbc);
                JTextField t1 = new JTextField(24);
                t1.setFont(new Font("Arial", Font.PLAIN, 24));
                gbc.gridx = 1;
                r.add(t1, gbc);
            } else if ("Name".equals(selected)) {
                JLabel l2 = new JLabel("Name:");
                l2.setFont(new Font("Arial", Font.PLAIN, 18));
                gbc.gridy = 2;
                gbc.gridx = 0;
                r.add(l2, gbc);
                JTextField t2 = new JTextField(24);
                t2.setFont(new Font("Arial", Font.PLAIN, 24));
                gbc.gridx = 1;
                r.add(t2, gbc);
            }

            gbc.gridy = 3;
            gbc.gridx = 0;
            gbc.gridwidth = 2;
            gbc.anchor = GridBagConstraints.CENTER;
            JPanel bp = new JPanel();
            bp.add(submit);
            r.add(bp, gbc);

            r.revalidate();
            r.repaint();
        });




        submit.addActionListener(e -> {
            Object selected = label.getSelectedItem();
            String input = ""; // get input from the dynamic JTextField you added
            ResultSet rs = null;

            try {
                if("Case ID".equals(selected)) {
                    rs = CrimeDatabaseUtil.searchCaseById(input);
                } else if("Name".equals(selected)) {
                    rs = CrimeDatabaseUtil.searchCaseByName(input);
                }

                if(rs != null && rs.next()) {
                    // Display details (for simplicity using JOptionPane)
                    JOptionPane.showMessageDialog(frame3, "Case Found: " +
                            "\nID: " + rs.getString("case_id") +
                            "\nName: " + rs.getString("name") +
                            "\nStatus: " + rs.getString("status"));
                } else {
                    JOptionPane.showMessageDialog(frame3, "No record found!");
                }
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        });




        frame3.setVisible(true);
    }


    private JButton createButton(String text, String iconPath) {
        JButton button = new JButton(text);
        button.setFont(new Font("Segoe UI", Font.BOLD, 18));
        button.setFocusPainted(false);
        button.setBackground(new Color(0, 102, 204));
        button.setForeground(Color.WHITE);

        try {
            ImageIcon icon = new ImageIcon(iconPath);
            Image img = icon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH);
            button.setIcon(new ImageIcon(img));
            button.setHorizontalTextPosition(SwingConstants.CENTER);
            button.setVerticalTextPosition(SwingConstants.BOTTOM);
        } catch (Exception e) {
            System.out.println("Icon not found: " + iconPath);
        }

        return button;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DashboardUI().setVisible(true));
        SwingUtilities.invokeLater(() -> new CrimeEntryUI().setVisible(true));
    }
}