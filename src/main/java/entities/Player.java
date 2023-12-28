package entities;

import utils.Constants;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 15;
    private int playerAction = Constants.PlayerConstants.IDLE;
    private int playerDirection = -1;
    private boolean moving = false;


    public Player(float x, float y) {
        super(x, y);
        loadAnimations();
    }

    public void update() {
        updateAnimationTick();
        setAnimation();
        updatePosition();
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][animationIndex], (int) x, (int) y, 256, 160, null);
    }

    public void setDirection(int direction) {
        this.playerDirection = direction;
        moving = true;
    }

    public void setMoving(boolean moving) {
        this.moving = moving;
    }

    private void updatePosition() {
        if (moving) {
            switch (playerDirection) {
                case Constants.Directions.LEFT -> this.x -= 5;
                case Constants.Directions.UP -> this.y -= 5;
                case Constants.Directions.RIGHT -> this.x += 5;
                case Constants.Directions.DOWN -> this.y += 5;
            }
        }
    }

    private void setAnimation() {
        if (moving) {
            playerAction = Constants.PlayerConstants.RUNNING;
        } else {
            playerAction = Constants.PlayerConstants.IDLE;
        }
    }

    private void updateAnimationTick() {
        animationTick++;
        if(animationTick >= animationSpeed) {
            animationIndex++;
            animationTick = 0;
            if (animationIndex >= Constants.PlayerConstants.getSpriteAmount(playerAction)) {
                animationIndex = 0;
            }
        }
    }

    private void loadAnimations() {
        InputStream is = getClass().getResourceAsStream("/player_sprites.png");
        try {
            var img = ImageIO.read(is);

            animations = new BufferedImage[9][6];

            for(int j = 0; j < animations.length; j++) {
                for(int i = 0; i < animations[j].length; i++) {
                    animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
                }
            }
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
}
