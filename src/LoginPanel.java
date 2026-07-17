import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginPanel extends JPanel{

    public LoginPanel(gameMain mainFrame){
        setLayout(null);
        setBackground(Color.darkGray);

        JLabel titleLabel = new JLabel("ورود به بازی" , SwingConstants.CENTER);
        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        titleLabel.setBounds(300, 50, 200, 40);
        add(titleLabel);

        JLabel userLabel = new JLabel("نام کاربری");
        userLabel.setForeground(Color.WHITE);
        userLabel.setBounds(200, 150, 100, 30);
        add(userLabel);

        JTextField userField = new JTextField();
        userField.setBounds(320, 150, 200, 30);
        add(userField);

        JLabel passLabel = new JLabel("رمز عبور:");
        passLabel.setForeground(Color.WHITE);
        passLabel.setBounds(200, 210, 100, 30);
        add(passLabel);

        JPasswordField passField = new JPasswordField();
        passField.setBounds(320, 210, 200, 30);
        add(passField);

        JButton loginButton = new JButton("ورود");
        loginButton.setBounds(320, 280, 90, 35);
        add(loginButton);

        JButton goToRegisterButton = new JButton("ثبت نام");
        goToRegisterButton.setBounds(430, 280, 90, 35);
        add(goToRegisterButton);

        //دکمه ها  ------------------

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String username = userField.getText();
                String password = new String(passField.getPassword());

                if (username.isEmpty() || password.isEmpty()){
                    JOptionPane.showMessageDialog(LoginPanel.this, "خطا", "لطفا تمامی فیلد ها را پر کنید.", JOptionPane.WARNING_MESSAGE);
                    return;
                }

                User loggedInUser = DatabaseManager.loginUser(username, password);

                if (loggedInUser != null){
                    JOptionPane.showMessageDialog(LoginPanel.this, "ورود با موفقیت انجام شد :)");

                    mainFrame.setCurrentUser(loggedInUser);
                    mainFrame.changePanel("Menu");
                } else {
                    JOptionPane.showMessageDialog(LoginPanel.this, "نام کاربری یا رمز عبور اشتباه است :(", "خطا", JOptionPane.WARNING_MESSAGE);
                }
            }
        });

        goToRegisterButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mainFrame.changePanel("Register");
            }
        });
    }
}
