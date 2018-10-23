package be.uclouvain.lingi2252.groupN.sensors;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.signals.Signal;

import java.util.concurrent.TimeUnit;

public abstract class Sensor {
    String name;
    Signal lastValue;
    CommunicationHub commHub;

    public Sensor(String name, CommunicationHub commHub) {
        this.name = name;
        this.commHub = commHub;
        lastValue = null;
    }

    public void sense(Signal signal) {
        System.out.println("Sensor [" + name + "] sensed something unusual!");
        if (!signal.equals(lastValue))
            lastValue = signal;
        send(signal);
    }

    public void send(Signal signal) {
        commHub.elaborate(signal, this);
    }

    public String getName() {
        return name;
    }
}
