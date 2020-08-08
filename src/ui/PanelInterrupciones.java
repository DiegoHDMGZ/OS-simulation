package ui;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import model.io.Device;
import model.io.Interrupcion;
import model.gestionProcesos.Politica;
import model.general.Proceso;
import model.io.TipoInterrupcion;

@SuppressWarnings("serial")
public class PanelInterrupciones extends JFrame{

	public static int nroVentanas = 0;
	public PanelInterrupciones(List <Interrupcion> interrup) {
            new JFrame("Interrupciones");
            setTitle("Interrupciones");
            
            int windowWidth = 2000;    // Window width in pixels
	    int windowHeight = 2000;   // Window height in pixels
	    setBounds(50, 100,       // Set position
	    windowWidth, windowHeight);  // and size
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    addWindowListener(new PanelInterrupcionesEvents());
	    nroVentanas=1;
            //Create and set up the content pane.
            TableDemo newContentPane = new TableDemo(interrup);
            newContentPane.setOpaque(true); //content panes must be opaque
            setContentPane(newContentPane);

            //Display the window.
            pack();
            setVisible(true);	    
	    
	}
        public PanelInterrupciones(){
            new JFrame("Interrupciones");
            setTitle("Interrupciones");            
            int windowWidth = 2000;    // Window width in pixels
	    int windowHeight = 2000;   // Window height in pixels
	    setBounds(50, 100,       // Set position
	    windowWidth, windowHeight);  // and size
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    addWindowListener(new PanelInterrupcionesEvents());
	    nroVentanas=1;
        }
         public  void correrInterrupcion(List <Interrupcion> interrup){
            TableDemo newContentPane = new TableDemo(interrup);
            newContentPane.setOpaque(true); //content panes must be opaque
            setContentPane(newContentPane);

            //Display the window.
            pack();
            setVisible(true);
            
        }
        
        
        class TableDemo extends JPanel {
        private boolean DEBUG = false;
        public TableDemo(List <Interrupcion> interrup) {
        super(new GridLayout(1, 0));        
        JTable table = new JTable(new MyTableModel(interrup));
        table.setPreferredScrollableViewportSize(new Dimension(700, 300));
        
        //Para centrar las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.setDefaultRenderer(String.class, centerRenderer);
        table.getColumnModel().getColumn(2).setPreferredWidth(160);
        table.getColumnModel().getColumn(0).setPreferredWidth(20);
        table.getColumnModel().getColumn(3).setPreferredWidth(100);
        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);
    }

        class MyTableModel extends AbstractTableModel {
            private int tiempoArribo;
            private int duracion;
            private TipoInterrupcion tipo;
            private Proceso devolver; //solo para Interrupciones IO
            Device device;
            private String[] columnNames ={
                "Nro","Vector number" ,"Descripcion", "Device" , "Proceso","Tiempo De Arribo","Estado"
            };
            private Object[][] data;
    
         @SuppressWarnings("empty-statement")
        public MyTableModel(List <Interrupcion> interrup){            
            data = new Object[interrup.size()][7];
            for(int i=0;i<interrup.size();i++){
                data[i][0]=" "+(i+1);
                data[i][1] = Integer.toString(interrup.get(i).getCodigo());
                data[i][2] = interrup.get(i).getTipo().toString();
                data[i][3] = "";
                if(interrup.get(i).getDevice() != null) {
                    data[i][3] = interrup.get(i).getDevice().toString();
                } 
                data[i][4] = "";
                if(interrup.get(i).getProceso() != null) {
                    data[i][4] = interrup.get(i).getProceso().toString();
                }
                /*if(interrup.get(i).getDevice() != null) {
                    data[i][4] = interrup.get(i).getDevice().toString();
                }
                if(interrup.get(i).getTipo()== TipoInterrupcion.IO){
                    data[i][2] = "IO";
                    data[i][3] = "P"+interrup.get(i).getDevolver().getPid(); 
                    data[i][4] = interrup.get(i).getDevice().toString();
                }else{
                    data[i][2] = "Timer";
                    data[i][3] = "P" + interrup.get(i).getDevolver().getPid();  
                    data[i][4]=" ";
                }*/
                
                data[i][5] = Integer.toString(interrup.get(i).getTiempoArribo());
                
                data[i][6] = interrup.get(i).getEstado().toString();
            }
        
        }
        public int getColumnCount() {
            return columnNames.length;
        }

        public int getRowCount() {
            return data.length;
        }

        public String getColumnName(int col) {
            return columnNames[col];
        }

        public Object getValueAt(int row, int col) {
          return data[row][col];
        }

        public Class getColumnClass(int c) {
          return getValueAt(0, c).getClass();

        }

    /**
     * Don't need to implement this method unless your table's editable.
     */
        public boolean isCellEditable(int row, int col) {
          //Note that the data/cell address is constant,
          //no matter where the cell appears onscreen.
          if (col < 10) {
            return false;
          } else {
            return true;
          }
        }

    /**
     * Don't need to implement this method unless your table's data can
     * change.
     */
        public void setValueAt(Object value, int row, int col) {
          if (DEBUG) {
            System.out.println("Setting value at " + row + "," + col
                + " to " + value + " (an instance of "
                + value.getClass() + ")");
          }

          data[row][col] = value;
          fireTableCellUpdated(row, col);

          if (DEBUG) {
            System.out.println("New value of data:");
            printDebugData();
          }
        }

        private void printDebugData() {
          int numRows = getRowCount();
          int numCols = getColumnCount();

          for (int i = 0; i < numRows; i++) {
            System.out.print("    row " + i + ":");
            for (int j = 0; j < numCols; j++) {
              System.out.print("  " + data[i][j]);
            }
            System.out.println();
          }
          System.out.println("--------------------------");
        }
    }

  
 
    }
	
	

@SuppressWarnings("serial")
class ModeloTablaInterrupciones extends AbstractTableModel{

	String[] Columns = { "Nro Interrupcion", "Proceso", "Tiempo de Arribo", "Tiempo Duracion", "Tipo" };
	
	
	
	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return Columns.length;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return 4;
	}

	@Override
	public Object getValueAt(int arg0, int arg1) {
		// TODO Auto-generated method stub
		return null;
	}
	
	
	
}





class PanelInterrupcionesEvents implements WindowListener{

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		PanelPlanificadorCPU.panelInterrup=0;
	}

	@Override
	public void windowClosing(WindowEvent e) {
		// TODO Auto-generated method stub
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}
	
    }
}