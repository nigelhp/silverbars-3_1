Feature: An order is added to the board upon registration
  As a user of Silver Bars Marketplace
  I want registered orders to be added to the order board
  So that I can accurately gauge market sentiment

  Scenario: A buy order can be registered
    Given there are no existing orders
    When "user1" registers an order to BUY 3.5 kg for £303
    Then the order board is:
      | buy quantity | buy price | sell quantity | sell price |
      |          3.5 |       303 |               |            |


  Scenario: A sell order can be registered
    Given there are no existing orders
    When "user1" registers an order to SELL 3.5 kg for £303
    Then the order board is:
      | buy quantity | buy price | sell quantity | sell price |
      |              |           |           3.5 |        303 |


  Scenario: Sell orders with the same price are aggregated
    Given there are no existing orders
    And "user1" registers an order to SELL 3.5 kg for £306
    When "user4" registers an order to SELL 2.0 kg for £306
    Then the order board is:
      | buy quantity | buy price | sell quantity | sell price |
      |              |           |           5.5 |        306 |


  Scenario: Buy orders with the same price are aggregated
    Given there are no existing orders
    And "user1" registers an order to BUY 3.5 kg for £306
    When "user4" registers an order to BUY 2.0 kg for £306
    Then the order board is:
      | buy quantity | buy price | sell quantity | sell price |
      |          5.5 |       306 |               |            |


  Scenario: Orders with the same price but different types are not aggregated
    Given there are no existing orders
    And "user1" registers an order to BUY 3.5 kg for £306
    When "user4" registers an order to SELL 2.0 kg for £306
    Then the order board is:
      | buy quantity | buy price | sell quantity | sell price |
      |          3.5 |       306 |           2.0 |        306 |
