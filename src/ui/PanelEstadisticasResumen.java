package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.table.AbstractTableModel;


public class PanelEstadisticasResumen extends JFrame implements ActionListener {

	private Double[][][] data;
	private Double[][] dataFF;
	private Double[][] dataBF;
	private Double[][] dataWF;

	public PanelEstadisticasResumen(String title, Double[][][] _estadisticas/*,Double[][] _estadisticaFF,Double[][] _estadisticaBF,Double[][] _estadisticaWF*/) {
		setTitle(title);
		int windowWidth = 1200; // Window width in pixels
		int windowHeight = 600; // Window height in pixels
		setBounds(50, 100, // Set position
						windowWidth, windowHeight); // and size
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		// Create and set up the content pane.
		setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));

		setResizable(false);

		JPanel panelButton = new JPanel(new GridBagLayout());
		JButton mostrarResultado = new JButton();
		mostrarResultado.setText("Mostrar Resultado");
		mostrarResultado.addActionListener(this);
		panelButton.add(mostrarResultado);

		data = _estadisticas;
		//dataFF = _estadisticaFF;
		//dataBF = _estadisticaBF;
		//dataWF = _estadisticaWF;
		/*PanelMemoriaEstadistica FirstFit = new PanelMemoriaEstadistica("First Fit", new TableEstadistica(dataFF));*/
		PanelMemoriaEstadistica FirstFit = new PanelMemoriaEstadistica("First Fit", new TableEstadistica(_estadisticas[0]));
		/*PanelMemoriaEstadistica BestFit = new PanelMemoriaEstadistica("Best Fit", new TableEstadistica(_estadisticasBF));*/
		PanelMemoriaEstadistica BestFit = new PanelMemoriaEstadistica("Best Fit", new TableEstadistica(_estadisticas[1]));
		/*PanelMemoriaEstadistica WorstFit = new PanelMemoriaEstadistica("Worst Fit", new TableEstadistica(_estadisticasWF));*/
		PanelMemoriaEstadistica WorstFit = new PanelMemoriaEstadistica("Worst Fit", new TableEstadistica(_estadisticas[2]));

		add(FirstFit);
		add(BestFit);
		add(WorstFit);
		add(panelButton);
