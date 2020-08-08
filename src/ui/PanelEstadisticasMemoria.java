package ui;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class PanelEstadisticasMemoria extends JPanel {
	final static int width = 330 , height = 80;
	private JLabel fragLabel, tiempoLabel, procesosLabel, frag, tiempo, procesos;
	public PanelEstadisticasMemoria (String title, String _fragmentacion, String _tiempo, String _procesos){
		Border bGreyLine = BorderFactory.createLineBorder(Color.white,
						1);
		TitledBorder tBorder = BorderFactory.createTitledBorder(bGreyLine,
						title, TitledBorder.LEFT, TitledBorder.TOP, new Font(
										"Helvetica Neue", Font.BOLD, 13));

		setBorder(tBorder);
		setBackground(Color.WHITE);
		setLayout(new GridLayout(0, 2));

		fragLabel = new JLabel(_fragmentacion);
		fragLabel.setFont(new Font("Helvetica Neue", 13, 11));
		frag = new JLabel(" " + 0,SwingConstants.CENTER);
		frag.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
		tiempoLabel = new JLabel(_tiempo);
		tiempoLabel.setFont(new Font("Helvetica Neue", 12, 11));
		tiempo = new JLabel(" " + 0,SwingConstants.CENTER);
		tiempo.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
		procesosLabel = new JLabel(_procesos);
		procesosLabel.setFont(new Font("Helvetica Neue", 12, 11));
		procesos = new JLabel(" " + 0,SwingConstants.CENTER);
		procesos.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
		add(fragLabel);
		add(frag);
		add(tiempoLabel);

		add(tiempo);
		add(procesosLabel);
		add(procesos);



		setSize(width, height);
		setMinimumSize(new Dimension(width, height));
	}
	public void setEstadisticas(double _frag, double _tiempo, int _proc) {
		frag.setText(String.format("%.4f", _frag).replaceAll(",", ".")+ " %");
		tiempo.setText(String.format("%.2f", _tiempo).replaceAll(",", "."));
		procesos.setText(Integer.toString(_proc).replaceAll(",", "."));
	}

	public JLabel getFrag() {
		return frag;
	}

	public JLabel getTiempo() {
		return tiempo;
	}

	public JLabel getProcesos() {
		return procesos;
	}

	public Dimension getMinimumSize() {
		return new Dimension(width, height);
	}

	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

}
