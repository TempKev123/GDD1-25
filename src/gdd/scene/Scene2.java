package gdd.scene;

import gdd.AudioPlayer;
import gdd.Game;
import static gdd.Global.*;
import gdd.SoundEffect; // โฌ…๏ธ� เน€เธ�เธดเน�เธกเน�เธซเน�เน�เธ�เน�เน�เธ�
import gdd.SpawnDetails;
import gdd.powerup.MultiShot;
import gdd.powerup.PowerUp;
import gdd.powerup.SpeedUp;
import gdd.powerup.WeaponUpgrade;
import gdd.sprite.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;


public class Scene2 extends JPanel {
    private int frame = 0;
    private List<PowerUp> powerups;
    private List<Enemy> enemies;
    private List<Explosion> explosions;
    private List<Shot> shots;
    private Player player;
    private int gameOverCountdown = -1;
    private int elapsedFrames = 0;
    private int jefflife=1; 
    private int jeffno=3;



    // private Shot shot;

    final int BLOCKHEIGHT = 50;
    final int BLOCKWIDTH = 50;

    final int BLOCKS_TO_DRAW = BOARD_HEIGHT / BLOCKHEIGHT;

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String message = "Game Over";

    private String winmessage = "YOU WIN!";

    private final Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private final Random randomizer = new Random();

    private Timer timer;
    private final Game game;
    

    private int currentRow = -1;
    // TODO load this map from a file
    private int mapOffset = 0;
    private final int[][] MAP;

    private int currentStage = 1;
    private boolean inTransition = false;
    private int transitionTimer = 0;
    private final int TRANSITION_DURATION = 180; // 3 วินาที @ 60fps
private List<Enemy> currentGroup = new ArrayList<>();

  private List<List<Enemy>> alienGroups = new ArrayList<>();


  private int currentGroupIndex = 0;
  private boolean waitingForNextGroup = false;
  private int waitTimer = 0;
  private final int WAIT_DURATION = 120; // 2 วินาที

  private int alienShootCooldown = 0;
  private final int SHOOT_DELAY = 120; // ยิงทีละลำทุก 0.5 วิ
  private int shooterIndex = 0;




    // random generates a map of 0 and 1s to determine star positions
    {
        int[][] tempMap = new int[24][12];
        Random rand = new Random();
        for (int i = 0; i < tempMap.length; i++) {
            for (int j = 0; j < tempMap[i].length; j++) {
                tempMap[i][j] = rand.nextInt(4); // random int: 0, 1, 2 or 3
            }
        }
        MAP = tempMap;
    }

    private HashMap<Integer, SpawnDetails> spawnMap = new HashMap<>();
    private AudioPlayer audioPlayer;
    public Scene2(Game game) {
        this.game = game;
        // initBoard();
        // gameInit();
        loadSpawnDetails();
        loadAlienGroups();
    }

