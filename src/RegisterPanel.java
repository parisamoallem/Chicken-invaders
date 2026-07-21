import javax.swing.*;
import java.awt.*;

public class RegisterPanel extends JPanel {

    public RegisterPanel(gameMain mainFrame) {
        setLayout(null);
        setBackground(new Color(30, 30, 50));

        JLabel titleLabel = new JLabel("CHICKEN INVADERS", SwingConstants.CENTER);
        titleLabel.setForeground(new Color(255, 215, 0)); // Gold Yellow
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 34));
        titleLabel.setBounds(200, 20, 400, 45);
        add(titleLabel);

        JPanel cardPanel = new JPanel(null);
        cardPanel.setBackground(new Color(45, 45, 70));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 120), 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setBounds(200, 75, 400, 465);


        JLabel subTitle = new JLabel("CREATE ACCOUNT", SwingConstants.CENTER);
        subTitle.setForeground(new Color(0, 255, 255)); // Cyan Accent
        subTitle.setFont(new Font("Consolas", Font.BOLD, 20));
        subTitle.setBounds(50, 15, 300, 30);
        cardPanel.add(subTitle);

        // یوزنیم
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Consolas", Font.BOLD, 13));
        userLabel.setBounds(40, 55, 320, 22);
        cardPanel.add(userLabel);

        JTextField userField = createStyledTextField();
        userField.setBounds(40, 80, 320, 35);
        cardPanel.add(userField);

        // پسورد
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Consolas", Font.BOLD, 13));
        passLabel.setBounds(40, 125, 320, 22);
        cardPanel.add(passLabel);

        JPasswordField passField = createStyledPasswordField();
        passField.setBounds(40, 150, 320, 35);
        cardPanel.add(passField);

        // باز چک کردن پسورد
        JLabel confirmLabel = new JLabel("Confirm Password:");
        confirmLabel.setForeground(Color.WHITE);
        confirmLabel.setFont(new Font("Consolas", Font.BOLD, 13));
        confirmLabel.setBounds(40, 195, 320, 22);
        cardPanel.add(confirmLabel);

        JPasswordField confirmField = createStyledPasswordField();
        confirmField.setBounds(40, 220, 320, 35);
        cardPanel.add(confirmField);

        // رجیستر
        JButton registerButton = new JButton("REGISTER");
        registerButton.setFont(new Font("Consolas", Font.BOLD, 14));
        registerButton.setFocusPainted(false);
        registerButton.setBounds(40, 280, 320, 40);
        cardPanel.add(registerButton);

        // برگشتن به قسمت لاگین
        JLabel hasAccountLabel = new JLabel("Already registered?", SwingConstants.CENTER);
        hasAccountLabel.setForeground(new Color(180, 180, 210));
        hasAccountLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
        hasAccountLabel.setBounds(40, 335, 320, 20);
        cardPanel.add(hasAccountLabel);

        JButton backButton = new JButton("Back to Login");
        backButton.setFont(new Font("Consolas", Font.BOLD, 13));
        backButton.setFocusPainted(false);
        backButton.setBounds(80, 365, 240, 35);
        cardPanel.add(backButton);

        add(cardPanel);

        // اکشن هاشون
        registerButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());
            String confirm = new String(confirmField.getPassword());

            if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                showCustomDialog("Please fill in all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            if (!password.equals(confirm)) {
                showCustomDialog("Passwords do not match!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            boolean success = DatabaseManager.registerUser(username, password);

            if (success) {
                showCustomDialog("Account created successfully! Please login.", "Success", JOptionPane.INFORMATION_MESSAGE);

                userField.setText("");
                passField.setText("");
                confirmField.setText("");

                mainFrame.changePanel("Login");
            } else {
                showCustomDialog("This username is already taken!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        backButton.addActionListener(e -> {
            userField.setText("");
            passField.setText("");
            confirmField.setText("");
            mainFrame.changePanel("Login");
        });
    }

    private JTextField createStyledTextField() {
        JTextField field = new JTextField();
        field.setFont(new Font("Consolas", Font.PLAIN, 14));
        field.setBackground(new Color(25, 25, 40));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.YELLOW);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 100), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private JPasswordField createStyledPasswordField() {
        JPasswordField field = new JPasswordField();
        field.setFont(new Font("Consolas", Font.PLAIN, 14));
        field.setBackground(new Color(25, 25, 40));
        field.setForeground(Color.WHITE);
        field.setCaretColor(Color.YELLOW);
        field.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(70, 70, 100), 1),
                BorderFactory.createEmptyBorder(5, 10, 5, 10)
        ));
        return field;
    }

    private void showCustomDialog(String message, String title, int messageType) {
        UIManager.put("OptionPane.background", new Color(30, 30, 50));
        UIManager.put("Panel.background", new Color(30, 30, 50));
        UIManager.put("OptionPane.messageForeground", Color.WHITE);
        UIManager.put("OptionPane.messageFont", new Font("Consolas", Font.BOLD, 14));
        JOptionPane.showMessageDialog(this, message, title, messageType);
    }
}