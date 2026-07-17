import javax.swing.ImageIcon;
import java.awt.*;

public class SpaceShip {
    public static final int WIDTH = 55;
    public static final int HEIGHT = 65;

    private int x, y;
    private int speed = 6;
    private Image shipImage;

    public boolean moveLeft = false;
    public boolean moveRight = false;
    public boolean moveUp = false;
    public boolean moveDown = false;

    public SpaceShip(int startX, int startY) {
        this.x = startX;
        this.y = startY;
        this.shipImage = loadImage("resources/spaceship.png");
    }

    private Image loadImage(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE || icon.getIconWidth() > 0) {
                return icon.getImage();
            }
        } catch (Exception e) {
            System.out.println("خطا در لود تصویر فضاپیما: " + e.getMessage());
        }
        return null;
    }

    public void update(int screenWidth, int screenHeight) {
        if (moveLeft && x > 0) x -= speed;
        if (moveRight && x < screenWidth - WIDTH) x += speed;
        if (moveUp && y > screenHeight / 2) y -= speed;
        if (moveDown && y < screenHeight - HEIGHT - 50) y += speed;
    }

    public Bullet fire() {
        int bulletX = this.x + (WIDTH / 2) - (Bullet.WIDTH / 2);
        int bulletY = this.y;
        return new Bullet(bulletX, bulletY);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, WIDTH, HEIGHT);
    }

    public void draw(Graphics2D g) {
        if (shipImage != null) {
            g.drawImage(shipImage, x, y, WIDTH, HEIGHT, null);
        } else {
            g.setColor(Color.RED);
            int[] xPoints = {x + WIDTH / 2, x, x + WIDTH};
            int[] yPoints = {y, y + HEIGHT, y + HEIGHT};
            g.fillPolygon(xPoints, yPoints, 3);
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
}