
package model.gestionMemoria;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import model.general.*;

public class MMU {
    
    //parametro
    private double factor = 0.1;
    //variables
    private Estrategia estrategia;
    
    private Map<Integer , Integer> baseProceso;
    private LinkedList<Bloque> bloques;
    
    private double fragmentacion;
    private int cntFragmentacion;
    int cargadosEnMemoria;
    int iteraciones;
    
    public MMU(Estrategia estrategia) {
        this.estrategia = estrategia;
        baseProceso = new HashMap<>();
        bloques = new LinkedList<>();
        bloques.add(new Bloque(TipoBloque.SO , 0 , SO.getMemoria().getSoMemory() ));
        bloques.add(new Bloque(TipoBloque.Hueco ,SO.getMemoria().getSoMemory() , SO.getMemoria().getAvailable()));
        cargadosEnMemoria = 0;
        fragmentacion = 0.0;
        cntFragmentacion = 0;
        iteraciones = 0;
    }

    public long toPhysical(int dir , int idProceso) {
        Integer x = baseProceso.get(idProceso);
        if(x == null){
            return -1;
        }
        
        return (long)dir + (long)x * (long)SO.getMemoria().getFactor();
    }
    
    public void free(Proceso p) {
        int pos = 0;
        for(Bloque actual : bloques) {
            if(actual.getPid() == p.getPid()) {
                if(pos == 0) {
                    Bloque siguiente = bloques.get(pos + 1);
                    if(bloques.size() == 1) {
                        actual.setTipo(TipoBloque.Hueco);
                    } else if(siguiente.getCaracter() == 'H') {
                        actual.setTipo(TipoBloque.Hueco);
                        actual.setLongitud(actual.getLongitud() + siguiente.getLongitud());
                        bloques.remove(pos + 1);
                    } else {
                        actual.setTipo(TipoBloque.Hueco);
                    }
                } else if(pos == bloques.size() - 1) {
                    Bloque anterior = bloques.get(pos - 1);
                    if(anterior.getCaracter() == 'H') {
                        anterior.setLongitud(anterior.getLongitud() + actual.getLongitud());
                        bloques.removeLast();
                    } else {
                        actual.setTipo(TipoBloque.Hueco);
                    }
                } else {
                    Bloque anterior = bloques.get(pos - 1);
                    Bloque siguiente = bloques.get(pos + 1);
                    if(anterior.getCaracter() == 'H') {
                        if(siguiente.getCaracter() == 'H') {
                            anterior.setLongitud(anterior.getLongitud() + actual.getLongitud() + siguiente.getLongitud());
                            bloques.remove(pos + 1);
                            bloques.remove(pos);
                        } else {
                            anterior.setLongitud(anterior.getLongitud() + actual.getLongitud());
                            bloques.remove(pos);
                        }
                    } else if(siguiente.getCaracter() == 'H') {
                        actual.setTipo(TipoBloque.Hueco);
                        actual.setLongitud(actual.getLongitud() + siguiente.getLongitud());
                        bloques.remove(pos + 1);
                    } else {
                        actual.setTipo(TipoBloque.Hueco);
                    }
                }
                break;
            }
            pos++;
        }
        
        baseProceso.remove(p.getPid());
        

    }
    
    public void store(Proceso p , Bloque b , int ind) {
        int sz = p.getPcb().getMemoryLimit();
        if(sz < b.getLongitud()) {
            bloques.add(ind + 1 , new Bloque(TipoBloque.Hueco , b.getInicio() + sz , b.getLongitud() - sz));
        }
        b.setTipo(TipoBloque.Proceso);
        b.setPid(p.getPid());
        b.setLongitud(sz);
        
        baseProceso.put(p.getPid(), b.getInicio());
    }
    
    public Boolean firstFit(Proceso p) {
        int i = 0;
        for(Bloque b : bloques) {
            int sz = p.getPcb().getMemoryLimit();
            if(b.getCaracter() == 'H' && b.getLongitud() >= sz) { 
                store(p , b , i);
                iteraciones += i + 1;
                return true; 
            }
            i++;
        }
        return false;
    }
    
    public Boolean bestFit(Proceso p) {
        int i = 0;
        int ind = -1;
        Bloque elegido = null;
        for(Bloque b : bloques) {
            int sz = p.getPcb().getMemoryLimit();
            if(b.getCaracter() == 'H' && b.getLongitud() >= sz) {
                if(elegido == null || b.getLongitud() < elegido.getLongitud()) {
                    ind = i;
                    elegido = b;
                } 
            }
            i++;
        }
        iteraciones += bloques.size();
        if(elegido == null) {
            return false;
        }
        
        store(p , elegido , ind);
        return true;
    }
    
    public Boolean worstFit(Proceso p) {
        int i = 0;
        int ind = -1;
        Bloque elegido = null;
        
        for(Bloque b : bloques) {
            int sz = p.getPcb().getMemoryLimit();
            if(b.getCaracter() == 'H' && b.getLongitud() >= sz) {
                if(elegido == null || b.getLongitud() > elegido.getLongitud()) {
                    ind = i;
                    elegido = b;
                } 
            }
            i++;
        }
        iteraciones += bloques.size();
        if(elegido == null) {
            return false;
        }
        
        store(p , elegido , ind);
        return true;
    }
    
    public int totalDisponible() {
        int ans = 0;
        for(Bloque b : bloques) {
            if(b.getTipo() == TipoBloque.Hueco) {
                ans += b.getLongitud();
            }
        }
        return ans;
    }
    
    public Boolean allocateProcess(Proceso p) {
        Boolean ans;
        switch(estrategia) {
            case BestFit :
               ans = bestFit(p);
               break;
            case WorstFit :
                ans = worstFit(p);
                break;
            default :
                ans =  firstFit(p);
                break;
        }
        if(ans) {
            cargadosEnMemoria++;
        } else {
            fragmentacion = fragmentacion * cntFragmentacion + 1.0 * totalDisponible() / SO.getMemoria().getSize() ;
            cntFragmentacion++;
            fragmentacion = 1.0 * fragmentacion / cntFragmentacion;
        }
        return ans;
    }
    
    public Estrategia getEstrategia() {
        return estrategia;
    }

    public void setEstrategia(Estrategia estrategia) {
        this.estrategia = estrategia;
    }

    public LinkedList<Bloque> getBloques() {
        return bloques;
    }

    public double getFragmentacion() {
        return fragmentacion;
    }

    public int getCargadosEnMemoria() {
        return cargadosEnMemoria;
    }
    
    public double getTiempoAccesoMemoria() {
       return iteraciones * factor; 
    }
    
    
}
