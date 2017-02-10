package com.github.nigelhp.silverbars.board;

import com.github.nigelhp.silverbars.order.Order;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.Comparator;

import static com.github.nigelhp.silverbars.board.CommonTestLanguage.SOME_USER_ID;
import static com.github.nigelhp.silverbars.board.CommonTestLanguage.aBuyOrder;
import static com.github.nigelhp.silverbars.board.CommonTestLanguage.aSellOrder;
import static com.github.nigelhp.silverbars.board.CommonTestLanguage.aSummaryEntry;
import static com.github.nigelhp.silverbars.board.CommonTestLanguage.price;
import static com.github.nigelhp.silverbars.board.CommonTestLanguage.quantity;
import static com.github.nigelhp.silverbars.order.Order.Type.BUY;
import static com.github.nigelhp.silverbars.order.Order.Type.SELL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;

public class OrderTypeSummaryTest {

    private static final Order.Type SOME_ORDER_TYPE = BUY;
    private static final Comparator<QuantityPrice> SOME_ORDERING = Comparator.comparing(qp -> 0);

    @Test
    public void aSummaryIsEmptyWhenNoOrdersAreRegistered() {
        OrderTypeSummary underTest = new OrderTypeSummary(SOME_ORDER_TYPE, SOME_ORDERING);

        assertThat(underTest.getSummary(), empty());
    }

    @Test
    public void aBuySummaryAcceptsABuyOrderOnRegistration() {
        OrderTypeSummary underTest = new OrderTypeSummary(BUY, SOME_ORDERING);

        underTest.register(aBuyOrder(quantity("3.5"), price(306)));

        assertThat(underTest.getSummary(), contains(aSummaryEntry(quantity("3.5"), price(306))));
    }

    @Test
    public void aBuySummaryIgnoresASellOrderOnRegistration() {
        OrderTypeSummary underTest = new OrderTypeSummary(BUY, SOME_ORDERING);

        underTest.register(aSellOrder(quantity("3.5"), price(306)));

        assertThat(underTest.getSummary(), empty());
    }

    @Test
    public void aBuySummaryAcceptsABuyOrderOnCancellation() {
        OrderTypeSummary underTest = new OrderTypeSummary(BUY, SOME_ORDERING);
        Order order = aBuyOrder(quantity("3.5"), price(306));
        underTest.register(order);

        underTest.cancel(order);

        assertThat(underTest.getSummary(), empty());
    }

    @Test
    public void aBuySummaryIgnoresASellOrderOnCancellation() {
        OrderTypeSummary underTest = new OrderTypeSummary(BUY, SOME_ORDERING);
        underTest.register(aBuyOrder(quantity("3.5"), price(306)));

        underTest.cancel(aSellOrder(quantity("3.5"), price(306)));

        assertThat(underTest.getSummary(), contains(aSummaryEntry(quantity("3.5"), price(306))));
    }

    @Test
    public void aSellSummaryAcceptsASellOrderOnRegistration() {
        OrderTypeSummary underTest = new OrderTypeSummary(SELL, SOME_ORDERING);

        underTest.register(aSellOrder(quantity("3.5"), price(306)));

        assertThat(underTest.getSummary(), contains(aSummaryEntry(quantity("3.5"), price(306))));
    }

    @Test
    public void aSellSummaryIgnoresABuyOrderOnRegistration() {
        OrderTypeSummary underTest = new OrderTypeSummary(SELL, SOME_ORDERING);

        underTest.register(aBuyOrder(quantity("3.5"), price(306)));

        assertThat(underTest.getSummary(), empty());
    }

    @Test
    public void aSellSummaryAcceptsASellOrderOnCancellation() {
        OrderTypeSummary underTest = new OrderTypeSummary(SELL, SOME_ORDERING);
        Order order = aSellOrder(quantity("3.5"), price(306));
        underTest.register(order);

        underTest.cancel(order);

        assertThat(underTest.getSummary(), empty());
    }

