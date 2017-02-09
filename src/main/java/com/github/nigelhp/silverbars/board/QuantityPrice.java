package com.github.nigelhp.silverbars.board;

import java.math.BigDecimal;

public class QuantityPrice {

    private final BigDecimal quantity;
    private final Integer price;

    public QuantityPrice(BigDecimal quantity, Integer price) {
        this.quantity = quantity;
        this.price = price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        QuantityPrice that = (QuantityPrice) o;

        if (!quantity.equals(that.quantity)) return false;
        return price.equals(that.price);
    }

    @Override
    public int hashCode() {
        int result = quantity.hashCode();
        result = 31 * result + price.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return String.format("%f kg for £%s", quantity, price);
    }
}
