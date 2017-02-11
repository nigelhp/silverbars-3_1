## Silver Bars Marketplace v3.1
## Contents
* Exercise Instructions
* Assumptions Made
* Design Decisions
* Build Instructions

### Exercise Instructions (duplicated here for convenience)
Imagine you're working as a programmer for a company called Silver Bars Marketplace and you have just received a new requirement. In it we would like to display to our users how much demand for silver bars there is on the market.

To do this we would like to have a 'Live Order Board' that can provide us with the following functionality:

1) Register an order. Order must contain these fields:
* user id
* order quantity (e.g.: 3.5 kg)
* price per kg (e.g.: £303)
* order type: BUY or SELL

2) Cancel a registered order - this will remove the order from 'Live Order Board'

3) Get summary information of live orders (see explanation below)
Imagine we have received the following orders:

a) SELL: 3.5 kg for £306 [user1]

b) SELL: 1.2 kg for £310 [user2]

c) SELL: 1.5 kg for £307 [user3]

d) SELL: 2.0 kg for £306 [user4]

Our ‘Live Order Board’ should provide us the following summary information:

5.5 kg for £306 // order a + order d

1.5 kg for £307 // order c

1.2 kg for £310 // order b

The first thing to note here is that orders for the same price should be merged together (even when they are from different users). In this case it can be seen that order a) and d) were for the same amount (£306) and this is why only their sum (5.5 kg) is displayed (for £306) and not the individual orders (3.5 kg and 2.0 kg).

The last thing to note is that for SELL orders the orders with lowest prices are displayed first. Opposite is true for the BUY orders. 

Please provide the implementation of the live order board which will be packaged and shipped as a library to be used by the UI team. No database or UI/WEB is needed for this assignment (we're absolutely fine with in memory solution). The only important thing is that you just write it according to your normal standards.

NOTE: if during your implementation you'll find that something could be designed in multiple different ways, just implement the one which seems most reasonable to you and if you could provide a short (once sentence) reasoning why you choose this way and not another one, it would be great.

### Assumptions Made
##### Categorise orders by type and summarise independently
The example provided consists of only SELL orders, and does not describe a scenario where there exist both BUY and
SELL orders at the same price point.  Should the 'Live Order Board' display a single quantity (such as a net quantity or
total sum), or should the SELL and BUY orders be presented independently?

Although there is no indication of BUY/SELL in the example board, there is a requirement for different ordering to be
applied to BUY and SELL orders.  I have therefore assumed that the 'Live Order Board' will display a summary where
orders are categorised by the order type, for example:

| BUY Quantity | BUY Price | SELL Quantity | SELL Price |
|-------------:|----------:|--------------:|-----------:|
|          1.5 |       310 |           5.5 |        306 |
|          2.5 |       308 |           1.5 |        307 |
|          3.0 |       306 |           1.2 |        310 |

##### Lowest unit of pricing is £1
 
##### Volume of orders
I have assumed that a large volume of orders makes calculating the 'Live Order Board' summary in a purely functional
way impractical (see Representation of Summary State).

##### 'Live Order Board' is not the order booking system
When an order is registered via the booking system it is presumably allocated a unique identifier, which can then
subsequently be used to cancel the order.  As any single user could potentially place multiple orders with the same
type, price, and quantity - we cannot rely on these properties of an order for cancellation purposes.

I have assumed that an order booking system is responsible for the validation of orders on registration, and ensuring
that only orders in an appropriate state can be cancelled.  I have assumed that the 'Live Order Board' is simply
responsible for providing the order summary, and is fed valid order registration & cancellation events by the booking 
system.  The order board therefore assumes that if it receives a cancellation, it has previously processed an associated 
registration.

##### UserId is mandatory
The exercise states that an Order must contain a userId, and so I have added it to the representation, even though it is
unused by this implementation.

### Design Decisions
##### Representation of Summary State
A 'Live Order Board' summary is in effect a function from a collection of orders to a summary.  It is a straightforward 
exercise to model this in a functional way: partition the orders by type, and then group by price, summing the 
quantities in the group, and then sorting the resulting groups by price.

In the absence of evidence that this will not work, my preference would be the simplicity of this functional approach,
calculating the summaries from the underlying orders, rather than maintaining separate "summary state".

I have however chosen to implement separate "summary state" which can be updated by individual orders in this 
implementation, under the assumption that there is evidence that the volume of orders makes the purely functional
approach impractical.

##### Representation of Quantity
It is presumably important that `1.1+1.1+1.1 == 3.3` (which it will not with floating point arithmetic).

If the only concern is presentation, we could just take of this with formatting.

However, assuming that we want to accurately reflect aggregate quantities, the usual approaches are to use a decimal
type (BigDecimal in the case of Java), or to use an integral type at a non-fractional unit of representation (i.e. use
an int and model the quantity in grams rather than kg).

I have used BigDecimal in this case.  There are some performance implications to this decision, and BigDecimal also has
an unfortunate implementation of equals that takes account of scale (so that 
`new BigDecimal("0").equals(new BigDecimal("0.0")) == false`) but this has not caused any particular issues in the tests 
as yet.  The main advantage of using BigDecimal is that it allows the tests to more easily reflect the style of the 
examples given in the exercise.

##### Concurrent Orders
It seems likely that the order booking system will process multiple orders concurrently.

There are numerous ways that this could be handled by the 'Live Order Board'.

We could use an agent / actor type model, whereby the summary state is safely managed by a single thread in response to
messages on some queue / channel mechanism.  Or we could use the traditional "shared state model" of Java, relying on
either low-level synchronization primitives or the higher-level abstractions of java.util.concurrent.

As this implementation is in Java, I have chosen to support multiple concurrent requests with "shared state", relying on
ConcurrentHashMap in particular to facilitate a high degree of safe concurrency.

Note however that if we were to account for the cancellation of an order before the registration of that order, 
because the current implementation attempts to prevent negative quantities, there is the possibility of either a lost
cancellation or an exception.  If the cancellation price point does not currently exist, the cancellation will be lost.
If the price point does exist, but processing the cancellation results in a negative quantity, an exception will be
thrown.  This could be avoided by allowing the summary to temporarily represent a negative quantity (possibly filtering
them out for display purposes), on the grounds that it will be corrected once the registration is successfully accounted 
for.

In the interests of time, I have not implemented any tests that concurrently interact with the 'Live Order Board' for
this exercise.  It goes without saying that these would be required in a real system anticipating such usage.

### Build Instructions
Run unit tests: `gradle test`

Run acceptance tests: `gradle cucumber`