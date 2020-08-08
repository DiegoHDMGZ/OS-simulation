package model.general;

public class CPU {
    private Proceso running;
    
    public void exec() {
        running.setPrimeraEjecucion(true);
        running.getPcb().setBurstTime(running.getPcb().getBurstTime() - 1);
        running.getPcb().nextPC();
        running.getPcb().setTiempoEjecucion(running.getPcb().getTiempoEjecucion() + 1);
        running.getPcb().setTiempoEjecucionTotal(running.getPcb().getTiempoEjecucionTotal() + 1);
        
        if(running.getPcb().getBurstTime() == 0) {
            running.getPcb().getStack().changeStack(true);
        } else {
            running.getPcb().getStack().changeStack(false);
        }
        running.getHeap().changeHeap();
        
        /*System.out.println("Para P" + running.getPid() + " : ");
        System.out.println("Stack = ");
        running.getPcb().getStack().mostrarBloques();
        System.out.println("Heap = ");
        running.getPcb().getHeap().mostrarBloques();*/
        SO.decreaseTimer();
    }

    public Proceso getRunning() {
        return running;
    }

    public void setRunning(Proceso running) {
        this.running = running;
    }
    
    public void free() {
        running = null;
    }
    
}
