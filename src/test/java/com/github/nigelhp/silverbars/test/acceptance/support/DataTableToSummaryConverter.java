package com.github.nigelhp.silverbars.test.acceptance.support;

import com.github.nigelhp.silverbars.board.QuantityPrice;
import com.github.nigelhp.silverbars.board.Summary;
import cucumber.api.DataTable;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DataTableToSummaryConverter {

    private static final int FIRST_ROW_INDEX = 1;
    private static final int COLUMN_BUY_QTY = 0;
    private static final int COLUMN_BUY_PRICE = 1;
    private static final int COLUMN_SELL_QTY = 2;
    private static final int COLUMN_SELL_PRICE = 3;

    public Summary convert(DataTable table) {
        List<List<String>> cells = table.cells(FIRST_ROW_INDEX);
        return cells.stream().reduce(emptySummary(), DataTableToSummaryConverter::addDataRow,
                DataTableToSummaryConverter::addSummary);
    }

    private static Summary emptySummary() {
        return new Summary(new ArrayList<>(), new ArrayList<>());
    }

    private static Summary addDataRow(Summary summary, List<String> dataRow) {
        List<QuantityPrice> buys = summary.getBuyEntries();
        List<QuantityPrice> sells = summary.getSellEntries();
        Optional<QuantityPrice> buy = toQuantityPrice(dataRow.get(COLUMN_BUY_QTY), dataRow.get(COLUMN_BUY_PRICE));
        Optional<QuantityPrice> sell = toQuantityPrice(dataRow.get(COLUMN_SELL_QTY), dataRow.get(COLUMN_SELL_PRICE));
        buy.ifPresent(buys::add);
        sell.ifPresent(sells::add);

        return new Summary(buys, sells);
    }

    private static Summary addSummary(Summary a, Summary b) {
        List<QuantityPrice> buys = a.getBuyEntries();
        List<QuantityPrice> sells = a.getSellEntries();
        buys.addAll(b.getBuyEntries());
        sells.addAll(b.getSellEntries());

        return new Summary(buys, sells);
    }

    private static boolean isEmpty(String field) {
        return field.trim().isEmpty();
    }

    private static Optional<QuantityPrice> toQuantityPrice(String quantity, String price) {
        if (isEmpty(quantity) || isEmpty(price)) {
            return Optional.empty();
        } else {
            return Optional.of(new QuantityPrice(new BigDecimal(quantity), Integer.valueOf(price)));
        }
    }
}
