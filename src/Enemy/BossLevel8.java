package Enemy;

import chickeninvaders.game.Egg;
import java.awt.*;
import java.util.List;

public class BossLevel8 extends Boss {

    private double vSpeed = 0;
    private final double baseY;
    private double vAccel = 0.02;
    private int vDirection = 1;
    private long lastDirectionChange = 0;

    public BossLevel8(int panelWidth) {
        super(panelWidth / 2.0 - 90, 30, 180, 135, 100, 2.0, 1000, 5.0);
        this.baseY = 30;
        this.bossImage = loadImage("resources/boss_8.png");
    }

    @Override
    public void update(int panelWidth, int panelHeight) {
        x += hSpeed * hDirection;
        if (x <= 0 || x + width >= panelWidth) hDirection *= -1;

        long now = System.currentTimeMillis();
        if (now - lastDirectionChange > 4000 && Math.random() < 0.02) {
            hDirection *= -1;
            lastDirectionChange = now;
        }

        vSpeed += vAccel * vDirection;
        if (vSpeed > 1.5 || vSpeed < -1.5) vDirection *= -1;
        y += vSpeed;
        if (y < baseY - 50) y = baseY - 50;
        if (y > baseY + 50) y = baseY + 50;
    }

    @Override
    public void tryAttack(List<Egg> eggsOut) {
        long now = System.currentTimeMillis();
        if (now - lastAttackTime < attackIntervalMs) return;
        lastAttackTime = now;
        double cx = getCenterX();
        double cy = getCenterY();
        for (int i = 0; i < 8; i++) {
            double angle = Math.toRadians(45 * i);
            eggsOut.add(new Egg(cx, cy, Math.cos(angle) * eggSpeed, Math.sin(angle) * eggSpeed));
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (bossImage != null) {
            g.drawImage(bossImage, (int) x, (int) y, width, height, null);
        } else {
            g.setColor(new Color(90, 20, 120));
            g.fillOval((int) x, (int) y, width, height);
            g.setColor(Color.BLACK);
            g.drawOval((int) x, (int) y, width, height);
            g.setColor(Color.RED);
            g.fillOval((int) (x + width * 0.25), (int) (y + height * 0.3), 18, 18);
            g.fillOval((int) (x + width * 0.6), (int) (y + height * 0.3), 18, 18);
        }
    }
}