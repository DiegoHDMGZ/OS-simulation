
package test;

import java.util.Random;
import model.general.SO;

public class Generator {
   private static int seed = 37; 
    public static Random random;

    public Generator() {
        random = new Random(seed);
    }
    
    public static void changeSeed() {
        seed = (int) System.currentTimeMillis();
        random = new Random(seed);
        SO.clearFijados();
    }
    
    
    
}
