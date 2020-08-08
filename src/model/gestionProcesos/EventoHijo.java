
package model.gestionProcesos;
import model.general.SO;
import model.general.Proceso;
import test.Generator;

public class EventoHijo {
    
    //parametros
    private final int maxHijos = 4;
    private final int minHijos = 2;

    private int tiempoArriboEnProceso; //tiempo que debe estar ejecutandose un Proceso para que aparezca este evento
    private int cantidadHijos;
    private Boolean activado;
 
    public EventoHijo(Proceso p) {
        activado = true;
        cantidadHijos = Generator.random.nextInt(maxHijos - minHijos + 1) + minHijos;
        //tiempoArriboEnProceso = (Generator.random.nextInt(Math.min(p.getBurstTimeInicial() - 1 , SO.getTimerValue()- 1)));
        tiempoArriboEnProceso = (Generator.random.nextInt(p.getBurstTimeInicial() - 1 ));
    }
    
    public EventoHijo() {
        activado = true;
        cantidadHijos = 0;
        tiempoArriboEnProceso = 0;
    }
    

    public int getTiempoArriboEnProceso() {
        return tiempoArriboEnProceso;
    }

    public int getCantidadHijos() {
        return cantidadHijos;
    }

    public void setTiempoArriboEnProceso(int tiempoArriboEnProceso) {
        this.tiempoArriboEnProceso = tiempoArriboEnProceso;
    }

    public void setCantidadHijos(int cantidadHijos) {
        this.cantidadHijos = cantidadHijos;
    }
    
    public void desactivar() {
        activado = false;
    }

    public Boolean getActivado() {
        return activado;
    }

    @Override
    public String toString() {
        return "( " + tiempoArriboEnProceso + " , " +  cantidadHijos + " )";
    }
    
    
    
}
