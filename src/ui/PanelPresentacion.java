package ui;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.*;

@SuppressWarnings("serial")
public class PanelPresentacion extends JFrame {

	JLabel lblIntegrantes;
	JButton btnSiguiente;

	public PanelPresentacion() {
		
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		setTitle("Planificador de procesos");
		setBounds(screenSize.width/8, screenSize.height/8, screenSize.width*3/4, screenSize.height*6/7);
		setLocationRelativeTo(null);
		
                Container masterPanel = getContentPane();
		masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.Y_AXIS));
		
//		JScrollPane scrollPanel = new JScrollPane(new PanelImage());
//		scrollPanel.setBorder(null);
		
		JPanel panelTxt = new JPanel();
		panelTxt.setLayout(new FlowLayout(FlowLayout.CENTER));
		lblIntegrantes = new JLabel(txtIntegrantes);
		panelTxt.add(lblIntegrantes);
		
		btnSiguiente = new JButton("Continuar");
		JPanel panelButton = new JPanel();
		panelButton.setLayout(new FlowLayout(FlowLayout.RIGHT));
		panelButton.add(btnSiguiente);
		
		masterPanel.add(new PanelImage());
		masterPanel.add(panelTxt);
		masterPanel.add(panelButton);
		
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		btnSiguiente.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				setVisible(false);
				new PanelPlanificadorCPU();
			}
		});
		setVisible(true);

	}
	String txtIntegrantes = "<html><h1 style=\"text-align: center;\">Curso: Sistemas Operativos</h4><br>"
			+ "<font size=\"5\">Integrantes<br>"
			+ "<table style=\"width:100%\">" + 
			"  <tr>" + 
			"    <td><font size=\"5\">Guerra Anchante Juan Carlos</font></td>" + 
			"    <td><font size=\"5\">20152534B</font></td> " + 
			"  </tr>" + 
			"  <tr>" + 
			"    <td><font size=\"5\">Huaman Patricio Samir Harry</td>" + 
			"    <td><font size=\"5\">20164014I</font></td> " + 
			"  </tr>" + 
			"  <tr>" + 
			"    <td><font size=\"5\">Hurtado de Mendoza Gonzalez Zuniga Diego</td>" + 
			"    <td><font size=\"5\">20160112F</font></td> " + 
			"  </tr>" + 
			"  <tr>" + 
			"    <td><font size=\"5\">Lopez Lizana Rafael</td>" + 
			"    <td><font size=\"5\">20164141K</font></td> " + 
			"  </tr>" + 
			"  <tr>" + 
			"    <td><font size=\"5\">Sarrin Cepeda Paulo Cesar</td>" + 
			"    <td><font size=\"5\">20164082D</font></td> " + 
			"  </tr>" + 
			
			"</table>"
			+ " <h4 style=\"text-align: center;\"><font size=\"5\">Lima - 2019</font></h4></html>";
}


@SuppressWarnings("serial")
class PanelImage extends JPanel {
	JLabel logoUni;
	
	public PanelImage() {
		setLayout(new FlowLayout(FlowLayout.CENTER));
		logoUni = new JLabel();
		logoUni.setIcon(new ImageIcon(getClass().getResource("/imagenes/logoUNI.png")));
		logoUni.setSize(40, 40);
		add(logoUni);
	}

}
