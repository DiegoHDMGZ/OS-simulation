package ui;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

import javax.swing.JFrame;
import javax.swing.table.AbstractTableModel;


import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;
import java.util.Vector;
import javax.swing.JLabel;
import javax.swing.table.DefaultTableCellRenderer;
import model.io.Device;
import model.gestionProcesos.Politica;
import model.general.Proceso;


@SuppressWarnings("serial")
public class PanelDeColaDeProcesos extends JFrame{

       public static int nroVentanas = 0; 
       

	public PanelDeColaDeProcesos(String title,List<Proceso> j,List<Proceso> r1,TreeSet<Proceso> r2,Map<Device , List<Proceso> > d,Politica politica,List<Device> devices) {
            new JFrame(title);
            setTitle("Colas de Procesos");
            
            int windowWidth = 5000;    // Window width in pixels
	    int windowHeight = 600;   // Window height in pixels
	    setBounds(50, 100,       // Set position
	    windowWidth, windowHeight);  // and size
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    addWindowListener(new PanelColaDeProcesosEvents());
	    nroVentanas=1;
            //Create and set up the content pane.
            TableDemo newContentPane = new TableDemo(j,r1,r2,d,politica,devices);
            newContentPane.setOpaque(true); //content panes must be opaque
            setContentPane(newContentPane);

            //Display the window.
            pack();
            setVisible(true);
            
	    
	    
        }
        public PanelDeColaDeProcesos(){
            new JFrame("Colas de Procesos");
            setTitle("Colas de Procesos");
            
            int windowWidth = 5000;    // Window width in pixels
	    int windowHeight = 600;   // Window height in pixels
	    setBounds(50, 100,       // Set position
	    windowWidth, windowHeight);  // and size
	    setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	    addWindowListener(new PanelColaDeProcesosEvents());
	    nroVentanas=1;
        }
        public  void correrProceso(List<Proceso> j,List<Proceso> r1,TreeSet<Proceso> r2,Map<Device , List<Proceso> > d,Politica politica,List<Device> devices){
            TableDemo newContentPane = new TableDemo(j,r1,r2,d,politica,devices);
            newContentPane.setOpaque(true); //content panes must be opaque
            setContentPane(newContentPane);

            //Display the window.
            pack();
            setVisible(true);
            
        }
class TableDemo extends JPanel {
    private boolean DEBUG = false;
    public TableDemo(List<Proceso> j,List<Proceso> r1,TreeSet<Proceso> r2,Map<Device , List<Proceso> > d,Politica politica,List<Device> devices) {
        super(new GridLayout(1, 0));        
        JTable table = new JTable(new MyTableModel(j,r1,r2,d,politica,devices));
        table.setPreferredScrollableViewportSize(new Dimension(600, 300));
        
        //Para centrar las celdas
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment( JLabel.CENTER );
        table.setDefaultRenderer(String.class, centerRenderer);

        //Create the scroll pane and add the table to it.
        JScrollPane scrollPane = new JScrollPane(table);

        //Add the scroll pane to this panel.
        add(scrollPane);
  }

  class MyTableModel extends AbstractTableModel {
    private String[] columnNames ;
    private Object[][] data;
    
