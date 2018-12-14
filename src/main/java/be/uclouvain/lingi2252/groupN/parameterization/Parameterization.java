package be.uclouvain.lingi2252.groupN.parameterization;

import be.uclouvain.lingi2252.groupN.*;
import be.uclouvain.lingi2252.groupN.actuators.Actuator;
import be.uclouvain.lingi2252.groupN.procedures.ObjectTracker;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;
import be.uclouvain.lingi2252.groupN.warningsystem.AirQualityTester;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmSystem;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Math.toIntExact;

public class Parameterization {
    private static final Parameterization SINGLE_INSTANCE = new Parameterization();

    private Parameterization() {
    }

    public static Parameterization getInstance() {
        return SINGLE_INSTANCE;
    }

    public static String toClassName(String string) {
        String[] split;
        if (string.contains("_")) {
            split = string.split("_");
        } else {
            split = string.split(" ");
        }
        StringBuilder res = new StringBuilder();

        for (String aSplit : split) {
            res.append(aSplit.substring(0, 1).toUpperCase()).append(aSplit.substring(1));
        }

        return res.toString();
    }

    /**
     * Initializes the parametrization component with a given configuration
     *
     * @param filepath: a configuration file of JSON format
     */
    public void initialize(String filepath) {
        ModelChecker.getInstance().initialize();

        File jsonFile = new File(filepath);

        JSONParser jsonParser = new JSONParser();
        try {
            JSONObject mainFile = (JSONObject) jsonParser.parse(new FileReader(jsonFile));

            JSONObject jsonUsers = (JSONObject) mainFile.get("users");
            addUsersToHouse(jsonUsers);

            JSONObject jsonRooms = (JSONObject) mainFile.get("rooms");
            addRoomsToHouse(jsonRooms);

            JSONObject jsonFeatures = (JSONObject) mainFile.get("additional_features");
            addFeaturesToHouse(jsonFeatures, mainFile);

            if (!ModelChecker.getInstance().checkFeatures()) {
                throw new IllegalArgumentException("This file [" + filepath + "] is not compliant with the feature model!");
            }

        } catch (IOException | ParseException e) {
            throw new IllegalArgumentException("This file [" + filepath + "] does not exist or is not a valid JSON file!");
        }
    }

    private void addUsersToHouse(JSONObject jsonUsers) {
        List<User> users = new ArrayList<>();
        List<User> owners = new ArrayList<>();

        for (Object o : jsonUsers.keySet()) {
            String key = (String) o;
            User user = new User(key);
            users.add(user);

            int userStatus = toIntExact((long) jsonUsers.get(key));
            if (userStatus == 0) {
                owners.add(user);
            }
        }

        // Populate the house with the list of owners
        House house = House.getInstance();
        house.initialize(owners);
        house.addUsers(users);
    }

    private void addRoomsToHouse(JSONObject jsonRooms) {
        List<Room> rooms = new ArrayList<>();

        // Populate the room list with the ones present in the .json file
        for (Object roomObj : jsonRooms.keySet()) {
            String roomKey = (String) roomObj;
            Room room = new Room(roomKey);
            rooms.add(room);
            ModelChecker.getInstance().addFeature(roomKey);

            JSONObject jsonRoom = (JSONObject) jsonRooms.get(roomKey);

            // Populate each room with the corresponding sensors
            JSONObject jsonSensors = (JSONObject) jsonRoom.get("sensor");
            addSensorsToRoom(room, jsonSensors);

            // Populate each room with the corresponding sensors
            JSONArray jsonEquipment = (JSONArray) jsonRoom.get("equipment");
            addEquipmentToRoom(room, jsonEquipment);
        }

        House.getInstance().addRooms(rooms);
    }

    private void addSensorsToRoom(Room room, JSONObject jsonSensors) {
        if (jsonSensors == null) return;

        for (Object sensorObj : jsonSensors.keySet()) {
            String sensorKey = (String) sensorObj;
            int nbSensors = toIntExact((long) jsonSensors.get(sensorKey));

            List<Sensor> sensors = new ArrayList<>();

            String classPath = "be.uclouvain.lingi2252.groupN.sensors." + toClassName(sensorKey);

            try {
                Class<?> clazz = Class.forName(classPath);
                Constructor<?> ctor = clazz.getConstructor(String.class, CommunicationHub.class);
                for (int i = 0; i < nbSensors; i++) {
                    Sensor sensor = (Sensor) ctor.newInstance(room.getName() + '_' + sensorKey + '_' + i, room.getCommHub());
                    sensors.add(sensor);
                    ModelChecker.getInstance().addFeature(sensorKey);
                }
            } catch (ClassNotFoundException e) {
                System.out.println("Sensor [" + classPath + "] doesn't exist or isn't implemented yet!");
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
                System.out.println(sensorKey);
            }

            room.addSensors(sensors);
        }
    }

    private void addEquipmentToRoom(Room room, JSONArray jsonEquipment) {
        if (jsonEquipment == null) return;

        for (Object equipmentObj : jsonEquipment) {
            String equipmentKey = (String) equipmentObj;

            String classPath = "be.uclouvain.lingi2252.groupN.actuators." + toClassName(equipmentKey);

            try {
                Class<?> clazz = Class.forName(classPath);
                Constructor<?> ctor = clazz.getConstructor(Room.class);

                room.addEquipment((Actuator) ctor.newInstance(room));
                ModelChecker.getInstance().addFeature(equipmentKey);

            } catch (ClassNotFoundException e) {
                System.out.println("Actuator [" + classPath + "] doesn't exist or isn't implemented yet!");
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
    }

    private void addFeaturesToHouse(JSONObject jsonFeatures, JSONObject mainFile) {
        House house = House.getInstance();
        List<CommunicationHub> commHubs = house.getRooms().stream().map(Room::getCommHub).collect(Collectors.toList());

        CentralUnit.getInstance().initialize(commHubs);

        for (Object featureObj : jsonFeatures.keySet()) {
            String featureKey = (String) featureObj;
            boolean isPresent = (boolean) jsonFeatures.get(featureKey);

            if (isPresent) {
                ModelChecker.getInstance().addFeature(featureKey);
                switch (featureKey) {
                    case "air_quality_tester":
                        JSONObject jsonAirThresholds = (JSONObject) mainFile.get("air_quality_thresholds");
                        Double humidityThreshold = (Double) jsonAirThresholds.get("humidity");
                        Double fineParticlesThreshold = (Double) jsonAirThresholds.get("fine_particles");
                        Double harmfulGasThreshold = (Double) jsonAirThresholds.get("harmful_gas");
                        AirQualityTester.enable();
                        AirQualityTester.getInstance().initialize(commHubs, humidityThreshold, fineParticlesThreshold, harmfulGasThreshold);
                        break;
                    case "alarm_system":
                        AlarmSystem.enable();
                        AlarmSystem.getInstance().initialize(commHubs);
                        break;
                    case "smart_assistant":
                        SmartAssistant.enable();
                        break;
                    case "object_tracking":
                        ObjectTracker.enable();
                        break;
                    case "lock_down":
                        //ObjectTracker.enable();
                        break;
                    case "evacuation":
                        //ObjectTracker.enable();
                        break;
                    default:
                        System.out.println("Feature [" + featureKey + "] doesn't exist or isn't implemented yet!");
                        break;

                }
            }
        }
    }
}