package com.github.nigelhp.silverbars.board;

import java.math.BigDecimal;

class CommonTestLanguage {
    static BigDecimal quantity(String value) {
        return new BigDecimal(value);
    }

    static Integer price(int value) {
        return value;
    }
}
