package model.general;

import model.gestionProcesos.TipoProceso;
import model.io.Device;
import model.gestionProcesos.Error;
import model.gestionProcesos.ListaErrores;
import model.gestionProcesos.Estado;
import model.gestionProcesos.Politica;
import java.util.ArrayList;
import java.util.List;
import model.gestionMemoria.Bloque;
import model.gestionMemoria.Heap;
import model.gestionMemoria.Stack;
import model.gestionMemoria.TipoBloque;
import model.util.Conversor;
import test.Generator;

public class PCB {
    

    
    //variables
    private int pid;
    private Estado estado; 
    private TipoProceso tipo;
    private int PC; 
    private int burstTime;
    private double prioridad;
    private Proceso ancestro;
    private List<Proceso> hijos;
    private Device device;
    private int tiempoDeviceRestante;
    private Politica politica; //solo es util en el multilevel
    private int tiempoEjecucion;
    
    private int tiempoRetorno;
    private int tiempoRespuesta;
    private int tiempoEspera;
    
    private int tiempoEjecucionTotal;
    private int burstTimeInicial;
    
    //private int codigoError;
    //private String descripcionError;
    
    //Todos los limites de memoria están en MB
    private int memoryLimit;
    
    private Bloque code;
    private Bloque data;
    private Heap heap;
    private Stack stack;
    
    public PCB(int burstTime, double prioridad, int codeLimit, int dataLimit, int heapLimit, int stackLimit) {
        estado = Estado.Nuevo;
        
        hijos = new ArrayList<>();
        this.burstTime = burstTime;
        this.prioridad = prioridad;
        this.burstTimeInicial = burstTime;
        tiempoDeviceRestante = 0;
        device = null;
        tiempoEjecucion = 0;
        
        tiempoRetorno = 0;
        tiempoRespuesta = 0;
        tiempoEspera = 0;
        //codigoError = -1;
        //descripcionError = "";
        
        code = new Bloque(TipoBloque.Code, 0, codeLimit);
        data = new Bloque(TipoBloque.Data, codeLimit, dataLimit);
        
        heap = new Heap(this.data.getInicio() + this.data.getLongitud() , heapLimit);
        stack = new Stack(heap.getHeapBase() + heap.getHeapLimit() , stackLimit);
        this.memoryLimit = stack.getStackBase() + stack.getStackLimit();
        PC = 0;
    }
    
    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public int getPC() {
        return PC;
    }
    
    public long getPCFisico() {
        return SO.getMmu().toPhysical(PC, pid);
    }

    public void setPC(int PC) {
        this.PC = PC;
    }

    public int getBurstTime() {
        return burstTime;
    }

    public void setBurstTime(int burstTime) {
        this.burstTime = burstTime;
    }

    public double getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public Proceso getAncestro() {
        return ancestro;
    }

    public void setAncestro(Proceso ancestro) {
        this.ancestro = ancestro;
    }

    public List<Proceso> getHijos() {
        return hijos;
    }
    
    public double getCPUuse() {
        if(tiempoRetorno == 0) {
            return 0;
        }

        return (double)(tiempoEjecucionTotal) / tiempoRetorno * 100.0;
    }

    public Device getDevice() {
        return device;
    }

    public void setDevice(Device device) {
        this.device = device;
    }
    
    public void nextPC() {
        PC = Generator.random.nextInt(code.getLongitud() * SO.getMemoria().getFactor());
    }
    
    public void addHijo(Proceso p) {
        hijos.add(p);
    }
    
    public void removeHijo(Proceso p) {
       hijos.remove(p);
    }

    public int getTiempoDeviceRestante() {
        return tiempoDeviceRestante;
    }
    
    public void useDevice() {
        tiempoDeviceRestante--;
        if(tiempoDeviceRestante == 0) {
            device = null;
        }
    }

    public int getTiempoEjecucion() {
        return tiempoEjecucion;
    }

    public void setTiempoEjecucion(int tiempoEjecucion) {
        this.tiempoEjecucion = tiempoEjecucion;
    }

    public void setTiempoDeviceRestante(int tiempoDeviceRestante) {
        this.tiempoDeviceRestante = tiempoDeviceRestante;
    }

    public Politica getPolitica() {
        return politica;
    }

    public void setPolitica(Politica politica) {
        this.politica = politica;
    }

    public int getTiempoRetorno() {
        return tiempoRetorno;
    }

    public void setTiempoRetorno(int tiempoRetorno) {
        this.tiempoRetorno = tiempoRetorno;
    }

