import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;

public class gameMain extends JFrame {
    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;

    private User currentUser;
    private MainMenu menuPanel;
    private SettingsPanel settingsPanel;
    private HighScorePanel highScorePanel;

    public gameMain(){
        setTitle("Chicken Invaders - AP Project");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        gamePanel = new GamePanel(this);

        LoginPanel loginPanel = new LoginPanel(this);
        RegisterPanel registerPanel = new RegisterPanel(this);
        menuPanel = new MainMenu(this);

        highScorePanel = new HighScorePanel(this);

        settingsPanel = new SettingsPanel(this);

        HowToPlayPanel howToPlayPanel = new HowToPlayPanel(this);

        mainPanel.add(gamePanel, "Game");
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(registerPanel, "Register");
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(highScorePanel, "HighScores");
        mainPanel.add(settingsPanel, "Settings");
        mainPanel.add(howToPlayPanel, "HowToPlay");

        add(mainPanel);
        cardLayout.show(mainPanel, "Login");
    }

    public void changePanel(String panelName){
        cardLayout.show(mainPanel, panelName);   //متد که بقیه کلاسا هم بتونن پنل رو جابجا کنن
    }

    public GamePanel getGamePanel() {
        return gamePanel;
    }

    public HighScorePanel getHighScorePanel() {
        return highScorePanel;
    }

    public SettingsPanel getSettingsPanel() {
        return settingsPanel;
    }

    public void startNewGame() {
        if (currentUser == null) {
            changePanel("Login");
            return;
        }

        if (gamePanel != null) {
            mainPanel.remove(gamePanel);
        }

        gamePanel = new GamePanel(this);
        mainPanel.add(gamePanel, "Game");

        changePanel("Game");

        gamePanel.requestFocusInWindow();
        gamePanel.startGame();
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null && menuPanel != null) {
            menuPanel.setLoggedInLabel(user.getUsername());

            SoundManager.setThemeOn(user.isBgmOn());
        }
    }

    public static void main(String[] args) {
        DatabaseManager.initDatabase();

        System.out.println("--- تست ثبت‌نام ---");
        boolean registerResult = DatabaseManager.registerUser("p", "1");

        javax.swing.SwingUtilities.invokeLater(() -> {
            new gameMain().setVisible(true);
        });
    }
}
