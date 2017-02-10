package com.github.nigelhp.silverbars.board;

import java.math.BigDecimal;
import java.util.Comparator;

import static java.util.Comparator.comparing;

public class QuantityPrice {

    public static final Comparator<QuantityPrice> PRICE_ASCENDING_COMPARATOR = comparing(QuantityPrice::getPrice);
    public static final Comparator<QuantityPrice> PRICE_DESCENDING_COMPARATOR = PRICE_ASCENDING_COMPARATOR.reversed();

    private final BigDecimal quantity;
    private final Integer price;

    public QuantityPrice(BigDecimal quantity, Integer price) {
        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Integer getPrice() {
        return price;
    }

    public QuantityPrice add(QuantityPrice other) {
        return new QuantityPrice(quantity.add(other.quantity), price);
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
        return String.format("%.1f kg for £%s", quantity, price);
    }
}
