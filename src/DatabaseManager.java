import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    private static final String URL = "jdbc:sqlite:C:/Users/fariba/IdeaProjects/finalProject/game.db";

    private static Connection connect() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            System.out.println("درایور پیدا نشد: " + e.getMessage());
        }
        return DriverManager.getConnection(URL);
    }

    public static void initDatabase(){
        String userSql = "CREATE TABLE IF NOT EXISTS users ("
                + " username TEXT PRIMARY KEY,"
                + " password TEXT NOT NULL,"
                + " highscore INTEGER DEFAULT 0,"
                + " last_level INTEGER DEFAULT 1,"
                + " bgm_on INTEGER DEFAULT 1,"
                + " sfx_shot_on INTEGER DEFAULT 1,"
                + " sfx_crash_on INTEGER DEFAULT 1,"
                + " sfx_gameover_on INTEGER DEFAULT 1"
                + ");";

        String gameSql = "CREATE TABLE IF NOT EXISTS games ("
                + " id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + " username TEXT,"
                + " score INTEGER,"
                + " level_reached INTEGER,"
                + " play_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,"
                + " sound_settings TEXT,"
                + " FOREIGN KEY(username) REFERENCES users(username)"
                + " );";

        try(Connection conn = connect();
        Statement stmt = conn.createStatement()){
            stmt.execute(userSql);
            stmt.execute(gameSql);
            System.out.println("دیتابیس با موفقیت راه‌اندازی شد.");
        } catch (SQLException e) {
            System.out.println("خطا در ساخت جدول دیتابیس: " + e.getMessage());
        }
    }

    public static boolean registerUser(String username, String password){
        String sql = "INSERT INTO users(username, password) VALUES(?, ?)";

        try(Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.executeUpdate();
            System.out.println("کاربر جدید با موفقیت ثبت‌نام شد.");
            return true;
        } catch (SQLException e){
            System.out.println("خطا در ثبت‌نام (احتمالاً نام کاربری تکراری است): " + e.getMessage());
            return false;
        }
    }

    public static User loginUser(String username, String password){
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";

        try(Connection conn = connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)){
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            ResultSet rs = pstmt.executeQuery();

            if(rs.next()){
                User user = new User(rs.getString("username"), rs.getString("password"));
                user.setHighScore(rs.getInt("highscore"));
                user.setLastLevel(rs.getInt("last_level"));
                user.setBgmOn(rs.getInt("bgm_on") == 1);
                user.setSfxCrashOn(rs.getInt("sfx_crash_on") == 1);
                user.setSfxShotOn(rs.getInt("sfx_shot_on") == 1);
                user.setSfxGameOverOn(rs.getInt("sfx_gameover_on") == 1);

                System.out.println("ورود موفقیت‌آمیز بود! خوش آمدید " + username);
                return user;
            }
        } catch (SQLException e){
            System.out.println("خطا در ورود به برنامه: " + e.getMessage());
        }
        System.out.println("نام کاربری یا رمز عبور اشتباه است.");
        return null;
    }

    public static void updateUser(User user){
        String sql = "UPDATE users SET highscore = ?, last_level = ?, bgm_on = ?,"
                   + "sfx_shot_on = ?, sfx_crash_on = ?, sfx_gameover_on = ? WHERE username = ?";
        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, user.getHighScore());
            pstmt.setInt(2, user.getLastLevel());
            pstmt.setInt(3, user.isBgmOn() ? 1 : 0);
            pstmt.setInt(4, user.isSfxShotOn() ? 1 : 0);
            pstmt.setInt(5, user.isSfxCrashOn() ? 1 : 0);
            pstmt.setInt(6, user.isSfxGameOverOn() ? 1 : 0);
            pstmt.setString(7, user.getUsername());
            pstmt.executeUpdate();
            System.out.println("اطلاعات کاربر در دیتابیس به‌روزرسانی شد.");
        } catch (SQLException e) {
            System.out.println("خطا در به‌روزرسانی کاربر: " + e.getMessage());
        }
    }

    public static void saveGameResult(User user, int score, int levelReached){
        String sql = "INSERT INTO games(username, score, level_reached, sound_settings) VALUES(?, ?, ?, ?)";

        String soundSummary = "M:" + user.isBgmOn() + ",S:" + user.isSfxShotOn() + ",C" + user.isSfxCrashOn() + ",E:" + user.isSfxGameOverOn();

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, user.getUsername());
            pstmt.setInt(2, score);
            pstmt.setInt(3, levelReached);
            pstmt.setString(4, soundSummary);
            pstmt.executeUpdate();
            System.out.println("نتیجه بازی با موفقیت ثبت شد.");
        } catch (SQLException e) {
            System.out.println("خطا در ثبت نتیجه بازی: " + e.getMessage());
        }

        if (score > user.getHighScore())
            user.setHighScore(score);
        if (levelReached > user.getLastLevel())
            user.setLastLevel(levelReached);

        updateUser(user);
    }

    //جدول High Scores: به ازای هر کاربر فقط بالاترین امتیازش
    public static List<String[]> getTopScores(int limit) {
        List<String[]> rows = new ArrayList<>();
        String sql = "SELECT username, MAX(score) AS score, level_reached, play_time "
                   + "FROM games GROUP BY username ORDER BY score DESC LIMIT ?";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, limit);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                rows.add(new String[]{
                        rs.getString("username"),
                        String.valueOf(rs.getInt("score")),
                        String.valueOf(rs.getInt("level_reached")),
                        rs.getString("play_time")
                });
            }
        } catch (SQLException e) {
            System.out.println("خطا در خواندن جدول امتیازات: " + e.getMessage());
        }
        return rows;
    }
}

