package be.uclouvain.lingi2252.groupN.warningsystem;

import be.uclouvain.lingi2252.groupN.House;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.User;
import be.uclouvain.lingi2252.groupN.procedures.LockDownManager;
import be.uclouvain.lingi2252.groupN.signals.Contact;
import be.uclouvain.lingi2252.groupN.signals.Frame;
import be.uclouvain.lingi2252.groupN.signals.Motion;
import be.uclouvain.lingi2252.groupN.signals.Signal;

import java.util.stream.Collectors;

public class AlarmSystem extends WarningSystem {

    //fields
    private static final AlarmSystem SINGLE_INSTANCE = new AlarmSystem();
    private static boolean enabled = false;
    private AlarmStatus status;

    private AlarmSystem() {
    }

    public static void enable() {
        enabled = true;
    }

    public static AlarmSystem getInstance() {
        if (enabled) return SINGLE_INSTANCE;
        System.out.println("There is no alarm system in this house");
        return null;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public void initialize() {
        super.initialize();
        this.status = AlarmStatus.DISARMED;
    }

    public void compute(Signal signal, Room room) {
        if (signal instanceof Motion && signal.extract().equals("FALL")) {
            emergencyCall("FALL", "Somebody fell in [" + room.getName() + "]");

        } else if (signal instanceof Frame) {
            if (House.getInstance().getResidents().stream().map(User::getName).collect(Collectors.toList()).contains(signal.extract())) {
                System.out.println("[" + signal.extract() + "] recognized.");
                this.setStatus(AlarmStatus.DISARMED);
            } else {
                //Something / someone else than a user has been detected
            }

        } else if ((this.status == AlarmStatus.ARMED || this.status == AlarmStatus.PRESENCE) && signal instanceof Contact) {
            System.out.println("A detachment has been detected in [" + room.getName() + "]");
            ring(room, "detachment detected");
            emergencyCall("BREAK-IN", "Somebody not authorized entered in [" + room.getName() + "]");


        }


    }

    @Override
    public void ring(Room room, String issue) {
        if (issue.equals("detachment detected")) {
            System.out.print("Alarm starts ringing in the house...");
            System.out.println("[unusual detachment] detected!");
            if (LockDownManager.isEnabled()) LockDownManager.getInstance().lockDown(room);
        }
    }

    public AlarmStatus getStatus() {
        return status;
    }

    public void setStatus(AlarmStatus status) {
        if (status != this.status) {
            this.status = status;
            System.out.println("Alarm status: " + status.getDescription() + ".");
        }
    }
}
