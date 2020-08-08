package model.general;

import model.io.TipoInterrupcion;
import model.io.EstadoInterrupcion;
import model.io.Interrupcion;
import model.gestionProcesos.*;
import model.io.*;
import model.util.*;
import model.gestionMemoria.*;

import static java.lang.Thread.sleep;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import test.Generator;

class Data {
    int burst, prioridad , memoria;

    public Data(int burst, int prioridad, int memoria) {
        this.burst = burst;
        this.prioridad = prioridad;
        this.memoria = memoria;
    }
    
}

public class SO {
    
    //parametros
    private final static int fixTimerValue = 15;
    private final static double porcentajeContextSwitch = 0.05;
    private final static int maxStackSize = 75; //En MB
    private final static int maxHeapSize = 90; //En MB
    private final static double probabilidadAnormal = 0.01;
    private final double probabilidadInterrupcion = 0.01;
    private final int maxTiempoEsperaMemoria = 250;
    //debug
    private final static Boolean debug = false; //True para debugging
    
    //Componentes
    private static CPU cpu;
    private static List<Device> devices;
    private static Memoria memoria;
    
    //Planificacion
    private static Scheduler scheduler;
    private  Dispatcher dispatcher;
    private static int timer;
    private static MMU mmu;
    
    private static int quantum; //0 si es que no tiene RR
    
    private static int bitEstado; // 0 = SO, 1 = User
    
    private int tiempoContextSwitch;
    
    private List<Interrupcion> interrupciones;
    private List<Interrupcion> historicoInterrupciones;
    private List<Proceso> anormales;
    private static List<Proceso> historicoProcesos;
    
    private List<Proceso> esperaMemoria;
   
    private static List<Proceso> procesosFijados; //Para repetir los mismos procesos
    
    private static int tiempoSimulacion; //el tiempo que va la simulacion
    private static int timerValue;
    
    private List<Data> adicionales;
    
    public void precalcular() {
        //primera iteracion
        devices = new ArrayList<>();
        devices.add(new Device("HD" , "0001"));
        devices.add(new Device("Impresora" , "0010"));
        devices.add(new Device("Pantalla" , "0011"));
        List<Proceso> procesosPrecalculados = new ArrayList<>();
        Generator g = new Generator();
        Proceso.reset();
        memoria = new Memoria();
        Proceso p = new Proceso(0 , Politica.FCFS);
        
        procesosPrecalculados.add(p);
        
        int i = 0;
        while(i < procesosPrecalculados.size()) {
            Proceso padre  = procesosPrecalculados.get(i);
            if(padre.getEventoHijo().getActivado()) {
                for(int c = 0; c < padre.getEventoHijo().getCantidadHijos(); c++) {
                    Proceso hijo = new Proceso(0, Politica.FCFS);
                    procesosPrecalculados.add(hijo);
                    padre.addHijo(hijo);
                    hijo.getPcb().setAncestro(padre);
                }
                padre.getEventoHijo().desactivar();
            }
            
            
            i++;
        }
        //System.out.println("size = " + procesosPrecalculados.size());
        procesosFijados = procesosPrecalculados;
        procesosFijados.sort(new CompID()); //evaluar si es necesario ordenar
        /*for(Proceso x : procesosFijados) {
            System.out.println(x);
        }*/
        
        /*System.out.println("Termino");
        System.out.println("");
        System.out.println("");*/
    }
    
    public SO(Politica politica , Boolean expropiativo , int quantum , Estrategia estrategia) {
        if(procesosFijados == null) {
            precalcular();
        }
        adicionales = new ArrayList<>();
        /*System.out.println("ENTRO");
        if(procesosFijados == null) {
            System.out.println("PF es null");
        } else {
            System.out.println("PF NO es null");
        }*/
        tiempoSimulacion  = 0;
        tiempoContextSwitch = 0;
        bitEstado = 0;
        
        cpu = new CPU();
        memoria = new Memoria();
        mmu = new MMU(estrategia);
        /*if(procesosFijados == null) {
            devices = new ArrayList<>();
            devices.add(new Device("HD" , "0001"));
            devices.add(new Device("Impresora" , "0010"));
            devices.add(new Device("Pantalla" , "0011"));
        }*/
        
        if(politica == Politica.RR || politica == Politica.MULTILEVEL) {
            this.timerValue = quantum;
        } else {
            this.timerValue = fixTimerValue;
        }
        timer = timerValue;
        
        
        interrupciones = new ArrayList<>();
 //     Generator g = new Generator();
        Proceso.reset();
        
        anormales = new ArrayList<>();
        historicoInterrupciones= new ArrayList<>();
        historicoProcesos = new ArrayList<>();
        esperaMemoria = new ArrayList<>();
        
    
        Proceso init;
        //if(procesosFijados != null) {
            
            if(debug) {
                System.out.print("procesosFijados = [");
                for(Proceso p : procesosFijados) {
                    System.out.print("P" + p.getPid() + " , ");
                }
                System.out.println("]");
            }
            init = new Proceso(0 , politica , -1, -1);
        //} else {
         //   init = new Proceso(0, politica);
        //}
        
        if(debug) {
            System.out.println("Evento Hijo = " + init.getEventoHijo());
        }
        
        
        
        scheduler = new Scheduler(politica, expropiativo , init);
        dispatcher = new Dispatcher();
        
    
        this.quantum = quantum;
        
        historicoProcesos.add(init);
        
        mmu.allocateProcess(init);
        
    }
    
