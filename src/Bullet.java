import javax.swing.ImageIcon;
import java.awt.*;

public class Bullet {
    private static final int SPEED = 8;
    public static final int WIDTH = 6;
    public static final int HEIGHT = 20;

    private int x, y;
    private boolean alive = true;
    private Image bulletImage;

    public Bullet(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.bulletImage = loadImage("resources/player_bullet.png");
    }

    private Image loadImage(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE || icon.getIconWidth() > 0) {
                return icon.getImage();
            }
        } catch (Exception e) {
            System.out.println("خطا در لود تصویر تیر: " + e.getMessage());
        }
        return null;
    }

    public void update() {
        y -= SPEED;
        if (y < -HEIGHT) {
            alive = false;
        }
    }

    public boolean isAlive() { return alive; }
    public void kill() { this.alive = false; }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void draw(Graphics2D g) {
        if (!alive) return;

        if (bulletImage != null) {
            g.drawImage(bulletImage, x, y, WIDTH, HEIGHT, null);
        } else {
            g.setColor(Color.YELLOW);
            g.fillRect(x, y, WIDTH, HEIGHT);
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
}