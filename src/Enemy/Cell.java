package Enemy;

import Enemy.EnemyType;
import javax.swing.ImageIcon;
import java.awt.*;

public class Cell {

    public final int row;
    public final int col;
    public final EnemyType type;

    public int respawnCount;

    private Image cellBgImage;

    public Cell(int row, int col, EnemyType type, int respawnCount) {
        this.row = row;
        this.col = col;
        this.type = type;
        this.respawnCount = respawnCount;
        this.cellBgImage = loadImage("resources/cell_bg.png"); // اختیاری
    }

    private Image loadImage(String path) {
        try {
            ImageIcon icon = new ImageIcon(path);
            if (icon.getImageLoadStatus() == MediaTracker.COMPLETE || icon.getIconWidth() > 0) {
                return icon.getImage();
            }
        } catch (Exception e) {
        }
        return null;
    }

    public void drawDebug(Graphics2D g, int startX, int startY, int cellWidth, int cellHeight) {
        if (cellBgImage != null) {
            g.drawImage(cellBgImage, startX, startY, cellWidth, cellHeight, null);
        } else {
            g.setColor(new Color(255, 255, 255, 30));
            g.drawRect(startX, startY, cellWidth, cellHeight);
        }
    }
}