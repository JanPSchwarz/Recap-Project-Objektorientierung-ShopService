import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShopServiceTest {

    @Test
    void addOrderTest() {
        // GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1");

        // WHEN
        Order actual = shopService.addOrder(productsIds);

        // THEN
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, Instant.now());
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        // GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        // THEN
        assertThrows(ProductNotFoundException.class, () -> shopService.addOrder(productsIds));
    }

    @Test
    void findAllWithOrderStatus_shouldReturnTrueWhenOrdersFound() {
        ShopService shopService = new ShopService();
        OrderRepo repo = shopService.getOrderRepo();
        Order newOrder = new Order("1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, Instant.now());
        repo.addOrder(newOrder);

        List<Order> actualOrders = shopService.findAllWithOrderStatus(OrderStatus.PROCESSING);
        List<Order> expectedOrders = List.of(newOrder);

        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void findAllWithOrderStatus_shouldReturnTrueWhenOrdersEmpty() {
        ShopService shopService = new ShopService();
        OrderRepo repo = shopService.getOrderRepo();
        Order newOrder = new Order("1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING, Instant.now());
        repo.addOrder(newOrder);

        List<Order> actualOrders = shopService.findAllWithOrderStatus(OrderStatus.COMPLETED);
        List<Order> expectedOrders = List.of(newOrder);

        assertTrue(actualOrders.isEmpty());
        assertNotEquals(expectedOrders, actualOrders);
    }

    @Test
    void updateOrder_shouldReturnTrueWhenOrderUpdated() {
        // SETUP
        ShopService shopService = new ShopService();
        OrderRepo repo = shopService.getOrderRepo();
        Order newOrder = new Order("1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING);
        repo.addOrder(newOrder);

        // EXPECTED
        Order expectedUpdatedOrder = new Order("1", List.of(new Product("1", "Apfel")), OrderStatus.COMPLETED);

        // THEN
        Order actualUpdatedOrder = shopService.updateOrder(newOrder.id(), OrderStatus.COMPLETED);
        assertEquals(expectedUpdatedOrder, actualUpdatedOrder);
    }

    @Test
    void updateOrder_shouldThrowErrorWhenOrderNotUpdated() {
        // SETUP
        ShopService shopService = new ShopService();

        String notExistingOrderId = "1";

        // THEN
        assertThrows(OrderNotFoundException.class, () -> shopService.updateOrder(notExistingOrderId, OrderStatus.COMPLETED));
    }
}
