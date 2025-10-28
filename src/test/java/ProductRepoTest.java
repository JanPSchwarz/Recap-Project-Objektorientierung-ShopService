import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProductRepoTest {

    @org.junit.jupiter.api.Test
    void getProducts() {
        // GIVEN
        ProductRepo repo = new ProductRepo();

        // WHEN
        List<Product> actual = repo.getProducts();

        // THEN
        List<Product> expected = new ArrayList<>();
        expected.add(new Product("1", "Apfel"));
        assertEquals(actual, expected);
    }

    @org.junit.jupiter.api.Test
    void getProductById() {
        // GIVEN
        ProductRepo repo = new ProductRepo();

        // WHEN
        Optional<Product> actual = repo.getProductById("1");

        // THEN
        Product expected = new Product("1", "Apfel");
        assertEquals(actual.orElse(null), expected);
    }

    @org.junit.jupiter.api.Test
    void addProduct() {
        // GIVEN
        ProductRepo repo = new ProductRepo();
        Product newProduct = new Product("2", "Banane");

        // WHEN
        Product actual = repo.addProduct(newProduct);

        // THEN
        Product expected = new Product("2", "Banane");
        assertEquals(actual, expected);
        assertEquals(repo.getProductById("2").orElse(null), expected);
    }

    @org.junit.jupiter.api.Test
    void removeProduct() {
        // GIVEN
        ProductRepo repo = new ProductRepo();

        // WHEN
        repo.removeProduct("1");

        // THEN
        assertNull(repo.getProductById("1").orElse(null));
    }
}
