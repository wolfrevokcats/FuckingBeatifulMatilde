package be.uclouvain.lingi2252.groupN.warningsystem;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.House;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.User;
import be.uclouvain.lingi2252.groupN.signals.Frame;
import be.uclouvain.lingi2252.groupN.signals.Motion;
import be.uclouvain.lingi2252.groupN.signals.Signal;

import java.util.*;
import java.util.stream.Collectors;

public class AlarmSystem {

    //fields
    protected List<CommunicationHub> hubs;
    private Boolean status;
    // <message,contact numbers>
    private Map<String, List<String>> contacts;

    public AlarmSystem(List<CommunicationHub> hubs) {
        this.status = false;
        this.hubs = hubs;
        this.contacts = new HashMap<>();
        this.contacts.put("FALL", new ArrayList<>(Arrays.asList("118", "Adalberto")));
        this.contacts.put("FIRE", new ArrayList<>(Arrays.asList("115", "Natalie")));
        this.contacts.put("BREAK-IN", new ArrayList<>(Arrays.asList("112", "Julie")));
    }

    public void compute(Signal signal, Room room) {
        if (signal instanceof Motion && signal.extract().equals("FALL")) {
            emergencyCall("FALL", "Somebody fell in [" + room.getName() + "]");

        } else if (signal instanceof Frame) {
            if (House.getInstance().getResidents().stream().map(User::getName).collect(Collectors.toList()).contains(signal.extract())) {
                System.out.println("[" + signal.extract() + "] recognized.");
                this.setEngaged(false);
            } else {
                //Something / someone else than a user has been detected
            }

        }

    }

    public void ring(Room room, String issue) {
        //send lists of commands to commhubs

    }

    public void emergencyCall(String reason, String message) {
        List<String> toBeCalled = this.contacts.get(reason);
        for (String contact : toBeCalled) {
            System.out.println("Calling " + contact + " with this message \"" + message + "\"");
        }

    }

    public void setEngaged(Boolean flag) {
        if (flag != status) {
            this.status = flag;
            System.out.println("Alarm status: " + (status ? "armed" : "disarmed"));
        }
    }


}
