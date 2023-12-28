package core;

import inputs.KeyboardInputs;
import inputs.MouseInputs;

import javax.swing.*;
import java.awt.*;


public class GamePanel extends JPanel {

    private MouseInputs mouseInputs;
    private Game game;


    public GamePanel(Game game) {
        this.game = game;
        this.mouseInputs = new MouseInputs(this);
        setPanelSize();
        addKeyListener(new KeyboardInputs(this));
        addMouseListener(mouseInputs);
        addMouseMotionListener(mouseInputs);
    }

    private void setPanelSize() {
        Dimension size = new Dimension(1280, 800);
        setPreferredSize(size);
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        game.render(g);
    }

    public void updateGame() {

    }

    public Game getGame() {
        return game;
    }
}
