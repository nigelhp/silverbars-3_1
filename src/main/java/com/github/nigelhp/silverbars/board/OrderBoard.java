package com.github.nigelhp.silverbars.board;

import com.github.nigelhp.silverbars.order.Order;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import static com.github.nigelhp.silverbars.order.Order.Type.BUY;
import static com.github.nigelhp.silverbars.order.Order.Type.SELL;

public class OrderBoard {

    private final ConcurrentMap<Integer, QuantityPrice> buysByPrice;
    private final ConcurrentMap<Integer, QuantityPrice> sellsByPrice;

    public OrderBoard() {
        buysByPrice = new ConcurrentHashMap<>();
        sellsByPrice = new ConcurrentHashMap<>();
    }

    public void register(Order order) {
        QuantityPrice orderEntry = new QuantityPrice(order.getQuantity(), order.getPrice());

        if (order.getType() == BUY) {
            buysByPrice.merge(order.getPrice(), orderEntry, QuantityPrice::add);
        } else if (order.getType() == SELL) {
            sellsByPrice.merge(order.getPrice(), orderEntry, QuantityPrice::add);
        } else {
            throw new AssertionError(String.format("Order has unrecognised type: [%s]", order.getType()));
        }
    }

    public Summary getSummary() {
        Comparator<QuantityPrice> priceDescending = Comparator.comparing(QuantityPrice::getPrice).reversed();
        Comparator<QuantityPrice> priceAscending = Comparator.comparing(QuantityPrice::getPrice);

        List<QuantityPrice> buys = new ArrayList<>(buysByPrice.values());
        buys.sort(priceDescending);

        List<QuantityPrice> sells = new ArrayList<>(sellsByPrice.values());
        sells.sort(priceAscending);

        return new Summary(buys, sells);
    }
}
