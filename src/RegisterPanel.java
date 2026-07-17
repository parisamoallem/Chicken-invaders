import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class RegisterPanel extends JPanel {

    public RegisterPanel(gameMain mainFrame){
        setLayout(null);
        setBackground(Color.darkGray);

        JLabel titleLabel = new JLabel("ثبت نام کاربر جدید", SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(300, 50, 200, 40);
        add(titleLabel);

        JLabel userLabel = new JLabel("نام کاربری:");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(200, 130, 100, 30);
        add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(320, 130, 200, 30);
        add(userField);

        JLabel passLabel = new JLabel("رمز عبور:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(200, 190, 100, 30);
        add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(320, 190, 200, 30);
        add(passField);

        JLabel confirmLabel = new JLabel("تکرار رمز:");
        confirmLabel.setForeground(Color.WHITE);
        confirmLabel.setBounds(200, 250, 100, 30);
        add(confirmLabel);

        JPasswordField confirmField = new JPasswordField();
        confirmField.setBounds(320, 250, 200, 30);
        add(confirmField);

        JButton registerButton = new JButton("ثبت نام");
        registerButton.setBounds(320, 310, 90, 35);
        add(registerButton);

        JButton backButton = new JButton("بازگشت");
        backButton.setBounds(430, 310, 90, 35);
        add(backButton);

        //منطق دکمه‌ها -----------------

        registerButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText().trim();
                String password = new String(passField.getPassword());
                String confirm = new String(confirmField.getPassword());

                if (username.isEmpty() || password.isEmpty() || confirm.isEmpty()) {
                    JOptionPane.showMessageDialog(RegisterPanel.this,
                            "لطفاً همه فیلدها را پر کنید.", "خطا", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                if (!password.equals(confirm)) {
                    JOptionPane.showMessageDialog(RegisterPanel.this,
                            "رمز عبور و تکرار آن یکسان نیستند ._. ", "خطا", JOptionPane.WARNING_MESSAGE);
                    return;
                }
                boolean success = DatabaseManager.registerUser(username, password);

                if (success) {
                    JOptionPane.showMessageDialog(RegisterPanel.this,
                            "ثبت ‌نام با موفقیت انجام شد. اکنون وارد شوید.");

                    // خالی کردن فیلدهامون
                    userField.setText("");
                    passField.setText("");
                    confirmField.setText("");

                    // صفحه لاگین
                    mainFrame.changePanel("Login");
                } else {
                    JOptionPane.showMessageDialog(RegisterPanel.this,
                            "این نام کاربری قبلاً ثبت شده است!", "خطا", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        backButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.changePanel("Login");
            }
        });
    }
}
