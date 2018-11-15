package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

public class Heaters extends TemperatureControl {
    private boolean status;
    private Room owner;

    public Heaters(Room owner) {
        this.owner = owner;
        this.status = false;
        this.minTemp = 19.0;
        this.maxTemp = 20.0;
    }

    @Override
    public void set(Boolean status) {
        if (status != this.status)
            System.out.println("Heaters turned " + (status ? "on" : "off") + " in [" + owner.getName() + "]");
    }

    @Override
    public void schedule(Boolean status, Double from, Double to) {

    }

    @Override
    public Boolean checkStatus() {
        return status;
    }

    @Override
    public void giveTemperature(Double temperature) {
        if (temperature > maxTemp) set(false);
        else if (temperature < minTemp) set(true);
    }
}
