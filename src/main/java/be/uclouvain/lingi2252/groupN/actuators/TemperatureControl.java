package be.uclouvain.lingi2252.groupN.actuators;

import be.uclouvain.lingi2252.groupN.Room;

import java.util.Observable;
import java.util.Observer;

public abstract class TemperatureControl extends Switchable implements Observer {
    protected Double minTemp;
    protected Double maxTemp;
    protected Double lastTemp;

    protected TemperatureControl(Room owner) {
        super(owner);
        this.minTemp = 19.0;
        this.maxTemp = 20.0;
    }

    @Override
    public void update(Observable o, Object arg) {
        lastTemp = (Double) arg;
        if (lastTemp > maxTemp) set(false);
        else if (lastTemp < minTemp) set(true);
    }

    public void setTargetTemp(Double minTemp, Double maxTemp) {
        if (minTemp.equals(maxTemp)) {
            this.minTemp += minTemp;
            this.maxTemp += maxTemp;
        } else {
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
        }

        if (lastTemp != null) update(null, lastTemp);
    }
}
