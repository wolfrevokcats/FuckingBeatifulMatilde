package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.warningsystem.AirQualityTester;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmSystem;

import java.util.ArrayList;
import java.util.List;

public class House {

    private static final House SINGLE_INSTANCE = new House();
    // fields
    private List<Room> rooms;
    private List<User> residents;
    private List<User> owners;
    private CentralUnit central;
    private AlarmSystem alarm;
    private AirQualityTester airQT;

    //constructor
    private House() {
        this.rooms = new ArrayList<>();
        this.residents = new ArrayList<>();
        this.owners = new ArrayList<>();
        this.central = null;
        this.alarm = null;
        this.airQT = null;
    }

    public static House getInstance() {
        return SINGLE_INSTANCE;
    }

    public void initialize(List<User> owners) {
        this.owners = owners;
        this.rooms = new ArrayList<>();
        alarm = null;
        airQT = null;
        residents = new ArrayList<>();
    }

    //methods
    public void addRoom(Room room) {
        this.rooms.add(room);
    }

    public void addRooms(List<Room> rooms) {
        this.rooms.addAll(rooms);
    }

    public void removeRoom(Room room) {
        this.rooms.remove(room);
    }

    public Room getRoom(String name) {
        return this.rooms.stream()
                .filter(room -> room.getName().equals(name))
                .findAny()
                .orElse(null);
    }

    public User getUser(String name) {
        return this.residents.stream()
                .filter(resident -> resident.getName().equals(name))
                .findAny()
                .orElse(null);
    }

    public void addUser(User user) {
        this.residents.add(user);
    }

    public void addUsers(List<User> users) {
        this.residents.addAll(users);
    }

    public void removeUser(User user) {
        this.residents.remove(user);
    }

    public void addAlarm(AlarmSystem alarm) {
        this.alarm = alarm;
    }

    public void addAirQT(AirQualityTester airQualityTester) {
        this.airQT = airQualityTester;
    }

    public AlarmSystem getAlarm() {
        return this.alarm;
    }

    public AirQualityTester getAirQT() {
        return this.airQT;
    }

    public List<User> getResidents() {
        return this.residents;
    }

    public List<User> getOwners() {
        return this.owners;
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
