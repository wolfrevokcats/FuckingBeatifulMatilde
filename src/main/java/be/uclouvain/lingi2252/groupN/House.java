package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.warningsystem.AirQualityTester;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmSystem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class House {

    // fields
    private List<Room> rooms;
    private List<User> residents;
    private List<User> owners;
    private CentralUnit central;
    private AlarmSystem alarm;
    private AirQualityTester airQT;

    //constructor
    public House(List<User> owners) {
        this.owners = owners;
        this.rooms = new ArrayList<>();
//        alarm = new AlarmSystem(rooms.stream().map(Room::getCommHub).collect(Collectors.toList()));
//        airQT = new AirQualityTester(rooms.stream().map(Room::getCommHub).collect(Collectors.toList()), .8, 10000.0, 100.0);
        alarm = null;
        airQT = null;
        residents = new ArrayList<>();
    }

    //methods
    public void addRoom(Room room) {
        rooms.add(room);
    }

    public void addRooms(List<Room> rooms) {
        this.rooms.addAll(rooms);
    }

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

    public void removeUser(User user) {
        residents.remove(user);
    }

    public void addAlarm(AlarmSystem alarm) {
        this.alarm = alarm;
    }

    public void addAirQT(AirQualityTester airQualityTester) {
        this.airQT = airQualityTester;
    }

    public AlarmSystem getAlarm() {
        return alarm;
    }

    public AirQualityTester getAirQT() {
        return airQT;
    }

    public List<User> getResidents() {
        return residents;
    }

    public List<User> getOwners() {
        return owners;
    }
}
