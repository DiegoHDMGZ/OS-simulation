import javax.swing.*;
import java.awt.event.*;
 
@SuppressWarnings("deprecation")
public class panelControl extends JFrame {
 String radio,check;
 JPanel panel = new JPanel();
 ButtonGroup grupoCheck = new ButtonGroup();
 ButtonGroup grupoRadio = new ButtonGroup();
 JRadioButton radio1 = new JRadioButton("Primera Opcion del Radio");
 JRadioButton radio2 = new JRadioButton("Segunda Opcion del Radio");
 JRadioButton radio3 = new JRadioButton("Tercera Opcion del Radio");
 JCheckBox check1 = new JCheckBox("Primera Opcion del Check");
 JCheckBox check2 = new JCheckBox("Segunda Opcion del Check");
 JCheckBox check3 = new JCheckBox("Tercera Opcion del Check");
 JTextArea mensajes = new JTextArea(30,35);
 JScrollPane scroll1 = new JScrollPane(mensajes);
 JScrollPane scroll2 = new JScrollPane(panel);
 
 public panelControl() {
  super("Los Grupos");
  setLocation(100,100);
  setSize(230,270);
  panel.add(check1);
  panel.add(check2);
  panel.add(check3);
  panel.add(radio1);
  panel.add(radio2);
  panel.add(radio3);
  grupoCheck.add(check1);
  grupoCheck.add(check2);
  grupoCheck.add(check3);
  grupoRadio.add(radio1);
  grupoRadio.add(radio2);
  grupoRadio.add(radio3);
  mensajes.setEditable(false);
  panel.add("South",scroll1);
  check1.addActionListener(new MiEscucha());
  check2.addActionListener(new MiEscucha());
  check3.addActionListener(new MiEscucha());
  radio1.addActionListener(new MiEscucha());
  radio2.addActionListener(new MiEscucha());
  radio3.addActionListener(new MiEscucha());
  add(scroll2);
  show();
  this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 }
 
 class MiEscucha implements ActionListener {
  public void actionPerformed(ActionEvent evt) {
   mensajes.append("Presionado el boton: "+
    evt.getActionCommand()+"\n");
  }
 }
 
 public static void main(String[] arg) {
    JOptionPane.showMessageDialog(null,"Un Panel con Scroll\n");
    JOptionPane.showMessageDialog(null,"By Carlitox ("+
   "http://calitoxenlaweb.blogspot.com)");
    new panelControl();
 }
}