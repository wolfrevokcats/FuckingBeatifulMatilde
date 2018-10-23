package be.uclouvain.lingi2252.groupN.warningsystem;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.signals.Motion;
import be.uclouvain.lingi2252.groupN.signals.Signal;

import java.util.List;

public class AlarmSystem {

    //fields
    protected List<CommunicationHub> hubs;
    String emergencyNumber;
    String emergencyPerson;

    public AlarmSystem(List<CommunicationHub> hubs) {
        this.hubs = hubs;
        emergencyNumber = "112";
        emergencyPerson = "[Adalberto]";
    }

    public void compute(Signal signal, Room room) {
        if (signal instanceof Motion && signal.extract().equals("FALL")) {
            emergencyCall("Somebody fell in [" + room.getName() + "]");
        }
    }

    public void ring(Room room, String issue) {
        //send lists of commands to commhubs

    }

    public void emergencyCall(String message) {
        System.out.println("Calling " + emergencyNumber + " with this message \"" + message + "\"");
        System.out.println("Calling emergency contact " + emergencyPerson);
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }
}
