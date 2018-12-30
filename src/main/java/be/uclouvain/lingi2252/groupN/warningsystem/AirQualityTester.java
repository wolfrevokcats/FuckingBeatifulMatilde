package be.uclouvain.lingi2252.groupN.warningsystem;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.Room;
import be.uclouvain.lingi2252.groupN.procedures.EvacuationManager;
import be.uclouvain.lingi2252.groupN.procedures.ObjectTracker;
import be.uclouvain.lingi2252.groupN.signals.Air;
import be.uclouvain.lingi2252.groupN.signals.Signal;

import java.util.List;
import java.util.Map;


public class AirQualityTester extends WarningSystem {

    // fields
    private static final AirQualityTester SINGLE_INSTANCE = new AirQualityTester();
    private static boolean enabled = false;
    private Double humidityThreshold;
    private Double fineParticlesThreshold;
    private Double harmfulGasThreshold;

    private AirQualityTester() {
    }

    public static void enable() {
        enabled = true;
    }

    public static AirQualityTester getInstance() {
        if (enabled) return SINGLE_INSTANCE;
        System.out.println("There is no air quality tester in this house");
        return null;
    }

    public static boolean isEnabled() {
        return enabled;
    }

    public void initialize(Double humidityThreshold, Double fineParticlesThreshold, Double harmfulGasThreshold) {
        this.harmfulGasThreshold = harmfulGasThreshold;
        this.fineParticlesThreshold = fineParticlesThreshold;
        this.humidityThreshold = humidityThreshold;
    }

    @Override
    public void compute(Signal signal, Room room) {
        if (signal instanceof Air) {
            Map<String, Double> air = ((Air) signal).extract();
            if (air.get("fine particles") >= fineParticlesThreshold) {
                System.out.println("Detected fine particles [" + air.get("fine particles") + "] above threshold [" + fineParticlesThreshold + "] in [" + room.getName() + "]");
                ring(room, "fine particles");
            }
            if (air.get("humidity") >= humidityThreshold) {
                System.out.println("Detected humidity [" + air.get("humidity") + "] above threshold [" + humidityThreshold + "] in [" + room.getName() + "]");
                ring(room, "humidity");
            }
            if (air.get("harmful gas") >= harmfulGasThreshold) {
                System.out.println("Detected harmful gas [" + air.get("harmful gas") + "] above threshold [" + harmfulGasThreshold + "] in [" + room.getName() + "]");
                ring(room, "harmful gas");
            }
        }
    }

    @Override
    public void ring(Room room, String issue) {
        if (issue.equals("harmful gas")) {
            System.out.print("Alarm starts ringing in the house...");
            System.out.println("[harmful gas] detected!");
            if (EvacuationManager.isEnabled()) EvacuationManager.getInstance().evacuate();
            if (ObjectTracker.isEnabled()) ObjectTracker.getInstance().find(room, new String[]{"smoke"}, true);
        }
        //send lists of commands to commhubs
    }

    public Double getHumidityThreshold() {
        return humidityThreshold;
    }

    public void setHumidityThreshold(Double humidityThreshold) {
        this.humidityThreshold = humidityThreshold;
    }

    public Double getFineParticlesThreshold() {
        return fineParticlesThreshold;
    }

    public void setFineParticlesThreshold(Double fineParticlesThreshold) {
        this.fineParticlesThreshold = fineParticlesThreshold;
    }

    public Double getHarmfulGasThreshold() {
        return harmfulGasThreshold;
    }

    public void setHarmfulGasThreshold(Double harmfulGasThreshold) {
        this.harmfulGasThreshold = harmfulGasThreshold;
    }
}

