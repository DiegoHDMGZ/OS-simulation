
package model.gestionMemoria;

import java.util.LinkedList;
import model.general.SO;
import test.Generator;


public class Stack {
    LinkedList<Bloque> bloques;
    private int stackBase; //en KB
    private int stackLimit; //en KB
    private int posStack; //La posicion actual del stack en KB
    private int dirStack; //direccion logica
    
    public Stack(int stackBase, int stackLimit) {
        
        this.stackBase = stackBase;
        this.stackLimit = stackLimit;
        this.posStack = stackBase + stackLimit - 1;
        this.dirStack = (stackBase + stackLimit - 1) * SO.getMemoria().getFactor();
        this.bloques = new LinkedList<>();
        actualizar();
    }
    
    public int getStackBase() {
        return stackBase;
    }

    public int getStackLimit() {
        return stackLimit;
    }

    public int getPosStack() {
        return posStack;
    }

    public LinkedList<Bloque> getBloques() {
        return bloques;
    }

    public int getDirStack() {
        return dirStack;
    }

    public void mostrarBloques() {
        for(Bloque b : bloques) {
            System.out.println(b);
        }
        System.out.println("");
    }
    
    public void actualizar() {
        this.bloques = new LinkedList<>();
        
        if(posStack != stackBase) {
            Bloque b = new Bloque(TipoBloque.Hueco, stackBase, posStack - stackBase);
            bloques.add(b);
        }
        
        if(posStack != stackBase + stackLimit - 1) {
            Bloque b = new Bloque(TipoBloque.Stack, posStack,  stackBase + stackLimit - posStack );
            bloques.add(b);
        }
        bloques.getFirst().activarSeparacion();
    }

    public void changeStack(Boolean ultimo) {
        if(ultimo) {
            posStack = (stackBase + stackLimit - 1);
        } else {
            posStack = Generator.random.nextInt(stackLimit )  + stackBase ;
        } 
        dirStack = Generator.random.nextInt(SO.getMemoria().getFactor()) + posStack * SO.getMemoria().getFactor();
        actualizar();
    }

}
