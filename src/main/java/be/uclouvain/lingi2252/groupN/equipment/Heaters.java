package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

public class Heaters extends TemperatureControl {
    public Heaters(Room owner) {
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
}
