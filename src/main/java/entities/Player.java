package entities;

import core.Game;
import utils.Constants.*;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.HelpMethods.*;

public class Player extends Entity {

    private BufferedImage[][] animations;
    private int animationTick, animationIndex, animationSpeed = 15;
    private int playerAction = PlayerConstants.IDLE;
    private boolean moving = false, attacking = false;
    private boolean left, right, up, down, jump;
    private final float playerSpeed = 1.0f * Game.SCALE;
    private int[][] levelData;
    private float xDrawOffset = 21 * Game.SCALE;
    private float yDrawOffset = 4 * Game.SCALE;

    //jumping || gravity
    private float airSpeed = 0f;
    private float gravity = 0.04f * Game.SCALE;
    private float jumpSpeed = -2.25f * Game.SCALE;
    private float fallSpeedAfterCollionsion = 0.5f * Game.SCALE;
    private boolean inAir = false;



    public Player(float x, float y, int width, int height) {
        super(x, y, width, height);
        loadAnimations();
        initialiseHitbox(x, y, (int) (20 * Game.SCALE), (int) (27 * Game.SCALE));
    }

    public void update() {
        updateAnimationTick();
        setAnimation();
        updatePosition();
    }

    public void render(Graphics g) {
        g.drawImage(animations[playerAction][animationIndex], (int) (hitbox.x - xDrawOffset), (int) (hitbox.y - yDrawOffset), width, height, null);
        drawHitbox(g);
    }

    private void updatePosition() {
        moving = false;

        if (jump) {
            jump();
        }
        
        if(!left && !right && !inAir) {
            return;
        }

        if(!inAir) {
            if(!isEntityOnFloor(hitbox, levelData)) {
                inAir = true;
            }
        }

        float xSpeed = 0;

        if (left) xSpeed -= playerSpeed;
        if (right) xSpeed += playerSpeed;

        if (inAir) {
            if(canMoveHere(hitbox.x, hitbox.y + airSpeed, hitbox.width, hitbox.height, levelData)) {
                hitbox.y += airSpeed;
                airSpeed += gravity;
                updateXPos(xSpeed);
            } else {
                hitbox.y = getEntityYPosUnderRoofOrAboveFloor(hitbox, airSpeed);
                if (airSpeed > 0) {
                    resetInAir();
                } else {
                    airSpeed = fallSpeedAfterCollionsion;
                }
                updateXPos(xSpeed);
            }
        } else {
            updateXPos(xSpeed);
        }

        if (playerSpeed > 0) {
            moving = true;
        }
    }

    private void jump() {
        if (inAir) {
            return;
        }
        inAir = true;
        airSpeed = jumpSpeed;
    }

    private void resetInAir() {
        inAir = false;
        airSpeed = 0;
    }

    private void updateXPos(float xSpeed) {
        if (canMoveHere(hitbox.x + xSpeed, hitbox.y, hitbox.width, hitbox.height, levelData)) {
            hitbox.x += xSpeed;
        } else {
            hitbox.x = getEntityXPosNextToWall(hitbox, xSpeed);
        }
    }

    private void setAnimation() {
        int startAnimation = playerAction;

        if (moving) {
            playerAction = PlayerConstants.RUNNING;
        } else {
            playerAction = PlayerConstants.IDLE;
        }

        if (inAir) {
            if (airSpeed < 0) {
                playerAction = PlayerConstants.JUMP;
            } else {
                playerAction = PlayerConstants.FALLING;
            }
        }

        if (attacking) {
            playerAction = PlayerConstants.ATTACK_1;
        }

        if (startAnimation != playerAction) {
            resetAnimation();
        }
    }

    private void resetAnimation() {
        animationTick = 0;
        animationIndex = 0;
    }

    private void updateAnimationTick() {
        animationTick++;
        if(animationTick >= animationSpeed) {
            animationIndex++;
            animationTick = 0;
            if (animationIndex >= PlayerConstants.getSpriteAmount(playerAction)) {
                animationIndex = 0;
                attacking = false;
            }
        }
    }

    private void loadAnimations() {
         var img = LoadSave.getSpriteAtlas(LoadSave.PLAYER_ATLAS);
        animations = new BufferedImage[9][6];

        for(int j = 0; j < animations.length; j++) {
            for(int i = 0; i < animations[j].length; i++) {
                animations[j][i] = img.getSubimage(i * 64, j * 40, 64, 40);
            }
        }
    }

    public void loadLevelData(int[][] levelData) {
        this.levelData = levelData;
        if (!isEntityOnFloor(hitbox, levelData)) {
            inAir = true;
        }
    }

    public boolean isLeft() {
        return left;
    }

    public void setLeft(boolean left) {
        this.left = left;
    }

    public boolean isRight() {
        return right;
    }

    public void setRight(boolean right) {
        this.right = right;
    }

    public boolean isUp() {
        return up;
    }

    public void setUp(boolean up) {
        this.up = up;
    }

    public boolean isDown() {
        return down;
    }

    public void setDown(boolean down) {
        this.down = down;
    }

    public boolean isAttacking() {
        return attacking;
    }

    public void setAttacking(boolean attacking) {
        this.attacking = attacking;
    }

    public void setJump(boolean jump) {
        this.jump = jump;
    }

    public void resetDirectionBooleans() {
        left = false;
        right = false;
        up = false;
        down = false;
    }
}
