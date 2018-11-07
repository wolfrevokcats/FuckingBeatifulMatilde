package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.signals.Air;
import be.uclouvain.lingi2252.groupN.signals.Frame;
import be.uclouvain.lingi2252.groupN.signals.Motion;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

import java.util.*;
import java.text.*;

public class Scenario {

    // constructor
    public Scenario(){

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

    public static void scenario1() throws InterruptedException{
        Parameterization.getInstance().initialize(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "house2.json").toString());
        House house = Parameterization.getInstance().getHouse();


        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat ("HH:mm:ss");

        User matilde = house.getUser("matilde");
        System.out.print("At " + ft.format(dNow) + " ");
        matilde.enterRoom(house,"entrance");
        house.getRoom("entrance").getEquipment("lights").set(true);



    }



    public static void scenario2() throws InterruptedException {
        Parameterization.getInstance().initialize(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "samplehouse.json").toString());
        House house = Parameterization.getInstance().getHouse();

        User matilde = house.getUser("matilde");
        matilde.enterRoom(house, "kitchen");
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getEquipment("cookers").set(true);
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getSensor("kitchen_cameras_0").sense(new Frame("smoke near cooker"));
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getSensor("kitchen_air_sensors_0").sense(new Air(5000.0, 150.0, .6));
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").getSensor("kitchen_motion_sensors_0").sense(new Motion("FALL"));
    }

    public static void scenario3() throws InterruptedException{

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
