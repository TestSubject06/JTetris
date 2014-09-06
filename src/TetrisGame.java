
import java.awt.Graphics;
import java.io.*;
import java.text.DecimalFormat;
import javax.swing.ImageIcon;

/**
 * The main game state.
 * @author Zack
 */
public class TetrisGame extends GameState{
    
    boolean paused = false, loading = false, saving = false, shouldQuake = false;
    int dropTimer, timeS, timeM, counter, quake = 0, menuSelection = 0;
    DecimalFormat fmt;
    DecimalFormat fmt2; ImageIcon backdrop, dialoge;
    ImageIcon[] largeIcons, smallIcons;
    PlayingField field;

    /**
     * Starts up a Tetris game
     * @param slot the game to load. If slot is 0 then the game will start fresh
     */
    public TetrisGame(int slot) {
        field = new PlayingField(20, 75);
        backdrop = new ImageIcon(this.getClass().getResource("assets/backdrop.png"));
        dialoge = new ImageIcon(this.getClass().getResource("assets/dialoge.png"));

        TetrisSoundManager.init();
        BitmapFontManager.init();
        BitmapFontManager.addBitmap("fixed", new ImageIcon(this.getClass().getResource("assets/fixedfont.png")).getImage());

        fmt = new DecimalFormat("00.##"); //Cheating for the 0 padding for the varous numbers
        fmt2 = new DecimalFormat("00000.##");

        largeIcons = new ImageIcon[7];
        smallIcons = new ImageIcon[7];
        for(int i = 0; i< 7; i++){
            largeIcons[i] = new ImageIcon(this.getClass().getResource("assets/next" + i + "L.png"));
            smallIcons[i] = new ImageIcon(this.getClass().getResource("assets/next" + i + "S.png"));
        }
        System.out.println(slot);
        if(slot != 0){
            loadFromFile(slot);
        }
        TetrisSoundManager.playTrackA();
    }

