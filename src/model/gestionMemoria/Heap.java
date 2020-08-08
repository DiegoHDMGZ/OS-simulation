
package model.gestionMemoria;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import model.general.SO;
import test.Generator;


public class Heap {
    private int heapBase;
    private int heapLimit;
    
    private LinkedList<Bloque> bloques;
    
    //parametros
    
    public Heap(int heapBase, int heapLimit) {
        this.heapBase = heapBase;
        this.heapLimit = heapLimit;
        
        bloques = new LinkedList<>();
        bloques.add(new Bloque(TipoBloque.Hueco , heapBase, heapLimit));
    }
    
    /*public void mostrarBloques() {
        for(Bloque b : bloques) {
            System.out.println(b);
        }
        System.out.println("");
    }*/
    
    public void changeHeap() {
        bloques = new LinkedList<>();
        int base = heapBase;
        Boolean hueco;
        if(Generator.random.nextInt(2) == 0) {
            hueco = false;
        } else {
            hueco = true;
        }
        int maxSize = (int) Math.round(0.4 * SO.getMaxHeapSize());
        while(base != heapBase + heapLimit) {
            TipoBloque t;
            if(hueco) {
                t = TipoBloque.Hueco;
            } else {
                t = TipoBloque.Heap;
            }
            int lim = Generator.random.nextInt(Math.min(heapLimit - (base - heapBase), maxSize)) + 1;
            bloques.add(new Bloque(t , base , lim));
            
            base = base + lim;
            hueco = !hueco;
        }
        
    }

    public int getHeapBase() {
        return heapBase;
    }

    public int getHeapLimit() {
        return heapLimit;
    }

    public LinkedList<Bloque> getBloques() {
        return bloques;
    }
    
    
   
    
}
