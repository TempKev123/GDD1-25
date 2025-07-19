package gdd.powerup;

import static gdd.Global.*;
import gdd.sprite.Player;
import javax.swing.ImageIcon;

public class SpeedUp extends PowerUp {

    public SpeedUp(int x, int y) {
        super(x, y);
        ImageIcon ii = new ImageIcon(IMG_POWERUP_SPEEDUP);
        var scaledImage = ii.getImage().getScaledInstance(
                ii.getIconWidth(),
                ii.getIconHeight(),
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    @Override
    public void act() {
        this.x -= 2;
        if (this.x + getImage().getWidth(null) < 0) {
            this.die(); // หายเมื่อออกจอซ้าย
        }
    }

    @Override
    public void upgrade(Player player) {
        player.upgradeSpeed();  // ✅ ใช้ระบบหลายระดับ
        this.die();
    }
}
