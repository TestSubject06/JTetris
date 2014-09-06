/**
 * The O piece.
 * @author Zack
 */
public class O extends Piece{
    /**
     * Creates a new O piece at the location
     * @param x the X in tiles
     * @param y the Y in tiles
     */
    public O(int x, int y){
        super(x, y, 1);
        rotation1 = rotation2 = rotation3 = rotation4 = new int[][]{{0,0,0,0},
                                                                    {0,1,1,0},
                                                                    {0,1,1,0},
                                                                    {0,0,0,0}};
        currentRotation = rotation1;
        registerEdges();
        checkSpawn();
        for(int i = 0; i < 4; i++){
            for(int k = 0; k <4; k++){
                if(currentRotation[i][k] == 1)
                    PlayingField.getPlayingField().setTile(x+i, y+k, 1);
            }
        }
    }
}
