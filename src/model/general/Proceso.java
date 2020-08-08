package model.general;

import model.gestionProcesos.TipoProceso;
import model.gestionProcesos.EventoHijo;
import model.gestionProcesos.Politica;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import test.Generator;

import java.util.Comparator;
import model.gestionMemoria.Heap;
import model.gestionMemoria.Stack;
import model.gestionProcesos.Estado;
import model.io.*;

public class Proceso {
    
    //parametros
    private static final int maxBurstTime = 70;
    private static final int minBurstTime = 4;
    private final int prioridadMaxima = 10;
    private final int prioridadMinima = 0;
    private final int maxCantidadIO = 10;
    private final int minCantidadIO = 0;
    private final int periodo = 20; //solo para la politica priority-time
    private final double factor = 1.0 / periodo; // cantidad de prioridad que disminuye por unidad de tiempo de arribo
    private final int fixNumeroProcesos = 20; //numero de procesos aproximados que se busca tener
    private final int minDataSize = 10; //En MB
    private final int maxDataSize = 60; //En MB
    private final int minCodeSize = 10; //En MB
    private final int maxCodeSize = 70; //En MB
 
    private static int numTotalProceso = 1;
    private static int numProceso = 0;
    
    private PCB pcb;
    private int tiempoArribo;
    private Boolean primeraEjecucion = false;
    private int pidAuxiliar;

    //variables de simulacion
    private EventoHijo eventoHijo;
    private List<EventoIO> eventoIO;
    private int tiempoEsperaMemoria;
    
    //historico
    private List<Proceso> hijosFijo;
    private List<EventoIO> ioFijo;
    
    private Boolean fueAgregado;
    
   
    public Proceso(int tiempoArribo , Politica politica){
        this.tiempoArribo = tiempoArribo;
        hijosFijo = new ArrayList<>();
        ioFijo = new ArrayList<>();
        
        int burstTimeInicial = Generator.random.nextInt(maxBurstTime - minBurstTime + 1) + minBurstTime;
        int prioridad = Generator.random.nextInt(prioridadMaxima - prioridadMinima + 1) + prioridadMinima;
        
        //memoria
        int codeLimit = Generator.random.nextInt(maxCodeSize - minCodeSize + 1) + minCodeSize;
        int dataLimit = Generator.random.nextInt(maxDataSize - minDataSize + 1) + minDataSize;
        int heapLimit = SO.getMaxHeapSize();
        int stackLimit = SO.getMaxStackSize();

        if(politica == Politica.PRIORITY_TIME) {
            this.pcb = new PCB(burstTimeInicial , prioridad - factor * tiempoArribo , codeLimit, dataLimit, heapLimit, stackLimit);
        } else {
            this.pcb = new PCB(burstTimeInicial , prioridad , codeLimit, dataLimit, heapLimit, stackLimit);
        }
        pcb.setPid(++numProceso);
        if(pcb.getPid() <= 10 /*|| Generator.random.nextInt(2) == 0*/) {
            pcb.setTipo(TipoProceso.SO);
        } else {
            pcb.setTipo(TipoProceso.Usuario);
        }
        eventoHijo = new EventoHijo(this);
        if(numTotalProceso >= fixNumeroProcesos) {
            eventoHijo.setCantidadHijos(0);
            eventoHijo.setTiempoArriboEnProceso(-1);
            eventoHijo.desactivar();
        } else if(numTotalProceso + eventoHijo.getCantidadHijos() > fixNumeroProcesos) {
            eventoHijo.setCantidadHijos(fixNumeroProcesos - numTotalProceso);
        }
        
        generarListaIO(burstTimeInicial);

        if(politica == Politica.MULTILEVEL) {
            if(Generator.random.nextInt(2) == 0/*pcb.getTipo() == TipoProceso.SO*/) {
                this.pcb.setPolitica(Politica.PRIORITY);
            } else {
                this.pcb.setPolitica(Politica.RR);
            }
        } else{
            this.pcb.setPolitica(politica);
        }
       
        numTotalProceso += eventoHijo.getCantidadHijos();
        tiempoEsperaMemoria = 0;
        
        fueAgregado = false;
    }
    
