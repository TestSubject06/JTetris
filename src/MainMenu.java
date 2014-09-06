
import java.awt.Graphics;
import java.awt.Image;
import java.io.*;
import javax.swing.ImageIcon;

/*
 * The Main Menu state. Handles the options screen, as well as the initial game loading.
 */
public class MainMenu extends GameState{

    movingIcon T, E, T2, R, I, S, backboard;
    boolean hasT = false, hasT2 = false, hasE = false, hasS = false, hasI = false, hasR = false, hasBB = false, isAnimating = true, loading = false, options = false, config = false;
    int timer = 0, menuSelection = 0;
    String musicState = "ON";
    String soundState = "ON";
    int configProgress = 0;
    ImageIcon backdrop;
    int[] responses;

    /**
     * Creates and initializes the main menu
     */
    public MainMenu() {
        backdrop = new ImageIcon(this.getClass().getResource("assets/mmbackdrop.png"));
        T = new movingIcon(new ImageIcon(this.getClass().getResource("assets/t.png")), 40, -60);
        E = new movingIcon(new ImageIcon(this.getClass().getResource("assets/e.png")), 95, -60);
        T2 = new movingIcon(new ImageIcon(this.getClass().getResource("assets/t2.png")), 150, -60);
        R = new movingIcon(new ImageIcon(this.getClass().getResource("assets/r.png")), 205, -60);
        I = new movingIcon(new ImageIcon(this.getClass().getResource("assets/i.png")), 260, -60);
        S = new movingIcon(new ImageIcon(this.getClass().getResource("assets/s.png")), 315,-60);
        backboard = new movingIcon(new ImageIcon(this.getClass().getResource("assets/titleBoard.png")),34,-60);
        responses = new int[]{37,39,88,90,40,38,10};
        loadConfig();
    }

