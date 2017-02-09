package com.github.nigelhp.silverbars.board;

import java.util.List;

public class Summary {

    private final List<QuantityPrice> buyEntries;
    private final List<QuantityPrice> sellEntries;

    public Summary(List<QuantityPrice> buyEntries, List<QuantityPrice> sellEntries) {
        this.buyEntries = buyEntries;
        this.sellEntries = sellEntries;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Summary summary = (Summary) o;

        if (!buyEntries.equals(summary.buyEntries)) return false;
        return sellEntries.equals(summary.sellEntries);
    }

    @Override
    public int hashCode() {
        int result = buyEntries.hashCode();
        result = 31 * result + sellEntries.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Summary{" +
                "buyEntries=" + buyEntries +
                ", sellEntries=" + sellEntries +
                '}';
    }
}