    @SuppressWarnings("empty-statement")
    public MyTableModel(List<Proceso> j,List<Proceso> r1,TreeSet<Proceso> r2,Map<Device , List<Proceso> > d,Politica politica,List<Device> devices){
        data = new Object[j.size()][3];
        int queEs;
        if(politica.getCod()==0 || politica.getCod()==1){
            queEs=1;
        }else{
            if(politica.getCod()==3 || politica.getCod()==4 || politica.getCod() == 5){
                queEs=2;
            }else{
                queEs=3;
            }
        }
        
        Vector impre=new Vector();
        Vector pantalla=new Vector();
        Vector hd=new Vector();
        int r=0;
        for(Device ayu:devices){
            if(r==0){
                for(Proceso a : d.get(ayu)) {                    
                    hd.addElement(a.getPid());
                }
            }else{
                if(r==1){
                   for(Proceso a : d.get(ayu)) {                    
                    impre.addElement(a.getPid());
                   } 
                }else{
                    for(Proceso a : d.get(ayu)) {                    
                        pantalla.addElement(a.getPid());
                    } 
                }
             
             }
            r++;
            
        }
        
        
        if(queEs==1 || queEs==2){            
            String[] columnNames2= {"Cola De Procesos", "Cola De Listos", "Cola de Disco Duro","Cola Impresora","Cola Pantalla"};           
            columnNames=columnNames2;
            data = new Object[j.size()][5];
                            
            if(queEs==1){
                for( int i=0;i<j.size();i++){
                    data[i][0]="P"+j.get(i).getPid();  
                    if(i>=r1.size()){
                        data[i][1]=" ";
                    }else{
                        data[i][1]="P"+r1.get(i).getPid();                
                    }
                    if(i>=hd.size()){
                        data[i][2]=" ";
                    }else{
                        data[i][2]="P"+hd.get(i);              
                    }
                    if(i>=impre.size()){
                        data[i][3]=" ";
                    }else{
                        data[i][3]="P"+impre.get(i);
                    }
                    if(i>=pantalla.size()){
                        data[i][4]=" ";
                    }else{
                        data[i][4]="P"+pantalla.get(i);               
                    }
                }
                
            }else{
                
                Vector v = new Vector();
               
                for(Proceso a : r2) {                    
                    v.addElement(a.getPid());                 
                }
                for( int i=0;i<j.size();i++){
                    data[i][0]="P"+j.get(i).getPid();  
                    if(i>=v.size()){
                        data[i][1]=" ";
                    }else{
                        data[i][1]="P"+v.get(i);                
                    }
                    if(i>=hd.size()){
                        data[i][2]=" ";
                    }else{
                        data[i][2]="P"+hd.get(i);              
                    }
                    if(i>=impre.size()){
                        data[i][3]=" ";
                    }else{
                        data[i][3]="P"+impre.get(i);
                    }
                    if(i>=pantalla.size()){
                        data[i][4]=" ";
                    }else{
                        data[i][4]="P"+pantalla.get(i);               
                    }
                    
                }
            }
       
        }else{
            String[] columnNames2= {"Cola De Procesos", "Cola de Listos: Round Robin","Cola de Listos: Prioridad", "Cola de Disco Duro","Cola Impresora","Cola Pantalla"};
            columnNames=columnNames2;
            
            data = new Object[j.size()][6];
            Vector v = new Vector();
               
                for(Proceso a : r2) {                    
                    v.addElement(a.getPid());                 
            }
            for(int i=0;i<j.size();i++){
                data[i][0]="P"+j.get(i).getPid(); 
                if(i>=r1.size()){
                   data[i][1]=" ";
                }else{
                   data[i][1]="P"+r1.get(i).getPid();                
                }
                if(i>=v.size()){
                   data[i][2]=" ";
                }else{
                   data[i][2]="P"+v.get(i);
                }
                if(i>=hd.size()){
                        data[i][3]=" ";
                    }else{
                        data[i][3]="P"+hd.get(i);              
                    }
                    if(i>=impre.size()){
                        data[i][4]=" ";
                    }else{
                        data[i][4]="P"+impre.get(i);
                    }
                    if(i>=pantalla.size()){
                        data[i][5]=" ";
                    }else{
                        data[i][5]="P"+pantalla.get(i);               
                    }
            }
            
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

    /**
     * JTable uses this method to determine the default renderer/ editor for
     * each cell. If we didn't implement this method, then the last column
     * would contain text ("true"/"false"), rather than a check box.
     */
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

  /**
   * Create the GUI and show it. For thread safety, this method should be
   * invoked from the event-dispatching thread.
   */
 
}
    

class PanelColaDeProcesosEvents implements WindowListener{
        
	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub
		PanelPlanificadorCPU.panelColas=0;
		
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