    public Proceso(int tiempoArribo , Politica politica , int idPadre , int iter) {
        this.tiempoArribo = tiempoArribo;
        Proceso p;
        
        int id;
        if(idPadre == -1) {
            id = 0;
        } else {
            Proceso padre = SO.getProcesosFijados().get(idPadre - 1);
            /*System.out.println(padre);
            System.out.println(padre.getHijosFijo().size());*/
            id = padre.getHijosFijo().get(iter).getPid() - 1;
        }
  
        p = SO.getProcesosFijados().get(id);
        
        if(politica == Politica.PRIORITY_TIME) {
            this.pcb = new PCB(p.getBurstTimeInicial() , p.getPrioridad() - factor * tiempoArribo, 
            p.getCodeLimit() , p.getDataLimit() , p.getHeap().getHeapLimit() , p.getStack().getStackLimit());
        } else {
            this.pcb = new PCB(p.getBurstTimeInicial() , p.getPrioridad(),
            p.getCodeLimit() , p.getDataLimit() , p.getHeap().getHeapLimit() , p.getStack().getStackLimit());
        }
        
        //this.pcb.setTipo(p.pcb.getTipo());
        this.eventoHijo = new EventoHijo();
        this.eventoHijo.setCantidadHijos(p.getEventoHijo().getCantidadHijos());
        this.eventoHijo.setTiempoArriboEnProceso(p.getEventoHijo().getTiempoArriboEnProceso());
        
        this.eventoIO = new ArrayList<>();
        //this.eventoIO = p.getIoFijo();
        for(EventoIO e : p.getIoFijo()) {
            this.eventoIO.add(e);
        } 
        hijosFijo = new ArrayList<>(); //no altera el de la primera iteracion
        ioFijo = new ArrayList<>();
        
        if(idPadre == -1) {
            this.pidAuxiliar = 1;
            //this.pcb.setPid(1);
        } else {
            this.pidAuxiliar = id + 1;
            //this.pcb.setPid(id + 1);
        }
        pcb.setPid(++numProceso);
        
        if(pcb.getPid() <= 10 /*|| Generator.random.nextInt(2) == 0*/) {
            pcb.setTipo(TipoProceso.SO);
        } else {
            pcb.setTipo(TipoProceso.Usuario);
        }
        
        if(politica == Politica.MULTILEVEL) {
            if(Generator.random.nextInt(2) == 0/*pcb.getTipo() == TipoProceso.SO*/) {
                this.pcb.setPolitica(Politica.PRIORITY);
            } else {
                this.pcb.setPolitica(Politica.RR);
            }
        } else{
            this.pcb.setPolitica(politica);
        }
       
        numTotalProceso += eventoHijo.getCantidadHijos();
        tiempoEsperaMemoria = 0;
        fueAgregado = false;
    }
    
    public Proceso(int tiempoArribo, Politica politica, int burst, int prioridad, int memoria) {
        this.tiempoArribo = tiempoArribo;
        hijosFijo = new ArrayList<>();
        ioFijo = new ArrayList<>();
        
        if(memoria < 4) {
            memoria = 4;
        }
        //memoria
        int codeLimit = memoria / 4;
        int dataLimit = memoria / 4;
        int heapLimit = memoria / 4;
        int stackLimit = memoria - codeLimit - dataLimit - heapLimit;

        if(politica == Politica.PRIORITY_TIME) {
            this.pcb = new PCB(burst , prioridad - factor * tiempoArribo , codeLimit, dataLimit, heapLimit, stackLimit);
        } else {
            this.pcb = new PCB(burst , prioridad , codeLimit, dataLimit, heapLimit, stackLimit);
        }
        pcb.setPid(++numProceso);
        if(pcb.getPid() <= 10 /*|| Generator.random.nextInt(2) == 0*/) {
            pcb.setTipo(TipoProceso.SO);
        } else {
            pcb.setTipo(TipoProceso.Usuario);
        }
        eventoHijo = new EventoHijo(this);
        eventoHijo.setCantidadHijos(0);
        eventoHijo.setTiempoArriboEnProceso(-1);
        eventoHijo.desactivar();
        generarListaIO(burst);

        if(politica == Politica.MULTILEVEL) {
            if(Generator.random.nextInt(2) == 0/*pcb.getTipo() == TipoProceso.SO*/) {
                this.pcb.setPolitica(Politica.PRIORITY);
            } else {
                this.pcb.setPolitica(Politica.RR);
            }
        } else{
            this.pcb.setPolitica(politica);
        }
       
        numTotalProceso += eventoHijo.getCantidadHijos();
        tiempoEsperaMemoria = 0;
        
        fueAgregado = true;
    }

    public int getPidAuxiliar() {
        return pidAuxiliar;
    }
    
    
    
    private void generarListaIO(int burstTimeInicial) {
        eventoIO = new ArrayList<>();
        int cantidadIO = Generator.random.nextInt((int) Math.ceil(1.0 * burstTimeInicial / (maxBurstTime / maxCantidadIO)) - minCantidadIO + 1) + minCantidadIO;
        List<Integer> posibilidades = new ArrayList<>();
        for(int i = 0; i < burstTimeInicial; i++) {
            posibilidades.add(i);
        }
        int elegidos[] = new int[cantidadIO];
        
        for(int i = 0; i < cantidadIO; i++) {
            int x = Generator.random.nextInt(posibilidades.size());
            elegidos[i] = posibilidades.get(x) ;
            posibilidades.remove(x);
        }
        
        Arrays.sort(elegidos);
        //System.out.println("P" + this.getPid());
        for(int i = 0; i < cantidadIO; i++) {
            EventoIO e = new EventoIO(elegidos[i]);
            eventoIO.add(e);
            ioFijo.add(e);
            
            //System.out.println("Evento " + i + e);
        }
    }

