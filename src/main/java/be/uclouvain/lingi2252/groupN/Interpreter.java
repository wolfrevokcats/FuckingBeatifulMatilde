package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.equipment.Conditioners;
import be.uclouvain.lingi2252.groupN.equipment.Fireplaces;
import be.uclouvain.lingi2252.groupN.equipment.Heaters;
import be.uclouvain.lingi2252.groupN.equipment.TemperatureControl;
import be.uclouvain.lingi2252.groupN.sensors.TemperatureSensor;
import be.uclouvain.lingi2252.groupN.signals.Temperature;
import be.uclouvain.lingi2252.groupN.warningsystem.AirQualityTester;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmSystem;
import be.uclouvain.lingi2252.groupN.warningsystem.WarningSystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

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
        features.put("Add a new sensor", 7);
        features.put("Add a new equipment", 8);

        methods = new HashMap<>();
        methods.put(1, "changeActualTemp");
        methods.put(2, "changeDesiredTemp");
        methods.put(3, "changeAlarmStatus");
        methods.put(4, "changeThresholds");
        methods.put(5, "changeContacts");
        methods.put(6, "addRoom");
        methods.put(7, "addSensor");
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
        if (!AlarmSystem.isEnabled() && !AirQualityTester.isEnabled()) subFeatures.remove("Change the emergency contacts");

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
            System.out.print("Type information for contact no." + i + ": ");
            contacts.add(sc.nextLine());
        }

        WarningSystem.addOrReplaceContact(reason, contacts);
    }

    private void addRoom() {
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
    }

    private void addSensor() {
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
    }

    private void addEquipment() {
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
    }
}
