package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.equipment.Doors;
import be.uclouvain.lingi2252.groupN.equipment.Equipment;
import be.uclouvain.lingi2252.groupN.equipment.Lights;
import be.uclouvain.lingi2252.groupN.equipment.Windows;
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
        equipmentList.stream()
                .filter(equipment -> equipment instanceof Lights)
                .filter(lights -> !lights.checkStatus())
                .forEach(lights -> lights.set(true));
    }

    public void roomLeft(User user) {
        System.out.println("[" + user.getName() + "] left the room [" + name + "]");
        equipmentList.stream()
                .filter(equipment -> equipment instanceof Lights)
                .filter(Equipment::checkStatus)
                .forEach(lights -> lights.set(false));
    }

    public void lockdown() {

    }

    public List<Frame> findObject(String[] objects) {
        List<Camera> cameras = sensors.stream().filter(sensor -> sensor instanceof Camera).map(sensor -> (Camera) sensor).collect(Collectors.toList());
        List<Frame> lastFrames = cameras.stream().map(camera -> (Frame) commHub.getLastValue(camera)).collect(Collectors.toList());

        List<Frame> res = new ArrayList<>();

        for (String object : objects) {
            res.addAll(lastFrames.stream()
                    .filter(Objects::nonNull)
                    .filter(frame -> frame.extract().contains(object))
                    .collect(Collectors.toList()));
        }

        return res;
    }

    public void evacuate() {
        System.out.println("Triggering [" + name + "] evacuation");
        for (Equipment equipment : equipmentList) {
            if (equipment instanceof Doors || equipment instanceof Windows)
                equipment.set(true);
        }
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

    public Sensor getSensor(String name) {
        for (Sensor sensor : sensors)
            if (sensor.getName().equals(name))
                return sensor;

        return null;
    }

    public Equipment getEquipment(String name) {
        String className = "be.uclouvain.lingi2252.groupN.equipment." + Parameterization.toClassName(name);
        return equipmentList.stream()
                .filter(equipment -> {
                    try {
                        return Class.forName(className).isInstance(equipment);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException("No such equipment [" + name + "] in this room [" + this.name + "]!");
                    }
                })
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No such equipment [" + name + "] in this room [" + this.name + "]!"));
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

    public List<Equipment> getEquipmentList() {
        return equipmentList;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }
}
