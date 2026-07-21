package Enemy;

import Enemy.Egg;
import java.awt.*;
import java.util.List;

public class BossLevel4 extends Boss {

    private double vSpeed = 0.5;
    private int vDirection = 1;
    private final double baseY;

    public BossLevel4(int panelWidth) {
        // تغییر عرض به ۱۴۰ و ارتفاع به ۱۰۰ جهت سایز منطقی و چشم‌نواز تصویر
        super(panelWidth / 2.0 - 70, 40, 140, 100, 150, 1.5, 1500, 4.0);
        this.baseY = 40;
        this.bossImage = loadImage("resources/boss1.png");
    }

    @Override
    public void update(int panelWidth, int panelHeight) {
        x += hSpeed * hDirection;
        if (x <= 0 || x + width >= panelWidth) hDirection *= -1;

        y += vSpeed * vDirection;
        if (y < baseY - 20 || y > baseY + 20) vDirection *= -1;
    }

    @Override
    public void tryAttack(List<Egg> eggsOut) {
        long now = System.currentTimeMillis();
        if (now - lastAttackTime < attackIntervalMs) return;
        lastAttackTime = now;
        double cx = getCenterX();
        double cy = getCenterY();
        int[][] dirs = {{0, 1}, {0, -1}, {1, 0}, {-1, 0}};
        for (int[] d : dirs) {
            eggsOut.add(new Egg(cx, cy, d[0] * eggSpeed, d[1] * eggSpeed));
        }
    }

    @Override
    public void draw(Graphics2D g) {
        if (bossImage != null) {
            g.drawImage(bossImage, (int) x, (int) y, width, height, null);
        } else {
            // جایگزین هندسی در صورت عدم لود عکس
            g.setColor(new Color(200, 60, 60));
            g.fillOval((int) x, (int) y, width, height);
            g.setColor(Color.BLACK);
            g.drawOval((int) x, (int) y, width, height);
            g.setColor(Color.YELLOW);
            g.fillOval((int) (x + width * 0.25), (int) (y + height * 0.3), 14, 14);
            g.fillOval((int) (x + width * 0.65), (int) (y + height * 0.3), 14, 14);
        }
    }
}