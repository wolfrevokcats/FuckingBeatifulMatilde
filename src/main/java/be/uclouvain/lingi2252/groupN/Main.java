package be.uclouvain.lingi2252.groupN;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws InterruptedException {

        System.out.println("--- Choose a scenario to implement ---");
        System.out.println(" Press \n 1: Scenario1 \n 2: Scenario2\n 3: Scenario3");
        int input = (new Scanner(System.in)).nextInt();
        switch(input){
            case 1:
                System.out.println(" --- First Scenario Implemented ---");
                Scenario.scenario1();
                break;

            case 2:
                System.out.println("--- Second Scenario Implemented ---");
                Scenario.scenario2();
                break;

            case 3:
                System.out.println("--- Third Scenario Implemented ---");
                Scenario.scenario3();
                break;
        }

    }

}
