package gdd.sprite;

import static gdd.Global.*;

public class Alien1Circle extends Alien1 {

    private enum Phase { CIRCLE, EXIT_LEFT, RETURN }
    private Phase phase;

    private int tick = 0;

    private double angle;
    private int radius;
    private double angularSpeed = 0.03;

    private int centerX, centerY;
    private int originalX, originalY;
    private int shootCooldown = 90;


    public Alien1Circle(int centerX, int centerY, int radius, double angle) {
        super(0, 0);
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
        this.angle = angle;
        this.phase = Phase.CIRCLE;
        setStopAtCenter(false);
        updatePosition();

        this.originalX = this.x;
        this.originalY = this.y;
    }

    private void updatePosition() {
        this.x = centerX + (int)(radius * Math.cos(angle));
        this.y = centerY + (int)(radius * Math.sin(angle));
    }
    public boolean canShoot() {
    return phase == Phase.CIRCLE;
}


    @Override
    public void act() {
        switch (phase) {
     case CIRCLE -> {
    tick++;
    angle += angularSpeed;
    updatePosition();

    // 🔫 ยิงทุก 1.5 วินาที เฉพาะตอนหมุน
    shootCooldown--;
    if (shootCooldown <= 0) {
        getBullets().add(new EnemyBullet(x, y + getImage().getHeight(null) / 2));
        shootCooldown = 90;
    }

    if (tick >= 300) {
        phase = Phase.EXIT_LEFT;
        tick = 0;
    }
}


            case EXIT_LEFT -> {
                this.x -= 3;
                if (x + getImage().getWidth(null) < 0) {
                    phase = Phase.RETURN;
                    this.x = BOARD_WIDTH + 50;
                    this.y = originalY;
                }
            }

            case RETURN -> {
    if (x > originalX) {
        x -= 3; // ค่อย ๆ บินกลับซ้าย
    } else {
        x = originalX;
        phase = Phase.CIRCLE;
        tick = 0;
    }
}

        }
    }
}
