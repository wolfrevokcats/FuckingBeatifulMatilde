package be.uclouvain.lingi2252.groupN.equipment;

public abstract class TemperatureControl implements Equipment {
    protected Double minTemp;
    protected Double maxTemp;

    public abstract void giveTemperature(Double temperature);

    public Double getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(Double minTemp) {
        this.minTemp = minTemp;
    }

    public Double getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(Double maxTemp) {
        this.maxTemp = maxTemp;
    }
}
