
package model.io;


public enum TipoInterrupcion {
    IO (60), TIMER (40) , DIVIDE(0) , NULL(2) , FLOATING(16) , MEMORY(50);
    
    private int codigo;

    private TipoInterrupcion(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }

    @Override
    public String toString() {
        if(codigo == 60) {
            return "IO";
        }
        if(codigo == 40) {
            return "TIMER";
        }
        if(codigo == 0) {
            return "DIVIDE ERROR";
        }
        if (codigo == 2) {
            return "NULL INTERRUPT";
        }
        if(codigo == 16) {
            return "FLOATING-POINT ERROR";
        }
        if(codigo == 50) {
            return "INSUFICIENTE MEMORIA";
        }
        return "";
    }
    
    
    
}
