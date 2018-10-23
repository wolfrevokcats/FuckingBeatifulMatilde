package be.uclouvain.lingi2252.groupN.signals;

public class Frame implements Signal {
    private String frame;

    public Frame(String frame) {
        this.frame = frame;
    }

    @Override
    public String extract() {
        return frame;
    }
}
