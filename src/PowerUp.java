import javax.swing.ImageIcon;
import java.awt.*;
import java.net.URL;

public class PowerUp {

    public enum Type { ADD_FIRE, RAPID_FIRE, EXTRA_LIFE, SHIELD, FREEZE }

    public static final int SIZE = 36; // یکم بزرگتر و واضح تر
    private static final int SPEED = 2;

    private double x, y;
    private final Type type;
    private boolean alive = true;
    private Image powerUpImage;

    public PowerUp(double x, double y, Type type) {
        this.x = x;
        this.y = y;
        this.type = type;
        this.powerUpImage = loadTypeImage(type);
    }

    // لود هوشمند مسیر تصاویر پاورآپ ها
    private Image loadTypeImage(Type type) {
        String imageName;
        switch (type) {
            case ADD_FIRE:   imageName = "bomb_shot.png"; break;
            case RAPID_FIRE: imageName = "add_shot.png"; break;
            case EXTRA_LIFE: imageName = "heal.png"; break;
            case SHIELD:     imageName = "shield.png"; break;
            case FREEZE:     imageName = "freeze.png"; break;
            default:         imageName = "fast_shot.png"; break;
        }

        try {
            URL imgUrl = getClass().getClassLoader().getResource("resources/" + imageName);
            if (imgUrl != null) {
                return new ImageIcon(imgUrl).getImage();
            }
            ImageIcon icon = new ImageIcon("resources/" + imageName);
            if (icon.getIconWidth() > 0) return icon.getImage();
        } catch (Exception ignored) {}

        return null;
    }

    public static Type randomType() {
        Type[] all = Type.values();
        return all[(int) (Math.random() * all.length)];
    }

    public void update() { y += SPEED; }
    public boolean isAlive() { return alive; }
    public void kill() { this.alive = false; }
    public Type getType() { return type; }
    public double getY() { return y; }

    public Rectangle getBounds() {
        return new Rectangle((int) x, (int) y, SIZE, SIZE);
    }

    public void draw(Graphics2D g) {
        if (!alive) return;

        if (powerUpImage != null) {
            g.drawImage(powerUpImage, (int) x, (int) y, SIZE, SIZE, null);
        } else {
            Color borderCol = Color.CYAN;
            String letter = "P";

            switch (type) {
                case ADD_FIRE: borderCol = Color.ORANGE; letter = "F"; break;
                case RAPID_FIRE: borderCol = Color.RED; letter = "R"; break;
                case EXTRA_LIFE: borderCol = Color.GREEN; letter = "+"; break;
                case SHIELD: borderCol = Color.BLUE; letter = "S"; break;
                case FREEZE: borderCol = new Color(0, 230, 255); letter = "Z"; break;
            }

            g.setColor(new Color(20, 20, 35, 200));
            g.fillRoundRect((int) x, (int) y, SIZE, SIZE, 10, 10);

            g.setColor(borderCol);
            g.setStroke(new BasicStroke(2));
            g.drawRoundRect((int) x, (int) y, SIZE, SIZE, 10, 10);

            g.setFont(new Font("Consolas", Font.BOLD, 18));
            FontMetrics fm = g.getFontMetrics();
            int tx = (int) x + (SIZE - fm.stringWidth(letter)) / 2;
            int ty = (int) y + ((SIZE - fm.getHeight()) / 2) + fm.getAscent();
            g.drawString(letter, tx, ty);
        }
    }
}