    public SO(Politica politica , Boolean expropiativo , int quantum ) {
        this(politica , expropiativo, quantum, Estrategia.FirstFit);
    }
    
    /*public SO(Politica politica, Boolean expropiativo, Estrategia estrategia) {
        this(politica, expropiativo, 0 , estrategia);
    }*/
   
    void fork(int cantidad ) {
        if(scheduler.getJobQueue().size() == 1) {
            for(Data d : adicionales) {
                Proceso p = new Proceso(tiempoSimulacion + 1 , scheduler.getPolitica() , d.burst, d.prioridad , d.memoria);
                historicoProcesos.add(p);
                cpu.getRunning().addHijo(p);
                p.getPcb().setAncestro(cpu.getRunning());
                Boolean hayMemoria = mmu.allocateProcess(p);

                if(hayMemoria) {
                    scheduler.addProceso(p);
                } else {
                    esperaMemoria.add(p);
                }
            }
        }
        for(int i = 0; i < cantidad; i++) {
            Proceso p;
            if(procesosFijados != null) {
                p = new Proceso(tiempoSimulacion + 1 , scheduler.getPolitica() , cpu.getRunning().getPidAuxiliar() , i);
            } else {
                p = new Proceso(tiempoSimulacion + 1, scheduler.getPolitica());
            }
            historicoProcesos.add(p); 
            cpu.getRunning().addHijo(p);
            p.getPcb().setAncestro(cpu.getRunning());
            
            Boolean hayMemoria = mmu.allocateProcess(p);
            //scheduler.addProceso(p);
            
            if(hayMemoria) {
                scheduler.addProceso(p);
            } else {
                esperaMemoria.add(p);
            }
            //System.out.println("haciendo fork con " + p);
        }
    }
    
    void handleIOrequest(EventoIO eIO) {
        //cambiar pcb , agregar proceso a la deviceQueue
        cpu.getRunning().getPcb().setDevice(eIO.getDevice());
        scheduler.addToDeviceQueue(eIO.getDevice(), cpu.getRunning());
        cpu.getRunning().getPcb().setTiempoDeviceRestante(eIO.getDuracion());
    }
    
    void callSD(Estado salienteEstado) { //call scheduler and dispatcher (saliente + entrante)
        Proceso siguiente = null;
        if(scheduler.hayProcesos()) {
            siguiente = scheduler.nextProcess();
        }
        
        if(! (salienteEstado == Estado.Terminado && siguiente == null )) {
            tiempoContextSwitch += Math.ceil((double)cpu.getRunning().getPcb().getTiempoEjecucion() * porcentajeContextSwitch);
        } 
        if(salienteEstado == Estado.Terminado) {
            mmu.free(cpu.getRunning());
        }
        dispatcher.contextSwitch(cpu.getRunning() , salienteEstado);
      
        if(siguiente != null) {
            dispatcher.contextSwitch(siguiente);
        } 
    }
    
    void callSD() { //call scheduler and dispatcher (solo entrante)
        Proceso siguiente = null;
        if(scheduler.hayProcesos()) {
            siguiente = scheduler.nextProcess();
        }

        if(siguiente != null) {
            dispatcher.contextSwitch(siguiente);
            tiempoContextSwitch++;
        } else {
          cpu.free();
        }
    }
    
    private void recalcularTiempos() {
        for(Proceso p : scheduler.getJobQueue()) {
            p.addTiempoRetorno();
            p.addTiempoRespuesta();
            if(p.getPcb().getEstado() == Estado.Listo) {
                p.addTiempoEspera();
            }
        }
    }
    
