package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.sensors.Camera;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Main {
    public static void main(String[] args) {
        scenario2();
    }

    private static void scenario2() {
        List<User> users = new ArrayList<>(Arrays.asList(new User("Matilde"), new User("Quentin"), new User("Benoît"), new User("Kim")));
        List<Room> rooms = new ArrayList<>(Arrays.asList(new Room("entrance"), new Room("kitchen"), new Room("living room"), new Room("bedroom"), new Room("bathroom")));
        House house = new House(users.get(0), rooms);
        house.addUsers(users);
        List<Sensor> entranceCameras = new ArrayList<>(Arrays.asList(new Camera(), new Camera()));
        List<Sensor> kitchenCameras = new ArrayList<>(Arrays.asList(new Camera(), new Camera()));
        List<Sensor> livingRoomCameras = new ArrayList<>(Arrays.asList(new Camera(), new Camera(), new Camera()));
        List<Sensor> bedroomCameras = new ArrayList<>(Arrays.asList(new Camera(), new Camera()));
        List<Sensor> bathroomCameras = new ArrayList<>(Arrays.asList(new Camera(), new Camera()));
        if (house.getRoom("entrance").isPresent()) {
            house.getRoom("entrance").get().addSensors(entranceCameras);
        }
        if (house.getRoom("kitchen").isPresent()) {
            house.getRoom("kitchen").get().addSensors(kitchenCameras);
        }
        if (house.getRoom("living room").isPresent()) {
            house.getRoom("living room").get().addSensors(livingRoomCameras);
        }
        if (house.getRoom("bedroom").isPresent()) {
            house.getRoom("bedroom").get().addSensors(bedroomCameras);
        }
        if (house.getRoom("bathroom").isPresent()) {
            house.getRoom("bathroom").get().addSensors(bathroomCameras);
        }
    }
}
