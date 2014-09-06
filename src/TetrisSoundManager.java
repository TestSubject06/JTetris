
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

/**
 * Singleton sound manager class
 * @author Zack
 */
public class TetrisSoundManager {
    private static Clip thunk, clear, rotate, tetris, error, move, levelup, save, trackA;
    private static AudioInputStream inputStream;
    public static boolean soundsMuted = false, musicMuted = false, isReady = false;

    /**
     * Preps the SoundManager for use.
     */
    public static void init(){
        if(!isReady){
            try{

                thunk = AudioSystem.getClip();
                clear = AudioSystem.getClip();
                rotate = AudioSystem.getClip();
                tetris = AudioSystem.getClip();
                error = AudioSystem.getClip();
                save = AudioSystem.getClip();
                levelup = AudioSystem.getClip();
                move = AudioSystem.getClip();
                trackA = AudioSystem.getClip();
                inputStream = AudioSystem.getAudioInputStream(TetrisSoundManager.class.getResource("assets/thunk.wav"));
                thunk.open(inputStream);
                inputStream = AudioSystem.getAudioInputStream(TetrisSoundManager.class.getResource("assets/clear.wav"));
                clear.open(inputStream);
                inputStream = AudioSystem.getAudioInputStream(TetrisSoundManager.class.getResource("assets/rotate.wav"));
                rotate.open(inputStream);
                inputStream = AudioSystem.getAudioInputStream(TetrisSoundManager.class.getResource("assets/tetris.wav"));
                tetris.open(inputStream);
                inputStream = AudioSystem.getAudioInputStream(TetrisSoundManager.class.getResource("assets/save.wav"));
                save.open(inputStream);
                inputStream = AudioSystem.getAudioInputStream(TetrisSoundManager.class.getResource("assets/error.wav"));
                error.open(inputStream);
                inputStream = AudioSystem.getAudioInputStream(TetrisSoundManager.class.getResource("assets/levelup.wav"));
                levelup.open(inputStream);
                inputStream = AudioSystem.getAudioInputStream(TetrisSoundManager.class.getResource("assets/tick.wav"));
                move.open(inputStream);
                inputStream = AudioSystem.getAudioInputStream(TetrisSoundManager.class.getResource("assets/trackA.wav"));
                trackA.open(inputStream);
                isReady = true;
            }catch(Throwable t){
                //
            }
        }
    }

    /**
     * Plays the Thunk sound.
     */
    public static void playThunk(){
        if(soundsMuted)
            return;
        try{
            thunk.stop();
            thunk.setFramePosition(0);
            thunk.start();
        }catch(Throwable t){
            //
        }
    }

    /**
     * Plays the Rotate sound.
     */
    public static void playRotate(){
                if(soundsMuted)
            return;
        try{
            rotate.stop();
            rotate.setFramePosition(0);
            rotate.start();
        }catch(Throwable t){
            //
        }
    }

    /**
     * Plays the Clear sound.
     */
    public static void playClear(){
                if(soundsMuted)
            return;
        try{
            clear.stop();
            clear.setFramePosition(0);
            clear.start();
        }catch(Throwable t){
            //
        }
    }

    /**
     * Plays the Tetris sound.
     */
    public static void playTetris(){
                if(soundsMuted)
            return;
        try{
            tetris.stop();
            tetris.setFramePosition(0);
            tetris.start();
        }catch(Throwable t){
            //
        }
    }

    /**
     * Plays the Levelup sound.
     */
    public static void playLevelup(){
                if(soundsMuted)
            return;
        try{
            levelup.stop();
            levelup.setFramePosition(0);
            levelup.start();
        }catch(Throwable t){
            //
        }
    }

    /**
     * Plays the Error sound.
     */
    public static void playError(){
                if(soundsMuted)
            return;
        try{
            error.stop();
            error.setFramePosition(0);
            error.start();
        }catch(Throwable t){
            //
        }
    }

    /**
     * Plays the Save sound.
     */
    public static void playSave(){
                if(soundsMuted)
            return;
        try{
            save.stop();
            save.setFramePosition(0);
            save.start();
        }catch(Throwable t){
            //
        }
    }

    /**
     * Plays the Drop sound.
     */
    public static void playMove(){
                if(soundsMuted)
            return;
        try{
            move.stop();
            move.setFramePosition(0);
            move.start();
        }catch(Throwable t){
            //
        }
    }

    /**
     * Plays the Music.
     */
    public static void playTrackA(){
        if(musicMuted)
            return;
        try{
            trackA.stop();
            trackA.setFramePosition(0);
            trackA.start();
            trackA.loop(Clip.LOOP_CONTINUOUSLY);
        }catch(Throwable t){
            //
        }
    }

    /**
     * Pauses the music.
     */
    public static void pauseMusic(){
        trackA.stop();
    }

    /**
     * Resumes the music.
     */
    public static void unpauseMusic(){
        trackA.loop(Clip.LOOP_CONTINUOUSLY);
    }
}
