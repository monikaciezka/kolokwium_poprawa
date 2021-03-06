package edu.iis.mto.testreactor.reservation;

public class ReservationItem {

    private Product product;

    private int quantity;

    @SuppressWarnings("unused")
    private ReservationItem() {}

    ReservationItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    void changeQuantityBy(int change) {
        int changed = quantity + change;
        if (changed <= 0) {
            throw new DomainOperationException(null, "change below 1");
        }
        this.quantity = changed;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }
}
