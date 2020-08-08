
package model.gestionProcesos;

public enum Politica {
    FCFS(0), RR(1) , MULTILEVEL(2) , PRIORITY(3) , SJF(4), PRIORITY_TIME(5);
    //Multilevel = RR + Priority
    private int cod;
    
    Politica(int cod) {
        this.cod = cod;
    }  

    public int getCod() {
        return cod;
    }
    
}
