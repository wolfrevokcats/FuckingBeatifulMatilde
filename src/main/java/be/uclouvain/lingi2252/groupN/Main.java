package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.signals.Air;
import be.uclouvain.lingi2252.groupN.signals.Frame;
import be.uclouvain.lingi2252.groupN.signals.Motion;

import java.nio.file.Paths;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) {
        try {
            System.out.println("--- First Scenario ---");
            Scenario.scenario1();
            //System.out.println("--- Second Scenario ---");
            //Scenario.scenario2();
            //System.out.println("--- Third Scenario ---");
            //Scenario.scenario3();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