    private void initAudio() {
        try {
            String filePath = SFX_BGM;
            audioPlayer = new AudioPlayer(filePath);
            audioPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }


private void spawnAlien1Line(List<Enemy> group) {
    int x = BOARD_WIDTH;
    int startY = randomizer.nextInt(300 - 50) + 50;
    int gapY = 40;
    for (int i = 0; i < 5; i++) {
        int y = startY + i * gapY;
        Alien1 a = new Alien1(x, y);
        a.setStopAtCenter(true);
        group.add(a);
    }
}

private void spawnAlien1Circle(List<Enemy> group) {
    int centerX = BOARD_WIDTH / 2;
    int centerY = BOARD_HEIGHT / 2;
    int radius = 100;
    double angleStep = 2 * Math.PI / 6;
    for (int i = 0; i < 6; i++) {
        double angle = i * angleStep;
        Alien1Circle alien = new Alien1Circle(centerX, centerY, radius, angle); // ✅ ไม่มี player แล้ว
        group.add(alien);
    }
}



private void spawnTeleportShooters(List<Enemy> group) {
    for (int i = 0; i < 3; i++) {
        int x = 300 + randomizer.nextInt(BOARD_WIDTH - 400); // 🎯 ปลอดภัยอยู่ตรงกลางจอ
        int y = 100 + randomizer.nextInt(BOARD_HEIGHT - 200);
        AlienTeleportShooter ats = new AlienTeleportShooter(x, y);
        group.add(ats);
        System.out.println("Spawn TeleportShooter at x=" + x + ", y=" + y);
    }
}


private void mixGroup(List<Enemy> group, boolean includeCircle) {
    int roll = randomizer.nextInt(3);
    switch (roll) {
        case 0 -> spawnAlien1Line(group);
        case 1 -> spawnTeleportShooters(group);
        case 2 -> {
            if (includeCircle) {
                spawnAlien1Circle(group);
            } else {
                spawnAlien1Line(group);
            }
        }
    }
}


private void loadAlienGroups() {
    System.out.println("Loading alien groups for stage " + currentStage);
    alienGroups.clear();

    int groupCount = 3 + currentStage % 2; // เพิ่มความหลากหลาย
    for (int g = 0; g < groupCount; g++) {
        List<Enemy> group = new ArrayList<>();

        boolean isLastGroup = (g == groupCount - 1);

        // 👑 Boss Jeff เฉพาะกลุ่มสุดท้าย
        if (isLastGroup && currentStage % 2 == 0) {
            int jeffY = 100 + (currentStage * 17) % 300;
            Jeff boss = new Jeff(BOARD_WIDTH, jeffY);
            group.add(boss);
        } else {
            // 🎯 เปลี่ยนพฤติกรรมศัตรูตาม currentStage
            if (currentStage <= 3) {
                spawnAlien1Line(group);
            } else if (currentStage <= 6) {
                spawnAlien1Circle(group);
            } else if (currentStage <= 10) {
                spawnTeleportShooters(group);
            } else if (currentStage <= 15) {
                mixGroup(group, true);
            } else {
                mixGroup(group, true);
                spawnAlien1Circle(group); // เพิ่มความโหด
            }
        }

        alienGroups.add(group);
    }
}


private void loadSpawnDetails() {
    //spawnMap.put(50, new SpawnDetails("Jeff", BOARD_WIDTH, 200)); // spawn at frame 1000
    //there are 18600 frames in 5 minuites
    Random rand = new Random();

    // ───── PowerUps ─────
    //spawnMap.put(100, new SpawnDetails("Jeff_boss", BOARD_WIDTH, 100)); // if you want to test the boss early
    spawnMap.put(18000, new SpawnDetails("Jeff_boss", BOARD_WIDTH, 100));
    spawnMap.put(805, new SpawnDetails("PowerUp-SpeedUp", BOARD_WIDTH, 300));
    spawnMap.put(501, new SpawnDetails("PowerUp-MultiShot", BOARD_WIDTH, 200));
    spawnMap.put(1500, new SpawnDetails("PowerUp-MultiShot", BOARD_WIDTH, 200));
    spawnMap.put(2100, new SpawnDetails("PowerUp-MultiShot", BOARD_WIDTH, 200));
    spawnMap.put(6000, new SpawnDetails("PowerUp-MultiShot", BOARD_WIDTH, 200));
    spawnMap.put(16500, new SpawnDetails("PowerUp-MultiShot", BOARD_WIDTH, 200));
    spawnMap.put(10000, new SpawnDetails("PowerUp-WeaponUpgrade", BOARD_WIDTH, 150));
    spawnMap.put(10001, new SpawnDetails("PowerUp-SpeedUp", BOARD_WIDTH, 500));
    spawnMap.put(18005, new SpawnDetails("PowerUp-SpeedUp", BOARD_WIDTH, 300));

}



    private void initBoard() {

    }

    public void start() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        requestFocusInWindow();
        setBackground(Color.black);

        timer = new Timer(1000 / 60, new GameCycle());
        timer.start();

        gameInit();
        initAudio();
    }

    public void stop() {
        timer.stop();
        try {
            if (audioPlayer != null) {
                audioPlayer.stop();
            }
        } catch (Exception e) {
            System.err.println("Error closing audio player.");
        }
    }

