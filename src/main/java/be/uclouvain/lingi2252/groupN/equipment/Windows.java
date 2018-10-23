package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

import java.util.Optional;

public class Windows implements Equipment {
    private Boolean status;
    private Room owner;

    public Windows(Room owner) {
        this.owner = owner;
        status = false;
    }

    public Windows(Room owner, Boolean status) {
        this.owner = owner;
        this.status = status;
    }

    @Override
    public Boolean checkStatus() {
        return this.status;
    }

    @Override
    public void schedule(Boolean status, Double from, Double to) {

    }

    @Override
    public void set(Boolean status) {
        this.status = status;
        if (status)
            System.out.println("All windows opened in [" + owner.getName() + "]");
        else
            System.out.println("All windows closed in [" + owner.getName() + "]");

    }

    @Override
    public void setAuto(Optional<Double> time) {

    }
}
