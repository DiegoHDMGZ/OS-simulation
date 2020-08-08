
package model.io;

import java.util.Comparator;
import model.io.Device;
import model.general.Proceso;
import test.Generator;

public class Interrupcion {
    private int tiempoArribo;
    private int codigo;
    private TipoInterrupcion tipo;
    private Proceso proceso; //solo para Interrupciones IO
    private Device device;
    private EstadoInterrupcion estado;
    

    /*public Interrupcion(int tiempoArribo , Proceso p) {
        this.tiempoArribo = tiempoArribo;
        this.tipo = TipoInterrupcion.TIMER;
        this.proceso = p;
        this.device = null;
        this.estado = EstadoInterrupcion.Pendiente;
        this.codigo = tipo.getCodigo();
    }*/
    
    public Interrupcion(int tiempoArribo, Proceso p, Device d) {
        this.tiempoArribo = tiempoArribo;
        this.tipo = TipoInterrupcion.IO;
        this.proceso = p;
        this.device = d;
        this.estado = EstadoInterrupcion.Pendiente;
        this.codigo = tipo.getCodigo() + Integer.parseUnsignedInt(d.getCodigo(), 2);
    }
    
    public Interrupcion(int tiempoArribo , Proceso p) {
        this.tiempoArribo = tiempoArribo;
        this.proceso = p;
        this.device = null;
        this.estado = EstadoInterrupcion.Pendiente;
        
        String opcionesCodigo[] = {"DIVIDE" , "NULL" , "FLOATING" };
        
        this.tipo = TipoInterrupcion.valueOf(opcionesCodigo[Generator.random.nextInt(opcionesCodigo.length)]);
        this.codigo = tipo.getCodigo();
    }

    public Interrupcion(int tiempoArribo, Proceso p , TipoInterrupcion tipo) {
        this.tiempoArribo = tiempoArribo;
        this.tipo = tipo;
        this.codigo = tipo.getCodigo();
        this.proceso = p;
        this.estado = EstadoInterrupcion.Pendiente;
        this.device = null;
    }
    
    

    public int getTiempoArribo() {
        return tiempoArribo;
    }

    public TipoInterrupcion getTipo() {
        return tipo;
    }

    public Proceso getProceso() {
        return proceso;
    }
    
    public Device getDevice(){
        return device;
    }

    public EstadoInterrupcion getEstado() {
        return estado;
    }

    public void setEstado(EstadoInterrupcion estado) {
        this.estado = estado;
    }
    
    @Override
    public String toString() {
        String s = "(tiempoArribo = " + tiempoArribo  + " tipo = " + tipo + " device = " + device + " estado = " + estado  
                + " devolver = P" + ( (proceso != null) ? proceso.getPid() : null) + " )" ;
        return s;
    }

    public int getCodigo() {
        return codigo;
    }
    
    
    
    
    
}
