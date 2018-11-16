package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.equipment.*;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;
import be.uclouvain.lingi2252.groupN.sensors.TemperatureSensor;
import be.uclouvain.lingi2252.groupN.signals.Temperature;
import be.uclouvain.lingi2252.groupN.warningsystem.AirQualityTester;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmSystem;
import be.uclouvain.lingi2252.groupN.warningsystem.WarningSystem;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static java.lang.Math.toIntExact;

public class Interpreter {
    private static final Interpreter SINGLE_INSTANCE = new Interpreter();
    private Map<String, Integer> features;
    private Map<Integer, String> methods;
    private Scanner sc;

    private Interpreter() {
        sc = new Scanner(System.in).useLocale(Locale.US);

        features = new HashMap<>();
        features.put("Exit", 0);
        features.put("Change the actual temperature in the house", 1);
        features.put("Change the desired temperature in the house", 2);
        features.put("Arm or disarm the alarm System", 3);
        features.put("Change the air quality thresholds", 4);
        features.put("Change the emergency contacts", 5);
        features.put("Add a new room", 6);
        features.put("Add new sensors to a room", 7);
        features.put("Add new equipment to a room", 8);

        methods = new HashMap<>();
        methods.put(1, "changeActualTemp");
        methods.put(2, "changeDesiredTemp");
        methods.put(3, "changeAlarmStatus");
        methods.put(4, "changeThresholds");
        methods.put(5, "changeContacts");
        methods.put(6, "addRoom");
        methods.put(7, "addSensors");
        methods.put(8, "addEquipment");
    }

    public static Interpreter getInstance() {
        return SINGLE_INSTANCE;
    }

    private String welcome(Map<String, Integer> subFeatures) {
        StringBuilder res = new StringBuilder();
        res.append("Type a number:\n");

        for (String feature : subFeatures.keySet()) {
            res.append(subFeatures.get(feature)).append(". ");
            res.append(feature).append("\n");
        }

        return res.toString();
    }

    private Map<String, Integer> checkFeatures() {
        Map<String, Integer> subFeatures = new HashMap<>(features);

        if (!AlarmSystem.isEnabled()) subFeatures.remove("Arm or disarm the alarm System");
        if (!AirQualityTester.isEnabled()) subFeatures.remove("Change the air quality thresholds");
        if (!AlarmSystem.isEnabled() && !AirQualityTester.isEnabled())
            subFeatures.remove("Change the emergency contacts");

        return subFeatures;
    }

