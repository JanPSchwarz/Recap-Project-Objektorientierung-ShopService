import org.junit.jupiter.api.Test;

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
        Order expected = new Order("-1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING);
        assertEquals(expected.products(), actual.products());
        assertNotNull(expected.id());
    }

    @Test
    void addOrderTest_whenInvalidProductId_expectNull() {
        // GIVEN
        ShopService shopService = new ShopService();
        List<String> productsIds = List.of("1", "2");

        // WHEN
        Order actual = shopService.addOrder(productsIds);

        // THEN
        assertNull(actual);
    }

    @Test
    void findAllWithOrderStatus_shouldReturnTrueWhenOrdersFound() {
        ShopService shopService = new ShopService();
        OrderRepo repo = shopService.getOrderRepo();
        Order newOrder = new Order("1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING);
        repo.addOrder(newOrder);

        List<Order> actualOrders = shopService.findAllWithOrderStatus(OrderStatus.PROCESSING);
        List<Order> expectedOrders = List.of(newOrder);

        assertEquals(expectedOrders, actualOrders);
    }

    @Test
    void findAllWithOrderStatus_shouldReturnTrueWhenOrdersEmpty() {
        ShopService shopService = new ShopService();
        OrderRepo repo = shopService.getOrderRepo();
        Order newOrder = new Order("1", List.of(new Product("1", "Apfel")), OrderStatus.PROCESSING);
        repo.addOrder(newOrder);

        List<Order> actualOrders = shopService.findAllWithOrderStatus(OrderStatus.COMPLETED);
        List<Order> expectedOrders = List.of(newOrder);

        assertTrue(actualOrders.isEmpty());
        assertNotEquals(expectedOrders, actualOrders);
    }
}
