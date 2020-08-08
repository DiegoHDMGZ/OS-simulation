package ui.panelSegmentoProcesos;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class FramSegmentoMemoria extends JPanel{
    private Container masterPanel;
    
    public FramSegmentoMemoria(){        
        new JPanel();
        setBackground(Color.MAGENTA);
        setLayout(new GridLayout(3,3));
        
        JLabel label = new JLabel("P");
        label.setHorizontalAlignment(SwingConstants.CENTER);
        JLabel hueco1 = new JLabel("");
        JLabel hueco2 = new JLabel("");
        JLabel hueco3 = new JLabel("");
        JLabel hueco4 = new JLabel("");
        JLabel hueco5 = new JLabel("");
        JLabel hueco6 = new JLabel("");
        JLabel label1 = new JLabel("xx");
        label1.setHorizontalAlignment(SwingConstants.LEFT);
        label1.setVerticalAlignment(SwingConstants.TOP);
        JLabel label3 = new JLabel("ff");
        label3.setVerticalAlignment(SwingConstants.BOTTOM);
        label3.setHorizontalAlignment(SwingConstants.RIGHT);
        add(label1);
        add(hueco1);
        add(hueco2);
        add(hueco3);
        add(label);
        add(hueco4);
        add(hueco5);
        add(hueco6);
        add(label3);
        //add(pane);

//        boxLayout.add(pane);
//        add(boxLayout);



    }
}
