package com.github.nigelhp.silverbars.test.acceptance.steps;

import com.github.nigelhp.silverbars.board.OrderBoard;
import com.github.nigelhp.silverbars.board.Summary;
import com.github.nigelhp.silverbars.order.Order;
import com.github.nigelhp.silverbars.test.acceptance.support.DataTableToSummaryConverter;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.math.BigDecimal;
import java.util.Optional;

import static com.github.nigelhp.silverbars.order.Order.Type.BUY;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class OrderBoardStepdefs {

    private Optional<OrderBoard> orderBoard = Optional.empty();

    @Given("^there are no existing orders$")
    public void there_are_no_existing_orders() {
        orderBoard = Optional.of(new OrderBoard());
    }

    @When("^\"([^\"]*)\" registers an order to BUY (\\d\\.\\d) kg for £(\\d+)$")
    public void registers_an_order_to_BUY_kg_for_£(String userId, String quantity, int price) {
        orderBoard.ifPresent(b -> b.register(new Order(userId, BUY, new BigDecimal(quantity), price)));
    }

    @Then("^the order board is:$")
    public void the_order_board_is(DataTable table) {
        DataTableToSummaryConverter converter = new DataTableToSummaryConverter();
        Optional<Summary> exampleSummary = Optional.of(converter.convert(table));

        assertThat(orderBoard.map(OrderBoard::getSummary), is(exampleSummary));
    }
}
