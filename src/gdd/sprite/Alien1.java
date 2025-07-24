package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;
import java.util.ArrayList;
import java.util.List;

public class Alien1 extends Enemy {

    private static final int ENEMY_SPEED = 2;
    private List<EnemyBullet> bullets = new ArrayList<>();

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
    public void act() {
        this.x -= ENEMY_SPEED;
        if (this.x + getImage().getWidth(null) < 0) {
            die();
        }

        // move bullets
        for (EnemyBullet bullet : bullets) {
            bullet.act();
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
}
