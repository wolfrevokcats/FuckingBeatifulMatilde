package be.uclouvain.lingi2252.groupN.actuators;

import be.uclouvain.lingi2252.groupN.Room;

public class Heaters extends TemperatureControl {
    public Heaters(Room owner) {
        super(owner);
        this.minTemp = 19.0;
        this.maxTemp = 20.0;
    }
}
