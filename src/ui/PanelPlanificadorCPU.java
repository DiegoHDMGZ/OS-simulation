package ui;

import ui.backup.PanelEstadisticasResumenBackUp;
import model.gestionMemoria.Estrategia;
import ui.panelMemoria.FrameLinkedList;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.net.URL;
import java.util.*;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.Timer;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import model.general.SO;
import model.io.Device;
import model.io.Interrupcion;
import model.gestionProcesos.Politica;
import model.general.Proceso;
import model.gestionMemoria.Bloque;
import model.gestionMemoria.TipoBloque;
import model.util.Conversor;
import test.Generator;
import ui.panelSegmentoProcesos.SegmentoMemoria;
import ui.util.GradientButton;


@SuppressWarnings("serial")
public class PanelPlanificadorCPU extends JFrame implements ActionListener {

    private SO so;
    final private String nombresCortos[] = {"FCFS", "SJF Exp", "SJF No Exp", "Prio Exp", "Prio No Exp", "Prio-Time Exp",
            "Prio-Time No Exp", "RR", "Multinivel"};
    final private String nombresLargos[] = {"First Come First Served", "Shortest Job First Expropiativo",
            "Shortest Job First No Expropiativo", "Prioridades Expropiativo",
            "Prioridades No Expropiativos","Prioridades-Tiempo Expropiativo", "Prioridades-Tiempo No Expropiativo" ,"Round Robin", "Multinivel"};
    
    final private Politica politicaFijo[] = {Politica.FCFS , Politica.SJF , Politica.SJF, Politica.PRIORITY 
            , Politica.PRIORITY , Politica.PRIORITY_TIME , Politica.PRIORITY_TIME , Politica.RR, Politica.MULTILEVEL};
    final private Boolean expropiativoFijo[] = {false , true , false, true, false, true , false, true , true};
    final private Estrategia estrategiasFijo[] = {Estrategia.FirstFit , Estrategia.BestFit, Estrategia.WorstFit};
    
    private List<PoliticaGUI> listPoliticas = new ArrayList<>();
    final private String nombresCortosMemoria[] = {"First Fit", "Best Fit", "Worst Fit"};
    final private String nombresLargosMemoria[] = {"First Fit", "Best Fit", "Worst Fit"};
    private List<PoliticaGUI> listPoliticasMemory = new ArrayList<>();
    private JCheckBox comenzarSimulacion;
    private ImageIcon playPic, pausePic, pressPic;
   
    private PanelDeColaDeProcesos x = new PanelDeColaDeProcesos();
    private PanelInterrupciones y = new PanelInterrupciones();
    private FrameLinkedList frame =  FrameLinkedList.GenerarPanelMemoria();
    JPanel memoria = new JPanel();
    JPanel segmentos = new JPanel();

    private JMenuBar menuBar;
    private JMenu fileMenu, algoritmoMenu,estrategiaMenu ,opcionesMenu, velocidadMenu;
    private JMenuItem nuevoMenuItem, resetMenuItem, salirMenuItem;
    private JRadioButtonMenuItem fps1, fps10, fps20, fps30, fps40, fps50, fps60, fps70, fps80, fps90, fps100;
    List<Proceso> j ;
    List<Proceso> r1 ; // RR or FCFS
    TreeSet<Proceso> r2 ; // priority o SJF
    Map<Device, List<Proceso>> d ;
    Politica politica ;
    List<Device> devices ;
    Estrategia estrategiaActual = Estrategia.FirstFit;
    Politica politicaActual = Politica.FCFS;
    Boolean esExpropiativo = Boolean.TRUE;
    int quantum =0;
    private JLabel statusBar, algolLbl, estratLabel;
    private PanelEstadisticas waitSP, turnSP, responseSP;
    private PanelEstadisticasMemoria memoriaSP;
    private PanelReloj cpuTimePanel;

    private JButton mostrarInterrupciones;
    private JButton mostrarColaDeProcesos;
    private JButton mostrarEstadisticas;
    private JButton mostrarListaEncadenada;
    private GradientButton agregarProceso;
    private GradientButton ejecucionRapida;
    
    
    public Double estadisticas[][][];
    String mensaje = "CONSIDERACIONES A TENER EN CUENTA"
            + "\n1. Para ejecutar sobre los mismo procesos, Archivo -> Resetear los mismos procesos\n"
            + "2. Para ejecutar sobre distintos procesos  Archivo ->  nuevos procesos (aleatorios) \n"
            + "3. Para acceder al PCB, pause la simulación y haga click en el id del proceso\n"
            + "4. En el modo no expropiativo se ignoran las interrupciones";
    public static int panelInterrup = 0;
    public static int panelColas = 0;
    int frameNumber = -1;
    int fps = 30;
    int retrasar;
    int cont = 0;
    int contEstad = 0;
    int contInterrupciones = 0;
    Timer temporizador;
    Timer temporizadorGraf;
    boolean pausa = true;
    boolean terminado = true;

    JPanel panelColaProcesos;
    JPanel listPoliticasCheckbox;

    String currentAlg;
    String nombreEstrategiaActual;

