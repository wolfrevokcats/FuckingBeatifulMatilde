package be.uclouvain.lingi2252.groupN.procedures;

import be.uclouvain.lingi2252.groupN.House;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.sensors.Camera;
import be.uclouvain.lingi2252.groupN.signals.Frame;

import java.util.*;
import java.util.stream.Collectors;

public class ObjectTracker {
    private static final ObjectTracker SINGLE_INSTANCE = new ObjectTracker();
    private static boolean enabled = false;

    private ObjectTracker() {

    }

    public static ObjectTracker getInstance() {
        if (enabled) return SINGLE_INSTANCE;
        System.out.println("Object Tracking is not available in this house");
        return null;
    }

    public static void enable() {
        enabled = true;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public List<Frame> find(Room room, String[] things, boolean main) {
        if (main)
            System.out.println("Object Tracking procedure triggered to find " + Arrays.toString(things) + " in [" + room.getName() + "]");

        List<Camera> cameras = room.getSensors().stream().filter(sensor -> sensor instanceof Camera).map(sensor -> (Camera) sensor).collect(Collectors.toList());
        List<Frame> lastFrames = cameras.stream().map(camera -> (Frame) room.getCommHub().getLastValue(camera)).collect(Collectors.toList());

        List<Frame> res = new ArrayList<>();

        for (String thing : things) {
            res.addAll(lastFrames.stream()
                    .filter(Objects::nonNull)
                    .filter(frame -> frame.extract().contains(thing))
                    .collect(Collectors.toList()));
        }

        return res;
    }

    public Map<Room, List<Frame>> find(String[] things) {
        System.out.println("Object Tracking procedure triggered to find " + Arrays.toString(things));

        List<Room> rooms = House.getInstance().getRooms();
        Map<Room, List<Frame>> possibleMatches = new HashMap<>();

        rooms.stream()
                .filter(room -> !find(room, things, false).isEmpty())
                .forEach(room -> possibleMatches.put(room, find(room, things, false)));

        return possibleMatches;
    }
}
