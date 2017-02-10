package com.github.nigelhp.silverbars.board;

import com.github.nigelhp.silverbars.order.Order;

import static com.github.nigelhp.silverbars.board.QuantityPrice.PRICE_ASCENDING_COMPARATOR;
import static com.github.nigelhp.silverbars.board.QuantityPrice.PRICE_DESCENDING_COMPARATOR;
import static com.github.nigelhp.silverbars.order.Order.Type.BUY;
import static com.github.nigelhp.silverbars.order.Order.Type.SELL;

public class OrderBoard {

    private final OrderTypeSummary buySummary;
    private final OrderTypeSummary sellSummary;

    public OrderBoard() {
        this(new OrderTypeSummary(BUY, PRICE_DESCENDING_COMPARATOR),
                new OrderTypeSummary(SELL, PRICE_ASCENDING_COMPARATOR));
    }

    OrderBoard(OrderTypeSummary buySummary, OrderTypeSummary sellSummary) {
        assert (buySummary != null) : "buySummary may not be null";
        assert (sellSummary != null) : "sellSummary may not be null";
        this.buySummary = buySummary;
        this.sellSummary = sellSummary;
    }

    public void register(Order order) {
        buySummary.register(order);
        sellSummary.register(order);
    }

    public Summary getSummary() {
        return new Summary(buySummary.getSummary(), sellSummary.getSummary());
    }
}
