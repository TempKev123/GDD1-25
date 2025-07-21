package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;

public class Jeff extends Enemy{
    private String head;
    public Jeff(int x, int y,int type) {
        super(x, y);
        initJeff(x, y);
    }

    private void initJeff(int x, int y) {
        this.x = x;
        this.y = y;
        if (x==200){
            head=IMG_BOSS1;
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
}
