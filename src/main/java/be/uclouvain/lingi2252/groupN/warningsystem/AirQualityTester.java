package be.uclouvain.lingi2252.groupN.warningsystem;

import be.uclouvain.lingi2252.groupN.CommunicationHub;
import be.uclouvain.lingi2252.groupN.signals.Signal;

import java.util.List;
import java.util.Map;


public class AirQualityTester extends AlarmSystem{

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
    public void compute(Signal signal) {

    }

    @Override
    public void ring() {
        //send lists of commands to commhubs
    }
}

