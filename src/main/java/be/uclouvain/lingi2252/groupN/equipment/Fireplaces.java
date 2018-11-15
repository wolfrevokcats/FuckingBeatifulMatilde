package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

public class Fireplaces extends TemperatureControl {
    public Fireplaces(Room owner) {
        super(owner);
        this.minTemp = 19.0;
        this.maxTemp = 20.0;
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
        if (temperature > maxTemp) set(false);
        else if (temperature < minTemp) set(true);
    }
}
