package com.github.nigelhp.silverbars.test.acceptance.steps;

import com.github.nigelhp.silverbars.board.OrderBoard;
import com.github.nigelhp.silverbars.board.QuantityPrice;
import com.github.nigelhp.silverbars.board.Summary;
import com.github.nigelhp.silverbars.order.Order;
import cucumber.api.DataTable;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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

    @When("^I register an order to BUY (\\d\\.\\d) kg for £(\\d+)$")
    public void i_register_an_order_to_BUY_kg_for_£(String quantity, int price) {
        orderBoard.ifPresent(b -> b.register(new Order(BUY, new BigDecimal(quantity), price)));
    }

    @Then("^the order board is:$")
    public void the_order_board_is(DataTable table) {
        // Write code here that turns the phrase above into concrete actions
        // For automatic transformation, change DataTable to one of
        // List<YourType>, List<List<E>>, List<Map<K,V>> or Map<K,V>.
        // E,K,V must be a scalar (String, Integer, Date, enum etc)

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

        Optional<Summary> summary = orderBoard.map(OrderBoard::getSummary);
        assertThat(summary, is(Optional.of(new Summary(buyEntries, sellEntries))));
    }
}
