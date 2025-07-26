package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;

public class EnemyBullet extends Sprite {

    private final double dx = -4;
    private final double dy = 0;

    public EnemyBullet(int x, int y) {
        this.x = x;
        this.y = y;

        var ii = new ImageIcon("GDD1-25\\src\\images\\fire2.png"); // หรือ IMG_BOMB
        setImage(ii.getImage());
    }

    @Override
    public void act() {
        x += dx;
        y += dy;

        if (x < -50 || x > BOARD_WIDTH + 50 || y < -50 || y > BOARD_HEIGHT + 50) {
            die(); // จาก Sprite → ทำให้ invisible
        }
    }
}
