package be.uclouvain.lingi2252.groupN.equipment;

public interface Equipment {

    void schedule(Boolean status, Double from, Double to);

    Boolean checkStatus();

    void set(Boolean status);

}
