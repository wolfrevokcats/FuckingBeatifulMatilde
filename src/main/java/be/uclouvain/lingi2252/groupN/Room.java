package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.equipment.Cookers;
import be.uclouvain.lingi2252.groupN.equipment.Doors;
import be.uclouvain.lingi2252.groupN.equipment.Equipment;
import be.uclouvain.lingi2252.groupN.equipment.Windows;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;

import java.util.ArrayList;
import java.util.List;


public class Room {

    //Fields
    private String name;
    private List<Sensor> sensors;
    private CommunicationHub commHub;
    private List<Equipment> equipmentList;


    //constructor
    public Room(String name){
        this.name = name;
        this.commHub = new CommunicationHub(this);
        this.sensors = new ArrayList<>();
        this.equipmentList = new ArrayList<>();
    }


    //methods
    public void roomEntered(User user){

    }

    public void addSensor(Sensor sensor){
        sensors.add(sensor);
    }

    public void addSensors(List<Sensor> sensors) {
        this.sensors.addAll(sensors);
    }

    public void removeSensor(Sensor sensor){
        sensors.remove(sensor);
    }

    public void lockdown(){

    }

    public void evacuate() {

    }

    public void addEquipment(Equipment equipment) {
        equipmentList.add(equipment);
    }

    public void addEquipment(List<Equipment> equipmentList) {
        this.equipmentList.addAll(equipmentList);
    }

    public void removeEquipment(Equipment equipment) {
        equipmentList.remove(equipment);
    }

    public String getName() {
        return name;
    }

    public CommunicationHub getCommHub() {
        return commHub;
    }

    public Sensor getSensor(String name) {
        for (Sensor sensor : sensors)
            if (sensor.getName().equals(name))
                return sensor;

        return null;
    }

    public Equipment getEquipment(String name) {
        switch (name) {
            case "doors":
                return equipmentList.stream()
                            .filter(equipment -> equipment instanceof Doors)
                            .findAny()
                            .orElseThrow(() -> new IllegalArgumentException("No such equipment [" + name + "] in this room [" + this.name + "]!"));
            case "windows":
                return equipmentList.stream()
                        .filter(equipment -> equipment instanceof Windows)
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("No such equipment [" + name + "] in this room [" + this.name + "]!"));
            case "cookers":
                return equipmentList.stream()
                        .filter(equipment -> equipment instanceof Cookers)
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("No such equipment [" + name + "] in this room [" + this.name + "]!"));
            default:
                throw new IllegalArgumentException("No such equipment [" + name + "] in this room [" + this.name + "]!");
        }
    }
}
