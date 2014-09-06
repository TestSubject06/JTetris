
import javax.swing.JFrame;
/**
 * TETRIIISSSS
 * @author Zack
 * @version 1.00
 */
public class Tetris {

    /**
     * Entry Point.
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        JFrame frame = new JFrame(randomString());
        TetrisPanel panel = new TetrisPanel();
        panel.setState(new MainMenu());
        frame.add(panel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);
        frame.pack();
    }

    /**
     * returns a random string.
     * @return a random string
     */
    private static String randomString(){
        int i = (int)(Math.random()*3);
        switch(i){
            case 0:
                return "Tetris!";
            case 1:
                return "Russian games at their peak.";
            case 2:
                return "From not-russia with fun!";
        }
        return "Hurrrrrr";
    }
}