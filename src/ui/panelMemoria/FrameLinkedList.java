package ui.panelMemoria;

import ui.panelMemoria.PanelLinkedList;
import ui.panelMemoria.LinkedListMemory;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import model.gestionMemoria.Bloque;
import java.util.LinkedList;
public class FrameLinkedList extends JFrame {
    
    Timer timer = new Timer(10, new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            drawBurst();
        }
    });
    JLabel titulo = new JLabel("Lista Encadenada");
    JButton button = new JButton("Add");
    JButton button2 = new JButton("Delete");
    JButton button3 = new JButton("Ramdon");
    JButton button4 = new JButton("Add List");
    private int x = 30;
    private int y = 0;
    JPanel topRow = new JPanel();
    JPanel middleRow = new JPanel();
    private static int step = 40;
    private static int height = 20;

    private static int PALTO;
    private static int PANCHO;

    private LinkedList<Bloque> lista;
    
    private static int numVentana = 0;
    private static FrameLinkedList frame;
    
    public static FrameLinkedList GenerarPanelMemoria(){
        if(numVentana == 0){
            frame = new FrameLinkedList();
            numVentana++;
            return frame;  
        }else{
            return frame;
        }  
    }
    
    
    private FrameLinkedList() {

        timer.start();
        
        Toolkit tk = Toolkit.getDefaultToolkit();

        PALTO = tk.getScreenSize().height;
        PANCHO = tk.getScreenSize().width;
        titulo.setHorizontalTextPosition(SwingConstants.CENTER);
        
        setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        setSize(730, PALTO * 2 / 3);
        setMaximumSize(new Dimension(730, PALTO * 2 / 3));
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());
        setResizable(true);
        
        topRow.setLayout(new GridLayout(1, 4));
        topRow.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        /*button4.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                drawBurst();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });
        
        
        button3.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {    
                Random random = new Random();
                int max = random.nextInt(40);
                limpiarPaneles();
                for (int i = 0; i < max; i++) {
                    anadirPanelBloque();
                }
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        button2.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
                limpiarPaneles();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });

        button.addMouseListener(new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
                anadirPanelBloque();
            }

            @Override
            public void mousePressed(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseReleased(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            public void mouseExited(MouseEvent e) {
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            }
        });*/
        topRow.add(titulo);
//        topRow.add(button);
//        topRow.add(button2);
//        topRow.add(button3);
//        topRow.add(button4);
        topRow.setVisible(true);
        add(topRow, BorderLayout.NORTH);

        //PanelLinkedList p = new PanelLinkedList(new LinkedListMemory(x, y));
        //x += PanelLinkedList.getW() + step;

        //p.setVisible(true);
        middleRow.setPreferredSize(new Dimension(730, 500));
        middleRow.setMaximumSize(new Dimension(730,500));
        JScrollPane scroll = new JScrollPane(middleRow);
        scroll.setPreferredSize(new Dimension(1000,500));
        middleRow.setLayout(new BorderLayout());
        //middleRow.add(p, BorderLayout.CENTER);
        middleRow.setVisible(true);
        add(scroll, BorderLayout.CENTER);
        middleRow.revalidate();
        middleRow.repaint();

        //setVisible(true);
        revalidate();
        repaint();
    }

    public static int getStep() {
        return step;
    }

    void anadirPanelBloque() {
        LinkedListMemory lista = new LinkedListMemory(x, y);
        PanelLinkedList nuevoPanel = new PanelLinkedList(lista);
        if (lista.getId() % 5 != 0) {
            x += PanelLinkedList.getW() + step;
        } else {
            y += PanelLinkedList.getH() + height;
            x = 30;
        }

        nuevoPanel.setVisible(true);
        middleRow.add(nuevoPanel, BorderLayout.CENTER);
        middleRow.revalidate();
        middleRow.repaint();
        revalidate();
        repaint();
    }
    
    void anadirPanelBloque(Bloque bloque) {
        LinkedListMemory lista = new LinkedListMemory(x, y, bloque);
        PanelLinkedList nuevoPanel = new PanelLinkedList(lista);
        if (lista.getId() % 3 != 0) {
            x += PanelLinkedList.getW() + step;
        } else {
            y += PanelLinkedList.getH() + height;
            x = 30;
        }
        nuevoPanel.setVisible(true);
        middleRow.add(nuevoPanel, BorderLayout.CENTER);
        middleRow.revalidate();
        middleRow.repaint();
        revalidate();
        repaint();
    }

    void limpiarPaneles() {
        middleRow.removeAll();
        middleRow.revalidate();
        middleRow.repaint();
        PanelLinkedList.reset();
        x = 30;
        y = 0;
    }
    public void setLista(LinkedList lista){
        this.lista = lista;
    }
    
    public void drawBurst(){
        if(lista == null){
            System.out.println("Lista sin asignar");
        }else if(lista.isEmpty()){
            System.out.println("Lista Vacia");
        }else{
            limpiarPaneles();
            for(Bloque bloque : lista){
                anadirPanelBloque(bloque);
            }
        }     
    }
}
