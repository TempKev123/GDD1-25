package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;

public class Jeff extends Enemy{
    private String head;
    public Jeff(int x, int y) {
        super(x, y);
        initJeff(x, y);
    }

    private void initJeff(int x, int y) {
        this.x = x;
        this.y = y;
        if (y==200){//yes the head jeff uses is determeined by the y position easiest way to do this
            head=IMG_BOSS1;
        }
        else if (y==1){
            head=IMG_BOSS3;
        }
        else{
            head=IMG_BOSS2;
        }
        var ii = new ImageIcon(head);
        var scaledImage = ii.getImage().getScaledInstance(
                ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }
@Override
public void act() {
    System.out.println("Jeff is acting");
}
}
