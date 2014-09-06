
import java.awt.Graphics;
import java.util.ArrayList;
import javax.swing.ImageIcon;

/**
 * The playing field. Contains most of the logic for the game.
 * @author Zack
 */
public class PlayingField {
    public static final int BOARD_WIDTH = 10, BOARD_HEIGHT = 20;
    private static PlayingField playingField;
    
    private int[][] boardStatus = new int[BOARD_WIDTH][BOARD_HEIGHT];
    private int x, y, linesCleared, score, dropTimer = 1200, nextMilestone = 800, level = 1;
    private boolean gameOver = false;
    private ImageIcon boardBase, tileCyan, tileOrange, tileYellow, tileGreen, tileRed, tileBlue, tilePurple, ghost;
    private Piece activePiece;
    private ArrayList<Integer> nextPieces;
    private String flavortext = "";
    
    /**
     * Creates a new playing field at the specified coordinates.
     * @param x the x
     * @param y the y
     */
    public PlayingField(int x, int y){
        //Setup the assets.
        tileYellow = new ImageIcon(this.getClass().getResource("assets/yellowTile.png"));
        tileOrange = new ImageIcon(this.getClass().getResource("assets/orangeTile.png"));
        tileCyan = new ImageIcon(this.getClass().getResource("assets/cyanTile.png"));
        tileGreen = new ImageIcon(this.getClass().getResource("assets/greenTile.png"));
        tileBlue = new ImageIcon(this.getClass().getResource("assets/blueTile.png"));
        tilePurple = new ImageIcon(this.getClass().getResource("assets/purpleTile.png"));
        tileRed = new ImageIcon(this.getClass().getResource("assets/redTile.png"));
        ghost = new ImageIcon(this.getClass().getResource("assets/ghost.png"));
        boardBase = new ImageIcon(this.getClass().getResource("assets/board.png"));
        //Initialize some stuff
        this.x = x;
        this.y = y;
        playingField = this; //nyah nyah nyah leaking this in constructor aww wah java. shut up.
        nextPieces = new ArrayList();
        while(nextPieces.size() < 4){
            nextPieces.add((int)(Math.random()*7));
        }
        makeNewPiece();
    }
    
    /**
     * Secondary constructor used when loading the game.
     * @param x the x
     * @param y the y
     * @param level the current Level
     * @param score the current Score
     * @param linesCleared the number of lines cleared
     * @param currentPiece the active piece
     * @param nextPieces the next four pieces
     * @param boardStatus the status of the board
     */
    public PlayingField(int x, int y, int level, int score, int linesCleared, int currentPiece, int[] nextPieces, int[][] boardStatus){
        this(x, y);
        this.level = level;
        for(int i = 0; i<level-1; i++){
            nextMilestone+=1200;
            dropTimer*=.8;
            if(dropTimer < 30){
                dropTimer = 30;
            }
        }
        this.linesCleared = linesCleared;
        this.score = score;
        activePiece.phaseOut();
        activePiece = null;
        switch(currentPiece){
            case 0:
                setActivePiece(new O(3, -2));
                break;
            case 1:
                setActivePiece(new I(3, -1));
                break;
            case 2:
                setActivePiece(new L(3, -1));
                break;
            case 3:
                setActivePiece(new J(3, -2));
                break;
            case 4:
                setActivePiece(new S(4, -1));
                break;
            case 5:
                setActivePiece(new Z(4, -1));
                break;
            case 6:
                setActivePiece(new T(3, -1));
                break;
        }
        this.nextPieces = new ArrayList();
        for(int i = 0; i<nextPieces.length; i++){
            this.nextPieces.add(nextPieces[i]);
        }
        this.boardStatus = boardStatus;
        activePiece.phaseIn();
    }
    
    /**
     * Is the game over?
     * @return well, is it?
     */
    public boolean isLossCondition(){
        if(getActivePiece().checkOffscreen()){
            endGame();
            return true;
        }
        return false;
    }
    
