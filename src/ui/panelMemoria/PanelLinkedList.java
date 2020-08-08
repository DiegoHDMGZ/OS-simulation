
package ui.panelMemoria;

import ui.panelMemoria.LinkedListMemory;
import java.awt.*;
import javax.swing.*;
import model.general.SO;
import model.gestionMemoria.TipoBloque;

public class PanelLinkedList extends JPanel{


    LinkedListMemory lista;
    
    
    private static int w1 = 180;
    private static int w2 = 10;
    private static int w = w1 + w2;
    
    private static Point start;
    private static Point end;
    
    private static int h = 20;
    private static boolean first= true;
    PanelLinkedList(LinkedListMemory lista) {
        this.lista = lista;
    }
    
    @Override
    public void paintComponent(Graphics g) {
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(2));
        g2.drawRect(lista.getX(), lista.getY(), w1, h);
        if(lista.getBloque().getTipo() == TipoBloque.SO){
            g2.setColor(Color.BLUE);
            g2.fillRect(lista.getX()+1, lista.getY()+1, w1*3/16-2, h-2);
            g2.setColor(Color.WHITE);
            g2.drawString(lista.getS(), lista.getX() + 5, lista.getY() + 15);
        }else if(lista.getBloque().getTipo()== TipoBloque.Proceso){
            g2.setColor(Color.GREEN);
            g2.fillRect(lista.getX()+1, lista.getY()+1, w1*3/16-2, h-2);
            g2.setColor(Color.BLACK);
            g2.drawString(lista.getS(), lista.getX() + 5, lista.getY() + 15);
        }else if(lista.getBloque().getTipo() == TipoBloque.Hueco){
            g2.setColor(Color.BLACK);
            g2.fillRect(lista.getX()+1, lista.getY()+1, w1*3/16-2, h-2);
            g2.setColor(Color.WHITE);
            g2.drawString(lista.getS(), lista.getX() + 5, lista.getY() + 15);
        }
        g2.setColor(Color.BLACK);
        g2.drawRect(lista.getX()+ w1*3/16, lista.getY(), w1*17/40, h);
        g2.drawString(lista.getInicio(), lista.getX()+ w1*3/16+5, lista.getY()+15);
        g2.drawString(String.valueOf(lista.getLongitud())+ " " + SO.getMemoria().getUnidades(), lista.getX()+ w1*3/16+5+w1*17/40, lista.getY()+15);
        g2.drawRect(lista.getX() + w1, lista.getY(), w2, h);
        
        Point start;
        Point end;
        //System.out.println(lista.getBloque());
        if(this.lista.getId()-1 == 0){
            //System.out.println(TipoBloque.SO);
            first = false;
        }else{
            if((this.lista.getId()-1)%3!=0){
                start = new Point(lista.getX()- FrameLinkedList.getStep()-5, lista.getY() + 10 );
                end = new Point(lista.getX(),  lista.getY() + 10);      
            }else {
                drawArrowBack(g2);
                start =  new Point(10,lista.getY()+10);
                end = new Point(30,lista.getY()+10);
            }
            Stroke lineStroke = new BasicStroke();
            Stroke arrowStroke = new BasicStroke();
            Arrow.draw(g2, start , end, lineStroke, arrowStroke, 10);
    
        } 
        /*
        if(this.lista.getId()%5!=0){
            start = new Point(lista.getX() + w1 + 5 , lista.getY() + 10 );
            end = new Point(lista.getX() + w1 + 5 + FrameLinkedList.getStep() ,  lista.getY() + 10);      
        }else{
            drawArrowBack(g2);
            start =  new Point(10,lista.getY()+50);
            end = new Point(30,lista.getY()+50);
        }
        Stroke lineStroke = new BasicStroke();
        Stroke arrowStroke = new BasicStroke();
        Arrow.draw(g2, start , end, lineStroke, arrowStroke, 10);
        */
        
        
    }

    public static int getW() {
        return w;
    }
    
    static int getH(){
        return h;
    }
    /*void drawArrowBack(Graphics2D g2){
        g2.drawLine(lista.getX()+w1+5, lista.getY()+10, lista.getX()+w+20,lista.getY()+10);
        g2.drawLine(lista.getX()+w+20, lista.getY()+10, lista.getX()+w+20, lista.getY()+30);
        System.out.println(lista.getX()+w+20);
        g2.drawLine(lista.getX()+w+20, lista.getY()+30, 10, lista.getY()+30);
        g2.drawLine(10, lista.getY()+30, 10, lista.getY()+50);    
    }*/
    void drawArrowBack(Graphics2D g2){
        g2.drawLine(10, lista.getY()+10, 10, lista.getY()-10);
        g2.drawLine(10, lista.getY()-10, 700, lista.getY()-10);
        g2.drawLine(700, lista.getY()-10, 700, lista.getY()-30);
        g2.drawLine(700, lista.getY()-30, 675,lista.getY()-30);
    }
    static void reset() {
        LinkedListMemory.reset();
    }
}
