package be.uclouvain.lingi2252.groupN.interpreter;

import be.uclouvain.lingi2252.groupN.parameterization.ModelChecker;
import be.uclouvain.lingi2252.groupN.warningsystem.AirQualityTester;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmStatus;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmSystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;

public class Interpreter {
    private static final Interpreter SINGLE_INSTANCE = new Interpreter();
    private Map<String, String> features;
    private Map<String, String> methods;
    private Scanner sc;

    private Interpreter() {
        sc = new Scanner(System.in).useLocale(Locale.US);

        features = new HashMap<>();
        features.put("Exit", "0");
        features.put("Change the actual temperature in the house", "1");
        features.put("Change the desired temperature in the house", "2");
        features.put("Arm or disarm the alarm system", "3");
        features.put("Change the air quality thresholds", "4");
        features.put("Change the emergency contacts", "5");
        features.put("Add a new room", "6");
        features.put("Add a new sensor to a room", "7");
        features.put("Add a new actuator to a room", "8");
        features.put("Remove a room", "9");
        features.put("Remove a sensor from a room", "10");
        features.put("Remove an actuator from a room", "11");

        methods = new HashMap<>();
        methods.put("1", "changeActualTemp");
        methods.put("2", "changeDesiredTemp");
        methods.put("3", "changeAlarmStatus");
        methods.put("4", "changeThresholds");
        methods.put("5", "changeContacts");
        methods.put("6", "addRoom");
        methods.put("7", "addSensor");
        methods.put("8", "addEquipment");
        methods.put("9", "removeRoom");
        methods.put("10", "removeSensor");
        methods.put("11", "removeEquipment");
    }

    public static Interpreter getInstance() {
        return SINGLE_INSTANCE;
    }

    public String welcome(Map<String, String> subFeatures) {
        StringBuilder res = new StringBuilder();
        res.append("Type a number:\n");

        for (String feature : subFeatures.keySet()) {
            res.append(subFeatures.get(feature)).append(". ");
            res.append(feature).append("\n");
        }

        return res.toString();
    }

    public Map<String, String> checkFeatures() {
        Map<String, String> subFeatures = new HashMap<>(features);

        if (!AlarmSystem.isEnabled()) subFeatures.remove("Arm or disarm the alarm system");
        if (!AirQualityTester.isEnabled()) subFeatures.remove("Change the air quality thresholds");
        if (!AlarmSystem.isEnabled() && !AirQualityTester.isEnabled())
            subFeatures.remove("Change the emergency contacts");

        return subFeatures;
    }

    private String input() {
        String line = sc.nextLine();
        try (Scanner reader = new Scanner(line)) {
            if (line.equals("")) return reader.nextLine();
            return line;
        }
    }

    public void interpret() {
        Map<String, String> availableFeatures = checkFeatures();
        System.out.println(welcome(availableFeatures));

        String choice = input();

        if (choice.equals("0")) return;

        try {
            Method method = this.getClass().getDeclaredMethod(methods.get(choice));
            method.invoke(this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException | NullPointerException e) {
            System.out.println("This choice is not available.");
        }

        interpret();
    }

    /**
     * @param object : only sensors and actuators
     */
    public boolean checkAddIntegrity(Object object) {
        ModelChecker.getInstance().addFeature(object.getClass().getSimpleName());

        if (ModelChecker.getInstance().checkFeatures()) {
            return true;
        } else {
            ModelChecker.getInstance().removeFeature(object.getClass().getSimpleName());
            return false;
        }
    }

    /**
     * @param object : only sensors and actuators
     */
    public boolean checkRemoveIntegrity(Object object) {
        ModelChecker.getInstance().removeFeature(object.getClass().getSimpleName());

        if (ModelChecker.getInstance().checkFeatures()) {
            return true;
        } else {
            ModelChecker.getInstance().addFeature(object.getClass().getSimpleName());
            return false;
        }
    }

    private void changeActualTemp() {
        System.out.println("What is the temperature inside the house?");

        String temp = input();

        System.out.println(CommandReceiver.getInstance().receiveCommand("changeActualTemp " + temp));
    }

    private void changeDesiredTemp() {
        System.out.println("What is the minimum temperature you want inside the house?");
        String minTemp = input();

        System.out.println("What is the maximum temperature you want inside the house?");
        String maxTemp = input();

        System.out.println(CommandReceiver.getInstance().receiveCommand("changeDesiredTemp " + minTemp + " " + maxTemp));
    }

    private void changeAlarmStatus() {
        AlarmStatus status = AlarmSystem.getInstance().getStatus();
        System.out.print("The alarm system is currently " + status.getDescription() + ". ");
        System.out.println("Do you want to change its status? (Y/N)");

        String yesOrNo = input();

        switch (yesOrNo.toLowerCase()) {
            case "y":
            case "yes":
                System.out.println("What is the new status of the alarm?");
                Object newStatus = input();
                System.out.println(CommandReceiver.getInstance().receiveCommand("changeAlarmStatus " + newStatus));
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
        String humidityThreshold = input();

        System.out.println("Current fine particles threshold: " + airQT.getFineParticlesThreshold());
        System.out.print("New threshold: ");
        String fineParticlesThreshold = input();

        System.out.println("Current harmful gas threshold: " + airQT.getHarmfulGasThreshold());
        System.out.print("New threshold: ");
        String harmfulGasThreshold = input();

        System.out.println(CommandReceiver.getInstance().receiveCommand("changeThresholds " + humidityThreshold + " " + fineParticlesThreshold + " " + harmfulGasThreshold));
    }

    private void changeContacts() {
        System.out.println("For which emergency should these contacts be called?");
        String reason = input();

        System.out.println("Type all the contacts that should be called for this emergency, separated with spaces:");
        String contacts = input();

        System.out.println(CommandReceiver.getInstance().receiveCommand("changeContacts " + reason + " " + contacts));
    }

    private void addRoom() {
        System.out.println("What is the name of the room you want to add?");
        String roomName = input();

        System.out.println(CommandReceiver.getInstance().receiveCommand("addRoom " + roomName));
    }

    private void removeRoom() {
        System.out.println("What is the name of the room you want to remove?");
        String roomName = input();

        System.out.println(CommandReceiver.getInstance().receiveCommand("removeRoom " + roomName));
    }

    private void addSensor() {
        System.out.println("Where do you want to add a sensor?");
        String roomName = input();

        System.out.println("What is the type of sensor you want to add?");
        String sensorName = input();

        System.out.println(CommandReceiver.getInstance().receiveCommand("addSensor " + roomName + " " + sensorName));
    }

    private void removeSensor() {
        System.out.println("Where do you want to remove a sensor?");
        String roomName = input();

        System.out.println("What is the name of the sensor you want to remove?");
        String sensorName = input();

        System.out.println(CommandReceiver.getInstance().receiveCommand("removeSensor " + roomName + " " + sensorName));
    }

    private void addEquipment() {
        System.out.println("Where do you want to add actuators?");
        String roomName = input();

        System.out.println("What is the type of actuator you want to add?");
        String actuatorName = input();

        System.out.println(CommandReceiver.getInstance().receiveCommand("addActuator " + roomName + " " + actuatorName));
    }

    private void removeEquipment() {
        System.out.println("Where do you want to remove an actuator?");
        String roomName = input();

        System.out.println("What is the name of the actuator you want to remove?");
        String actuatorName = input();

        System.out.println(CommandReceiver.getInstance().receiveCommand("removeEquipment " + roomName + " " + actuatorName));
    }
}
