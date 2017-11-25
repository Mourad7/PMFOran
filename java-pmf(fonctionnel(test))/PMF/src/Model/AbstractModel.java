package Model;
import java.util.Observable;

public abstract class AbstractModel extends Observable
{

	protected float consigneTemperature; //valeurConsigne
	protected float peltierTemperature; //valeurTemperaturePeltier
	protected float internalTemperature; //valeurTemperatureInterne
	protected float hygrometry; // valeurHygrometrie
	protected boolean stateDoor; //booleen sur l'ouverture de porte
	protected float valeur_rose; //valeur de de la rosee
	protected boolean stateRunning; //booleen sur l'etat de fonctionnement
	protected boolean heatError; //booleen sur l'erreur de chaleur pour le message d'alerte
	protected boolean roseError; // booleen sur l'erreur de rosée pour le message d'alerte  

	/* 
	 * accesseurs sur la consigne de temperature
	 */
	public abstract float getConsigneTemperature(); 
	public abstract void setConsigneTemperature(float consigneTemperature);
	/* 
	 * accesseurs sur la temperature interne
	 */
	public abstract float getInternalTemperature();
	public abstract void setInternalTemperature(float internalTemperature);
	/* 
	 * accesseurs sur la temperature du module peltier
	 */
	public abstract float getPeltierTemperature();
	public abstract void setPeltierTemperature(float peltierTemperature);
	/* 
	 * accesseurs sur les valeurs d'humidite
	 */
	public abstract float  getHygrometry();
	public abstract void setHygrometry(float hygrometry);
	/* 
	 * accesseurs sur l'etat de la porte
	 */
	public abstract boolean getStateDoor();
	public abstract void setStateDoor(boolean stateDoor);
	/* 
	 * accesseurs sur valuer de rosee
	 */
	public abstract float getValeur_rose();
	public abstract void setValeur_rose(float stateDoor);
	/* 
	 * accesseurs sur l'etat de fonctionnement
	 */
	public abstract boolean getStateRunning();
	public abstract void setStateRunning(boolean stateRunning);
	/* 
	 * accesseurs sur la marge d'erreur de challeur
	 */
	public abstract boolean getHeatError();
	public abstract void setHeatError(boolean heatError);
	/* 
	 * accesseurs sur la marge d'erreur de rosee
	 */
	public abstract boolean getRoseError();
	public abstract void setRoseError(boolean roseError);
}