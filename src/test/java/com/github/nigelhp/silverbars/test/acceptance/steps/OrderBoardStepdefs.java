package com.github.nigelhp.silverbars.test.acceptance.steps;

import com.github.nigelhp.silverbars.board.OrderBoard;
import com.github.nigelhp.silverbars.board.Summary;
import com.github.nigelhp.silverbars.order.Order;
import com.github.nigelhp.silverbars.order.Order.Type;
import com.github.nigelhp.silverbars.test.acceptance.support.DataTableToSummaryConverter;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class OrderBoardStepdefs {

    private Optional<OrderBoard> orderBoard;
    private final AtomicReference<Summary> lastSummary;
    private final DataTableToSummaryConverter tableConverter;

    public OrderBoardStepdefs() {
        orderBoard = Optional.empty();
        lastSummary = new AtomicReference<>();
        tableConverter = new DataTableToSummaryConverter();
    }

    @Given("^there are no existing orders$")
    public void there_are_no_existing_orders() {
        OrderBoard board = new OrderBoard();
        board.addListener(lastSummary::set);
        orderBoard = Optional.of(board);
    }

    @When("^\"([^\"]*)\" registers an order to (BUY|SELL) (\\d\\.\\d) kg for £(\\d+)$")
    public void registers_an_order_to_for(String userId, String type, String quantity, int price) {
        orderBoard.ifPresent(b -> b.register(new Order(userId, Type.valueOf(type), new BigDecimal(quantity), price)));
    }

    @When("^\"([^\"]*)\" cancels an order to (BUY|SELL) (\\d+\\.\\d) kg for £(\\d+)$")
    public void cancels_an_order_to_SELL_kg_for_£(String userId, String type, String quantity, int price) {
        orderBoard.ifPresent(b -> b.cancel(new Order(userId, Type.valueOf(type), new BigDecimal(quantity), price)));
    }

    @Then("^the order board is:$")
    public void the_order_board_is(DataTable table) {
        Summary exampleSummary = tableConverter.convert(table);
        assertThat(lastSummary.get(), is(exampleSummary));
    }
}
