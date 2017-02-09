package com.github.nigelhp.silverbars.board;

import com.github.nigelhp.silverbars.order.Order;

import java.util.ArrayList;
import java.util.Collections;

public class OrderBoard {
    private Order order;

    public void register(Order order) {
        this.order = order;
    }

    public Summary getSummary() {
        if (order == null) {
            return new Summary(new ArrayList<>(), new ArrayList<>());
        } else {
            if (order.getType() == Order.Type.BUY) {
                return new Summary(Collections.singletonList(new QuantityPrice(order.getQuantity(), order.getPrice())), Collections.emptyList());
            } else if (order.getType() == Order.Type.SELL) {
                return new Summary(Collections.emptyList(), Collections.singletonList(new QuantityPrice(order.getQuantity(), order.getPrice())));
            } else {
                throw new IllegalStateException("order not a buy or sell");
            }
        }
    }
}
