package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;

import gdd.SoundEffect;

public class Shot extends Sprite {

    private static final int H_SPACE = 20;
    private static final int SHOT_SPEED = 10; // ความเร็วกระสุนแนวนอน

    public Shot() {
    }

    public Shot(int x, int y) {
        initShot(x, y);
        SoundEffect.play("GDD1-25\\src\\audio\\shoot.wav"); // 🔊 เล่นเสียงยิงทันที
    }

    private void initShot(int x, int y) {
        var ii = new ImageIcon(IMG_SHOT);

        var scaledImage = ii.getImage().getScaledInstance(
                ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);

        // ยิงออกด้านขวาจากปากปืน
        setX(x + H_SPACE);
        setY(y); // ไม่จำเป็นต้องเลื่อน Y แล้ว
    }

    public void act() {
    x += SHOT_SPEED;

    if (x > BOARD_WIDTH) {
        die();  // หายเมื่อออกขวา
    }
}

}
