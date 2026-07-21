import Enemy.Boss;
import Enemy.BossLevel4;
import Enemy.BossLevel8;
import Enemy.Cell;
import Enemy.Egg;
import Enemy.Enemy;
import Enemy.EnemyType;
import Enemy.FastEnemy;
import Enemy.NormalEnemy;
import Enemy.ShooterEnemy;
import Enemy.ZigzagEnemy;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;

public class GamePanel extends JPanel implements ActionListener {
    public static final int SCREEN_WIDTH = 800;
    public static final int SCREEN_HEIGHT = 600;

    private gameMain mainFrame;
    private User currentUser;
    private Timer gameTimer;
    private Image backgroundImage;

    private SpaceShip spaceShip;
    private ArrayList<Bullet> bullets = new ArrayList<>();
    private ArrayList<Egg> enemyEggs = new ArrayList<>();
    private ArrayList<PowerUp> powerUps = new ArrayList<>();
    private ArrayList<Explosion> explosions = new ArrayList<>();

    private boolean spacePressed = false;
    private long lastShotTime = 0;
    private final long SHOT_COOLDOWN = 300;
    private final long RAPID_COOLDOWN = 120;

    //شبکه مرغ ها
    private final int ROWS = 5;
    private final int COLS = 8;
    private final int COL_STEP = 88;
    private final int ROW_STEP = 62;
    //پایین ترین جایی که مرغ ها اجازه دارن برسن
    private final int ENEMY_FLOOR_Y = 500;

    private Enemy[][] chickens = new Enemy[ROWS][COLS];
    private Cell[][] cells = new Cell[ROWS][COLS];
    private double formationX, formationY;
    private int gridDir = 1;
    private int stepDir = 1;   // 1 یعنی شبکه پایین میره، -1 یعنی برمیگرده به بالا

    private Boss boss;

    private int level = 1;
    private int score = 0;
    private int lives = 3;
    private int fireCount = 1;   // تیرهای همزمان
    private final int MAX_FIRE = 5;

    private boolean isPaused = false;
    private boolean isGameOver = false;
    private boolean isWin = false;
    private boolean resultSaved = false;

    private long rapidUntil = 0;
    private long shieldUntil = 0;
    private long freezeUntil = 0;

    private long gameStartTime;
    private long lastEggDropTime;
    private long levelBannerUntil = 0;


    public GamePanel(gameMain mainFrame) {
        this.mainFrame = mainFrame;
        this.currentUser = currentUser;
        setLayout(null);
        setBackground(Color.BLACK);
        setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
        setFocusable(true);

        this.backgroundImage = loadImage("resources/background.png");

        int startShipX = (SCREEN_WIDTH / 2) - (SpaceShip.WIDTH / 2);
        int startShipY = SCREEN_HEIGHT - SpaceShip.HEIGHT - 70;
        this.spaceShip = new SpaceShip(startShipX, startShipY);

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                handleKeyEvent(e.getKeyCode(), true);
            }

