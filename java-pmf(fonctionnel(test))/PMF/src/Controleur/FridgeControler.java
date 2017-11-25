package Controleur;

import Model.FridgeModel;
import Vue.FridgeView;
import gnu.io.CommPortIdentifier;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;


public class FridgeControler extends AbstractControler implements ActionListener, KeyListener, SerialPortEventListener  
{

    private FridgeModel model; //creation de l'objet model
    private FridgeView view; //creation de l'objet vue
    
   

    private SerialPort serialPort; //creation de l'objet de port serie

    private static final String PORT_NAMES[] =  //stockage des noms de ports utilisées
    	{
            "COM3",
        };

    private BufferedReader input; //creation du bufferedreader pour la lecture de données
    private OutputStream output; //creation de l'objet qui permettera l'envoi de données vers l'arduino
    private static final int TIME_OUT = 2000; //initialisation du temps maximum d'attente
    private static final int DATA_RATE = 9600; //initialisation du debit de données


    public FridgeControler() //constructeur
    { 
        model = FridgeModel.getInstance();
        view = new FridgeView(this);
        model.addObserver(view);

        view.validate.addActionListener(this);
        view.componentUpdateTemperature.addKeyListener(this);
        view.setVisible(true);
    }

    @Override //creation de la consigne pour l'ouverture de la porte (l'ouverture automatique n'etant pas possible sur notre frigo)
    public void consigneDoor() 
    {
    	
    }

    @Override
    public void toggleDoor()
    {
    	
    }

    public float getIntTemperature() //accesseur sur la temperature interne
    {
    	return model.getInternalTemperature();
    }
    public boolean getHeatError() //accesseur sur l'erreur de temperature pour creer une alerte
    {
    	return model.getHeatError();
    }
    public boolean getRoseError() //accesseur sur l'erreur de point de rosée afin de creer une alerte
    {
    	return model.getRoseError();
    }
    public float getPeltierTemperature() //accesseur sur la temperature du module a effet peltier
    {
    	return model.getPeltierTemperature();
    }
    public float getConsigneTemperature() //accesseur sur la temperature de consigne
    {
    	return  model.getConsigneTemperature();
    }
    public float getHygro() //accesseur sur le taux d'humidite
    {
    	return  model.getHygrometry();
    }
    public float getRose() //accesseur sur la valeur du point de rosée
    {
    	return  model.getValeur_rose();
    }
    
 



    @Override
    public void actionPerformed(ActionEvent e) //fonction gerant la validation de la consigne
    {
        if (e.getSource() == view.validate)
        {
          String test = view.componentUpdateTemperature.getText();
          if (test.equals(""))
          {
                JOptionPane d = new JOptionPane();
                d.showMessageDialog(view.container, "Il n'y a pas de consigne",
                        "Erreur", JOptionPane.WARNING_MESSAGE);    //message d'erreur si une valeur nulle est entrée
          }
          else 
          {
            if (estUnNombre(test))
            {
                    float nouvelleConsigne = Float.parseFloat(test);
                    model.setConsigneTemperature(nouvelleConsigne);
            }  //nouvelle consigne validée si la valeur est un nombre
            else
            {
                    JOptionPane d = new JOptionPane();
                    d.showMessageDialog(view.container, "Ceci n'est pas un nombre",
                            "Erreur", JOptionPane.WARNING_MESSAGE); //message d'erreur si la valeur entré n'est pas un nombre( des characteres par exemple)
            }
           }
        }
    }

    @Override
    public void keyTyped(KeyEvent e)  //methode inutilisé du KeyListener
    {

    }

