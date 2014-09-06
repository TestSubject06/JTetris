/**
 * The L piece (It's actually the J)
 * @author Zack
 */
public class L extends Piece{
    /**
     * Creates a new L piece at the location
     * @param x the X in tiles
     * @param y the Y in tiles
     */
    public L(int x, int y){
        super(x, y, 3);
        rotation1 = new int[][]{{0,0,0,0},
                                {0,0,0,1},
                                {0,1,1,1},
                                {0,0,0,0}};
                
        rotation2 = new int[][]{{0,0,0,0},
                                {0,0,1,0},
                                {0,0,1,0},
                                {0,0,1,1}};
        
        rotation3 = new int[][]{{0,0,0,0},
                                {0,0,0,0},
                                {0,1,1,1},
                                {0,1,0,0}};
        
        rotation4 = new int[][]{{0,0,0,0},
                                {0,1,1,0},
                                {0,0,1,0},
                                {0,0,1,0}};
        currentRotation = rotation4;
        registerEdges();
        checkSpawn();
        for(int i = 0; i < 4; i++){
            for(int k = 0; k <4; k++){
                if(currentRotation[i][k] == 1)
                    PlayingField.getPlayingField().setTile(x+i, y+k, 3);
            }
        }
    }
}
