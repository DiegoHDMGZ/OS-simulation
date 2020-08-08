/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ui.panelSegmentoProcesos;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import static java.lang.Integer.max;
import java.util.LinkedList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.GroupLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.LayoutStyle;
import javax.swing.WindowConstants;
import model.general.SO;
import model.gestionMemoria.Bloque;
import model.gestionMemoria.TipoBloque;

/**
 *
 * @author Rafael
 */
public class SegmentoMemoria extends JPanel{
    Color colorProcesoEjecutando=new Color(56, 255, 56);      
    Color colorSO=new Color(78, 164, 171);    
    Color colorHueco=new Color(111, 116, 117  );
    Color colorProceso=new Color(241, 133, 67  );
    int tamanioMemoria=3000;
    int tamanioSegmento=500;
    int multiplicador=2;
    public SegmentoMemoria(String inicio,String fin,TipoBloque tipo,int id,int tamanio,boolean estado,LinkedList <Bloque> segmentos,boolean memoriaOsegmento ){
        new JPanel();
        JLabel jLabel1 = new JLabel();
        
        JLabel jLabel2 = new JLabel();
        JLabel jLabel3 = new JLabel();
        jLabel1.setText(inicio);
        jLabel2.setText(fin);     
        int longitud=tamanio*11/50;//tamaño del bloque;
        int longitud1,longitud2,longitud3;
        if(memoriaOsegmento){
            longitud=longitud*multiplicador;
        }
        if(tipo==TipoBloque.Hueco){
            setBackground(colorHueco);
            longitud1=longitud*2/10;
            longitud2=longitud*6/10;
            longitud3=longitud*2/10;
            if(!memoriaOsegmento){            
                longitud=tamanio*4;
                longitud1=12;
                longitud2=longitud*4/10;
                longitud3=12;
                if(longitud<25){
                    longitud1=8;
                    longitud2=8;
                    longitud3=8;
                }
            }
            
            jLabel3.setText(" ("+tamanio+ " " +  SO.getMemoria().getUnidades() + ")");
            if(longitud<3){
                jLabel3.setText("");
                
            }
        }else if(tipo==TipoBloque.Proceso){
            longitud1=longitud*3/10;
            longitud2=longitud*4/10;
            longitud3=longitud*3/10;
            jLabel3.setText("P"+id+" ("+tamanio+ " " +  SO.getMemoria().getUnidades() + ")");
            if(estado){
                setBackground(colorProcesoEjecutando);
            }else{
                setBackground(colorProceso);                
            }
            if(!memoriaOsegmento){
                jLabel3.setText("");
                setBackground(new Color(114, 230, 249  ));
            }
        }else if(tipo==TipoBloque.SO){
            longitud1=longitud*2/10;
            longitud2=longitud*6/10;
            longitud3=longitud*2/10;
            
            
            jLabel3.setText("S.O."+" ("+tamanio+ " " +  SO.getMemoria().getUnidades() + ")");
            setBackground(colorSO);
        }else{
            longitud=max(tamanio*4,12);
            longitud1=12;
            longitud3=12;
            if(longitud<20){
            longitud2=longitud*6/10;
           
            }else{                
                longitud2=longitud*4/10;
            } 
            
            jLabel3.setText(tipo.name()+" ("+tamanio+ " " +  SO.getMemoria().getUnidades() + ")");
            if(tipo==TipoBloque.Code){                
                setBackground(new Color(35,50,80)); 
            }
            if(tipo==TipoBloque.Data){
                setBackground(new Color(241, 133, 67 )); 
            }
            if(tipo==TipoBloque.Stack){                
                setBackground(new Color(38, 178, 161)); 
            }
            if(tipo==TipoBloque.Heap){
                setBackground(new Color(192, 47, 128 ));
                if(id==1){
                    jLabel3.setText(" ("+tamanio+ " " +  SO.getMemoria().getUnidades() + ")");
                }
                
            }
        }
      
        //setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);  
        JPanel memoria = new JPanel();
        BoxLayout box = new BoxLayout(memoria, BoxLayout.Y_AXIS);
        memoria.setLayout(box);
        //memoria.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        memoria.removeAll();
        boolean heap=false;
        for(Bloque bloque: segmentos){ 
            if(bloque.esSeparacion()){
                 SegmentoMemoria proceso2= new SegmentoMemoria("","",TipoBloque.Proceso,0,10,bloque.enEjeucion(),bloque.getSegmentoProceso(),false);            
                 memoria.add(proceso2);
            }
            SegmentoMemoria proceso1= new SegmentoMemoria(bloque.getInicioHexadecimal(),bloque.getFinHexadecimal(),bloque.getTipo(),0,bloque.getLongitud(),bloque.enEjeucion(),bloque.getSegmentoProceso(),false);            
            if(heap){
                proceso1= new SegmentoMemoria(bloque.getInicioHexadecimal(),bloque.getFinHexadecimal(),bloque.getTipo(),1,bloque.getLongitud(),bloque.enEjeucion(),bloque.getSegmentoProceso(),false);            
            }
            if(bloque.getTipo() ==TipoBloque.Heap){
                heap=true;
            }
            memoria.add(proceso1);
         }
        int tamanDirecciones=10;
        int tamanioLetra=13;
        int var=165;        
        if(longitud>150){
            tamanDirecciones=14;
            tamanioLetra=15;
            
        }
        
        jLabel2.setFont(new Font("Serif",Font.PLAIN,tamanDirecciones));
        jLabel1.setFont(new Font("Serif",Font.PLAIN,tamanDirecciones));
       
        if(longitud<20){
            tamanioLetra=10;
        }
        
        jLabel3.setFont(new Font("Serif", Font.PLAIN, tamanioLetra));
        jLabel3.setForeground(Color.white);
        jLabel3.setAlignmentX(Component.CENTER_ALIGNMENT);
        jLabel1.setAlignmentY(Component.TOP_ALIGNMENT);
        jLabel2.setAlignmentY(Component.BOTTOM_ALIGNMENT);
        if(tipo==TipoBloque.Proceso){
             jLabel3.addMouseListener(new MouseListener() {

			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseExited(MouseEvent arg0) {
                            try {
                                // TODO Auto-generated method stub
                                Robot robot = new Robot()    ;
                                robot.keyPress(KeyEvent.VK_ESCAPE);
                            } catch (AWTException ex) {
                                Logger.getLogger(SegmentoMemoria.class.getName()).log(Level.SEVERE, null, ex);
                            }
                            
			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				//JOptionPane.showMessageDialog(null,memoria, "Segmento Memoria de P"+id,
					//	JOptionPane.PLAIN_MESSAGE);
                                

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {                                
				JOptionPane.showMessageDialog(null,memoria, "Segmento Memoria de P"+id,
						JOptionPane.PLAIN_MESSAGE);
			}
        });
        }
        
        
        
        jLabel2.setForeground(Color.white);
        jLabel1.setForeground(Color.white);
        GroupLayout jPanel1Layout = new GroupLayout(this);
        setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel2, GroupLayout.Alignment.TRAILING)
                    .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addGap(var, var, var))))
            .addGroup(GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 100, GroupLayout.PREFERRED_SIZE)
                .addGap(35, 35, 35))
            .addGap(35,35,35)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, GroupLayout.PREFERRED_SIZE, longitud1, GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel3, GroupLayout.PREFERRED_SIZE, 1+longitud2   , GroupLayout.PREFERRED_SIZE)
                .addComponent(jLabel2, GroupLayout.PREFERRED_SIZE, longitud3, GroupLayout.PREFERRED_SIZE))
        );
    }
    
}
