package be.uclouvain.lingi2252.groupN.interpreter;

import be.uclouvain.lingi2252.groupN.House;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.actuators.Conditioners;
import be.uclouvain.lingi2252.groupN.actuators.Fireplaces;
import be.uclouvain.lingi2252.groupN.actuators.Heaters;
import be.uclouvain.lingi2252.groupN.actuators.TemperatureControl;
import be.uclouvain.lingi2252.groupN.sensors.TemperatureSensor;
import be.uclouvain.lingi2252.groupN.signals.Temperature;
import be.uclouvain.lingi2252.groupN.warningsystem.AirQualityTester;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmStatus;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmSystem;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Collection;

public class CommandReceiver {
    private static final CommandReceiver SINGLE_INSTANCE = new CommandReceiver();

    private CommandReceiver() {

    }

    public static CommandReceiver getInstance() {
        return SINGLE_INSTANCE;
    }

    private Object callMethod(String sMethod, String... arguments) throws Exception {
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
        } catch (Exception e) {
            e.printStackTrace();
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
//
//    private void changeContacts() {
//        System.out.println("How many contacts do you wish to add?");
//        Object nbContacts = input("int");
//
//        if (nbContacts == null) {
//            System.out.println("This is not a valid number, please try again.");
//            changeContacts();
//            return;
//        }
//
//        System.out.println("For which emergency should these contacts be called?");
//        Object reason = input("string");
//
//        if (reason == null) {
//            System.out.println("This is not a valid reason, please try again.");
//            changeContacts();
//            return;
//        }
//
//        List<String> contacts = new ArrayList<>();
//        for (int i = 0; i < (int) nbContacts; i++) {
//            System.out.print("Type information for contact no." + (i + 1) + ": ");
//            contacts.add((String) input("string"));
//        }
//
//        WarningSystem.addOrReplaceContact((String) reason, contacts);
//    }
//
//    private void addRoom() {
//        System.out.println("What is the name of the room you want to add?");
//        Object roomName = input("string");
//
//        if (roomName == null) {
//            System.out.println("This is not a valid name, please try again.");
//            addRoom();
//            return;
//        }
//
//        House.getInstance().addRoom(new Room((String) roomName));
//    }
//
//    private void addSensors() {
//        System.out.println("Where do you want to add sensors?");
//        Object roomName = input("string");
//
//        if (roomName == null) {
//            System.out.println("This is not a valid name, please try again.");
//            addSensors();
//            return;
//        }
//
//        Room room = House.getInstance().getRoom((String) roomName);
//        if (room == null) {
//            System.out.println("This room does not exist, please try again.");
//            addSensors();
//            return;
//        }
//
//        System.out.println("How many sensors do you want to add?");
//        Object nbSensors = input("int");
//
//        if (nbSensors == null) {
//            System.out.println("This is not a valid number, please try again.");
//            addSensors();
//            return;
//        }
//
//        for (int i = 0; i < (int) nbSensors; i++) {
//            System.out.print("Type of sensor no." + (i + 1) + ": ");
//            Object sensorName = input("string");
//
//            Sensor sensor = SensorFactory.getSensor((String) sensorName, room);
//            if (sensor == null) i--;
//            else {
//                if (checkIntegrity(sensor)) House.getInstance().getRoom((String) roomName).addSensor(sensor);
//                else System.out.println("This cannot be added.");
//            }
//        }
//    }
//
//    private void addEquipment() {
//        System.out.println("Where do you want to add actuators?");
//        Object roomName = input("string");
//
//        if (roomName == null) {
//            System.out.println("This is not a valid name, please try again.");
//            addEquipment();
//            return;
//        }
//
//        Room room = House.getInstance().getRoom((String) roomName);
//        if (room == null) {
//            System.out.println("This room does not exist, please try again.");
//            addEquipment();
//            return;
//        }
//
//        System.out.println("How many actuators do you want to add?");
//        Object nbEquipment = input("int");
//
//        if (nbEquipment == null) {
//            System.out.println("This is not a valid number, please try again.");
//            addEquipment();
//            return;
//        }
//
//        List<Actuator> actuatorList = new ArrayList<>();
//        for (int i = 0; i < (int) nbEquipment; i++) {
//            System.out.print("Type of actuators no." + (i + 1) + ": ");
//            Object equipmentName = input("string");
//
//            Actuator actuator = ActuatorFactory.getActuator((String) equipmentName, room);
//            if (actuator == null) i--;
//            else {
//                if (checkIntegrity(actuator)) House.getInstance().getRoom((String) roomName).addEquipment(actuator);
//                else System.out.println("This cannot be added.");
//            }
//        }
//
//        if (checkIntegrity(actuatorList)) House.getInstance().getRoom((String) roomName).addEquipment(actuatorList);
//        else System.out.println("This cannot be added.");
//    }
}
