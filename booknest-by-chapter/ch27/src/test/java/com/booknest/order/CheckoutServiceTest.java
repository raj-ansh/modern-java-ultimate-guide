package com.booknest.order;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.booknest.cart.Cart;
import com.booknest.catalog.Book;
import com.booknest.common.Repository;
import com.booknest.payment.Card;
import com.booknest.pricing.MembershipLevel;
import com.booknest.pricing.Promotion.NoPromotion;
import com.booknest.pricing.ShippingPolicy;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CheckoutServiceTest {

    private static final Instant FRIDAY_NOON =
            Instant.parse("2026-03-06T18:00:00Z");

    @Mock
    private Repository<Order, String> orders;    // a mock: we choose
    @Mock                                         // its answers and
    private OrderLog orderLog;                    // check its calls

    private final Clock clock =
            Clock.fixed(FRIDAY_NOON, ZoneId.of("America/Chicago"));
    private final Cart cart = new Cart();

    @BeforeEach
    void oneBookInTheCart() {
        cart.add(new Book("The Quiet Library", "Asha Rao", "12.99"),
                1);
    }

    private CheckoutService service() {
        return new CheckoutService(orders, orderLog,
                ShippingPolicy.standard(), clock);
    }

    private CheckoutService.Placed checkOut(CheckoutService service) {
        return service.checkout(cart, MembershipLevel.NONE,
                new NoPromotion(), new Card("4242"));
    }

    @Test
    void savesAndLogsThePricedOrder() throws IOException {
        CheckoutService.Placed placed = checkOut(service());

        ArgumentCaptor<Order> saved =
                ArgumentCaptor.forClass(Order.class);
        verify(orders).save(saved.capture());
        Order order = saved.getValue();
        assertEquals("BN-1001", order.id());
        assertEquals(new BigDecimal("19.05"), order.total());
        assertEquals(FRIDAY_NOON, order.placedAt());
        verify(orderLog).append(order);
        assertTrue(placed.logged());
    }

    @Test
    void numbersContinueAfterStoredOrders() {
        when(orders.count()).thenReturn(41);    // stubbing an answer

        CheckoutService service = service();
        assertEquals("BN-1042", checkOut(service).order().id());
        assertEquals("BN-1043", checkOut(service).order().id());
    }

    @Test
    void aFailedLogWriteDoesNotLoseTheSale() throws IOException {
        doThrow(new IOException("disk full"))
                .when(orderLog).append(any());

        CheckoutService.Placed placed = checkOut(service());

        verify(orders).save(placed.order());    // still stored
        assertFalse(placed.logged());           // and reported
    }
}
