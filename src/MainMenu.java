import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenu extends JPanel{

    private String username;

    public MainMenu(gameMain mainFrame){
        setLayout(null);
        setBackground(new Color(30, 30, 50));

        JLabel titleLabel = new JLabel("CHICKEN INVADERS", SwingConstants.CENTER);
        titleLabel.setForeground(Color.YELLOW);
        titleLabel.setFont(new Font("Consolas", Font.BOLD, 36));
        titleLabel.setBounds(200, 50, 400, 50);
        add(titleLabel);

        JButton btnNewGame = new JButton("New Game");
        JButton btnHighScores = new JButton("High Scores");
        JButton btnSettings = new JButton("Settings");
        JButton btnHowToPlay = new JButton("How To Play");
        JButton btnExit = new JButton("Exit");

        btnNewGame.setBounds(300, 160, 200, 40);
        btnHighScores.setBounds(300, 220, 200, 40);
        btnSettings.setBounds(300, 280, 200, 40);
        btnHowToPlay.setBounds(300, 340, 200, 40);
        btnExit.setBounds(300, 400, 200, 40);

        add(btnNewGame);
        add(btnHighScores);
        add(btnSettings);
        add(btnHowToPlay);
        add(btnExit);

        //-------------------------------

        btnNewGame.addActionListener(e -> {
            mainFrame.changePanel("Game");
            mainFrame.getGamePanel().startGame();
        });

        btnHighScores.addActionListener(e -> {
            mainFrame.getHighScorePanel().refresh();
            mainFrame.changePanel("HighScores");
        });

        btnSettings.addActionListener(e -> {
            mainFrame.getSettingsPanel().refresh();
            mainFrame.changePanel("Settings");
        });

        btnHowToPlay.addActionListener(e -> mainFrame.changePanel("HowToPlay"));


        btnExit.addActionListener(e -> {
            UIManager.put("OptionPane.background", new Color(30, 30, 50));
            UIManager.put("Panel.background", new Color(30, 30, 50));
            UIManager.put("OptionPane.messageForeground", Color.WHITE);
            UIManager.put("OptionPane.messageFont", new Font("Consolas", Font.BOLD, 15));

            Object[] options = {"YES", "NO"};

            int confirm = JOptionPane.showOptionDialog(
                    this,
                    "Are you sure you want to exit?",
                    "Exit Game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    options,
                    options[1]
            );
            if (confirm == 0) {
                System.exit(0);
            }
        });
    }

    public void setLoggedInLabel(String username) {
        this.username = username;
    }
}
