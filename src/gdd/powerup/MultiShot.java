package gdd.powerup;

import static gdd.Global.*;
import gdd.sprite.Player;
import javax.swing.ImageIcon;

public class MultiShot extends PowerUp {

    public MultiShot(int x, int y) {
        super(x, y);
        var ii = new ImageIcon(IMG_POWERUP_MULTISHOT); // เพิ่ม path รูปใน Global
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth(), ii.getIconHeight(), java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    @Override
    public void upgrade(Player player) {
        int level = player.getMultishotLevel();
        if (level < 4) {
            player.setMultishotLevel(level + 1);
            System.out.println("Upgraded to multishot level: " + (level + 1));
        }
        this.die(); 
    }

    @Override
public void act() {
    this.x -= 2;
    if (this.x + getImage().getWidth(null) < 0) {
        this.die(); // หายจากจอซ้าย
    }
}

}
