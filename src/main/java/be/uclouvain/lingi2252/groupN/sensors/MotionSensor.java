package be.uclouvain.lingi2252.groupN.sensors;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.signals.Signal;

public class MotionSensor extends Sensor {
    public MotionSensor(String name, CommunicationHub commHub) {
        super(name, commHub);
    }

    @Override
    public void send(Signal signal) {

    }

    @Override
    public void sense(Signal signal) {

    }
}
