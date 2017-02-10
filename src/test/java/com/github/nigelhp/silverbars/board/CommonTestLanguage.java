package com.github.nigelhp.silverbars.board;

import com.github.nigelhp.silverbars.order.Order;

import java.math.BigDecimal;

import static com.github.nigelhp.silverbars.order.Order.Type.BUY;
import static com.github.nigelhp.silverbars.order.Order.Type.SELL;

class CommonTestLanguage {

    static final String SOME_USER_ID = "user1";

    static BigDecimal quantity(String value) {
        return new BigDecimal(value);
    }

    static Integer price(int value) {
        return value;
    }

    static Order aBuyOrder(BigDecimal quantity, Integer price) {
        return aBuyOrder(SOME_USER_ID, quantity, price);
    }

    static Order aBuyOrder(String userId, BigDecimal quantity, Integer price) {
        return new Order(userId, BUY, quantity, price);
    }

    static Order aSellOrder(BigDecimal quantity, Integer price) {
        return aSellOrder(SOME_USER_ID, quantity, price);
    }

    static Order aSellOrder(String userId, BigDecimal quantity, Integer price) {
        return new Order(userId, SELL, quantity, price);
    }

    static QuantityPrice aSummaryEntry(BigDecimal quantity, Integer price) {
        return new QuantityPrice(quantity, price);
    }
}
