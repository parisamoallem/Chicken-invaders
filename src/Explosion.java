import javax.swing.ImageIcon;
import java.awt.*;

//افکت انفجار: اگر عکس explosion.png در پوشه resources باشد همان رسم میشود
//وگرنه یک دایره محو شونده به جایش نشان داده میشود
public class Explosion {

    private static final long LIFETIME_MS = 400;

    private double x, y;
    private int maxRadius;
    private long startTime;
    private Image explosionImage;

    public Explosion(double centerX, double centerY, int maxRadius) {
        this.x = centerX;
        this.y = centerY;
        this.maxRadius = maxRadius;
        this.startTime = System.currentTimeMillis();
        this.explosionImage = loadImage("resources/Explosion.png");
    }

    private Image loadImage(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE || icon.getIconWidth() > 0) {
                return icon.getImage();
            }
        } catch (Exception e) {
            System.out.println("خطا در لود تصویر انفجار: " + e.getMessage());
        }
        return null;
    }

    public boolean isAlive() {
        return System.currentTimeMillis() - startTime < LIFETIME_MS;
    }

    public void draw(Graphics2D g) {
        long elapsed = System.currentTimeMillis() - startTime;
        double progress = elapsed / (double) LIFETIME_MS;
        if (progress > 1) progress = 1;

        int radius = (int) (maxRadius * progress);

        if (explosionImage != null) {
            int size = maxRadius + radius;
            g.drawImage(explosionImage, (int) x - size / 2, (int) y - size / 2, size, size, null);
        } else {
            int alpha = (int) (220 * (1 - progress));
            if (alpha < 0) alpha = 0;
            g.setColor(new Color(255, 180, 40, alpha));
            g.fillOval((int) x - radius, (int) y - radius, radius * 2, radius * 2);
            g.setColor(new Color(255, 90, 20, alpha));
            g.drawOval((int) x - radius, (int) y - radius, radius * 2, radius * 2);
        }
    }
}
