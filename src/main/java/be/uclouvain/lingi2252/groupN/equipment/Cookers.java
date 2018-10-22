package be.uclouvain.lingi2252.groupN.equipment;

import java.util.Optional;

public class Cookers implements Equipment {
    private Boolean status;

    public Cookers() {
        status = false;
    }

    public Cookers(Boolean status) {
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

    }

    @Override
    public void setAuto(Optional<Double> time) {

    }
}
