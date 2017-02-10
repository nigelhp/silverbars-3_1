Feature: An order is added to the board upon registration
  As a user of Silver Bars Marketplace
  I want registered orders to be added to the order board
  So that I can accurately gauge market sentiment

  Background:
    Given there are no existing orders

  Scenario: A buy order can be registered
    When "user1" registers an order to BUY 3.5 kg for £303
    Then the order board is:
      | buy quantity | buy price | sell quantity | sell price |
      |          3.5 |       303 |               |            |


  Scenario: A sell order can be registered
    When "user1" registers an order to SELL 3.5 kg for £303
    Then the order board is:
      | buy quantity | buy price | sell quantity | sell price |
      |              |           |           3.5 |        303 |


  Scenario: Sell orders with the same price are aggregated
    Given "user1" registers an order to SELL 3.5 kg for £306
    When "user4" registers an order to SELL 2.0 kg for £306
    Then the order board is:
      | buy quantity | buy price | sell quantity | sell price |
      |              |           |           5.5 |        306 |


  Scenario: Buy orders with the same price are aggregated
    Given "user1" registers an order to BUY 3.5 kg for £306
    When "user4" registers an order to BUY 2.0 kg for £306
    Then the order board is:
      | buy quantity | buy price | sell quantity | sell price |
      |          5.5 |       306 |               |            |


  Scenario: Orders with the same price but different types are not aggregated
    Given "user1" registers an order to BUY 3.5 kg for £306
    When "user4" registers an order to SELL 2.0 kg for £306
    Then the order board is:
      | buy quantity | buy price | sell quantity | sell price |
      |          3.5 |       306 |           2.0 |        306 |


  Scenario: Buy orders are listed highest price first
    Given "user1" registers an order to BUY 3.5 kg for £306
    And "user2" registers an order to BUY 1.2 kg for £310
    When "user3" registers an order to BUY 1.5 kg for £307
    Then the order board is:
      | buy quantity | buy price | sell quantity | sell price |
      |          1.2 |       310 |               |            |
      |          1.5 |       307 |               |            |
      |          3.5 |       306 |               |            |