    /**
     * Renders the state
     * @param g the image to render to.
     */
    @Override
    public void render(Graphics g){
        g.drawImage(backdrop.getImage(), 0, 0, null);

        field.draw(g);

        BitmapFontManager.drawString("fixed", 203, 105, "LINES:" + field.getLinesCleared(), g);
        BitmapFontManager.drawString("fixed", 203, 130, "SCORE:" + fmt2.format(field.getScore()), g);

        BitmapFontManager.drawString("fixed", 203, 217, "LEVEL:" + field.getLevel(), g);
        BitmapFontManager.drawString("fixed", 203, 242, "TIME :" + fmt.format(timeM) + ":" + fmt.format(timeS), g);

        drawNextPieces(g);



        if(field.isGameOver() && !loading){
            TetrisSoundManager.pauseMusic();
            g.drawImage(dialoge.getImage(), 9, 142, null);
            BitmapFontManager.drawString("fixed", 13, 146, "GAME OVER", g);
            if(menuSelection == 0)
                BitmapFontManager.drawString("fixed", 13, 171, ".NEW GAME", g);
            else
                BitmapFontManager.drawString("fixed", 13, 171, " NEW GAME", g);
            if(menuSelection == 1)
                BitmapFontManager.drawString("fixed", 13, 196, ".LOAD GAME", g);
            else
                BitmapFontManager.drawString("fixed", 13, 196, " LOAD GAME", g);
            if(menuSelection == 2)
                BitmapFontManager.drawString("fixed", 13, 221, ".EXIT GAME", g);
            else
                BitmapFontManager.drawString("fixed", 13, 221, " EXIT GAME", g);
        }
        if(paused && !saving && !loading){
            g.drawImage(dialoge.getImage(), 9, 142, null);
            BitmapFontManager.drawString("fixed", 13, 146, "PAUSED", g);
            if(menuSelection == 0)
                BitmapFontManager.drawString("fixed", 13, 171, ".RESUME", g);
            else
                BitmapFontManager.drawString("fixed", 13, 171, " RESUME", g);
            if(menuSelection == 1)
                BitmapFontManager.drawString("fixed", 13, 196, ".SAVE GAME", g);
            else
                BitmapFontManager.drawString("fixed", 13, 196, " SAVE GAME", g);
            if(menuSelection == 2)
                BitmapFontManager.drawString("fixed", 13, 221, ".LOAD GAME", g);
            else
                BitmapFontManager.drawString("fixed", 13, 221, " LOAD GAME", g);
            if(menuSelection == 3)
                BitmapFontManager.drawString("fixed", 13, 246, ".EXIT GAME", g);
            else
                BitmapFontManager.drawString("fixed", 13, 246, " EXIT GAME", g);
        }
        if(saving){
            g.drawImage(dialoge.getImage(), 9, 142, null);
            BitmapFontManager.drawString("fixed", 13, 146, "SAVE GAME", g);
            if(menuSelection == 0)
                BitmapFontManager.drawString("fixed", 13, 171, ".SLOT 1", g);
            else
                BitmapFontManager.drawString("fixed", 13, 171, " SLOT 1", g);
            if(menuSelection == 1)
                BitmapFontManager.drawString("fixed", 13, 196, ".SLOT 2", g);
            else
                BitmapFontManager.drawString("fixed", 13, 196, " SLOT 2", g);
            if(menuSelection == 2)
                BitmapFontManager.drawString("fixed", 13, 221, ".SLOT 3", g);
            else
                BitmapFontManager.drawString("fixed", 13, 221, " SLOT 3", g);
            if(menuSelection == 3)
                BitmapFontManager.drawString("fixed", 13, 246, ".BACK", g);
            else
                BitmapFontManager.drawString("fixed", 13, 246, " BACK", g);
        }
        if(loading){
            g.drawImage(dialoge.getImage(), 9, 142, null);
            BitmapFontManager.drawString("fixed", 13, 146, "LOAD GAME", g);
            if(menuSelection == 0)
                BitmapFontManager.drawString("fixed", 13, 171, ".SLOT 1", g);
            else
                BitmapFontManager.drawString("fixed", 13, 171, " SLOT 1", g);
            if(menuSelection == 1)
                BitmapFontManager.drawString("fixed", 13, 196, ".SLOT 2", g);
            else
                BitmapFontManager.drawString("fixed", 13, 196, " SLOT 2", g);
            if(menuSelection == 2)
                BitmapFontManager.drawString("fixed", 13, 221, ".SLOT 3", g);
            else
                BitmapFontManager.drawString("fixed", 13, 221, " SLOT 3", g);
            if(menuSelection == 3)
                BitmapFontManager.drawString("fixed", 13, 246, ".BACK", g);
            else
                BitmapFontManager.drawString("fixed", 13, 246, " BACK", g);
        }
    }

    /**
     * Draws the next pieces lineup
     * @param g the image to draw to.
     */
    private void drawNextPieces(Graphics g) {
        ImageIcon a = largeIcons[field.getNextPieces().get(0)];
        g.drawImage(a.getImage(), 235-(a.getIconWidth()/2), 360-(a.getIconHeight()/2), null);
        a = smallIcons[field.getNextPieces().get(1)];
        g.drawImage(a.getImage(), 285-(a.getIconWidth()/2), 360-(a.getIconHeight()/2), null);
        a = smallIcons[field.getNextPieces().get(2)];
        g.drawImage(a.getImage(), 325-(a.getIconWidth()/2), 360-(a.getIconHeight()/2), null);
        a = smallIcons[field.getNextPieces().get(3)];
        g.drawImage(a.getImage(), 365-(a.getIconWidth()/2), 360-(a.getIconHeight()/2), null);
    }

