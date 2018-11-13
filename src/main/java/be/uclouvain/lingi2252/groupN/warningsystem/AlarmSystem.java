package be.uclouvain.lingi2252.groupN.warningsystem;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.House;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.User;
import be.uclouvain.lingi2252.groupN.signals.Frame;
import be.uclouvain.lingi2252.groupN.signals.Motion;
import be.uclouvain.lingi2252.groupN.signals.Signal;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class AlarmSystem {

    //fields
    protected List<CommunicationHub> hubs;
    private Boolean status;
    // <message,contact numbers>
    private Map<String ,List<String>> contacts;

    public AlarmSystem(List<CommunicationHub> hubs) {
        this.hubs = hubs;
        this.contacts = new HashMap<>();
        this.contacts.put("FALL",new ArrayList<>(Arrays.asList("118","Adalberto")));
        this.contacts.put("FIRE",new ArrayList<>(Arrays.asList("115","Natalie")));
        this.contacts.put("BREAK-IN",new ArrayList<>(Arrays.asList("112","Julie")));
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
        System.out.println("Calling " + this.contacts.get(message) + " with this message \"" + message + "\"");
        System.out.println("Calling emergency contact " + emergencyPerson);
    }

    public void setEmergencyNumber(String emergencyNumber) {
        this.emergencyNumber = emergencyNumber;
    }

    public void setEngaged(Boolean flag){
        this.status = flag;
        System.out.println("Alarm status: " + (status ? "armed" : "disarmed"));
    }


}
