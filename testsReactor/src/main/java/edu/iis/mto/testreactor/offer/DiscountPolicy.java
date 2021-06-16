package edu.iis.mto.testreactor.offer;

import edu.iis.mto.testreactor.money.Money;
import edu.iis.mto.testreactor.reservation.Product;

public interface DiscountPolicy {

    public Discount applyDiscount(Product product, int quantity, Money regularCost);
}
