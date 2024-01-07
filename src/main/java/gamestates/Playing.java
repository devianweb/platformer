package gamestates;

import core.Game;
import entities.Player;
import levels.LevelManager;
import ui.PauseOverlay;
import utils.LoadSave;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.Random;

import static utils.Constants.Environment.*;

public class Playing extends State implements StateMethods{

    private Player player;
    private LevelManager levelManager;
    private PauseOverlay pauseOverlay;
    private boolean paused = false;

    private int xLevelOffset;
    private int leftBorder = (int) (0.2 * Game.GAME_WIDTH);
    private int rightBorder = (int) (0.8 * Game.GAME_WIDTH);
    private int levelTilesWide = LoadSave.getLevelData()[0].length;
    private int maxTilesOffset = levelTilesWide - Game.TILES_IN_WIDTH;
    private int maxLevelOffsetX = maxTilesOffset * Game.TILES_SIZE;

    private BufferedImage backgroundImage, bigCloud, smallCloud;
    private int[] smallCloudsPos;
    private Random rnd = new Random();

    private float bigCloudXPosition = 0;
    private final float bigCloudSpeed = 0.05f * Game.SCALE;

    private float smallCloudXOffset = (float) (Game.GAME_WIDTH - (2 * SMALL_CLOUD_WIDTH)) / 2;
    private float smallCloudXPosition = 0f;
    private final float smallCloudSpeed = 0.2f * Game.SCALE;



    public Playing(Game game) {
        super(game);
        initialiseClasses();

        backgroundImage = LoadSave.getSpriteAtlas(LoadSave.PLAYING_BACKGROUND_IMAGE);
        bigCloud = LoadSave.getSpriteAtlas(LoadSave.BIG_CLOUDS);
        smallCloud = LoadSave.getSpriteAtlas(LoadSave.SMALL_CLOUDS);
        smallCloudsPos = new int[4];
        for (int i = 0; i < smallCloudsPos.length / 2; i++) {
            smallCloudsPos[i] = (int) (90 * Game.SCALE + rnd.nextInt((int) (100 * Game.SCALE)));
        }
        for (int i = 0; i < smallCloudsPos.length / 2; i++) {
            smallCloudsPos[i + 2] = smallCloudsPos[i];
        }
    }

    private void initialiseClasses() {
        levelManager = new LevelManager(game);
        player = new Player(200, 200, (int) (64 * Game.SCALE), (int) (40 * Game.SCALE));
        player.loadLevelData(levelManager.getCurrentLevel().getLevelData());
        pauseOverlay = new PauseOverlay(this);
    }

    @Override
    public void update() {
        if (!paused) {
            levelManager.update();
            player.update();
            checkCloseToBorder();
            updateCloudTick();
        } else {
            pauseOverlay.update();
        }
    }

    private void checkCloseToBorder() {
        int playerX = (int) player.getHitbox().x;
        int diff = playerX - xLevelOffset;

        if (diff > rightBorder) {
            xLevelOffset += diff - rightBorder;
        } else if (diff < leftBorder) {
            xLevelOffset += diff - leftBorder;
        }

        if (xLevelOffset > maxLevelOffsetX) {
            xLevelOffset = maxLevelOffsetX;
        } else if (xLevelOffset < 0) {
            xLevelOffset = 0;
        }
    }


    @Override
    public void draw(Graphics g) {
        g.drawImage(backgroundImage, 0, 0, Game.GAME_WIDTH, Game.GAME_HEIGHT, null);
        drawClouds(g);
        levelManager.draw(g, xLevelOffset);
        player.render(g, xLevelOffset);

        if (paused) {
            g.setColor(new Color(0,0,0,150));
            g.fillRect(0,0, Game.GAME_WIDTH, Game.GAME_HEIGHT);
            pauseOverlay.draw(g);
        }
    }

    private void drawClouds(Graphics g) {
        for(int i = 0; i < smallCloudsPos.length; i++) {
            g.drawImage(smallCloud, (int) ((-((SMALL_CLOUD_WIDTH * 2) + smallCloudXOffset)) + (SMALL_CLOUD_WIDTH + smallCloudXOffset) * i - smallCloudXPosition), smallCloudsPos[i], SMALL_CLOUD_WIDTH, SMALL_CLOUD_HEIGHT, null);
        }

        for(int i = 0; i < 4; i++) {
            g.drawImage(bigCloud, (int) (-BIG_CLOUD_WIDTH + i * BIG_CLOUD_WIDTH - bigCloudXPosition), (int) (204 * Game.SCALE), BIG_CLOUD_WIDTH, BIG_CLOUD_HEIGHT, null);
        }
    }

    private void updateCloudTick() {
        bigCloudXPosition -= bigCloudSpeed;
        smallCloudXPosition -= smallCloudSpeed;

        if (bigCloudXPosition < -BIG_CLOUD_WIDTH) {
            bigCloudXPosition = 0f;
        } else if (smallCloudXPosition < -((SMALL_CLOUD_WIDTH + smallCloudXOffset) * 2)) {
            smallCloudXPosition = 0;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON1) {
            player.setAttacking(true);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (paused) {
            pauseOverlay.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseReleased(e);
        }
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        if (paused) {
            pauseOverlay.mouseMoved(e);
        }
    }

    public void mouseDragged(MouseEvent e) {
        if(paused) {
            pauseOverlay.mouseDragged(e);
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.setLeft(true);
            case KeyEvent.VK_D -> player.setRight(true);
            case KeyEvent.VK_SPACE -> player.setJump(true);
            case KeyEvent.VK_ESCAPE -> paused = !paused;
            default -> System.out.println("unrecognised key");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A -> player.setLeft(false);
            case KeyEvent.VK_D -> player.setRight(false);
            case KeyEvent.VK_SPACE -> player.setJump(false);
        }
    }

    public Player getPlayer() {
        return player;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }

    public void windowFocusLost() {
        player.resetDirectionBooleans();
    }
}
