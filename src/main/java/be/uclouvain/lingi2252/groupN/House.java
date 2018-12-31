package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.actuators.TemperatureControl;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;

public class House {

    private static final House SINGLE_INSTANCE = new House();
    // fields
    private List<Room> rooms;
    private List<User> residents;
    private List<User> owners;

    //constructor
    private House() {
        this.rooms = new ArrayList<>();
        this.residents = new ArrayList<>();
        this.owners = new ArrayList<>();
    }

    public static House getInstance() {
        return SINGLE_INSTANCE;
    }

    public void initialize(List<User> owners) {
        this.owners.addAll(owners);
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
        room.getActuatorList().stream()
                .filter(actuator -> actuator instanceof TemperatureControl)
                .forEach(actuator -> room.getCommHub().deleteObservers());
        room.getActuatorList().clear();
        room.getSensors().forEach(Observable::deleteObservers);
        room.getSensors().clear();
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

    public void addUsers(List<User> users) {
        this.residents.addAll(users);
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
