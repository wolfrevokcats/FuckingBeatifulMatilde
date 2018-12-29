package be.uclouvain.lingi2252.groupN.actuators;

import be.uclouvain.lingi2252.groupN.Room;

public abstract class Actuator {
    protected Room owner;
    private Boolean status;

    protected Actuator(Room owner) {
        this.owner = owner;
        this.status = false;
    }

    public Boolean checkStatus() {
        return status;
    }

    public abstract void set(Boolean status);

    protected void setStatus(Boolean status) {
        if (status != this.status) {
            this.status = status;
            System.out.print(this.getClass().getSimpleName());
            if (this instanceof Lockable) {
                System.out.print(" " + (status ? "opened" : "closed"));
            } else if (this instanceof Switchable) {
                System.out.print(" turned " + (status ? "on" : "off"));
            } else {
                throw new IllegalStateException(this.getClass().getSimpleName() + " status could not be changed!");
            }
            System.out.println(" in [" + owner.getName() + "]");
        }
    }

}
