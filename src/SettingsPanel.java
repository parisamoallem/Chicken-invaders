import javax.swing.*;
import java.awt.*;

public class SettingsPanel extends JPanel {

    private gameMain mainFrame;

    private JCheckBox bgmBox;
    private JCheckBox sfxShotBox;
    private JCheckBox sfxCrashBox;
    private JCheckBox sfxGameOverBox;

    public SettingsPanel(gameMain mainFrame) {
        this.mainFrame = mainFrame;
        setLayout(null);
        setBackground(new Color(30, 30, 50));

        JLabel titleLabel = new JLabel("SETTINGS", SwingConstants.CENTER);
        titleLabel.setForeground(new Color(255, 215, 0)); // Gold Yellow
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 36));
        titleLabel.setBounds(200, 35, 400, 50);
        add(titleLabel);

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(null);
        cardPanel.setBackground(new Color(45, 45, 70));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 120), 2, true),
                BorderFactory.createEmptyBorder(10, 10, 10, 10)
        ));
        cardPanel.setBounds(220, 110, 360, 280);

        //   فاصله کافی تا باکس تیک داشته باشن
        bgmBox = createStyledCheckBox("        Background Music");
        sfxShotBox = createStyledCheckBox("        Laser Shot Sound");
        sfxCrashBox = createStyledCheckBox("        Explosion Sound");
        sfxGameOverBox = createStyledCheckBox("        Game Over Sound");

        bgmBox.setBounds(40, 35, 280, 30);
        sfxShotBox.setBounds(40, 85, 280, 30);
        sfxCrashBox.setBounds(40, 135, 280, 30);
        sfxGameOverBox.setBounds(40, 185, 280, 30);

        cardPanel.add(bgmBox);
        cardPanel.add(sfxShotBox);
        cardPanel.add(sfxCrashBox);
        cardPanel.add(sfxGameOverBox);

        add(cardPanel);

        JButton saveButton = new JButton("Save");
        saveButton.setFont(new Font("Consolas", Font.BOLD, 14));
        saveButton.setFocusPainted(false);
        saveButton.setBounds(270, 420, 120, 38);

        JButton backButton = new JButton("Back");
        backButton.setFont(new Font("Consolas", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.setBounds(410, 420, 120, 38);

        add(saveButton);
        add(backButton);

        saveButton.addActionListener(e -> {
            User user = mainFrame.getCurrentUser();
            if (user == null) {
                mainFrame.changePanel("Menu");
                return;
            }
            user.setBgmOn(bgmBox.isSelected());
            user.setSfxShotOn(sfxShotBox.isSelected());
            user.setSfxCrashOn(sfxCrashBox.isSelected());
            user.setSfxGameOverOn(sfxGameOverBox.isSelected());

            DatabaseManager.updateUser(user);
            SoundManager.setThemeOn(user.isBgmOn());

            JOptionPane.showMessageDialog(
                    this,
                    "Settings updated successfully!",
                    "Success",
                    JOptionPane.INFORMATION_MESSAGE
            );
            mainFrame.changePanel("Menu");
        });

        backButton.addActionListener(e -> mainFrame.changePanel("Menu"));
    }

    private JCheckBox createStyledCheckBox(String text) {
        JCheckBox box = new JCheckBox(text);
        box.setForeground(Color.WHITE);
        box.setBackground(new Color(45, 45, 70));
        box.setFont(new Font("Consolas", Font.PLAIN, 15));
        box.setFocusPainted(false);
        return box;
    }

    public void refresh() {
        User user = mainFrame.getCurrentUser();
        if (user == null) return;
        bgmBox.setSelected(user.isBgmOn());
        sfxShotBox.setSelected(user.isSfxShotOn());
        sfxCrashBox.setSelected(user.isSfxCrashOn());
        sfxGameOverBox.setSelected(user.isSfxGameOverOn());
    }
}