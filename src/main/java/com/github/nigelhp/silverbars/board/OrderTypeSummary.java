package com.github.nigelhp.silverbars.board;

import com.github.nigelhp.silverbars.order.Order;

import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static java.util.stream.Collectors.toList;

class OrderTypeSummary {

    private final Order.Type orderType;
    private final Comparator<QuantityPrice> ordering;
    private final ConcurrentMap<Integer, QuantityPrice> entriesByPrice;

    OrderTypeSummary(Order.Type orderType, Comparator<QuantityPrice> ordering) {
        this.orderType = orderType;
        this.ordering = ordering;
        entriesByPrice = new ConcurrentHashMap<>();
    }

    void register(Order order) {
        if (order.getType() == orderType) {
            QuantityPrice orderEntry = new QuantityPrice(order.getQuantity(), order.getPrice());
            entriesByPrice.merge(order.getPrice(), orderEntry, QuantityPrice::add);
        }
    }

    List<QuantityPrice> getSummary() {
        return entriesByPrice.values().stream().sorted(ordering).collect(toList());
    }
}
