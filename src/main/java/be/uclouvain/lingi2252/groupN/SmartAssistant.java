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
                    //raise min and max by one degree
                } else {
                    //mettre le min à la temperature demandée, conserver delta
                }
            } else if (input.contains("lower") || input.contains("decrease")) {
                if (potentialNumber.equals("")) {
                    //lower min and max by one degree
                } else {
                    //mettre le max à la temperature demandée, conserver delta
                }
            }
        }
        return " ";
    }
}
