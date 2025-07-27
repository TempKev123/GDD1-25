package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;
import java.util.List;
import java.util.ArrayList;

public class Jeff extends Enemy{
    private List<EnemyBullet> bullets = new ArrayList<>();
    private int shootCooldown = 0;
    private int patternStep = 0;
    private String head;
    private int hitboxWidth = 200;  // Made bigger
    private int hitboxHeight = 250; // Made bigger
    private int dy = 1;
    
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

    private void shootPattern() {
        switch(patternStep) {
            case 0:
                shootStraight();
                break;
            case 1:
                shootSpread();
                break;
            case 2:
                shootRapid();
                break;
        }
        patternStep = (patternStep + 1) % 3;
    }

    private void shootStraight() {
        // Fire from middle-left of sprite
        int bulletX = x; // Left edge of sprite
        int bulletY = y + getImage().getHeight(null) / 2; // Middle height of sprite
        bullets.add(new EnemyBullet(bulletX, bulletY));
    }
    
    private void shootSpread() {
        // Fire from middle-left of sprite with spread pattern
        int baseX = x; // Left edge of sprite
        int baseY = y + getImage().getHeight(null) / 2; // Middle height of sprite
        bullets.add(new EnemyBullet(baseX, baseY - 15));
        bullets.add(new EnemyBullet(baseX, baseY));
        bullets.add(new EnemyBullet(baseX, baseY + 15));
    }

    private void shootRapid() {
        // Fire from middle-left of sprite with rapid pattern
        int bulletX = x; // Left edge of sprite
        int centerY = y + getImage().getHeight(null) / 2; // Middle height of sprite
        int bulletY1 = centerY - 20; // Slightly above center
        int bulletY2 = centerY + 20; // Slightly below center
        bullets.add(new EnemyBullet(bulletX, bulletY1));
        bullets.add(new EnemyBullet(bulletX, bulletY2));
    }

    public List<EnemyBullet> getBullets() {
        return bullets;
    }

    @Override
    public void act() {
        if (x > BOARD_WIDTH - getImage().getWidth(null) - 10) {
            // Step 1: Move left until just inside the screen
            x -= 2; // Move in smoothly
        } else {
            // Step 2: Hover up and down
            y += dy;

            // Clamp and reverse direction when hitting top/bottom bounds
            if (y <= 20 || y >= BOARD_HEIGHT - getImage().getHeight(null) - 20) {
                dy = -dy; // Reverse direction
            }
        }

        // Shoot cooldown countdown
        if (shootCooldown > 0) {
            shootCooldown--;
        } else {
            shootPattern();
            shootCooldown = 90; // adjust cooldown (frames) as needed
        }

        // Update bullets
        for (int i = bullets.size() - 1; i >= 0; i--) {
            EnemyBullet b = bullets.get(i);
            b.act();
            if (!b.isVisible()) {
                bullets.remove(i);
            }
        }
    }

    @Override
    public int getX() {
        // Return the actual sprite position for rendering
        return x;
    }

    @Override
    public int getY() {
        // Return the actual sprite position for rendering
        return y;
    }

    @Override
    public boolean collidesWith(Sprite other) {
        if (other == null || !this.isVisible() || !other.isVisible()) {
            return false;
        }

        // Calculate hitbox position - centered on the middle-left of the sprite
        int spriteWidth = getImage().getWidth(null);
        int spriteHeight = getImage().getHeight(null);
        
        // Position hitbox at middle-left of sprite
        int hitboxX = this.x; // Left edge of sprite
        int hitboxY = this.y + (spriteHeight / 2) - (hitboxHeight / 2);


        // Define other sprite's bounding box
        int otherX = other.getX();
        int otherY = other.getY();
        int otherWidth = other.getImage().getWidth(null);
        int otherHeight = other.getImage().getHeight(null);

        // Check rectangle intersection
        return hitboxX < otherX + otherWidth
                && hitboxX + hitboxWidth > otherX
                && hitboxY < otherY + otherHeight
                && hitboxY + hitboxHeight > otherY;
    }   
    
}