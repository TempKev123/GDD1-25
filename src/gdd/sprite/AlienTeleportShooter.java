package gdd.sprite;

import static gdd.Global.*;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.swing.JPanel;

public class AlienTeleportShooter extends Alien1 {
    private int teleportCooldown = 180; // วาร์ปทุก 3 วินาที
    private int shootCooldown = 90;     // ยิงทุก 1.5 วินาที

    private List<EnemyBullet> bullets = new ArrayList<>();
    private Random rand = new Random();

    public AlienTeleportShooter(int x, int y) {
        super(x, y);
        setStopAtCenter(false);
    }

    @Override
    public void act() {
        // 🔫 ยิงกระสุน
        shootCooldown--;
        if (shootCooldown <= 0) {
            bullets.add(new EnemyBullet(x, y + getImage().getHeight(null) / 2));
            shootCooldown = 90;
        }

        // 🌀 วาร์ปไปที่ใหม่
        teleportCooldown--;
        if (teleportCooldown <= 0) {
            teleport();
            teleportCooldown = 180;
        }

        // 💫 อัปเดตกระสุน
        List<EnemyBullet> toRemove = new ArrayList<>();
        for (EnemyBullet b : bullets) {
            b.act();
            if (!b.isVisible()) {
                toRemove.add(b);
            }
        }
        bullets.removeAll(toRemove);
    }

   private void teleport() {
    // วาร์ปในฝั่งขวา: จากครึ่งจอถึงขอบขวา - 50
    int minX = BOARD_WIDTH / 2;
    int maxX = BOARD_WIDTH - 50;
    int newX = rand.nextInt(maxX - minX) + minX;

    int minY = 50;
    int maxY = BOARD_HEIGHT - 100;
    int newY = rand.nextInt(maxY - minY) + minY;

    this.x = newX;
    this.y = newY;
}


    public void render(Graphics2D g2d, JPanel panel) {
        g2d.drawImage(getImage(), getX(), getY(), panel);

        for (EnemyBullet b : bullets) {
            if (b.isVisible()) {
                g2d.drawImage(b.getImage(), b.getX(), b.getY(), panel);
            }
        }
    }

    public List<EnemyBullet> getBullets() {
        return bullets;
    }

    // 👁 ไม่ต้องเช็ก fade-in อีกต่อไป
    public boolean isReady() {
        return true;
    }
}
