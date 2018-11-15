package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

public class Cookers extends Equipment {
    public Cookers(Room owner) {
        super(owner);
    }

    @Override
    public void schedule(Boolean status, Double from, Double to) {

    }

    @Override
    public void set(Boolean status) {
        setStatus("turn on/off", status);
    }
}
