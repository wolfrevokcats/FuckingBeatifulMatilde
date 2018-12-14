package be.uclouvain.lingi2252.groupN.warningsystem;

public enum AlarmStatus {
    ARMED("armed"),
    DISARMED("disarmed"),
    PRESENCE("armed in presence mode");

    private final String description;

    AlarmStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
