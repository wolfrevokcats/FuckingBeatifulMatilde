package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.equipment.Cookers;
import be.uclouvain.lingi2252.groupN.equipment.Doors;
import be.uclouvain.lingi2252.groupN.equipment.Equipment;
import be.uclouvain.lingi2252.groupN.equipment.Windows;
import be.uclouvain.lingi2252.groupN.sensors.AirSensor;
import be.uclouvain.lingi2252.groupN.sensors.Camera;
import be.uclouvain.lingi2252.groupN.sensors.MotionSensor;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;
import be.uclouvain.lingi2252.groupN.signals.Air;
import be.uclouvain.lingi2252.groupN.signals.Frame;
import be.uclouvain.lingi2252.groupN.signals.Motion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        try {
            scenario2();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void scenario2() throws InterruptedException {
        List<User> users = new ArrayList<>(Arrays.asList(new User("Matilde"), new User("Quentin"), new User("Benoît"), new User("Kim")));

        House house = new House(users.get(0));

        List<Room> rooms = new ArrayList<>(Arrays.asList(new Room(house, "entrance"), new Room(house, "kitchen"), new Room(house, "living room"), new Room(house, "bedroom"), new Room(house, "bathroom")));

        house.addRooms(rooms);
        house.addUsers(users);

        if (house.getRoom("entrance").isPresent()) {
            Room entrance = house.getRoom("entrance").get();
            List<Sensor> entranceSensors = new ArrayList<>(Arrays.asList(new Camera("entcam1", entrance.getCommHub()), new Camera("entcam2", entrance.getCommHub()), new AirSensor("enthgd1", entrance.getCommHub()), new MotionSensor("entmos1", entrance.getCommHub())));
            List<Equipment> entranceEquipment = new ArrayList<>(Arrays.asList(new Doors(entrance), new Windows(entrance)));
            entrance.addSensors(entranceSensors);
            entrance.addEquipment(entranceEquipment);
        }
        if (house.getRoom("kitchen").isPresent()) {
            Room kitchen = house.getRoom("kitchen").get();
            List<Sensor> kitchenSensors = new ArrayList<>(Arrays.asList(new Camera("kitcam1", kitchen.getCommHub()), new Camera("kitcam2", kitchen.getCommHub()), new AirSensor("kithgd1", kitchen.getCommHub()), new MotionSensor("kitmos1", kitchen.getCommHub())));
            List<Equipment> kitchenEquipment = new ArrayList<>(Arrays.asList(new Doors(kitchen), new Windows(kitchen), new Cookers(kitchen)));
            kitchen.addSensors(kitchenSensors);
            kitchen.addEquipment(kitchenEquipment);
        }
        if (house.getRoom("living room").isPresent()) {
            Room livingRoom = house.getRoom("living room").get();
            List<Sensor> livingRoomSensors = new ArrayList<>(Arrays.asList(new Camera("livcam1", livingRoom.getCommHub()), new Camera("livcam2", livingRoom.getCommHub()), new Camera("livcam3", livingRoom.getCommHub()), new AirSensor("livhgd1", livingRoom.getCommHub()), new MotionSensor("livmos1", livingRoom.getCommHub()), new MotionSensor("livmos2", livingRoom.getCommHub())));
            List<Equipment> livingRoomEquipment = new ArrayList<>(Arrays.asList(new Doors(livingRoom), new Windows(livingRoom)));
            livingRoom.addSensors(livingRoomSensors);
            livingRoom.addEquipment(livingRoomEquipment);
        }
        if (house.getRoom("bedroom").isPresent()) {
            Room bedroom = house.getRoom("bedroom").get();
            List<Sensor> bedroomSensors = new ArrayList<>(Arrays.asList(new Camera("bedcam1", bedroom.getCommHub()), new Camera("bedcam2", bedroom.getCommHub()), new AirSensor("bedhgd1", bedroom.getCommHub()), new MotionSensor("bedmos1", bedroom.getCommHub())));
            List<Equipment> bedroomEquipment = new ArrayList<>(Arrays.asList(new Doors(bedroom), new Windows(bedroom)));
            bedroom.addSensors(bedroomSensors);
            bedroom.addEquipment(bedroomEquipment);
        }
        if (house.getRoom("bathroom").isPresent()) {
            Room bathroom = house.getRoom("bathroom").get();
            List<Sensor> bathroomSensors = new ArrayList<>(Arrays.asList(new Camera("batcam1", bathroom.getCommHub()), new Camera("batcam2", bathroom.getCommHub()), new AirSensor("bathgd1", bathroom.getCommHub()), new MotionSensor("batmos1", bathroom.getCommHub())));
            List<Equipment> bathroomEquipment = new ArrayList<>(Collections.singletonList(new Doors(bathroom)));
            bathroom.addSensors(bathroomSensors);
            bathroom.addEquipment(bathroomEquipment);
        }

        User matilde = users.get(0);
        matilde.enterRoom(house, "kitchen");
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.print(".");
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.print(".");
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.print(".\n");
        house.getRoom("kitchen").get().getEquipment("cookers").set(true);
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.print(".");
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.print(".");
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.print(".\n");
        house.getRoom("kitchen").get().getSensor("kitcam1").sense(new Frame("smoke near cooker"));
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.print(".");
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.print(".");
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.print(".\n");
        house.getRoom("kitchen").get().getSensor("kithgd1").sense(new Air(5000.0, 150.0, .6));
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.print(".");
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.print(".");
        TimeUnit.MILLISECONDS.sleep(500);
        System.out.print(".\n");
        house.getRoom("kitchen").get().getSensor("kitmos1").sense(new Motion("FALL"));
    }
}