//        TableEstadisticas newContentPane = new TableEstadisticas(_estadisticas);
//        newContentPane.setOpaque(true); // content panes must be opaque
//
//        setContentPane(newContentPane);
		// Display the window.
		// pack();
		setVisible(true);
	}

	final public String nombresCortos[] = {"FCFS", "SJF Exp", "SJF No Exp", "Prio Exp", "Prio No Exp", "Prio-Time Exp",
					"Prio-Time No Exp", "RR", "Multinivel"};
	final private String nombresCortosMemoria[] = {"First Fit", "Best Fit", "Worst Fit"};

	private void mostrarResumen(Double[][][] data) {
		Double bestRend = data[0][0][1], bestUso = data[0][0][2], bestProcs = data[0][0][8];
		Double bestWait = data[0][0][3], bestResp = data[0][0][4], bestRet = data[0][0][5], bestFrag = data[0][0][6], bestAcceso = data[0][0][7];
		String nomBestRend = nombresCortos[0] + nombresCortosMemoria[0], nomBestUso = nombresCortos[0]+ nombresCortosMemoria[0], nomBestWait = nombresCortos[0]+ nombresCortosMemoria[0],
						nomBestResp = nombresCortos[0]+ nombresCortosMemoria[0], nomBestRet = nombresCortos[0]+ nombresCortosMemoria[0], nomBestFrag = nombresCortos[0]+ nombresCortosMemoria[0],
						nomBestAcceso = nombresCortos[0], nomBestProcs = nombresCortos[0];
		for (int i = 0; i < nombresCortosMemoria.length; i++) {
			for (int j = 1; j < nombresCortos.length; j++) {
				if (data[i][j][1] != null && data[i][j][1] > bestRend) {
					bestRend = data[i][j][1];
					nomBestRend = nombresCortos[j] + " - " + nombresCortosMemoria[i];
				}
				if (data[i][j][2] != null && data[i][j][2] > bestUso) {
					bestUso = data[i][j][2];
					nomBestUso = nombresCortos[j] + " - " + nombresCortosMemoria[i];
				}
				if (data[i][j][3] != null && data[i][j][3] < bestWait) {
					bestWait = data[i][j][3];
					nomBestWait = nombresCortos[j] + " - " + nombresCortosMemoria[i];
				}
				if (data[i][j][4] != null && data[i][j][4] < bestResp) {
					bestResp = data[i][j][4];
					nomBestResp = nombresCortos[j] + " - " + nombresCortosMemoria[i];
				}
				if (data[i][j][5] != null && data[i][j][5] < bestRet) {
					bestRet = data[i][j][5];
					nomBestRet = nombresCortos[j] + " - " + nombresCortosMemoria[i];
				}
				if (data[i][j][6] != null && data[i][j][6] < bestFrag) {
					bestFrag = data[i][j][6];
					nomBestFrag = nombresCortos[j] + " - " + nombresCortosMemoria[i];
				}
				if (data[i][j][7] != null && data[i][j][7] < bestAcceso) {
					bestAcceso = data[i][j][7];
					nomBestAcceso = nombresCortos[j] + " - " + nombresCortosMemoria[i];
				}
				if (data[i][j][8] != null && data[i][j][8] > bestProcs) {
					bestProcs = data[i][j][8];
					nomBestProcs = nombresCortos[j] + " - " + nombresCortosMemoria[i];
				}

			}
		}

		String message = "La combinación con mejor rendimiento fue: " + nomBestRend + ", con un promedio de " + bestRend + "\n"
						+ "La combinación con mejor % de uso fue: " + nomBestUso + ", con un promedio de " + bestUso + "\n"
						+ "La combinacion con mejor T. de espera fue: " + nomBestWait + ", con un promedio de " + bestWait + "\n"
						+ "La combinación con mejor T. de respuesta fue: " + nomBestResp + ", con un promedio de " + bestResp + "\n"
						+ "La combinación con mejor T. de retorno fue: " + nomBestRet + ", con un promedio de " + bestRet + "\n"
						+ "La combinacion con mejor Fragmentación fue: " + nomBestFrag + ", con un promedio de " + bestFrag + "\n"
						+ "La combinación con mejor T. de acceso a mem. fue: " + nomBestAcceso + ", con un promedio de " + bestAcceso + "\n"
						+ "La combinación con mejor Cant. de procs. cargados a mem. fue: " + nomBestProcs + ", con un promedio de " + bestProcs;

		JOptionPane.showMessageDialog(null, message, "Resultados Estadisticos", 1);

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		//throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
		// TODO Auto-generated method stub
        /*mensajeResumen(nombresCortos, dataFF);
        mensajeResumen(nombresCortos, dataBF);
        mensajeResumen(nombresCortos, dataWF);*/

		mostrarResumen(data);
        /*mensajeResumen(nombresCortos, data);
        mensajeResumen(nombresCortos, data);
        mensajeResumen(nombresCortos, data);*/


	}
//    private void mensajeResumen(String[] nombresCortos ,Double[][] data){
//        Double bestRend = data[0][1], bestUso = data[0][2];
//        Double bestWait = data[0][3], bestResp = data[0][4], bestRet = data[0][5];
//        String nomBestRend = nombresCortos[0], nomBestUso = nombresCortos[0], nomBestWait = nombresCortos[0], nomBestResp = nombresCortos[0], nomBestRet = nombresCortos[0];
//        for (int i = 1; i < nombresCortos.length; i++) {
//            if (data[i][1] != null && data[i][1] > bestRend) {
//                bestRend = data[i][1];
//                nomBestRend = nombresCortos[i];
//            }
//            if (data[i][2] != null && data[i][2] > bestUso) {
//                bestUso = data[i][2];
//                nomBestUso = nombresCortos[i];
//            }
//            if (data[i][3] != null && data[i][3] < bestWait) {
//                bestWait = data[i][3];
//                nomBestWait = nombresCortos[i];
//            }
//            if (data[i][4] != null && data[i][4] < bestResp) {
//                bestResp = data[i][4];
//                nomBestResp = nombresCortos[i];
//            }
//            if (data[i][5] != null && data[i][5] < bestRet) {
//                bestRet = data[i][5];
//                nomBestRet = nombresCortos[i];
//            }
//        }
//        String message = "La politica con mejor rendimiento es: " + nomBestRend + " con un valor de: " + bestRend + "\n"
//                + "La politica con mejor Porcentaje de Uso es: " + nomBestUso
//                + " con un valor promedio de: " + bestUso + " %\n"
//                + "La politica con mejor Tiempo de Espera es: " + nomBestWait + " con un valor promedio de: "
//                + bestWait + "\n"
//                + "La politica con mejor Tiempo de Respuesta es: " + nomBestResp
//                + " con un valor promedio de: " + bestResp + "\n"
//                + "La politica con mejor Tiempo de Retorno es: " + nomBestRet
//                + " con un valor promedio de: " + bestRet + "\n";
//        JOptionPane.showMessageDialog(null, message, "Resultados Estadisticos", 1);
//    }

}


