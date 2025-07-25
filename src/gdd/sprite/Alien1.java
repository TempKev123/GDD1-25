package gdd.sprite;

import static gdd.Global.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

public class Alien1 extends Enemy {

    private static final int ENEMY_SPEED = 2;
    private List<EnemyBullet> bullets = new ArrayList<>();
    private boolean stopAtCenter = false; // ✅ เพิ่มตัวแปรนี้

    public Alien1(int x, int y) {
        super(x, y);
        initAlien1(x, y);
    }

    private void initAlien1(int x, int y) {
        this.x = x;
        this.y = y;

        var ii = new ImageIcon(IMG_ENEMY);
        var scaledImage = ii.getImage().getScaledInstance(
                ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    // 👾 ศัตรูเคลื่อนแนวนอน (ขวา → ซ้าย)
    @Override
    public void act() {
        if (stopAtCenter && x <= BOARD_WIDTH / 2) {
            x = BOARD_WIDTH / 2;
            return; // ❗ หยุดเมื่อถึงกลาง
        }

        x -= ENEMY_SPEED;

        for (EnemyBullet bullet : bullets) {
            if (bullet.isVisible()) bullet.act();
        }

        if (x + getImage().getWidth(null) < 0) {
            die();
        }
    }

    public void fire() {
        int startX = this.x;
        int startY = this.y + this.getImage().getHeight(null) / 2;
        bullets.add(new EnemyBullet(startX, startY));
    }

    public List<EnemyBullet> getBullets() {
        return bullets;
    }

    public void setStopAtCenter(boolean stop) {
        this.stopAtCenter = stop;
    }

    @Override
    public void die() {
        super.die();
        bullets.clear(); // 💥 ล้างกระสุนเมื่อ Alien ตาย
    }
}
