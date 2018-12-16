package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.actuators.TemperatureControl;
import be.uclouvain.lingi2252.groupN.sensors.AirSensor;
import be.uclouvain.lingi2252.groupN.sensors.Camera;
import be.uclouvain.lingi2252.groupN.sensors.ContactSensor;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;
import be.uclouvain.lingi2252.groupN.signals.*;
import be.uclouvain.lingi2252.groupN.warningsystem.AirQualityTester;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmSystem;

import java.util.HashMap;
import java.util.Map;
import java.util.Observable;
import java.util.Observer;

public class CommunicationHub extends Observable implements Observer {

    // fields
    private Room owner;
    private Map<Sensor, Signal> lastValues;

    //constructor
    public CommunicationHub(Room owner) {
        this.owner = owner;
        lastValues = new HashMap<>();
    }

    @Override
    public void update(Observable o, Object arg) {
        Sensor sensor = (Sensor) o;
        Signal signal = (Signal) arg;

        lastValues.put(sensor, signal);

        if (signal instanceof Temperature) {
            System.out.println("Temperature measured [" + signal.extract() + "] in [" + owner.getName() + "]");

            notifyObservers(signal.extract());

        } else if (signal instanceof Motion) {
            if (signal.extract().equals("FALL")) {
                System.out.println("Fall detected in [" + owner.getName() + "]");
                AlarmSystem.getInstance().compute(signal, owner);
            }
        } else if (signal instanceof Frame && sensor instanceof Camera) {
            AlarmSystem.getInstance().compute(signal, owner);

        } else if (signal instanceof Air && sensor instanceof AirSensor) {
            AirQualityTester.getInstance().compute(signal, owner);

        } else if (signal instanceof Contact & sensor instanceof ContactSensor) {
            AlarmSystem.getInstance().compute(signal, owner);
        }
    }

    public Signal getLastValue(String sensorName) {
        for (Sensor sensor : lastValues.keySet()) {
            if (sensor.getName().equals(sensorName)) return lastValues.get(sensor);
        }
        return null;
    }

    public Signal getLastValue(Sensor sensor) {
        return lastValues.get(sensor);
    }

    public Room getOwner() {
        return owner;
    }
}
