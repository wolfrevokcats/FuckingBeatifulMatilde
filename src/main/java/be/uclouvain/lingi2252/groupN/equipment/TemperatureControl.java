package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

public abstract class TemperatureControl extends Equipment {
    protected Double minTemp;
    protected Double maxTemp;

    protected TemperatureControl(Room owner) {
        super(owner);
    }

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
