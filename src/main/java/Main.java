import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

public class Main {
    public static void main(String[] args) throws IOException {
        Product toothBrush = new Product("1", "Tooth Brush");
        Product shampoo = new Product("2", "Shampoo");
        Product bread = new Product("3", "Bread");
        Product cornflakes = new Product("4", "Cornflakes");

        ProductRepo productRepo = ProductRepo.builder().products(List.of(toothBrush, shampoo, bread, cornflakes)).build();

        ShopService shopService = ShopService.builder().productRepo(productRepo).build();

        List<String> transactionsList = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get("transactions.txt"))) {
            lines.filter(line -> !line.isEmpty()).forEach(transactionsList::add);
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
                case "printOrders" -> shopService.getOrderRepo().getOrders().forEach(System.out::println);

                default -> System.out.println("Invalid command: " + command);

            }

        }


    }

    static String createUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
