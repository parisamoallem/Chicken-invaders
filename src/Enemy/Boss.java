package Enemy;

import chickeninvaders.game.Egg;
import javax.swing.ImageIcon;
import java.awt.*;
import java.util.List;

public abstract class Boss {

    protected double x, y;
    protected int width, height;
    protected int maxHealth;
    protected int health;
    protected double hSpeed;
    protected int hDirection = 1;
    protected long lastAttackTime = 0;
    protected long attackIntervalMs;
    protected double eggSpeed;

    protected Image bossImage;

    public Boss(double x, double y, int width, int height, int maxHealth, double hSpeed,
                long attackIntervalMs, double eggSpeed) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.maxHealth = maxHealth;
        this.health = maxHealth;
        this.hSpeed = hSpeed;
        this.attackIntervalMs = attackIntervalMs;
        this.eggSpeed = eggSpeed;
    }

    protected Image loadImage(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE || icon.getIconWidth() > 0) {
                return icon.getImage();
            }
        } catch (Exception e) {
            System.out.println("خطا در لود تصویر غول " + path + ": " + e.getMessage());
        }
        return null;
    }

    public abstract void update(int panelWidth, int panelHeight);
    public abstract void tryAttack(List<Egg> eggsOut);

    public void takeDamage(int amount) {
        health -= amount;
        if (health < 0) health = 0;
    }

    public boolean isDead() { return health <= 0; }
    public int getHealth() { return health; }
    public int getMaxHealth() { return maxHealth; }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, width, height);
    }

    public double getCenterX() { return x + width / 2.0; }
    public double getCenterY() { return y + height / 2.0; }

    public void drawHealthBar(Graphics2D g, int panelWidth) {
        int barWidth = panelWidth - 100;
        int barHeight = 18;
        int barX = 50;
        int barY = 15;
        g.setColor(Color.DARK_GRAY);
        g.fillRect(barX, barY, barWidth, barHeight);
        double ratio = health / (double) maxHealth;
        g.setColor(new Color((int) (255 * (1 - ratio)), (int) (200 * ratio), 40));
        g.fillRect(barX, barY, (int) (barWidth * ratio), barHeight);
        g.setColor(Color.WHITE);
        g.drawRect(barX, barY, barWidth, barHeight);
        g.drawString("Boss HP: " + health + " / " + maxHealth, barX + 5, barY + 14);
    }

    public abstract void draw(Graphics2D g);
}