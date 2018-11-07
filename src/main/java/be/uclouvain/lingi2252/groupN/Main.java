package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.signals.Air;
import be.uclouvain.lingi2252.groupN.signals.Frame;
import be.uclouvain.lingi2252.groupN.signals.Motion;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        try {
            scenario2();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void scenario2() throws InterruptedException {
        Parameterization.getInstance().initialize(Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "samplehouse.json").toString());
        House house = Parameterization.getInstance().getHouse();

        User matilde = house.getResidents().get(0);
        matilde.enterRoom(house, "kitchen");
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").get().getEquipment("cookers").set(true);
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").get().getSensor("kitchen_cameras_0").sense(new Frame("smoke near cooker"));
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").get().getSensor("kitchen_air_sensors_0").sense(new Air(5000.0, 150.0, .6));
        simpleDisplayDelay(3, 500);
        house.getRoom("kitchen").get().getSensor("kitchen_motion_sensors_0").sense(new Motion("FALL"));
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