    /**
     * Renders the current state of the Main Menu onto the graphic g
     * @param g the image you want the MainMenu to draw to
     */
    @Override
    public void render(Graphics g){
        g.drawImage(backdrop.getImage(), 0, 0, null);
        g.drawImage(backboard.image, backboard.x, backboard.y, null);
        g.drawImage(T.image, T.x, T.y, null);
        g.drawImage(E.image, E.x, E.y, null);
        g.drawImage(T2.image, T2.x, T2.y, null);
        g.drawImage(R.image, R.x, R.y, null);
        g.drawImage(I.image, I.x, I.y, null);
        g.drawImage(S.image, S.x, S.y, null);
        if(!isAnimating && !loading && !options){
            if(menuSelection == 0)
                BitmapFontManager.drawString("fixed", 115, 125, ".NEW  GAME", g);
            else
                BitmapFontManager.drawString("fixed", 115, 125, " NEW  GAME", g);
            if(menuSelection == 1)
                BitmapFontManager.drawString("fixed", 115, 150, ".LOAD GAME", g);
            else
                BitmapFontManager.drawString("fixed", 115, 150, " LOAD GAME", g);
            if(menuSelection == 2)
                BitmapFontManager.drawString("fixed", 115, 175, ".OPTIONS", g);
            else
                BitmapFontManager.drawString("fixed", 115, 175, " OPTIONS", g);
            if(menuSelection == 3)
                BitmapFontManager.drawString("fixed", 115, 225, ".EXIT", g);
            else
                BitmapFontManager.drawString("fixed", 115, 225, " EXIT", g);
        }if(loading){
            if(menuSelection == 0)
                BitmapFontManager.drawString("fixed", 115, 125, ".SLOT 1", g);
            else
                BitmapFontManager.drawString("fixed", 115, 125, " SLOT 1", g);
            if(menuSelection == 1)
                BitmapFontManager.drawString("fixed", 115, 150, ".SLOT 2", g);
            else
                BitmapFontManager.drawString("fixed", 115, 150, " SLOT 2", g);
            if(menuSelection == 2)
                BitmapFontManager.drawString("fixed", 115, 175, ".SLOT 3", g);
            else
                BitmapFontManager.drawString("fixed", 115, 175, " SLOT 3", g);
            if(menuSelection == 3)
                BitmapFontManager.drawString("fixed", 115, 225, ".BACK", g);
            else
                BitmapFontManager.drawString("fixed", 115, 225, " BACK", g);
        }if(options && !config){
            if(menuSelection == 0)
                BitmapFontManager.drawString("fixed", 115, 125, ".MUSIC " + musicState, g);
            else
                BitmapFontManager.drawString("fixed", 115, 125, " MUSIC " + musicState, g);
            if(menuSelection == 1)
                BitmapFontManager.drawString("fixed", 115, 150, ".SOUND " + soundState, g);
            else
                BitmapFontManager.drawString("fixed", 115, 150, " SOUND " + soundState, g);
            if(menuSelection == 2)
                BitmapFontManager.drawString("fixed", 115, 175, ".CONTROLS", g);
            else
                BitmapFontManager.drawString("fixed", 115, 175, " CONTROLS", g);
            if(menuSelection == 3)
                BitmapFontManager.drawString("fixed", 115, 225, ".BACK", g);
            else
                BitmapFontManager.drawString("fixed", 115, 225, " BACK", g);
        }if(config && configProgress == 0){
            if(menuSelection == 0)
                BitmapFontManager.drawString("fixed", 115, 125, ".CONFIG ALL", g);
            else
                BitmapFontManager.drawString("fixed", 115, 125, " CONFIG ALL", g);
            if(menuSelection == 1)
                BitmapFontManager.drawString("fixed", 115, 175, ".BACK", g);
            else
                BitmapFontManager.drawString("fixed", 115, 175, " BACK", g);
        }if(configProgress != 0){
            switch(configProgress){
                case 1:
                    BitmapFontManager.drawString("fixed", 75, 175, "PRESS LEFT", g);
                    break;
                case 2:
                    BitmapFontManager.drawString("fixed", 75, 175, "PRESS RIGHT", g);
                    break;
                case 3:
                    BitmapFontManager.drawString("fixed", 75, 175, "PRESS ROTATE CW", g);
                    break;
                case 4:
                    BitmapFontManager.drawString("fixed", 75, 175, "PRESS ROTATE CCW", g);
                    break;
                case 5:
                    BitmapFontManager.drawString("fixed", 75, 175, "PRESS HARD DROP", g);
                    break;
                case 6:
                    BitmapFontManager.drawString("fixed", 75, 175, "PRESS SOFT DROP", g);
                    break;
                case 7:
                    BitmapFontManager.drawString("fixed", 75, 175, "PRESS START", g);
                    break;
            }
        }
    }

    /**
     * Updates the state of the Main Menu
     * @param elapsed how much time has passed since the last frame
     */
    @Override
    public void update(long elapsed){
        timer += elapsed;

        //The animation of the Tetris Pieces falling down
        if(isAnimating){
            if(timer > 300 && !hasBB){
                backboard.accelY = 1;
                hasBB = true;
            }
            if(timer > 600 && !hasT){
                T.accelY = 1;
                hasT = true;
            }
            if(timer > 700 && !hasE){
                E.accelY = 1;
                hasE = true;
            }
            if(timer > 800 && !hasT2){
                T2.accelY = 1;
                hasT2 = true;
            }
            if(timer > 900 && !hasR){
                R.accelY = 1;
                hasR = true;
            }
            if(timer > 1000 && !hasI){
                I.accelY = 1;
                hasI = true;
            }
            if(timer > 1100 && !hasS){
                S.accelY = 1;
                hasS = true;
            }
            if(backboard.y >= 8){
                backboard.velocityY = 0;
                backboard.accelY = 0;
                backboard.y = 8;
            }
            if(T.y >= 15){
                T.velocityY = 0;
                T.accelY = 0;
                T.y = 15;
            }
            if(T2.y >= 15){
                T2.velocityY = 0;
                T2.accelY = 0;
                T2.y = 15;
            }
            if(E.y >= 15){
                E.velocityY = 0;
                E.accelY = 0;
                E.y = 15;
            }
            if(R.y >= 15){
                R.velocityY = 0;
                R.accelY = 0;
                R.y = 15;
            }
            if(I.y >= 15){
                I.velocityY = 0;
                I.accelY = 0;
                I.y = 15;
            }
            if(S.y >= 15){
                S.velocityY = 0;
                S.accelY = 0;
                S.y = 15;
                isAnimating = false;
            }
            backboard.y += backboard.velocityY;
            backboard.velocityY += backboard.accelY;
            T.y += T.velocityY;
            T.velocityY += T.accelY;
            T2.y += T2.velocityY;
            T2.velocityY += T2.accelY;
            E.y += E.velocityY;
            E.velocityY += E.accelY;
            R.y += R.velocityY;
            R.velocityY += R.accelY;
            I.y += I.velocityY;
            I.velocityY += I.accelY;
            S.y += S.velocityY;
            S.velocityY += S.accelY;
        }else{
            //If the animation was interrupted, set the pieces where they need to be
            if(backboard.y != 8){
                backboard.y = 8;
            }
            if(T.y != 15){
                T.y = 15;
            }
            if(T2.y != 15){
                T2.y = 15;
            }
            if(E.y != 15){
                E.y = 15;
            }
            if(R.y != 15){
                R.y = 15;
            }
            if(I.y != 15){
                I.y = 15;
            }
            if(S.y != 15){
                S.y = 15;
            }
        }
    }

