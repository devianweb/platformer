package core;

import inputs.KeyboardInputs;
import inputs.MouseInputs;
import utils.Constants.*;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private int xDelta = 100, yDelta = 100;
    private BufferedImage img, subImage;
    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 15;
    private int playerAction = PlayerConstants.IDLE;
    private int playerDirection = -1;
    private boolean moving = false;


    public GamePanel() {
        importImg();
        loadAnimations();

        mouseInputs = new MouseInputs(this);
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void loadAnimations() {
        animations = new BufferedImage[9][6];

        for(int j = 0; j < animations.length; j++) {
            for(int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            }
        }

    }

    private void importImg() {
        InputStream is = getClass().getResourceAsStream("/player_sprites.png");
        try {
            img = ImageIO.read(is);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setPreferredSize(size);
    }

    public void setDirection(int direction) {
        this.playerDirection = direction;
        moving = true;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.drawImage(animations[playerAction][animationIndex], xDelta, yDelta, 256, 160, null);

    }

    private void updatePosition() {
        if (moving) {
            switch (playerDirection) {
                case Directions.LEFT -> xDelta -= 5;
                case Directions.UP -> yDelta -= 5;
                case Directions.RIGHT -> xDelta += 5;
                case Directions.DOWN -> yDelta += 5;
            }
        }
    }

    private void setAnimation() {
        if (moving) {
            playerAction = PlayerConstants.RUNNING;
        } else {
            playerAction = PlayerConstants.IDLE;
        }
    }

    private void updateAnimationTick() {
        animationTick++;
        if(animationTick >= animationSpeed) {
            animationIndex++;
            animationTick = 0;
            if (animationIndex >= PlayerConstants.getSpriteAmount(playerAction)) {
                animationIndex = 0;
            }
        }
    }

    public void updateGame() {
        updateAnimationTick();
        setAnimation();
        updatePosition();
    }
}
