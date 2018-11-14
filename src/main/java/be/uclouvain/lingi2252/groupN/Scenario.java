package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.signals.Air;
import be.uclouvain.lingi2252.groupN.signals.Frame;
import be.uclouvain.lingi2252.groupN.signals.Motion;

import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class Scenario {

    // constructor
    public Scenario() {

    }

//    Considering the user gets home at 8PM, the security system
//    recognizes the owner and, once entered inside, the light
//    automatically turns on. Depending on the outer temperature
//    the house calibrates the indoor one through some specific
//    temperature sensors, also possibly according to the user’s
//    preset, which is 19C.
//    Here the communication between user and system is performed
//    through the house’s smart assistant. The user feels cold and tells the system to raise the temperature. The heaters
//    turn on until a temperature of 21C is reached.
//    Moreover the user can’t find his medicine and he asks the
//    house where it is and the system, after having searched and
//    found it, makes the smart assistant reply indicating its location
//    as "The medicine is on the kitchen’s table". When the user goes
//    to bed, the lights turn off automatically.

    public static void scenario1() throws InterruptedException {
        Parameterization.getInstance().initialize(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "richHouse.json").toString());
        House house = House.getInstance();


        Date dNow = new Date();
        Date customDate = new Date(dNow.getYear(),dNow.getMonth(), dNow.getDate(), 20, 0, 0);
        SimpleDateFormat ft = new SimpleDateFormat("HH:mm:ss");

        User matilde = house.getUser("matilde");
        System.out.print("At " + ft.format(customDate) + " ");
        // Recognize the owner: signal --> frame --> camera


        matilde.enterRoom("entrance");

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



    // At 21:00 the user goes to sleep and the alarm system is armed.
    // Close to midnight the contact sensor detects a detachment, spreading an alarm signal to
    // the alarm system through the communication hub.
    // The cameras detects the intrusive presence.
    // Communication Hub:
    // 1) Close and lock all the doors in the house
    // 2) Call the emergency numbers


    public static void scenario3() throws InterruptedException {
        Parameterization.getInstance().initialize(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "poorHouse.json").toString());
        House house = House.getInstance();

        User user = house.getUser("julie");
        user.enterRoom("bedroom");
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getEquipment("cookers").set(true);
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getSensor("kitchen_cameras_0").sense(new Frame("smoke near cooker"));
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getSensor("kitchen_air_sensors_0").sense(new Air(5000.0, 150.0, .6));
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getSensor("kitchen_motion_sensors_0").sense(new Motion("FALL"));


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