    @Override
    public void keyPressed(KeyEvent e) // on verifie la consigne lorsque la touche entrée est presse
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) 
        {
            String test = view.componentUpdateTemperature.getText();
            if (test.equals(""))
            {
                JOptionPane d = new JOptionPane();
                d.showMessageDialog(view.container, "Il n'y a pas de consigne",
                        "Erreur", JOptionPane.WARNING_MESSAGE);
            }
            else
            {
                if (estUnNombre(test))
                {
                    float nouvelleConsigne = Float.parseFloat(test);
                    model.setConsigneTemperature(nouvelleConsigne);
                } 
                else 
                {
                    JOptionPane d = new JOptionPane();
                    d.showMessageDialog(view.container, "Ceci n'est pas un nombre",
                            "Erreur", JOptionPane.WARNING_MESSAGE);

                }
            }
        }

    }

    @Override
    public void keyReleased(KeyEvent e) //fonction unitilisé du KeyListener
    {

    }




    public boolean estUnNombre(String chaine) //booleen de verification si la consigne est un nombre, si c'est un nombre on retourne un true
    //sinon on retourne un false
    {
        try
        {
            Float.parseFloat(chaine); 
        } 
        catch (NumberFormatException e)
        {
            return false;
        }

        return true;
    }



    public void initialize()//fonction d'initialisation du controleur, appelé dans le main
    {
        CommPortIdentifier portId = null; //on initialise un identifiant de port serie
        Enumeration portEnum = CommPortIdentifier.getPortIdentifiers(); //on cree une enumeration avec les ports series disponibles

        //dabord on trouve une instance d'un port serie contenu dans PORT_NAMES
        while (portEnum.hasMoreElements()) 
        {
            CommPortIdentifier currPortId = (CommPortIdentifier) portEnum.nextElement();
            for (String portName : PORT_NAMES) 
            {
                if (currPortId.getName().equals(portName)) 
                {
                    portId = currPortId;
                    break;
                }
            }
        }

        if (portId == null) 
        {
            System.out.println("Could not find COM port.");
            return;
        }
           //si on ne trouve pas de port serie disponible on retourne un message d'erreur
        try
        {
            
        	//on ouvre le port serie
            serialPort = (SerialPort) portId.open(this.getClass().getName(),
                    TIME_OUT);

            // on met en place les paramettres des ports
            serialPort.setSerialPortParams(DATA_RATE,
                    SerialPort.DATABITS_8,
                    SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

            // on ouvre l'envoie et reception de données
            input = new BufferedReader(new InputStreamReader(serialPort.getInputStream()));
            output = serialPort.getOutputStream();

           
            //on ajoute des EventListener
            serialPort.addEventListener( this);
            serialPort.notifyOnDataAvailable(true);
        } 
        catch (Exception e) 
        {
            System.err.println(e.toString());
        }
    }

    
    
    
    public synchronized void close() // methode appelle pour fermer les ports series apres la fin de leur utilisation
    {
        if (serialPort != null)
        {
            serialPort.removeEventListener();
            serialPort.close();
        }
    }

    
     //gere les evenements au niveau du port serie, lis les données et les affiche
    public synchronized void serialEvent(SerialPortEvent oEvent)
    {
        System.out.println("seriEvent");

        if (oEvent.getEventType() == SerialPortEvent.DATA_AVAILABLE)
        {
            try 
            {
                String inputLine=input.readLine();
                System.out.println(inputLine);
                List<String> items = Arrays.asList(inputLine.split("\\s*,\\s*"));


                float hygo = Float.valueOf(items.get(0));
                float tempExt = Float.valueOf(items.get(1));
                float tempInt = Float.valueOf(items.get(2));
                float rose = Float.valueOf(items.get(3));



                if(tempInt - model.getPeltierTemperature() > 1) //comparaison des temperature afin de definir les erreur limites de chaleur
                {
                    model.setHeatError(true);
                }
                else
                {
                    model.setHeatError(false);
                }

                //Si temperature interne depasse le point de rosee, on alerte l'utilisateur
                if(rose > model.getPeltierTemperature())
                	
                {
                    model.setRoseError(true);
                }
                else
                {
                    model.setRoseError(false);
                }

                 
                //actualisation des données des temperatures et humidité suivant les données recus 
                model.setHygrometry(hygo); 
                model.setInternalTemperature(tempExt);
                model.setPeltierTemperature(tempInt);
                model.setValeur_rose(rose);
                
              
                if(model.getConsigneTemperature() < tempInt) //comparaison entre la consigne et la temperature lue pour appeler la fonction on_off
                {
                    if(!model.getStateRunning())
                    {
                        on_off("1");
                    }
                }
                else
                {
                    if(model.getStateRunning())
                    {
                        on_off("2");
                    }
                }
            
            } 
            catch (Exception e) 
            {
                System.err.println(e.toString());
            }
        }
        
    }

    
     //  on_off: fonction qui allume ou eteint le module peltier
     
    
    String retoursig;
	
    
    public String getRetourSig() //accesseur sur la valeur de retour de signal
    {
    	return retoursig;
    }
    public String setRetourSig(String sig) //accesseur sur la valeur de retour de signal
    {
    	return sig = retoursig;
    }
    
    public synchronized void on_off(String signal)
    {
        try
        {
            if(signal == "1")
            {
            	
                model.setStateRunning(true);
                System.out.println("on");
                setRetourSig("1");
            }
        
            else
            {
            	 model.setStateRunning(false);
                 System.out.println("off");
                 setRetourSig("2");
            }
            String serialMessage = signal;
            output.write(serialMessage.getBytes());
        }
        catch (Exception e)
        {
            System.err.println(e.toString());
        }


    }
    
  
  /*  Thread Thread = new Thread()
    		{
    	     @Override public void run()
    	     {
    	    	try {
					Thread.sleep(1000);
				} catch (InterruptedException e1) {
					
					e1.printStackTrace();
				}
    	    	
    	    	PrintWriter toarduino = new PrintWriter(output);
    	    	while(true)
    	    	{
    	    		toarduino.print(model.getStateRunning());
    	    		toarduino.flush();
    	    		try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
					
						e.printStackTrace();
					} 
    	    		
    	    	}
    	     }
    	     
    		};
*/

}