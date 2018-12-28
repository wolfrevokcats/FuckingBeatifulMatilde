package be.uclouvain.lingi2252.groupN.actuators;

import be.uclouvain.lingi2252.groupN.Room;

public abstract class Lockable extends Actuator {
    private boolean locked;

    protected Lockable(Room owner) {
        super(owner);
    }

    @Override
    public void set(Boolean status) {
        this.setStatus(status);
    }

    public void setLocked(boolean locked) {
        if (locked != this.locked) {
            this.locked = locked;
            System.out.println(this.getClass().getSimpleName() + " " + (locked ? "locked" : "unlocked") + " in [" + owner.getName() + "]");
        }
    }
}
