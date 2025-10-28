import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data

public class ShopService {
    @Builder.Default
    private ProductRepo productRepo = new ProductRepo();
    @Builder.Default
    private OrderRepo orderRepo = new OrderMapRepo();

    public List<Order> findAllWithOrderStatus(OrderStatus orderStatus) {
        List<Order> orders = orderRepo.getOrders().stream().filter(order -> order.orderStatus().equals(orderStatus
        )).toList();
        return orders;
    }

    public Order addOrder(List<String> productIds) throws ProductNotFoundException {
        List<Product> products = new ArrayList<>();

        for (String productId : productIds) {
            Optional<Product> productToOrder = productRepo.getProductById(productId);
            Product product = productToOrder.orElseThrow(() -> new ProductNotFoundException(productId));
            products.add(product);
        }

        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING);

        return orderRepo.addOrder(newOrder);
    }
}