            @Override
            public void keyReleased(KeyEvent e) {
                handleKeyEvent(e.getKeyCode(), false);
            }
        });

        gameTimer = new Timer(16, this);
    }

    private Image loadImage(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE || icon.getIconWidth() > 0) {
                return icon.getImage();
            }
        } catch (Exception e) {
            System.out.println("خطا در لود تصویر " + path + ": " + e.getMessage());
        }
        return null;
    }

    public void startGame() {
        level = 1;
        score = 0;
        lives = 3;
        fireCount = 1;
        isPaused = false;
        isGameOver = false;
        isWin = false;
        resultSaved = false;
        gameStartTime = System.currentTimeMillis();
        setupLevel(level);
        gameTimer.start();
        requestFocusInWindow();
    }

    public void handleKeyEvent(int keyCode, boolean isPressed) {
        if (isGameOver || isWin) {
            if (keyCode == KeyEvent.VK_ESCAPE && isPressed) {
                gameTimer.stop();
                mainFrame.changePanel("Menu");
            }
            return;
        }

        if (keyCode == KeyEvent.VK_ESCAPE && isPressed) {
            saveResult();
            gameTimer.stop();
            mainFrame.changePanel("Menu");
            return;
        }

        if (keyCode == KeyEvent.VK_P && isPressed) {
            isPaused = !isPaused;
            return;
        }

        if (isPaused) return;

        if (keyCode == KeyEvent.VK_LEFT || keyCode == KeyEvent.VK_A) spaceShip.moveLeft = isPressed;
        if (keyCode == KeyEvent.VK_RIGHT || keyCode == KeyEvent.VK_D) spaceShip.moveRight = isPressed;
        if (keyCode == KeyEvent.VK_UP || keyCode == KeyEvent.VK_W) spaceShip.moveUp = isPressed;
        if (keyCode == KeyEvent.VK_DOWN || keyCode == KeyEvent.VK_S) spaceShip.moveDown = isPressed;
        if (keyCode == KeyEvent.VK_SPACE) spacePressed = isPressed;
    }

    //---------------- ساخت مراحل ----------------

    private void setupLevel(int lvl) {
        bullets.clear();
        enemyEggs.clear();
        powerUps.clear();
        boss = null;
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                chickens[r][c] = null;
                cells[r][c] = null;
            }
        }

        lastEggDropTime = System.currentTimeMillis();
        levelBannerUntil = System.currentTimeMillis() + 1300;

        //مراحل 4 و 8 غول دارن
        if (lvl == 4) {
            boss = new BossLevel4(SCREEN_WIDTH);
            return;
        }
        if (lvl == 8) {
            boss = new BossLevel8(SCREEN_WIDTH);
            return;
        }

        formationX = 60;
        formationY = 70;
        gridDir = 1;
        stepDir = 1;

        EnemyType[] types = typesForLevel(lvl);
        int counter = cellCounterForLevel(lvl);
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                EnemyType type = types[c % types.length];
                Cell cell = new Cell(r, c, type, counter);
                cells[r][c] = cell;
                chickens[r][c] = createEnemy(type, cell, slotX(c), slotY(r), false);
            }
        }
    }

    private Enemy createEnemy(EnemyType type, Cell cell, double x, double y, boolean entering) {
        Enemy e;
        if (type == EnemyType.FAST) e = new FastEnemy(cell, x, y, entering);
        else if (type == EnemyType.ZIGZAG) e = new ZigzagEnemy(cell, x, y, entering);
        else if (type == EnemyType.SHOOTER) e = new ShooterEnemy(cell, x, y, entering);
        else e = new NormalEnemy(cell, x, y, entering);

        e.setHealth(healthFor(type));
        return e;
    }

    private EnemyType[] typesForLevel(int lvl) {
        switch (lvl) {
            case 1: return new EnemyType[]{EnemyType.NORMAL};
            case 2: return new EnemyType[]{EnemyType.NORMAL, EnemyType.FAST};
            case 3: return new EnemyType[]{EnemyType.NORMAL, EnemyType.ZIGZAG};
            case 5: return new EnemyType[]{EnemyType.SHOOTER, EnemyType.FAST};
            case 6: return new EnemyType[]{EnemyType.ZIGZAG, EnemyType.SHOOTER};
            case 7: return new EnemyType[]{EnemyType.NORMAL, EnemyType.FAST, EnemyType.ZIGZAG, EnemyType.SHOOTER};
            default: return new EnemyType[]{EnemyType.NORMAL};
        }
    }

    private int cellCounterForLevel(int lvl) {
        switch (lvl) {
            case 1:
            case 2: return 2;
            case 3:
            case 5: return 3;
            case 6:
            case 7: return 4;
            default: return 2;
        }
    }

    //سرعت افقی شبکه
    private double gridSpeedForLevel(int lvl) {
        switch (lvl) {
            case 1: return 1.0;
            case 2: return 1.5;
            case 3: return 2.0;
            case 5: return 2.5;
            case 6: return 3.0;
            case 7: return 3.5;
            default: return 1.5;
        }
    }

    //گام عمودی موقع برخورد به لبه
    private int stepYForLevel(int lvl) {
        switch (lvl) {
            case 1:
            case 2: return 20;
            case 3:
            case 5: return 25;
            case 6:
            case 7: return 30;
            default: return 20;
        }
    }

    private long eggIntervalForLevel(int lvl) {
        switch (lvl) {
            case 1: return 3000;
            case 2: return 2000;
            case 3: return 1500;
            case 5: return 1000;
            case 6: return 800;
            case 7: return 700;
            default: return 1500;
        }
    }

    private int healthFor(EnemyType type) {
        boolean strong = level >= 5;
        if (type == EnemyType.FAST) return strong ? 2 : 1;
        return strong ? 3 : 2;
    }

    //امتیاز هر نوع دشمن
    private int pointsFor(EnemyType type) {
        if (type == EnemyType.FAST) return 15;
        if (type == EnemyType.ZIGZAG) return 20;
        if (type == EnemyType.SHOOTER) return 25;
        return 10;
    }

    private double slotX(int col) {
        return formationX + col * COL_STEP;
    }

    private double slotY(int row) {
        return formationY + row * ROW_STEP;
    }

    //---------------- حلقه اصلی بازی ----------------

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }

    public void updateGame() {
        if (isGameOver || isWin || isPaused) return;

        long now = System.currentTimeMillis();
        boolean frozen = now < freezeUntil;
        boolean shieldOn = now < shieldUntil;
        double time = (now - gameStartTime) / 1000.0;

        spaceShip.update(SCREEN_WIDTH, SCREEN_HEIGHT);

        //شلیک بازیکن
        if (spacePressed) {
            long cooldown = (now < rapidUntil) ? RAPID_COOLDOWN : SHOT_COOLDOWN;
            if (now - lastShotTime >= cooldown) {
                fireBullets();
                SoundManager.playShot();
                lastShotTime = now;
            }
        }

        //آپدیت گلوله ها
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            b.update();
            if (!b.isAlive()) {
                bullets.remove(i);
                i--;
            }
        }

        if (boss != null) {
            updateBoss(frozen);
        } else {
            updateChickens(frozen, shieldOn, time);
        }

        updateEggs(frozen, shieldOn);
        updatePowerUps();

        //پاک کردن انفجارهای نموم شده
        for (int i = 0; i < explosions.size(); i++) {
            if (!explosions.get(i).isAlive()) {
                explosions.remove(i);
                i--;
            }
        }
    }

    private void fireBullets() {
        int centerX = spaceShip.getX() + SpaceShip.WIDTH / 2;
        int spacing = 16;
        for (int i = 0; i < fireCount; i++) {
            double offset = (i - (fireCount - 1) / 2.0) * spacing;
            bullets.add(new Bullet((int) (centerX - Bullet.WIDTH / 2 + offset), spaceShip.getY()));
        }
    }

    //---------------- مراحل شبکه ای ----------------

    private void updateChickens(boolean frozen, boolean shieldOn, double time) {
        // اولین و آخرین ستون زنده و پایین ترین ردیف زنده
        int minCol = -1, maxCol = -1, maxRow = -1;
        for (int c = 0; c < COLS; c++) {
            for (int r = 0; r < ROWS; r++) {
                Enemy ch = chickens[r][c];
                if (ch != null && ch.isAlive()) {
                    if (minCol == -1) minCol = c;
                    maxCol = c;
                    if (r > maxRow) maxRow = r;
                }
            }
        }

        if (!frozen && minCol != -1) {
            formationX += gridSpeedForLevel(level) * gridDir;
            double leftX = formationX + minCol * COL_STEP;
            double rightX = formationX + maxCol * COL_STEP + 50;

            if (gridDir > 0 && rightX >= SCREEN_WIDTH - 10) {
                gridDir = -1;
                formationY += stepYForLevel(level) * stepDir;
            } else if (gridDir < 0 && leftX <= 10) {
                gridDir = 1;
                formationY += stepYForLevel(level) * stepDir;
            }

            double lowestY = ENEMY_FLOOR_Y - 50 - maxRow * ROW_STEP;
            if (formationY >= lowestY) {
                formationY = lowestY;
                stepDir = -1;
            }
            if (formationY <= 70) {
                formationY = 70;
                stepDir = 1;
            }
        }

        //آپدیت جای هر مرغ + موج ملایم
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Enemy ch = chickens[r][c];
                if (ch == null || !ch.isAlive()) continue;
                ch.setFrozen(frozen);
                double bob = frozen ? 0 : Math.sin(time * 2.5 + c * 0.6) * 4;
                ch.update(slotX(c), slotY(r) + bob, time);
            }
        }

        //برخورد گلوله ها با مرغا
        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            boolean hit = false;
            for (int r = 0; r < ROWS && !hit; r++) {
                for (int c = 0; c < COLS && !hit; c++) {
                    Enemy ch = chickens[r][c];
                    if (ch == null || !ch.isAlive()) continue;
                    Rectangle chRect = new Rectangle(ch.getX(), ch.getY(), ch.getWIDTH(), ch.getHEIGHT());
                    if (b.getBounds().intersects(chRect)) {
                        ch.takeDamage();
                        if (!ch.isAlive()) {
                            chickenDestroyed(r, c, ch, true);
                        }
                        bullets.remove(i);
                        i--;
                        hit = true;
                    }
                }
            }
        }

        Rectangle shipRect = spaceShip.getBounds();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                Enemy ch = chickens[r][c];
                if (ch == null || !ch.isAlive()) continue;
                Rectangle chRect = new Rectangle(ch.getX(), ch.getY(), ch.getWIDTH(), ch.getHEIGHT());
                if (chRect.intersects(shipRect)) {
                    ch.setAlive(false);
                    chickenDestroyed(r, c, ch, false);
                    if (!shieldOn) hitShip();
                    if (isGameOver) return;
                }
            }
        }

        //شلیک افقی مرغای Shooter
        if (!frozen) {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    Enemy ch = chickens[r][c];
                    if (ch == null || !ch.isAlive()) continue;
                    if (ch instanceof ShooterEnemy && ((ShooterEnemy) ch).shouldFireNow()) {
                        double dir = (spaceShip.getX() > ch.getX()) ? 5 : -5;
                        enemyEggs.add(new Egg(ch.getX() + ch.getWIDTH() / 2.0, ch.getY() + ch.getHEIGHT() / 2.0, dir, 0));
                    }
                }
            }

            long now = System.currentTimeMillis();
            if (now - lastEggDropTime >= eggIntervalForLevel(level)) {
                dropRandomEgg();
                lastEggDropTime = now;
            }
        }

        if (isGridCleared()) {
            score += 200;
            advanceLevel();
        }
    }

    private void dropRandomEgg() {
        ArrayList<Enemy> alive = new ArrayList<>();
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (chickens[r][c] != null && chickens[r][c].isAlive()) {
                    alive.add(chickens[r][c]);
                }
            }
        }
        if (alive.isEmpty()) return;
        Enemy ch = alive.get((int) (Math.random() * alive.size()));
        enemyEggs.add(new Egg(ch.getX() + ch.getWIDTH() / 2.0, ch.getY() + ch.getHEIGHT(), 0, 4));
    }

    private void chickenDestroyed(int r, int c, Enemy dead, boolean givePrize) {
        EnemyType type = cells[r][c].type;
        explosions.add(new Explosion(dead.getX() + dead.getWIDTH() / 2.0, dead.getY() + dead.getHEIGHT() / 2.0, 26));
        SoundManager.playExplosion();

        if (givePrize) {
            score += pointsFor(type);
            //پاورآپ. احتمال 20 درصد
            if (Math.random() < 0.20) {
                powerUps.add(new PowerUp(dead.getX() + dead.getWIDTH() / 2.0 - PowerUp.SIZE / 2.0,
                        dead.getY(), PowerUp.randomType()));
            }
        }

        cells[r][c].respawnCount--;
        if (cells[r][c].respawnCount > 0) {
            double fromX = (Math.random() < 0.5) ? 0 : SCREEN_WIDTH - 50;
            chickens[r][c] = createEnemy(type, cells[r][c], fromX, -50, true);
        } else {
            chickens[r][c] = null;
        }
    }

    private boolean isGridCleared() {
        for (int r = 0; r < ROWS; r++) {
            for (int c = 0; c < COLS; c++) {
                if (chickens[r][c] != null && chickens[r][c].isAlive()) return false;
                if (cells[r][c] != null && cells[r][c].respawnCount > 0) return false;
            }
        }
        return true;
    }

    //---------------- مراحل غول ----------------

    private void updateBoss(boolean frozen) {
        if (!frozen) {
            boss.update(SCREEN_WIDTH, SCREEN_HEIGHT);
            boss.tryAttack(enemyEggs);
        }

        for (int i = 0; i < bullets.size(); i++) {
            Bullet b = bullets.get(i);
            if (b.getBounds().intersects(boss.getBounds())) {
                boss.takeDamage(1);
                explosions.add(new Explosion(b.getX(), b.getY(), 14));
                bullets.remove(i);
                i--;

                if (boss.isDead()) {
                    explosions.add(new Explosion(boss.getCenterX(), boss.getCenterY(), 60));
                    SoundManager.playExplosion();
                    score += (level == 8) ? 1000 : 500;
                    advanceLevel();
                    return;
                }
            }
        }
    }

    //---------------- تخم ها و پاورآپ ها ----------------

    private void updateEggs(boolean frozen, boolean shieldOn) {
        for (int i = 0; i < enemyEggs.size(); i++) {
            Egg egg = enemyEggs.get(i);
            if (!frozen) egg.update();

            //خروج از صفحه => حذف بشه
            if (egg.x < -30 || egg.x > SCREEN_WIDTH + 30 || egg.y < -30 || egg.y > SCREEN_HEIGHT + 30) {
                enemyEggs.remove(i);
                i--;
                continue;
            }

            if (egg.getBounds().intersects(spaceShip.getBounds())) {
                enemyEggs.remove(i);
                i--;
                if (!shieldOn) hitShip();
            }
        }
    }

    private void hitShip() {
        lives--;
        explosions.add(new Explosion(spaceShip.getX() + SpaceShip.WIDTH / 2.0,
                spaceShip.getY() + SpaceShip.HEIGHT / 2.0, 34));
        SoundManager.playLifeLost();
        if (lives <= 0) {
            triggerGameOver();
        }
    }

    private void updatePowerUps() {
        for (int i = 0; i < powerUps.size(); i++) {
            PowerUp p = powerUps.get(i);
            p.update();

            if (p.getY() > SCREEN_HEIGHT) {
                powerUps.remove(i);
                i--;
                continue;
            }

            if (p.getBounds().intersects(spaceShip.getBounds())) {
                applyPowerUp(p.getType());
                powerUps.remove(i);
                i--;
            }
        }
    }

    private void applyPowerUp(PowerUp.Type type) {
        long now = System.currentTimeMillis();
        if (type == PowerUp.Type.ADD_FIRE && fireCount < MAX_FIRE) fireCount++;
        if (type == PowerUp.Type.RAPID_FIRE) rapidUntil = now + 8000;
        if (type == PowerUp.Type.EXTRA_LIFE && lives < 5) lives++;
        if (type == PowerUp.Type.SHIELD) shieldUntil = now + 10000;
        if (type == PowerUp.Type.FREEZE) freezeUntil = now + 3000;
    }

    //---------------- پایان مرحله و بازی ----------------

    private void advanceLevel() {
        if (level >= 8) {
            isWin = true;
            gameTimer.stop();
            SoundManager.playGameOver();
            saveResult();
            return;
        }
        level++;
        setupLevel(level);
    }

    private void triggerGameOver() {
        isGameOver = true;
        gameTimer.stop();
        SoundManager.playGameOver();
        saveResult();
    }

    //ذخیره نتیجه بازی توی دیتابیس
    private void saveResult() {
        if (resultSaved || currentUser == null) return;
        DatabaseManager.saveGameResult(currentUser, score, level);
        resultSaved = true;
    }

    //---------------- رسم ----------------

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, SCREEN_WIDTH, SCREEN_HEIGHT, null);
        }

        for (PowerUp p : powerUps) p.draw(g2d);
        for (Egg egg : enemyEggs) egg.draw(g2d);
        for (Bullet b : bullets) b.draw(g2d);

        if (boss != null) {
            boss.draw(g2d);
            boss.drawHealthBar(g2d, SCREEN_WIDTH);
        } else {
            for (int r = 0; r < ROWS; r++) {
                for (int c = 0; c < COLS; c++) {
                    if (chickens[r][c] != null) chickens[r][c].draw(g2d);
                }
            }
        }

        for (Explosion ex : explosions) ex.draw(g2d);

        // سپر دور هواپیما
        if (System.currentTimeMillis() < shieldUntil) {
            g2d.setColor(new Color(70, 150, 255, 90));
            g2d.fillOval(spaceShip.getX() - 8, spaceShip.getY() - 8,
                    SpaceShip.WIDTH + 16, SpaceShip.HEIGHT + 16);
        }
        spaceShip.draw(g2d);

        drawHud(g2d);
        drawMessages(g2d);
    }

    // اطلاعات کنار بازی
    private void drawHud(Graphics2D g2d) {
        long now = System.currentTimeMillis();
        g2d.setColor(Color.WHITE);
        g2d.setFont(new Font("Arial", Font.BOLD, 16));
        g2d.drawString("Score: " + score, 15, 70);
        g2d.drawString("Level: " + level + " / 8", 15, 92);
        g2d.drawString("Lives: " + lives, SCREEN_WIDTH - 100, 70);
        g2d.drawString("Fire: " + fireCount, SCREEN_WIDTH - 100, 92);

        String name = (currentUser != null) ? currentUser.getUsername() : "Guest";
        g2d.setFont(new Font("Arial", Font.PLAIN, 14));
        g2d.drawString("Player: " + name, SCREEN_WIDTH / 2 - 50, 70);

        // پاورآپ های فعال
        int y = 114;
        g2d.setFont(new Font("Arial", Font.BOLD, 13));
        if (now < rapidUntil) {
            g2d.setColor(new Color(255, 80, 80));
            g2d.drawString("RAPID " + ((rapidUntil - now) / 1000 + 1) + "s", 15, y);
            y += 18;
        }
        if (now < shieldUntil) {
            g2d.setColor(new Color(90, 160, 255));
            g2d.drawString("SHIELD " + ((shieldUntil - now) / 1000 + 1) + "s", 15, y);
            y += 18;
        }
        if (now < freezeUntil) {
            g2d.setColor(new Color(0, 210, 230));
            g2d.drawString("FREEZE " + ((freezeUntil - now) / 1000 + 1) + "s", 15, y);
        }
    }

    private void drawMessages(Graphics2D g2d) {
        if (!isGameOver && !isWin && System.currentTimeMillis() < levelBannerUntil) {
            g2d.setColor(Color.YELLOW);
            g2d.setFont(new Font("Consolas", Font.BOLD, 44));
            String s = (level == 4 || level == 8) ? "BOSS - LEVEL " + level : "LEVEL " + level;
            centerString(g2d, s, SCREEN_HEIGHT / 2 - 20);
        }

        if (isPaused) {
            g2d.setColor(new Color(0, 0, 0, 180));
            g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Consolas", Font.BOLD, 48));
            centerString(g2d, "PAUSED", 280);
            g2d.setFont(new Font("Arial", Font.PLAIN, 18));
            centerString(g2d, "Press P to Resume", 330);
        }

        if (isGameOver) {
            g2d.setColor(new Color(0, 0, 0, 220));
            g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            g2d.setColor(Color.RED);
            g2d.setFont(new Font("Consolas", Font.BOLD, 50));
            centerString(g2d, "GAME OVER!", 270);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 18));
            centerString(g2d, "Final Score: " + score + "   |   Level: " + level, 320);
            centerString(g2d, "Press ESC to Return to Main Menu", 355);
        }

        if (isWin) {
            g2d.setColor(new Color(0, 0, 0, 220));
            g2d.fillRect(0, 0, SCREEN_WIDTH, SCREEN_HEIGHT);
            g2d.setColor(Color.GREEN);
            g2d.setFont(new Font("Consolas", Font.BOLD, 50));
            centerString(g2d, "YOU WIN!", 270);
            g2d.setColor(Color.WHITE);
            g2d.setFont(new Font("Arial", Font.PLAIN, 18));
            centerString(g2d, "Final Score: " + score, 320);
            centerString(g2d, "Press ESC to Return to Main Menu", 355);
        }
    }

    private void centerString(Graphics2D g2d, String s, int y) {
        int w = g2d.getFontMetrics().stringWidth(s);
        g2d.drawString(s, (SCREEN_WIDTH - w) / 2, y);
    }
}
