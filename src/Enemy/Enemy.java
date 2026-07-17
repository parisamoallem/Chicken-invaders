package Enemy;
import javax.swing.ImageIcon;
import java.awt.*;

public abstract class Enemy {

    // اندازه استاندارد و منطقی مرغ‌ها در صفحه بازی
    public static final int SIZE = 45;

    public double x, y;
    protected final Cell cell;
    protected boolean alive = true;
    protected boolean entering;
    protected double enterSpeed = 4.0;
    private boolean frozen = false;

    protected Image enemyImage;

    public Enemy(Cell cell, double startX, double startY, boolean entering) {
        this.cell = cell;
        this.x = startX;
        this.y = startY;
        this.entering = entering;
    }

    protected Image loadImage(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE || icon.getIconWidth() > 0) {
                return icon.getImage();
            } else {
                System.out.println("هشدار: تصویر یافت نشد یا ناقص است: " + path);
            }
        } catch (Exception e) {
            System.out.println("خطا در لود تصویر " + path + ": " + e.getMessage());
        }
        return null;
    }

    public Cell getCell() { return cell; }
    public EnemyType getType() { return cell.type; }

    public boolean isEntering() { return entering; }
    public boolean isAlive() { return alive; }
    public void kill() { alive = false; }

    public void setFrozen(boolean frozen) { this.frozen = frozen; }
    public boolean isFrozen() { return frozen; }

    protected void updateEntering(double targetX, double targetY) {
        double dx = targetX - x;
        double dy = targetY - y;
        double dist = Math.hypot(dx, dy);
        if (dist < enterSpeed) {
            x = targetX;
            y = targetY;
            entering = false;
        } else {
            x += enterSpeed * dx / dist;
            y += enterSpeed * dy / dist;
        }
    }

    public abstract void update(double targetX, double targetY, double time);

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, SIZE, SIZE);
    }

    public abstract Color getColor();

    public void draw(Graphics2D g) {
        if (!alive) return;

        if (enemyImage != null) {
            g.drawImage(enemyImage, (int) x, (int) y, SIZE, SIZE, null);

            if (frozen) {
                g.setColor(new Color(0, 191, 255, 100));
                g.fillOval((int) x, (int) y, SIZE, SIZE);
            }
        } else {
            g.setColor(frozen ? Color.CYAN.darker() : getColor());
            g.fillOval((int) x, (int) y, SIZE, SIZE);
            g.setColor(Color.BLACK);
            g.drawOval((int) x, (int) y, SIZE, SIZE);
            g.fillPolygon(new int[]{(int) x + SIZE / 2 - 4, (int) x + SIZE / 2 + 4, (int) x + SIZE / 2},
                    new int[]{(int) y + SIZE - 2, (int) y + SIZE - 2, (int) y + SIZE + 6}, 3);
        }
    }
}