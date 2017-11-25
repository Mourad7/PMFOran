package Controleur;
import Model.AbstractModel;

import java.awt.event.ActionEvent; 
import java.util.Observable; //importation de la librairie gerant le design pattern observeur


public abstract class AbstractControler extends Observable
{
	protected AbstractModel model;
 
	public AbstractControler() {}

	public abstract void consigneDoor();

	public abstract void toggleDoor();

	//public abstract void updateTemperature();
}