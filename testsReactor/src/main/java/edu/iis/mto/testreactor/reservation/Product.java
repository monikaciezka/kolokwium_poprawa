package edu.iis.mto.testreactor.reservation;

import java.util.Date;

import edu.iis.mto.testreactor.money.Money;

public class Product {

    private Id id;
    private Money price;

    private String name;

    private ProductType productType;
    private ProductStatus status;

    public Product(Id aggregateId, Money price, String name, ProductType productType) {
        this.id = aggregateId;
        this.price = price;
        this.name = name;
        this.productType = productType;
    }

    public Id getId() {
        return id;
    }

    public boolean isAvailable() {
        return !isRemoved();
    }

    public boolean isRemoved() {
        return status == ProductStatus.ARCHIVE;
    }

    public Money getPrice() {
        return price;
    }

    public String getName() {
        return name;
    }

    public ProductType getProductType() {
        return productType;
    }

    public ProductData generateSnapshot() {
        return new ProductData(getId(), price, name, productType, new Date());
    }
}
