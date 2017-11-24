package Vue;

import Controleur.FridgeControler;
import Model.FridgeModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Observable;
import java.util.Observer;

import javafx.scene.effect.Light;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.jfree.ui.RefineryUtilities;

public class FridgeView extends JFrame implements Observer {

    private FridgeControler controler; //objet controleur
    int VarTemperature = 15; //variable temperature
    int x = 0; 
    
    //creation des panels contenant des informations
    private Panel stats;
    private Panel componentUpdateState;
    private Panel updateWriteContainer;
    private Panel componentUpdateWrite;
    private Panel alerteContainer;
    private Panel componentGraphics;

    //creations des titres
    private Label componentStatTitle;
    private Label componentPeltierTemperature;
    private Label componentInternalTemperature;
    private Label componentHygrometry;
    protected Label componentConsigne;
    private Label componentRose;

    private Label componentAlerteTemperature;
    private Label componentAlerteCondensation;

    private Label proposition;

    public TextField componentUpdateTemperature;
    private Font gras;


    public Button validate; //creation du bouton de validation de la consigne

    public Container container; //creation du container des donn�es

    public FridgeView(FridgeControler controler) 
    {
        setTitle("Centre de controle du frigo"); //titre de la fenetre de l'application
        setSize(1200, 600); //taille de la fenetre
        this.controler = controler;


        Container c = getContentPane();
        c.setLayout(new BorderLayout());

        stats = new Panel(new GridLayout(6, 0));
        componentUpdateState = new Panel(new GridLayout(2, 0));
        componentUpdateWrite = new Panel();
        alerteContainer = new Panel();
        updateWriteContainer = new Panel(new GridLayout(2, 0));
        componentGraphics = new Panel();

        componentStatTitle = new Label("Etat du systeme");
        gras = new Font("Courrier", Font.BOLD, 20);
        componentStatTitle.setFont(gras);
        stats.setBackground(Color.lightGray);


        componentAlerteTemperature = new Label("Attention, la temperature du frigo augmente rapidement !");
        componentAlerteTemperature.setForeground(new Color(255, 0, 0));
        componentAlerteCondensation = new Label("Attention, de la condensation peut se former !");
        componentAlerteCondensation.setForeground(new Color(255, 0, 0));

        //graphe des temperatures
        XYSeries externe = new XYSeries("Temperatures Externes");
        XYSeries interne = new XYSeries("Temperature Interne");
        XYSeries consigne = new XYSeries("Temperature de Consigne");
        XYSeries pointrose = new XYSeries("Temperature Point de ros�e");
        XYSeriesCollection dataset = new XYSeriesCollection(externe);
        dataset.addSeries(interne);
        dataset.addSeries(consigne);
        dataset.addSeries(pointrose);
        
        
        
        JFreeChart chart = ChartFactory.createXYLineChart("Evolution temperature", "Temps (secondes)", "Degre  (�C)", dataset);
        componentGraphics.add(new ChartPanel(chart));


      //graphe d'humidit�
     //   XYSeries humidite = new XYSeries("Humidite");
       // XYSeriesCollection datahum = new XYSeriesCollection(humidite);
       // datahum.addSeries(humidite);
       // JFreeChart charthum = ChartFactory.createXYLineChart("Niveau humudit�", "Temps(Secondes)","% humidite", datahum);
       // componentGraphics.add(new ChartPanel(charthum));
        
       // Thread hum = new Thread(() -> {
         //   // Draw Graph
          //  while (true) {
           //     try {
            //            humidite.add(x++, controler.getHygro());
             //           componentGraphics.repaint();
              //          Thread.sleep(1000);
              //  } catch (Exception e) {
              //  }
       //     }
      //  });
      //  hum.start();
        
            Thread thread = new Thread(() -> {
                // Draw Graph
                while (true) {
                    try {
                            externe.add(x++,controler.getPeltierTemperature() ); // recupere la temparture du module peltier //controler.getPeltierTemperature() (a mettre a la place de la donn�e de simulation)
                            interne.add(x++, controler.getIntTemperature()); //recupere la temparture du module peltier //controler.getIntTemperature() (a mettre a la place de la donn�e de simulation)
                            consigne.add(x++, controler.getConsigneTemperature());//recupere la temparture du module peltier //controler.getConsigneTemperature() (a mettre a la place de la donn�e de simulation)
                            pointrose.add(x++, controler.getRose());//recupere la temparture du module peltier //controler.getRose() (a mettre a la place de la donn�e de simulation)
  
                            componentGraphics.repaint();
                            Thread.sleep(1000);
                    } catch (Exception e) {
                    }
                }
            });
            thread.start();

         
        
        componentInternalTemperature = new Label("Temperature Interne : " + controler.getIntTemperature() + " �C");
        componentPeltierTemperature = new Label("Temperature module Peltier : " + controler.getPeltierTemperature() + " �C");
        componentConsigne = new Label("Consigne : " + controler.getConsigneTemperature() + " �C");
        componentHygrometry = new Label("Humidite :" + controler.getHygro() + "%");
        componentRose = new Label("Point de Rosee :" + controler.getRose() + " �C");
        componentUpdateTemperature = new TextField();
        proposition = new Label("Vous pouvez changer la valeur de la consigne en l'ecrivant : ");


        validate = new Button("Valider");

        stats.add(componentStatTitle);
        stats.add(componentPeltierTemperature);
        stats.add(componentInternalTemperature);
        stats.add(componentHygrometry);
        stats.add(componentConsigne);
        stats.add(componentRose);
        

        componentUpdateWrite.add(proposition);
        componentUpdateWrite.add(componentUpdateTemperature);
        componentConsigne.setPreferredSize(new Dimension(50, 24));
        componentUpdateWrite.add(validate);


        updateWriteContainer.add(componentUpdateWrite);


        alerteContainer.add(componentAlerteCondensation);
        alerteContainer.add(componentAlerteTemperature);

        updateWriteContainer.add(alerteContainer);

        componentUpdateState.add(updateWriteContainer);
        componentUpdateState.add(componentGraphics);


        c.add(stats, BorderLayout.EAST);
        c.add(componentUpdateState, BorderLayout.CENTER);

        setDefaultCloseOperation(3);
    }


    @Override
    public void update(Observable o, Object arg) {
        componentConsigne.setText("Consigne : " + controler.getConsigneTemperature() + " �C");
        float valeurTemperature = controler.getPeltierTemperature();
        alerteContainer.add(componentAlerteTemperature);


        if (controler.getHeatError()) {
            alerteContainer.add(componentAlerteTemperature);
        } else {
            alerteContainer.remove(componentAlerteTemperature);
        }

        if (controler.getRoseError()) {
            alerteContainer.add(componentAlerteCondensation);
        } else {
            alerteContainer.remove(componentAlerteCondensation);
        }


        componentPeltierTemperature.setText("Temperature Externe : " + controler.getPeltierTemperature() + " �C");
        componentInternalTemperature.setText("Temperature Interne : " + controler.getIntTemperature() + " �C");
        componentHygrometry.setText("Humidite :" + controler.getHygro());
        componentRose.setText("Point de Rosee :" + controler.getRose() + " �C");
        
    }
    

    
    
}
