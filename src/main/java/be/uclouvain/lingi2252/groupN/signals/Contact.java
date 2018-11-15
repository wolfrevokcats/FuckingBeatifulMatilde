package be.uclouvain.lingi2252.groupN.signals;

public class Contact implements Signal{

    private Boolean open;

    public Contact(Boolean status){
        this.open = status;
    }

    @Override
    public Object extract() {
        return open;
    }
}
