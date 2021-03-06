package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.interpreter.Interpreter;
import be.uclouvain.lingi2252.groupN.parameterization.Parameterization;
import be.uclouvain.lingi2252.groupN.signals.*;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmStatus;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmSystem;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Scenario {

    // constructor
    private Scenario() {

    }

    public static void scenario1() throws InterruptedException {
        Parameterization.getInstance().initialize(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "richHouse.json").toString());
        House house = House.getInstance();

        Interpreter.getInstance().interpret();

        Date dNow = new Date();
        Date customDate = new Date(dNow.getYear(), dNow.getMonth(), dNow.getDate(), 19, 57, 44);
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");

        AlarmSystem.getInstance().setStatus(AlarmStatus.ARMED);
        User quentin = house.getUser("quentin");
        System.out.println("Time: " + ft.format(customDate));
        simpleDisplayDelay(3, 500);

        house.getRoom("garden").getSensor("garden_camera_4").sense(new Frame("quentin"));
        simpleDisplayDelay(3, 500);
        quentin.enterRoom("entrance");
        simpleDisplayDelay(3, 500);
        house.getRoom("entrance").getSensor("entrance_temperature_sensor_0").sense(new Temperature(19.0));
        simpleDisplayDelay(3, 500);
        quentin.askToSmartAssistant("Can you raise the temperature?");
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getSensor("kitchen_camera_0").sense(new Frame("medicine on table"));
        simpleDisplayDelay(3, 500);
        quentin.askToSmartAssistant("Where is my medicine?");
        simpleDisplayDelay(3, 500);
        quentin.enterRoom("bedroom");
        simpleDisplayDelay(3, 500);
        quentin.askToSmartAssistant("Turn off the lights.");
    }

    public static void scenario2() throws InterruptedException {
        Parameterization.getInstance().initialize(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "middleclassHouse.json").toString());
        House house = House.getInstance();

        User matilde = house.getUser("matilde");
        matilde.enterRoom("kitchen");
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getEquipment("cookers").set(true);
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getSensor("kitchen_camera_0").sense(new Frame("smoke near cooker"));
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getSensor("kitchen_air_sensor_0").sense(new Air(5000.0, 150.0, .6));
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getSensor("kitchen_motion_sensor_0").sense(new Motion("FALL"));
    }

    public static void scenario3() throws InterruptedException {
        Parameterization.getInstance().initialize(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "poorHouse.json").toString());
        House house = House.getInstance();

        User julie = house.getUser("julie");
        System.out.println("At 21.00 CEST " + julie.getName() + " goes to sleep");
        simpleDisplayDelay(3, 500);
        AlarmSystem.getInstance().setStatus(AlarmStatus.PRESENCE);
        simpleDisplayDelay(3, 500);

        julie.enterRoom("bedroom");
        simpleDisplayDelay(3, 500);
        System.out.println("[" + julie.getName() + "]" + " switches manually [off] the lights");
        house.getRoom("bedroom").getEquipment("lights").set(false);
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getEquipment("windows").set(true);
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getSensor("kitchen_contact_sensor_0").sense(new Contact(true));

    }

    public static void simpleDisplayDelay(int nbPoints, int individualDelay) throws InterruptedException {
        for (int i = 0; i < nbPoints; i++) {
            TimeUnit.MILLISECONDS.sleep(individualDelay);
            System.out.print(".");
        }
        TimeUnit.MILLISECONDS.sleep(individualDelay);
        System.out.print("\n");
    }
}
