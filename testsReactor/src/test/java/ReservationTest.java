import edu.iis.mto.testreactor.money.Money;
import edu.iis.mto.testreactor.offer.Discount;
import edu.iis.mto.testreactor.offer.DiscountPolicy;
import edu.iis.mto.testreactor.offer.Offer;
import edu.iis.mto.testreactor.offer.OfferItem;
import edu.iis.mto.testreactor.reservation.*;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ReservationTest {

    Reservation reservation;
    @Mock
    ClientData client;
    @Mock
    DiscountPolicy discountPolicy;
    @Mock
    Product mockProduct;
    Date date;
    Id id;

    @BeforeEach
    void setUp() {
        id = Id.generate();

    }

    @Test
    void emptyItemListShouldReturnEmptyOffer() {
        reservation = new Reservation(id, Reservation.ReservationStatus.OPENED, client, date);
        Offer offer = reservation.calculateOffer(discountPolicy);
        assertEquals(Collections.emptyList(), offer.getAvailabeItems());
        assertEquals(Collections.emptyList(), offer.getUnavailableItems());
    }

    @Test
    void addingProductWhenClosedShouldProduceError() {
        Product product = new Product(Id.generate(), new Money(100), "Product", ProductType.FOOD);
        reservation = new Reservation(id, Reservation.ReservationStatus.CLOSED, client, date);
        assertThrows(DomainOperationException.class, ()-> { reservation.add(product, 1);});
    }

    @Test
    void shouldAddProductSuccessfully() {
        Product product = new Product(Id.generate(), new Money(100), "Product", ProductType.FOOD);
        reservation = new Reservation(id, Reservation.ReservationStatus.OPENED, client, date);
        reservation.add(product, 10);
        assertEquals(reservation.contains(product), true);

    }

    @Test
    void shouldReturnListOfOneAvailableProduct() {
        Product product = new Product(Id.generate(), new Money(100), "Product", ProductType.FOOD);
        reservation = new Reservation(id, Reservation.ReservationStatus.OPENED, client, date);
        reservation.add(product, 10);
        when(discountPolicy.applyDiscount(product, 10, new Money((100)))).thenReturn(new Discount("promotion", new Money(5)));
        Offer offer = reservation.calculateOffer(discountPolicy);
        assertEquals(offer.getAvailabeItems().size(), 1);

    }

    @Test
    void whenAddingMultipleOfTheSameProductReturnOneOfferItem() {
        Product product = new Product(Id.generate(), new Money(100), "Product", ProductType.FOOD);
        reservation = new Reservation(id, Reservation.ReservationStatus.OPENED, client, date);
        reservation.add(product, 10);
        reservation.add(product, 5);
        when(discountPolicy.applyDiscount(product, 15, new Money((100)))).thenReturn(new Discount("promotion", new Money(5)));
        Offer offer = reservation.calculateOffer(discountPolicy);
        assertEquals(offer.getAvailabeItems().size(), 1);
    }

    @Test
    void whenTryingToCloseClosedShouldThroughException() {
        reservation = new Reservation(id, Reservation.ReservationStatus.CLOSED, client, date);
        assertThrows(DomainOperationException.class, ()->{reservation.close();});
    }

    @Test
    void shouldReturnOfferWithTwoAvailableProducts() {
        Product product = new Product(Id.generate(), new Money(100), "Product", ProductType.FOOD);
        Product product2 = new Product(Id.generate(), new Money(50), "Product", ProductType.DRUG);
        reservation = new Reservation(id, Reservation.ReservationStatus.OPENED, client, date);
        reservation.add(product, 10);
        reservation.add(product2, 5);
        when(discountPolicy.applyDiscount(product, 10, new Money((100)))).thenReturn(new Discount("promotion", new Money(5)));
        when(discountPolicy.applyDiscount(product2, 5, new Money((50)))).thenReturn(new Discount("promotion", new Money(5)));

        Offer offer = reservation.calculateOffer(discountPolicy);
        assertEquals(offer.getAvailabeItems().size(), 2);
    }
}
