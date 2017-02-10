package com.github.nigelhp.silverbars.board;

import org.junit.Test;

import java.math.BigDecimal;
import java.util.List;

import static com.github.nigelhp.silverbars.board.CommonTestLanguage.price;
import static com.github.nigelhp.silverbars.board.CommonTestLanguage.quantity;
import static com.github.nigelhp.silverbars.board.QuantityPrice.PRICE_ASCENDING_COMPARATOR;
import static com.github.nigelhp.silverbars.board.QuantityPrice.PRICE_DESCENDING_COMPARATOR;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.fail;

public class QuantityPriceTest {

    @Test(expected = IllegalArgumentException.class)
    public void quantityMayNotBeNegative() {
        new QuantityPrice(BigDecimal.ONE.negate(), price(303));
    }

    @Test(expected = IllegalArgumentException.class)
    public void priceMayNotBeNegative() {
        new QuantityPrice(quantity("2.0"), price(-1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void priceMayNotBeZero() {
        new QuantityPrice(quantity("2.0"), price(0));
    }

    @Test
    public void additionRequiresMatchingPrices() {
        QuantityPrice quantityPrice = new QuantityPrice(quantity("2.0"), price(303));
        QuantityPrice other = new QuantityPrice(quantity("2.0"), price(302));
        try {
            quantityPrice.add(other);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void additionIsCommutative() {
        QuantityPrice a = new QuantityPrice(quantity("2.0"), price(303));
        QuantityPrice b = new QuantityPrice(quantity("3.5"), price(303));
        QuantityPrice sum = new QuantityPrice(quantity("5.5"), price(303));

        assertThat("a+b", a.add(b), is(sum));
        assertThat("b+a", b.add(a), is(sum));
    }

    @Test
    public void additionIsAssociative() {
        QuantityPrice a = new QuantityPrice(quantity("2.0"), price(303));
        QuantityPrice b = new QuantityPrice(quantity("3.5"), price(303));
        QuantityPrice c = new QuantityPrice(quantity("1.5"), price(303));
        QuantityPrice sum = new QuantityPrice(quantity("7.0"), price(303));

        assertThat("a+b+c", a.add(b).add(c), is(sum));
        assertThat("(b+c)+a", b.add(c).add(a), is(sum));
    }

    @Test
    public void additionOfIdentityValueDoesNotChangeQuantity() {
        QuantityPrice a = new QuantityPrice(quantity("2.0"), price(303));
        QuantityPrice zero = new QuantityPrice(quantity("0.0"), price(303));

        assertThat(a.add(zero), is(a));
    }

    @Test
    public void subtractionRequiresMatchingPrices() {
        QuantityPrice quantityPrice = new QuantityPrice(quantity("2.0"), price(303));
        QuantityPrice other = new QuantityPrice(quantity("2.0"), price(302));
        try {
            quantityPrice.subtract(other);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void subtractionReturnsTheDifference() {
        QuantityPrice a = new QuantityPrice(quantity("3.0"), price(303));
        QuantityPrice b = new QuantityPrice(quantity("1.0"), price(303));

        assertThat(a.subtract(b), is(new QuantityPrice(quantity("2.0"), price(303))));
    }

    @Test
    public void subtractionOfSelfReturnsZero() {
        QuantityPrice a = new QuantityPrice(quantity("3.0"), price(303));

        assertThat(a.subtract(a), is(new QuantityPrice(quantity("0.0"), price(303))));
    }

    @Test
    public void subtractionOfIdentityValueDoesNotChangeQuantity() {
        QuantityPrice a = new QuantityPrice(quantity("3.0"), price(303));
        QuantityPrice zero = new QuantityPrice(quantity("0.0"), price(303));

        assertThat(a.subtract(zero), is(a));
    }

    @Test
    public void subtractionDoesNotPermitANegativeResultQuantity() {
        QuantityPrice a = new QuantityPrice(quantity("1.0"), price(303));
        QuantityPrice b = new QuantityPrice(quantity("3.0"), price(303));

        try {
            a.subtract(b);
            fail("Should have thrown IllegalArgumentException");
        } catch (IllegalArgumentException expected) {
            // expected
        }
    }

    @Test
    public void priceAscendingComparator() {
        QuantityPrice price308 = new QuantityPrice(quantity("1.5"), price(308));
        QuantityPrice price310 = new QuantityPrice(quantity("2.5"), price(310));
        QuantityPrice price306 = new QuantityPrice(quantity("3.5"), price(306));
        List<QuantityPrice> entries = asList(price308, price310, price306);

        entries.sort(PRICE_ASCENDING_COMPARATOR);

        assertThat(entries, contains(price306, price308, price310));
    }

    @Test
    public void priceDescendingComparator() {
        QuantityPrice price306 = new QuantityPrice(quantity("3.5"), price(306));
        QuantityPrice price310 = new QuantityPrice(quantity("2.5"), price(310));
        QuantityPrice price308 = new QuantityPrice(quantity("1.5"), price(308));
        List<QuantityPrice> entries = asList(price306, price310, price308);

        entries.sort(PRICE_DESCENDING_COMPARATOR);

        assertThat(entries, contains(price310, price308, price306));
    }
}
