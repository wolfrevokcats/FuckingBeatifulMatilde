package be.uclouvain.lingi2252.groupN.interpreter;

import be.uclouvain.lingi2252.groupN.House;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.Scenario;
import be.uclouvain.lingi2252.groupN.User;
import be.uclouvain.lingi2252.groupN.actuators.*;
import be.uclouvain.lingi2252.groupN.parameterization.ModelChecker;
import be.uclouvain.lingi2252.groupN.sensors.Sensor;
import be.uclouvain.lingi2252.groupN.sensors.SensorFactory;
import be.uclouvain.lingi2252.groupN.sensors.TemperatureSensor;
import be.uclouvain.lingi2252.groupN.signals.Temperature;
import be.uclouvain.lingi2252.groupN.warningsystem.AirQualityTester;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmStatus;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmSystem;
import be.uclouvain.lingi2252.groupN.warningsystem.WarningSystem;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CommandReceiver {
    private static final CommandReceiver SINGLE_INSTANCE = new CommandReceiver();

    private CommandReceiver() {

    }

    public static CommandReceiver getInstance() {
        return SINGLE_INSTANCE;
    }

    private Object callMethod(String sMethod, String... arguments) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        if (arguments.length == 1) {
            Method method = this.getClass().getDeclaredMethod(sMethod, String.class);
            return method.invoke(this, arguments[0]);
        } else {
            Class[] types = new Class[arguments.length];
            for (int i = 0; i < arguments.length; i++) {
                types[i] = String.class;
            }
            Method method = this.getClass().getDeclaredMethod(sMethod, types);
            return method.invoke(this, arguments);
        }
    }

    public String receiveCommand(String inputString) {
        String[] input = inputString.split(" ");
        String res = "This command is not supported.";
        try {
            res = (String) callMethod(input[0], Arrays.copyOfRange(input, 1, input.length));
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            System.out.println("Command: " + inputString);
        }
        return res;
    }

    private String changeActualTemp(String temp) {
        Double dTemp;
        try {
            dTemp = Double.parseDouble(temp);
        } catch (Exception e) {
            return "This is not a valid temperature";
        }

        House.getInstance().getRooms().stream()
                .filter(room -> !room.getName().equals("garden"))
                .map(Room::getSensors)
                .flatMap(Collection::stream)
                .filter(sensor -> sensor instanceof TemperatureSensor)
                .forEach(sensor -> sensor.sense(new Temperature(dTemp)));

        return "The actual temperature is now " + dTemp + " °C.";
    }

    private String changeDesiredTemp(String minTemp, String maxTemp) {
        Double dMinTemp, dMaxTemp;
        try {
            dMinTemp = Double.parseDouble(minTemp);
            dMaxTemp = Double.parseDouble(maxTemp);
        } catch (Exception e) {
            return "These are not valid temperatures";
        }

        House.getInstance().getRooms().stream()
                .map(Room::getActuatorList)
                .flatMap(Collection::stream)
                .filter(equipment -> equipment instanceof Heaters || equipment instanceof Fireplaces)
                .forEach(equipment -> ((TemperatureControl) equipment).setTargetTemp(dMinTemp, dMinTemp + 1));

        House.getInstance().getRooms().stream()
                .map(Room::getActuatorList)
                .flatMap(Collection::stream)
                .filter(equipment -> equipment instanceof Conditioners)
                .forEach(equipment -> ((TemperatureControl) equipment).setTargetTemp(dMaxTemp - 1, dMaxTemp));

        return "Temperature setpoints set between " + dMinTemp + " and " + dMaxTemp + " °C.";
    }

    private String changeAlarmStatus(String status) {
        if (!AlarmSystem.isEnabled()) return "There is no alarm system in this house.";

        switch (status.toLowerCase()) {
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
                return "This is not a valid status, please try again.";
        }

        return "The alarm status has been set to " + status.toLowerCase() + ".";
    }

    private String changeThresholds(String humThresh, String FPThresh, String HGThresh) {
        if (!AirQualityTester.isEnabled()) return "There is no air quality tester in this house.";

        Double hT, FPT, HGT;

        try {
            hT = Double.parseDouble(humThresh);
            FPT = Double.parseDouble(FPThresh);
            HGT = Double.parseDouble(HGThresh);
        } catch (Exception e) {
            return "These are not valid thresholds";
        }

        AirQualityTester.getInstance().setHumidityThreshold(hT);
        AirQualityTester.getInstance().setFineParticlesThreshold(FPT);
        AirQualityTester.getInstance().setHarmfulGasThreshold(HGT);

        return "Air quality thresholds have been changed to :\n- Humidity: " + hT + "\n- Fine particles: " + FPT + "\n- Harmful Gas: " + HGT;
    }

    private String changeContacts(String... reasonAndNumbers) {
        String reason = reasonAndNumbers[0];
        List<String> contacts = new ArrayList<>(Arrays.asList(Arrays.copyOfRange(reasonAndNumbers, 1, reasonAndNumbers.length)));

        WarningSystem.addOrReplaceContact(reason, contacts);
        return "Emergency contacts for " + reason + " have been updated.";
    }

    private String addRoom(String roomName) {
        House.getInstance().addRoom(new Room((String) roomName));
        return "The room " + roomName + " has been added.";
    }

    private String removeRoom(String roomName) {
        String res = "";

        try {
            House.getInstance().removeRoom(House.getInstance().getRoom(roomName));
            ModelChecker.getInstance().removeFeature(roomName);

            if (!ModelChecker.getInstance().checkFeatures()) {
                res += "This is not compliant with the feature model.";
                ModelChecker.getInstance().addFeature(roomName);
                throw new NullPointerException("");
            }

            res += "The room " + roomName + " has been removed.";

        } catch (NullPointerException e) {
            res += "The room " + roomName + " could not be removed.";
        }

        return res;
    }

    private String addSensor(String roomName, String sensorName) {
        Room room = House.getInstance().getRoom(roomName);
        Sensor sensor = SensorFactory.getSensor(sensorName, room);
        if (sensor == null) {
            return "Sensor " + sensorName + " does not exist, not added.";
        } else {
            if (Interpreter.getInstance().checkAddIntegrity(sensor)) {
                House.getInstance().getRoom(roomName).addSensor(sensor);
                return "Sensor " + sensorName + " has been added to " + roomName + ".";
            } else {
                return "Sensor " + sensorName + " could not be added to " + roomName + ", not compliant with feature model.\n";
            }
        }
    }

    private String removeSensor(String roomName, String sensorName) {
        try {
            Room room = House.getInstance().getRoom(roomName);
            Sensor sensor = room.getSensor(sensorName);

            if (Interpreter.getInstance().checkRemoveIntegrity(sensor)) {
                House.getInstance().getRoom(roomName).removeSensor(sensor);
                return "Sensor " + sensorName + " has been removed from " + roomName + ".";
            } else {
                return "Sensor " + sensorName + " could not be removed from " + roomName + ", not compliant with feature model.\n";
            }
        } catch (NullPointerException e) {
            return "Sensor " + sensorName + " does not exist in " + roomName + ".";
        }
    }

    private String addEquipment(String roomName, String actuatorName) {
        Room room = House.getInstance().getRoom(roomName);
        Actuator actuator = ActuatorFactory.getActuator(actuatorName, room);
        if (actuator == null) {
            return "Actuator " + actuatorName + " does not exist, not added.";
        } else {
            if (Interpreter.getInstance().checkAddIntegrity(actuator)) {
                House.getInstance().getRoom(roomName).addEquipment(actuator);
                return "Actuator " + actuatorName + " has been added to " + roomName + ".";
            } else {
                return "Actuator " + actuatorName + " could not be added to " + roomName + ", not compliant with feature model.\n";
            }
        }
    }

    private String removeEquipment(String roomName, String equipmentName) {
        try {
            Room room = House.getInstance().getRoom(roomName);
            Actuator actuator = room.getEquipment(equipmentName);

            if (Interpreter.getInstance().checkRemoveIntegrity(actuator)) {
                House.getInstance().getRoom(roomName).removeEquipment(actuator);
                return "Actuator " + equipmentName + " has been removed from " + roomName + ".";
            } else {
                return "Actuator " + equipmentName + " could not be removed from " + roomName + ", not compliant with feature model.\n";
            }
        } catch (NullPointerException e) {
            return "Actuator " + equipmentName + " does not exist in " + roomName + ".";
        }
    }

    private String enterRoom(String userName, String roomName) {
        User user = House.getInstance().getUser(userName);
        if (user == null) return userName + " is not a valid user.";
        Room room = House.getInstance().getRoom(roomName);
        if (room == null) return roomName + " is not a valid room.";

        user.enterRoom(roomName);
        return userName + " is now in " + roomName + ".";
    }

    private String scenario1(String string) throws InterruptedException {
        Scenario.scenario1();
        return string + "\nScenario 1 finished";
    }

    private String scenario2(String string) throws InterruptedException {
        Scenario.scenario2();
        return string + "\nScenario 2 finished";
    }

    private String scenario3(String string) throws InterruptedException {
        Scenario.scenario3();
        return string + "\nScenario 3 finished";
    }
}
