package be.uclouvain.lingi2252.groupN.sensors;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.signals.Signal;

public abstract class Sensor {
    private String name;
    private Signal lastValue;
    private CommunicationHub commHub;

    public Sensor(String name, CommunicationHub commHub) {
        this.name = name;
        this.commHub = commHub;
        lastValue = null;
    }

    public void sense(Signal signal) {
        System.out.println("Sensor [" + name + "] received a signal");
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
