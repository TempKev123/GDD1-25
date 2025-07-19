package gdd.powerup;

import static gdd.Global.*;
import gdd.sprite.Player;
import javax.swing.ImageIcon;

public class LazerUpgrade extends PowerUp {

    public LazerUpgrade(int x, int y) {
        super(x, y);
        setImage(new ImageIcon(IMG_POWERUP_LAZER).getImage());
    }

    @Override
    public void upgrade(Player player) {
        player.setWeaponType(2); // WeaponType 2 = Lazer
    }

    @Override
    public void act() {
        this.x -= 2;
        if (this.x < -getImage().getWidth(null)) {
            this.die();
        }
    }
}
