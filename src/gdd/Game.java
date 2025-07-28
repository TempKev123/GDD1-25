package gdd;

import gdd.scene.Scene1;
import gdd.scene.Scene2;
import gdd.scene.TitleScene;
import javax.swing.JFrame;

public class Game extends JFrame  {

    TitleScene titleScene;
    Scene1 scene1;
    Scene2 scene2;

    public Game() {
        titleScene = new TitleScene(this);
        System.out.println("Game constructor called");
        scene1 = new Scene1(this);
        scene2 = new Scene2(this);
        initUI();
        loadTitle();
        System.out.println("Game initialized with TitleScene");
    }

    private void initUI() {

        setTitle("STAR EMBERS");
        System.out.println("Initializing UI...");
        setSize(Global.BOARD_WIDTH, Global.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

    }

    public void loadTitle() {
        System.out.println("Loading Title Scene...");
        getContentPane().removeAll();
        // add(new Title(this));
        add(titleScene);
        titleScene.start();
        revalidate();
        repaint();
    }

    public void loadScene1() {
        System.out.println("Loading Scene 1...");
        // ....
        System.out.println("Loading Scene 1... inside LoadScene1");
    }

    public void loadScene2() {
        System.out.println("Loading Scene 2...");
        getContentPane().removeAll();
        add(scene1);
        titleScene.stop();
        scene1.start();
        revalidate();
        repaint();
    }

    public void loadScene3() {
        System.out.println("Loading Scene 3...");
        getContentPane().removeAll();
        add(scene2);
        titleScene.stop();
        scene2.start();
        revalidate();
        repaint();
    }
}