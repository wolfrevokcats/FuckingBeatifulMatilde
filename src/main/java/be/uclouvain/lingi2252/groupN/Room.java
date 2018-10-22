package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.equipment.Equipment;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;

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
}
