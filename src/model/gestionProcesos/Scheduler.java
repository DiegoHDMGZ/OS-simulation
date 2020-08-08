
package model.gestionProcesos;

import java.util.ArrayList;
import java.util.Collections;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import model.util.*;
import model.general.*;
import model.io.*;

public class Scheduler {
    
    private Politica politica;
  
    private  Boolean expropiativo; //Solo es elegible en caso de PRIORITY , SJF , PRIORITY_TIME
    //FCFS es no expropaitivo, RR es expropiativo ambos por default , Multinivel es expropiativo por default
    
    private List<Proceso> jobQueue;
    private List<Proceso> readyQueue; //RR or FCFS
    private TreeSet<Proceso> readyQueue2; //SJF o PRIORITY o PRIORITY_TIME
    //en multilevel : readyQueue = RR , readyQueue2 = Priority . La cola de Priority tiene prioridad
    private Map<Device , List<Proceso> > deviceQueue;
    
    public Scheduler(Politica politica , Boolean expropiativo , Proceso init ) {
        this.politica = politica;
        if(politica == Politica.FCFS) {
            this.expropiativo = false;
        } else {
            if(politica == Politica.RR || politica == Politica.MULTILEVEL) {
                this.expropiativo = true;
            } else {
                this.expropiativo = expropiativo;
            }
            
        }
        readyQueue = new ArrayList<>(); 
        jobQueue = new ArrayList<>() ;
       
        deviceQueue = new HashMap<>();
        for(Device d : SO.getDevices()) {
            deviceQueue.put(d, new ArrayList<>());
        }
        
        if(politica == Politica.SJF ) {
            readyQueue2 = new TreeSet<>(new CompBurst());
        } else if(politica == Politica.PRIORITY || politica == Politica.MULTILEVEL || politica == Politica.PRIORITY_TIME) {
            readyQueue2 = new TreeSet<>(new CompPrioridad());
        } 
        
        addProceso(init);
    }
    
    public Proceso nextFCFS() {
        return readyQueue.get(0);
    }
    
    public Proceso nextSJF() {
        return readyQueue2.first();
    }
    
    private Proceso nextRR() {
       return readyQueue.get(0);
    }
    
    private Proceso nextPriority() {
        return readyQueue2.first();
    }
    
    private Proceso nextMultilevel() {
        if(!readyQueue2.isEmpty()) {
            return nextPriority();
        } else {
            return nextRR();
        }
    }
    
    public Proceso nextPriorityTime() {
        return nextPriority();
    }
    
    public Proceso nextProcess() {
        switch(politica) {
            case FCFS: return nextFCFS();
            case RR : return nextRR(); 
            case SJF: return nextSJF();
            case PRIORITY : return nextPriority();
            case MULTILEVEL : return nextMultilevel();
            default : return nextPriorityTime();
        }
    }
    
    public Boolean hayProcesos() {
        switch (politica) {
            case FCFS:
            case RR:
                return !readyQueue.isEmpty();
            case SJF:
            case PRIORITY:
            case PRIORITY_TIME:
                return !readyQueue2.isEmpty();
            default:
                return !readyQueue.isEmpty() || !readyQueue2.isEmpty();
        }
       
    }
    
    public List<Proceso> getReadyQueue() {
        return readyQueue;
    }

    public TreeSet<Proceso> getReadyQueue2() {
        return readyQueue2;
    }
    
    public void addProceso(Proceso p) {
        p.getPcb().setEstado(Estado.Listo);
        jobQueue.add(p);
        addToReadyQueue(p);
    }
   
    public void addToReadyQueue(Proceso p) {
        if(politica == Politica.FCFS || politica == Politica.RR) {
            readyQueue.add(p);
        } else if(politica == Politica.SJF || politica == Politica.PRIORITY || politica == Politica.PRIORITY_TIME) {
            readyQueue2.add(p);
        } else {
            if(p.getPcb().getPolitica() == Politica.RR) {
                readyQueue.add(p);
            } else {
                readyQueue2.add(p);
            }
        }
        p.getPcb().setEstado(Estado.Listo);
        
    }
    
    public void removeFromReadyQueue(Proceso p) {
        if(politica == Politica.FCFS || politica == Politica.RR) {
            readyQueue.remove(p);
        } else if(politica == Politica.SJF || politica == Politica.PRIORITY || politica == Politica.PRIORITY_TIME) {
            readyQueue2.remove(p);
        } else {
            if(p.getPcb().getPolitica() == Politica.RR) {
                readyQueue.remove(p);
            } else {
                readyQueue2.remove(p);
            }
        }
    }
    
    public void removeProceso(Proceso p) {
        removeFromReadyQueue(p);
        jobQueue.remove(p);
    }
    
    public void addToDeviceQueue(Device d, Proceso p) {
        deviceQueue.get(d).add(p);
        readyQueue.remove(p);
    }
    
    public Politica getPolitica() {
        return politica;
    }

    public Boolean getExpropiativo() {
        return expropiativo;
    }

    public List<Proceso> getJobQueue() {
        return jobQueue;
    }
    
    public Map<Device, List<Proceso>> getDeviceQueue() {
        return deviceQueue;
    }

 
    
    
}
