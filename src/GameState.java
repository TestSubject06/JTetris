
import java.awt.Graphics;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 * An abstract game state class
 * @author Zack
 */
public abstract class GameState {

    /**
     * Updates the state of the Game State
     * @param elapsed how much time has passed since the last frame
     */
    public abstract void update(long elapsed);
    /**
     * Renders the current state of the Game State onto the graphic g
     * @param g the image you want the MainMenu to draw to
     */
    public abstract void render(Graphics g);
    /**
     * Called to pass down Keyboard Events from the master Panel.
     * @param keyCode the key that was pressed.
     */
    public abstract void keyPressed(int keyCode);
    /**
     * Destroys the state.
     */
    public abstract void destroy();
}
