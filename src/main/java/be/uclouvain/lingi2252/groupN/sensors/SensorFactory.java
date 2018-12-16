package be.uclouvain.lingi2252.groupN.sensors;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.parameterization.Parameterization;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

import static java.lang.Math.toIntExact;

public class SensorFactory {
    public static Sensor getSensor(String type, Room room) {
        String classPath = "be.uclouvain.lingi2252.groupN.sensors." + Parameterization.toClassName(type);

        try {
            Class<?> clazz = Class.forName(classPath);
            Constructor<?> ctor = clazz.getConstructor(String.class, CommunicationHub.class);

            int sensorId = toIntExact(room.getSensors().stream()
                    .filter(clazz::isInstance)
                    .count());

            return (Sensor) ctor.newInstance(room.getName() + '_' + type + '_' + sensorId, room.getCommHub());

        } catch (ClassNotFoundException e) {
            System.out.println("Sensor [" + classPath + "] doesn't exist or isn't implemented yet!");
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }

        return null;
    }
}
