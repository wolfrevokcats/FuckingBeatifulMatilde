package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.WarningSystem.AirQualityTester;
import be.uclouvain.lingi2252.groupN.WarningSystem.AlarmSystem;

import java.util.List;
import java.util.Optional;

public class House {

    // fields
    private List<Room> rooms;
    private List<User> residents;
    private User owner;
    private CentralUnit central;
    private Optional<AlarmSystem> alarm;
    private Optional<AirQualityTester> airQC;

    //constructor
    public House(User room){

    }

    //methods
    public void addRoom(Room room){

    }

    public void removeRoom(Room room){

    }

    public void addUser(User user){

    }

    public void removeUser(User user){

    }


}
