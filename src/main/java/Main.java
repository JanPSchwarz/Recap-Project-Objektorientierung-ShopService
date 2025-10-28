import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class Main {
    public static void main(String[] args) {
        Product toothBrush = new Product(createUUID(), "Tooth Brush");
        Product shampoo = new Product(createUUID(), "Shampoo");

        ProductRepo productRepo = ProductRepo.builder().products(List.of(toothBrush, shampoo)).build();
        System.out.println(productRepo.getProducts());

        List<String> productIds = List.of(toothBrush.id(), shampoo.id());

        ShopService shopService = ShopService.builder().productRepo(productRepo).build();

        Order newOrder = shopService.addOrder(productIds);
        Order completedOrder = new Order(createUUID(), List.of(toothBrush), OrderStatus.COMPLETED);
        shopService.getOrderRepo().addOrder(completedOrder);

        System.out.println("all orders:");
        System.out.println(shopService.getOrderRepo().getOrders());

        List<Order> result = shopService.findAllWithOrderStatus(OrderStatus.COMPLETED);
        System.out.println("Result:");
        System.out.println(result);

    }

    static String createUUID() {
        UUID uuid = UUID.randomUUID();
        return uuid.toString();
    }
}