    public void interpret() {
        Map<String, Integer> availableFeatures = checkFeatures();
        System.out.println(welcome(availableFeatures));

        int choice = sc.nextInt();

        if (choice == 0) return;

        try {
            Method method = this.getClass().getDeclaredMethod(methods.get(choice));
            method.invoke(this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        interpret();
    }

    private void changeActualTemp() {
        System.out.println("What is the temperature inside the house?");

        double temp = sc.nextDouble();

        House.getInstance().getRooms().stream()
                .filter(room -> !room.getName().equals("garden"))
                .map(Room::getSensors)
                .flatMap(Collection::stream)
                .filter(sensor -> sensor instanceof TemperatureSensor)
                .forEach(sensor -> sensor.sense(new Temperature(temp)));
    }

    private void changeDesiredTemp() {
        System.out.println("What is the minimum temperature you want inside the house?");
        double minTemp = sc.nextDouble();

        System.out.println("What is the maximum temperature you want inside the house?");
        double maxTemp = sc.nextDouble();

        House.getInstance().getRooms().stream()
                .map(Room::getEquipmentList)
                .flatMap(Collection::stream)
                .filter(equipment -> equipment instanceof Heaters || equipment instanceof Fireplaces)
                .forEach(equipment -> ((TemperatureControl) equipment).setTargetTemp(minTemp, minTemp + 1));

        House.getInstance().getRooms().stream()
                .map(Room::getEquipmentList)
                .flatMap(Collection::stream)
                .filter(equipment -> equipment instanceof Conditioners)
                .forEach(equipment -> ((TemperatureControl) equipment).setTargetTemp(maxTemp - 1, maxTemp));
    }

    private void changeAlarmStatus() {
        boolean armed = AlarmSystem.getInstance().isArmed();
        System.out.println("The alarm system is currently " + (armed ? "" : "dis") + "armed. Do you want to " + (armed ? "dis" : "") + "arm it? (Y/N)");

        String yesOrNo = sc.nextLine();

        if (yesOrNo.equals("")) yesOrNo = sc.nextLine();

        switch (yesOrNo.toLowerCase()) {
            case "y":
            case "yes":
                AlarmSystem.getInstance().setEngaged(!armed);
                break;
            case "n":
            case "no":
                System.out.println("Fine. The alarm system is still" + (armed ? "" : "dis") + "armed.");
                break;
            default:
                System.out.println("Unknown command. Please try again.");
                changeAlarmStatus();
                break;
        }
    }

    private void changeThresholds() {
        AirQualityTester airQT = AirQualityTester.getInstance();

        System.out.println("Current humidity threshold: " + airQT.getHumidityThreshold());
        System.out.print("New threshold: ");
        Double humidityThreshold = sc.nextDouble();
        airQT.setHumidityThreshold(humidityThreshold);

        System.out.println("Current fine particles threshold: " + airQT.getFineParticlesThreshold());
        System.out.print("New threshold: ");
        Double fineParticlesThreshold = sc.nextDouble();
        airQT.setFineParticlesThreshold(fineParticlesThreshold);

        System.out.println("Current harmful gas threshold: " + airQT.getHarmfulGasThreshold());
        System.out.print("New threshold: ");
        Double harmfulGasThreshold = sc.nextDouble();
        airQT.setHarmfulGasThreshold(harmfulGasThreshold);
    }

    private void changeContacts() {
        System.out.println("How many contacts do you wish to add?");
        int nbContacts = sc.nextInt();

        System.out.println("For which emergency should these contacts be called?");
        String reason = sc.nextLine();
        if (reason.equals("")) reason = sc.nextLine();

        List<String> contacts = new ArrayList<>();
        for (int i = 0; i < nbContacts; i++) {
            System.out.print("Type information for contact no." + (i + 1) + ": ");
            contacts.add(sc.nextLine());
        }

        WarningSystem.addOrReplaceContact(reason, contacts);
    }

    private void addRoom() {
        System.out.println("What is the name of the room you want to add?");
        String roomName = sc.nextLine();
        if (roomName.equals("")) roomName = sc.nextLine();

        House.getInstance().addRoom(new Room(roomName));
    }

    private void addSensors() {
        System.out.println("Where do you want to add sensors?");
        String roomName = sc.nextLine();
        if (roomName.equals("")) roomName = sc.nextLine();

        Room room = House.getInstance().getRoom(roomName);
        if (room == null) {
            System.out.println("This room does not exist, please try again.");
            addSensors();
        } else {
            System.out.println("How many sensors do you want to add?");
            int nbSensors = sc.nextInt();

            List<Sensor> sensors = new ArrayList<>();
            for (int i = 0; i < nbSensors; i++) {
                System.out.print("Type of sensor no." + (i + 1) + ": ");
                String sensorName = sc.nextLine();
                if (sensorName.equals("")) sensorName = sc.nextLine();

                String classPath = "be.uclouvain.lingi2252.groupN.sensors." + Parameterization.toClassName(sensorName);
                try {
                    Class<?> clazz = Class.forName(classPath);
                    Constructor<?> ctor = clazz.getConstructor(String.class, CommunicationHub.class);
                    int sensorId = toIntExact(room.getSensors().stream()
                            .filter(clazz::isInstance)
                            .count());
                    Sensor sensor = (Sensor) ctor.newInstance(room.getName() + '_' + sensorName + '_' + sensorId, room.getCommHub());
                    sensors.add(sensor);
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    System.out.println("[" + sensorName + "] does not exist as a sensor, please try again.");
                    i--;
                }
            }
            House.getInstance().getRoom(roomName).addSensors(sensors);
        }
    }

    private void addEquipment() {
        System.out.println("Where do you want to add equipment?");
        String roomName = sc.nextLine();
        if (roomName.equals("")) roomName = sc.nextLine();

        Room room = House.getInstance().getRoom(roomName);
        if (room == null) {
            System.out.println("This room does not exist, please try again.");
            addEquipment();
        } else {
            System.out.println("How much equipment do you want to add?");
            int nbEquipment = sc.nextInt();

            List<Equipment> equipmentList = new ArrayList<>();
            for (int i = 0; i < nbEquipment; i++) {
                System.out.print("Type of equipment no." + (i + 1) + ": ");
                String equipmentName = sc.nextLine();
                if (equipmentName.equals("")) equipmentName = sc.nextLine();

                String classPath = "be.uclouvain.lingi2252.groupN.equipment." + Parameterization.toClassName(equipmentName);
                try {
                    Class<?> clazz = Class.forName(classPath);
                    Constructor<?> ctor = clazz.getConstructor(Room.class);
                    Equipment equipment = (Equipment) ctor.newInstance(room);
                    equipmentList.add(equipment);
                } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                    System.out.println("[" + equipmentName + "] does not exist as an equipment, please try again.");
                    i--;
                }
            }
            House.getInstance().getRoom(roomName).addEquipment(equipmentList);
        }
    }
}
