package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

public abstract class Lockable extends Equipment {
    private boolean locked;

    protected Lockable(Room owner) {
        super(owner);
    }

    @Override
    public void set(Boolean status) {
        setStatus("open/close", status);
    }

    public void setLocked(boolean locked) {
        if (locked != this.locked) {
            this.locked = locked;
            System.out.println(this.getClass().getSimpleName() + " " + (locked ? "locked" : "unlocked") + " in [" + owner.getName() + "]");
        }
    }
}
