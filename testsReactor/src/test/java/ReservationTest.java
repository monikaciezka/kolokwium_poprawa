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
        Reservation.ReservationStatus status = Reservation.ReservationStatus.OPENED; //.CLOSED

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
    }

    @Test
    @Disabled
    void addingUnavailableProductShouldProduceError() {
      //  Product product = new Product(Id.generate(), new Money(100), "Product", ProductType.FOOD);
        reservation = new Reservation(id, Reservation.ReservationStatus.CLOSED, client, date);
        when(mockProduct.isAvailable()).thenReturn(false);
        assertThrows(DomainOperationException.class, ()-> { reservation.add(mockProduct, 1);});
    }

    @Test
    void name() {
        Product product = new Product(Id.generate(), new Money(100), "Product", ProductType.FOOD);
        reservation = new Reservation(id, Reservation.ReservationStatus.CLOSED, client, date);
        reservation.add(product, 10);
        reservation.add(product, 5);
        when(discountPolicy.applyDiscount(any(), any(), any())).thenReturn(new Discount("promotion", new Money(5)));
        Offer offer = reservation.calculateOffer(discountPolicy);
        assertEquals(1, offer.getAvailabeItems().size());
    }
}
