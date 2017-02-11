package com.github.nigelhp.silverbars.board;

import com.github.nigelhp.silverbars.order.Order;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static com.github.nigelhp.silverbars.board.QuantityPrice.PRICE_ASCENDING_COMPARATOR;
import static com.github.nigelhp.silverbars.board.QuantityPrice.PRICE_DESCENDING_COMPARATOR;
import static com.github.nigelhp.silverbars.order.Order.Type.BUY;
import static com.github.nigelhp.silverbars.order.Order.Type.SELL;

public class OrderBoard {

    @FunctionalInterface
    public interface Listener {
        void onOrderBoardChanged(Summary newSummary);
    }

    private final OrderTypeSummary buySummary;
    private final OrderTypeSummary sellSummary;
    private final List<Listener> listeners;

    public OrderBoard() {
        this(new OrderTypeSummary(BUY, PRICE_DESCENDING_COMPARATOR),
                new OrderTypeSummary(SELL, PRICE_ASCENDING_COMPARATOR));
    }

    OrderBoard(OrderTypeSummary buySummary, OrderTypeSummary sellSummary) {
        assert (buySummary != null) : "buySummary may not be null";
        assert (sellSummary != null) : "sellSummary may not be null";
        this.buySummary = buySummary;
        this.sellSummary = sellSummary;
        listeners = new CopyOnWriteArrayList<>();
    }

    public void register(Order order) {
        buySummary.register(order);
        sellSummary.register(order);
        fireOnOrderBoardChanged();
    }

    public void cancel(Order order) {
        buySummary.cancel(order);
        sellSummary.cancel(order);
        fireOnOrderBoardChanged();
    }

    public Summary getSummary() {
        return new Summary(buySummary.getSummary(), sellSummary.getSummary());
    }

    public void addListener(Listener listener) {
        listeners.add(listener);
    }

    public void removeListener(Listener listener){
        listeners.remove(listener);
    }

    private void fireOnOrderBoardChanged() {
        Summary summary = getSummary();
        listeners.forEach(listener -> listener.onOrderBoardChanged(summary));
    }
}