    private void handleInterrupt() {
                 
        if(interrupciones.get(0).getTipo() == TipoInterrupcion.IO) {
            scheduler.addToReadyQueue(interrupciones.get(0).getProceso());
        }

        if(scheduler.getExpropiativo()) {
            if(interrupciones.get(0).getTipo() == TipoInterrupcion.TIMER) {
                if(timerValue >= fixTimerValue && Generator.random.nextDouble() <= probabilidadAnormal && cpu.getRunning().getPid() != 0 ) {
                    if(debug) {
                        System.out.println("P" + cpu.getRunning().getPid() + " MURIO en tiempo " + tiempoSimulacion);
                    }

                    anormales.add(cpu.getRunning());
                    //cpu.getRunning().kill();
                    dispatcher.contextSwitch(cpu.getRunning(), Estado.Terminado);
                    
                    
                }
            } 

            if(cpu.getRunning() != null) {
                dispatcher.contextSwitch(cpu.getRunning(), Estado.Listo);
            }
            
            interrupciones.get(0).setEstado(EstadoInterrupcion.Manejada);
        } else {
            interrupciones.get(0).setEstado(EstadoInterrupcion.Ignorada);
        }
        //en caso sea no expropiativo, simplemente agrega a la cola de listos pero no expropia del CPU

        interrupciones.remove(0);
    }
    
    public void addInterrupcion(Interrupcion interrupcion) {
        interrupciones.add(interrupcion);
        historicoInterrupciones.add(interrupcion);   
    }
  
