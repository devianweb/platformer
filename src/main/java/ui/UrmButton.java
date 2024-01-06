package ui;

import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.URMButtons.URM_SIZE;
import static utils.Constants.UI.URMButtons.URM_SIZE_DEFAULT;

public class UrmButton extends PauseButton {
    private BufferedImage[] images;
    private int rowIndex, index;
    private boolean mouseOver, mousePressed;

    public UrmButton(int x, int y, int width, int height, int rowIndex) {
        super(x, y, width, height);
        this.rowIndex = rowIndex;
        loadImages();
    }

    private void loadImages() {
        var temp = LoadSave.getSpriteAtlas(LoadSave.URM_BUTTONS);
        images = new BufferedImage[3];
        for(int i = 0; i < images.length; i++) {
            images[i] = temp.getSubimage(i * URM_SIZE_DEFAULT, rowIndex * URM_SIZE_DEFAULT, URM_SIZE_DEFAULT, URM_SIZE_DEFAULT);
        }
    }

    public void update() {
        if (mouseOver) {
            index = 1;
        } else if (mousePressed) {
            index = 2;
        } else {
            index = 0;
        }
    }

    public void draw(Graphics g) {
        g.drawImage(images[index], x, y, URM_SIZE, URM_SIZE, null);
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }

    public boolean isMouseOver() {
        return mouseOver;
    }

    public void setMouseOver(boolean mouseOver) {
        this.mouseOver = mouseOver;
    }

    public boolean isMousePressed() {
        return mousePressed;
    }

    public void setMousePressed(boolean mousePressed) {
        this.mousePressed = mousePressed;
    }
}
