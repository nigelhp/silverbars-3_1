package com.github.nigelhp.silverbars.order;

import java.math.BigDecimal;

public class Order {

    public enum Type {
        BUY, SELL
    }

    private final String userId;
    private final Type type;
    private final BigDecimal quantity;
    private final Integer price;

    public Order(String userId, Type type, BigDecimal quantity, Integer price) {
        if (userId.trim().isEmpty()) {
            throw new IllegalArgumentException("userId may not be empty");
        }
        if (type == null){
            throw new IllegalArgumentException("type may not be null");
        }
        if (quantity.signum() <= 0) {
            throw new IllegalArgumentException("quantity must be positive");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("price must be positive");
        }

        this.userId = userId;
        this.type = type;
        this.quantity = quantity;
        this.price = price;
    }

    public String getUserId() {
        return userId;
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
