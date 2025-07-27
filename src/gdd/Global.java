package gdd;
import java.nio.file.Paths;

public class Global {
    private Global() {
        // Prevent instantiation
    }

    public static final int SCALE_FACTOR = 3; // Scaling factor for sprites

    public static final int BOARD_WIDTH = 716; // Doubled from 358
    public static final int BOARD_HEIGHT = 700; // Doubled from 350
    public static final int BORDER_RIGHT = 60; // Doubled from 30
    public static final int BORDER_LEFT = 10; // Doubled from 5

    public static final int GROUND = 580; // Doubled from 290
    public static final int BOMB_HEIGHT = 10; // Doubled from 5

    public static final int ALIEN_HEIGHT = 30; // Doubled from 12
    public static final int ALIEN_WIDTH = 30; // Doubled from 12
    public static final int JEFF_HEIGHT = 69; // Doubled from 75
    public static final int JEFF_WIDTH = 98; // Doubled from 100
    public static final int ALIEN_INIT_X = 300; // Doubled from 150
    public static final int ALIEN_INIT_Y = 10; // Doubled from 5
    public static final int ALIEN_GAP = 30; // Gap between aliens

    public static final int GO_DOWN = 30; // Doubled from 15
    public static final int NUMBER_OF_ALIENS_TO_DESTROY = 24;
    public static final int CHANCE = 5;
    public static final int DELAY = 17;
    public static final int PLAYER_WIDTH = 30; // Doubled from 15
    public static final int PLAYER_HEIGHT = 20; // Doubled from 10


    // Images
    public static final String IMG_ENEMY = Paths.get("src", "images", "alien.png").toString();
    public static final String IMG_PLAYER = Paths.get("src", "images", "ship.png").toString();
    public static final String IMG_SHOT = Paths.get("src", "images", "fire1.png").toString();
    public static final String IMG_SUPERSHOT = Paths.get("src", "images", "fire_poweerup.png").toString();
    public static final String IMG_EXPLOSION = Paths.get("src", "images", "explosion.png").toString();
    public static final String IMG_TITLE = Paths.get("src", "images", "title.png").toString();
    public static final String IMG_POWERUP_SPEEDUP = Paths.get("src", "images", "speedup.png").toString();
    public static final String IMG_ALIEN2 = Paths.get("src", "images", "alien2.png").toString();
    public static final String IMG_POWERUP_MULTISHOT = Paths.get("src", "images", "multishot.png").toString();
    public static final String IMG_POWERUP_WEAPON = Paths.get("src", "images", "upgrade.png").toString();
    public static final String IMG_POWERUP_LAZER = Paths.get("src", "images", "upgrade.png").toString();
    public static final String IMG_BOSS1 = Paths.get("src", "images", "jeff.png").toString();
    public static final String IMG_BOSS2 = Paths.get("src", "images", "jeff_mech_head.png").toString();
    public static final String IMG_BOSS3 = Paths.get("src", "images", "jeff_bone_head.png").toString();
    public static final String IMG_BOSS_NECK = Paths.get("src", "images", "jeff_neck.png").toString();
    public static final String IMG_ENEMY_BULLET = Paths.get("src", "images", "fire2.png").toString();

    // SFX
    public static final String SFX_SHOOT = Paths.get("src", "audio", "shoot.wav").toString();
    public static final String SFX_EXPLOSION = Paths.get("src", "audio", "explosion.wav").toString();
    public static final String SFX_INVKILLED = Paths.get("src", "audio", "invaderkilled.wav").toString();  
    public static final String SFX_TITLE = Paths.get("src", "audio", "title.wav").toString();
    public static final String SFX_BGM = Paths.get("src", "audio", "scene1.wav").toString();
}


