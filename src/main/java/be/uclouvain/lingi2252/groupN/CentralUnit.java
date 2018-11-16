package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.equipment.Conditioners;
import be.uclouvain.lingi2252.groupN.equipment.Fireplaces;
import be.uclouvain.lingi2252.groupN.equipment.Heaters;
import be.uclouvain.lingi2252.groupN.equipment.TemperatureControl;
import be.uclouvain.lingi2252.groupN.signals.Frame;

import java.util.*;

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
                .map(Room::getEquipmentList)
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
                .map(Room::getEquipmentList)
                .flatMap(Collection::stream)
                .filter(equipment -> equipment instanceof Conditioners)
                .forEach(equipment -> ((TemperatureControl) equipment).setTargetTemp(lowTemp, highTemp));
    }

    public void setBlindsSchedule(Double fromHour, Double toHour) {

    }

    public String findObject(String[] objects) {
        Map<Room, List<Frame>> possibleMatches = new HashMap<>();
        System.out.println("Object Tracking procedure triggered to find " + Arrays.toString(objects));

        hubs.stream()
                .map(CommunicationHub::getOwner)
                .forEach(room -> possibleMatches.put(room, room.findObject(objects)));

        StringBuilder res = new StringBuilder().append("Possible matches for ").append(Arrays.toString(objects)).append(":\n");
        boolean found = false;

        for (Room room : possibleMatches.keySet()) {
            for (Frame frame : possibleMatches.get(room)) {
                res.append(frame.extract()).append(" in [").append(room.getName()).append("]");
                found = true;
            }
        }

        if (!found) res.append("No match found.");

        return res.toString();
    }
}
