package utils;

import core.Game;

import java.awt.geom.Rectangle2D;
import java.util.Arrays;

public class HelpMethods {

    public static boolean canMoveHere(float x, float y, float width, float height, int[][] levelData) {
        if(isSolid(x, y, levelData)) {
            return false;
        } else if (isSolid(x + width, y + height, levelData)) {
            return false;
        } else if (isSolid(x + width, y, levelData)) {
            return false;
        } else if (isSolid(x, y + height, levelData)) {
            return false;
        }

        return true;
    }

    private static boolean isSolid(float x, float y, int[][] levelData) {
        int maxWidth = Arrays.stream(levelData).findFirst().map(width -> width.length * Game.TILES_SIZE).orElse(0);
        if (x < 0 || x >= maxWidth) {
            return true;
        } else if (y < 0 || y >= Game.GAME_HEIGHT) {
            return true;
        }

        var xIndex = x / Game.TILES_SIZE;
        var yIndex = y / Game.TILES_SIZE;
        var value = levelData[(int) yIndex][(int) xIndex];

        if (value >= 48 || value < 0 || value != 11) {
            return true;
        } else {
            return false;
        }
    }

    public static float getEntityXPosNextToWall(Rectangle2D.Float hitbox, float xSpeed) {
        int currentTile = (int) (hitbox.x / Game.TILES_SIZE);
        if (xSpeed > 0) {
            int tileXPos = currentTile * Game.TILES_SIZE;
            int xOffset = (int) (Game.TILES_SIZE - hitbox.width);
            return tileXPos + xOffset - 1;
        } else {
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static float getEntityYPosUnderRoofOrAboveFloor(Rectangle2D.Float hitbox, float airSpeed) {
        int currentTile = (int) (hitbox.y / Game.TILES_SIZE);
        if (airSpeed > 0) {
            int tileYPos = currentTile * Game.TILES_SIZE;
            int yOffset = (int) (Game.TILES_SIZE - hitbox.height);
            return tileYPos + yOffset - 1;
        } else {
            return currentTile * Game.TILES_SIZE;
        }
    }

    public static boolean isEntityOnFloor(Rectangle2D.Float hitbox, int[][] levelData) {
        if(isSolid(hitbox.x, hitbox.y + hitbox.height + 1, levelData)) {
           return true;
        } else if (isSolid(hitbox.x + hitbox.width, hitbox.y + hitbox.height, levelData)){
           return true;
        }

        return false;
    }
}
