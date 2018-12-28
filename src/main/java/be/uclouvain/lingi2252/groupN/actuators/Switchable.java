package be.uclouvain.lingi2252.groupN.actuators;

import be.uclouvain.lingi2252.groupN.Room;

public abstract class Switchable extends Actuator {
    public Switchable(Room owner) {
        super(owner);
    }

    @Override
    public void set(Boolean status) {
        this.setStatus(status);
    }
}
