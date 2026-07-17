package Enemy;

import javax.swing.ImageIcon;
import java.awt.*;

public class Egg {

    public static final int WIDTH = 20;
    public static final int HEIGHT = 25;

    public double x, y;
    public double speedX, speedY;
    private boolean alive = true;

    private Image eggImage;

    public Egg(double startX, double startY, double speedX, double speedY) {
        this.x = startX;
        this.y = startY;
        this.speedX = speedX;
        this.speedY = speedY;
        this.eggImage = loadImage("resources/egg.png");
    }

    private Image loadImage(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE || icon.getIconWidth() > 0) {
                return icon.getImage();
            }
        } catch (Exception e) {
            System.out.println("خطا در لود تصویر تخم‌مرغ: " + e.getMessage());
        }
        return null;
    }

    public void update() {
        x += speedX;
        y += speedY;
    }

    public boolean isAlive() { return alive; }
    public void kill() { this.alive = false; }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, WIDTH, HEIGHT);
    }

    public void draw(Graphics2D g) {
        if (!alive) return;

        if (eggImage != null) {
            g.drawImage(eggImage, (int) x, (int) y, WIDTH, HEIGHT, null);
        } else {
            g.setColor(new Color(245, 240, 220));
            g.fillOval((int) x, (int) y, WIDTH, HEIGHT);
            g.setColor(Color.BLACK);
            g.drawOval((int) x, (int) y, WIDTH, HEIGHT);
        }
    }
}