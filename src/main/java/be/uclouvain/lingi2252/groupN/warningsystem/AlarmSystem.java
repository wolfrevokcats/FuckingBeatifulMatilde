package be.uclouvain.lingi2252.groupN.warningsystem;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.House;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.User;
import be.uclouvain.lingi2252.groupN.signals.Frame;
import be.uclouvain.lingi2252.groupN.signals.Motion;
import be.uclouvain.lingi2252.groupN.signals.Signal;

import java.util.List;
import java.util.stream.Collectors;

public class AlarmSystem {

    //fields
    protected List<CommunicationHub> hubs;
    private String emergencyNumber;
    private String emergencyPerson;
    private Boolean status;


    public AlarmSystem(List<CommunicationHub> hubs) {
        this.hubs = hubs;
        emergencyNumber = "112";
        emergencyPerson = "[Adalberto]";
    }

    public void compute(Signal signal, Room room) {
        if (signal instanceof Motion && signal.extract().equals("FALL")) {
            emergencyCall("Somebody fell in [" + room.getName() + "]");
        } else if (signal instanceof Frame && House.getInstance().getResidents().stream().map(User::getName).collect(Collectors.toList()).contains(signal.extract())) {
            this.setEngaged(false);
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

    public void setEngaged(Boolean status){
        this.status = status;
        System.out.println("Alarm status: " + (status ? "armed" : "disarmed"));
    }


}
