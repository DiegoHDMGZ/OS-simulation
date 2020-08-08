package ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import model.gestionProcesos.Estado;
import model.general.Proceso;
import model.gestionProcesos.TipoProceso;


@SuppressWarnings("serial")
public class PanelProceso extends JPanel {

	Proceso proceso;

	/**
	 * Ancho del panel de proceso.
	 */
	static final int PPANCHO = 25;

	/**
	 * Alto del panel de proceso.
	 */
	static final int PPALTO = 230;

	/**
	 * La altura a la que desea los medidores dibujados. Hago una proporci�n de 1:1
	 * con mi r�faga m�xima.
	 */
	static final int BARALTURA = 200;

	/**
	 * Algunos colores para dibujar.
	 */
	Color burstColor, initBurstColor = Color.darkGray, unarrivedColor, lblColor, finishColor, anormalColor;

	/**
	 * EL label para mostrar el PID.
	 */
	JLabel pidLbl, userLabel;

	/**
	 * Quieres ver los procesos unarrived?
	 */
	static boolean showHidden = true;

	/** Constructor por defecto. Genera su propio proceso. */
//	PanelProceso() {
//		proceso = new Proceso();
//		initPanel();
//	}

	/**
	 * Constructor parametrico.
	 * 
	 * @param p p sobre el que se basa este panel.
	 */
	 public PanelProceso(Proceso p) {
		proceso = p;
		initPanel();
	}

	/**
	 * Construye el panel
	 */
	void initPanel() {
		setAlignmentX(Component.CENTER_ALIGNMENT);
		setLayout(new BorderLayout());
		String tipoProc = (proceso.getPcb().getTipo()==TipoProceso.Usuario ? "U" : "SO");
		userLabel = new JLabel( "" + tipoProc );
		userLabel.setFont(new Font("Helvetica", Font.BOLD, 12));
		userLabel.setHorizontalAlignment(SwingConstants.CENTER);
		pidLbl = new JLabel("" + (int) proceso.getPid());
		pidLbl.setFont(new Font("Helvetica", Font.BOLD, 12));
		pidLbl.setHorizontalAlignment(SwingConstants.CENTER);

		pidLbl.addMouseListener(new MouseListener() {

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
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub

			}

			@Override
			public void mouseClicked(MouseEvent arg0) {
				JOptionPane.showMessageDialog(null, proceso.getPcb().toString(), "PCB",
						JOptionPane.INFORMATION_MESSAGE);
			}
		});

		setSize(PPANCHO, PPALTO);
		setBackground(Color.white);
		setOpaque(true);
		add(userLabel, BorderLayout.NORTH);
		add(pidLbl, "South");
	}

	/**
	 * Si el proceso termino removerlo. De lo contrario actualizar su medidor de
	 * burst.
	 */
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
//		if (proceso.isFinalizado() == true) {
//			setVisible(false);
//		} else {
//			DrawBursts(g);
//		}
		DrawBursts(g);
	}

	/**
	 * Dibujar el panel burst. Dibujar el burst restante sobre el burst acabado.
	 * Dibujar el proceso activo en un color brillante.
	 */
	void DrawBursts(Graphics g) {
		int initBurstHeight = 0, burstHeight = 0;
		int width = 0;
		initBurstHeight = (int) proceso.getBurstTimeInicial();
		burstHeight = (int) proceso.getPcb().getBurstTime();
		width = (int) PPANCHO - 2; // fuera por un error in swing?

		lblColor = (proceso.getPcb().getEstado() == Estado.Listo ? Color.black : Color.lightGray);

		initBurstColor = (proceso.getPcb().getEstado() == Estado.Listo ? Color.lightGray : Color.lightGray);

//		burstColor = (proceso.getPcb().getEstado()==Estado.Listo) ? (proceso.getPcb().getEstado()==Estado.Ejecutando ? Color.green
//				: Color.yellow)
//				: (showHidden ? Color.darkGray : Color.white);

		switch (proceso.getPcb().getEstado()) {
		case Listo:
			burstColor = Color.ORANGE;
			break;
		case Ejecutando:
			burstColor = Color.green;
			break;
		case Bloqueado:
			burstColor = Color.red;
		default:
			burstColor = Color.white;
		}

		finishColor = Color.black;
		anormalColor = Color.magenta;
//		pidLbl.setForeground(lblColor);
//		pidLbl.setBackground(proceso.getPcb().getEstado() == Estado.Ejecutando ? Color.red : Color.white);
		if (proceso.getPcb().getEstado() == Estado.Terminado && proceso.getPcb().getBurstTime()>0) {
			g.setColor(anormalColor);
			g.drawRect(0, BARALTURA - initBurstHeight, width, initBurstHeight);
			g.setColor(anormalColor);
			g.fillRect(1, BARALTURA - initBurstHeight, width - 1, initBurstHeight);
		} else if (proceso.getPcb().getEstado() == Estado.Terminado && proceso.getPcb().getBurstTime()==0) {
			g.setColor(finishColor);
			g.drawRect(0, BARALTURA - initBurstHeight, width, initBurstHeight);
			g.setColor(finishColor);
			g.fillRect(1, BARALTURA - initBurstHeight, width - 1, initBurstHeight);
		} else if (proceso.getPcb().getEstado() == Estado.Listo) {
			g.setColor(burstColor);
			g.drawRect(0, BARALTURA - initBurstHeight, width, initBurstHeight);
			g.setColor(burstColor);
			g.fillRect(1, BARALTURA - burstHeight + 1, width - 1, burstHeight - 1);
		} else if (proceso.getPcb().getEstado() == Estado.Ejecutando) {
			g.setColor(burstColor);
			g.drawRect(0, BARALTURA - initBurstHeight, width, initBurstHeight);
			g.setColor(burstColor);
			g.fillRect(1, BARALTURA - burstHeight + 1, width - 1, burstHeight - 1);
		}
		else if (proceso.getPcb().getEstado() == Estado.Bloqueado) {
			g.setColor(Color.red);
			g.drawRect(0, BARALTURA - initBurstHeight, width, initBurstHeight);
			g.setColor(Color.red);
			g.fillRect(1, BARALTURA - burstHeight + 1, width - 1, burstHeight - 1);
		} else if (showHidden) {
			g.setColor(initBurstColor);
			g.drawRect(0, BARALTURA - initBurstHeight, width, initBurstHeight);
		}

	}

	/**
	 * Obtener el valor de proceso.
	 * 
	 * @return valor de proceso.
	 */
	public Proceso getProceso() {
		return proceso;
	}

	/**
	 * Ajustar el valor de proceso.
	 * 
	 * @param v Valor a asignar a proceso.
	 */
	public void setProceso(Proceso v) {
		this.proceso = v;
	}

	public Dimension getPreferredSize() {
		return (new Dimension(PPANCHO, PPALTO));
	}

	/**
	 * Obtener el valor de showHidden.
	 * 
	 * @return valor de showHidden.
	 */
	public static boolean getShowHidden() {
		return showHidden;
	}

	/**
	 * Ajustar el valor de showHidden.
	 * 
	 * @param v Valor a asignar a showHidden.
	 */
	public static void setShowHidden(boolean v) {
		showHidden = v;
	}

}
