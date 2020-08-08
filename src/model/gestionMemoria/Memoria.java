package model.gestionMemoria;

public class Memoria {
    //Es byte-addressable : Decir que direcciona 4GB es decir que hay 2^32 direcciones (4G) cada una apuntando a un Byte
    private final int soMemory = 1096; //En MB
    private final int available = 3000;//8000; //En MB
    private final int size = available + soMemory; //En MB
    private final int factor = (1 << 20); //factor a multiplicar. Como son MB (20 bits), el factor es 2 ^ 20
    //(1 << 20) es un bitwise operator para obtener 2^20
    private final String unidades = "MB";
    
    public int getSoMemory() {
        return soMemory;
    }

    public int getAvailable() {
        return available;
    }

    public int getSize() {
        return size;
    }

    public int getFactor() {
        return factor;
    }

    public String getUnidades() {
        return unidades;
    }
    
    
    

}
