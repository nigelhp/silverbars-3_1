package com.github.nigelhp.silverbars.order;

import org.junit.Test;

import static com.github.nigelhp.silverbars.order.Order.Type.BUY;
import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;

public class OrderTest {

    @Test(expected = NullPointerException.class)
    public void userIdMayNotBeNull() {
        new Order(null, BUY, ONE, 300);
    }

    @Test(expected = IllegalArgumentException.class)
    public void userIdMayNotBeEmpty() {
        new Order("   ", BUY, ONE, 300);
    }

    @Test(expected = IllegalArgumentException.class)
    public void typeMayNotBeNull() {
        new Order("user1", null, ONE, 300);
    }

    @Test(expected = IllegalArgumentException.class)
    public void quantityMayNotBeNegative() {
        new Order("user1", BUY, ONE.negate(), 300);
    }

    @Test(expected = IllegalArgumentException.class)
    public void quantityMayNotBeZero() {
        new Order("user1", BUY, ZERO, 300);
    }

    @Test(expected = IllegalArgumentException.class)
    public void priceMayNotBeNegative() {
        new Order("user1", BUY, ONE, -1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void priceMayNotBeZero() {
        new Order("user1", BUY, ONE, 0);
    }
}
