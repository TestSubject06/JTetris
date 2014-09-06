

/**
 * The base class for all pieces. Contains most of the logic.
 * @author Zack
 */
public class Piece {
    
    //These are all needed by subclasses.
    protected int x, y; //i tiles, of the top left of the model
    protected int rotation, tileID;
    protected int[][] rotation1, rotation2, rotation3, rotation4;
    protected int[][] currentRotation;
    protected int[] lows, highs, lefts, rights;
    
    /**
     * Creates a new Piece.
     * @param x the X coordinate in tiles
     * @param y the Y coordinate in tiles
     * @param tileID the unique ID for use with rendering on the board
     */
    public Piece(int x, int y, int tileID){
        this.x = x;
        this.y = y;
        this.tileID = tileID;
        rotation = 3;
        lefts = new int[4];
        rights = new int[4];
        highs = new int[4];
        lows = new int[4];
    }
    
    /**
     * Attempts to move in a given direction.
     * @param dir The direction to move in. 0 = Down; 1 = Left; 2 = Right
     * @return Returns whether or not the move was a success
     */
    public boolean move(int dir){
        boolean canMove = true;
        switch(dir){
            case 0:
                for(int i = 0; i < 4; i++){
                    if(lows[i] != -1)
                        if(!PlayingField.getPlayingField().isTileVacant(x+i,y+lows[i]+1))
                            canMove = false;
                }

                if(canMove){
                    y++;
                    for(int i = 0; i < 4; i++){
                        if(highs[i] != -1)
                            PlayingField.getPlayingField().setTile(x+i, y+highs[i]-1, 0);
                        if(lows[i] != -1)
                            PlayingField.getPlayingField().setTile(x+i, y+lows[i], tileID);
                    }
                    return true;
                }
                break;

            case 1:
                for(int i = 0; i < 4; i++){
                    if(lefts[i] != -1)
                        if(!PlayingField.getPlayingField().isTileVacant(x+lefts[i]-1,y+i))
                            canMove = false;
                }

                if(canMove){
                    x--;
                    for(int i = 0; i < 4; i++){
                        if(rights[i] != -1)
                            PlayingField.getPlayingField().setTile(x+rights[i]+1, y+i, 0);
                        if(lefts[i] != -1)
                            PlayingField.getPlayingField().setTile(x+lefts[i], y+i, tileID);
                    }
                    return true;
                }
                break;

            case 2:
                for(int i = 0; i < 4; i++){
                    if(rights[i] != -1)
                        if(!PlayingField.getPlayingField().isTileVacant(x+rights[i]+1,y+i))
                            canMove = false;
                }

                if(canMove){
                    x++;
                    for(int i = 0; i < 4; i++){
                        if(lefts[i] != -1)
                            PlayingField.getPlayingField().setTile(x+lefts[i]-1, y+i, 0);
                        if(rights[i] != -1)
                            PlayingField.getPlayingField().setTile(x+rights[i], y+i, tileID);
                    }
                    return true;
                }
                break;
        }
        return false;
    }

    /**
     * Phases the block out of existence. This is useful for getting the state
     * of the board without the activePiece.
     */
    public void phaseOut(){
        for(int i = 0; i < 4; i++){
            for(int k = 0; k <4; k++){
                if(currentRotation[i][k] == 1)
                    PlayingField.getPlayingField().setTile(x+i, y+k, 0);
            }
        }
    }
    /**
     * Phases the piece back onto the board. This is used when bringing a piece
     * back after a phaseOut.
     */
    public void phaseIn(){
        for(int i = 0; i < 4; i++){
            for(int k = 0; k <4; k++){
                if(currentRotation[i][k] == 1)
                    PlayingField.getPlayingField().setTile(x+i, y+k, tileID);
            }
        }
    }

