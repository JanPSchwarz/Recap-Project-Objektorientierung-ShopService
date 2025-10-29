import lombok.*;

import java.time.Instant;
import java.util.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data

public class ShopService {
    @Builder.Default
    private ProductRepo productRepo = new ProductRepo();
    @Builder.Default
    @With
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

        Order newOrder = new Order(UUID.randomUUID().toString(), products, OrderStatus.PROCESSING, Instant.now());

        return orderRepo.addOrder(newOrder);
    }


    public Order updateOrder(String orderId, OrderStatus orderStatus) throws OrderNotFoundException {
        Optional<Order> optionalOrder = orderRepo.getOrderById(orderId);

        Order orderToUpdate = optionalOrder.orElseThrow(() -> new OrderNotFoundException(orderId));

        orderToUpdate = orderToUpdate.withOrderStatus(orderStatus);
        orderRepo.addOrder(orderToUpdate);

        return orderToUpdate;
    }

    public Map<OrderStatus, Order> getOldestOrderByStatus() {
        Map<OrderStatus, Order> resultMap = new HashMap<>();

        List<Order> orders = orderRepo.getOrders();
        List<OrderStatus> allOrderStatuses = List.of(OrderStatus.PROCESSING, OrderStatus.IN_DELIVERY, OrderStatus.COMPLETED);

        for (OrderStatus orderStatus : allOrderStatuses) {
            orders.stream().filter(order -> order.orderStatus().equals(orderStatus)).min(Comparator.comparing(Order::timeOfOrder)).ifPresent(order -> {
                resultMap.put(orderStatus, order);
            });
        }
        return resultMap;
    }
}
