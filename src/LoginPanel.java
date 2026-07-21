import javax.swing.*;
import java.awt.*;

public class LoginPanel extends JPanel {

    public LoginPanel(gameMain mainFrame) {
        setLayout(null);
        setBackground(new Color(30, 30, 50));

        JLabel titleLabel = new JLabel("CHICKEN INVADERS", SwingConstants.CENTER);
        titleLabel.setForeground(new Color(255, 215, 0)); // Gold Yellow
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 34));
        titleLabel.setBounds(200, 30, 400, 45);
        add(titleLabel);

        JPanel cardPanel = new JPanel(null);
        cardPanel.setBackground(new Color(45, 45, 70));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 120), 2, true),
                BorderFactory.createEmptyBorder(15, 15, 15, 15)
        ));
        cardPanel.setBounds(200, 95, 400, 420);

        JLabel subTitle = new JLabel("PLAYER LOGIN", SwingConstants.CENTER);
        subTitle.setForeground(new Color(0, 255, 255)); // Cyan Accent
        subTitle.setFont(new Font("Consolas", Font.BOLD, 20));
        subTitle.setBounds(50, 20, 300, 30);
        cardPanel.add(subTitle);

        // یوزرنیم
        JLabel userLabel = new JLabel("Username:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        userLabel.setBounds(40, 70, 320, 25);
        cardPanel.add(userLabel);

        JTextField userField = createStyledTextField();
        userField.setBounds(40, 100, 320, 38);
        cardPanel.add(userField);

        // پسورد
        JLabel passLabel = new JLabel("Password:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setFont(new Font("Consolas", Font.BOLD, 14));
        passLabel.setBounds(40, 150, 320, 25);
        cardPanel.add(passLabel);

        JPasswordField passField = createStyledPasswordField();
        passField.setBounds(40, 180, 320, 38);
        cardPanel.add(passField);

        // لاگین
        JButton loginButton = new JButton("LOGIN");
        loginButton.setFont(new Font("Consolas", Font.BOLD, 15));
        loginButton.setFocusPainted(false);
        loginButton.setBounds(40, 245, 320, 42);
        cardPanel.add(loginButton);

        // ساختن اکانت
        JLabel noAccountLabel = new JLabel("Don't have an account?", SwingConstants.CENTER);
        noAccountLabel.setForeground(new Color(180, 180, 210));
        noAccountLabel.setFont(new Font("Consolas", Font.PLAIN, 12));
        noAccountLabel.setBounds(40, 305, 320, 20);
        cardPanel.add(noAccountLabel);

        JButton goToRegisterButton = new JButton("Create New Account");
        goToRegisterButton.setFont(new Font("Consolas", Font.BOLD, 13));
        goToRegisterButton.setFocusPainted(false);
        goToRegisterButton.setBounds(80, 335, 240, 35);
        cardPanel.add(goToRegisterButton);

        add(cardPanel);

        // اکشن هاشون
        loginButton.addActionListener(e -> {
            String username = userField.getText().trim();
            String password = new String(passField.getPassword());

            if (username.isEmpty() || password.isEmpty()) {
                showCustomDialog("Please fill in all fields!", "Warning", JOptionPane.WARNING_MESSAGE);
                return;
            }

            User loggedInUser = DatabaseManager.loginUser(username, password);

            if (loggedInUser != null) {
                userField.setText("");
                passField.setText("");
                mainFrame.setCurrentUser(loggedInUser);
                mainFrame.changePanel("Menu");
            } else {
                showCustomDialog("Invalid username or password!", "Login Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        goToRegisterButton.addActionListener(e -> {
            userField.setText("");
            passField.setText("");
            mainFrame.changePanel("Register");
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