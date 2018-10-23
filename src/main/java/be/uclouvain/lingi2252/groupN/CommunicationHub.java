package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.sensors.AirSensor;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;
import be.uclouvain.lingi2252.groupN.signals.Air;
import be.uclouvain.lingi2252.groupN.signals.Signal;
import be.uclouvain.lingi2252.groupN.warningsystem.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CommunicationHub {

    // fields
    private AlarmSystem alarm;
    private Room owner;
    private AirQualityTester airQC;
    private Map<Sensor, Signal> lastValues;

    //constructor
    public CommunicationHub(Room owner){
        this.owner = owner;
        lastValues = new HashMap<>();
    }

    public CommunicationHub(Room owner, AirQualityTester airQC){
        this.owner = owner;
        this.airQC = airQC;
        lastValues = new HashMap<>();
    }

    public CommunicationHub(Room owner, AlarmSystem alarm){
        this.owner = owner;
        this.alarm = alarm;
        lastValues = new HashMap<>();
    }

    public CommunicationHub(Room owner, AlarmSystem alarm, AirQualityTester airQC){
        this.owner = owner;
        this.alarm = alarm;
        this.airQC = airQC;
        lastValues = new HashMap<>();
    }

    //methods
    public void sendToRoom(Command command){

    }

    public void elaborate(Signal signal, Sensor sensor) {
        lastValues.put(sensor, signal);
        if (signal instanceof Air && sensor instanceof AirSensor)
            airQC.compute(signal, owner);
    }

    public Signal getLastValue(Sensor sensor) {
        return lastValues.get(sensor);
    }
}
