package Model;

public class FridgeModel extends AbstractModel 
{
    private static FridgeModel instance; //creation d'une instance de fridgemodel

    public static FridgeModel getInstance() //recuperation de l'instance
    {
        if(null==instance)
        {
            instance = new FridgeModel();
        }
        return instance;
    }

    @Override
    public float getConsigneTemperature() //accesseurs sur la temperature de consigne 
    { 
    	return this.consigneTemperature; 
    }
    @Override
    public void setConsigneTemperature(float consigneTemperature) 
    {
        this.consigneTemperature = consigneTemperature;
        setChanged();
        notifyObservers();
    }

    @Override
    public float getInternalTemperature()  //accesseurs sur la temperature Interne
    {
    	return this.internalTemperature; 
    }
    @Override
    public void setInternalTemperature(float internalTemperature) 
    {
        this.internalTemperature = internalTemperature;
        setChanged();
        notifyObservers();
    }

    @Override
    public float getPeltierTemperature()  // accesseurs sur la temperature du module peltier
    {
    return this.peltierTemperature; 
    }
    @Override
    public void setPeltierTemperature(float peltierTemperature) 
    {
        this.peltierTemperature = peltierTemperature;
        setChanged();
        notifyObservers();
    }

    @Override
    public float getHygrometry() //accesseurs sur la valeur d'humidité
    {
    return this.hygrometry;
    }
    @Override
    public void setHygrometry(float hygrometry) 
    {
        this.hygrometry = hygrometry;
        setChanged();
        notifyObservers();
    }

    @Override
    public boolean getStateDoor() //accesseurs du booleen sur l'etat de la porte(utilisable sur une porte automatique)
    {
    return stateDoor;
    }
    @Override
    public void setStateDoor(boolean stateDoor) 
    {
        this.stateDoor = stateDoor;
        setChanged();
        notifyObservers();
    }

    @Override
    public float getValeur_rose()  //accesseurs sur le point de rosée
    {
        return valeur_rose;
    }
    @Override
    public void setValeur_rose(float valeur_rose)
    {
        this.valeur_rose = valeur_rose;
    }

    @Override
    public boolean getStateRunning() //accesseurs sur le booleen de l'etat de fonctionnement
    {
        return this.stateRunning;
    }
    @Override
    public void setStateRunning(boolean stateRunning)
    {
        this.stateRunning = stateRunning;
    }
    @Override
    public boolean getHeatError() //accesseurs sur le booleen d'erreur de chaleur
    {
        return heatError;
    }

    @Override
    public void setHeatError(boolean heatError)
    {
        this.heatError = heatError;
    }

    @Override
    public boolean getRoseError() //accesseurs sur le booleen de l'erreur de point de rosée
    {
        return roseError;
    }

    @Override
    public void setRoseError(boolean roseError)
    {
        this.roseError = roseError;
    }
}