package model.util;


public class Conversor {
    private final static int cifras = 8;
    
    public static String toHexadecimal(long x) {
        String ans =  Long.toHexString(x).toUpperCase();
        int l = ans.length();
        for(int i = 0; i < (cifras - l); i++) {
            ans = "0" + ans;
        }
        return ans;
    }
    
    public static Integer fromMBtoGB(int x) {
        return x / (1 << 10);
    }
}
