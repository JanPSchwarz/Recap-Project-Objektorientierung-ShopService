import lombok.With;

import java.time.Instant;
import java.util.List;

@With
public record Order(
        String id,
        List<Product> products,
        OrderStatus orderStatus,
        Instant timeOfOrder
) {
    public Order(String id, List<Product> products, OrderStatus orderStatus, Instant timeOfOrder) {
        this.id = id;
        this.products = products;
        this.orderStatus = orderStatus;
        this.timeOfOrder = timeOfOrder;
    }

    public Order(String id, List<Product> products, OrderStatus orderStatus) {
        this(id, products, orderStatus, null);
    }
}
