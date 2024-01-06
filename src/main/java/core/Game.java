package core;


import entities.Player;
import gamestates.Gamestate;
import gamestates.Playing;
import gamestates.Menu;
import levels.LevelManager;

import java.awt.*;

public class Game implements Runnable {

    private final int FPS_SET = 120;
    private final int UPS_SET = 200;

    private GameWindow gameWindow;
    private GamePanel gamePanel;
    private Thread gameThread;
    private Playing playing;
    private Menu menu;

    public static final float SCALE = 2f;
    public static final int TILES_DEFAULT_SIZE = 32;
    public static final int TILES_IN_WIDTH = 26;
    public static final int TILES_IN_HEIGHT = 14;
    public static final int TILES_SIZE = (int) (TILES_DEFAULT_SIZE * SCALE);
    public static final int GAME_WIDTH = TILES_SIZE * TILES_IN_WIDTH;
    public static final int GAME_HEIGHT = TILES_SIZE * TILES_IN_HEIGHT;


    public Game() {
        initialiseClasses();
        this.gamePanel = new GamePanel(this);
        this.gameWindow = new GameWindow(gamePanel);
        this.gamePanel.requestFocus();
        startGameLoop();
    }

    private void initialiseClasses() {
        menu = new Menu(this);
        playing = new Playing(this);
    }

    private void startGameLoop() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    public void update() {
        switch(Gamestate.state) {
            case MENU -> {
                menu.update();
            }
            case PLAYING -> {
                playing.update();
            }
        }
    }

    public void render(Graphics g) {
        switch(Gamestate.state) {
            case MENU -> {
                menu.draw(g);
            }
            case PLAYING -> {
                playing.draw(g);
            }
            default -> System.exit(0);
        }
    }

    @Override
    public void run() {
        double timePerFrame = 1000000000.0 / FPS_SET;
        double timePerUpdate = 1000000000.0 / UPS_SET;

        int frames = 0;
        int updates = 0;
        long lastCheck = System.currentTimeMillis();
        long previousTime = System.nanoTime();

        double deltaU = 0;
        double deltaF = 0;

        while (true) {
            var currentTime = System.nanoTime();

            deltaU += (currentTime - previousTime) / timePerUpdate;
            deltaF += (currentTime - previousTime) / timePerFrame;

            previousTime = currentTime;

            if(deltaU >= 1) {
                update();
                updates++;
                deltaU--;
            }

            if (deltaF >= 1) {
                gamePanel.repaint();
                frames++;
                deltaF--;
            }

            if (System.currentTimeMillis() - lastCheck >= 1000) {
                lastCheck = System.currentTimeMillis();
                System.out.println("FPS: " + frames + " | UPS: " + updates);
                frames = 0;
                updates = 0;
            }
        }
    }

    public void windowFocusLost() {
        if(Gamestate.state == Gamestate.PLAYING) {
            playing.getPlayer().resetDirectionBooleans();
        }
    }

    public Playing getPlaying() {
        return playing;
    }

    public Menu getMenu() {
        return menu;
    }
}
