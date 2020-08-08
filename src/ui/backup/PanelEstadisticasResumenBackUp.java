package ui.backup;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

@SuppressWarnings("serial")
public class PanelEstadisticasResumenBackUp extends JFrame {
	
    public PanelEstadisticasResumenBackUp(String title, Double[][] _estadisticas) {
        new JFrame(title);
        setTitle(title);
        int windowWidth = 800; // Window width in pixels
        int windowHeight = 600; // Window height in pixels
        setBounds(50, 100, // Set position
                windowWidth, windowHeight); // and size
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        // Create and set up the content pane.
        TableEstadisticas newContentPane = new TableEstadisticas(_estadisticas);
        newContentPane.setOpaque(true); // content panes must be opaque

        setContentPane(newContentPane);
        // Display the window.
        pack();
        setVisible(true);
    }

}

@SuppressWarnings("serial")
class TableEstadisticas extends JPanel {
	
	final public String nombresCortos[] = {"FCFS", "SJF Exp", "SJF No Exp", "Prio Exp", "Prio No Exp", "Prio-Time Exp",
            "Prio-Time No Exp", "RR", "Multinivel"};
	
    public TableEstadisticas(Double[][] _estadisticas) {
        
        super(new GridLayout(1, 0));
        JTable table = new JTable(new TableEstadisticasModel(_estadisticas));
        table.setPreferredScrollableViewportSize(new Dimension(1000, 300));
        setLayout(new BorderLayout());
        // Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        JPanel btnPanel = new JPanel();
        btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        JButton btnMostrarResultados = new JButton("Mostrar Resultados");
        btnMostrarResultados.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Auto-generated method stub

               
                
                Double bestRend =_estadisticas[0][1], bestUso = _estadisticas[0][2];
                Double bestWait = _estadisticas[0][3], bestResp = _estadisticas[0][4], bestRet = _estadisticas[0][5];
                String nomBestRend = nombresCortos[0], nomBestUso =nombresCortos[0], nomBestWait =nombresCortos[0], nomBestResp =nombresCortos[0], nomBestRet =nombresCortos[0];
                for(int i=1; i<nombresCortos.length; i++) {
                	if(_estadisticas[i][1]!=null && _estadisticas[i][1] > bestRend) {
                		bestRend = _estadisticas[i][1];
                		nomBestRend = nombresCortos[i];
                	}
                	if(_estadisticas[i][2]!=null && _estadisticas[i][2] > bestUso ) {
                		bestUso = _estadisticas[i][2];
                		nomBestUso = nombresCortos[i];
                	}
                	if(_estadisticas[i][3]!=null && _estadisticas[i][3] < bestWait ) {
                		bestWait = _estadisticas[i][3];
                		nomBestWait = nombresCortos[i];
                	}
                	if(_estadisticas[i][4]!=null && _estadisticas[i][4] < bestResp ) {
                		bestResp = _estadisticas[i][4];
                		nomBestResp = nombresCortos[i];
                	}
                	if(_estadisticas[i][5]!=null && _estadisticas[i][5] < bestRet) {
                		bestRet = _estadisticas[i][5];
                		nomBestRet = nombresCortos[i];
                	}
                }
                String message = "La politica con mejor rendimiento es: " + nomBestRend + " con un valor de: " + bestRend + "\n"
                        + "La politica con mejor Porcentaje de Uso es: " + nomBestUso
                        + " con un valor promedio de: " + bestUso + " %\n"
                        + "La politica con mejor Tiempo de Espera es: " + nomBestWait+ " con un valor promedio de: "
                        + bestWait + "\n"
                        + "La politica con mejor Tiempo de Respuesta es: " + nomBestResp
                        + " con un valor promedio de: " + bestResp + "\n"
                        + "La politica con mejor Tiempo de Retorno es: " + nomBestRet
                        + " con un valor promedio de: " + bestRet + "\n";
                JOptionPane.showMessageDialog(null, message, "Resultados Estadisticos", 1);

            }
        });
        // Add the scroll pane to this panel.
        add(scrollPane, BorderLayout.CENTER);
        btnPanel.add(btnMostrarResultados);
        add(btnPanel, BorderLayout.SOUTH);

    }

}

@SuppressWarnings("serial")
class TableEstadisticasModel extends AbstractTableModel {

    String[] columns = {"Algoritmo", "Rendimiento CPU", "% Medio de uso CPU", "Tiempo de Espera Medio",
            "Tiempo de Respuesta Medio", "Tiempo de Retorno Medio"};

    final public String nombresCortos[] = {"FCFS", "SJF Exp", "SJF No Exp", "Prio Exp", "Prio No Exp", "Prio-Time Exp",
            "Prio-Time No Exp", "RR", "Multinivel"};
        
    Object[][] data;

    public TableEstadisticasModel(Double[][] _estadisticas) {
        // TODO Auto-generated constructor stub
        data = _estadisticas;

    }

    @Override
    public String getColumnName(int col) {
        return columns[col];
    }

    @Override
    public int getColumnCount() {
        // TODO Auto-generated method stub
        return columns.length;
    }

    @Override
    public int getRowCount() {
        // TODO Auto-generated method stub
        return data.length;
    }

    @Override
    public Object getValueAt(int row, int col) {
        if (col == 0) {
            return nombresCortos[row];
        } else if (col != 0) {
//			
            return data[row][col];
        }

        return data[row][col];
    }

}

