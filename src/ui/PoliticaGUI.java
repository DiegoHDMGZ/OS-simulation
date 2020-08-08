package ui;

import javax.swing.*;

public class PoliticaGUI {

    JRadioButtonMenuItem itemMenu;
    boolean done;
    JCheckBox checkBox;

    public PoliticaGUI(String shortName, String extendName ){

        this.itemMenu = new JRadioButtonMenuItem(shortName);
        this.itemMenu.setToolTipText(extendName);
        this.checkBox = new JCheckBox(shortName);
        this.checkBox.setToolTipText(extendName);
        initState();

    }

    public void initState(){
        checkBox.setEnabled(false);
        done = false;
    }

    public String getName(){
        return this.itemMenu.getText();
    }

    public  String getHelpText(){
        return this.itemMenu.getToolTipText();
    }

    public JRadioButtonMenuItem getItemMenu() {
        return this.itemMenu;
    }

    public void setItemMenu(JRadioButtonMenuItem itemMenu) {
        this.itemMenu = itemMenu;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public JCheckBox getCheckBox() {
        return this.checkBox;
    }

    public void setCheckBox(JCheckBox checkBox) {
        this.checkBox = checkBox;
    }

    public void check(){
        this.checkBox.setSelected(true);
    }

    public void uncheck(){
        this.checkBox.setSelected(false);
    }

    public void select(){
        this.itemMenu.setSelected(true);
    }

    public void unselect(){
        this.itemMenu.setSelected(false);
    }


}