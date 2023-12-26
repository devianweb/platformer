package core;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;

    private int xDelta = 100;
    private int yDelta = 100;
    private int xDir = 1;
    private int yDir = 1;
    private Random random;
    private Color color;

    public GamePanel() {
        random = new Random();
        mouseInputs = new MouseInputs(this);
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    public void changeXDelta(int value) {
        this.xDelta += value;
    }

    public void changeYDelta(int value) {
        this.yDelta += value;
    }

    public void setRectPos(int x, int y) {
        this.xDelta = x;
        this.yDelta = y;
        repaint();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        updateRectangle();
        g.setColor(color);
        g.fillRect(xDelta, yDelta,200,50);
    }

    private void updateRectangle() {
        xDelta += xDir;
        yDelta += yDir;
        if (xDelta > 400 || xDelta < 0) {
            xDir*=-1;
            this.color = getRndColor();
        }
        if (yDelta > 400 || yDelta < 0) {
            yDir*=-1;
            this.color = getRndColor();
        }

        yDelta += yDir;
    }

    private Color getRndColor() {
        int r = random.nextInt(255);
        int b = random.nextInt(255);
        int g = random.nextInt(255);

        return new Color(r, b, g);
    }
}