    private void gameInit() {

        enemies = new ArrayList<>();
        powerups = new ArrayList<>();
        explosions = new ArrayList<>();
        shots = new ArrayList<>();
        loadSpawnDetails();   // โหลด PowerUps
        loadAlienGroups();    // โหลด wave-based enemy


        // for (int i = 0; i < 4; i++) {
        // for (int j = 0; j < 6; j++) {
        // var enemy = new Enemy(ALIEN_INIT_X + (ALIEN_WIDTH + ALIEN_GAP) * j,
        // ALIEN_INIT_Y + (ALIEN_HEIGHT + ALIEN_GAP) * i);
        // enemies.add(enemy);
        // }
        // }
        player = new Player();
        // shot = new Shot();
    }

private void drawMap(Graphics g) {


     // stars background
    int scrollOffset = (frame) % BLOCKWIDTH;
    //int scrollOffset = BLOCKWIDTH - (frame % BLOCKWIDTH);


    int baseCol = (frame) / BLOCKWIDTH;
    int colsNeeded = (BOARD_WIDTH / BLOCKWIDTH) + 2;

    for (int screenCol = 0; screenCol < colsNeeded; screenCol++) {
        int mapCol = (baseCol + screenCol) % MAP[0].length;

        //int x = BOARD_WIDTH - ((screenCol * BLOCKWIDTH) - scrollOffset);
        int x = ((screenCol * BLOCKWIDTH) - scrollOffset);

        if (x > BOARD_WIDTH || x < -BLOCKWIDTH) {
            continue;
        }

        for (int row = 0; row < MAP.length; row++) {
            if (MAP[row][mapCol] == 1) {
                int y = row * BLOCKHEIGHT;
                drawStarCluster(g, x, y, BLOCKWIDTH, BLOCKHEIGHT);
            }
            else if (MAP[row][mapCol] == 2) {
                int y = row * BLOCKHEIGHT;
                drawStarSmall(g, x, y, BLOCKWIDTH, BLOCKHEIGHT);
            }
            else if (MAP[row][mapCol] == 3) {
                int y = row * BLOCKHEIGHT;
                drawStar(g, x, y, BLOCKWIDTH, BLOCKHEIGHT);
            }
        }
    }
    // HUD: Display power-up levels
         Graphics2D g2d = (Graphics2D) g;
    Font font = new Font("Consolas", Font.BOLD, 16);
    g2d.setFont(font);

    // 🎯 Stage
    g2d.setColor(Color.GREEN);
    g2d.drawString("LEVEL 2 WAVE: " + currentStage, 20, 30);

    // ⏱ Time (กลางบน)
    int seconds = elapsedFrames / 60;
    int minutes = seconds / 60;
    seconds %= 60;
    String timeStr = String.format("TIME: %02d:%02d", minutes, seconds);
    g2d.setColor(Color.WHITE);
    FontMetrics fm = g2d.getFontMetrics();
    int timeX = (BOARD_WIDTH - fm.stringWidth(timeStr)) / 2;
    g2d.drawString(timeStr, timeX, 30);

    // 🌀 Multishot Icon
    ImageIcon multiIcon = new ImageIcon(IMG_POWERUP_MULTISHOT);
    g2d.drawImage(multiIcon.getImage(), 20, 60, null);           // Y = 60
    g2d.setColor(Color.CYAN);
    g2d.drawString("× " + player.getMultishotLevel(), 50, 78);  // Y = 78 (ไอคอน + ช่องว่าง)

    // ⚡ Speed Icon
    ImageIcon speedIcon = new ImageIcon(IMG_POWERUP_SPEEDUP);
    g2d.drawImage(speedIcon.getImage(), 20, 100, null);          // Y = 100 → ห่างจาก Multishot 40px
    g2d.drawString("× " + player.getSpeedLevel(), 50, 118);     // Y = 118
    // 🎯 Arcade Score HUD - ขวาบน
g2d.setColor(Color.YELLOW);
g2d.setFont(new Font("Consolas", Font.BOLD, 16));

String scoreStr = String.format("SCORE: %06d", ScoreTrack.instance.getScore());
int scoreX = BOARD_WIDTH - g2d.getFontMetrics().stringWidth(scoreStr) - 20;
int scoreY = 30;
g2d.drawString(scoreStr, scoreX, scoreY);
}

