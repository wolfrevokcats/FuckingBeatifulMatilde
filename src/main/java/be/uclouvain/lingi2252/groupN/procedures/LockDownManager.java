package be.uclouvain.lingi2252.groupN.procedures;

import be.uclouvain.lingi2252.groupN.House;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.actuators.Actuator;
import be.uclouvain.lingi2252.groupN.actuators.Lockable;

public class LockDownManager {
    private static final LockDownManager SINGLE_INSTANCE = new LockDownManager();
    private static boolean enabled = false;

    private LockDownManager() {
    }

    public static LockDownManager getInstance() {
        if (enabled) return SINGLE_INSTANCE;
        System.out.println("Lock-down is not available in this house");
        return null;
    }

    public static void enable() {
        enabled = true;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public void lockDown(Room room) {
        System.out.println("Applying lock-down procedure to room [" + room.getName() + "]");
        for (Actuator actuator : room.getActuatorList()) {
            if (actuator instanceof Lockable) {
                actuator.set(false);
                ((Lockable) actuator).setLocked(true);
            }

        }
    }

    public void lockDown() {
        House.getInstance().getRooms().forEach(this::lockDown);
    }
}
