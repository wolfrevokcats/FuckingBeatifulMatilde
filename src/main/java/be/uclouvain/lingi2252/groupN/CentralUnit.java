package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.actuators.Conditioners;
import be.uclouvain.lingi2252.groupN.actuators.Fireplaces;
import be.uclouvain.lingi2252.groupN.actuators.Heaters;
import be.uclouvain.lingi2252.groupN.actuators.TemperatureControl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CentralUnit {

    // fields
    private static final CentralUnit SINGLE_INSTANCE = new CentralUnit();
    private List<CommunicationHub> hubs;

    //constructor
    private CentralUnit() {
        this.hubs = new ArrayList<>();
    }

    public static CentralUnit getInstance() {
        return SINGLE_INSTANCE;
    }

    public void initialize(List<CommunicationHub> hubs) {
        this.hubs = hubs;
    }


    //methods

    /**
     * Sets the maximum and minimum temperature for all the heaters and fireplaces in the house
     *
     * @param lowTemp  : if the temperature goes below this one, the heaters will turn on
     * @param highTemp : if the temperature goes above this one, the heaters will turn off
     *                 If lowTemp and highTemp are equal, the temperature will be increased of lowTemp degrees
     */
    public void setHeatingTempRules(Double lowTemp, Double highTemp) {
        hubs.stream()
                .map(CommunicationHub::getOwner)
                .map(Room::getActuatorList)
                .flatMap(Collection::stream)
                .filter(equipment -> equipment instanceof Heaters || equipment instanceof Fireplaces)
                .forEach(equipment -> ((TemperatureControl) equipment).setTargetTemp(lowTemp, highTemp));
    }

    /**
     * Sets the maximum and minimum temperature for all the conditioners in the house
     *
     * @param lowTemp  : if the temperature goes below this one, the conditioners will turn off
     * @param highTemp : if the temperature goes above this one, the conditioners will turn on
     *                 If lowTemp and highTemp are equal, the temperature will be increased of lowTemp degrees
     */
    public void setCoolingTempRules(Double lowTemp, Double highTemp) {
        hubs.stream()
                .map(CommunicationHub::getOwner)
                .map(Room::getActuatorList)
                .flatMap(Collection::stream)
                .filter(equipment -> equipment instanceof Conditioners)
                .forEach(equipment -> ((TemperatureControl) equipment).setTargetTemp(lowTemp, highTemp));
    }
}
