package com.github.nigelhp.silverbars.board;

import java.math.BigDecimal;
import java.util.Comparator;

import static java.util.Comparator.comparing;

public class QuantityPrice {

    static final Comparator<QuantityPrice> PRICE_ASCENDING_COMPARATOR = comparing(QuantityPrice::getPrice);
    static final Comparator<QuantityPrice> PRICE_DESCENDING_COMPARATOR = PRICE_ASCENDING_COMPARATOR.reversed();

    private final BigDecimal quantity;
    private final Integer price;

    public QuantityPrice(BigDecimal quantity, Integer price) {
        if (quantity.signum() < 0) {
            throw new IllegalArgumentException(String.format("quantity may not be negative [%s]", quantity));
        }
        if (price <= 0) {
            throw new IllegalArgumentException(String.format("price must be positive [%s]", price));
        }

        this.quantity = quantity;
        this.price = price;
    }

    public BigDecimal getQuantity() {
        return quantity;
    }

    public Integer getPrice() {
        return price;
    }

    QuantityPrice add(QuantityPrice other) {
        if (price.equals(other.getPrice())) {
            return new QuantityPrice(quantity.add(other.quantity), price);
        } else {
            throw new IllegalArgumentException(String.format("Cannot add a [%s] with price [%s] to one with a price of [%s]",
                    this.getClass().getSimpleName(), other.getPrice(), price));
        }
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
