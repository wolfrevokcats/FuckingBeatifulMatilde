package be.uclouvain.lingi2252.groupN;

public class User {

    //fields
    private String name;
    private Room currentLocation;

    //constructor
    public User(String name) {
        this.name = name;
        this.currentLocation = null;
    }

    //methods
    public void enterRoom(String roomName) {
        if (currentLocation != null) {
            currentLocation.roomLeft(this);
        }

        if (House.getInstance().getRoom(roomName) != null) {
            House.getInstance().getRoom(roomName).roomEntered(this);
            currentLocation = House.getInstance().getRoom(roomName);
        } else {
            throw new IllegalArgumentException("No such room [" + roomName + "] in this house!");
        }
    }

    public void askToSmartAssistant(String request) {
        if (SmartAssistant.isEnabled()) System.out.println(SmartAssistant.getInstance().ask(this, request));
        else System.out.println("There is no smart assistant in this house.");
    }

    public Room getLocation() {
        return currentLocation;
    }

    public String getName() {
        return name;
    }
}
