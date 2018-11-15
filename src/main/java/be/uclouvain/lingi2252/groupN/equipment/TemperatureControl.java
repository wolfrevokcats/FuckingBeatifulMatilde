package be.uclouvain.lingi2252.groupN.equipment;

public abstract class TemperatureControl implements Equipment {
    protected Double minTemp;
    protected Double maxTemp;

    public abstract void giveTemperature(Double temperature);

    public Double getMinTemp() {
        return minTemp;
    }

    public void setTargetTemp(Double minTemp, Double maxTemp) {
        if (minTemp.equals(maxTemp)) {
            this.minTemp += minTemp;
            this.maxTemp += maxTemp;
        } else {
            this.minTemp = minTemp;
            this.maxTemp = maxTemp;
        }
    }

    public Double getMaxTemp() {
        return maxTemp;
    }
}