    /**
     * Mostly for updating timers.
     * @param elapsed the amount of time that has passed since the last frame.
     */
    @Override
    public void update(long elapsed){
        if(!field.isGameOver() && !paused){
            dropTimer+= elapsed;
            if(dropTimer >= field.getDropTimer()){
                dropTimer -= field.getDropTimer();
                field.dropPiece();
                TetrisSoundManager.playMove();
            }
            counter += elapsed;
            if(counter > 1000){
                timeS++;
                counter -= 1000;
                if(timeS == 60){
                    timeS = 0;
                    timeM++;
                }
            }
        }



        //muahahahhahahahahahahaha
        //aaaHAHAHAHAHAHAHAHAHAHAHAHA
        //HAAAAAAAAHAHAHAHAHHAHAHAHAHAHAHAHAHAHAH
        //AAAGGGHAHAHAHAHAHHGHAHAGHAHGAHGAHAGHHaghaGHAghagjkdfhgjkdfkgbjdfb...
        if(shouldQuake){
            quake+=2;
            field.setY(field.getY() + 2);
            if(quake >= 6)
                shouldQuake = false;
        }else{
            if(quake > 0){
                quake--;
                field.setY(field.getY() - 1);
            }
        }
    }

    /**
     * Saves the game to the TetrisData.dat
     * @param saveSlot the slot to save to
     * @return true if successful
     */
    private boolean saveToFile(int saveSlot){
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
            String data = field.getLevel() + "-"
                    + field.getScore() + "-"
                    + field.getLinesCleared() + "-"
                    + timeS + "-"
                    + timeM + "-"
                    + (field.getActivePiece().tileID-1) + "-"
                    + field.getNextPieces().get(0)
                    + field.getNextPieces().get(1)
                    + field.getNextPieces().get(2)
                    + field.getNextPieces().get(3)
                    + "-";
            field.getActivePiece().phaseOut();
            for(int i = 0; i < PlayingField.BOARD_HEIGHT; i++){
                for(int k = 0; k < PlayingField.BOARD_WIDTH; k++){
                    data+=""+field.getBoardStatus()[k][i];
                }
            }
            field.getActivePiece().phaseIn();
            switch(saveSlot){
                case 1:
                    out.write(line1);
                    out.newLine();
                    out.write(data);
                    out.newLine();
                    out.write(line3);
                    out.newLine();
                    out.write(line4);
                    break;
                case 2:
                    out.write(line1);
                    out.newLine();
                    out.write(line2);
                    out.newLine();
                    out.write(data);
                    out.newLine();
                    out.write(line4);
                    break;
                case 3:
                    out.write(line1);
                    out.newLine();
                    out.write(line2);
                    out.newLine();
                    out.write(line3);
                    out.newLine();
                    out.write(data);
                    break;
            }
            out.flush();
            out.close();
            return true;
        }catch(IOException e){
            System.out.println(e.getLocalizedMessage());
            return false;
        }
    }
    
    /**
     * Loads the game from TetrisData.dat if it exists.
     * @param saveSlot the slot to load from
     * @return true if successful
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
            this.timeS = timeS;
            this.timeM = timeM;
            field = new PlayingField(20, 75, level, score, lines, currentPiece, next, boardStatus);
            br.close();
            fr.close();
            return true;
        }catch(Throwable t){
            return false;
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
     * most of the menu logic is here.
     * @param keyCode the key that was pressed.
     */
    @Override
    public void keyPressed(int keyCode) {
        if(!field.isGameOver() && !paused && !loading && !saving){              //Game
            if(keyCode == Gamepad.BUTTON_LEFT){
                field.getActivePiece().move(1);
            }
            if(keyCode == Gamepad.BUTTON_RIGHT){
                field.getActivePiece().move(2);
            }
            if(keyCode == Gamepad.BUTTON_DOWN){
                field.dropPiece();
                TetrisSoundManager.playMove();
                dropTimer = 0;
            }
            if(keyCode == Gamepad.BUTTON_UP){
                field.getActivePiece().settle();
                field.dropPiece(); //This will return false, but check for the clears etc...
                dropTimer = 0;
                shouldQuake = true;
                TetrisSoundManager.playThunk();
            }
            if(keyCode == Gamepad.BUTTON_X){
                field.getActivePiece().rotate(0);
                TetrisSoundManager.playRotate();
            }
            if(keyCode == Gamepad.BUTTON_Z){
                field.getActivePiece().rotate(1);
                TetrisSoundManager.playRotate();
            }
            if(keyCode == Gamepad.BUTTON_ESC || keyCode == Gamepad.BUTTON_START){
                TetrisSoundManager.pauseMusic();
                paused = true;
            }
        }else if(field.isGameOver() && !loading && !saving){                    //Game Over menu
            if(keyCode == 10 || keyCode == 88){
                switch(menuSelection){
                    case 0:
                        field = new PlayingField(20, 75);
                        timeS = 0;
                        timeM = 0;
                        counter = 0;
                        TetrisSoundManager.playTrackA();
                        break;
                    case 1:
                        loading = true;
                        menuSelection = 0;
                        break;
                    case 2:
                        System.exit(0);
                        break;
                }
            }
            if(keyCode == 40){
                TetrisSoundManager.playRotate();
                menuSelection++;
                if(menuSelection > 2)
                    menuSelection = 0;
            }
            if(keyCode == 38){
                TetrisSoundManager.playRotate();
                menuSelection--;
                if(menuSelection < 0)
                    menuSelection = 2;
            }
            if(keyCode == Gamepad.BUTTON_ESC){
                System.exit(0);
            }
        }else if(paused && !loading && !saving){                                //Pause Menu
            if(keyCode == Gamepad.BUTTON_ESC || keyCode == 90){
                paused = false;
                TetrisSoundManager.unpauseMusic();
                menuSelection = 0;
            }
            if(keyCode == 10 || keyCode == 88){
                switch(menuSelection){
                    case 0:
                        TetrisSoundManager.unpauseMusic();
                        paused = false;
                        break;
                    case 1:
                        saving = true;
                        menuSelection = 0;
                        break;
                    case 2:
                        loading = true;
                        menuSelection = 0;
                        break;
                    case 3:
                        TetrisPanel.tetrisPanel.setState(new MainMenu());
                        break;
                }
            }
            if(keyCode == 40){
                TetrisSoundManager.playRotate();
                menuSelection++;
                if(menuSelection > 3)
                    menuSelection = 0;
            }
            if(keyCode == 38){
                TetrisSoundManager.playRotate();
                menuSelection--;
                if(menuSelection < 0)
                    menuSelection = 3;
            }
        }else if(saving){                                                       //Save Menu
            if(keyCode == Gamepad.BUTTON_ESC || keyCode == 90){
                TetrisSoundManager.unpauseMusic();
                paused = false;
                saving = false;
                menuSelection = 0;
            }
            if(keyCode == 10 || keyCode == 88){
                if(menuSelection < 3){
                    if(saveToFile(menuSelection+1)){
                        menuSelection = 0;
                        saving = false;
                        TetrisSoundManager.playSave();
                    }else{
                        TetrisSoundManager.playError();
                    }
                }else{
                    saving = false;
                    menuSelection = 0;
                }
            }
            if(keyCode == 40){
                TetrisSoundManager.playRotate();
                menuSelection++;
                if(menuSelection > 3)
                    menuSelection = 0;
            }
            if(keyCode == 38){
                TetrisSoundManager.playRotate();
                menuSelection--;
                if(menuSelection < 0)
                    menuSelection = 3;
            }
        }else if(loading){                                                      //Load Menu
            if(keyCode == Gamepad.BUTTON_ESC || keyCode == 90){
                TetrisSoundManager.unpauseMusic();
                paused = false;
                loading = false;
                menuSelection = 0;
            }
            if(keyCode == 10 || keyCode == 88){
                if(menuSelection < 3){
                    if(loadFromFile(menuSelection+1)){
                        menuSelection = 0;
                        loading = false;
                        if(!paused)
                            TetrisSoundManager.playTrackA();
                    }else{
                        TetrisSoundManager.playError();
                    }
                }else{
                    loading = false;
                    menuSelection = 0;
                }
            }
            if(keyCode == 40){
                TetrisSoundManager.playRotate();
                menuSelection++;
                if(menuSelection > 3)
                    menuSelection = 0;
            }
            if(keyCode == 38){
                TetrisSoundManager.playRotate();
                menuSelection--;
                if(menuSelection < 0)
                    menuSelection = 3;
            }
        }
    }
}