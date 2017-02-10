package com.github.nigelhp.silverbars.test.acceptance.support;

import com.github.nigelhp.silverbars.board.QuantityPrice;
import com.github.nigelhp.silverbars.board.Summary;
import cucumber.api.DataTable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class DataTableToSummaryConverter {

    public Summary convert(DataTable table) {
        List<QuantityPrice> buyEntries = new ArrayList<>();
        List<QuantityPrice> sellEntries = new ArrayList<>();
        List<List<String>> cells = table.cells(1);
        for (List<String> row : cells) {
            if (!row.get(0).trim().isEmpty()) {
                QuantityPrice buyEntry = new QuantityPrice(new BigDecimal(row.get(0)), Integer.valueOf(row.get(1)));
                buyEntries.add(buyEntry);
            }

            if (!row.get(2).trim().isEmpty()) {
                QuantityPrice sellEntry = new QuantityPrice(new BigDecimal(row.get(2)), Integer.valueOf(row.get(3)));
                sellEntries.add(sellEntry);
            }
        }

        return new Summary(buyEntries, sellEntries);
    }
}