   private void drawStarCluster(Graphics g, int x, int y, int width, int height) {
    int centerX = x + width / 2;
    int centerY = y + height / 2;

    // 🌈 เปลี่ยนสีหลักของดาวตาม currentStage
    switch (currentStage) {
        case 1 -> g.setColor(Color.PINK);
        case 2 -> g.setColor(Color.GREEN);
        case 3 -> g.setColor(Color.CYAN);
        default -> g.setColor(Color.YELLOW);
    }

    // Main star (larger)
    g.fillOval(centerX - 2, centerY - 2, 4, 4);

    // Smaller surrounding stars
    g.setColor(Color.green);
    g.fillOval(centerX - 15, centerY - 10, 2, 2);
    g.fillOval(centerX + 12, centerY - 8, 2, 2);
    g.fillOval(centerX - 8, centerY + 12, 2, 2);
    g.fillOval(centerX + 10, centerY + 15, 2, 2);

    // Tiny stars for more detail
    g.fillOval(centerX - 20, centerY + 5, 1, 1);
    g.fillOval(centerX + 18, centerY - 15, 1, 1);
    g.fillOval(centerX - 5, centerY - 18, 1, 1);
    g.fillOval(centerX + 8, centerY + 20, 1, 1);
}

    private void drawStarSmall(Graphics g, int x, int y, int width, int height) {
    // Set star color to white
    

    // Draw a single star (small white dot in the center of the given area)
    int centerX = x + width / 2;
    int centerY = y + height / 2;
    g.setColor(Color.YELLOW);
    g.fillOval(centerX - 12, centerY - 1, 2, 2); // 2x2 pixel star
    g.setColor(Color.BLUE);
    g.fillOval(centerX + 30, centerY +20, 3, 3); 
    g.setColor(Color.WHITE);
    g.fillOval(centerX+10, centerY + 5, 4, 4);     
}
private void drawStar(Graphics g, int x, int y, int width, int height) {
    // Set star color to white  
    int centerX = x + width / 2;
    int centerY = y + height / 2;    
    g.setColor(Color.WHITE);
    g.fillOval(centerX+17, centerY - 50, 4, 4);
}


private void explodePlayer() {
    if (player.isDying()) return;

    Image explosionImage = new ImageIcon(IMG_EXPLOSION).getImage();
    int centerX = player.getX() + player.getImage().getWidth(null) / 2;
    int centerY = player.getY() + player.getImage().getHeight(null) / 2;
    int scaledWidth = explosionImage.getWidth(null) * 2;
    int scaledHeight = explosionImage.getHeight(null) * 2;
    Image scaledExplosion = explosionImage.getScaledInstance(
            scaledWidth, scaledHeight, Image.SCALE_SMOOTH);

    explosions.add(new Explosion(
            centerX - scaledWidth / 2,
            centerY - scaledHeight / 2,
            scaledExplosion
    ));

    SoundEffect.play(SFX_EXPLOSION, 0f);
    player.setDying(true);
    gameOverCountdown = 60;
}




    private void drawAliens(Graphics g) {

        for (Enemy enemy : enemies) {

            if (enemy.isVisible()) {

                g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            }

            if (enemy.isDying()) {

                enemy.die();
            }
        }
    }

