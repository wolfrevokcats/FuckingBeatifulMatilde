package be.uclouvain.lingi2252.groupN.procedures;

import be.uclouvain.lingi2252.groupN.House;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.actuators.Actuator;
import be.uclouvain.lingi2252.groupN.actuators.Doors;
import be.uclouvain.lingi2252.groupN.actuators.Windows;

public class EvacuationManager {
    private static final EvacuationManager SINGLE_INSTANCE = new EvacuationManager();
    private static boolean enabled = false;

    private EvacuationManager() {
    }

    public static EvacuationManager getInstance() {
        if (enabled) return SINGLE_INSTANCE;
        System.out.println("Evacuation is not available in this house");
        return null;
    }

    public static void enable() {
        enabled = true;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public void evacuate(Room room) {
        System.out.println("Triggering [" + room.getName() + "] evacuation");
        for (Actuator actuator : room.getActuatorList()) {
            if (actuator instanceof Doors || actuator instanceof Windows)
                actuator.set(true);
        }
    }

    public void evacuate() {
        House.getInstance().getRooms().forEach(this::evacuate);
    }
}
