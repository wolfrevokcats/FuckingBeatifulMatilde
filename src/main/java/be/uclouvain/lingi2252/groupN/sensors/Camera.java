package be.uclouvain.lingi2252.groupN.sensors;

import be.uclouvain.lingi2252.groupN.CommunicationHub;

public class Camera extends Sensor {
    public Camera(String name, CommunicationHub commHub) {
        super(name, commHub);
    }
}
