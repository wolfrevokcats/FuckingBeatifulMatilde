package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

public class Fireplaces extends TemperatureControl {
    public Fireplaces(Room owner) {
        super(owner);
        this.minTemp = 19.0;
        this.maxTemp = 20.0;
    }
}
