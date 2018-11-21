package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.equipment.*;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;
import be.uclouvain.lingi2252.groupN.sensors.TemperatureSensor;
import be.uclouvain.lingi2252.groupN.signals.Temperature;
import be.uclouvain.lingi2252.groupN.warningsystem.AirQualityTester;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmStatus;
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

    public String welcome(Map<String, Integer> subFeatures) {
        StringBuilder res = new StringBuilder();
        res.append("Type a number:\n");

        for (String feature : subFeatures.keySet()) {
            res.append(subFeatures.get(feature)).append(". ");
            res.append(feature).append("\n");
        }

        return res.toString();
    }

    public Map<String, Integer> checkFeatures() {
        Map<String, Integer> subFeatures = new HashMap<>(features);

        if (!AlarmSystem.isEnabled()) subFeatures.remove("Arm or disarm the alarm System");
        if (!AirQualityTester.isEnabled()) subFeatures.remove("Change the air quality thresholds");
        if (!AlarmSystem.isEnabled() && !AirQualityTester.isEnabled())
            subFeatures.remove("Change the emergency contacts");

        return subFeatures;
    }

    private Object input(String inputType) {
        try {
            String line = sc.nextLine();
            Scanner reader = new Scanner(line);
            switch (inputType) {
                case "string":
                    String str = sc.nextLine();
                    if (str.equals("")) return sc.nextLine();
                    return str;
                case "int":
                    return reader.nextInt();
                case "double":
                    return reader.nextDouble();
                default:
                    return null;
            }
        } catch (InputMismatchException e) {
            return null;
        }

    }

    public void interpret() {
        Map<String, Integer> availableFeatures = checkFeatures();
        System.out.println(welcome(availableFeatures));

        Object choice = input("int");

        if (choice == null) {
            System.out.println("This is not a valid option, please try again.");
            interpret();
        }

        if ((int) choice == 0) return;

        try {
            Method method = this.getClass().getDeclaredMethod(methods.get((int) choice));
            method.invoke(this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }

        interpret();
    }

    private void changeActualTemp() {
        System.out.println("What is the temperature inside the house?");

        Object temp = input("double");

        if (temp == null) {
            System.out.println("This is not a valid temperature, please try again.");
            changeActualTemp();
        }

        House.getInstance().getRooms().stream()
                .filter(room -> !room.getName().equals("garden"))
                .map(Room::getSensors)
                .flatMap(Collection::stream)
                .filter(sensor -> sensor instanceof TemperatureSensor)
                .forEach(sensor -> sensor.sense(new Temperature((double) temp)));
    }

    private void changeDesiredTemp() {
        System.out.println("What is the minimum temperature you want inside the house?");
        Object minTemp = input("double");

        if (minTemp == null) {
            System.out.println("This is not a valid temperature, please try again.");
            changeActualTemp();
        }

        System.out.println("What is the maximum temperature you want inside the house?");
        Object maxTemp = input("double");

        if (maxTemp == null) {
            System.out.println("This is not a valid temperature, please try again.");
            changeActualTemp();
        }

        House.getInstance().getRooms().stream()
                .map(Room::getEquipmentList)
                .flatMap(Collection::stream)
                .filter(equipment -> equipment instanceof Heaters || equipment instanceof Fireplaces)
                .forEach(equipment -> ((TemperatureControl) equipment).setTargetTemp((double) minTemp, (double) minTemp + 1));

        House.getInstance().getRooms().stream()
                .map(Room::getEquipmentList)
                .flatMap(Collection::stream)
                .filter(equipment -> equipment instanceof Conditioners)
                .forEach(equipment -> ((TemperatureControl) equipment).setTargetTemp((double) maxTemp - 1, (double) maxTemp));
    }

    private void changeAlarmStatus() {
        AlarmStatus status = AlarmSystem.getInstance().getStatus();
        System.out.print("The alarm system is currently " + status.getDescription() + ".");
        System.out.println("Do you want to change this status? (Y/N)");

        Object yesOrNo = input("string");

        if (yesOrNo == null) {
            System.out.println("This is not a valid answer, please try again.");
            changeAlarmStatus();
        }

        switch (((String) yesOrNo).toLowerCase()) {
            case "y":
            case "yes":
                System.out.println("What is the new status of the alarm?");
                Object newStatus = input("string");
                if (newStatus == null) {
                    System.out.println("This is not a valid answer, please try again.");
                    changeAlarmStatus();
                }
                switch (((String) newStatus).toLowerCase()) {
                    case "armed":
                        AlarmSystem.getInstance().setStatus(AlarmStatus.ARMED);
                        break;
                    case "disarmed":
                        AlarmSystem.getInstance().setStatus(AlarmStatus.DISARMED);
                        break;
                    case "presence":
                        AlarmSystem.getInstance().setStatus(AlarmStatus.PRESENCE);
                        break;
                    default:
                        System.out.println("This is not a valid status, please try again.");
                        changeAlarmStatus();
                        break;
                }

                break;
            case "n":
            case "no":
                System.out.println("Fine. The alarm system is still " + status.getDescription() + ".");
                break;
            default:
                System.out.println("This is not a valid answer, please try again.");
                changeAlarmStatus();
                break;
        }
    }

    private void changeThresholds() {
        AirQualityTester airQT = AirQualityTester.getInstance();

        System.out.println("Current humidity threshold: " + airQT.getHumidityThreshold());
        System.out.print("New threshold: ");
        Object humidityThreshold = input("double");
        if (humidityThreshold == null) {
            System.out.println("This is not a valid threshold, please try again.");
            changeThresholds();
        }
        airQT.setHumidityThreshold((double) humidityThreshold);

        System.out.println("Current fine particles threshold: " + airQT.getFineParticlesThreshold());
        System.out.print("New threshold: ");
        Object fineParticlesThreshold = input("double");
        if (fineParticlesThreshold == null) {
            System.out.println("This is not a valid threshold, please try again.");
            changeThresholds();
        }
        airQT.setFineParticlesThreshold((double) fineParticlesThreshold);

        System.out.println("Current harmful gas threshold: " + airQT.getHarmfulGasThreshold());
        System.out.print("New threshold: ");
        Object harmfulGasThreshold = input("double");
        if (harmfulGasThreshold == null) {
            System.out.println("This is not a valid threshold, please try again.");
            changeThresholds();
        }
        airQT.setHarmfulGasThreshold((double) harmfulGasThreshold);
    }

    private void changeContacts() {
        System.out.println("How many contacts do you wish to add?");
        Object nbContacts = input("int");

        if (nbContacts == null) {
            System.out.println("This is not a valid number, please try again.");
            changeContacts();
        }

        System.out.println("For which emergency should these contacts be called?");
        Object reason = input("string");

        if (reason == null) {
            System.out.println("This is not a valid reason, please try again.");
            changeContacts();
        }

        List<String> contacts = new ArrayList<>();
        for (int i = 0; i < (int) nbContacts; i++) {
            System.out.print("Type information for contact no." + (i + 1) + ": ");
            contacts.add((String) input("string"));
        }

        WarningSystem.addOrReplaceContact((String) reason, contacts);
    }

    private void addRoom() {
        System.out.println("What is the name of the room you want to add?");
        Object roomName = input("String");

        if (roomName == null) {
            System.out.println("This is not a valid name, please try again.");
            addRoom();
        }

        House.getInstance().addRoom(new Room((String) roomName));
    }

    private void addSensors() {
        System.out.println("Where do you want to add sensors?");
        Object roomName = input("String");

        if (roomName == null) {
            System.out.println("This is not a valid name, please try again.");
            addSensors();
        }

        Room room = House.getInstance().getRoom((String) roomName);
        if (room == null) {
            System.out.println("This room does not exist, please try again.");
            addSensors();
        }

        System.out.println("How many sensors do you want to add?");
        Object nbSensors = input("int");

        if (nbSensors == null) {
            System.out.println("This is not a valid number, please try again.");
            addSensors();
        }

        List<Sensor> sensors = new ArrayList<>();
        for (int i = 0; i < (int) nbSensors; i++) {
            System.out.print("Type of sensor no." + (i + 1) + ": ");
            Object sensorName = input("string");

            String classPath = "be.uclouvain.lingi2252.groupN.sensors." + Parameterization.toClassName((String) sensorName);
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
        House.getInstance().getRoom((String) roomName).addSensors(sensors);
    }

    private void addEquipment() {
        System.out.println("Where do you want to add equipment?");
        Object roomName = input("String");

        if (roomName == null) {
            System.out.println("This is not a valid name, please try again.");
            addEquipment();
        }

        Room room = House.getInstance().getRoom((String) roomName);
        if (room == null) {
            System.out.println("This room does not exist, please try again.");
            addEquipment();
        }

        System.out.println("How much equipment do you want to add?");
        Object nbEquipment = input("int");

        if (nbEquipment == null) {
            System.out.println("This is not a valid number, please try again.");
            addEquipment();
        }

        List<Equipment> equipmentList = new ArrayList<>();
        for (int i = 0; i < (int) nbEquipment; i++) {
            System.out.print("Type of equipment no." + (i + 1) + ": ");
            Object equipmentName = input("string");

            String classPath = "be.uclouvain.lingi2252.groupN.equipment." + Parameterization.toClassName((String) equipmentName);
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
        House.getInstance().getRoom((String) roomName).addEquipment(equipmentList);
    }
}
