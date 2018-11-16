package be.uclouvain.lingi2252.groupN.warningsystem;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.signals.Signal;

import java.util.*;

public abstract class WarningSystem {
    private List<CommunicationHub> hubs;
    // <message,contact numbers>
    private Map<String, List<String>> contacts;

    public void initialize(List<CommunicationHub> hubs) {
        this.hubs = hubs;
        this.contacts = new HashMap<>();
        this.contacts.put("FALL", new ArrayList<>(Arrays.asList("118", "Adalberto")));
        this.contacts.put("FIRE", new ArrayList<>(Arrays.asList("115", "Natalie")));
        this.contacts.put("BREAK-IN", new ArrayList<>(Arrays.asList("112", "Julie")));
    }

    public abstract void compute(Signal signal, Room room);

    public void ring(Room room, String issue) {
        //send lists of commands to commhubs

    }

    public void emergencyCall(String reason, String message) {
        List<String> toBeCalled = this.contacts.get(reason);
        for (String contact : toBeCalled) {
            System.out.println("Calling " + contact + " with this message \"" + message + "\"");
        }
    }


}
