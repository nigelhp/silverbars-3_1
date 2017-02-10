Feature: An order is removed from the board upon cancellation
  As a user of Silver Bars Marketplace
  I want cancelled orders to be removed from the order board
  So that I can accurately gauge market sentiment

  Background:
    Given there are no existing orders

  Scenario: A buy order can be registered
    Given "user1" registers an order to SELL 3.5 kg for £306
    When "user1" cancels an order to SELL 3.5 kg for £306
    Then the order board is:
      | buy quantity | buy price | sell quantity | sell price |
      |              |           |               |            |