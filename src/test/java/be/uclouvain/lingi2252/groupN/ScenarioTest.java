package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.parameterization.Parameterization;
import be.uclouvain.lingi2252.groupN.signals.*;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmStatus;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmSystem;
import org.junit.Test;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ScenarioTest {
    @Test
    public void scenario1Test() {
        Parameterization.getInstance().initialize(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "richHouse.json").toString());
        House house = House.getInstance();


        Date dNow = new Date();
        Date customDate = new Date(dNow.getYear(), dNow.getMonth(), dNow.getDate(), 19, 57, 44);
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");

        AlarmSystem.getInstance().setStatus(AlarmStatus.ARMED);
        User quentin = house.getUser("quentin");
        System.out.println("Time: " + ft.format(customDate));

        house.getRoom("garden").getSensor("garden_camera_4").sense(new Frame("quentin"));
        quentin.enterRoom("entrance");
        house.getRoom("entrance").getSensor("entrance_temperature_sensor_0").sense(new Temperature(19.0));
        quentin.askToSmartAssistant("Can you raise the temperature?");
        house.getRoom("kitchen").getSensor("kitchen_camera_0").sense(new Frame("medicine on table"));
        quentin.askToSmartAssistant("Where is my medicine?");
        quentin.enterRoom("bedroom");
        quentin.askToSmartAssistant("Turn off the lights.");
    }

    @Test
    public void scenario2Test() {
        Parameterization.getInstance().initialize(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "middleclassHouse.json").toString());
        House house = House.getInstance();

        User matilde = house.getUser("matilde");
        matilde.enterRoom("kitchen");
        house.getRoom("kitchen").getEquipment("cookers").set(true);
        house.getRoom("kitchen").getSensor("kitchen_camera_0").sense(new Frame("smoke near cooker"));
        house.getRoom("kitchen").getSensor("kitchen_air_sensor_0").sense(new Air(5000.0, 150.0, .6));
        house.getRoom("kitchen").getSensor("kitchen_motion_sensor_0").sense(new Motion("FALL"));
    }

    @Test
    public void scenario3Test() throws InterruptedException {
        Parameterization.getInstance().initialize(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "poorHouse.json").toString());
        House house = House.getInstance();

        User julie = house.getUser("julie");
        System.out.println("At 21.00 CEST " + julie.getName() + " goes to sleep");
        AlarmSystem.getInstance().setStatus(AlarmStatus.PRESENCE);

        julie.enterRoom("bedroom");
        System.out.println("[" + julie.getName() + "]" + " switches manually [off] the lights");
        house.getRoom("bedroom").getEquipment("lights").set(false);
        house.getRoom("kitchen").getEquipment("windows").set(true);
        house.getRoom("kitchen").getSensor("kitchen_contact_sensor_0").sense(new Contact(true));
    }

    @Test
    public void simpleDisplayDelayTest() throws InterruptedException {
        Scenario.simpleDisplayDelay(3,10);
    }
}
