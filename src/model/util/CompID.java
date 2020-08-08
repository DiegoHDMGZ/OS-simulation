package model.util;

import java.util.Comparator;
import model.general.Proceso;

public class CompID implements Comparator<Proceso> {

    @Override
    public int compare(Proceso o1, Proceso o2) {
        if(o1 == o2) {
            return 0;
        }
     
        return o1.getPid() < o2.getPid() ? -1 : 1;
     
    }
    
}
