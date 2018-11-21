package be.uclouvain.lingi2252.groupN.equipment;

import be.uclouvain.lingi2252.groupN.Room;

public abstract class TemperatureControl extends Switchable {
    protected Double minTemp;
    protected Double maxTemp;
    protected Double lastTemp;

    protected TemperatureControl(Room owner) {
        super(owner);
    }

    public void giveTemperature(Double temperature) {
        lastTemp = temperature;
        if (temperature > maxTemp) set(false);
        else if (temperature < minTemp) set(true);
    }

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

        if (lastTemp != null) giveTemperature(lastTemp);
    }

    public Double getMaxTemp() {
        return maxTemp;
    }
}