    public static int getMaxBurstTime() {
        return maxBurstTime;
    }

    public static int getNumProceso() {
        return numProceso;
    }

    public int getPid() {
        return pcb.getPid();
    }

    public PCB getPcb() {
        return pcb;
    }

    public int getTiempoArribo() {
        return tiempoArribo;
    }

    public int getBurstTimeInicial() {
        return pcb.getBurstTimeInicial();
    }

    public void setPid(int pid) {
        this.pcb.setPid(pid);
    }

    public void setPcb(PCB pcb) {
        this.pcb = pcb;
    }

    public double getPrioridad() {
        return pcb.getPrioridad();
    }

    public void setTiempoArribo(int tiempoArribo) {
        this.tiempoArribo = tiempoArribo;
    }

    public TipoProceso getTipo() {
        return pcb.getTipo();
    }

    public EventoHijo getEventoHijo() {
        return eventoHijo;
    }

    public void setPrimeraEjecucion(Boolean primeraEjecucion) {
        this.primeraEjecucion = primeraEjecucion;
    }
    
    public int getTiempoEjecucionTotal() {
        return pcb.getBurstTimeInicial() - pcb.getBurstTime();
    }

    @Override
    public String toString() {
        /*Proceso padre = pcb.getAncestro();
        if(padre == null) {
            return "( P" + pcb.getPid() + " , estado = " + pcb.getEstado() +", " + "burst = " + pcb.getBurstTime() +" , " + "prioridad = " + pcb.getPrioridad() + "," + "tdevice = " + pcb.getTiempoDeviceRestante() + " , "  + pcb.getAncestro() + " , " + pcb.getPolitica() +" , eventoHijo = " + eventoHijo +" )";
        } else {
            return "( P" + pcb.getPid() + " , estado = " + pcb.getEstado() + ", " + "burst = " + pcb.getBurstTime() +" , " + "prioridad = " + pcb.getPrioridad() + "," +  "tdevice = " + pcb.getTiempoDeviceRestante() + " , "  + "P"+ pcb.getAncestro().getPid() + " , " + pcb.getPolitica() + " , eventoHijo = "+ eventoHijo+ " )";
        }*/
        return "P" + pcb.getPid();
    }
    
    public void addTiempoRetorno() {
        pcb.setTiempoRetorno(pcb.getTiempoRetorno() + 1);
    }
    
    public void addTiempoEspera() {
        pcb.setTiempoEspera(pcb.getTiempoEspera() + 1);
    }
    
    public void addTiempoRespuesta() {
        if(!primeraEjecucion) {
            pcb.setTiempoRespuesta(pcb.getTiempoRespuesta() + 1);
        }
        
    }
    
    public int getTiempoRetorno() {
        return pcb.getTiempoRetorno();
    }
    
    public int getTiempoEspera() {
        return pcb.getTiempoEspera();
    }
    
    public int getTiempoRespuesta() {
        return pcb.getTiempoRespuesta();
    }
    
    public double getCPUUse() {
        return pcb.getCPUuse();
    }
    public static void reset() {
        numProceso = 0;
        numTotalProceso = 1;
    }
    
    public void addHijo(Proceso p) {
        hijosFijo.add(p);
        pcb.addHijo(p);
    }

    public List<Proceso> getHijosFijo() {
        return hijosFijo;
    }
    
    /*public void kill() {
        pcb.generarCodigoError();
    }*/

    public int getCodeBase() {
        return pcb.getCode().getInicio();
    }

    public int getCodeLimit() {
        return pcb.getCode().getLongitud();
    }

    public int getDataBase() {
        return pcb.getData().getInicio();
    }

    public int getDataLimit() {
        return pcb.getData().getLongitud();
    }

    public Heap getHeap() {
        return pcb.getHeap();
    }

    public Stack getStack() {
        return pcb.getStack();
    }

    public List<EventoIO> getIoFijo() {
        return ioFijo;
    }
    
    public EventoIO nextIO() {
        if(eventoIO.isEmpty()) {
            return null;
        } else {
            return eventoIO.get(0);
        }
    }
    
    public void removeEventoIO() {
        eventoIO.remove(0);
    }
    
    public Boolean esAnormal() {
        return pcb.getBurstTime() > 0 && pcb.getEstado() == Estado.Terminado;
    }

    public int getTiempoEsperaMemoria() {
        return tiempoEsperaMemoria;
    }

    public void setTiempoEsperaMemoria(int tiempoEsperaMemoria) {
        this.tiempoEsperaMemoria = tiempoEsperaMemoria;
    }
    
    public void incresaseTiempoEsperaMemoria(){
        tiempoEsperaMemoria++;
    }

    public Boolean getFueAgregado() {
        return fueAgregado;
    }
    
    
}
