package edu.iis.mto.testreactor.reservation;

import edu.iis.mto.testreactor.money.Money;

public class ReservedProduct {

    private String name;

    private Money totalCost;

    private Id productId;

    private int quantity;

    public ReservedProduct(Id productId, String name, int quantity, Money totalCost) {
        this.productId = productId;
        this.name = name;
        this.quantity = quantity;
        this.totalCost = totalCost;
    }

    public String getName() {
        return name;
    }

    public Money getTotalCost() {
        return totalCost;
    }

    public Id getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
