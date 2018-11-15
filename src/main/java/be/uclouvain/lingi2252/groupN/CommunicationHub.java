package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.equipment.TemperatureControl;
import be.uclouvain.lingi2252.groupN.sensors.AirSensor;
import be.uclouvain.lingi2252.groupN.sensors.Camera;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;
import be.uclouvain.lingi2252.groupN.signals.*;

import java.util.HashMap;
import java.util.Map;

public class CommunicationHub {

    // fields
    private Room owner;
    private Map<Sensor, Signal> lastValues;

    //constructor
    public CommunicationHub(Room owner) {
        this.owner = owner;
        lastValues = new HashMap<>();
    }

    //methods
    public void elaborate(Signal signal, Sensor sensor) {
        lastValues.put(sensor, signal);

        if (signal instanceof Temperature) {
            System.out.println("Temperature measured [" + signal.extract() + "] in [" + owner.getName() + "].");

            owner.getEquipmentList().stream()
                    .filter(equipment -> equipment instanceof TemperatureControl)
                    .forEach(equipment -> ((TemperatureControl) equipment).giveTemperature((Double) signal.extract()));

        } else if (signal instanceof Motion) {
            if (signal.extract().equals("FALL")) {
                System.out.println("Fall detected in [" + owner.getName() + "]");
                House.getInstance().getAlarm().compute(signal, owner);
            }
        } else if (signal instanceof Frame && sensor instanceof Camera) {
            House.getInstance().getAlarm().compute(signal, owner);

        } else if (signal instanceof Air && sensor instanceof AirSensor) {
            House.getInstance().getAirQT().compute(signal, owner);
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
