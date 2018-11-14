package be.uclouvain.lingi2252.groupN.signals;

public class Temperature implements Signal {
    private Double temp;

    public Temperature(Double temp) {
        this.temp = temp;
    }

    @Override
    public Object extract() {
        return temp;
    }
}
