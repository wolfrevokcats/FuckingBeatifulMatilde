package be.uclouvain.lingi2252.groupN.equipment;

public class Fireplaces extends TemperatureControl {
    boolean status;

    @Override
    public void set(Boolean status) {

    }

    @Override
    public void schedule(Boolean status, Double from, Double to) {

    }

    @Override
    public Boolean checkStatus() {
        return status;
    }

    @Override
    public void giveTemperature(Double temperature) {

    }
}
