package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.sensors.AirSensor;
import be.uclouvain.lingi2252.groupN.sensors.Camera;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;
import be.uclouvain.lingi2252.groupN.signals.Air;
import be.uclouvain.lingi2252.groupN.signals.Frame;
import be.uclouvain.lingi2252.groupN.signals.Motion;
import be.uclouvain.lingi2252.groupN.signals.Signal;

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
    public void sendToRoom(Command command) {

    }

    public void elaborate(Signal signal, Sensor sensor) {
        lastValues.put(sensor, signal);

        if (signal instanceof Motion)
            if (signal.extract().equals("FALL")) {
                System.out.println("Fall detected in [" + owner.getName() + "]");
                House.getInstance().getAlarm().compute(signal, owner);
            }

        if (signal instanceof Frame && sensor instanceof Camera) {
            House.getInstance().getAlarm().compute(signal, owner);
        }

        if (signal instanceof Air && sensor instanceof AirSensor)
            House.getInstance().getAirQT().compute(signal, owner);
    }

    public Signal getLastValue(Sensor sensor) {
        return lastValues.get(sensor);
    }
}