    public Boolean run(int delay) { //devuelve true si la simulacion todavia continua

        try {
            sleep(delay);
        } catch (InterruptedException ex) {
            Logger.getLogger(SO.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        Boolean huboContextSwitch = false;
        if(tiempoContextSwitch > 0) {
            tiempoContextSwitch--;
            //tiempoSimulacion++;
            if(debug) {
                System.out.println("----CAMBIO DE  CONTEXTO----");
            }
            
            recalcularTiempos();
            huboContextSwitch = true;
            //return true;
        }
        
        if(!huboContextSwitch && scheduler.getJobQueue().isEmpty()) {
            //simulacion terminada
            /*if(procesosFijados == null) {
                procesosFijados = new ArrayList<>();
                for(Proceso p : historicoProcesos) {
                    procesosFijados.add(p);
                }
            }*/
            return false;
        }
        
        Boolean huboInterrupcion = false;
        
        if(!huboContextSwitch && !interrupciones.isEmpty()) {
            huboInterrupcion = true;
            handleInterrupt();
        }
        
        
        if(!huboContextSwitch && !huboInterrupcion) {
            if(cpu.getRunning() == null) {
                callSD();
                if(cpu.getRunning() != null) {
                    //caso especial en el que no habia ningun proceso ejecutadose y entra uno nuevo
                    //llamos a run nuevamente para poder ir al cambio de contexto
                    return run(delay);
                }
                recalcularTiempos();

            } else {
                //gestionar eventos
                EventoHijo eH = cpu.getRunning().getEventoHijo();
                EventoIO eIO = cpu.getRunning().nextIO();
                Boolean bloquear = false;
                if(eH.getCantidadHijos() > 0 &&
                    eH.getTiempoArriboEnProceso() == cpu.getRunning().getTiempoEjecucionTotal() && cpu.getRunning().getEventoHijo().getActivado()) {
                    fork( eH.getCantidadHijos());
                    bloquear = true;
                    cpu.getRunning().getEventoHijo().desactivar();

                } else if(eIO != null && eIO.getTiempoArribo() == cpu.getRunning().getTiempoEjecucionTotal()) {
                    bloquear = true;
                    handleIOrequest(eIO);
                    cpu.getRunning().removeEventoIO();
                    if(debug) {
                        System.out.println("P"+ cpu.getRunning().getPid() + " ENTRO EN EVENTO IO");
                        System.out.println(cpu.getRunning());
                        System.out.println("tDevice = " + cpu.getRunning().getPcb().getTiempoDeviceRestante());
                    }  
                } 

                if(!bloquear) {
                    cpu.exec();
                }
                recalcularTiempos();
                if(bloquear || cpu.getRunning().getPcb().getBurstTime() == 0 /*|| (quantum == cpu.getRunning().getPcb().getTiempoEjecucion() && cpu.getRunning().getPcb().getPolitica() == Politica.RR)*/) {
                    //llamar a planificador y despachador
                    if(bloquear) {
                        callSD(Estado.Bloqueado);
                    } else if ( cpu.getRunning().getPcb().getBurstTime() == 0) {
                        callSD(Estado.Terminado);
                    } else {
                        if(debug) {
                            System.out.println("--------------QUANTUM TERMINADOOOO-----------");
                        }
                        
                        callSD(Estado.Listo);
                        
                        System.out.println("timer = " + timer );
                        System.out.println("huboContextSwitch = " + huboContextSwitch);
                        
                    }
                } 
            }
        } else if (!huboContextSwitch){
            recalcularTiempos();
        }
  
        tiempoSimulacion++;
        
        //agregamos las interrupciones de timer y de IO que se hayan generado
        
        if(!huboContextSwitch && timer == 0) {
            if(cpu.getRunning() == null && debug) {
                System.out.println("------------------RUNNING NULL EN ADD-----------------------");
            }
            Interrupcion interrupcion = new Interrupcion(tiempoSimulacion, cpu.getRunning(), TipoInterrupcion.TIMER );
            addInterrupcion(interrupcion);
            timer = timerValue;
        } 
        
        if(!huboContextSwitch && cpu.getRunning() != null && Generator.random.nextDouble() <= probabilidadInterrupcion) {
            addInterrupcion(new Interrupcion(tiempoSimulacion , cpu.getRunning() ));
        }
        
        for(Device d : devices) {
            List<Proceso> q = scheduler.getDeviceQueue().get(d);
            if(!q.isEmpty()) {
                Proceso p = q.get(0);

                p.getPcb().useDevice();
                if(p.getPcb().getTiempoDeviceRestante() == 0) {
                    Interrupcion interrupcion = new Interrupcion(tiempoSimulacion , p , d);
                    addInterrupcion(interrupcion);
   
                    q.remove(0);
                }
            }   
        }
        
        if(!esperaMemoria.isEmpty()) {
            int i = 0;
            while(i < esperaMemoria.size()) {
                Proceso p = esperaMemoria.get(i);
                if(p.getPcb().getMemoryLimit() > SO.getMemoria().getAvailable()) {
                    p.getPcb().setEstado(Estado.Terminado);
                    anormales.add(p);
                    esperaMemoria.remove(p);
                    Proceso ancestro = p.getPcb().getAncestro();
           
                    if(ancestro != null) {
                        ancestro.getPcb().removeHijo(p);
                        if(ancestro.getPcb().getHijos().isEmpty()) {
                            scheduler.addToReadyQueue(ancestro);
                        }
                    }
           
                    continue;
                }
                if(mmu.allocateProcess(p)) {
                    scheduler.addProceso(p);
                    esperaMemoria.remove(p);
                    continue;
                } else {
                    p.incresaseTiempoEsperaMemoria();
                }
                
                if(p.getTiempoEsperaMemoria() == maxTiempoEsperaMemoria) {
                    p.getPcb().setEstado(Estado.Terminado);
                    anormales.add(p);
                    esperaMemoria.remove(p);
                    Proceso ancestro = p.getPcb().getAncestro();
           
                    if(ancestro != null) {
                        ancestro.getPcb().removeHijo(p);
                        if(ancestro.getPcb().getHijos().isEmpty()) {
                            scheduler.addToReadyQueue(ancestro);
                        }
                    }
           
                    continue;
                }
                
                i++;
            }
        }
        return true;
    }
    
    public int getTiempoRetornoMinimo() {
        Boolean primero = true;
        
        int mini = 0;
        for(Proceso p : historicoProcesos) {
            if(!p.esAnormal()) {
                if(primero) {
                    primero = false;
                    mini = p.getTiempoRetorno();
                } else {
                    mini = Math.min(mini, p.getTiempoRetorno());
                }
            }
        }
        return mini;
    }
    
    public int getTiempoRetornoMaximo() {
        Boolean primero = true;
        int maxi = 0;
        for(Proceso p : historicoProcesos) {
            if(!p.esAnormal()) {
                if(primero) {
                    primero = false;
                    maxi = p.getTiempoRetorno();
                } else {
                    maxi = Math.max(maxi, p.getTiempoRetorno());
                }
            }
        }
        return maxi;
    }
       
    public double getTiempoRetornoMedio() {
        double medio = 0;
        int cont = 0;
        for(Proceso p : historicoProcesos) {
            if(!p.esAnormal()) {
                medio +=  p.getTiempoRetorno();
                cont++;
            }
        }
        if(cont == 0) {
            return 0;
        }
        medio /= cont;
        return medio;
    }
    
    public int getTiempoRespuestaMinimo() {
        int mini = 0;
        Boolean primero = true;
        for(Proceso p : historicoProcesos) {
            if(!p.esAnormal()) {
                if(primero) {
                    primero = false;
                    mini = p.getTiempoRespuesta();
                } else {
                    mini = Math.min(mini, p.getTiempoRespuesta());
                }
            }
        }
        return mini;
    }
    
    public int getTiempoRespuestaMaximo() {  
        Boolean primero = true;
        int maxi = 0;
        for(Proceso p : historicoProcesos) {
            if(!p.esAnormal()) {
                if(primero) {
                    primero = false;
                    maxi = p.getTiempoRespuesta();
                } else {
                    maxi = Math.max(maxi, p.getTiempoRespuesta());
                }
            }
        }  
        return maxi;
    }
       
    public double getTiempoRespuestaMedio() {
        double medio = 0;
        int cont = 0;
        for(Proceso p : historicoProcesos) {
            if(!p.esAnormal()) {
                medio +=  p.getTiempoRespuesta();
                cont++;
            }
           
        }
        if(cont == 0) {
            return 0;
        }
        medio /= cont;
        return medio;
    }
    
    public int getTiempoEsperaMinimo() {
        int mini = 0;
        Boolean primero = true;
        for(Proceso p : historicoProcesos) {
            if(!p.esAnormal()) {
                if(primero) {
                    primero = false;
                    mini = p.getTiempoEspera();
                } else {
                    mini = Math.min(mini, p.getTiempoEspera());
                }
            }
        }
        
        return mini;
    }
    
    public int getTiempoEsperaMaximo() {
        Boolean primero = true;
        int maxi = 0;
        for(Proceso p : historicoProcesos) {
            if(!p.esAnormal()) {
                if(primero) {
                    primero = false;
                    maxi = p.getTiempoEspera();
                } else {
                    maxi = Math.max(maxi, p.getTiempoEspera());
                }
            }
        }  
        return maxi;
    }
       
    public double getTiempoEsperaMedio() {
        double medio = 0;
        int cont = 0;
        for(Proceso p : historicoProcesos) {
            if(!p.esAnormal()) {
                medio +=  p.getTiempoEspera();
                cont++;
            }
            
        }
        if(cont == 0) {
            return 0;
        }
        medio /= cont;
        return medio;
    }
    
    public double getRendimiento() {
        if(tiempoSimulacion == 0) {
            return 0;
        }
        int terminados = historicoProcesos.size() - scheduler.getJobQueue().size() - anormales.size();
        return (double)terminados / tiempoSimulacion;
    }
    
    public double getUsoCPUMinimo() {
        double mini = 0;
        Boolean primero = true;
        for(Proceso p : historicoProcesos) {
            if(!p.esAnormal()) {
                if(primero) {
                    primero = false;
                    mini = p.getCPUUse();
                } else {
                    mini = Math.min(mini, p.getCPUUse());
                }
            }
        }
        return mini;
    }
    
    public double getUsoCPUMaximo() {
        Boolean primero = true;
        double maxi = 0;
        for(Proceso p : historicoProcesos) {
            if(!p.esAnormal()) {
                if(primero) {
                    primero = false;
                    maxi = p.getCPUUse();
                } else {
                    maxi = Math.max(maxi, p.getCPUUse());
                }
            }
        }  
        return maxi;
    }
       
    public double getUsoCPUMedio() {
        double medio = 0;
        int cont = 0;
        for(Proceso p : historicoProcesos) {
            if(!p.esAnormal()) {
                medio +=  p.getCPUUse();
                cont++;
            }
            
        }
        if(cont == 0) {
            return 0;
        }
        medio /= cont;
        return medio;
    }
    
    public double getFragmentacionPromedio() { //fragmentacion % = disponible / RAM en cada intento fallido
        return mmu.getFragmentacion();
    }
    
    public int getCargadosEnMemoria() {
        return mmu.getCargadosEnMemoria();
    }
    
    public double getTiempoAccesoMemoria() {
        return mmu.getTiempoAccesoMemoria();
    }

    public  Dispatcher getDispatcher() {
        return dispatcher;
    }

    public static int getTiempoSimulacion() {
        return tiempoSimulacion;
    }

    public static List<Device> getDevices() {
        return devices;
    }

    public static Scheduler getScheduler() {
        return scheduler;
    }

    public static int getBitEstado() {
        return bitEstado;
    }

    public static void setBitEstado(int bitEstado) {
        SO.bitEstado = bitEstado;
    }

    public List<Interrupcion> getHistoricoInterrupciones() {
        return historicoInterrupciones;
    }

    /*public static int getFixTimerValue() {
        return fixTimerValue;
    }*/

    public static int getTimerValue() {
        return timerValue;
    }
    
    public List<Proceso> getAnormales() {
        return anormales;
    }

    public static void setTimer(int timer) {
        SO.timer = timer;
    }

    public static void decreaseTimer() {
        timer--;
    }
    
    public void printListas() {
        System.out.println("T = " + tiempoSimulacion);
        System.out.println("running = " + cpu.getRunning());
        List<Proceso> v = scheduler.getJobQueue();
        System.out.print("jobQueue = [ ");
        
        Boolean empieza = true;
        for(Proceso p : v) {
            if(!empieza) {
                System.out.print(" ; ");
            }
            System.out.print(p );
            empieza = false;
        }
        System.out.println(" ]");
        
        if(scheduler.getPolitica() == Politica.FCFS || scheduler.getPolitica() == Politica.RR || scheduler.getPolitica() == Politica.MULTILEVEL) {
            v = scheduler.getReadyQueue();
            System.out.print("readyQueue = [ ");

            empieza = true;
            for(Proceso p : v) {
                if(!empieza) {
                    System.out.print(" ; ");
                }
                System.out.print(p );
                empieza = false;
            }
            System.out.println(" ]");
        } 
        
        if(scheduler.getPolitica() == Politica.PRIORITY || scheduler.getPolitica() == Politica.SJF || scheduler.getPolitica() == Politica.MULTILEVEL || scheduler.getPolitica() == Politica.PRIORITY_TIME){
            TreeSet<Proceso> s  = scheduler.getReadyQueue2();
            System.out.print("readyQueue2 = [ ");

            empieza = true;
            for(Proceso p : s) {
                if(!empieza) {
                    System.out.print(" ; ");
                }
                System.out.print(p );
                empieza = false;
            }
            System.out.println(" ]");
        }
       
        
        for(Device d : devices) {
            System.out.print("deviceQueue[" + d + "] = [");
            v = scheduler.getDeviceQueue().get(d);
            empieza = true;
            for(Proceso p : v) {
                if(!empieza) {
                    System.out.print(" ; ");
                }
                System.out.print(p );
                empieza = false;
            }
            System.out.println(" ]");
         
        }
        System.out.println("");
    }

    public List<Proceso> getHistoricoProcesos() {
        return historicoProcesos;
    }
    
    public void printHistorico() {
        System.out.println("Historico =  ");
        Boolean empieza = true;
        for(Proceso p : historicoProcesos) {
            System.out.println(p );
            empieza = false;
        }
        System.out.println("");
    }

    public static List<Proceso> getProcesosFijados() {
        return procesosFijados;
    }
    
    public static void clearFijados() {
        procesosFijados = null;
    }

   public static int getQuantum() {
        return quantum;
    }

    public static CPU getCpu() {
        return cpu;
    }

    public static int getMaxStackSize() {
        return maxStackSize;
    }

    public static int getMaxHeapSize() {
        return maxHeapSize;
    }
    
    public Politica getPolitica() {
        return scheduler.getPolitica();
    }

    public static Memoria getMemoria() {
        return memoria;
    }
    
    public int getSizeMemoria() {
        return memoria.getSize();
    }

    public static MMU getMmu() {
        return mmu;
    }
    
    public LinkedList<Bloque> getListaEnlazada(){
        return mmu.getBloques();
    }
    
    public static LinkedList<Bloque> getSegmentoProceso(int pid) {
        Proceso p = null;
        for(Proceso x : historicoProcesos) {
            if(x.getPid() == pid) {
                p = x;
                break;
            }
        }
        
        if(p == null) {
            return new LinkedList<>();
        }
        LinkedList<Bloque> ans = new LinkedList<>();
        ans.add(p.getPcb().getCode());
        ans.add(p.getPcb().getData());
        ans.addAll(p.getPcb().getHeap().getBloques());
        ans.addAll(p.getPcb().getStack().getBloques());
                
        return ans;
    }
    
    public void addProcesoTemporal(int burst, int prioridad, int memoria) {
        adicionales.add(new Data(burst, prioridad, memoria));
    }
}
