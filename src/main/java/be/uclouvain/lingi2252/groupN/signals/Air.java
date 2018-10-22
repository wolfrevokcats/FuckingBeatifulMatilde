package be.uclouvain.lingi2252.groupN.signals;

import java.util.HashMap;
import java.util.Map;

public class Air implements Signal {
    private Double fineParticles;
    private Double harmfulGas;
    private Double humidity;

    public Air(Double fineParticles, Double harmfulGas, Double humidity) {
        this.fineParticles = fineParticles;
        this.harmfulGas = harmfulGas;
        this.humidity = humidity;
    }
    @Override
    public Map<String, Double> extract() {
        Map<String, Double> airMetrics = new HashMap<>();

        airMetrics.put("fine particles", fineParticles);
        airMetrics.put("harmful gas", harmfulGas);
        airMetrics.put("humidity", humidity);

        return airMetrics;
    }
}
