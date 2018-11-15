package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

public class Conditioners extends TemperatureControl {
    public Conditioners(Room owner) {
        super(owner);
        this.minTemp = 25.0;
        this.maxTemp = 26.0;
    }

    @Override
    public void set(Boolean status) {
        setStatus("turn on/off", status);
    }

    @Override
    public void schedule(Boolean status, Double from, Double to) {

    }

    @Override
    public void giveTemperature(Double temperature) {
        if (temperature > maxTemp) set(true);
        else if (temperature < minTemp) set(false);
    }
}
