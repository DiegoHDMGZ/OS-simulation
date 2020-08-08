package model.gestionMemoria;

import java.util.LinkedList;
import model.general.SO;
import model.util.Conversor;

public class Bloque {
    
    private TipoBloque tipo;
    private int pid; //solo cuando tipo es 'P'
    private int inicio;
    private int longitud;
    
    //variable auxiliar
    private Boolean separacion;

    public Bloque(TipoBloque tipo, int inicio, int longitud) {
        this.tipo = tipo;
        this.inicio = inicio;
        this.longitud = longitud;
        this.pid = -1;
        this.separacion = false;
    }

    public Bloque(TipoBloque tipo, int pid, int inicio, int longitud) {
        this.tipo = tipo;
        this.pid = pid;
        this.inicio = inicio;
        this.longitud = longitud;
        this.separacion = false;
    }
    
    public Character getCaracter() {
        if(tipo == TipoBloque.Proceso) {
            return 'P';
        } else if(tipo == TipoBloque.Hueco) {
            return 'H';
        } else if(tipo == TipoBloque.SO) {
            return 'S';
        } else {
            return '-';
        }
    }

    public TipoBloque getTipo() {
        return tipo;
    }


    public int getPid() {
        return pid;
    }

    public int getInicio() { //en M
        return inicio;
    }
    
    public long getInicioDecimal() {
        long a = (long)inicio;
        long b = (long) SO.getMemoria().getFactor();
        return a * b;
    }
    
    public long getFinDecimal() {
        long a = (long) (inicio + longitud);
        long b = (long) SO.getMemoria().getFactor();
        return (a * b - 1);
    }

    public int getLongitud() { //en MB
        return longitud;
    }
    
    /*public int getFin() { // en MB
        return inicio + longitud - 1;
    }*/

    public void setTipo(TipoBloque tipo) {
        this.tipo = tipo;
        this.pid = -1;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public void setInicio(int inicio) {
        this.inicio = inicio;
    }

    public void setLongitud(int longitud) {
        this.longitud = longitud;
    }

    @Override
    public String toString() {
        return "( " + tipo + (tipo == TipoBloque.Proceso ? Integer.toString(pid) : "") + " , " + inicio + " , " + longitud + " )";
    }
    
    public String getInicioHexadecimal() {
        long x = getInicioDecimal();
        return Conversor.toHexadecimal(x);
    }
    
    public String getFinHexadecimal() {
        long x = getFinDecimal();
        return Conversor.toHexadecimal(x);
    }
    
    public Boolean enEjeucion() {
        if(tipo != TipoBloque.Proceso) {
            return false;
        }
        
        if(SO.getCpu().getRunning() != null && SO.getCpu().getRunning().getPid() == pid) {
            return true;
        } else {
            return false;
        }
    }
    
    public Boolean esSeparacion(){
        return separacion;
    }
    
    public void activarSeparacion(){
        separacion = true;
    }
    
    public LinkedList<Bloque> getSegmentoProceso() {
        return SO.getSegmentoProceso(pid);
    }
}
