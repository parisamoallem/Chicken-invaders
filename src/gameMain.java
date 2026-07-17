import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.*;

public class gameMain extends JFrame{

    private CardLayout cardLayout;
    private JPanel mainPanel;
    private GamePanel gamePanel;

    private User currentUser;
    private MainMenu menuPanel;

    public gameMain(){
        //پنجره اصلی
        setTitle("Chicken Invaders - AP Project");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        //سیستم کارت و پنل ها
        cardLayout = new CardLayout();
        mainPanel = new JPanel(cardLayout);

        LoginPanel loginPanel = new LoginPanel(this);
        MainMenu menuPanel = new MainMenu(this);

        JPanel highScoresPanel = new JPanel();
        highScoresPanel.setBackground(Color.GREEN);

        JPanel settingsPanel = new JPanel();
        settingsPanel.setBackground(Color.ORANGE);

        JPanel howToPlayPanel = new JPanel();
        howToPlayPanel.setBackground(Color.PINK);

        mainPanel.add(gamePanel, "Game");
        mainPanel.add(loginPanel, "Login");
        mainPanel.add(menuPanel, "Menu");
        mainPanel.add(highScoresPanel, "HighScores");
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

    public void startNewGame() {
        if (currentUser == null) {
            changePanel("Login");
            return;
        }

        // اگر بازی قبلی در جریان بوده، پنل قدیمی را حذف می‌کنیم تا ریست شود
        if (gamePanel != null) {
            mainPanel.remove(gamePanel);
        }

        // ساخت پنل بازی جدید با مشخصات کاربر جاری
        gamePanel = new GamePanel(this, User currentUser);
        mainPanel.add(gamePanel, "Game");

        changePanel("Game");

        // فوکوس کیبورد روی بازی و شروع حلقه تایمر بازی
        gamePanel.requestFocusInWindow();
        gamePanel.startGame();
    }

    // گتر و ستر برای کاربر فعلی برنامه
    public User getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        if (user != null && menuPanel != null) {
            // تغییر لیبل منوی اصلی به خوش‌آمدگویی با نام کاربر (مثلاً: خوش آمدی p!)
            menuPanel.setLoggedInLabel(user.getUsername());
        }
    }


    public static void main(String[] args) {
        // ۱. ابتدا دیتابیس را راه‌اندازی می‌کنیم تا جدول ساخته شود
        DatabaseManager.initDatabase();

        System.out.println("--- تست ثبت‌نام ---");
        boolean registerResult = DatabaseManager.registerUser("p", "1");

        // ۴. اجرای برنامه گرافیکی اصلی (کد قبلی خودت)
        javax.swing.SwingUtilities.invokeLater(() -> {
            new gameMain().setVisible(true);
        });
    }


}
