package ui;

import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.text.*;

/**
 *         Un simple panel para mostrar min/media/max para cualquier
 *         cuantificador.
 */
@SuppressWarnings("serial")
public class PanelEstadisticas extends JPanel {

	final static int width = 180 , height = 80;

	private JLabel minLabel, mediaLabel, maxLabel, min, media, max;

	public PanelEstadisticas(String title) {
		
		Border bGreyLine = BorderFactory.createLineBorder(Color.white,
				1);
		TitledBorder tBorder = BorderFactory.createTitledBorder(bGreyLine,
				title, TitledBorder.LEFT, TitledBorder.TOP, new Font(
						"Helvetica Neue", Font.BOLD, 13));

		setBorder(tBorder);
		setBackground(Color.WHITE);
		setLayout(new GridLayout(0, 2));

		minLabel = new JLabel("Min: ");
		minLabel.setFont(new Font("Helvetica Neue", 13, 11));
		min = new JLabel(" " + 0);
		min.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
		mediaLabel = new JLabel("Med: ");
		mediaLabel.setFont(new Font("Helvetica Neue", 12, 11));
		media = new JLabel(" " + 0);
		media.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
		maxLabel = new JLabel("Max: ");
		maxLabel.setFont(new Font("Helvetica Neue", 12, 11));
		max = new JLabel(" " + 0);
		max.setFont(new Font("Helvetica Neue", Font.BOLD, 12));
//
		add(minLabel);
		add(min);
		add(mediaLabel);
		add(media);
		add(maxLabel);
		add(max);

		setSize(width, height);
		setMinimumSize(new Dimension(width, height));
	}



	/**
	 * Actualizar las estadisticas
	 */
	public void setEstadisticas(int _min, double _media, int _max) {
		NumberFormat nf = NumberFormat.getInstance();
		nf.setMaximumFractionDigits(2);
		nf.setMinimumFractionDigits(2);
		nf.setGroupingUsed(false);

		min.setText(Integer.toString(_min).replaceAll(",", "."));
		max.setText(Integer.toString(_max).replaceAll(",", "."));

		String s = nf.format(_media);
		media.setText(s.replaceAll(",", "."));
	}


	public JLabel getMedia() {
		return media;
	}

	public Dimension getMinimumSize() {
		return new Dimension(width, height);
	}

	public Dimension getPreferredSize() {
		return new Dimension(width, height);
	}

}