class PanelMemoriaEstadistica extends JPanel {

	private String politicaMemoria;
	private TableEstadistica table;

	public PanelMemoriaEstadistica(String Politica, TableEstadistica table) {
		this.politicaMemoria = Politica;
		this.table = table;
		setSize(new Dimension(800, 300));
		setLayout(new BorderLayout());
		setBorder(BorderFactory.createSoftBevelBorder(2));

		JPanel panelPolitica = new JPanel(new BorderLayout());

		JPanel paneltxtEstrategia = new JPanel();
		paneltxtEstrategia.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		paneltxtEstrategia.setLayout(new GridBagLayout());
		paneltxtEstrategia.setSize(new Dimension(100, 100));

		JLabel txtEstrategia = new JLabel();
		txtEstrategia.setText("   Estrategia   ");
		paneltxtEstrategia.add(txtEstrategia);

		JPanel paneltxtNombreEstrategia = new JPanel();
		paneltxtNombreEstrategia.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		paneltxtNombreEstrategia.setBackground(Color.WHITE);
		paneltxtNombreEstrategia.setLayout(new GridBagLayout());

		JLabel txtNombreEstrategia = new JLabel();
		txtNombreEstrategia.setText(this.politicaMemoria);
		paneltxtNombreEstrategia.add(txtNombreEstrategia);

		panelPolitica.add(paneltxtEstrategia, BorderLayout.NORTH);
		panelPolitica.add(paneltxtNombreEstrategia, BorderLayout.CENTER);

		add(panelPolitica, BorderLayout.WEST);
		add(this.table, BorderLayout.CENTER);
	}
}

class TableEstadistica extends JPanel {

	final public String nombresCortos[] = {"FCFS", "SJF Exp", "SJF No Exp", "Prio Exp", "Prio No Exp", "Prio-Time Exp",
					"Prio-Time No Exp", "RR", "Multinivel"};

	public TableEstadistica(Double[][] _estadisticas) {
		super(new GridLayout(1, 0));
		JTable table = new JTable(new TableEstadisticasModell(_estadisticas));
		table.setPreferredScrollableViewportSize(new Dimension(1000, 300));
		setLayout(new BorderLayout());
		// Create the scroll pane and add the table to it.
		JScrollPane scrollPane = new JScrollPane(table);

		//JPanel btnPanel = new JPanel();
		//btnPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        /*JButton btnMostrarResultados = new JButton("Mostrar Resultados");
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
        });*/
		// Add the scroll pane to this panel.
		add(scrollPane, BorderLayout.CENTER);
		//btnPanel.add(btnMostrarResultados);
		//add(btnPanel, BorderLayout.SOUTH);

	}

}

class TableEstadisticasModell extends AbstractTableModel {

	String[] columns = {"Politica", "Rendimiento CPU", "% Medio de uso CPU", "Tiempo de Espera Medio",
					"Tiempo de Respuesta Medio", "Tiempo de Retorno Medio", "Fragmentación", "Tiempo de acceso", "Procs. cargados"};

	final public String nombresCortos[] = {"FCFS", "SJF Exp", "SJF No Exp", "Prio Exp", "Prio No Exp", "Prio-Time Exp",
					"Prio-Time No Exp", "RR", "Multinivel"};

	Object[][] data;

	public TableEstadisticasModell(Double[][] _estadisticas) {
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
			return data[row][col];
		}

		return data[row][col];
	}
}
