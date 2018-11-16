package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.warningsystem.AirQualityTester;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmSystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class Interpreter {
    private static final Interpreter SINGLE_INSTANCE = new Interpreter();
    private Map<String, Integer> features;
    private Map<Integer, String> methods;

    private Interpreter() {
        features = new HashMap<>();
        features.put("Change the actual temperature in the house", 0);
        features.put("Change the desired temperature in the house", 1);
        features.put("Arm or disarm the alarm System", 2);
        features.put("Change the air quality thresholds", 3);
        features.put("Change the emergency contacts", 4);
        features.put("Add a new room", 5);
        features.put("Add a new sensor", 6);
        features.put("Add a new equipment", 7);

        methods = new HashMap<>();
        methods.put(0, "changeActualTemp");
        methods.put(1, "changeDesiredTemp");
        methods.put(2, "changeAlarmStatus");
        methods.put(3, "changeThresholds");
        methods.put(4, "changeContacts");
        methods.put(5, "addRoom");
        methods.put(6, "addSensor");
        methods.put(7, "addEquipment");
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

        Scanner sc = new Scanner(System.in);
        int choice = sc.nextInt();

        try {
            Method method = this.getClass().getDeclaredMethod(methods.get(choice));
            method.invoke(this);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    private void changeActualTemp() {
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
    }

    private void changeDesiredTemp() {
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
    }

    private void changeAlarmStatus() {
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
    }

    private void changeThresholds() {
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
    }

    private void changeContacts() {
        System.out.println(Arrays.toString(Thread.currentThread().getStackTrace()));
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
