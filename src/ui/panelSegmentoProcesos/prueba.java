/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panelSegmentoProcesos;

/**
 *
 * @author Rafael
 */
public class prueba {
    private String inicio;
    private String fin;
    private String nombre;
    private int id;
    private int tamanio;
    boolean estado;
    public prueba(String a,String b,String c,int i, int tam,boolean es){
        inicio=a;
        fin=b;
        nombre=c;
        if(nombre=="P"){
            id=i;
        }
        tamanio=tam;
        estado=es;
        
        
        
    }
    public String getInicio(){
        return inicio;
    }

    public String getFin() {
        return fin;
    }

    public String getNombre() {
        return nombre;
    }

    public int getId() {
        return id;
    }

    public int getTamanio() {
        return tamanio;
    }
    public boolean getEstado() {
        return estado;
    }
    
}
