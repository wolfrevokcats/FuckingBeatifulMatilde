package be.uclouvain.lingi2252.groupN;

public class User {

    //fields
    private String name;


    //constructor
    public User(String name){

    }

    //methods
    public void enterRoom(House house, String roomName){
        if (house.getRoom(roomName).isPresent()) {
            house.getRoom(roomName).get().roomEntered(this);
        } else {
            throw new IllegalArgumentException("No such room [" + roomName + "] in this house!");
        }
    }

}
