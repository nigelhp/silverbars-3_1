package com.github.nigelhp.silverbars.order;

import java.math.BigDecimal;

public class Order {

    public enum Type {
        BUY, SELL
    }

    private final Type type;
    private final BigDecimal quantity;
    private final Integer price;

    public Order(Type type, BigDecimal quantity, Integer price) {
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }

    public Type getType() {
        return type;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Integer getPrice() {
        return price;
    }
}
