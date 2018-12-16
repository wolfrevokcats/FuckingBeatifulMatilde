package be.uclouvain.lingi2252.groupN.actuators;

import be.uclouvain.lingi2252.groupN.Room;

import java.util.Observable;

public class Conditioners extends TemperatureControl {
    public Conditioners(Room owner) {
        super(owner);
        this.minTemp = 25.0;
        this.maxTemp = 26.0;
    }

    @Override
    public void update(Observable o, Object arg) {
        lastTemp = (Double) arg;
        if (lastTemp > maxTemp) set(true);
        else if (lastTemp < minTemp) set(false);
    }
}
