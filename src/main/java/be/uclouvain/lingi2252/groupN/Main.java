package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.equipment.Cookers;
import be.uclouvain.lingi2252.groupN.equipment.Doors;
import be.uclouvain.lingi2252.groupN.equipment.Equipment;
import be.uclouvain.lingi2252.groupN.equipment.Windows;
import be.uclouvain.lingi2252.groupN.sensors.Camera;
import be.uclouvain.lingi2252.groupN.sensors.HarmfulGasDetector;
import be.uclouvain.lingi2252.groupN.sensors.MotionSensor;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
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

        if (house.getRoom("entrance").isPresent()) {
            Room entrance = house.getRoom("entrance").get();
            List<Sensor> entranceSensors = new ArrayList<>(Arrays.asList(new Camera("entcam1", entrance.getCommHub()), new Camera("entcam2", entrance.getCommHub()), new HarmfulGasDetector("enthfg1", entrance.getCommHub()), new MotionSensor("entmos1", entrance.getCommHub())));
            List<Equipment> entranceEquipment = new ArrayList<>(Arrays.asList(new Doors(), new Windows()));
            entrance.addSensors(entranceSensors);
            entrance.addEquipment(entranceEquipment);
        }
        if (house.getRoom("kitchen").isPresent()) {
            Room kitchen = house.getRoom("kitchen").get();
            List<Sensor> kitchenSensors = new ArrayList<>(Arrays.asList(new Camera("kitcam1", kitchen.getCommHub()), new Camera("kitcam2", kitchen.getCommHub()), new HarmfulGasDetector("kithfg1", kitchen.getCommHub()), new MotionSensor("kitmos1", kitchen.getCommHub())));
            List<Equipment> kitchenEquipment = new ArrayList<>(Arrays.asList(new Doors(), new Windows(), new Cookers()));
            kitchen.addSensors(kitchenSensors);
            kitchen.addEquipment(kitchenEquipment);
        }
        if (house.getRoom("living room").isPresent()) {
            Room livingRoom = house.getRoom("living room").get();
            List<Sensor> livingRoomSensors = new ArrayList<>(Arrays.asList(new Camera("livcam1", livingRoom.getCommHub()), new Camera("livcam2", livingRoom.getCommHub()), new Camera("livcam3", livingRoom.getCommHub()), new HarmfulGasDetector("livhfg1", livingRoom.getCommHub()), new MotionSensor("livmos1", livingRoom.getCommHub()), new MotionSensor("livmos2", livingRoom.getCommHub())));
            List<Equipment> livingRoomEquipment = new ArrayList<>(Arrays.asList(new Doors(), new Windows()));
            livingRoom.addSensors(livingRoomSensors);
            livingRoom.addEquipment(livingRoomEquipment);
        }
        if (house.getRoom("bedroom").isPresent()) {
            Room bedroom = house.getRoom("bedroom").get();
            List<Sensor> bedroomSensors = new ArrayList<>(Arrays.asList(new Camera("bedcam1", bedroom.getCommHub()), new Camera("bedcam2", bedroom.getCommHub()), new HarmfulGasDetector("bedhfg1", bedroom.getCommHub()), new MotionSensor("bedmos1", bedroom.getCommHub())));
            List<Equipment> bedroomEquipment = new ArrayList<>(Arrays.asList(new Doors(), new Windows()));
            bedroom.addSensors(bedroomSensors);
            bedroom.addEquipment(bedroomEquipment);
        }
        if (house.getRoom("bathroom").isPresent()) {
            Room bathroom = house.getRoom("bathroom").get();
            List<Sensor> bathroomSensors = new ArrayList<>(Arrays.asList(new Camera("batcam1", bathroom.getCommHub()), new Camera("batcam2", bathroom.getCommHub()), new HarmfulGasDetector("bathfg1", bathroom.getCommHub()), new MotionSensor("batmos1", bathroom.getCommHub())));
            List<Equipment> bathroomEquipment = new ArrayList<>(Collections.singletonList(new Doors()));
            bathroom.addSensors(bathroomSensors);
            bathroom.addEquipment(bathroomEquipment);
        }
    }
}
