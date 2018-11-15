package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

public class Doors extends Equipment {
    public Doors(Room owner) {
        super(owner);
    }

    @Override
    public void schedule(Boolean status, Double from, Double to) {

    }

    @Override
    public void set(Boolean status) {
        setStatus("open/close", status);
    }
}
