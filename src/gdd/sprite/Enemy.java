package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;

public class Enemy extends Sprite {

    private static final int ENEMY_SPEED = 2;

    public Enemy(int x, int y) {
        initEnemy(x, y);
    }

    private void initEnemy(int x, int y) {
        this.x = x;
        this.y = y;

        var ii = new ImageIcon(IMG_ENEMY);
        var scaledImage = ii.getImage().getScaledInstance(
                ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    public void act() {
        this.x -= ENEMY_SPEED;

        // หายเมื่อพ้นขอบจอ
        if (this.x + getImage().getWidth(null) < 0) {
            die();
        }
    }
}
