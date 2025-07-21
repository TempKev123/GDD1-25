package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;
import java.awt.Image;

public class Explosion extends Sprite {


    public Explosion(int x, int y, Image explosionImage) {

        this.x = x;
        this.y = y;
        setImage(explosionImage);
        visibleFrames = 30;
    }

    @Override
    public void act() {
        visibleCountDown();
        // this.x += direction;
    }


}
