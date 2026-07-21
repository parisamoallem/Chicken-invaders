import javax.swing.*;
import java.awt.*;

public class HowToPlayPanel extends JPanel {

    public HowToPlayPanel(gameMain mainFrame) {
        setLayout(null);
        setBackground(new Color(30, 30, 50));

        JLabel titleLabel = new JLabel("HOW TO PLAY", SwingConstants.CENTER);
        titleLabel.setForeground(new Color(255, 215, 0));
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 36));
        titleLabel.setBounds(200, 30, 400, 50);
        add(titleLabel);

        String guide = "<html>"
                + "<body style='font-family: Segoe UI, sans-serif; color: #FFFFFF; text-align: center;'>"
                + "  <div style='margin-bottom: 12px; font-size: 14px;'>"
                + "    <span style='font-size: 18px;'>🚀</span> <b>Move Ship:</b> Use <span style='color: #00FFFF;'>LEFT / RIGHT</span> Arrow Keys"
                + "  </div>"
                + "  <div style='margin-bottom: 12px; font-size: 14px;'>"
                + "    <span style='font-size: 18px;'>💥</span> <b>Fire Weapon:</b> Press <span style='color: #00FFFF;'>SPACEBAR</span> to Shoot"
                + "  </div>"
                + "  <div style='margin-bottom: 12px; font-size: 14px;'>"
                + "    <span style='font-size: 18px;'>🐔</span> <b>Score Points:</b> Defeat Chickens &amp; Mighty Bosses"
                + "  </div>"
                + "  <div style='margin-bottom: 12px; font-size: 14px;'>"
                + "    <span style='font-size: 18px;'>🥚</span> <b>Dodge Hazards:</b> Avoid Falling Eggs &amp; Enemy Lasers"
                + "  </div>"
                + "  <div style='margin-bottom: 5px; font-size: 14px;'>"
                + "    <span style='font-size: 18px;'>❤️</span> <b>Survival:</b> Don't Lose All Your Lives!"
                + "  </div>"
                + "</body>"
                + "</html>";

        //ایموجی ها از جمینای گرفته شدن برای زیباتر کردن پنل :)

        JTextPane guidePane = new JTextPane();
        guidePane.setContentType("text/html");
        guidePane.setText(guide);
        guidePane.setEditable(false);
        guidePane.setOpaque(false);

        JPanel cardPanel = new JPanel(new BorderLayout());
        cardPanel.setBackground(new Color(45, 45, 70, 200));
        cardPanel.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(80, 80, 120), 2, true),
                BorderFactory.createEmptyBorder(20, 20, 20, 20)
        ));
        cardPanel.setBounds(150, 100, 500, 310);
        cardPanel.add(guidePane, BorderLayout.CENTER);

        add(cardPanel);

        JButton backButton = new JButton("Back to Menu");
        backButton.setFont(new Font("Consolas", Font.BOLD, 14));
        backButton.setFocusPainted(false);
        backButton.setBounds(325, 440, 150, 40);
        add(backButton);

        backButton.addActionListener(e -> mainFrame.changePanel("Menu"));
    }
}