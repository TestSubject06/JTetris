
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.HashMap;

/**
 * Bitmap font manager class.
 * @author Zack
 */
public class BitmapFontManager {
    //Fonts follow the following format:
    //ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!?:.
    //It wouldn't be too hard to add another hashmap with the same font key and an
    //order string.
    private static HashMap<String, Image> fonts;
    private static HashMap<String, Rectangle> rects; //It needs to be a hashmap of hashmaps, hahaha
                                                     //As it is, only the most recent set of rects will
                                                     //persist. Which is fine for this case, since there's
                                                     //only one font.
    
    /**
     * Initializes the basics and gets the font manager ready to accept a bitmap.
     */
    public static void init(){
        fonts = new HashMap();
        rects = new HashMap();
    }
    
    /**
     * Adds a bitmap to the cache
     * A lof of this is hardcoded to work with my bitmap, but it can be easily extended.
     * @param key the string this will be referred to as
     * @param image the image data
     */
    public static void addBitmap(String key, Image image){
        if(!fonts.containsKey(key)){
            fonts.put(key, image);
            String s = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789!?:.";
            int prog = 0;
            for(int i = 0; i < 5; i++){
                for(int k = 0; k<9; k++){
                    if(prog < s.length()){
                        rects.put(s.substring(prog, prog+1), new Rectangle(k*16, i*24, 16, 24));
                        prog++;
                    }else{
                        return;
                    }
                }
            }
        }
    }
    
    /**
     * Draws a string using the specified bitmap.
     * A lof of this is hardcoded to work with my bitmap, but it can be easily extended.
     * @param font the key of the font
     * @param startX the X to start at
     * @param startY the Y to start at
     * @param text the test to write
     * @param g the image to write it to
     */
    public static void drawString(String font, int startX, int startY, String text, Graphics g){
        if(!fonts.containsKey(font)){
            return;
        }
        Image bits = fonts.get(font);
        Rectangle r;
        for(int i = 0; i< text.length(); i++){
            if(rects.containsKey(text.substring(i, i+1))){
                r = rects.get(text.substring(i, i+1));
                g.drawImage(bits, startX, startY, startX+r.width, startY+r.height, r.x, r.y, r.x+r.width, r.y+r.height, null);
                startX+=r.width;
            }else{
                startX+=rects.get("A").width;//Put in a space, because I'm dumb and didn't do that in the image.
            }
        }
    }
}
