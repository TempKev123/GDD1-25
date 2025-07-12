package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;

public class Alien2 extends Enemy {

    private static final int SPEED = 4;
    private int dy = 2; // เคลื่อนขึ้นลง

    public Alien2(int x, int y) {
        super(x, y);
        initAlien2(x, y);
    }

    private void initAlien2(int x, int y) {
        var ii = new ImageIcon(IMG_ALIEN2); // เพิ่มใน Global.java
        var scaledImage = ii.getImage().getScaledInstance(
                ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    @Override
    public void act() {
        x -= SPEED;
        y += dy;

        // เด้งบนล่าง
        if (y < 0 || y > BOARD_HEIGHT - getImage().getHeight(null)) {
            dy = -dy;
        }

        if (x + getImage().getWidth(null) < 0) {
            die();
        }
    }
}