    private void drawPowreUps(Graphics g) {

        for (PowerUp p : powerups) {

            if (p.isVisible()) {

                g.drawImage(p.getImage(), p.getX(), p.getY(), this);
            }

            if (p.isDying()) {

                p.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible() && !player.isDying()) {

            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {

            player.die();
        }
    }

    private void drawShot(Graphics g) {

        for (Shot shot : shots) {

            if (shot.isVisible()) {
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }
        }
    }

   private void drawEnemyBullets(Graphics g) {
    // Draw Alien1 bullets
    for (Enemy e : enemies) {
        if (e instanceof Alien1 alien1) {
            for (EnemyBullet b : alien1.getBullets()) {
                if (b.isVisible()) {
                    g.drawImage(b.getImage(), b.getX(), b.getY(), this);
                }
            }
        }
        // Draw Jeff bullets
        else if (e instanceof Jeff jeff) {
            for (EnemyBullet b : jeff.getBullets()) {
                if (b.isVisible()) {
                    g.drawImage(b.getImage(), b.getX(), b.getY(), this);
                }
            }
        }
    }
}


    private void drawExplosions(Graphics g) {

        List<Explosion> toRemove = new ArrayList<>();

        for (Explosion explosion : explosions) {

            if (explosion.isVisible()) {
                g.drawImage(explosion.getImage(), explosion.getX(), explosion.getY(), this);
                explosion.visibleCountDown();
            } else {
                toRemove.add(explosion);
            }
        }

        explosions.removeAll(toRemove);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);

        g.setColor(Color.white);
        g.drawString("FRAME: " + frame, 10, 10);

        g.setColor(Color.green);


        if (inTransition) {
    g.setColor(Color.YELLOW);
    g.setFont(new Font("Arial", Font.BOLD, 28));
    g.drawString("WAVE " + (currentStage + 1), BOARD_WIDTH / 2 - 70, BOARD_HEIGHT / 2);
}


        if (inGame) {

            drawMap(g);  // Draw background stars first
            drawExplosions(g);
            drawPowreUps(g);
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawEnemyBullets(g);
            // ✅ Draw Timer HUD
            int seconds = elapsedFrames / 60;
            int minutes = seconds / 60;
              seconds = seconds % 60;
              elapsedFrames++;

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }
            if (jeffno > 0) {
                gameOver(g);
            } else {
                winscreen(g);
            }
        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                BOARD_WIDTH / 2);
    }
    private void winscreen(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(winmessage, (BOARD_WIDTH - fontMetrics.stringWidth(winmessage)) / 2,
                BOARD_WIDTH / 2);
    }

private void update() {
    // ───── Load group if needed ─────
    if (!waitingForNextGroup && currentGroupIndex < alienGroups.size()) {
        currentGroup = alienGroups.get(currentGroupIndex);
        enemies.addAll(currentGroup);
        waitingForNextGroup = true;
    }

    // ───── Check if current group is done ─────
    if (waitingForNextGroup) {
        boolean allDead = true;
        for (Enemy e : currentGroup) {
            if (e.isVisible()) {
                allDead = false;
                break;
            }
        }

        if (allDead) {
            waitTimer++;
            if (waitTimer >= WAIT_DURATION) {
                waitTimer = 0;
                waitingForNextGroup = false;
                currentGroupIndex++;
                enemies.removeAll(currentGroup);
            }
        }
    }

    // ───── Player movement ─────
    player.act();

    // ───── Spawn PowerUps ─────
    SpawnDetails sd = spawnMap.get(frame);
    if (sd != null) {
        switch (sd.type) {
            case "PowerUp-SpeedUp" -> powerups.add(new SpeedUp(sd.x, sd.y));
            case "PowerUp-MultiShot" -> powerups.add(new MultiShot(sd.x, sd.y));
            case "PowerUp-WeaponUpgrade" -> powerups.add(new WeaponUpgrade(sd.x, sd.y));
            //case "Jeff" -> enemies.add(new Jeff(sd.x, sd.y));
            case "Jeff_boss" -> 
            {
                enemies.add(new Jeff(sd.x, 50));
                enemies.add(new Jeff(sd.x, 200));
                enemies.add(new Jeff(sd.x, 450));
            }
        }
    }

    // ───── PowerUps ─────
    for (PowerUp powerup : powerups) {
        if (powerup.isVisible()) {
            powerup.act();
            if (powerup.collidesWith(player)) {
                powerup.upgrade(player);
            }
        }
    }

    // ───── Enemy update ─────
    for (Enemy enemy : enemies) {
        if (enemy.isVisible()) {
            enemy.act();

            if (player.collidesWith(enemy)) {
                explodePlayer();
            }
        }

        // ───── Enemy Bullet collision ─────
        if (enemy instanceof Alien1 alien1) {
            for (EnemyBullet bullet : alien1.getBullets()) {
                if (bullet.isVisible()) {
                    bullet.act();
                    if (bullet.collidesWith(player)) {
                        bullet.die();
                        explodePlayer();
                    }
                }
            }
        } else if (enemy instanceof Jeff jeff) {
            for (EnemyBullet bullet : jeff.getBullets()) {
                if (bullet.isVisible()) {
                    bullet.act();
                    if (bullet.collidesWith(player)) {
                        bullet.die();
                        explodePlayer();
                    }
                }
            }
        }
    }

    // ───── Sequential Fire (Alien1 only) ─────
    if (alienShootCooldown > 0) {
        alienShootCooldown--;
    } else {
        List<Alien1> aliveAliens = new ArrayList<>();
        for (Enemy e : enemies) {
            if (e instanceof Alien1 a && a.isVisible()) {
                if (!(a instanceof Alien1Circle) || ((Alien1Circle) a).canShoot()) {
                    aliveAliens.add(a);
                }
            }
        }

        if (!aliveAliens.isEmpty()) {
            Alien1 shooter = aliveAliens.get(shooterIndex % aliveAliens.size());
            shooter.fire(); 
            shooterIndex++;
            alienShootCooldown = SHOOT_DELAY;
        }
    }

    // ───── Explosions ─────
    for (Explosion explosion : explosions) {
        explosion.act();
    }

    // ───── Game Over countdown ─────
    if (gameOverCountdown > 0) {
        gameOverCountdown--;
        if (gameOverCountdown == 0) {
            inGame = false;
        }
    }

    // ───── Player Shot vs Enemy ─────
    List<Shot> shotsToRemove = new ArrayList<>();
    for (Shot shot : shots) {
        shot.act();
        if (!shot.isVisible()) {
            shotsToRemove.add(shot);
            continue;
        }

        int shotX = shot.getX();
        int shotY = shot.getY();

        for (Enemy enemy : enemies) {
            if (!enemy.isVisible()) continue;

            int enemyX = enemy.getX();
            int enemyY = enemy.getY();
            int hitboxWidth = ALIEN_WIDTH;
            int hitboxHeight = ALIEN_HEIGHT;
            int EXSIZE = 2;
            boolean isJeff = false;

            if (enemy instanceof Jeff) {
                hitboxWidth = 200;
                hitboxHeight = 220;
                EXSIZE = 5;
                isJeff = true;
            }

            if (shotX >= enemyX && shotX <= enemyX + hitboxWidth
                && shotY >= enemyY && shotY <= enemyY + hitboxHeight) {

                Image explosionImage = new ImageIcon(IMG_EXPLOSION).getImage();
                Image scaledExplosion = explosionImage.getScaledInstance(
                        explosionImage.getWidth(null) * EXSIZE,
                        explosionImage.getHeight(null) * EXSIZE,
                        Image.SCALE_SMOOTH);

                int centerX = enemyX + hitboxWidth / 2;
                int centerY = enemyY + hitboxHeight / 2;

                explosions.add(new Explosion(
                        centerX - scaledExplosion.getWidth(null) / 2,
                        centerY - scaledExplosion.getHeight(null) / 2,
                        scaledExplosion
                ));

                if (isJeff && jefflife != 0) {
                    jefflife--;
                } else {
                    enemy.setImage(explosionImage);
                    enemy.setDying(true);
                    SoundEffect.play(SFX_INVKILLED, -5f);
                    deaths++;
                    ScoreTrack.instance.addScore(isJeff ? 1000 : 100);
                    if(isJeff){
                        jeffno--;
                        if (jeffno > 0) {
                            System.out.println("DRAGON defeated! Remaining: " + jeffno);
                        }
                        else {
                            System.out.println("DRAGON defeated! No more DRAGONS left.");
                            System.out.println("YOU WIN!");
                            inGame=false;
                        }
                    } 
                }

                shot.die();
                shotsToRemove.add(shot);
                break;
            }
        }
    }

    shots.removeAll(shotsToRemove);

    // ───── Stage Transition ─────
    if (!inTransition && currentGroupIndex >= alienGroups.size()) {
        inTransition = true;
        transitionTimer = TRANSITION_DURATION;
    }

    if (inTransition) {
        transitionTimer--;
        if (transitionTimer <= 0) {
            inTransition = false;
            currentStage++;
            currentGroupIndex = 0;
            shooterIndex = 0;
            alienGroups.clear();
            loadAlienGroups(); // 🔁 load wave ใหม่
        }
    }
}







