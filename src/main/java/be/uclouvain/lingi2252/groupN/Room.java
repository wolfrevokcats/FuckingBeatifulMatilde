package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.equipment.*;
import be.uclouvain.lingi2252.groupN.sensors.Camera;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;
import be.uclouvain.lingi2252.groupN.signals.Frame;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


public class Room {

    //Fields
    private String name;
    private List<Sensor> sensors;
    private CommunicationHub commHub;
    private List<Equipment> equipmentList;

    //constructor
    public Room(String name) {
        this.name = name;
        this.commHub = new CommunicationHub(this);
        this.sensors = new ArrayList<>();
        this.equipmentList = new ArrayList<>();
    }


    //methods
    public void roomEntered(User user) {
        System.out.println("[" + user.getName() + "] is entering the room [" + name + "]");
    }

    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public void addSensors(List<Sensor> sensors) {
        this.sensors.addAll(sensors);
    }

    public void removeSensor(Sensor sensor) {
        sensors.remove(sensor);
    }

    public void lockdown() {

    }

    public void evacuate() {
        System.out.println("Triggering [" + name + "] evacuation");
        for (Equipment equipment : equipmentList) {
            if (equipment instanceof Doors || equipment instanceof Windows)
                equipment.set(true);
        }
    }

    public void findObject(String object) {


    }

    public void findWhy(String what) {
        System.out.println("Object Tracking procedure triggered to find what caused [" + what + "] in [" + name + "]");
        if (what.equals("smoke")) {
            List<Camera> cameras = sensors.stream().filter(sensor -> sensor instanceof Camera).map(sensor -> (Camera) sensor).collect(Collectors.toList());
            List<Frame> lastFrames = cameras.stream().map(camera -> (Frame) commHub.getLastValue(camera)).collect(Collectors.toList());

            lastFrames.stream()
                    .filter(Objects::nonNull)
                    .filter(frame -> frame.extract().contains("smoke"))
                    .forEach(frame -> System.out.println("Smoke identified [" + frame.extract().substring(6) + "]"));

        }
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
            case "lights":
                return equipmentList.stream()
                        .filter(equipment -> equipment instanceof Lights)
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("No such equipment [" + name + "] in this room [" + this.name + "]!"));
            case "blinds":
                return equipmentList.stream()
                        .filter(equipment -> equipment instanceof Blinds)
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("No such equipment [" + name + "] in this room [" + this.name + "]!"));
            case "conditioners":
                return equipmentList.stream()
                        .filter(equipment -> equipment instanceof Conditioners)
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("No such equipment [" + name + "] in this room [" + this.name + "]!"));
            case "fireplaces":
                return equipmentList.stream()
                        .filter(equipment -> equipment instanceof Fireplaces)
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("No such equipment [" + name + "] in this room [" + this.name + "]!"));
            case "heaters":
                return equipmentList.stream()
                        .filter(equipment -> equipment instanceof Heaters)
                        .findAny()
                        .orElseThrow(() -> new IllegalArgumentException("No such equipment [" + name + "] in this room [" + this.name + "]!"));
            default:
                throw new IllegalArgumentException("No such equipment [" + name + "] in this room [" + this.name + "]!");
        }
    }
}