    /**
     * Checks for any clears on the board, and if there are any, clear them.
     * 
     * I was going to have them animate, but it would require way too much reworking.
     */
    public void checkForClears(){
        int numClears = 0;
        for(int i = 0; i < BOARD_HEIGHT; i++){
            boolean isClear = true;
            for(int k = 0; k < BOARD_WIDTH; k++){
                if(getBoardStatus()[k][i] == 0 && isClear){
                    isClear = false;
                }
            }
            if(isClear){
                for(int j = 0; j < BOARD_WIDTH; j++){
                    for(int l = i; l >= 0; l--){
                        if(l==0)
                            boardStatus[j][l] = 0;
                        else
                            boardStatus[j][l] = getBoardStatus()[j][l-1];
                    }
                }
                linesCleared++;
                numClears++;
            }
        }
        switch(numClears){
            case 0:
                break;
            case 1:
                score+=50;
                TetrisSoundManager.playClear();
                break;
            case 2:
                score+=150;
                TetrisSoundManager.playClear();
                break;
            case 3:
                score+=350;
                TetrisSoundManager.playClear();
                break;
            case 4:
                score+=500;
                TetrisSoundManager.playTetris();
                break;
        }
        if(score > getNextMilestone()){
            nextMilestone+=1200;
            level++;
            setDropTimer((int) (dropTimer * .8));
            if(getDropTimer() < 30){
                setDropTimer(30);
            }
            TetrisSoundManager.playLevelup();
        }
    }
    
    /**
     * Checks to see if a tile is vacant. If the tile is above the board(While in the left and right bounds), it will return true.
     * @param x the X to check
     * @param y the Y to check
     * @return is it clear
     */
    public boolean isTileVacant(int x, int y){
        if(x<0 || x>=BOARD_WIDTH){
            return false;//We still should lock the piece within the bounds, before that edge case of moving above the board.
        }
        if(y < 0){
            return true; //We want to be able to move about when the tile is off-screen.
        }
        try{
            if(getBoardStatus()[x][y] == 0){
                return true;
            }
            return false;
        }catch(Throwable t){
            return false;
        }
    }
    
    /**
     * Sets the tile on the board.
     * @param x the X tile
     * @param y the Y tile
     * @param tileID the ID to set the tile to
     */
    public void setTile(int x, int y, int tileID){
        if(x < BOARD_WIDTH && y < BOARD_HEIGHT && x >= 0 && y >= 0)
            boardStatus[x][y] = tileID;
    }
    
    /**
     * Draws the board.
     * @param g the image to draw to
     */
    public void draw(Graphics g){
        g.drawImage(boardBase.getImage(), getX(), getY(), null);
        drawGhost(g);
        for(int i = 0; i < BOARD_HEIGHT; i++){
            for(int k = 0; k < BOARD_WIDTH; k++){
                if(getBoardStatus()[k][i] == 1)
                    g.drawImage(tileYellow.getImage(), k*16+getX()+2, i*16+getY()+2, null);
                if(getBoardStatus()[k][i] == 2)
                    g.drawImage(tileCyan.getImage(), k*16+getX()+2, i*16+getY()+2, null);
                if(getBoardStatus()[k][i] == 3)
                    g.drawImage(tileOrange.getImage(), k*16+getX()+2, i*16+getY()+2, null);
                if(getBoardStatus()[k][i] == 4)
                    g.drawImage(tileBlue.getImage(), k*16+getX()+2, i*16+getY()+2, null);
                if(getBoardStatus()[k][i] == 5)
                    g.drawImage(tileGreen.getImage(), k*16+getX()+2, i*16+getY()+2, null);
                if(getBoardStatus()[k][i] == 6)
                    g.drawImage(tileRed.getImage(), k*16+getX()+2, i*16+getY()+2, null);
                if(getBoardStatus()[k][i] == 7)
                    g.drawImage(tilePurple.getImage(), k*16+getX()+2, i*16+getY()+2, null);
            }
        }
    }
    
