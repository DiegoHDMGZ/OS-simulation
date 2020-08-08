/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model.util;

import java.util.Comparator;
import model.general.Proceso;


public class CompPrioridad  implements Comparator<Proceso> {

    @Override
    public int compare(Proceso o1, Proceso o2) {
        if(o1 == o2) {
            return 0;
        }
      if(o1.getPcb().getPrioridad() == o2.getPcb().getPrioridad()) {
          return o1.getPid() < o2.getPid() ? -1 :1;
      } 
      
      return o1.getPcb().getPrioridad() > o2.getPcb().getPrioridad() ? -1 : 1;
    }
    
}