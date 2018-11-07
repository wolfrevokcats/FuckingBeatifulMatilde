package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

import java.util.Optional;

public class Cookers implements Equipment {
    private Boolean status;
    private Room owner;

    public Cookers(Room owner) {
        this.owner = owner;
        status = false;
    }

    public Cookers(Room owner, Boolean status) {
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
        System.out.println("Cookers turned " + (status ? "on" : "off") + " in [" + owner.getName() + "]");
    }

    @Override
    public void setAuto(Optional<Double> time) {

    }
}
