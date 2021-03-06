package com.github.nigelhp.silverbars.board;

import com.github.nigelhp.silverbars.order.Order;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Consumer;

import static java.util.stream.Collectors.toList;

class OrderTypeSummary {

    private static final QuantityPrice REMOVE_ENTRY = null;

    private final Order.Type orderType;
    private final Comparator<QuantityPrice> ordering;
    private final ConcurrentMap<Integer, QuantityPrice> entriesByPrice;

    OrderTypeSummary(Order.Type orderType, Comparator<QuantityPrice> ordering) {
        assert (orderType != null) : "orderType may not be null";
        assert (ordering != null) : "ordering may not be null";
        this.orderType = orderType;
        this.ordering = ordering;
        entriesByPrice = new ConcurrentHashMap<>();
    }

    void register(Order order) {
        processOrder(order, orderEntry -> entriesByPrice.merge(order.getPrice(), orderEntry, QuantityPrice::add));
    }

    void cancel(Order order) {
        processOrder(order, orderEntry -> entriesByPrice.computeIfPresent(order.getPrice(), (price, existingEntry) -> {
            QuantityPrice newEntry = existingEntry.subtract(orderEntry);
            return (newEntry.getQuantity().signum() == 0) ? REMOVE_ENTRY : newEntry;
        }));
    }

    private void processOrder(Order order, Consumer<QuantityPrice> action) {
        if (order.getType() == orderType) {
            QuantityPrice orderEntry = new QuantityPrice(order.getQuantity(), order.getPrice());
            action.accept(orderEntry);
        }
    }

    List<QuantityPrice> getSummary() {
        return entriesByPrice.values().stream().sorted(ordering).collect(toList());
    }
}
