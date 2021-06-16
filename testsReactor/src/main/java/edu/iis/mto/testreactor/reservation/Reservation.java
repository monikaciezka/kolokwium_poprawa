package edu.iis.mto.testreactor.reservation;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.iis.mto.testreactor.money.Money;
import edu.iis.mto.testreactor.offer.Discount;
import edu.iis.mto.testreactor.offer.DiscountPolicy;
import edu.iis.mto.testreactor.offer.Offer;
import edu.iis.mto.testreactor.offer.OfferItem;

public class Reservation {

    public enum ReservationStatus {
        OPENED,
        CLOSED
    }

    private Id id;
    private ReservationStatus status;

    private List<ReservationItem> items;

    private ClientData clientData;

    private Date createDate;

    @SuppressWarnings("unused")
    private Reservation() {}

    Reservation(Id id, ReservationStatus status, ClientData clientData, Date createDate) {
        this.id = id;
        this.status = status;
        this.clientData = clientData;
        this.createDate = createDate;
        this.items = new ArrayList<>();
    }

    public void add(Product product, int quantity) {
        if (isClosed()) {
            domainError("Reservation already closed");
        }
        if (!product.isAvailable()) {
            domainError("Product is no longer available");
        }

        if (contains(product)) {
            increase(product, quantity);
        } else {
            addNew(product, quantity);
        }
    }

    public Offer calculateOffer(DiscountPolicy discountPolicy) {
        List<OfferItem> availableItems = new ArrayList<>();
        List<OfferItem> unavailableItems = new ArrayList<>();

        for (ReservationItem item : items) {
            Product product = item.getProduct();
            if (product.isAvailable()) {
                Discount discount = discountPolicy.applyDiscount(product, item.getQuantity(), product.getPrice());
                OfferItem offerItem = new OfferItem(product.generateSnapshot(), item.getQuantity(), discount);

                availableItems.add(offerItem);
            } else {
                OfferItem offerItem = new OfferItem(product.generateSnapshot(), item.getQuantity());

                unavailableItems.add(offerItem);
            }
        }

        return new Offer(availableItems, unavailableItems);
    }

    private void addNew(Product product, int quantity) {
        ReservationItem item = new ReservationItem(product, quantity);
        items.add(item);
    }

    private void increase(Product product, int quantity) {
        for (ReservationItem item : items) {
            if (item.getProduct()
                    .equals(product)) {
                item.changeQuantityBy(quantity);
                break;
            }
        }
    }

    public boolean contains(Product product) {
        for (ReservationItem item : items) {
            if (item.getProduct()
                    .equals(product)) {
                return true;
            }
        }
        return false;
    }

    public boolean isClosed() {
        return status.equals(ReservationStatus.CLOSED);
    }

    public void close() {
        if (isClosed()) {
            domainError("Reservation is already closed");
        }
        status = ReservationStatus.CLOSED;
    }

    public List<ReservedProduct> getReservedProducts() {
        ArrayList<ReservedProduct> result = new ArrayList<>(items.size());

        for (ReservationItem item : items) {
            result.add(new ReservedProduct(item.getProduct()
                                               .getId(),
                    item.getProduct()
                        .getName(),
                    item.getQuantity(), calculateItemCost(item)));
        }

        return result;
    }

    private Money calculateItemCost(ReservationItem item) {
        return item.getProduct()
                   .getPrice()
                   .multiplyBy(item.getQuantity());
    }

    public ClientData getClientData() {
        return clientData;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public ReservationStatus getStatus() {
        return status;
    }

    private void domainError(String message) {
        throw new DomainOperationException(id, message);
    }
}
