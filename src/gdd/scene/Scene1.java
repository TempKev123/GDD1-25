package gdd.scene;

import gdd.AudioPlayer;
import gdd.Game;
import static gdd.Global.*;
import gdd.SpawnDetails;
import gdd.powerup.MultiShot;
import gdd.powerup.PowerUp;
import gdd.powerup.SpeedUp;
import gdd.powerup.WeaponUpgrade;
import gdd.sprite.Alien1;
import gdd.sprite.Alien2;
import gdd.sprite.Jeff;
import gdd.sprite.Enemy;
import gdd.sprite.EnemyBullet;
import gdd.sprite.Explosion;
import gdd.sprite.Player;
import gdd.sprite.Shot;
import java.awt.Color;
import java.awt.Image;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
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

    private void loadSpawnDetails() {
        //there are 18600 frames in 5 minuites
        // TODO load this from a file
        /*spawnMap.put(51, new SpawnDetails("Jeff", 500, 1)); // spawn jeff1
        spawnMap.put(52, new SpawnDetails("Jeff", 500, 200)); // spawn jeff2
        spawnMap.put(53, new SpawnDetails("Jeff", 500, 450)); // spawn jeff3*/
        spawnMap.put(50, new SpawnDetails("PowerUp-SpeedUp", BOARD_WIDTH, 100)); // spawn ขวา
        spawnMap.put(350, new SpawnDetails("PowerUp-MultiShot", BOARD_WIDTH, 200));
        spawnMap.put(200, new SpawnDetails("Alien1", 100, 0));
        spawnMap.put(400, new SpawnDetails("Alien1", 300, 0));
        spawnMap.put(401, new SpawnDetails("Alien2", BOARD_WIDTH, 100));
        spawnMap.put(402, new SpawnDetails("Alien2", BOARD_WIDTH, 200));
        
        spawnMap.put(405, new SpawnDetails("Alien1", 400, 0));
        spawnMap.put(406, new SpawnDetails("Alien1", 450, 0));
        spawnMap.put(047, new SpawnDetails("Alien1", 500, 0));
        spawnMap.put(408, new SpawnDetails("Alien1", 550, 0));
        
        
        spawnMap.put(441, new SpawnDetails("Alien2", BOARD_WIDTH, 390));
        spawnMap.put(442, new SpawnDetails("Alien2", BOARD_WIDTH, 100));
        spawnMap.put(443, new SpawnDetails("Alien2", BOARD_WIDTH, 400));
        spawnMap.put(444, new SpawnDetails("Alien2", BOARD_WIDTH, 20));
        spawnMap.put(445, new SpawnDetails("PowerUp-SpeedUp", BOARD_WIDTH, 400));
        spawnMap.put(446, new SpawnDetails("Alien1", 200, 0));
        spawnMap.put(447, new SpawnDetails("Alien2", BOARD_WIDTH, 500));
        spawnMap.put(448, new SpawnDetails("Alien2", BOARD_WIDTH, 200));
        spawnMap.put(449, new SpawnDetails("Alien1", 300, 0));
        spawnMap.put(451, new SpawnDetails("Alien2", BOARD_WIDTH, 300));
        spawnMap.put(452, new SpawnDetails("Alien2", BOARD_WIDTH, 400));
        spawnMap.put(453, new SpawnDetails("Alien2", BOARD_WIDTH, 30));

        spawnMap.put(450, new SpawnDetails("PowerUp-WeaponUpgrade", BOARD_WIDTH, 150));
        spawnMap.put(458, new SpawnDetails("Alien1", 550, 0));
        spawnMap.put(448, new SpawnDetails("Alien1", 450, 0));
        spawnMap.put(459, new SpawnDetails("Alien1", 50, 0));
        spawnMap.put(460, new SpawnDetails("Alien1", 150, 0));

        spawnMap.put(500, new SpawnDetails("Alien1", 100, 0));
        spawnMap.put(501, new SpawnDetails("Alien1", 150, 0));
        spawnMap.put(502, new SpawnDetails("Alien1", 200, 0));
        spawnMap.put(503, new SpawnDetails("Alien1", 350, 0));
        spawnMap.put(600, new SpawnDetails("Alien2", BOARD_WIDTH, 100));
        spawnMap.put(700, new SpawnDetails("Alien2", BOARD_WIDTH, 200));
        spawnMap.put(730, new SpawnDetails("Alien2", BOARD_WIDTH, 200));
        spawnMap.put(750, new SpawnDetails("Alien2", BOARD_WIDTH, 200));

        spawnMap.put(800, new SpawnDetails("Alien1", 300, 0));
        spawnMap.put(801, new SpawnDetails("Alien1", 150, 0));
        spawnMap.put(802, new SpawnDetails("Alien1", 200, 0));
        spawnMap.put(803, new SpawnDetails("Alien1", 350, 0));
        spawnMap.put(805, new SpawnDetails("PowerUp-SpeedUp", BOARD_WIDTH, 300));




        //spawnMap.put(18600, new SpawnDetails("nextstage", BOARD_WIDTH, 200));


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
        // Set star color to white
        g.setColor(Color.PINK);

        // Draw multiple stars in a cluster pattern
        // Main star (larger)
        int centerX = x + width / 2;
        int centerY = y + height / 2;
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
    for (Enemy e : enemies) {
        if (e instanceof Alien1 alien1) {
            for (EnemyBullet b : alien1.getBullets()) {
                if (b.isVisible()) { // ใช้ตามระบบ Sprite
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
    // ┌──────────────┐
    // │ Spawn Entities │
    // └──────────────┘
    SpawnDetails sd = spawnMap.get(frame);
    if (sd != null) {
        switch (sd.type) {
            case "Alien1" -> enemies.add(new Alien1(sd.x, sd.y));
            case "Alien2" -> enemies.add(new Alien2(sd.x, sd.y));
            case "PowerUp-SpeedUp" -> powerups.add(new SpeedUp(sd.x, sd.y));
            case "PowerUp-MultiShot" -> powerups.add(new MultiShot(sd.x, sd.y));
            case "PowerUp-WeaponUpgrade" -> powerups.add(new WeaponUpgrade(sd.x, sd.y));
            default -> System.out.println("Unknown enemy type: " + sd.type);
        }
    }

    // ┌──────────────┐
    // │ Win Condition │
    // └──────────────┘
    if (deaths == NUMBER_OF_ALIENS_TO_DESTROY) {
        inGame = false;
        timer.stop();
        message = "Game won!";
    }

    // ┌──────────────┐
    // │ Player Update │
    // └──────────────┘
    player.act();

    // ┌───────────────┐
    // │ PowerUp Logic │
    // └───────────────┘
    for (PowerUp powerup : powerups) {
        if (powerup.isVisible()) {
            powerup.act();
            if (powerup.collidesWith(player)) {
                powerup.upgrade(player);
            }
        }
    }

    // ┌──────────────┐
    // │ Enemy Update │
    // └──────────────┘
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
                        scaledWidth, scaledHeight, Image.SCALE_SMOOTH
                );

                explosions.add(new Explosion(
                        centerX - scaledWidth / 2,
                        centerY - scaledHeight / 2,
                        scaledExplosion
                ));

                player.setDying(true);
                gameOverCountdown = 60;
            }
        }

        // ┌────────────────────────────────────────┐
        // │ Alien1 ยิงกระสุนแนวตรง (ไม่เล็งผู้เล่น) │
        // └────────────────────────────────────────┘
        if (enemy instanceof Alien1 alien1) {
            if (frame % 60 == 0) {
                alien1.fire(); // 🔫 ยิงกระสุนแนวตรง
            }

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
    }

    // ┌────────────────┐
    // │ Explosion Timer │
    // └────────────────┘
    for (Explosion explosion : explosions) {
        explosion.act();
    }

    // ┌────────────────────┐
    // │ Game Over Countdown │
    // └────────────────────┘
    if (gameOverCountdown > 0) {
        gameOverCountdown--;
        if (gameOverCountdown == 0) {
            inGame = false;
        }
    }

    // ┌────────────────────────┐
    // │ Player Shots vs Enemies │
    // └────────────────────────┘
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
            int enemyX = enemy.getX();
            int enemyY = enemy.getY();

            if (enemy.isVisible()
                    && shotX >= enemyX && shotX <= enemyX + ALIEN_WIDTH
                    && shotY >= enemyY && shotY <= enemyY + ALIEN_HEIGHT) {

                Image explosionImage = new ImageIcon(IMG_EXPLOSION).getImage();
                Image scaledExplosion = explosionImage.getScaledInstance(
                        explosionImage.getWidth(null) * 2,
                        explosionImage.getHeight(null) * 2,
                        Image.SCALE_SMOOTH
                );

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