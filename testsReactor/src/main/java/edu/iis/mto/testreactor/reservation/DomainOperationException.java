package edu.iis.mto.testreactor.reservation;

public class DomainOperationException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -1698858061607208429L;
    private Id id;

    public DomainOperationException(Id id, String string) {
        super(string);
        this.id = id;
    }

    public Id getId() {
        return id;
    }
}
