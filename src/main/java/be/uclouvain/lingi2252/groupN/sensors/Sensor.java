package be.uclouvain.lingi2252.groupN.sensors;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.signals.Signal;

public abstract class Sensor {
    String name;
    Signal lastValue;
    CommunicationHub commHub;

    public Sensor(String name, CommunicationHub commHub) {
        this.name = name;
        this.commHub = commHub;
    }

    public abstract void sense(Signal signal);
    public abstract void send(Signal signal);
}
