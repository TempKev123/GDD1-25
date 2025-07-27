package gdd;

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
    public static final String IMG_ENEMY = "src/images/alien.png";
    public static final String IMG_PLAYER = "src/images/ship.png";
    public static final String IMG_SHOT = "src/images/fire1.png";
    public static final String IMG_SUPERSHOT = "src/images/fire_poweerup.png";
    public static final String IMG_EXPLOSION = "src/images/explosion.png";
    public static final String IMG_TITLE = "src/images/title.png";
    public static final String IMG_POWERUP_SPEEDUP = "src/images/speedup.png";
    public static final String IMG_ALIEN2 = "src/images/alien2.png";
    public static final String IMG_POWERUP_MULTISHOT = "src/images/multishot.png";
    public static final String IMG_POWERUP_WEAPON = "src/images/upgrade.png";
    public static final String IMG_POWERUP_LAZER = "src/images/upgrade.png";
    public static final String IMG_BOSS1 = "src/images/jeff.png";
    public static final String IMG_BOSS2 = "src/images/jeff_mech_head.png";
    public static final String IMG_BOSS3 = "src/images/jeff_bone_head.png";
    public static final String IMG_BOSS_NECK = "src/images/jeff_neck.png";
    public static final String IMG_ENEMY_BULLET = "src/images/fire2.png";

    //SFX
    public static final String SFX_SHOOT = "src/audio/shoot.wav";
    public static final String SFX_EXPLOSION = "src/audio/explosion.wav";
    public static final String SFX_INVKILLED = "src/audio/invaderkilled.wav";  
    public static final String SFX_TITLE = "src/audio/title.wav";
    public static final String SFX_BGM = "src/audio/scene1.wav";  
}
