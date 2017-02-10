package com.github.nigelhp.silverbars.board;

import com.github.nigelhp.silverbars.order.Order;
import org.junit.Test;

import java.util.List;

import static com.github.nigelhp.silverbars.board.CommonTestLanguage.aBuyOrder;
import static com.github.nigelhp.silverbars.board.CommonTestLanguage.aSellOrder;
import static com.github.nigelhp.silverbars.board.CommonTestLanguage.aSummaryEntry;
import static com.github.nigelhp.silverbars.board.CommonTestLanguage.price;
import static com.github.nigelhp.silverbars.board.CommonTestLanguage.quantity;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderBoardTest {

    private final OrderTypeSummary buySummary;
    private final OrderTypeSummary sellSummary;
    private OrderBoard underTest;

    public OrderBoardTest() {
        buySummary = mock(OrderTypeSummary.class);
        sellSummary = mock(OrderTypeSummary.class);
        underTest = new OrderBoard(buySummary, sellSummary);
    }

    @Test
    public void aBuyOrderIsRegisteredWithTheBuySummary() {
        Order order = aBuyOrder(quantity("3.5"), price(303));

        underTest.register(order);

        verify(buySummary).register(order);
    }

    @Test
    public void aSellOrderIsRegisteredWithTheSellSummary() {
        Order order = aSellOrder(quantity("3.5"), price(303));

        underTest.register(order);

        verify(sellSummary).register(order);
    }

    @Test
    public void aSummaryCombinesTheBuyAndSellSummaries() {
        List<QuantityPrice> buyEntries = asList(aSummaryEntry(quantity("3.5"), price(303)));
        List<QuantityPrice> sellEntries = asList(aSummaryEntry(quantity("2.0"), price(306)));
        when(buySummary.getSummary()).thenReturn(buyEntries);
        when(sellSummary.getSummary()).thenReturn(sellEntries);

        Summary summary = underTest.getSummary();

        assertThat(summary.getBuyEntries(), is(buyEntries));
        assertThat(summary.getSellEntries(), is(sellEntries));
    }

    @Test
    public void aSummaryOrdersBuyEntriesHighestPriceFirst() {
        underTest = new OrderBoard();
        underTest.register(aBuyOrder(quantity("2.5"), price(306)));
        underTest.register(aBuyOrder(quantity("2.5"), price(310)));
        underTest.register(aBuyOrder(quantity("2.5"), price(308)));

        Summary summary = underTest.getSummary();

        assertThat(summary.getBuyEntries(), contains(
                aSummaryEntry(quantity("2.5"), price(310)),
                aSummaryEntry(quantity("2.5"), price(308)),
                aSummaryEntry(quantity("2.5"), price(306))));
    }

    @Test
    public void aSummaryOrdersSellEntriesLowestPriceFirst() {
        underTest = new OrderBoard();
        underTest.register(aSellOrder(quantity("2.5"), price(308)));
        underTest.register(aSellOrder(quantity("2.5"), price(310)));
        underTest.register(aSellOrder(quantity("2.5"), price(306)));

        Summary summary = underTest.getSummary();

        assertThat(summary.getSellEntries(), contains(
                aSummaryEntry(quantity("2.5"), price(306)),
                aSummaryEntry(quantity("2.5"), price(308)),
                aSummaryEntry(quantity("2.5"), price(310))));
    }
}
