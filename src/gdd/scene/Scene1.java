package gdd.scene;

import gdd.AudioPlayer;
import gdd.Game;
import static gdd.Global.*;
import gdd.SpawnDetails;
import gdd.powerup.MultiShot;
import gdd.powerup.PowerUp;
import gdd.powerup.SpeedUp;
import gdd.powerup.WeaponUpgrade;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
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
import gdd.sprite.*;

public class Scene1 extends JPanel {
    private int frame = 0;
    private List<PowerUp> powerups;
    private List<Enemy> enemies;
    private List<Explosion> explosions;
    private List<Shot> shots;
    private Player player;
    private int gameOverCountdown = -1;
    // private Shot shot;

    final int BLOCKHEIGHT = 50;
    final int BLOCKWIDTH = 50;

    final int BLOCKS_TO_DRAW = BOARD_HEIGHT / BLOCKHEIGHT;

    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String message = "Game Over";

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
  private final int SHOOT_DELAY = 30; // ยิงทีละลำทุก 0.5 วิ
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
    private int lastRowToShow;
    private int firstRowToShow;

    public Scene1(Game game) {
        this.game = game;
        // initBoard();
        // gameInit();
        loadSpawnDetails();
        loadAlienGroups();
    }

    private void initAudio() {
        try {
            String filePath = "GDD1-25\\src\\audio\\scene1.wav";
            audioPlayer = new AudioPlayer(filePath);
            audioPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

private void loadAlienGroups() {
    int groupCount = 5;

    for (int g = 0; g < groupCount; g++) {
        List<Enemy> group = new ArrayList<>();

        if (g == 4) {
            // 👑 กลุ่มสุดท้าย → Boss (เปลี่ยนตาม stage ได้)
            Alien2 boss = new Alien2(BOARD_WIDTH, 180);
            group.add(boss);
        } else {
            int x = BOARD_WIDTH;
            int startY = 100;
            int gapY = 60;

            for (int i = 0; i < 5; i++) {
                int y = startY + (i * gapY);
                Alien1 alien = new Alien1(x, y);
                alien.setStopAtCenter(true);

                // ✨ เพิ่มความแตกต่างตาม stage โดยไม่เปลี่ยนรูปแบบ
                if (currentStage == 2) {
                    // ตัวอย่าง: เพิ่มความเร็ว หรือเตรียมยิง 2 นัด
                    // alien.setSpeed(alien.getSpeed() + 1); // ถ้ามี method
                    // alien.enableDoubleShot(); // ถ้ามีระบบยิงหลายแบบ
                }

                group.add(alien);
            }
        }

        alienGroups.add(group);
    }
}

private void loadSpawnDetails() {
    spawnMap.put(1000, new SpawnDetails("Jeff", BOARD_WIDTH, 200)); // spawn at frame 1000
    //there are 18600 frames in 5 minuites
        // TODO load this from a file
        /*spawnMap.put(51, new SpawnDetails("Jeff", 500, 1)); // spawn jeff1
        spawnMap.put(52, new SpawnDetails("Jeff", 500, 200)); // spawn jeff2
        spawnMap.put(53, new SpawnDetails("Jeff", 500, 450)); // spawn jeff3*/
    Random rand = new Random();

    // ───── PowerUps ─────
    spawnMap.put(50, new SpawnDetails("PowerUp-SpeedUp", BOARD_WIDTH, 100));
    spawnMap.put(350, new SpawnDetails("PowerUp-MultiShot", BOARD_WIDTH, 200));
    spawnMap.put(450, new SpawnDetails("PowerUp-WeaponUpgrade", BOARD_WIDTH, 150));
    spawnMap.put(805, new SpawnDetails("PowerUp-SpeedUp", BOARD_WIDTH, 300));

    // ───── Alien2 ─────
    int[] alien2Frames = {401, 402, 441, 442, 443, 444, 447, 448, 451, 452, 453, 600, 700, 730, 750};
    int[] alien2Y =      {100, 200, 390, 100, 400,  20, 500, 200, 300, 400,  30, 100, 200, 200, 200};

    for (int i = 0; i < alien2Frames.length; i++) {
        spawnMap.put(alien2Frames[i], new SpawnDetails("Alien2", BOARD_WIDTH, alien2Y[i]));
    }

    // ───── Alien1: แบบซิกแซก ─────
    int[] zigzagFrames = {200, 210, 220, 230, 240, 250};
    int baseY = 100;
    int[] offsetY = {0, 20, -15, 30, -10, 25};

    for (int i = 0; i < zigzagFrames.length; i++) {
        int x = BOARD_WIDTH; // spawn ด้านขวา
        int y = baseY + offsetY[i % offsetY.length];
        spawnMap.put(zigzagFrames[i], new SpawnDetails("Alien1", x, y));
    }

    // ───── Alien1: แบบสุ่ม Y ─────
    int[] randomYFrames = {300, 310, 320, 330, 340};
    for (int frame : randomYFrames) {
        int y = 50 + rand.nextInt(200); // Y 50–250
        spawnMap.put(frame, new SpawnDetails("Alien1", BOARD_WIDTH, y));
    }

    // ───── Alien1: Wave แนวนอน ─────
    int[] waveFrames = {400, 410, 420, 430, 440};
    int waveBaseY = 150;

    for (int i = 0; i < waveFrames.length; i++) {
        int x = BOARD_WIDTH;
        int y = waveBaseY + (int)(30 * Math.sin(i * Math.PI / 3)); // sin wave
        spawnMap.put(waveFrames[i], new SpawnDetails("Alien1", x, y));
    }

    // ───── Alien1: แถวเดียวคงที่ ─────
    int[] rowFrames = {500, 505, 510, 515, 520};
    int rowY = 220;

    for (int frame : rowFrames) {
        spawnMap.put(frame, new SpawnDetails("Alien1", BOARD_WIDTH, rowY));
    }

    // ───── Alien1: กระจายหลากหลาย ─────
    int[] spreadFrames = {600, 610, 620, 630};
    int[] spreadY = {80, 160, 240, 120};

    for (int i = 0; i < spreadFrames.length; i++) {
        spawnMap.put(spreadFrames[i], new SpawnDetails("Alien1", BOARD_WIDTH, spreadY[i]));
    }

    // ───── Alien1: แบบ delay ทยอย spawn ─────
    int delayY = 180;
    for (int i = 0; i < 5; i++) {
        int frame = 700 + (i * 20); // 700, 720, 740...
        spawnMap.put(frame, new SpawnDetails("Alien1", BOARD_WIDTH, delayY + i * 10));
    }
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
g.setColor(Color.CYAN);
g.setFont(new Font("Arial", Font.BOLD, 14));

g.drawString("Multishot Lv: " + player.getMultishotLevel(), 20, 40);
g.drawString("Speed Lv: " + player.getSpeedLevel(), 20, 60);
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
    g.setColor(Color.cyan);
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
    g.setColor(Color.RED);
    g.fillOval(centerX - 12, centerY - 1, 2, 2); // 2x2 pixel star
    g.setColor(Color.ORANGE);
    g.fillOval(centerX + 30, centerY +20, 3, 3); 
    g.setColor(Color.PINK);
    g.fillOval(centerX+10, centerY + 5, 4, 4);     
}
private void drawStar(Graphics g, int x, int y, int width, int height) {
    // Set star color to white  
    int centerX = x + width / 2;
    int centerY = y + height / 2;    
    g.setColor(Color.WHITE);
    g.fillOval(centerX+17, centerY - 50, 4, 4);
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
    g.drawString("STAGE " + (currentStage + 1), BOARD_WIDTH / 2 - 70, BOARD_HEIGHT / 2);
}


        if (inGame) {

            drawMap(g);  // Draw background stars first
            drawExplosions(g);
            drawPowreUps(g);
            drawAliens(g);
            drawPlayer(g);
            drawShot(g);
            drawEnemyBullets(g);

        } else {

            if (timer.isRunning()) {
                timer.stop();
            }

            gameOver(g);
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

private void update() {
    // ───── กลุ่มศัตรู wave-based ─────
   if (!waitingForNextGroup && currentGroupIndex < alienGroups.size()) {
    currentGroup = alienGroups.get(currentGroupIndex);
    enemies.addAll(currentGroup);
    waitingForNextGroup = true;
}

// ตรวจว่าศัตรูในกลุ่มตายหมดหรือยัง
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

            enemies.removeAll(currentGroup); // ✅ ล้างกลุ่มเดิมทิ้ง (ทุกชนิดศัตรู)
        }
    }
}



// ───── Spawn PowerUps จาก spawnMap ─────
SpawnDetails sd = spawnMap.get(frame);
if (sd != null) {
    switch (sd.type) {
        case "PowerUp-SpeedUp" -> powerups.add(new SpeedUp(sd.x, sd.y));
        case "PowerUp-MultiShot" -> powerups.add(new MultiShot(sd.x, sd.y));
        case "PowerUp-WeaponUpgrade" -> powerups.add(new WeaponUpgrade(sd.x, sd.y));
        case "Jeff" -> enemies.add(new Jeff(sd.x, sd.y));
        // ❌ ไม่ต้อง spawn Alien1/Alien2 ที่นี่แล้ว เพราะใช้ loadAlienGroups()
    }
}





    // ───── Player move ─────
    player.act();

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

                player.setDying(true);
                gameOverCountdown = 60;
            }
        }

        // ───── กระสุนของศัตรู (Alien1 และ Jeff) ─────
        if (enemy instanceof Alien1 alien1) {
            for (EnemyBullet bullet : alien1.getBullets()) {
                if (bullet.isVisible()) {
                    bullet.act();

                    if (bullet.collidesWith(player)) {
                        bullet.die();
                        player.setDying(true);
                        explosions.add(new Explosion(
                                player.getX(), player.getY(),
                                new ImageIcon(IMG_EXPLOSION).getImage()
                        ));
                        gameOverCountdown = 60;
                    }
                }
            }
        }
        // ───── Jeff bullet collision ─────
        else if (enemy instanceof Jeff jeff) {
            for (EnemyBullet bullet : jeff.getBullets()) {
                if (bullet.isVisible()) {
                    bullet.act();

                    if (bullet.collidesWith(player)) {
                        bullet.die();
                        player.setDying(true);
                        
                        // Create explosion at player position
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
                        gameOverCountdown = 60;
                    }
                }
            }
        }
    }

    // ───── ยิงทีละลำตามลำดับ ─────
    if (alienShootCooldown > 0) {
        alienShootCooldown--;
    } else {
        List<Alien1> aliveAliens = new ArrayList<>();
        for (Enemy e : enemies) {
            if (e instanceof Alien1 a && a.isVisible()) {
                aliveAliens.add(a);
            }
        }

        if (!aliveAliens.isEmpty()) {
            Alien1 shooter = aliveAliens.get(shooterIndex % aliveAliens.size());
            //shooter.fire(); 
            shooterIndex++;
            alienShootCooldown = SHOOT_DELAY; // เว้นจังหวะยิง
        }
    }

    // ───── Explosion update ─────
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

            if (shotX >= enemyX && shotX <= enemyX + ALIEN_WIDTH
                    && shotY >= enemyY && shotY <= enemyY + ALIEN_HEIGHT) {

                Image explosionImage = new ImageIcon(IMG_EXPLOSION).getImage();
                Image scaledExplosion = explosionImage.getScaledInstance(
                        explosionImage.getWidth(null) * 2,
                        explosionImage.getHeight(null) * 2,
                        Image.SCALE_SMOOTH);

                int centerX = enemyX + ALIEN_WIDTH / 2;
                int centerY = enemyY + ALIEN_HEIGHT / 2;

                explosions.add(new Explosion(
                        centerX - scaledExplosion.getWidth(null) / 2,
                        centerY - scaledExplosion.getHeight(null) / 2,
                        scaledExplosion
                ));

                enemy.setImage(explosionImage);
                enemy.setDying(true);
                deaths++;
                shot.die();
                shotsToRemove.add(shot);
            }
        }
    }

    shots.removeAll(shotsToRemove);