    @Test
    public void aSellSummaryIgnoresABuyOrderOnCancellation() {
        OrderTypeSummary underTest = new OrderTypeSummary(SELL, SOME_ORDERING);
        underTest.register(aSellOrder(quantity("3.5"), price(306)));

        underTest.cancel(aBuyOrder(quantity("3.5"), price(306)));

        assertThat(underTest.getSummary(), contains(aSummaryEntry(quantity("3.5"), price(306))));
    }

    @Test
    public void aSummaryDifferentiatesOrdersWithDifferentPrices() {
        OrderTypeSummary underTest = new OrderTypeSummary(SOME_ORDER_TYPE, SOME_ORDERING);
        underTest.register(someOrder("user1", quantity("3.5"), price(306)));
        underTest.register(someOrder("user2", quantity("3.5"), price(307)));

        assertThat(underTest.getSummary(), containsInAnyOrder(
                aSummaryEntry(quantity("3.5"), price(306)),
                aSummaryEntry(quantity("3.5"), price(307))));
    }

    @Test
    public void aSummaryAggregatesOrdersWithTheSamePriceRegardlessOfUser() {
        OrderTypeSummary underTest = new OrderTypeSummary(SOME_ORDER_TYPE, SOME_ORDERING);
        underTest.register(someOrder("user1", quantity("3.5"), price(306)));
        underTest.register(someOrder("user2", quantity("2.0"), price(306)));

        assertThat(underTest.getSummary(), contains(aSummaryEntry(quantity("5.5"), price(306))));
    }

    @Test
    public void aSummaryRetainsAPriceEntryFollowingCancellationWhenQuantityIsNonZero() {
        OrderTypeSummary underTest = new OrderTypeSummary(SOME_ORDER_TYPE, SOME_ORDERING);
        Order mistakenOrder = someOrder("user1", quantity("3.5"), price(306));
        underTest.register(mistakenOrder);
        underTest.register(someOrder("user2", quantity("2.0"), price(306)));
        underTest.cancel(mistakenOrder);

        assertThat(underTest.getSummary(), contains(aSummaryEntry(quantity("2.0"), price(306))));
    }

    @Test
    public void aSummaryCanOrderEntriesHighestPriceFirst() {
        Comparator<QuantityPrice> highestPriceFirst = Comparator.comparing(QuantityPrice::getPrice).reversed();
        OrderTypeSummary underTest = new OrderTypeSummary(SOME_ORDER_TYPE, highestPriceFirst);
        underTest.register(someOrder(quantity("2.5"), price(306)));
        underTest.register(someOrder(quantity("2.5"), price(310)));
        underTest.register(someOrder(quantity("2.5"), price(308)));

        assertThat(underTest.getSummary(), contains(
                aSummaryEntry(quantity("2.5"), price(310)),
                aSummaryEntry(quantity("2.5"), price(308)),
                aSummaryEntry(quantity("2.5"), price(306))));
    }

    @Test
    public void aSummaryCanOrderEntriesLowestPriceFirst() {
        Comparator<QuantityPrice> lowestPriceFirst = Comparator.comparing(QuantityPrice::getPrice);
        OrderTypeSummary underTest = new OrderTypeSummary(SOME_ORDER_TYPE, lowestPriceFirst);
        underTest.register(someOrder(quantity("2.5"), price(308)));
        underTest.register(someOrder(quantity("2.5"), price(310)));
        underTest.register(someOrder(quantity("2.5"), price(306)));

        assertThat(underTest.getSummary(), contains(
                aSummaryEntry(quantity("2.5"), price(306)),
                aSummaryEntry(quantity("2.5"), price(308)),
                aSummaryEntry(quantity("2.5"), price(310))));
    }

    private static Order someOrder(BigDecimal quantity, Integer price) {
        return someOrder(SOME_USER_ID, quantity, price);
    }

    private static Order someOrder(String userId, BigDecimal quantity, Integer price) {
        return new Order(userId, SOME_ORDER_TYPE, quantity, price);
    }
}
