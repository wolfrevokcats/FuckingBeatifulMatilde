package be.uclouvain.lingi2252.groupN;

import be.uclouvain.lingi2252.groupN.warningsystem.AirQualityTester;
import be.uclouvain.lingi2252.groupN.warningsystem.AlarmSystem;
import org.junit.Test;

import java.nio.file.Paths;

import static org.junit.Assert.*;

public class ParameterizationTest {

    @Test
    public void toClassNameTest() {
        assertEquals("Foo", Parameterization.toClassName("foo"));
        assertEquals("Bar", Parameterization.toClassName("bar_"));
        assertEquals("FooBar", Parameterization.toClassName("foo bar "));
        assertEquals("FooBar", Parameterization.toClassName("foo_bar"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void wrongFileTest() {
        Parameterization.getInstance().initialize((Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "wrongFile.json").toString()));
    }

    @Test(expected = IllegalArgumentException.class)
    public void notExistingFile() {
        Parameterization.getInstance().initialize((Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "foobar.json").toString()));
    }

    @Test
    public void normalInitializationTest() {
        House house = House.getInstance();
        assertNull(SmartAssistant.getInstance());
        assertNull(AlarmSystem.getInstance());
        assertNull(AirQualityTester.getInstance());
        try {
            house.getRoom("kitchen");
        } catch (IllegalArgumentException e) {
            assertEquals("There is no such room [kitchen] in this house.", e.getMessage());
        }
        try {
            house.getUser("quentin");
        } catch (IllegalArgumentException e) {
            assertEquals("There is no such resident [quentin] in this house.", e.getMessage());
        }
        assertTrue(house.getRooms().isEmpty());
        assertTrue(house.getResidents().isEmpty());
        assertTrue(house.getOwners().isEmpty());

        Parameterization.getInstance().initialize((Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "middleclassHouse.json").toString()));
        assertNull(SmartAssistant.getInstance());
        assertNotNull(AlarmSystem.getInstance());
        assertNotNull(AirQualityTester.getInstance());

        Room kitchen = house.getRoom("kitchen");
        assertEquals(3, kitchen.getEquipmentList().size());

        User quentin = house.getUser("quentin");
        quentin.enterRoom("kitchen");
        assertEquals(kitchen, quentin.getLocation());

        assertEquals(5, house.getRooms().size());
        assertEquals(4, house.getResidents().size());
        assertEquals(1, house.getOwners().size());

        Parameterization.getInstance().initialize((Paths.get(System.getProperty("user.dir"), "src", "main", "resources", "richHouse.json").toString()));
        assertNotNull(SmartAssistant.getInstance());
        assertNotNull(AlarmSystem.getInstance());
        assertNotNull(AirQualityTester.getInstance());

        kitchen = house.getRoom("kitchen");
        assertEquals(6, kitchen.getEquipmentList().size());

        quentin = house.getUser("quentin");
        quentin.enterRoom("kitchen");
        assertEquals(kitchen, quentin.getLocation());

        assertEquals(6, house.getRooms().size());
        assertEquals(4, house.getResidents().size());
        assertEquals(1, house.getOwners().size());

    }
}
