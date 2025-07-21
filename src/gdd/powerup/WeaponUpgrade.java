package gdd.powerup;

import gdd.sprite.Player;
import javax.swing.ImageIcon;
import static gdd.Global.*;

public class WeaponUpgrade extends PowerUp {
    public WeaponUpgrade(int x, int y) {
        super(x, y);
        var ii = new ImageIcon(IMG_POWERUP_WEAPON);
        setImage(ii.getImage());
    }

    @Override
    public void upgrade(Player player) {
        player.setWeaponType(1); // Set to 3-way
        this.die();
    }

    @Override
    public void act() {
        this.x -= 2;
        if (this.x + getImage().getWidth(null) < 0) {
            this.die();
        }
    }
}