    public int getTiempoRespuesta() {
        return tiempoRespuesta;
    }

    public void setTiempoRespuesta(int tiempoRespuesta) {
        this.tiempoRespuesta = tiempoRespuesta;
    }

    public int getTiempoEspera() {
        return tiempoEspera;
    }

    public void setTiempoEspera(int tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public TipoProceso getTipo() {
        return tipo;
    }

    public void setTipo(TipoProceso tipo) {
        this.tipo = tipo;
    }
    
    
    
    public String toStringHijos() {
        String s = "";
        
        for(int i = 0; i < hijos.size(); i++) {
            Proceso p = hijos.get(i);
            if(i != 0) {
                s += " , ";
            }
            s = s + "P" + p.getPid() ;
            
        }
        
        return s;
    }
    
    public String hexadecimalPC() {
        return Conversor.toHexadecimal(getPCFisico());//Integer.toHexString(getPCFisico()).toUpperCase();
    }

    @Override
    public String toString() {
        String prior ;
        if(politica != Politica.PRIORITY_TIME) {
            prior = Integer.toString((int) (prioridad + 0.1));
        } else {
            prior = String.format("%.2f" , prioridad);
        }
        
        String ans =  " ID = " + pid + "\n"
                + " Estado = " + estado + "\n"
                + " Tipo de proceso = " + tipo + "\n"
                + " Burst Time Inicial = " + burstTimeInicial + "\n"
                + " Burst Time Restante = " + burstTime + "\n"
                + " Prioridad = " + prior + "\n"
                + " Ancestro = " + (ancestro == null ? " " : "P" + ancestro.getPid()) + "\n"
                + " Hijos = [ " + toStringHijos() + " ]\n"
                + " Dispositivo = " + (device == null ? " " : device) + "\n"
                + " Tiempo restante en dispositivo = " + tiempoDeviceRestante + "\n"
                + " Politica = " + politica + "\n"
                + " Tiempo de Retorno = " + tiempoRetorno + "\n"
                + " Tiempo de Respuesta = " + tiempoRespuesta + "\n"
                + " Tiempo de Espera = " + tiempoEspera + "\n";
        
        /*if(codigoError != -1) {
            ans = ans + " codigoError = " + codigoError + "\n" + " descripcionError = " + descripcionError + "\n";
        }*/
        /*ans = ans 
                + " codeBase = " + code.getInicio() + " KB\n"
                + " codeLimit = " + code.getLongitud() + " KB\n"
                + " dataBase = " + data.getInicio() + " KB\n"
                + " dataLimit = " + data.getLongitud() + " KB\n"
                + " heapBase = " + heap.getHeapBase() + " KB\n"
                + " heapLimit = " + heap.getHeapLimit() + " KB\n"
                + " stackBase = " + stack.getStackBase() + " KB\n"
                + " stackLimit = " + stack.getStackLimit() + " KB\n"
                + " curStack = " + SO.getMmu().toPhysical(stack.getDirStack(), pid)   + "\n";*/
        
        long x = SO.getMmu().toPhysical(0, pid);
        if(x != -1) {
            ans = ans
                + " PC = " + getPCFisico()  + " ( hex = " + hexadecimalPC() + " )\n"
                + " Direccion inicial = " + x + " ( hex = " + Conversor.toHexadecimal(x) + " ) \n";         
        } else {
            ans = ans 
                + " PC = NO ESTÁ EN MEMORIA\n"
                + " Direccion inicial = NO ESTÁ EN MEMORIA\n";
        }
        ans = ans + " Tamaño = " + memoryLimit +" " + SO.getMemoria().getUnidades() +"\n";
        return  ans;
    }

    public int getTiempoEjecucionTotal() {
        return tiempoEjecucionTotal;
    }

    public void setTiempoEjecucionTotal(int tiempoEjecucionTotal) {
        this.tiempoEjecucionTotal = tiempoEjecucionTotal;
    }

    public int getBurstTimeInicial() {
        return burstTimeInicial;
    }

    public void setBurstTimeInicial(int burstTimeInicial) {
        this.burstTimeInicial = burstTimeInicial;
    }
    
    /*public void generarCodigoError() {
        Error e = ListaErrores.getError();
        codigoError = e.getCodigo();
        descripcionError = e.getDescripcion();
    }*/

    public Heap getHeap() {
        return heap;
    }

    public Stack getStack() {
        return stack;
    }

    public int getMemoryLimit() {
        return memoryLimit;
    }

    public Bloque getCode() {
        return code;
    }

    public Bloque getData() {
        return data;
    }
    
    
 
}    
   
