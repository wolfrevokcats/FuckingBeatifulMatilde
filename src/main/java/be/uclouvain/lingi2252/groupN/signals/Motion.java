package be.uclouvain.lingi2252.groupN.signals;

public class Motion implements Signal {
    private String motion; //TODO create enum?

    public Motion(String motion) {
        this.motion = motion;
    }

    @Override
    public String extract() {
        return motion;
    }
}
