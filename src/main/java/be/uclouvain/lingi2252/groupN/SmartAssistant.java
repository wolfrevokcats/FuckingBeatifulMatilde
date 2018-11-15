package be.uclouvain.lingi2252.groupN;

public class SmartAssistant {

    private static final SmartAssistant SINGLE_INSTANCE = new SmartAssistant();

    private SmartAssistant() {

    }

    public static SmartAssistant getInstance() {
        return SINGLE_INSTANCE;
    }

    public String ask(String input) {
        if (input.contains("temperature")) {
            String potentialNumber = input.replaceAll("[^0-9]+", "");
            if (input.contains("raise") || input.contains("increase")) {
                if (potentialNumber.equals("")) {
                    CentralUnit.getInstance().setHeatingTempRules(1.0, 1.0);
                    return "The setpoint temperature will be increased by one degree.";
                } else {
                    double temp = Double.parseDouble(potentialNumber);
                    CentralUnit.getInstance().setHeatingTempRules(temp, temp + 1.0);
                    return "The setpoint temperature will be increased to " + temp + " degrees.";
                }
            } else if (input.contains("lower") || input.contains("decrease")) {
                if (potentialNumber.equals("")) {
                    CentralUnit.getInstance().setCoolingTempRules(-1.0, -1.0);
                    return "The setpoint temperature will be decreased by one degree.";
                } else {
                    double temp = Double.parseDouble(potentialNumber);
                    CentralUnit.getInstance().setCoolingTempRules(temp - 1.0, temp);
                    return "The setpoint temperature will be decreased to " + temp + " degrees.";
                }
            }
        }
        return "";
    }
}
