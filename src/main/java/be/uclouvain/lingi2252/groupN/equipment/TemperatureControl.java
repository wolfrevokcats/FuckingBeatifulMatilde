package be.uclouvain.lingi2252.groupN.equipment;

public abstract class TemperatureControl implements Equipment {
    protected Double minTemp;
    protected Double maxTemp;

    public abstract void giveTemperature(Double temperature);
}
