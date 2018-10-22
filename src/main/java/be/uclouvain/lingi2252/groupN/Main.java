package be.uclouvain.lingi2252.groupN;

public class Main {

    public static void main(String[] args) {
        scenario2();
    }

    public static void scenario2() {
        User owner = new User("Matilde");
        User user1 = new User("Quentin");
        User user2 = new User("Benoît");
        User user3 = new User("Kim");
        House house = new House(owner);
        house.addUser(user1);
        house.addUser(user2);
        house.addUser(user3);
    }
}
