package inputs;

import core.GamePanel;
import utils.Constants.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class KeyboardInputs implements KeyListener {

    private GamePanel gamePanel;

    public KeyboardInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W -> gamePanel.getGame().getPlayer().setDirection(Directions.UP);
            case KeyEvent.VK_A -> gamePanel.getGame().getPlayer().setDirection(Directions.LEFT);
            case KeyEvent.VK_S -> gamePanel.getGame().getPlayer().setDirection(Directions.DOWN);
            case KeyEvent.VK_D -> gamePanel.getGame().getPlayer().setDirection(Directions.RIGHT);
            default -> System.out.println("unrecognised key");
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_W, KeyEvent.VK_A, KeyEvent.VK_S, KeyEvent.VK_D
                    -> gamePanel.getGame().getPlayer().setMoving(false);
        }
    }
}
