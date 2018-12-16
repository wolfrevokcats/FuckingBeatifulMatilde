package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.actuators.*;
import be.uclouvain.lingi2252.groupN.parameterization.ModelChecker;
import be.uclouvain.lingi2252.groupN.parameterization.Parameterization;
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
        features.put("Add new actuators to a room", 8);

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
        Scanner reader = null;
        try {
            String line = sc.nextLine();
            reader = new Scanner(line);
            switch (inputType) {
                case "string":
                    if (line.equals("")) return reader.nextLine();
                    return line;
                case "int":
                    return reader.nextInt();
                case "double":
                    return reader.nextDouble();
                default:
                    return null;
            }
        } catch (InputMismatchException e) {
            return null;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }

    }

    public void interpret() {
        Map<String, Integer> availableFeatures = checkFeatures();
        System.out.println(welcome(availableFeatures));

        Object choice = input("int");

        if (choice == null) {
            System.out.println("This is not a valid option, please try again.");
            interpret();
            return;
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

    private boolean checkIntegrity(Object object) {
        ModelChecker.getInstance().addFeature(object.getClass().getSimpleName());

        if (ModelChecker.getInstance().checkFeatures()) {
            return true;
        } else {
            ModelChecker.getInstance().removeFeature(object.getClass().getSimpleName());
            return false;
        }
    }

    private boolean checkIntegrity(List<Object> objects) {
        return objects.stream()
                .anyMatch(object -> !checkIntegrity(object));
    }

    private void changeActualTemp() {
        System.out.println("What is the temperature inside the house?");

        Object temp = input("double");

        if (temp == null) {
            System.out.println("This is not a valid temperature, please try again.");
            changeActualTemp();
            return;
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
            changeDesiredTemp();
            return;
        }

        System.out.println("What is the maximum temperature you want inside the house?");
        Object maxTemp = input("double");

        if (maxTemp == null) {
            System.out.println("This is not a valid temperature, please try again.");
            changeDesiredTemp();
            return;
        }

        House.getInstance().getRooms().stream()
                .map(Room::getActuatorList)
                .flatMap(Collection::stream)
                .filter(equipment -> equipment instanceof Heaters || equipment instanceof Fireplaces)
                .forEach(equipment -> ((TemperatureControl) equipment).setTargetTemp((double) minTemp, (double) minTemp + 1));

        House.getInstance().getRooms().stream()
                .map(Room::getActuatorList)
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
            return;
        }

        switch (((String) yesOrNo).toLowerCase()) {
            case "y":
            case "yes":
                System.out.println("What is the new status of the alarm?");
                Object newStatus = input("string");
                if (newStatus == null) {
                    System.out.println("This is not a valid answer, please try again.");
                    changeAlarmStatus();
                    return;
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
                        return;
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
            return;
        }
        airQT.setHumidityThreshold((double) humidityThreshold);

        System.out.println("Current fine particles threshold: " + airQT.getFineParticlesThreshold());
        System.out.print("New threshold: ");
        Object fineParticlesThreshold = input("double");
        if (fineParticlesThreshold == null) {
            System.out.println("This is not a valid threshold, please try again.");
            changeThresholds();
            return;
        }
        airQT.setFineParticlesThreshold((double) fineParticlesThreshold);

        System.out.println("Current harmful gas threshold: " + airQT.getHarmfulGasThreshold());
        System.out.print("New threshold: ");
        Object harmfulGasThreshold = input("double");
        if (harmfulGasThreshold == null) {
            System.out.println("This is not a valid threshold, please try again.");
            changeThresholds();
            return;
        }
        airQT.setHarmfulGasThreshold((double) harmfulGasThreshold);
    }

    private void changeContacts() {
        System.out.println("How many contacts do you wish to add?");
        Object nbContacts = input("int");

        if (nbContacts == null) {
            System.out.println("This is not a valid number, please try again.");
            changeContacts();
            return;
        }

        System.out.println("For which emergency should these contacts be called?");
        Object reason = input("string");

        if (reason == null) {
            System.out.println("This is not a valid reason, please try again.");
            changeContacts();
            return;
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
        Object roomName = input("string");

        if (roomName == null) {
            System.out.println("This is not a valid name, please try again.");
            addRoom();
            return;
        }

        House.getInstance().addRoom(new Room((String) roomName));
    }

    private void addSensors() {
        System.out.println("Where do you want to add sensors?");
        Object roomName = input("string");

        if (roomName == null) {
            System.out.println("This is not a valid name, please try again.");
            addSensors();
            return;
        }

        Room room = House.getInstance().getRoom((String) roomName);
        if (room == null) {
            System.out.println("This room does not exist, please try again.");
            addSensors();
            return;
        }

        System.out.println("How many sensors do you want to add?");
        Object nbSensors = input("int");

        if (nbSensors == null) {
            System.out.println("This is not a valid number, please try again.");
            addSensors();
            return;
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

        if (checkIntegrity(sensors)) House.getInstance().getRoom((String) roomName).addSensors(sensors);
        else System.out.println("This cannot be added.");
    }

    private void addEquipment() {
        System.out.println("Where do you want to add actuators?");
        Object roomName = input("string");

        if (roomName == null) {
            System.out.println("This is not a valid name, please try again.");
            addEquipment();
            return;
        }

        Room room = House.getInstance().getRoom((String) roomName);
        if (room == null) {
            System.out.println("This room does not exist, please try again.");
            addEquipment();
            return;
        }

        System.out.println("How many actuators do you want to add?");
        Object nbEquipment = input("int");

        if (nbEquipment == null) {
            System.out.println("This is not a valid number, please try again.");
            addEquipment();
            return;
        }

        List<Actuator> actuatorList = new ArrayList<>();
        for (int i = 0; i < (int) nbEquipment; i++) {
            System.out.print("Type of actuators no." + (i + 1) + ": ");
            Object equipmentName = input("string");

            String classPath = "be.uclouvain.lingi2252.groupN.actuators." + Parameterization.toClassName((String) equipmentName);
            try {
                Class<?> clazz = Class.forName(classPath);
                Constructor<?> ctor = clazz.getConstructor(Room.class);
                Actuator actuator = (Actuator) ctor.newInstance(room);
                actuatorList.add(actuator);
            } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | InstantiationException | IllegalAccessException e) {
                System.out.println("[" + equipmentName + "] does not exist as an actuator, please try again.");
                i--;
            }
        }

        if (checkIntegrity(actuatorList)) House.getInstance().getRoom((String) roomName).addEquipment(actuatorList);
        else System.out.println("This cannot be added.");
    }
}
