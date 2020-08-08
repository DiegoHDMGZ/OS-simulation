package ui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;


@SuppressWarnings("serial")
public class PanelReloj extends JPanel {

	final static int width = 290, height = 80;

	JLabel rendLabel, usoMinLabel, usoMedLabel, usoMaxLabel, rend, usoMin, usoMed, usoMax;

	public PanelReloj(String title) {
		Border bGreyLine = BorderFactory.createLineBorder(Color.WHITE,
				1);
		TitledBorder tBorder = BorderFactory.createTitledBorder(bGreyLine,
				title, TitledBorder.LEFT, TitledBorder.TOP, new Font(
						"Helvetica Neue", Font.BOLD, 14));
		setBorder(tBorder);
		setBackground(Color.WHITE);
		setLayout(new GridLayout(0, 2));

		usoMinLabel = new JLabel("Porc. uso minimo");
		usoMinLabel.setFont(new Font("Helvetica Neue", 12, 11));
		usoMin = new JLabel("" + 0);
		usoMin.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		usoMedLabel = new JLabel("Porc. uso medio");
		usoMedLabel.setFont(new Font("Helvetica Neue", 12, 11));
		usoMed = new JLabel("" + 0);
		usoMed.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		usoMaxLabel = new JLabel("Porc. uso maximo");
		usoMaxLabel.setFont(new Font("Helvetica Neue", 12, 11));
		usoMax = new JLabel("" + 0);
		usoMax.setFont(new Font("Helvetica Neue", Font.BOLD, 13));
		rendLabel = new JLabel("Rendimiento");
		rendLabel.setFont(new Font("Helvetica Neue", 12, 11));
		rend = new JLabel("" + 0);
		rend.setFont(new Font("Helvetica Neue", Font.BOLD, 13));

		add(usoMinLabel);
		add(usoMin);
		add(usoMedLabel);
		add(usoMed);
		add(usoMaxLabel);
		add(usoMax);
		add(rendLabel);
		add(rend);
		
		setSize(width, height);
		setMinimumSize(new Dimension(width, height));
	}

	/**
	 * Actualizar numeros mostrados
	 */
	public void setEstadisticas(double _rend, double _usoMin, double _usoMed, double _usoMax) {
		rend.setText(String.format("%.5f", _rend).replaceAll(",", ".")+" procesos/ut");
		usoMin.setText(String.format("%.2f", _usoMin).replaceAll(",", ".")+ " %");
		usoMed.setText(String.format("%.2f", _usoMed).replaceAll(",", ".")+ " %");
		usoMax.setText(String.format("%.2f", _usoMax).replaceAll(",", ".")+ " %");
	}
	
	

	public JLabel getRend() {
		return rend;
	}

	public JLabel getUsoMin() {
		return usoMin;
	}

	public JLabel getUsoMed() {
		return usoMed;
	}

	public JLabel getUsoMax() {
		return usoMax;
	}

	public Dimension getMinimumSize() {
		return new Dimension(width, height);
	}

	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

}
