package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

public class Conditioners extends TemperatureControl {
    private boolean status;
    private Room owner;

    public Conditioners(Room owner) {
        this.owner = owner;
        this.status = false;
        this.minTemp = 25.0;
        this.maxTemp = 26.0;
    }

    @Override
    public void set(Boolean status) {
        if (status != this.status)
            System.out.println("Conditioners turned " + (status ? "on" : "off") + " in [" + owner.getName() + "].");
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
        if (temperature > maxTemp) set(true);
        else if (temperature < minTemp) set(false);
    }
}
