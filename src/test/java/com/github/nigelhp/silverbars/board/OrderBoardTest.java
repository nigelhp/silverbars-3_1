package com.github.nigelhp.silverbars.board;

import com.github.nigelhp.silverbars.order.Order;
import org.junit.Test;

import java.math.BigDecimal;

import static com.github.nigelhp.silverbars.board.TestLanguage.price;
import static com.github.nigelhp.silverbars.board.TestLanguage.quantity;
import static com.github.nigelhp.silverbars.order.Order.Type.BUY;
import static com.github.nigelhp.silverbars.order.Order.Type.SELL;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.is;

public class OrderBoardTest {

    private static final String SOME_USER_ID = "user1";

    private final OrderBoard underTest;

    public OrderBoardTest() {
        underTest = new OrderBoard();
    }

    @Test
    public void anOrderBoardSummary_whenThereAreNoOrders() {
        Summary summary = underTest.getSummary();

        assertThat("buy", summary.getBuyEntries(), is(empty()));
        assertThat("sell", summary.getSellEntries(), is(empty()));
    }

    @Test
    public void anOrderBoardSummary_whenOnlyABuyOrderIsRegistered() {
        underTest.register(aBuyOrder(quantity("3.5"), price(306)));

        Summary summary = underTest.getSummary();

        assertThat("buy", summary.getBuyEntries(), contains(aSummaryEntry(quantity("3.5"), price(306))));
        assertThat("sell", summary.getSellEntries(), is(empty()));
    }

    @Test
    public void anOrderBoardSummary_whenOnlyASellOrderIsRegistered() {
        underTest.register(aSellOrder(quantity("3.5"), price(306)));

        Summary summary = underTest.getSummary();

        assertThat("buy", summary.getBuyEntries(), is(empty()));
        assertThat("sell", summary.getSellEntries(), contains(aSummaryEntry(quantity("3.5"), price(306))));
    }

    @Test
    public void anOrderBoardSummaryAggregatesSellOrdersWithTheSamePriceRegardlessOfUser() {
        underTest.register(aSellOrder("user1", quantity("3.5"), price(306)));
        underTest.register(aSellOrder("user2", quantity("2.0"), price(306)));

        Summary summary = underTest.getSummary();

        assertThat(summary.getSellEntries(), contains(aSummaryEntry(quantity("5.5"), price(306))));
    }

    private static Order aBuyOrder(BigDecimal quantity, Integer price) {
        return aBuyOrder(SOME_USER_ID, quantity, price);
    }

    private static Order aBuyOrder(String userId, BigDecimal quantity, Integer price) {
        return new Order(userId, BUY, quantity, price);
    }

    private static Order aSellOrder(BigDecimal quantity, Integer price) {
        return aSellOrder(SOME_USER_ID, quantity, price);
    }

    private static Order aSellOrder(String userId, BigDecimal quantity, Integer price) {
        return new Order("user1", SELL, quantity, price);
    }

    private static QuantityPrice aSummaryEntry(BigDecimal quantity, Integer price) {
        return new QuantityPrice(quantity, price);
    }
}
