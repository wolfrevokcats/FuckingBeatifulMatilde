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

public class AlarmSystem extends WarningSystem {

    //fields
    private static final AlarmSystem SINGLE_INSTANCE = new AlarmSystem();
    private static boolean enabled = false;
    private boolean armed;

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

    public void initialize(List<CommunicationHub> hubs) {
        super.initialize(hubs);
        this.armed = false;
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

    public void setEngaged(Boolean flag) {
        if (flag != armed) {
            this.armed = flag;
            System.out.println("Alarm status: " + (armed ? "armed" : "disarmed"));
        }
    }

    public boolean isArmed() {
        return armed;
    }
}
