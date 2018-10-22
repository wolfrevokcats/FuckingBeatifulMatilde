package be.uclouvain.lingi2252.groupN.equipment;

import java.util.Optional;

public interface Equipment {

    void setAuto(Optional<Double> time);
    void schedule(Boolean status, Double from, Double to);
    Boolean checkStatus();
    void set(Boolean status);

}
