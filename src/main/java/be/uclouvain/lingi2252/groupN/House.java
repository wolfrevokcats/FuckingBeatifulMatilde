package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.warningsystem.AirQualityTester;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmSystem;

import java.util.*;
import java.util.stream.Collectors;

public class House {

    // fields
    private List<Room> rooms;
    private List<User> residents;
    private User owner;
    private CentralUnit central;
    private AlarmSystem alarm;
    private AirQualityTester airQC;

    //constructor
    public House(User owner){
        this.owner = owner;
        this.rooms = new ArrayList<>();
        alarm = new AlarmSystem(rooms.stream().map(Room::getCommHub).collect(Collectors.toList()));
        airQC = new AirQualityTester(rooms.stream().map(Room::getCommHub).collect(Collectors.toList()), .8, 10000.0, 100.0);
        residents = new ArrayList<>();
    }

    public House(User owner, List<Room> rooms) {
        this.owner = owner;
        this.rooms = rooms;
        alarm = new AlarmSystem(rooms.stream().map(Room::getCommHub).collect(Collectors.toList()));
        airQC = new AirQualityTester(rooms.stream().map(Room::getCommHub).collect(Collectors.toList()), .8, 10000.0, 100.0);
        residents = new ArrayList<>();
    }

    //methods
    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void addRooms(List<Room> rooms) { this.rooms.addAll(rooms); }

    public void removeRoom(Room room) {
        rooms.remove(room);
    }

    public Optional<Room> getRoom(String name) {
        return rooms.stream()
                .filter(room -> room.getName().equals(name))
                .findAny();
    }

    public void addUser(User user) {
        residents.add(user);
    }

    public void addUsers(List<User> users) {
        this.residents.addAll(users);
    }

    public void removeUser(User user){
        residents.remove(user);
    }

    public AlarmSystem getAlarm() {
        return alarm;
    }

    public AirQualityTester getAirQC() {
        return airQC;
    }
}
