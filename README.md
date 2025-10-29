# ShopService

# Usage

### Instantiate Products:

```java

Product toothBrush = new Product("1", "Tooth Brush");
Product shampoo = new Product("2", "Shampoo");
Product bread = new Product("3", "Bread");
Product cornflakes = new Product("4", "Cornflakes");
```

### Add them to instantiated productRepo:

```java
    ProductRepo productRepo = ProductRepo.builder().products(List.of(toothBrush, shampoo, bread, cornflakes)).build();
```

### Add ProductRepo to instantiated ShopService:

```java
    ShopService shopService = ShopService.builder().productRepo(productRepo).build();
```

### Example of using ShopService Methods:


*Transaction TXT file:*

        addOrder A 1 2 3
        addOrder B 4 1
        setStatus A COMPLETED
        printOrders

##

Reading a file and doing transactions as listed:
```java
List<String> transactionsList = new ArrayList<>();

try (Stream<String> lines = Files.lines(Paths.get("transactions.txt"))) {
    lines.filter(line -> !line.isEmpty())
         .forEach(transactionsList::add);
} catch (IOException e) {
    System.out.println("File not found: " + e.getMessage());
}

Map<String, String> ordersFromTransactionFile = new HashMap<>();

for (String transaction : transactionsList) {
    String[] transactionArray = transaction.split(" ");
    String command = transactionArray[0];

    switch (command) {
        case "addOrder" -> {
            String orderKey = transactionArray[1];
            List<String> productIds = Stream.of(transactionArray).skip(2).toList();
            String madeOrderId = null;
            try {
                madeOrderId = shopService.addOrder(productIds).id();
            } catch (ProductNotFoundException e) {
                System.out.println("Order could not be added:");
                System.out.println(e.getMessage());
            }
            ordersFromTransactionFile.put(orderKey, madeOrderId);
        }
        case "setStatus" -> {
            String updateKey = transactionArray[1];
            String updateStatus = transactionArray[2];
            OrderStatus newOrderStatus = null;

            try {
                newOrderStatus = OrderStatus.valueOf(updateStatus);
            } catch (IllegalArgumentException e) {
                System.out.println("Invalid update status: " + updateStatus);
            }

            String orderIdToUpdate = ordersFromTransactionFile.get(updateKey);

            try {
                shopService.updateOrder(orderIdToUpdate, newOrderStatus);
            } catch (OrderNotFoundException e) {
                System.out.println("Order could not be updated:");
                System.out.println(e.getMessage());
            }
        }
        case "printOrders" -> shopService.getOrderRepo()
                                         .getOrders()
                                         .forEach(System.out::println);
        default -> System.out.println("Invalid command: " + command);
    }
}
```

## Output
Output from calling printOrders command:  

#### Created order B
```
Order[id=dd750203-ae65-4a87-b725-a239fbe4b941, products=[Product[id=4, name=Cornflakes], Product[id=1, name=Tooth Brush]], orderStatus=PROCESSING, timeOfOrder=2025-10-29T15:21:04.408682Z]
```
#### Updated order A
```
Order[id=8ee39c46-1313-43f3-8d25-289c134220b9, products=[Product[id=1, name=Tooth Brush], Product[id=2, name=Shampoo], Product[id=3, name=Bread]], orderStatus=COMPLETED, timeOfOrder=2025-10-29T15:21:04.408596Z]```
```
