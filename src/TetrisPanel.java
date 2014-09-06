
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * The master panel, handles the update/render calls, and catches all of the keyboard events.
 * @author Zack
 */
public class TetrisPanel extends JPanel implements KeyListener{
    public static TetrisPanel tetrisPanel;  //A reference to the panel, accessible from anywhere.
                                            //Used for switching the state. TetrisPanel.tetrisPanel.setState(new GameState);

    boolean firstFrame = true;
    int frameCount = 0;
    long targetFramerate = 1000/60, lastTime = 0, totalElapsedTime = 0, reportedFramerate = 0, elapsed = 0;
    Graphics bufferGraphics;
    Graphics2D consoleGraphics;
    Image console;
    Image offscreen;
    Dimension dim;
    GameState state;
    Font consoleFont;
    FontMetrics metrics;

    /**
     * Creates a new Master Panel.
     */
    public TetrisPanel() {
        setPreferredSize(new Dimension(400, 420));
        setSize(400, 420);
        dim = getSize();
        lastTime = System.currentTimeMillis();
        consoleFont = new Font(Font.MONOSPACED, Font.PLAIN, 14);


        setFocusable(true); //lets us listen for keyboard stuff.

        TetrisSoundManager.init();
        BitmapFontManager.init();
        BitmapFontManager.addBitmap("fixed", new ImageIcon(this.getClass().getResource("assets/fixedfont.png")).getImage());
        tetrisPanel = this; //oh WAAAAHHHHH Java. It's to give a reference to this class from anywhere. SHUT UP!

    }

    /**
     * Draws the frame.
     * @param g Graphics passed down from the Almighty Java VM™
     */
    @Override
    public void paint(Graphics g){
        elapsed = System.currentTimeMillis() - lastTime;
        lastTime = System.currentTimeMillis();
        if(firstFrame){
            addKeyListener(this);
            offscreen = createImage(dim.width, dim.height);
            bufferGraphics = offscreen.getGraphics();
            firstFrame = false;
            metrics = g.getFontMetrics(consoleFont);
        }else{
            bufferGraphics.clearRect(0, 0, dim.width, dim.height);

            state.render(bufferGraphics);


            bufferGraphics.setColor(new Color(10, 10, 10, 128));
            bufferGraphics.fillRect(0, 0, dim.width, dim.height);
            bufferGraphics.setColor(Color.white);
            bufferGraphics.setFont(consoleFont);
            String framerate = "FPS: " + reportedFramerate;
            bufferGraphics.drawString(framerate, dim.width - metrics.stringWidth("FPS: 000000000"), 10);

            bufferGraphics.setColor(Color.red);
            bufferGraphics.fillRect(75 + (int)(50*Math.sin((double)lastTime / (1000/3.14))), 50 + (int)(25*Math.sin((double)lastTime / (500/3.14))), 25, 25);

            g.drawImage(offscreen, 0, 0,this.getWidth(),this.getHeight(), this);
        }
        update(g);
    }

    /**
     * Updates the frame.
     * Why does this get a graphics g passed to it java? I don't understand.
     * @param g Graphics passed down from the Almighty Java VM™
     */
    @Override
    public void update(Graphics g){
        UpdateFPS();

        //At one point these were used to have the piece draw at the mouse position.
        //This is how you get the mouseX and mouseY in relation to the Top-Left of the
        //component.
        //
        //mouseX = MouseInfo.getPointerInfo().getLocation().x - getLocationOnScreen().x;
        //mouseY = MouseInfo.getPointerInfo().getLocation().y - getLocationOnScreen().y;

        state.update(elapsed);

        elapsed = System.currentTimeMillis() - lastTime;
        System.out.println(elapsed);

        try{
            if(elapsed < targetFramerate){
                Thread.sleep(targetFramerate - elapsed - 1); //Let's not have shit running at 2349 FPS, eh?
            }
        }catch(Throwable t){
            //
        }
        repaint();
    }

    /**
     * Updates the reported FPS.
     */
    private void UpdateFPS(){
        totalElapsedTime += elapsed;
        frameCount++;
        if(totalElapsedTime > 1000){
            reportedFramerate = (long)((double)frameCount / (double)totalElapsedTime*1000);
            frameCount = 0;
            totalElapsedTime -= 1000;
        }
    }

    /**
     * Sets the state to a new state.
     * @param newState the state that shall take over.
     */
    public void setState(GameState newState){
        if(state != null)
            state.destroy();
        state = newState;
    }

    /**
     * Unused
     * @param e
     */
    @Override
    public void keyTyped(KeyEvent e) {
        //
    }

    /**
     * Sends the key event to the GameState
     * @param e the Keyboard Event
     */
    @Override
    public void keyPressed(KeyEvent e) {
        state.keyPressed(e.getKeyCode());
    }

    /**
     * Unused
     * @param e
     */
    @Override
    public void keyReleased(KeyEvent e) {
        //
    }
}