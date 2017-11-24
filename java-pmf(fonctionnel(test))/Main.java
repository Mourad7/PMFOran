package Controleur;
import Model.AbstractModel;
import Model.FridgeModel;
import Vue.FridgeView;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String args[]) {
        FridgeControler controler = new FridgeControler(); //on cree un nouvel objet controlleur qui gerera les actions
        controler.initialize(); //on initialise le controleur
        
        /* 
        float consigne = controler.getConsigneTemperature();
        float tmpinte = controler.getIntTemperature();
       
       
        while(true)
        {
        	
      
        for(int i=0 ; i >2; i++ )
        {
        	
        
        if(consigne > tmpinte)
        {
        	System.out.println("fonctionnement normal");
        }
        else
        {
        	System.out.println("STOP");
        }
      
        }
        try {
			TimeUnit.SECONDS.sleep(1);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
        
        }*/
      


        
        
    }
}