    private void doGameCycle() {
        frame++;
        update();
        repaint();
    }

     private class GameCycle implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {
        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            player.keyPressed(e);

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE && inGame) {
                if (shots.size() < 18) {
                    SoundEffect.play(SFX_SHOOT);
                    int baseX = player.getX() + PLAYER_WIDTH;
                    int baseY = player.getY() + PLAYER_HEIGHT / 2;

                    int weapon = player.getWeaponType();
                    int level = player.getMultishotLevel();

                    if (weapon == 0) {
                        switch (level) {
                        case 0-> shots.add(new Shot(baseX, baseY ));
                        case 1 -> {
                            shots.add(new Shot(baseX, baseY ));
                            shots.add(new Shot(baseX-15, baseY-10));
                            shots.add(new Shot(baseX-15, baseY +10));
                            }
                        case 2 -> {
                            shots.add(new Shot(baseX, baseY ));
                            shots.add(new Shot(baseX-15, baseY-10));
                            shots.add(new Shot(baseX-15, baseY +10));
                            shots.add(new Shot(baseX-20, baseY-20));
                            shots.add(new Shot(baseX-20, baseY +20));
                        }
                        case 3 -> {
                            shots.add(new Shot(baseX, baseY ));
                            shots.add(new Shot(baseX-15, baseY-10));
                            shots.add(new Shot(baseX-15, baseY +10));
                            shots.add(new Shot(baseX-20, baseY-20));
                            shots.add(new Shot(baseX-20, baseY +20));
                            shots.add(new Shot(baseX-25, baseY-30));
                            shots.add(new Shot(baseX-25, baseY +30));
                            }
                        case 4 -> {
                            shots.add(new Shot(baseX, baseY ));
                            shots.add(new Shot(baseX-15, baseY-10));
                            shots.add(new Shot(baseX-15, baseY +10));
                            shots.add(new Shot(baseX-20, baseY-20));
                            shots.add(new Shot(baseX-20, baseY +20));
                            shots.add(new Shot(baseX-25, baseY-30));
                            shots.add(new Shot(baseX-25, baseY +30));
                            shots.add(new Shot(baseX-30, baseY-35));
                            shots.add(new Shot(baseX-30, baseY +35));
                            }
                        }} else {
                        switch (level) {
                            
                            case 0 -> {
                                shots.add(new Shot(baseX, baseY-25));
                                shots.add(new Shot(baseX, baseY));
                                shots.add(new Shot(baseX, baseY+25));
                            }
                            case 1 -> {
                                shots.add(new Shot(baseX, baseY+50));
                                shots.add(new Shot(baseX, baseY+25));
                                shots.add(new Shot(baseX, baseY));
                                shots.add(new Shot(baseX, baseY-25));
                                shots.add(new Shot(baseX, baseY-50));
                            }
                            case 2 -> {
                                shots.add(new Shot(baseX, baseY+75));
                                shots.add(new Shot(baseX, baseY+50));
                                shots.add(new Shot(baseX, baseY+25));
                                shots.add(new Shot(baseX, baseY));
                                shots.add(new Shot(baseX, baseY-25));
                                shots.add(new Shot(baseX, baseY-50));
                                shots.add(new Shot(baseX, baseY+75));
                            }
                            case 3 -> {
                                shots.add(new Shot(baseX, baseY+100));
                                shots.add(new Shot(baseX, baseY+75));
                                shots.add(new Shot(baseX, baseY+50));
                                shots.add(new Shot(baseX, baseY+25));
                                shots.add(new Shot(baseX, baseY));
                                shots.add(new Shot(baseX, baseY-25));
                                shots.add(new Shot(baseX, baseY-50));
                                shots.add(new Shot(baseX, baseY-75));
                                shots.add(new Shot(baseX, baseY-100));
                            }
                            case 4 -> {
                                shots.add(new Shot(baseX, baseY+125));
                                shots.add(new Shot(baseX, baseY+100));
                                shots.add(new Shot(baseX, baseY+75));
                                shots.add(new Shot(baseX, baseY+50));
                                shots.add(new Shot(baseX, baseY+25));
                                shots.add(new Shot(baseX, baseY));
                                shots.add(new Shot(baseX, baseY-25));
                                shots.add(new Shot(baseX, baseY-50));
                                shots.add(new Shot(baseX, baseY-75));
                                shots.add(new Shot(baseX, baseY-100));
                                shots.add(new Shot(baseX, baseY-125));
                            }
                        }
                    }
                }
            }
        }
    } 

}