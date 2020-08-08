package model.util;

import java.util.Comparator;
import model.general.Proceso;

public class CompBurst  implements Comparator<Proceso> {

    @Override
    public int compare(Proceso o1, Proceso o2) {
        if(o1 == o2) {
            return 0;
        }
      if(o1.getPcb().getBurstTime() == o2.getPcb().getBurstTime()) {
          return o1.getPid() < o2.getPid() ? -1 : 1;
      } 
      
      return o1.getPcb().getBurstTime() < o2.getPcb().getBurstTime() ? -1 : 1;
    }
    
}