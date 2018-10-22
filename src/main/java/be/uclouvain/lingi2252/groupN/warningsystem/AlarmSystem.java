package be.uclouvain.lingi2252.groupN.warningsystem;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.signals.Signal;

import java.util.List;

public class AlarmSystem {

    //fields
    protected List<CommunicationHub> hubs;
    String emergencyNumber;

    public AlarmSystem(List<CommunicationHub> hubs) {
        this.hubs = hubs;
        emergencyNumber = "112";
    }

    public void compute(Signal signal) {

    }

    public void ring() {
        //send lists of commands to commhubs
    }

    public void emergencyCall(String message) {
        System.out.println("Calling " + emergencyNumber + " with this message " + message);
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }
}
