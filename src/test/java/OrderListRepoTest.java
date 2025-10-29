import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderListRepoTest {

    @Test
    void getOrders() {
        // GIVEN
        OrderListRepo repo = new OrderListRepo();

        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(product), OrderStatus.PROCESSING);
        repo.addOrder(newOrder);

        // WHEN
        List<Order> actual = repo.getOrders();

        // THEN
        List<Order> expected = new ArrayList<>();
        Product product1 = new Product("1", "Apfel");
        expected.add(new Order("1", List.of(product1), OrderStatus.PROCESSING));

        assertEquals(actual, expected);
    }

    @Test
    void getOrderById() {
        // GIVEN
        OrderListRepo repo = new OrderListRepo();

        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(product), OrderStatus.PROCESSING);
        repo.addOrder(newOrder);

        // WHEN
        Optional<Order> actualOptional = repo.getOrderById("1");
        Order actual = actualOptional.orElse(null);

        // THEN
        Product product1 = new Product("1", "Apfel");
        Order expected = new Order("1", List.of(product1), OrderStatus.PROCESSING);

        assertEquals(actual, expected);
    }

    @Test
    void addOrder() {
        // GIVEN
        OrderListRepo repo = new OrderListRepo();
        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(product), OrderStatus.PROCESSING);

        // WHEN
        Order actual = repo.addOrder(newOrder);

        // THEN
        Order expected = new Order("1", List.of(product), OrderStatus.PROCESSING);
        assertEquals(actual, expected);
    }

    @Test
    void removeOrder() {
        // SETUP
        OrderListRepo repo = new OrderListRepo();
        Product product = new Product("1", "Apfel");
        Order newOrder = new Order("1", List.of(product), OrderStatus.PROCESSING, Instant.now());
        repo.addOrder(newOrder);

        // EXPECTED
        repo.removeOrder("1");
        Optional<Order> actualOptional = repo.getOrderById("1");
        Order actual = actualOptional.orElse(null);

        // THEN
        assertNull(actual);
    }
}
