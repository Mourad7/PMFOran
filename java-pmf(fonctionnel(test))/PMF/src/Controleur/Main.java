package Controleur;
import Model.AbstractModel;
import Model.FridgeModel;
import Vue.FridgeView;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String args[]) {
        FridgeControler controler = new FridgeControler(); //on cree un nouvel objet controlleur qui gerera les actions
        controler.initialize(); //on initialise le controleur
        
    
        
        
        float consigne = controler.getConsigneTemperature();
        float tmpinte = controler.getIntTemperature();
       
       
        while(consigne >= 0)
        {
        	
        	for(int i=0 ; i >2; i++ )
			{
				
			
			if(consigne > tmpinte)
			{
				controler.on_off("1");
			}
			else
			{
				controler.on_off("2");
			}
			}
        
        }
      


        
        
    }
}
