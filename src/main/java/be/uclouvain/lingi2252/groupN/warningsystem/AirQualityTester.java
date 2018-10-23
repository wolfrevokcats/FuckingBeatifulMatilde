package be.uclouvain.lingi2252.groupN.warningsystem;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.signals.Air;
import be.uclouvain.lingi2252.groupN.signals.Signal;

import java.util.List;
import java.util.Map;


public class AirQualityTester extends AlarmSystem {

    // fields
    private Double humidityThreshold;
    private Double fineParticlesThreshold;
    private Double harmfulGasThreshold;

    public AirQualityTester(List<CommunicationHub> hubs) {
        super(hubs);
    }

    public AirQualityTester(List<CommunicationHub> hubs, Double humidityThreshold, Double fineParticlesThreshold, Double harmfulGasThreshold) {
        super(hubs);
        this.harmfulGasThreshold = harmfulGasThreshold;
        this.fineParticlesThreshold = fineParticlesThreshold;
        this.humidityThreshold = humidityThreshold;
    }

    @Override
    public void compute(Signal signal, Room room) {
        if (signal instanceof Air) {
            Map<String, Double> air = (Map<String, Double>) signal.extract();
            if (air.get("fine particles") >= fineParticlesThreshold) {
                System.out.println("Fine particles [" + air.get("fine particles") + "] above threshold [" + fineParticlesThreshold + "] in [" + room.getName() + "]");
                ring(room, "fine particles");
            }
            if (air.get("humidity") >= humidityThreshold) {
                System.out.println("Humidity [" + air.get("humidity") + "] above threshold [" + humidityThreshold + "] in [" + room.getName() + "]");
                ring(room, "humidity");
            }
            if (air.get("harmful gas") >= harmfulGasThreshold) {
                System.out.println("Harmful gas [" + air.get("harmful gas") + "] above threshold [" + harmfulGasThreshold + "] in [" + room.getName() + "]");
                ring(room, "harmful gas");
            }
        }
    }

    @Override
    public void ring(Room room, String issue) {
        if (issue.equals("harmful gas")) {
            System.out.println("Alarm ringing in the house [harmful gas] detected!");
            room.evacuate();
            room.findWhy("smoke");
        }
        //send lists of commands to commhubs
    }
}

