package com.github.nigelhp.silverbars.board;

import com.github.nigelhp.silverbars.order.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class OrderBoard {

    private final ConcurrentMap<Integer, QuantityPrice> sells;
    private Order order;

    public OrderBoard() {
        sells = new ConcurrentHashMap<>();
    }

    public void register(Order order) {
        this.order = order;

        if (order.getType() == Order.Type.SELL) {
            QuantityPrice orderEntry = new QuantityPrice(order.getQuantity(), order.getPrice());
            sells.merge(order.getPrice(), orderEntry, QuantityPrice::add);
        }
    }

    public Summary getSummary() {
        if (order == null) {
            return new Summary(new ArrayList<>(), new ArrayList<>(sells.values()));
        } else {
            if (order.getType() == Order.Type.BUY) {
                return new Summary(Collections.singletonList(new QuantityPrice(order.getQuantity(), order.getPrice())), new ArrayList<>(sells.values()));
            } else {
                return new Summary(Collections.emptyList(), new ArrayList<>(sells.values()));
            }
        }
    }
}
