package gdd.sprite;

import javax.swing.ImageIcon;
import static gdd.Global.*;

public class LazerShot extends Shot {
    public LazerShot(int x, int y) {
        super(x, y);
        setImage(new ImageIcon(IMG_LAZER_SHOT).getImage());
    }

    @Override
    public void act() {
        this.x += 12; // เร็วกว่าปกติ
        if (this.x > BOARD_WIDTH) {
            this.die();
        }
    }
}
