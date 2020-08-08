
package model.gestionProcesos;

import model.general.SO;
import model.general.Proceso;

public class Dispatcher {
    //context switch cambiar bit modo, 
    //next pcb
    public void contextSwitch(Proceso saliente , Estado estado) {
        saliente.getPcb().setEstado(estado);
        SO.setBitEstado(0);
        saliente.getPcb().setTiempoEjecucion(0);
        SO.setTimer(SO.getTimerValue());
        if(estado == Estado.Terminado) {
            Proceso ancestro = saliente.getPcb().getAncestro();
           
            SO.getScheduler().removeProceso(saliente);
            if(ancestro != null) {
                ancestro.getPcb().removeHijo(saliente);
                if(ancestro.getPcb().getHijos().isEmpty()) {
                    SO.getScheduler().addToReadyQueue(ancestro);
                }
            }
           
           
        } else {
            if(estado == Estado.Listo) {
                SO.getScheduler().addToReadyQueue(saliente);
            }
            
        }
        SO.getCpu().free();
    }
    
    public void contextSwitch(Proceso entrante) {
        SO.setTimer(SO.getTimerValue());
        entrante.getPcb().setEstado(Estado.Ejecutando);
        if(entrante.getTipo() == TipoProceso.SO) {
            SO.setBitEstado(0);
        } else {
            SO.setBitEstado(1);
        }
        SO.getCpu().setRunning(entrante);
        SO.getScheduler().removeFromReadyQueue(entrante);
    }
}
