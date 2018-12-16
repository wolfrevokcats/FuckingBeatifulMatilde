package be.uclouvain.lingi2252.groupN.actuators;

import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.parameterization.Parameterization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ActuatorFactory {
    public static Actuator getActuator(String type, Room room) {
        String classPath = "be.uclouvain.lingi2252.groupN.actuators." + Parameterization.toClassName(type);

        try {
            Class<?> clazz = Class.forName(classPath);
            Constructor<?> ctor = clazz.getConstructor(Room.class);

            return (Actuator) ctor.newInstance(room);

        } catch (ClassNotFoundException e) {
            System.out.println("Sensor [" + classPath + "] doesn't exist or isn't implemented yet!");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }
}
