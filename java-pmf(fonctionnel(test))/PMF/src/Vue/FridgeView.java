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

public class FridgeView extends JFrame implements Observer //utilisation du design pattern observer
{

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

    public Container container; //creation du container des données

    public FridgeView(FridgeControler controler) 
    {
        setTitle("Centre de controle du frigo"); //titre de la fenetre de l'application
        setSize(1200, 600); //taille de la fenetre
        this.controler = controler;


         
        Container c = getContentPane();
        c.setLayout(new BorderLayout()); //on utilise un borderlayout pour disposer les données

        stats = new Panel(new GridLayout(6, 0)); //panel regroupant les statistiques
        componentUpdateState = new Panel(new GridLayout(2, 0));
        componentUpdateWrite = new Panel();
        alerteContainer = new Panel(); 
        updateWriteContainer = new Panel(new GridLayout(2, 0));
        componentGraphics = new Panel();

        componentStatTitle = new Label("Etat du systeme"); //titre du panel des stats
        gras = new Font("Courrier", Font.BOLD, 20); //parametrage de la police
        componentStatTitle.setFont(gras);
        stats.setBackground(Color.lightGray);//parametrage de la couleur de fond du panel des stats


        componentAlerteTemperature = new Label("Attention, la temperature du frigo augmente rapidement !"); //message d'alerte de trop haute temperature
        componentAlerteTemperature.setForeground(new Color(255, 0, 0));
        componentAlerteCondensation = new Label("Attention, de la condensation peut se former ! veuillez ouvrir la porte"); //message d'alerte pour la condensation
        componentAlerteCondensation.setForeground(new Color(255, 0, 0));

        //graphe des temperatures
        XYSeries externe = new XYSeries("Temperatures Externes");
        XYSeries interne = new XYSeries("Temperature Interne");
        XYSeries consigne = new XYSeries("Temperature de Consigne");
        XYSeries pointrose = new XYSeries("Temperature Point de rosée");
        XYSeriesCollection dataset = new XYSeriesCollection(externe); // on ajoute toutes les données de temperatures au meme graph 
        dataset.addSeries(interne);
        dataset.addSeries(consigne);
        dataset.addSeries(pointrose);
        
        
        
        JFreeChart chart = ChartFactory.createXYLineChart("Evolution temperature", "Temps (secondes)", "Degre  (°C)", dataset); //on cree le graphique de temperature
        componentGraphics.add(new ChartPanel(chart)); //on affiche le graphique de temperature


      //graphe d'humidité
        XYSeries humidite = new XYSeries("Humidite");
        XYSeriesCollection datahum = new XYSeriesCollection(humidite);      
        JFreeChart charthum = ChartFactory.createXYLineChart("Niveau humudité", "Temps(Secondes)","% humidite", datahum);
        componentGraphics.add(new ChartPanel(charthum));
     
        
            Thread thread = new Thread(() -> //thread qui gere l'actualisation des graphes avec les données du port serie
            { 
                // Draw Graph
                while (true)
                {
                    try
                    {
                            externe.add(x++,controler.getPeltierTemperature() ); // recupere la temperature du module peltier //controler.getPeltierTemperature() (a mettre a la place de la donnée de simulation)
                            interne.add(x++, controler.getIntTemperature()); //recupere la temperature interne //controler.getIntTemperature() (a mettre a la place de la donnée de simulation)
                            consigne.add(x++, controler.getConsigneTemperature());//recupere la temperature de consigne//controler.getConsigneTemperature() (a mettre a la place de la donnée de simulation)
                            pointrose.add(x++, controler.getRose());//recupere la temperature calculé du point de rosée//controler.getRose() (a mettre a la place de la donnée de simulation)
                            humidite.add(x++, controler.getHygro());//recupere le niveau d'humidité
                            componentGraphics.repaint(); //reactualisation du grahphe avec les nouvelles données
                            Thread.sleep(1000);//actualisation chaque seconde
                    } 
                    catch (Exception e)
                    {
                    }
                }
            });
            thread.start(); //on lance le thread de gestion des graphes

         
        //affichage des données recupere dans le panel des stats
        componentInternalTemperature = new Label("Temperature Interne : " + controler.getIntTemperature() + " °C"); //affichage de la temperature interne
        componentPeltierTemperature = new Label("Temperature module Peltier : " + controler.getPeltierTemperature() + " °C"); //affichage de la temperature du module peltier
        componentConsigne = new Label("Consigne : " + controler.getConsigneTemperature() + " °C");//affichage de la consigne
        componentHygrometry = new Label("Humidite :" + controler.getHygro() + "%"); //affichage de l'humidite
        componentRose = new Label("Point de Rosee :" + controler.getRose() + " °C");//affichage du point de rosée
        componentUpdateTemperature = new TextField(10); //le champ pour ecrire la valeur de consigne
        proposition = new Label("veuillez entrer la valeur de consigne : "); // affichage du message pour entrer la consigne


        validate = new Button("Valider");//bouton de validation de la consigne
        
        
        //on ajoute les messages d'affichage au panel des stats
        stats.add(componentStatTitle);
        stats.add(componentPeltierTemperature);
        stats.add(componentInternalTemperature);
        stats.add(componentHygrometry);
        stats.add(componentConsigne);
        stats.add(componentRose);
        
        //on ajoute les composant necessaire pour entrer la consigne a son panel
        componentUpdateWrite.add(proposition);
        componentUpdateWrite.add(componentUpdateTemperature);
        componentConsigne.setPreferredSize(new Dimension(50, 24));
        componentUpdateWrite.add(validate);

        
        updateWriteContainer.add(componentUpdateWrite);

        //on ajoute les message d'alerte
        alerteContainer.add(componentAlerteCondensation);
        alerteContainer.add(componentAlerteTemperature);

        updateWriteContainer.add(alerteContainer);
        //on ajoute les graphes et valeur de consigne a un composant qui sera par la suite affiche a l'aide du borderlayout(voir plus bas)
        componentUpdateState.add(updateWriteContainer);
        componentUpdateState.add(componentGraphics);

        //on affiche les stats a droite de l'ecran et les graphes au centre
        c.add(stats, BorderLayout.EAST);
        c.add(componentUpdateState, BorderLayout.CENTER);

        setDefaultCloseOperation(3);
        
        

    }


    @Override
    public void update(Observable o, Object arg)  //methode update du design pattern observer qui va recuperer les données actualisé du controleur 
    {
        componentConsigne.setText("Consigne : " + controler.getConsigneTemperature() + " °C");
        float valeurTemperature = controler.getPeltierTemperature();
        alerteContainer.add(componentAlerteTemperature);


        if (controler.getHeatError())
        {
            alerteContainer.add(componentAlerteTemperature);
        }
        else 
        {
            alerteContainer.remove(componentAlerteTemperature);
        }

        if (controler.getRoseError()) 
        {
            alerteContainer.add(componentAlerteCondensation);
        } 
        else
        {
            alerteContainer.remove(componentAlerteCondensation);
        }


        componentPeltierTemperature.setText("Temperature Externe : " + controler.getPeltierTemperature() + " °C");
        componentInternalTemperature.setText("Temperature Interne : " + controler.getIntTemperature() + " °C");
        componentHygrometry.setText("Humidite :" + controler.getHygro());
        componentRose.setText("Point de Rosee :" + controler.getRose() + " °C");
        
    }
    
    
    
}