// ───── Stage Transition เมื่อ wave สุดท้ายผ่านไป ─────
if (!inTransition && currentGroupIndex >= alienGroups.size()) {
    inTransition = true;
    transitionTimer = TRANSITION_DURATION;
}

if (inTransition) {
    transitionTimer--;
    if (transitionTimer <= 0) {
        inTransition = false;
        currentStage++;                // ⬆️ Stage +1
        currentGroupIndex = 0;
        shooterIndex = 0;
        alienGroups.clear();          // ล้าง wave เดิม
        loadAlienGroups();            // โหลด wave ใหม่
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
                if (shots.size() < 4) {
                    int baseX = player.getX() + PLAYER_WIDTH;
                    int baseY = player.getY() + PLAYER_HEIGHT / 2;

                    int weapon = player.getWeaponType();
                    int level = player.getMultishotLevel();

                    if (weapon == 1) {
                        shots.add(new Shot(baseX, baseY - 10));
                        shots.add(new Shot(baseX, baseY));
                        shots.add(new Shot(baseX, baseY + 10));
                    } else {
                        switch (level) {
                            case 0 -> shots.add(new Shot(baseX, baseY));
                            case 1 -> {
                                shots.add(new Shot(baseX, baseY - 10));
                                shots.add(new Shot(baseX, baseY + 10));
                            }
                            case 2 -> {
                                shots.add(new Shot(baseX, baseY - 15));
                                shots.add(new Shot(baseX, baseY));
                                shots.add(new Shot(baseX, baseY + 15));
                            }
                            case 3 -> {
                                shots.add(new Shot(baseX, baseY - 20));
                                shots.add(new Shot(baseX, baseY - 10));
                                shots.add(new Shot(baseX, baseY));
                                shots.add(new Shot(baseX, baseY + 10));
                                shots.add(new Shot(baseX, baseY + 20));
                            }
                            case 4 -> {
                                for (int i = -30; i <= 30; i += 10) {
                                    shots.add(new Shot(baseX, baseY + i));
                                }
                            }
                        }
                    }
                }
            }
        }
    } 

}