package com.github.nigelhp.silverbars.board;

import org.junit.Test;

import static com.github.nigelhp.silverbars.board.CommonTestLanguage.price;
import static com.github.nigelhp.silverbars.board.CommonTestLanguage.quantity;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class QuantityPriceTest {

    @Test
    public void add() {
        QuantityPrice a = new QuantityPrice(quantity("1"), price(303));

        QuantityPrice result = a.add(a);

        assertThat(result, is(new QuantityPrice(quantity("2"), price(303))));
    }
}
