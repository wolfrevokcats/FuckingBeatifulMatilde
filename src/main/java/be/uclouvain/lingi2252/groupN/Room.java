package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.actuators.Actuator;
import be.uclouvain.lingi2252.groupN.actuators.Lights;
import be.uclouvain.lingi2252.groupN.actuators.TemperatureControl;
import be.uclouvain.lingi2252.groupN.parameterization.Parameterization;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;

import java.util.ArrayList;
import java.util.List;
import java.util.Observer;


public class Room {

    //Fields
    private String name;
    private List<Sensor> sensors;
    private CommunicationHub commHub;
    private List<Actuator> actuatorList;

    //constructor
    public Room(String name) {
        this.name = name;
        this.commHub = new CommunicationHub(this);
        this.sensors = new ArrayList<>();
        this.actuatorList = new ArrayList<>();
    }


    //methods
    public void roomEntered(User user) {
        System.out.println("[" + user.getName() + "] is entering the room [" + name + "]");
        actuatorList.stream()
                .filter(equipment -> equipment instanceof Lights)
                .filter(lights -> !lights.checkStatus())
                .forEach(lights -> lights.set(true));
    }

    public void roomLeft(User user) {
        System.out.println("[" + user.getName() + "] left the room [" + name + "]");
        actuatorList.stream()
                .filter(equipment -> equipment instanceof Lights)
                .filter(Actuator::checkStatus)
                .forEach(lights -> lights.set(false));
    }

    public Sensor getSensor(String name) {
        for (Sensor sensor : sensors)
            if (sensor.getName().equals(name))
                return sensor;

        return null;
    }

    public Actuator getEquipment(String name) {
        String className = "be.uclouvain.lingi2252.groupN.actuators." + Parameterization.toClassName(name);
        return actuatorList.stream()
                .filter(equipment -> {
                    try {
                        return Class.forName(className).isInstance(equipment);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalArgumentException("The actuators [" + name + "] does not exist!");
                    }
                })
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("No such actuators [" + name + "] in this room [" + this.name + "]!"));
    }


    public void addSensor(Sensor sensor) {
        sensors.add(sensor);
    }

    public void removeSensor(Sensor sensor) {
        sensors.remove(sensor);
    }

    public void addEquipment(Actuator actuator) {
        actuatorList.add(actuator);
        if (actuator instanceof TemperatureControl) commHub.addObserver((TemperatureControl) actuator);
    }

    public void removeEquipment(Actuator actuator) {
        actuatorList.remove(actuator);
        commHub.deleteObserver((Observer) actuator);
    }

    public String getName() {
        return name;
    }

    public CommunicationHub getCommHub() {
        return commHub;
    }

    public List<Actuator> getActuatorList() {
        return actuatorList;
    }

    public List<Sensor> getSensors() {
        return sensors;
    }
}
