package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

import java.util.Optional;

public class Blinds implements Equipment {
    private Boolean status;
    private Room owner;

    public Blinds(Room owner) {
        this.owner = owner;
        status = false;
    }

    public Blinds(Room owner, Boolean status) {
        this.owner = owner;
        this.status = status;
    }

    @Override
    public Boolean checkStatus() {
        return null;
    }

    @Override
    public void schedule(Boolean status, Double from, Double to) {

    }

    @Override
    public void set(Boolean status) {
        this.status = status;
        if (status)
            System.out.println("Blinds turned on in [" + owner.getName() + "]");
        else
            System.out.println("Blinds turned off in [" + owner.getName() + "]");
    }

    @Override
    public void setAuto(Optional<Double> time) {

    }
}