    /**
     * Attempts to rotate in the given direction.
     * @param dir The direction to rotate in. 0 = Clockwise; 1 = Counter-Clockwise
     * @return whether the rotate was successful or not
     */
    public boolean rotate(int dir){
        //Let's get a wall kick rolling here.
        phaseOut();
        if(dir == 0){
            rotation--;
            rotation = rotation%4;
            if(rotation == -1)
                rotation = 3;
            if(rotation == 0)
                currentRotation = rotation1;
            if(rotation == 1)
                currentRotation = rotation2;
            if(rotation == 2)
                currentRotation = rotation3;
            if(rotation == 3)
                currentRotation = rotation4;
        }else{
            rotation++;
            rotation = rotation%4;
            if(rotation == -1)
                rotation = 3;
            if(rotation == 0)
                currentRotation = rotation1;
            if(rotation == 1)
                currentRotation = rotation2;
            if(rotation == 2)
                currentRotation = rotation3;
            if(rotation == 3)
                currentRotation = rotation4;
        }
        boolean rotationWorks = true;
        for(int i = 0; i < 4; i++){
            for(int k = 0; k <4; k++){
                if(currentRotation[i][k] == 1)
                    if (!PlayingField.getPlayingField().isTileVacant(x+i, y+k))
                        rotationWorks = false;
            }
        }
        if(!rotationWorks){
            x++; //Move to the right one
            rotationWorks = true;
            for(int i = 0; i < 4; i++){
                for(int k = 0; k <4; k++){
                    if(currentRotation[i][k] == 1)
                        if (!PlayingField.getPlayingField().isTileVacant(x+i, y+k))
                            rotationWorks = false;
                }
            }
            if(!rotationWorks){
                x-=2; //Move to the left one from the original
                rotationWorks = true;
                for(int i = 0; i < 4; i++){
                    for(int k = 0; k <4; k++){
                        if(currentRotation[i][k] == 1)
                            if (!PlayingField.getPlayingField().isTileVacant(x+i, y+k))
                                rotationWorks = false;
                    }
                }
                if(!rotationWorks){
                    x++; //All have failed, go home.
                    if(dir == 0){
                        rotation++;
                        rotation = rotation%4;
                        if(rotation == -1)
                            rotation = 3;
                        if(rotation == 0)
                            currentRotation = rotation1;
                        if(rotation == 1)
                            currentRotation = rotation2;
                        if(rotation == 2)
                            currentRotation = rotation3;
                        if(rotation == 3)
                            currentRotation = rotation4;
                    }else{
                        rotation--;
                        rotation = rotation%4;
                        if(rotation == -1)
                            rotation = 3;
                        if(rotation == 0)
                            currentRotation = rotation1;
                        if(rotation == 1)
                            currentRotation = rotation2;
                        if(rotation == 2)
                            currentRotation = rotation3;
                        if(rotation == 3)
                            currentRotation = rotation4;
                    }
                    phaseIn();
                    return false;
                }
            }
        }

        registerEdges();
        phaseIn();
        return true;
    }
    /**
     * Moves the piece down until it can no longer move down.
     * (Essentially slams the piece into the board)
     */
    public void settle(){
        while(move(0)){
            //just spam it till it isn't true.
        }
    }
    
    //
    //The following are used to construct the collision edges for the pieces.
    //They only work on solid, convex shapes (Which luckily is all the tetroids)
    //
    
    /**
     * Returns the last 1 found in terms of Y
     * @param col the column to check
     * @return the last 1 found.
     */
    public int checkColumnDown(int[] col){
        int ret = -1;
        for(int i = 0; i<col.length; i++){
            if(col[i] == 1)
                ret = i;
        }
        return ret;
    }
    /**
     * Returns the first 1 found in terms of Y
     * @param col the column to check
     * @return the first 1 found.
     */
    public int checkColumnUp(int[] col){
        for(int i = 0; i<col.length; i++){
            if(col[i] == 1)
               return i;
        }
        return -1;
    }
    
    /**
     * Returns the last 1 found in terms of X
     * @param row the row to check
     * @param twoD the model array
     * @return the last 1 found
     */
    public int checkRowRight(int row, int[][] twoD){
        int ret = -1;
        for(int i = 0; i<twoD.length; i++){
            if(twoD[i][row] == 1)
               ret = i;
        }
        return ret;
    }
    
    /**
     * Returns the first 1 found in terms of X
     * @param row the row to check
     * @param twoD the model array
     * @return the first 1 found
     */
    public int checkRowLeft(int row, int[][] twoD){
        for(int i = 0; i<twoD.length; i++){
            if(twoD[i][row] == 1)
               return i;
        }
        return -1;
    }
    
    /**
     * Registers all of the edges of the piece.
     * called when a piece rotates, spawns, or is otherwise changed.
     */
    public void registerEdges(){
        lows[0] = checkColumnDown(currentRotation[0]);
        lows[1] = checkColumnDown(currentRotation[1]);
        lows[2] = checkColumnDown(currentRotation[2]);
        lows[3] = checkColumnDown(currentRotation[3]);
        highs[0] = checkColumnUp(currentRotation[0]);
        highs[1] = checkColumnUp(currentRotation[1]);
        highs[2] = checkColumnUp(currentRotation[2]);
        highs[3] = checkColumnUp(currentRotation[3]);
        lefts[0] = checkRowLeft(0, currentRotation);
        lefts[1] = checkRowLeft(1, currentRotation);
        lefts[2] = checkRowLeft(2, currentRotation);
        lefts[3] = checkRowLeft(3, currentRotation);
        rights[0] = checkRowRight(0, currentRotation);
        rights[1] = checkRowRight(1, currentRotation);
        rights[2] = checkRowRight(2, currentRotation);
        rights[3] = checkRowRight(3, currentRotation);
    }
    
    /**
     * Checks to see if a piece is offscreen, by checking the lows.
     * @return 
     */
    public boolean checkOffscreen(){
        for(int i = 0; i < 4; i++){
            if(lows[i] != -1)
                if(y+lows[i] <= 0)
                    return true;
        }
        return false;
    }
    
    /**
     * Checks to see if the spawn area is safe for the piece, if there is a collision on spawn
     */
    public void checkSpawn(){
        boolean spawnWorks = true;
        for(int i = 0; i < 4; i++){
            for(int k = 0; k <4; k++){
                if(currentRotation[i][k] == 1)
                    if (!PlayingField.getPlayingField().isTileVacant(x+i, y+k))
                        spawnWorks = false;
            }
        }
        if(!spawnWorks){
            PlayingField.getPlayingField().endGame(); //GameOver.
        }
    }
}