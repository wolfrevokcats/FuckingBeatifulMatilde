package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.sensors.AirSensor;
import be.uclouvain.lingi2252.groupN.sensors.Camera;
import be.uclouvain.lingi2252.groupN.sensors.MotionSensor;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Parameterization {
    public static House newHouse(String filepath) {
        File jsonFile = new File(filepath);

        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject mainFile = (JSONObject) jsonParser.parse(new FileReader(jsonFile));

            // Extract lists of Users and Owners from .json file
            JSONObject jsonUsers = (JSONObject) mainFile.get("users");
            List<User> users = new ArrayList<>();
            List<User> owners = new ArrayList<>();

            // Fill the list of Users. Each object here are of type User
            for (Object o : jsonUsers.keySet()) {
                String key = (String) o;
                User user = new User(key);
                users.add(user);

                // Fining the owner inside the users list (assigned int = 0)
                int userStatus = (int) jsonUsers.get(key);
                if (userStatus == 0) {
                    owners.add(user);
                }
            }

            // Populate the house with the list of owners
            House house = new House(owners);
            house.addUsers(users);

            // Extract lists of Rooms from .json file
            JSONObject jsonRooms = (JSONObject) mainFile.get("rooms");
            List<Room> rooms = new ArrayList<>();

            // Populate the room list with the ones present in the .json file
            for (Object roomObj : jsonRooms.keySet()) {
                String roomKey = (String) roomObj;
                Room room = new Room(house, roomKey);
                rooms.add(room);

                JSONObject jsonRoom = (JSONObject) jsonRooms.get(roomKey);

                // Populate each room with the corresponding sensors
                JSONObject jsonSensors = (JSONObject) jsonRoom.get("sensors");
                for (Object sensorObj : jsonSensors.keySet()) {
                    String sensorKey = (String) sensorObj;
                    int nbSensors = (int) jsonSensors.get(sensorKey);

                    List<Sensor> sensors = new ArrayList<>();

                    // Connect each sensor to the Communication Hub
                    switch (sensorKey) {
                        case "cameras":
                            for (int i = 0; i < nbSensors; i++) {
                                sensors.add(new Camera(roomKey + '_' + sensorKey + '_' + i, room.getCommHub()));
                            }
                            break;
                        case "air_sensor":
                            for (int i = 0; i < nbSensors; i++) {
                                sensors.add(new AirSensor(roomKey + '_' + sensorKey + '_' + i, room.getCommHub()));
                            }
                            break;
                        case "motion_sensors":
                            for (int i = 0; i < nbSensors; i++) {
                                sensors.add(new MotionSensor(roomKey + '_' + sensorKey + '_' + i, room.getCommHub()));
                            }
                            break;
                        default:
                            System.out.println("Sensor doesn't exist or isn't implemented yet!");
                            break;
                    }

                    room.addSensors(sensors);
                }

                JSONArray jsonEquipment = (JSONArray) jsonRoom.get("equipment");
                for (Object equipmentObj : jsonEquipment) {

                }
            }

            house.addRooms(rooms);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
            return null;
        }
    }
}
