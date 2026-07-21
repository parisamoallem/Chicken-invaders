package Enemy;

import Enemy.Cell;
import javax.swing.ImageIcon;
import java.awt.*;

public abstract class Enemy {
    protected double x, y;
    protected int health;
    protected boolean alive = true;
    protected int width = 50;
    protected int height = 50;
    protected Image enemyImage;
    protected boolean entering = false;
    protected boolean frozen = false;
    protected Cell cell;

    protected double enterSpeed = 5.0;

    public Enemy() {
    }

    public Enemy(int startX, int startY, int health) {
        this.x = startX;
        this.y = startY;
        this.health = health;
    }

    public Enemy(Cell cell, double startX, double startY, boolean entering) {
        this.cell = cell;
        this.x = startX;
        this.y = startY;
        this.entering = entering;
        this.health = 2;
    }

    protected Image loadImage(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE || icon.getIconWidth() > 0) {
                return icon.getImage();
            }
        } catch (Exception e) {
            System.out.println("Error loading image: " + e.getMessage());
        }
        return null;
    }

    public void move(double dx, double dy) {
        this.x += dx;
        this.y += dy;
    }

    public void move(int dx, int dy) {
        this.x += dx;
        this.y += dy;
    }

    /**
     * enemy رو با سرعت enterSpeed به سمت مختصات هدف (targetX, targetY) حرکت می‌ده.
     * وقتی به هدف رسید، فلگ entering غیرفعال میشه تا از این به بعد update عادی اجرا بشه.
     */
    public void updateEntering(double targetX, double targetY) {
        double dx = targetX - this.x;
        double dy = targetY - this.y;
        double dist = Math.sqrt(dx * dx + dy * dy);

        if (dist <= enterSpeed || dist == 0) {
            this.x = targetX;
            this.y = targetY;
            this.entering = false;
        } else {
            this.x += (dx / dist) * enterSpeed;
            this.y += (dy / dist) * enterSpeed;
        }
    }

    public void takeDamage() {
        if (!alive) return;
        this.health--;
        if (this.health <= 0) {
            this.alive = false;
        }
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        draw(g2d);
    }

    public void draw(Graphics2D g) {
        if (!alive) return;

        if (enemyImage != null) {
            g.drawImage(enemyImage, (int) x, (int) y, width, height, null);
        } else {
            g.setColor(getColor());
            g.fillOval((int) x, (int) y, width, height);
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 12));
            g.drawString("HP: " + health, (int) x + 5, (int) y + 25);
        }

        if (frozen) {
            g.setColor(new Color(0, 191, 255, 110));
            g.fillOval((int) x, (int) y, width, height);
        }
    }

    public boolean isFrozen() {
        return frozen;
    }

    public void setFrozen(boolean frozen) {
        this.frozen = frozen;
    }

    public Cell getCell() {
        return cell;
    }

    public EnemyType getType() {
        return cell != null ? cell.type : EnemyType.NORMAL;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public int getX() { return (int) x; }
    public void setX(double x) { this.x = x; }
    public void setX(int x) { this.x = x; }

    public int getY() { return (int) y; }
    public void setY(double y) { this.y = y; }
    public void setY(int y) { this.y = y; }

    public int getWIDTH() { return width; }
    public int getHEIGHT() { return height; }
    public boolean isAlive() { return alive; }
    public void setAlive(boolean alive) { this.alive = alive; }

    public abstract void update(double targetX, double targetY, double time);

    public abstract Color getColor();
}