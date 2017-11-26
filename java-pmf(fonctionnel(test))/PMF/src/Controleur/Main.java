package Controleur;
import Model.AbstractModel;
import Model.FridgeModel;
import Vue.FridgeView;
import java.util.concurrent.TimeUnit;


public class Main {
    public static void main(String args[]) {
        FridgeControler controler = new FridgeControler(); //on cree un nouvel objet controlleur qui gerera les actions
        controler.initialize(); //on initialise le controleur
        
    
        
        
        float consigne = controler.getConsigneTemperature();//on recupere la temperature de consigne  
        float temperatureint =  controler.getIntTemperature(); //on recupere la temperature interne
       
       
        while(consigne >= 0) //on compare dans la boucle la consigne a la temperature interne et l'on eteint le module quand elle lui est inferieure
        {
        	
        	for(int i=0 ; i >2; i++ ) 
			{
				
			
			if(consigne > temperatureint)
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

