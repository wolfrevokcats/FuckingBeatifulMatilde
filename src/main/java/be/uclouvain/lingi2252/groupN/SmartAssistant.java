package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.actuators.Lights;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SmartAssistant {

    private static final SmartAssistant SINGLE_INSTANCE = new SmartAssistant();
    private static boolean enabled = false;

    private SmartAssistant() {
    }

    public static SmartAssistant getInstance() {
        if (enabled) return SINGLE_INSTANCE;
        System.out.println("There is no smart assistant in this house");
        return null;
    }

    public static void enable() {
        enabled = true;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public String ask(User user, String input) {
        if (checkInput(input, "temperature")) {
            String potentialNumber = input.replaceAll("[^0-9]+", "");
            if (checkInput(input, "raise") || checkInput(input, "increase")) {
                if (potentialNumber.equals("")) {
                    CentralUnit.getInstance().setHeatingTempRules(1.0, 1.0);
                    return "The setpoint temperature will be increased by one degree";
                } else {
                    double temp = Double.parseDouble(potentialNumber);
                    CentralUnit.getInstance().setHeatingTempRules(temp, temp + 1.0);
                    return "The setpoint temperature will be increased to " + temp + " degrees";
                }
            } else if (checkInput(input, "lower") || checkInput(input, "decrease")) {
                if (potentialNumber.equals("")) {
                    CentralUnit.getInstance().setCoolingTempRules(-1.0, -1.0);
                    return "The setpoint temperature will be decreased by one degree";
                } else {
                    double temp = Double.parseDouble(potentialNumber);
                    CentralUnit.getInstance().setCoolingTempRules(temp - 1.0, temp);
                    return "The setpoint temperature will be decreased to " + temp + " degrees";
                }
            }
        } else if (checkInput(input, "find") || checkInput(input, "where")) {
            String search = input.replaceAll("[^a-zA-Z ]", "").toLowerCase();
            search = search.replace("find ", " ").replace("where ", " ");
            search = search.replace(" is ", " ").replace(" my ", " ").replace(" the ", " ");
            List<String> words = new ArrayList<>(Arrays.asList(search.split(" ")));
            words.removeIf(String::isEmpty);
            return CentralUnit.getInstance().findObject(words.toArray(new String[0]));
        } else if (checkInput(input, "light")) {
            Room room = user.getLocation();
            if (checkInput(input, "off")) {
                room.getActuatorList().stream()
                        .filter(equipment -> equipment instanceof Lights)
                        .forEach(lights -> lights.set(false));
            } else {
                room.getActuatorList().stream()
                        .filter(equipment -> equipment instanceof Lights)
                        .forEach(lights -> lights.set(true));
            }
        }
        return "";
    }

    private boolean checkInput(String input, String wanted) {
        return (input.toLowerCase().contains(wanted.toLowerCase()));
    }
}
