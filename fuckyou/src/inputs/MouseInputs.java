package inputs;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import gamestates.Gamestate;
import main.GamePanel;

public class MouseInputs implements MouseListener, MouseMotionListener {

    private GamePanel gamePanel;

    public MouseInputs(GamePanel gamePanel) {
        this.gamePanel = gamePanel;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseMoved(e);
                break;
            case TICTAC:
                gamePanel.getGame().getTicTac().mouseMoved(e);
                break;
            // Add other cases if necessary
            default:
                break;
        }
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseClicked(e);
                break;
            case TICTAC:
                gamePanel.getGame().getTicTac().mouseClicked(e);
                break;
            // Add other cases if necessary
            default:
                break;
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mousePressed(e);
                break;
            case TICTAC:
                gamePanel.getGame().getTicTac().mousePressed(e);
                break;
            // Add other cases if necessary
            default:
                break;
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        switch (Gamestate.state) {
            case MENU:
                gamePanel.getGame().getMenu().mouseReleased(e);
                break;
            case TICTAC:
                gamePanel.getGame().getTicTac().mouseReleased(e);
                break;
            // Add other cases if necessary
            default:
                break;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // Implement as needed
    }

    @Override
    public void mouseExited(MouseEvent e) {
        // Implement as needed
    }
}
