package be.uclouvain.lingi2252.groupN.warningsystem;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.signals.Signal;

import java.util.*;

public abstract class WarningSystem {
    // <message,contact numbers>
    private static Map<String, List<String>> emergencyContacts = new HashMap<>();

    public static void addOrReplaceContact(String reason, List<String> contacts) {
        emergencyContacts.put(reason, contacts);
    }

    public void initialize() {
        emergencyContacts.put("FALL", new ArrayList<>(Arrays.asList("118", "Adalberto")));
        emergencyContacts.put("FIRE", new ArrayList<>(Arrays.asList("115", "Natalie")));
        emergencyContacts.put("BREAK-IN", new ArrayList<>(Arrays.asList("112", "Isabella")));
    }

    public abstract void compute(Signal signal, Room room);

    public abstract void ring(Room room, String issue);

    protected void emergencyCall(String reason, String message) {
        List<String> toBeCalled = emergencyContacts.get(reason);
        for (String contact : toBeCalled) {
            System.out.println("Calling " + contact + " with this message \"" + message + "\"");
        }
    }
}