    /**
     * Draws the ghost piece.
     * @param g the image to draw to
     */
    private void drawGhost(Graphics g){
        activePiece.phaseOut();
        boolean rowClear = true;
        int row = -5;
        for(int i = activePiece.y; i < BOARD_HEIGHT; i++){
            rowClear = true;
            for(int l = 0; l < 4; l++){
                if(activePiece.lows[l] != -1){
                    if(!isTileVacant(activePiece.x+l, i+activePiece.lows[l])){
                        rowClear = false;
                    }
                }
            }
            if(!rowClear){
                row = i-1;
                break;
            }
        }
        activePiece.phaseIn();
        for(int i = 0; i < 4; i++){
            for(int k = 0; k <4; k++){
                if(activePiece.currentRotation[i][k] == 1)
                    if(row+k >= 0)
                        g.drawImage(ghost.getImage(), (activePiece.x+i)*16+getX()+2, (row+k)*16+getY()+2, null);
            }
        }
    }
    
    /**
     * Destroys the old piece, Sets the first in line to the active piece, and then
     * makes a new piece for the lineup.
     */
    public void makeNewPiece(){
        setActivePiece(null);
        int type = getNextPieces().get(0);
        switch(type){
            case 0:
                setActivePiece(new O(3, -2));
                break;
            case 1:
                setActivePiece(new I(3, -1));
                break;
            case 2:
                setActivePiece(new L(3, -2));
                break;
            case 3:
                setActivePiece(new J(3, -2));
                break;
            case 4:
                setActivePiece(new S(4, -1));
                break;
            case 5:
                setActivePiece(new Z(4, -1));
                break;
            case 6:
                setActivePiece(new T(3, -1));
                break;
        }
        getNextPieces().remove(0);
        getNextPieces().add((int)(Math.random()*7));
    }
    
    /**
     * Drops the piece down once. If it fails, run checks.
     */
    public void dropPiece(){
        if(!activePiece.move(0)){
            checkForClears();
            isLossCondition();
            makeNewPiece();
        }
    }
    
    /**
     * Ends the game.
     */
    public void endGame(){
        gameOver = true;
    }

    /**
     * @return the playingField
     */
    public static PlayingField getPlayingField() {
        return playingField;
    }

    /**
     * @return the boardStatus
     */
    public int[][] getBoardStatus() {
        return boardStatus;
    }

    /**
     * @return the x
     */
    public int getX() {
        return x;
    }

    /**
     * @param x the x to set
     */
    public void setX(int x) {
        this.x = x;
    }

    /**
     * @return the y
     */
    public int getY() {
        return y;
    }

    /**
     * @param y the y to set
     */
    public void setY(int y) {
        this.y = y;
    }

    /**
     * @return the activePiece
     */
    public Piece getActivePiece() {
        return activePiece;
    }

    /**
     * @param activePiece the activePiece to set
     */
    public void setActivePiece(Piece activePiece) {
        this.activePiece = activePiece;
    }

    /**
     * @return the gameOver
     */
    public boolean isGameOver() {
        return gameOver;
    }

    /**
     * @return the linesCleared
     */
    public int getLinesCleared() {
        return linesCleared;
    }

    /**
     * @return the flavortext
     */
    public String getFlavortext() {
        return flavortext;
    }

    /**
     * @return the score
     */
    public int getScore() {
        return score;
    }

    /**
     * @return the dropTimer
     */
    public int getDropTimer() {
        return dropTimer;
    }

    /**
     * @return the nextMilestone
     */
    public int getNextMilestone() {
        return nextMilestone;
    }

    /**
     * @return the level
     */
    public int getLevel() {
        return level;
    }

    /**
     * @param dropTimer the dropTimer to set
     */
    public void setDropTimer(int dropTimer) {
        this.dropTimer = dropTimer;
    }

    /**
     * @return the nextPieces
     */
    public ArrayList<Integer> getNextPieces() {
        return nextPieces;
    }

}