    /**
     * Destroys the state.
     */
    @Override
    public void destroy(){
        //
    }

    /**
     * Called to pass down Keyboard Events from the master Panel.
     * @param keyCode the key that was pressed.
     */
    @Override
    public void keyPressed(int keyCode) {
        if(!isAnimating){
            if(!loading && !options){
                
                if(keyCode == 40){
                    menuSelection++;
                    TetrisSoundManager.playRotate();
                    if(menuSelection > 3){
                        menuSelection = 0;
                    }
                }
                if(keyCode == 38){
                    menuSelection--;
                    TetrisSoundManager.playRotate();
                    if(menuSelection < 0){
                        menuSelection = 3;
                    }
                }
                if(keyCode == 10 || keyCode == 88){
                    switch(menuSelection){
                        case 0:
                            TetrisPanel.tetrisPanel.setState(new TetrisGame(0));
                            break;
                        case 1:
                            loading = true;
                            menuSelection = 0;
                            break;
                        case 2:
                            options = true;
                            menuSelection = 0;
                            break;
                        case 3:
                            System.exit(0);
                            break;
                    }

                }
            }else if(loading && !options){
                if(keyCode == Gamepad.BUTTON_ESC || keyCode == 90){
                    loading = false;
                    menuSelection = 0;
                }
                if(keyCode == 40){
                    menuSelection++;
                    TetrisSoundManager.playRotate();
                    if(menuSelection > 3){
                        menuSelection = 0;
                    }
                }
                if(keyCode == 38){
                    menuSelection--;
                    TetrisSoundManager.playRotate();
                    if(menuSelection < 0){
                        menuSelection = 3;
                    }
                }
                if(keyCode == 10 || keyCode == 88){
                    if(menuSelection != 3){
                        if(loadFromFile(menuSelection+1)){
                            System.out.println(menuSelection+1);
                            TetrisPanel.tetrisPanel.setState(new TetrisGame(menuSelection+1));
                        }else{
                            TetrisSoundManager.playError();
                        }
                    }else{
                        loading = false;
                        menuSelection = 0;
                    }
                }
                
            }else if(options && !loading && !config){
                if(keyCode == Gamepad.BUTTON_ESC || keyCode == 90){
                    options = false;
                    menuSelection = 0;
                }
                if(keyCode == 40){
                    menuSelection++;
                    TetrisSoundManager.playRotate();
                    if(menuSelection > 3){
                        menuSelection = 0;
                    }
                }
                if(keyCode == 38){
                    menuSelection--;
                    TetrisSoundManager.playRotate();
                    if(menuSelection < 0){
                        menuSelection = 3;
                    }
                }
                if(keyCode == 10 || keyCode == 88){
                    switch(menuSelection){
                        case 0:
                            if(musicState == "ON"){
                                musicState = "OFF";
                                TetrisSoundManager.musicMuted = true;
                            }else{
                                musicState = "ON";
                                TetrisSoundManager.musicMuted = false;
                            }
                            break;
                        case 1:
                            if(soundState == "ON"){
                                soundState = "OFF";
                                TetrisSoundManager.soundsMuted = true;
                            }else{
                                soundState = "ON";
                                TetrisSoundManager.soundsMuted = false;
                            }
                            break;
                        case 2:
                            config = true;
                            menuSelection = 0;
                            break;
                        case 3:
                            options = false;
                            menuSelection = 0;
                            break;
                    }

                }
            }else if(config && configProgress == 0){
                if(keyCode == Gamepad.BUTTON_ESC || keyCode == 90){
                    config = false;
                    menuSelection = 0;
                }
                if(keyCode == 40){
                    menuSelection++;
                    TetrisSoundManager.playRotate();
                    if(menuSelection > 1){
                        menuSelection = 0;
                    }
                }
                if(keyCode == 38){
                    menuSelection--;
                    TetrisSoundManager.playRotate();
                    if(menuSelection < 0){
                        menuSelection = 1;
                    }
                }
                if(keyCode == 10 || keyCode == 88){
                    switch(menuSelection){
                        case 0:
                            configProgress = 1;
                            menuSelection = 0;
                            break;
                        case 1:
                            config = false;
                            menuSelection = 0;
                            saveConfig();
                            break;
                    }
                }
            }else if(configProgress != 0){
                responses[configProgress-1] = keyCode;
                configProgress++;
                if(configProgress > 7){
                    configProgress = 0;
                    Gamepad.BUTTON_LEFT = responses[0];
                    Gamepad.BUTTON_RIGHT = responses[1];
                    Gamepad.BUTTON_X = responses[2];
                    Gamepad.BUTTON_Z = responses[3];
                    Gamepad.BUTTON_UP = responses[4];
                    Gamepad.BUTTON_DOWN = responses[5];
                    Gamepad.BUTTON_START = responses[6];
                }
            }
        }
        isAnimating = false;
    }
    /**
     * Saves the config to TetrisData.dat
     * @return if the save was successful
     */
    private boolean saveConfig(){
        File save = new File("TetrisData.dat");
        if(!save.exists()){ //Save does not exist, make a new one.
            try{
                save.createNewFile();
            }catch(Throwable t){
                //
            }
        }
        try{
            FileReader fr = new FileReader(save);
            BufferedReader br = new BufferedReader(fr);
            String line1 = br.readLine();
            String line2 = br.readLine();
            String line3 = br.readLine();
            String line4 = br.readLine();
            if(line1 == null){
                line1 = "";
            }
            if(line2 == null){
                line2 = "";
            }
            if(line3 == null){
                line3 = "";
            }
            if(line4 == null){
                line4 = "";
            }
            FileWriter writer = new FileWriter(save, false);
            BufferedWriter out = new BufferedWriter(writer);
            String data = ""+ (musicState == "OFF" ? 1:0) + "-"
                            + (soundState == "OFF" ? 1:0) + "-"
                            + responses[0] + "-" //Left
                            + responses[1] + "-" //Right
                            + responses[2] + "-" //CW
                            + responses[3] + "-" //CCW
                            + responses[4] + "-" //Up
                            + responses[5] + "-" //Down
                            + responses[6];      //Start
            out.write(data);
            out.newLine();
            out.write(line2);
            out.newLine();
            out.write(line3);
            out.newLine();
            out.write(line4);
            
            out.close();
            writer.close();
            fr.close();
            br.close();
            return true;
        }catch(Throwable t){
            t.printStackTrace();
            return false;
        }
    }
    /**
     * Loads the config from TetrisData.dat, if it exists.
     * @return If the load was successful.
     */
    private boolean loadConfig(){
        File save = new File("TetrisData.dat");
        if(!save.exists()){
            return false;
        }
        try{
            FileReader fr = new FileReader(save);
            BufferedReader br = new BufferedReader(fr);
            String cf = br.readLine();
            String[] data = cf.split("-");

            if(data[0].length() != 1 || data[1].length() != 1 || data.length != 9){
                System.out.println(data[0].length());
                System.out.println(data[1].length());
                System.out.println(data.length);
                return false;
            }

            if(new Integer(data[0]) == 1){
                musicState = "OFF";
                TetrisSoundManager.musicMuted = true;
            }
            if(new Integer(data[1]) == 1){
                soundState = "OFF";
                TetrisSoundManager.soundsMuted = true;
            }

            Gamepad.BUTTON_LEFT = new Integer(data[2]);
            Gamepad.BUTTON_RIGHT = new Integer(data[3]);
            Gamepad.BUTTON_X = new Integer(data[4]);
            Gamepad.BUTTON_Z = new Integer(data[5]);
            Gamepad.BUTTON_UP = new Integer(data[6]);
            Gamepad.BUTTON_DOWN = new Integer(data[7]);
            Gamepad.BUTTON_START = new Integer(data[8]);
            
            return true;
        }catch(Throwable t){
            return false;
        }
    }
    /**
     * A cut down version of the Load Game from the TetrisGame class
     * It only performs the checks to see if the load is viable.
     * The TetrisGame state performs this check again.
     * @param saveSlot the slot to check
     * @return if the load is viable.
     */
    private boolean loadFromFile(int saveSlot){
        File save = new File("TetrisData.dat");
        if(!save.exists()){
            return false;
        }
        try{
            FileReader fr = new FileReader(save);
            BufferedReader br = new BufferedReader(fr);
            String cf = br.readLine();
            String s1 = br.readLine();
            String s2 = br.readLine();
            String s3 = br.readLine();
            String[] data = new String[0];
            switch(saveSlot){
                case 1:
                    data = s1.split("-");
                    break;
                case 2:
                    data = s2.split("-");
                    break;
                case 3:
                    data = s3.split("-");
                    break;
            }

            if(data[0].length() > 2 || data[1].length() > 5 || data[2].length() > 2 || data[3].length() > 2 || data[4].length() > 3 || data[5].length() != 1 || data[6].length() != 4 || data[7].length() != PlayingField.BOARD_WIDTH*PlayingField.BOARD_HEIGHT){
                return false;
            }
            int level = new Integer(data[0]);
            int score = new Integer(data[1]);
            int lines = new Integer(data[2]);
            int timeS = new Integer(data[3]);
            int timeM = new Integer(data[4]);
            int currentPiece = new Integer(data[5]);
            if(currentPiece > 6){
                return false;
            }
            int[] next = new int[]{new Integer(data[6].substring(0, 1)), new Integer(data[6].substring(1, 2))
                                 , new Integer(data[6].substring(2, 3)), new Integer(data[6].substring(3, 4))};
            if(next[0]>6 || next[1]>6 || next[2]>6 || next[3]>6){
                return false;
            }
            int[][] boardStatus = new int[PlayingField.BOARD_WIDTH][PlayingField.BOARD_HEIGHT];
            int prog = 0;
            for(int i = 0; i < PlayingField.BOARD_HEIGHT; i++){
                for(int k = 0; k < PlayingField.BOARD_WIDTH; k++){
                    boardStatus[k][i] = new Integer(data[7].substring(prog, prog+1));
                    if(boardStatus[k][i] > 7)
                        return false;
                    prog++;
                }
            }
            if(timeS > 59)
                return false;
            br.close();
            fr.close();
            return true;
        }catch(Throwable t){
            return false;
        }
    }

    /**
     * Just a basic move-able sprite class
     */
    private class movingIcon{
        public int x, y, velocityX, velocityY, accelX, accelY;
        public ImageIcon icon;
        public Image image;
        /**
         * Initializes the sprite
         * @param icon The image the sprite will be rendered as
         * @param x the X position
         * @param y the Y position
         */
        public movingIcon(ImageIcon icon,int x,int y){
            this.icon = icon;
            image = icon.getImage();
            this.x = x;
            this.y = y;
            velocityX = 0;
            velocityY = 0;
            accelY = 0;
            accelX = 0;
        }
    }
}