package be.uclouvain.lingi2252.groupN.sensors;

import be.uclouvain.lingi2252.groupN.CommunicationHub;

public class TemperatureSensor extends Sensor {
    public TemperatureSensor(String name, CommunicationHub commHub) {
        super(name, commHub);
    }
}
