
package model.io;

import model.general.SO;
import test.Generator;

public class EventoIO {
    //evento de requerimiento de IO de un proceso
    
    //parametros
    private final int maxDuracion = 30;
    private final int minDuracion = 10;
    
    private Device device;
    private int duracion;
    private int tiempoArribo;

    public EventoIO(int tiempoArribo){
        //activado = true;
        int n = SO.getDevices().size();
        device = SO.getDevices().get(Generator.random.nextInt(n));
        duracion = Generator.random.nextInt(maxDuracion - minDuracion + 1) + minDuracion;

        this.tiempoArribo = tiempoArribo;
    }

    public Device getDevice() {
        return device;
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDevice(Device device) {
        this.device = device;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public int getTiempoArribo() {
        return tiempoArribo;
    }

    public void setTiempoArribo(int tiempoArribo) {
        this.tiempoArribo = tiempoArribo;
    }
    
     @Override
    public String toString() {
        return "( tiempoAribo = " + tiempoArribo + " , duracion = " +  duracion + " , device = " + device + ")";
    }
    
    
}
