package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

public abstract class Equipment {
    protected Boolean status;
    protected Room owner;

    protected Equipment(Room owner) {
        this.owner = owner;
        this.status = false;
    }

    public Boolean checkStatus() {
        return status;
    }

    public abstract void set(Boolean status);

    protected void setStatus(String type, Boolean status) {
        if (status != this.status) {
            this.status = status;
            System.out.print(this.getClass().getSimpleName());
            switch (type) {
                case "open/close":
                    System.out.print(" " + (status ? "opened" : "closed"));
                    break;

                case "turn on/off":
                    System.out.print(" turned " + (status ? "on" : "off"));
                    break;
            }
            System.out.println(" in [" + owner.getName() + "]");
        }
    }

}
