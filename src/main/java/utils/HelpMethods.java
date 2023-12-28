package utils;

import core.Game;

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
        if (x < 0 || x >= Game.GAME_WIDTH) {
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
}
