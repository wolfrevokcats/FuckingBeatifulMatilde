package be.uclouvain.lingi2252.groupN.sensors;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.signals.Signal;

import java.util.Observable;

public abstract class Sensor extends Observable {
    private String name;
    private Signal lastValue;

    public Sensor(String name, CommunicationHub commHub) {
        this.name = name;
        addObserver(commHub);
        lastValue = null;
    }

    public void sense(Signal signal) {
        System.out.println("Sensor [" + name + "] received a signal");
        if (!signal.equals(lastValue)){
            lastValue = signal;
            notifyObservers(signal);
        }

    }

    public String getName() {
        return name;
    }
}
