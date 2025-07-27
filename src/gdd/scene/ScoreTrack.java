package gdd.scene;


public class ScoreTrack {
    // A single, static instance of ScoreTracker
    public static ScoreTrack instance = new ScoreTrack(); // <-- This line creates the single instance

    private int currentScore = 0;

    // Private constructor to prevent other classes from creating new instances
    private ScoreTrack() {
        System.out.println("ScoreTracker instance created (via static initialization).");
    }

    public void addScore(int points) {
        currentScore += points;
    }

    public int getScore() {
        return currentScore;
    }
}