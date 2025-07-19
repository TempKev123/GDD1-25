package gdd.sprite;

import static gdd.Global.*;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;

public class Player extends Sprite {

    private static final int START_X = 100;
    private static final int START_Y = 300;

    private int dy;
    private int width;
    private int height;
    private int currentSpeed = 2;
    private int speedLevel = 0;
    private int multishotLevel = 0;
    private int weaponType = 0; // 0 = default, 1 = 3-way

    private Rectangle bounds = new Rectangle(175, 135, 17, 32);

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        var ii = new ImageIcon(IMG_PLAYER);

        var scaledImage = ii.getImage().getScaledInstance(
                ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);

        this.width = scaledImage.getWidth(null);
        this.height = scaledImage.getHeight(null);

        setX(START_X);
        setY(START_Y);
    }

    // Movement logic
    public void act() {
        y += dy;
        x += dx;

        if (y < 0) {
            y = 0;
        }

        if (y > BOARD_HEIGHT - height) {
            y = BOARD_HEIGHT - height;
        }
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP) {
            dy = -currentSpeed;
        }

        if (key == KeyEvent.VK_DOWN) {
            dy = currentSpeed;
        }
         if (key == KeyEvent.VK_LEFT) {
            dx = -currentSpeed;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = currentSpeed;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_UP || key == KeyEvent.VK_DOWN) {
            dy = 0;
        }
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            dx = 0;
        }
    }

    // Speed power-up
    public int getSpeed() {
        return currentSpeed;
    }

    public int setSpeed(int speed) {
        if (speed < 1) {
            speed = 1;
        }
        this.currentSpeed = speed;
        return currentSpeed;
    }

    public void upgradeSpeed() {
        if (speedLevel < 4) {
            speedLevel++;
            currentSpeed = 2 + speedLevel;
        }
    }

    public int getSpeedLevel() {
        return speedLevel;
    }

    // Multishot power-up
    public void upgradeMultiShot() {
        if (multishotLevel < 4) {
            multishotLevel++;
        }
    }

    public int getMultishotLevel() {
        return multishotLevel;
    }

    public void setMultishotLevel(int level) {
        this.multishotLevel = level;
    }

    public void setWeaponType(int type) {
    this.weaponType = type;
}

public int getWeaponType() {
    return weaponType;
}

    // Reset all power-ups (use when game restarts)
    public void resetPowerUps() {
        multishotLevel = 0;
        speedLevel = 0;
        currentSpeed = 2;
        weaponType = 0;
    }

    // Optional: collision bounds
    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }
}