    /**
     * Constructor por defecto, construye y muestra un objeto aleatoria de
     * PlanificadorCPU.
     */
    public PanelPlanificadorCPU() {

        setBackground(Color.WHITE);

        Toolkit tk = Toolkit.getDefaultToolkit();
        Dimension screenSize = tk.getScreenSize();
        int screenHeight = screenSize.height;
        int screenWidth = screenSize.width;
        setSize(screenWidth * 19 / 20, screenHeight * 19 / 20);
        setLocation(screenWidth / 4, screenHeight / 4);
        setLocationRelativeTo(null);

        // configurando animacion y simulacion
        // so = new PlanificadorCPU();

        so = new SO(politicaActual, esExpropiativo, quantum,estrategiaActual);
        retrasar = (fps > 0) ? (1000 / fps) : 100;
        temporizador = new Timer(retrasar, this);
        temporizadorGraf = new Timer(2, this);
        temporizadorGraf.setCoalesce(false); // no combine eventos en cola
        temporizadorGraf.setInitialDelay(1);
        temporizador.setCoalesce(false); // no combine eventos en cola
        temporizador.setInitialDelay(0);
        // so.setFps(retrasar);

        // configuracion de panel
        setTitle("Simulador de Planificacion de Memoria y Procesos de la CPU");
        buildPoliticas();
        buildEstrategias();
        construirBotonesDeSimulacion();
        panelColaProcesos = new JPanel();
        JScrollPane scrollerPanelColaProcesos = new JScrollPane(panelColaProcesos);
        scrollerPanelColaProcesos.setBorder(null);

        construirMenus();
        buildStatusPanels();
        llenarPanelColaProcesos();
        actualizaSalidaDeDatosPorInterfaz();
        estadisticas = new Double[3][9][9];
        currentAlg = nombresCortos[0];
        nombreEstrategiaActual = nombresCortosMemoria[0];
        Container masterPanel = getContentPane();
        
        
        masterPanel.setLayout(new BoxLayout(masterPanel, BoxLayout.X_AXIS));
        
        JPanel extraTopRow = new JPanel();
        extraTopRow.setLayout(new GridBagLayout());
        extraTopRow.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel topRow = new JPanel();
        topRow.setLayout(new BorderLayout());
        topRow.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel middleRow = new JPanel();
        middleRow.setLayout(new FlowLayout(FlowLayout.CENTER));
        middleRow.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JPanel bottomRow = new JPanel();
        bottomRow.setLayout(new BoxLayout(bottomRow, BoxLayout.Y_AXIS));
        bottomRow.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        listPoliticasCheckbox = new JPanel();
        listPoliticasCheckbox.setLayout(new BoxLayout(listPoliticasCheckbox, BoxLayout.Y_AXIS));
        listPoliticasCheckbox.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        listPoliticasCheckbox.setAlignmentY(CENTER_ALIGNMENT);
        JLabel TituloList = new JLabel("Lista de Politicas Realizadas");
        TituloList.setAlignmentY(CENTER_ALIGNMENT);

        listPoliticasCheckbox.add(TituloList);
        for (PoliticaGUI politica :
                listPoliticas) {
            listPoliticasCheckbox.add(politica.getCheckBox());
        }
        
        topRow.add(scrollerPanelColaProcesos, "Center");
       // topRow.add(listPoliticasCheckbox, "West");
        //topRow.add(rafa, "East");
        
        
        

//        middleRow.add(cpuTimePanel);
        middleRow.add(responseSP);
        middleRow.add(turnSP);
        middleRow.add(waitSP);
        middleRow.add(memoriaSP);
        //middleRow.add(algolLbl);
        //bottomRow.add(statusBar,"North");
        bottomRow.add(statusBar,"North");
        bottomRow.add(algolLbl,"North");
        bottomRow.add(estratLabel,"North");
        bottomRow.add(cpuTimePanel,"North");
        bottomRow.add(middleRow, "Center");
        bottomRow.add(comenzarSimulacion, "South");
        

        // Leyendas
        JPanel panelLeyenda = new JPanel();
        panelLeyenda.setLayout(new FlowLayout(FlowLayout.LEADING, 10, 0));

        JLabel leyenda = new JLabel("Leyenda: ");
        leyenda.setFont(new Font("Helvetica", Font.BOLD, 13));
        JLabel leyendaBloqueado = new JLabel("Bloqueado");
        leyendaBloqueado.setFont(new Font("Helvetica", 12, 12));
        leyendaBloqueado.setForeground(Color.RED);
        JLabel leyendaColaListos = new JLabel("Listo");
        leyendaColaListos.setFont(new Font("Helvetica", Font.BOLD, 12));
        leyendaColaListos.setForeground(Color.ORANGE);
        JLabel leyendaEjecutando = new JLabel("Ejecutando");
        leyendaEjecutando.setFont(new Font("Helvetica", Font.BOLD, 12));
        leyendaEjecutando.setForeground(Color.GREEN);
        JLabel leyendaTerminado = new JLabel("Terminado");
        leyendaTerminado.setFont(new Font("Helvetica", 12, 12));
        leyendaTerminado.setForeground(Color.BLACK);
        JLabel leyendaTerminadoAnormal = new JLabel("Terminado Anormalmente");
        leyendaTerminadoAnormal.setFont(new Font("Helvetica", 12, 12));
        leyendaTerminadoAnormal.setForeground(Color.magenta);

        panelLeyenda.add(leyenda);
        panelLeyenda.add(leyendaBloqueado);
        panelLeyenda.add(leyendaColaListos);
        panelLeyenda.add(leyendaEjecutando);
        panelLeyenda.add(leyendaTerminado);
        panelLeyenda.add(leyendaTerminadoAnormal);

        JPanel panelMensaje = new JPanel(new FlowLayout(FlowLayout.LEFT));
        panelMensaje.setSize(20, 20);
        panelMensaje.setEnabled(false);
        JPanel panelBotones = new JPanel(new FlowLayout(FlowLayout.RIGHT));

        bottomRow.add(panelLeyenda, "South");
        JTextArea advisor = new JTextArea(mensaje, 4, 0);
        advisor.setEnabled(false);
        advisor.setFont(new Font("Arial", Font.BOLD, 12));
        advisor.setForeground(Color.BLACK);
        advisor.setBackground(this.getForeground());
        advisor.setSize(20, 20);
        panelMensaje.add(advisor);
        advisor.setDisabledTextColor(Color.BLACK);

        mostrarEstadisticas = new JButton("Mostrar Estadisticas");
        panelBotones.add(mostrarEstadisticas);
        mostrarEstadisticas.addActionListener(this);

        mostrarInterrupciones = new JButton("Mostrar Interrupciones");
        panelBotones.add(mostrarInterrupciones);
        mostrarInterrupciones.addActionListener(this);

        mostrarColaDeProcesos = new JButton("Mostrar Cola De Procesos");
        panelBotones.add(mostrarColaDeProcesos);
        mostrarColaDeProcesos.addActionListener(this);
        
        mostrarListaEncadenada = new JButton("Mostrar Lista Encadenada");
        panelBotones.add(mostrarListaEncadenada);
        //mostrarMemoria.setBackground(Color.red);
        mostrarListaEncadenada.addActionListener(this);
        
        agregarProceso = new GradientButton(Color.BLUE , Color.DARK_GRAY, GradientButton.TOP_TO_BOTTOM,"Agregar Proceso");
        agregarProceso.setForeground(Color.WHITE);
        agregarProceso.setBackground(Color.GREEN);
        panelBotones.add(agregarProceso);
        agregarProceso.addActionListener(this);
        
        ejecucionRapida = new GradientButton(Color.red , Color.DARK_GRAY, GradientButton.TOP_TO_BOTTOM,"Ejecucion rapida");
        ejecucionRapida.setForeground(Color.WHITE);
        ejecucionRapida.setBackground(Color.GREEN);
        panelBotones.add(ejecucionRapida);
        ejecucionRapida.addActionListener(this);
        
        
        
        GridBagConstraints grid = new GridBagConstraints();
        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.weightx = 0.5;
        grid.gridx = 0;
        grid.gridy = 0;
        //extraTopRow.add(panelMensaje, grid);

        grid.fill = GridBagConstraints.HORIZONTAL;
        grid.weightx = 0.5;
        grid.gridx = 5;
        grid.gridy = 0;
        extraTopRow.add(panelBotones, grid);
        
        
        JPanel bottomRow2 = new JPanel();   
      
               
        //enviar Datos del proceso
           BoxLayout box = new BoxLayout(memoria, BoxLayout.Y_AXIS);
        memoria.setLayout(box);
        memoria.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        memoria.setBackground(Color.white);
        BoxLayout box2 = new BoxLayout(segmentos, BoxLayout.Y_AXIS);
        segmentos.setLayout(box2);
        segmentos.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); 
        segmentos.setBackground(Color.white);
        bottomRow2.setLayout(new BoxLayout(bottomRow2, BoxLayout.Y_AXIS));
        bottomRow2.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));        
        bottomRow2.add(extraTopRow,"North");
        bottomRow2.add(topRow,"Center");
        bottomRow2.add(bottomRow,"South");
        JLabel nom= new JLabel("Gestion de Memoria ("+Conversor.fromMBtoGB(so.getSizeMemoria())+ " GB" + ")");
        nom.setAlignmentX(Component.CENTER_ALIGNMENT);
        nom.setBackground(new Color(234, 234, 234  ));
        JPanel bottomRow3 = new JPanel();  
        
        bottomRow3.setLayout(new BoxLayout(bottomRow3, BoxLayout.Y_AXIS));
        //bottomRow3.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));   
        bottomRow3.add(nom);
        bottomRow3.add(memoria);    
        bottomRow3.setBackground(new Color(234, 234, 234  ));  
        
        /*JLabel tituloSegmentos= new JLabel("Proceso ejecutando(segmentos memoria)");
        tituloSegmentos.setAlignmentX(Component.CENTER_ALIGNMENT);
        JPanel bottomRow4 = new JPanel();   
        
        bottomRow4.setLayout(new BoxLayout(bottomRow4, BoxLayout.Y_AXIS));
        
        bottomRow4.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));   
        bottomRow4.add(tituloSegmentos);
        bottomRow4.add(segmentos);
        bottomRow4.setBackground(new Color(234, 234, 234  ));*/
        
        masterPanel.add(bottomRow2);
        JPanel panelEstatico=new JPanel();
        panelEstatico.setSize(5,200);
        //panelEstatico.add(bottomRow4);
        panelEstatico.setBackground(new Color(234, 234, 234  ));
        JPanel panelEstatico2=new JPanel();
        JScrollPane scrollerPanelMemoria = new JScrollPane(panelEstatico2);
        scrollerPanelMemoria.setBorder(null);        
        panelEstatico2.add(bottomRow3);
        panelEstatico2.setBackground(new Color(234, 234, 234));
        scrollerPanelMemoria.setPreferredSize(new Dimension(600,150));
        //scrollerPanelMemoria.setContent(rect);
        masterPanel.add(panelEstatico);
        masterPanel.add(scrollerPanelMemoria);
        //masterPanel.add(new JPanel());
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
        setVisible(true);

    }

    /**
     * Remueve todos los PanelProceso que contiene la representacion grafica de la
     * cola de listos
     */
    private void vaciarPanelColaProcesos() {
        panelColaProcesos.removeAll();
    }

    private void buildPoliticas() {

        for (int i = 0; i < nombresCortos.length; i++) {
            listPoliticas.add(new PoliticaGUI(nombresCortos[i], nombresLargos[i]));
        }

    }
    private void buildEstrategias(){
        for (int i = 0; i < nombresCortosMemoria.length; i++) {
            listPoliticasMemory.add(new PoliticaGUI(nombresCortosMemoria[i], nombresLargosMemoria[i]));
        }
    }
    private void desmarcarListas() {
        contEstad = 0;
//        fcfs = sjfNoExp = rr = sjfExp = multiExp = prioNoExp = prioExp = prioTimeExp = prioTimeNoExp = false;
        for (PoliticaGUI politica :
                listPoliticas) {
            politica.setDone(false);
            politica.uncheck();
        }
    }

    private void desmarcarListasMemory() {
        contEstad = 0;
//        fcfs = sjfNoExp = rr = sjfExp = multiExp = prioNoExp = prioExp = prioTimeExp = prioTimeNoExp = false;
        for (PoliticaGUI politica :
                listPoliticasMemory) {
            politica.setDone(false);
            politica.uncheck();
        }
    }
    /**
     * Muestra los procesos del planificador en una cola
     */
    private void llenarPanelColaProcesos() {
        List<Proceso> v = so.getHistoricoProcesos();
        frame.setLista(so.getListaEnlazada());
        panelColaProcesos.setBackground(Color.white);
        panelColaProcesos.setOpaque(true);
        FlowLayout flay = new FlowLayout(FlowLayout.LEFT);
        panelColaProcesos.setLayout(flay);
        for (int i = 0; i < v.size(); i++) {
            PanelProceso p = new PanelProceso((Proceso) v.get(i));
            panelColaProcesos.add(p, "Left");
        }
        panelColaProcesos.revalidate();
    }

    /**
     * Setup the panels used to display status. CPU time, wait time, response time
     * and turnaround time
     */
    void buildStatusPanels() {
        statusBar = new JLabel("Tiempo de ejecucion: ");
        statusBar.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));
        statusBar.setAlignmentX(Component.CENTER_ALIGNMENT);

        cpuTimePanel = new PanelReloj("CPU");
        cpuTimePanel.setEstadisticas(0, 0, 0, 0);
        cpuTimePanel.setToolTipText("El porcentaje de uso del CPU y su rendimiento");

        waitSP = new PanelEstadisticas("Tiempo de espera");
        waitSP.setEstadisticas(0, 0, 0);
        waitSP.setToolTipText("El tiempo de espera es la cantidad total de tiempo que "
                + "un proceso en la cola de listos espera para ser ejecutado");

        turnSP = new PanelEstadisticas("Tiempo de retorno");
        turnSP.setEstadisticas(0, 0, 0);
        turnSP.setToolTipText("Tiempo transcurrido entre que se lanza un proceso y termina");

        responseSP = new PanelEstadisticas("Tiempo de respuesta");
        responseSP.setEstadisticas(0, 0, 0);
        responseSP.setToolTipText("Es el intervalo de tiempo desde que un proceso"
                + " es cargado en la cola de listos hasta que brinda su primera respuesta");

        memoriaSP = new PanelEstadisticasMemoria("Memoria","Fragmentacion: ","Tiempo de acceso a mem.: ", "Procs. cargados a mem.: ");
        memoriaSP.setEstadisticas(0.0,0.0,0);
        memoriaSP.setToolTipText("Son las estadisticas generales medibles de la memoria");

        algolLbl = new JLabel("Politica: \n FCFS", JLabel.CENTER);
        algolLbl.setBorder(BorderFactory.createEmptyBorder(0, 5, 5, 20));
        algolLbl.setAlignmentX(Component.CENTER_ALIGNMENT);

        estratLabel = new JLabel("Estrategia: First Fit", JLabel.CENTER);
        estratLabel.setBorder(BorderFactory.createEmptyBorder(0,5,5,10));
        estratLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
    }

    public void marcarLista(String currentAlg) {
        for (PoliticaGUI politica :
                listPoliticas) {
            String name = politica.getName();
            if (name.equalsIgnoreCase(currentAlg)) {
                politica.check();
                break;
            }
        }
    }

    public void saveEstadisticas(String currentAlg, String currentEstrategia) {
        Map<String, Integer> indice = new HashMap<>();
        for(int i = 0; i < nombresCortosMemoria.length ; i++)  {
            indice.put(nombresCortosMemoria[i], i);
        }
        
        int fil = 0;
        String messageBase = "Se ha terminado satisfactoriamente la politica ";
        for (PoliticaGUI politica :
                listPoliticas) {
            if (politica.getName().equalsIgnoreCase(currentAlg)) {
                fil = listPoliticas.indexOf(politica);
                politica.setDone(true);
               
                JOptionPane.showMessageDialog(null, messageBase + politica.getName());
                
                
            }
        }
        
        int indE = indice.get(currentEstrategia);
        estadisticas[indE][fil][0] = (double) fil;
        estadisticas[indE][fil][1] = Double.parseDouble(
                cpuTimePanel.getRend().getText().substring(0, cpuTimePanel.getRend().getText().indexOf(" ") - 1));
        estadisticas[indE][fil][2] = Double.parseDouble(
                cpuTimePanel.getUsoMed().getText().substring(0, cpuTimePanel.getUsoMed().getText().indexOf(" ") - 1));
        estadisticas[indE][fil][3] = Double.parseDouble(waitSP.getMedia().getText());
        estadisticas[indE][fil][4] = Double.parseDouble(responseSP.getMedia().getText());
        estadisticas[indE][fil][5] = Double.parseDouble(turnSP.getMedia().getText());
        estadisticas[indE][fil][6] = Double.parseDouble(memoriaSP.getFrag().getText().substring(0, memoriaSP.getFrag().getText().indexOf(" ")));
        estadisticas[indE][fil][7] = Double.parseDouble(memoriaSP.getTiempo().getText());
        estadisticas[indE][fil][8] = Double.parseDouble(memoriaSP.getProcesos().getText());
    }
    


    /*private int getQuantum(){
        int rr = 0;
        boolean req = false;
        do {
            String temp = JOptionPane.showInputDialog(null, "Ingrese el valor del Quantum");
            String regex = "^[1-9][0-9]*$";
            if (temp.matches(regex)) {
                rr = Integer.parseInt(temp);
                req = true;
            }
        } while (!req);
        return rr;
    }*/
    
    private int getQuantum() {
        return getCantidad("Ingrese el valor del quantum (positivo)" , 1 , 1000000);
    }
    
    private int getCantidad(String mensaje, int minimo, int maximo) {
        int x = 0;
        while(true){
            String temp = JOptionPane.showInputDialog(null, mensaje);
            if(temp == null) {
                continue;
            }
            String regex = "^[0-9][0-9]*$";
            if (temp.matches(regex)) {
                x = Integer.parseInt(temp);
                if(x >= minimo && x <= maximo) {      
                    break;
                }
            }
        } 
        return x;
    }

    public void obtenerListas(){
        j = so.getScheduler().getJobQueue();
        r1 = so.getScheduler().getReadyQueue(); // RR or FCFS
        r2 = so.getScheduler().getReadyQueue2(); // priority o SJF
        d = so.getScheduler().getDeviceQueue();
        politica = so.getScheduler().getPolitica();
        devices = so.getDevices();
    }

    /**
     * Metodo implementado de la interface ActionListener Comprueba el origen del
     * evento y responde
     */
    @SuppressWarnings("static-access")
    public void actionPerformed(ActionEvent e) {
        // Comenzar la Simulacion boton Start
        if (e.getSource() == comenzarSimulacion) {
            if (!pausa) {
                pausa = true;
                pararAnimacion();
            } else {
                pausa = false;
                agregarProceso.setEnabled(false);
                comenzarAnimacion();
            }
            // Temporizador
        } else if (e.getSource() == temporizadorGraf) {
            vaciarPanelColaProcesos();
            llenarPanelColaProcesos();      
            repaint();
            //frame.drawBurst();
        } else if (e.getSource() == temporizador) {
            if (panelInterrup > 0 && PanelInterrupciones.nroVentanas == 1 && contInterrupciones > 8) {
                contInterrupciones = 0;
                List<Interrupcion> interr = so.getHistoricoInterrupciones();
                y.correrInterrupcion(interr);
                panelInterrup++;
            }

            if ((panelColas > 0 && PanelDeColaDeProcesos.nroVentanas == 1 && cont > 8)) {
                cont = 0;
                obtenerListas();
                x.correrProceso(j, r1, r2, d, politica, devices);
                panelColas++;
            }
            cont++;
            contInterrupciones++;
            if (so.run(1)) {
                actualizaSalidaDeDatosPorInterfaz();
                vaciarPanelColaProcesos();
                llenarPanelColaProcesos();                
                terminado = false;
            } else {
                terminado = true;
                pararAnimacion();
                marcarLista(currentAlg);
                comenzarSimulacion.doClick();
                saveEstadisticas(currentAlg , nombreEstrategiaActual);
            }
//            if(frame.isVisible()){
//                frame.drawBurst();
//                frame.repaint();
//            }
            repaint();
        } else if (e.getSource() == nuevoMenuItem) {
            // Crear nuevos procesos Aleatorios
            Generator.changeSeed();
            agregarProceso.setEnabled(true);
            desmarcarListas();
            so = new SO(Politica.FCFS, false, 0);
            vaciarPanelColaProcesos();
            llenarPanelColaProcesos();
            estadisticas = new Double[3][9][9];
            actualizaSalidaDeDatosPorInterfaz();
            repaint();


        } else if (e.getSource() == resetMenuItem) {
            // so.restaurar(); se debe hacer un new CPU(politica, etc)
            agregarProceso.setEnabled(true);
            Politica politicaActual = SO.getScheduler().getPolitica();
            Boolean expActual = SO.getScheduler().getExpropiativo();
            int quantumActual = SO.getQuantum();
            so = new SO(politicaActual, expActual, quantumActual);
            //fcfsMenuItem.setSelected(true);
            vaciarPanelColaProcesos();
            llenarPanelColaProcesos();
            estadisticas = new Double[3][9][9];
            actualizaSalidaDeDatosPorInterfaz();

            repaint();

            // Salir del programa
        } else if (e.getSource() == salirMenuItem) {
            pararAnimacion();
            dispose();
            System.exit(0);
            // Ajustar la velocidad de simulacion
        }else if (e.getSource() == mostrarInterrupciones) {
            List<Interrupcion> interr = so.getHistoricoInterrupciones();
            if (panelInterrup == 0) {
                panelInterrup++;
                y.setVisible(true);
                y.correrInterrupcion(interr);
            } else if (panelInterrup > 0 && PanelInterrupciones.nroVentanas == 0) {
                y.setVisible(true);
                y.correrInterrupcion(interr);
            }
        } else if (e.getSource() == mostrarColaDeProcesos) {
            obtenerListas();
            if (panelColas == 0) {
                // JFrame panelColasDeProcesos = new PanelDeColaDeProcesos("Cola De
                // Procesos",j,r1,r2,d,politica,devices);
                panelColas++;
                x.setVisible(true);
                x.correrProceso(j, r1, r2, d, politica, devices);
            } else if (panelColas > 0 && PanelDeColaDeProcesos.nroVentanas == 0) {
                x.setVisible(true);
                x.correrProceso(j, r1, r2, d, politica, devices);
            }
        } else if (e.getSource() == mostrarEstadisticas){
            new PanelEstadisticasResumen("Resumen de Estadistica",estadisticas);
            //new PanelEstadisticasResumenBackUp("Resumen Estadisticas", estadisticas);
        }else if(e.getSource() == mostrarListaEncadenada){
            frame.setVisible(true);
            
        }else if (e.getSource() == ejecucionRapida) {
            /*so = new SO(Politica.FCFS, false, 0 , Estrategia.BestFit);
            
            while(so.run(0)) {
                
            }
            so = new SO(Politica.SJF, true, 0 , Estrategia.FirstFit);
            while(so.run(0)) {
                
            }*/
            int quant = 0;
            Boolean quantumNecesario = false;
            for(int i = 0; i < 3 ; i++) {
                for(int j = 0; j < 9; j++) {
                    if(estadisticas[i][j][0] == null) {
                        if(politicaFijo[j] == Politica.RR || politicaFijo[j] == Politica.MULTILEVEL) {
                            quantumNecesario = true;
                            break;
                        } 
                    }
                }
            }
            if(quantumNecesario) {
                quant = getQuantum();
            }
            for(int i = 0; i < 3 ; i++) {
                for(int j = 0; j < 9; j++) {         
                    if(estadisticas[i][j][0] == null) {
                        if(politicaFijo[j] == Politica.RR || politicaFijo[j] == Politica.MULTILEVEL) {
                            so = new SO(politicaFijo[j] , expropiativoFijo[j] , quant, estrategiasFijo[i]);
                        } else {
                            so = new SO(politicaFijo[j] , expropiativoFijo[j] , 0, estrategiasFijo[i]);
                        }

                        while(so.run(0)) {
                            
                        }
                        
                         cpuTimePanel.setEstadisticas(so.getRendimiento(), so.getUsoCPUMinimo(), so.getUsoCPUMedio(),
                                so.getUsoCPUMaximo());
                        waitSP.setEstadisticas(so.getTiempoEsperaMinimo(), so.getTiempoEsperaMedio(), so.getTiempoEsperaMaximo());
                        responseSP.setEstadisticas(so.getTiempoRespuestaMinimo(), so.getTiempoRespuestaMedio(),
                                so.getTiempoRespuestaMaximo());
                        turnSP.setEstadisticas(so.getTiempoRetornoMinimo(), so.getTiempoRetornoMedio(), so.getTiempoRetornoMaximo());
                        memoriaSP.setEstadisticas(so.getFragmentacionPromedio(),so.getTiempoAccesoMemoria(),so.getCargadosEnMemoria());
                        
                        estadisticas[i][j][0] = (double) j;
                        estadisticas[i][j][1] = Double.parseDouble(
                                cpuTimePanel.getRend().getText().substring(0, cpuTimePanel.getRend().getText().indexOf(" ") - 1));
                        estadisticas[i][j][2] = Double.parseDouble(
                                cpuTimePanel.getUsoMed().getText().substring(0, cpuTimePanel.getUsoMed().getText().indexOf(" ") - 1));
                        estadisticas[i][j][3] = Double.parseDouble(waitSP.getMedia().getText());
                        estadisticas[i][j][4] = Double.parseDouble(responseSP.getMedia().getText());
                        estadisticas[i][j][5] = Double.parseDouble(turnSP.getMedia().getText());
                        estadisticas[i][j][6] = Double.parseDouble(memoriaSP.getFrag().getText().substring(0, memoriaSP.getFrag().getText().indexOf(" ")));
                        estadisticas[i][j][7] = Double.parseDouble(memoriaSP.getTiempo().getText());
                        estadisticas[i][j][8] = Double.parseDouble(memoriaSP.getProcesos().getText());
                        vaciarPanelColaProcesos();
                        repaint();
                        /*estadisticas[i][j][1] = soAux.getRendimiento();
                        estadisticas[i][j][2] = soAux.getUsoCPUMedio();
                        estadisticas[i][j][3] = soAux.getTiempoEsperaMedio();
                        estadisticas[i][j][4] = soAux.getTiempoRespuestaMedio();
                        estadisticas[i][j][5] = soAux.getTiempoRetornoMedio();
                        estadisticas[i][j][6] = soAux.getFragmentacionPromedio(); ;
                        estadisticas[i][j][7] = soAux.getTiempoAccesoMemoria();;
                        estadisticas[i][j][8] = (double)soAux.getCargadosEnMemoria();*/
                    }
                }
            }
            JOptionPane.showMessageDialog(null, "Ejecución rápida finalizada correctamente");
        } else if(e.getSource() == agregarProceso) {
           int burst , prioridad , memoria ;
           burst = getCantidad("Ingrese el valor del Burst Time  [4 -  70]" , 4 , 70  );
           prioridad = getCantidad("Ingrese el valor de la prioridad [0 - 10]" , 0 , 10);
           memoria = getCantidad("Ingrese el valor del tamaño (MB) (>= 4 MB) " , 4 , 1000000);
            so.addProcesoTemporal(burst, prioridad, memoria);
        } else if (e.getSource() == fps1) {
            ajustarFPS(1);
        } else if (e.getSource() == fps10) {
            ajustarFPS(10);
        } else if (e.getSource() == fps20) {
            ajustarFPS(20);
        } else if (e.getSource() == fps30) {
            ajustarFPS(30);
        } else if (e.getSource() == fps40) {
            ajustarFPS(40);
        } else if (e.getSource() == fps50) {
            ajustarFPS(50);
        } else if (e.getSource() == fps60) {
            ajustarFPS(60);
        } else if (e.getSource() == fps70) {
            ajustarFPS(70);
        } else if (e.getSource() == fps80) {
            ajustarFPS(80);
        } else if (e.getSource() == fps90) {
            ajustarFPS(90);
        } else if (e.getSource() == fps100) {
            ajustarFPS(100);
        } else {
            
            for (PoliticaGUI politica :
                    listPoliticas) {
                if(e.getActionCommand() == politica.getName()){
                    pararAnimacion();
                    String name = politica.getName();
                    String message = "Politica: \n "+name;
                    comenzarSimulacion.setSelected(false);
                    if ( name.equalsIgnoreCase(nombresCortos[0]) ){
                        politicaActual = Politica.FCFS;
                        esExpropiativo = Boolean.FALSE;
                        quantum =0;
                        algolLbl.setText(message);
                        currentAlg = nombresCortos[0];
                    } else if ( name.equalsIgnoreCase(nombresCortos[1]) ){
                        politicaActual = Politica.SJF;
                        esExpropiativo = Boolean.TRUE;
                        quantum =0;
                        algolLbl.setText(message);
                        currentAlg = nombresCortos[1];
                    } else if ( name.equalsIgnoreCase(nombresCortos[2]) ){
                        politicaActual = Politica.SJF;
                        esExpropiativo = Boolean.FALSE;
                        quantum =0;
                        algolLbl.setText(message);
                        currentAlg = nombresCortos[2];
                    } else if ( name.equalsIgnoreCase(nombresCortos[3]) ){
                        politicaActual = Politica.PRIORITY;
                        esExpropiativo = Boolean.TRUE;
                        quantum =0;
                        algolLbl.setText(message);
                        currentAlg = nombresCortos[3];
                    } else if ( name.equalsIgnoreCase(nombresCortos[4]) ){
                        politicaActual = Politica.PRIORITY;
                        esExpropiativo = Boolean.FALSE;
                        quantum =0;
                        algolLbl.setText(message);
                        currentAlg = nombresCortos[4];
                    } else if ( name.equalsIgnoreCase(nombresCortos[5]) ){
                        politicaActual = Politica.PRIORITY_TIME;
                        esExpropiativo = Boolean.TRUE;
                        quantum =0;
                        algolLbl.setText(message);
                        currentAlg = nombresCortos[5];
                    } else if ( name.equalsIgnoreCase(nombresCortos[6]) ){
                        politicaActual = Politica.PRIORITY;
                        esExpropiativo = Boolean.FALSE;
                        quantum =0;
                        algolLbl.setText(message);
                        currentAlg = nombresCortos[6];
                    } else if ( name.equalsIgnoreCase(nombresCortos[7]) ) {
                        quantum = getQuantum();
                        politicaActual = Politica.RR;
                        esExpropiativo = Boolean.TRUE;
                        algolLbl.setText(message);
                        currentAlg = nombresCortos[7];
                    } else if ( name.equalsIgnoreCase(nombresCortos[8]) ){
                        politicaActual = Politica.MULTILEVEL;
                        esExpropiativo = Boolean.TRUE;
                        quantum = getQuantum();
                        algolLbl.setText(message);
                        currentAlg = nombresCortos[8];
                    }
                }
            }
            for (PoliticaGUI estrategia :
                    listPoliticasMemory) {
                if(e.getActionCommand() == estrategia.getName()){
                    pararAnimacion();
                    //System.out.println(e.getActionCommand());
                    String name = estrategia.getName();
                    comenzarSimulacion.setSelected(false);
                    String message = "Estrategia: "+name;
                    if ( name.equalsIgnoreCase(nombresCortosMemoria[0]) ){
                        estrategiaActual = Estrategia.FirstFit;
                        nombreEstrategiaActual = nombresCortosMemoria[0];
                    } else if ( name.equalsIgnoreCase(nombresCortosMemoria[1]) ){
                        estrategiaActual = Estrategia.BestFit;
                        nombreEstrategiaActual = nombresCortosMemoria[1];
                    }else{
                        estrategiaActual = Estrategia.WorstFit;
                        nombreEstrategiaActual = nombresCortosMemoria[2];
                    }
                    estratLabel.setText(message);
                }
            }
            agregarProceso.setEnabled(true);
            so = new SO(politicaActual,esExpropiativo,quantum,estrategiaActual);
        }
    }

    /**
     * Puede ser invocado desde cualquier hilo
     */
    public synchronized void comenzarAnimacion() {
        if (pausa) {
            // No hacer nada.
        } else {
            if (!temporizador.isRunning() && !temporizadorGraf.isRunning()) {
                temporizador.start();
                temporizadorGraf.start();
            }
        }
    }

    /**
     * Puede ser invocado desde cualquier hilo
     */
    public synchronized void pararAnimacion() {
        // Detiene el hilo de animacion
        if (temporizador.isRunning() && temporizadorGraf.isRunning()) {
            temporizador.stop();
            temporizadorGraf.stop();
        }
    }

    /**
     * Actualiza los estados y estadisticas del planificador
     */
    @SuppressWarnings("static-access")
    private void actualizaSalidaDeDatosPorInterfaz() {
        statusBar.setText("Tiempo De Ejecucion : " + so.getTiempoSimulacion());
        cpuTimePanel.setEstadisticas(so.getRendimiento(), so.getUsoCPUMinimo(), so.getUsoCPUMedio(),
                so.getUsoCPUMaximo());
        waitSP.setEstadisticas(so.getTiempoEsperaMinimo(), so.getTiempoEsperaMedio(), so.getTiempoEsperaMaximo());
        responseSP.setEstadisticas(so.getTiempoRespuestaMinimo(), so.getTiempoRespuestaMedio(),
                so.getTiempoRespuestaMaximo());
        turnSP.setEstadisticas(so.getTiempoRetornoMinimo(), so.getTiempoRetornoMedio(), so.getTiempoRetornoMaximo());
        memoriaSP.setEstadisticas(so.getFragmentacionPromedio(),so.getTiempoAccesoMemoria(),so.getCargadosEnMemoria());
       LinkedList<Bloque> memoriaProceso=so.getListaEnlazada();
        memoria.removeAll();
        
        LinkedList <Bloque> segmentosDeProceso=new LinkedList();
        for(Bloque bloque: memoriaProceso){
            SegmentoMemoria proceso1= new SegmentoMemoria(bloque.getInicioHexadecimal(),bloque.getFinHexadecimal(),bloque.getTipo(),bloque.getPid(),bloque.getLongitud(),bloque.enEjeucion(),bloque.getSegmentoProceso(),true);
            memoria.add(proceso1);
            if(bloque.enEjeucion()){
                segmentosDeProceso=bloque.getSegmentoProceso();
            }
            
        }
        segmentos.removeAll();
         boolean heap=false;
        for(Bloque bloque: segmentosDeProceso){
            if(bloque.esSeparacion()){
                 SegmentoMemoria proceso2= new SegmentoMemoria("","",TipoBloque.Proceso,0,10,bloque.enEjeucion(),bloque.getSegmentoProceso(),false);            
                 segmentos.add(proceso2);
            }
            SegmentoMemoria proceso1= new SegmentoMemoria(bloque.getInicioHexadecimal(),bloque.getFinHexadecimal(),bloque.getTipo(),0,bloque.getLongitud(),bloque.enEjeucion(),bloque.getSegmentoProceso(),false);            
            if(heap){
                proceso1= new SegmentoMemoria(bloque.getInicioHexadecimal(),bloque.getFinHexadecimal(),bloque.getTipo(),1,bloque.getLongitud(),bloque.enEjeucion(),bloque.getSegmentoProceso(),false);            
            }
            if(bloque.getTipo() ==TipoBloque.Heap){
                heap=true;
            }
            segmentos.add(proceso1);
        }
        
    }

    /**
     * Construye todos los Menus para la aplicacions
     */
    void construirMenus() {

        menuBar = new JMenuBar();
        setJMenuBar(menuBar);

        fileMenu = new JMenu("Archivo");

        nuevoMenuItem = new JMenuItem("Nuevos procesos (aleatorios)");
        nuevoMenuItem.addActionListener(this);
        fileMenu.add(nuevoMenuItem);

        resetMenuItem = new JMenuItem("Resetear los mismo procesos");
        resetMenuItem.addActionListener(this);
        fileMenu.add(resetMenuItem);

        salirMenuItem = new JMenuItem("Salir");
        salirMenuItem.addActionListener(this);
        fileMenu.add(salirMenuItem);

        menuBar.add(fileMenu);

        // Menu de opciones
        opcionesMenu = new JMenu("Opciones");
        // SubMenu de Algoritmos
        algoritmoMenu = new JMenu("Politicas");

        ButtonGroup grupoAlgoritmos = new ButtonGroup();


        for (PoliticaGUI politica :
                listPoliticas) {
            if (listPoliticas.indexOf(politica) == 0) {
                politica.getItemMenu().setSelected(true);
            }
            grupoAlgoritmos.add(politica.getItemMenu());
            politica.getItemMenu().addActionListener(this);
            algoritmoMenu.add(politica.getItemMenu());
        }

        opcionesMenu.add(algoritmoMenu);

        //SubMenu de estrategias

        estrategiaMenu = new JMenu("Estrategias");
        ButtonGroup grupoEstrategias = new ButtonGroup();

        for(PoliticaGUI estrategia : listPoliticasMemory){
            if (listPoliticasMemory.indexOf(estrategia) == 0) {
                estrategia.getItemMenu().setSelected(true);
            }
            grupoEstrategias.add(estrategia.getItemMenu());
            estrategia.getItemMenu().addActionListener(this);
            estrategiaMenu.add(estrategia.getItemMenu());

        }
        opcionesMenu.add(estrategiaMenu);

        // SubMenu de Velocidad de simulacions
        velocidadMenu = new JMenu("Velocidad de simulacion");

        velocidadMenu.setToolTipText("Ajuste la velocidad de so reloj animaci�n");
        ButtonGroup bg = new ButtonGroup();

        fps1 = new JRadioButtonMenuItem("1 fps");
        fps1.setToolTipText("Ajuste la velocidad de so reloj animaci�n");
        bg.add(fps1);
        fps1.addActionListener(this);
        velocidadMenu.add(fps1);

        fps10 = new JRadioButtonMenuItem("10 fps");
        fps10.setToolTipText("Ajuste la velocidad de so reloj animaci�n");
        bg.add(fps10);
        fps10.addActionListener(this);
        velocidadMenu.add(fps10);

        fps20 = new JRadioButtonMenuItem("20 fps");
        fps20.setToolTipText("Ajuste la velocidad de so reloj animaci�n");
        bg.add(fps20);
        fps20.addActionListener(this);
        velocidadMenu.add(fps20);

        fps30 = new JRadioButtonMenuItem("30 fps", true);
        fps30.setToolTipText("Ajuste la velocidad de so reloj animaci�n");
        bg.add(fps30);
        fps30.addActionListener(this);
        velocidadMenu.add(fps30);

        fps40 = new JRadioButtonMenuItem("40 fps");
        fps40.setToolTipText("Ajuste la velocidad de so reloj animaci�n");
        bg.add(fps40);
        fps40.addActionListener(this);
        velocidadMenu.add(fps40);

        fps50 = new JRadioButtonMenuItem("50 fps");
        fps50.setToolTipText("Ajuste la velocidad de so reloj animaci�n");
        bg.add(fps50);
        fps50.addActionListener(this);
        velocidadMenu.add(fps50);

        fps60 = new JRadioButtonMenuItem("60 fps");
        fps60.setToolTipText("Ajuste la velocidad de so reloj animaci�n");
        bg.add(fps60);
        fps60.addActionListener(this);
        velocidadMenu.add(fps60);

        fps70 = new JRadioButtonMenuItem("70 fps");
        fps70.setToolTipText("Ajuste la velocidad de so reloj animaci�n");
        bg.add(fps70);
        fps70.addActionListener(this);
        velocidadMenu.add(fps70);

        fps80 = new JRadioButtonMenuItem("80 fps");
        fps80.setToolTipText("Ajuste la velocidad de so reloj animaci�n");
        bg.add(fps80);
        fps80.addActionListener(this);

        fps90 = new JRadioButtonMenuItem("90 fps");
        fps90.setToolTipText("Ajuste la velocidad de so reloj animaci�n");
        bg.add(fps90);
        fps90.addActionListener(this);
        velocidadMenu.add(fps90);

        fps100 = new JRadioButtonMenuItem("100 fps");
        fps100.setToolTipText("Ajuste la velocidad de so reloj animaci�n");
        bg.add(fps100);
        fps100.addActionListener(this);
        velocidadMenu.add(fps100);

        opcionesMenu.add(velocidadMenu);
        opcionesMenu.addSeparator();

        menuBar.add(opcionesMenu);
    }

    /**
     * Ajuste de FPS para la animaci�n. Es limitado por la velocidad del hardware.
     */
    void ajustarFPS(int delay) {
        boolean state = pausa;
        pararAnimacion();
        delay = (delay > 0) ? (1000 / delay) : 100;
        temporizador.setDelay(delay);
        temporizadorGraf.setDelay(delay);
        if (!state && !terminado) {
            comenzarAnimacion();
        }
        // so.setFps(delay);
    }

    /**
     * Construir los botones para la simulacion
     */
    void construirBotonesDeSimulacion() {
        // Imagen Play
        URL iconURL = ClassLoader.getSystemResource("imagenes/play.png");
        if (iconURL != null) {
            playPic = new ImageIcon(iconURL, "play");
        }
        // Imagen Pause
        iconURL = ClassLoader.getSystemResource("imagenes/pause.png");
        if (iconURL != null) {
            pausePic = new ImageIcon(iconURL, "pause");
        }
        // Imagen cuando el boton esta precionado
        iconURL = ClassLoader.getSystemResource("imagenes/playing.png");
        if (iconURL != null) {
            pressPic = new ImageIcon(iconURL, "playing");
        }

        comenzarSimulacion = new JCheckBox(playPic, false);
        comenzarSimulacion.addActionListener(this);
        comenzarSimulacion.setSelectedIcon(pausePic);
        comenzarSimulacion.setPressedIcon(pressPic);
        comenzarSimulacion.setBorder(new EmptyBorder(0, 0, 0, 0));
        comenzarSimulacion.setToolTipText("Play/Pause");
        comenzarSimulacion.setAlignmentX(Component.LEFT_ALIGNMENT);
    }

    private void panelColaDeProcesos(String cola_De_Procesos, List<Proceso> j, List<Proceso> r1, TreeSet<Proceso> r2,
                                     Map<Device, List<Proceso>> d, Politica politica, List<Device> devices) {
        throw new UnsupportedOperationException("Not supported yet."); // To change body of generated methods, choose
        // Tools | Templates.
    }

}
