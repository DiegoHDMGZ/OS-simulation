package ui.panelMemoria;

import model.gestionMemoria.Bloque;


public class LinkedListMemory {   
    
    private int idLinkedLink;
    private int x, y;
    private String s;
    
    private Bloque bloque;
    private String inicio;
    private Integer longitud;
    
    private static int id = 0;
    
    
    public LinkedListMemory(int x, int y, Bloque bloque) {       
        this.bloque = bloque;
        this.idLinkedLink = ++id;
        this.x = x;
        this.y = y;
        this.inicio = bloque.getInicioHexadecimal();
        this.longitud = bloque.getLongitud();
        if(bloque.getCaracter()=='H'){
            this.s = "H";
        }else if(bloque.getCaracter()=='P'){
            this.s = "P"+String.valueOf(bloque.getPid());
        }else{
            this.s = "SO";
        }
    }
    public LinkedListMemory(int x, int y) {
       
        this.idLinkedLink = ++id;
        this.x = x;
        this.y = y;
        this.s = "P" + Integer.toString(id);
        
    } 
    public String getInicio(){
        return this.inicio;
    }
    
    public Integer getLongitud(){
        return this.longitud;
    }
    
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public String getS() {
        return s;
    }
    
    public int getId(){
        return idLinkedLink;
    }
    public Bloque getBloque(){
        return this.bloque;
    }
    static void reset() {
        //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
        id=0;
    }
    
}
