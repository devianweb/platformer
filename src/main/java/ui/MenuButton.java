package ui;

import gamestates.Gamestate;
import utils.LoadSave;

import java.awt.*;
import java.awt.image.BufferedImage;

import static utils.Constants.UI.Buttons.*;

public class MenuButton {
    private int xPos, yPos, rowIndex, index;

    private Gamestate gamestate;
    private BufferedImage[] images;
    private boolean mouseOver;
    private boolean mousePressed;

    public Rectangle getBounds() {
        return bounds;
    }

    private Rectangle bounds;

    public MenuButton(int xPos, int yPos, int rowIndex, Gamestate gamestate) {
        this.xPos = xPos;
        this.yPos = yPos;
        this.rowIndex = rowIndex;
        this.gamestate = gamestate;
        loadImages();
        initialiseBounds();
    }

    private void initialiseBounds() {
        bounds = new Rectangle(xPos - B_WIDTH_OFFSET, yPos, B_WIDTH, B_HEIGHT);
    }

    private void loadImages() {
        images = new BufferedImage[3];
        BufferedImage temp = LoadSave.getSpriteAtlas(LoadSave.MENU_BUTTONS);
        for (int i = 0; i < images.length; i++) {
            images[i] = temp.getSubimage(
                    i * B_WIDTH_DEFAULT,
                    rowIndex * B_HEIGHT_DEFAULT,
                    B_WIDTH_DEFAULT,
                    B_HEIGHT_DEFAULT);
        }
    }

    public void draw(Graphics g) {
        g.drawImage(images[index], xPos - B_WIDTH_OFFSET, yPos, B_WIDTH, B_HEIGHT, null);
    }

    public void update() {
        index = 0;
        if (mouseOver) {
            index = 1;
        }
        if (mousePressed) {
            index = 2;
        }
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

    public void applyGameState() {
        Gamestate.state = gamestate;
    }

    public void resetBools() {
        mouseOver = false;
        mousePressed = false;
    }
}
