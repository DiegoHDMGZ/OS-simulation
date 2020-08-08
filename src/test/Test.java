package test;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;
import model.general.SO;
import model.io.Interrupcion;
import model.gestionProcesos.Politica;
import model.general.Proceso;
import model.gestionMemoria.Bloque;
import model.gestionMemoria.Estrategia;
import model.io.EventoIO;

public class Test {
    
    public void mostrarInterrupciones(SO so) {
        List<Interrupcion> l = so.getHistoricoInterrupciones();
        System.out.println("Interruciones = ");
        int i = 1;
        for(Interrupcion x : l) {
            System.out.println(i + " : " + x);
            i++;
        }
    }
    
    public void mostrarEstadisticas(SO so) {
        System.out.println("Tiempo Espera Minimo = " + so.getTiempoEsperaMinimo());
        System.out.println("Tiempo Espera Medio = " + so.getTiempoEsperaMedio());
        System.out.println("Tiempo Espera Maximo = " + so.getTiempoEsperaMaximo());
        
        System.out.println("Tiempo Retorno Minimo = " + so.getTiempoRetornoMinimo());
        System.out.println("Tiempo Retorno Medio = " + so.getTiempoRetornoMedio());
        System.out.println("Tiempo Retorno Maximo = " + so.getTiempoRetornoMaximo());
        
        System.out.println("Tiempo Respuesta Minimo = " + so.getTiempoRespuestaMinimo());
        System.out.println("Tiempo Respuesta Medio = " + so.getTiempoRespuestaMedio());
        System.out.println("Tiempo Respuesta Maximo = " + so.getTiempoRespuestaMaximo());
        
        System.out.println("Uso CPU Minimo = " + so.getUsoCPUMinimo());
        System.out.println("Uso CPU Medio = " + so.getUsoCPUMedio());
        System.out.println("Uso CPU Maximo = " + so.getUsoCPUMaximo());
      
        System.out.println("Rendimiento = " + so.getRendimiento());
    }
      
    /*public static void main(String[] args) {
        int maxi = 2000;
        //Generator.changeSeed();
       
        SO so = new SO(Politica.FCFS , false  , 0 , Estrategia.FirstFit);
           
        //cpu.printListas();
        *//*System.out.println("Lista enlazada = ");
        for(Bloque b : so.getListaEnlazada()) {
            System.out.println(b);
        }*//*
        while(*//*maxi > 0 &&*//* so.run(0) ) {
            for(Bloque b : so.getListaEnlazada()) {
                if(b.getInicioDecimal() < 0 ) {
                    System.out.println("NEGATIVO");
                    System.out.println(b.getInicio());
                    System.out.println(b.getInicioDecimal());
                            
                }
            }
            maxi--;
        }
        
      
       
    }  */
}
