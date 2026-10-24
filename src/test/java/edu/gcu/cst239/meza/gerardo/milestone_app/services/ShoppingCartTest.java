package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

/**
 * Tests shopping-cart quantities, removal, and totals.
 */
class ShoppingCartTest {

    @Test
    void addsProductAndCombinesRepeatedAdditions() {
        // Arrange
        Product product = createProduct(1, 20.00);
        CartService cart = createCart(product);

        // Act: add the product for the first time
        boolean added = cart.addProduct(product, 2);

        // Assert
        assertTrue(added);
        assertEquals(1, cart.getAllCartItems().size());
        assertEquals(1, cart.getAllCartItems().get(0)
                .getProduct().getId());
        assertEquals(2, cart.getAllCartItems().get(0).getQuantity());

        // Act: add the same product again
        boolean addedAgain = cart.addProduct(product, 1);

        // Assert: one cart line with a combined quantity
        assertTrue(addedAgain);
        assertEquals(1, cart.getAllCartItems().size());
        assertEquals(3, cart.getAllCartItems().get(0).getQuantity());
    }

    @Test
    void updatesQuantityAndRejectsInvalidQuantities() {
        // Arrange
        Product product = createProduct(1, 20.00);
        CartService cart = createCart(product);
        cart.addProduct(product, 1);

        // Act
        boolean updated = cart.updateQuantity(1, 3);

        // Assert
        assertTrue(updated);
        assertEquals(3, cart.getAllCartItems().get(0).getQuantity());

        // Invalid requests must leave the quantity unchanged
        assertFalse(cart.addProduct(product, 0));
        assertFalse(cart.addProduct(product, -1));
        assertFalse(cart.updateQuantity(1, -1));
        assertFalse(cart.updateQuantity(1, 6));

        assertEquals(1, cart.getAllCartItems().size());
        assertEquals(3, cart.getAllCartItems().get(0).getQuantity());
    }

    @Test
    void removesSelectedCartItem() {
        // Arrange
        Product first = createProduct(1, 20.00);
        Product second = createProduct(2, 10.00);

        InventoryService inventory = new InventoryManager(List.of(
                new InventoryItem(first, 5),
                new InventoryItem(second, 5)));

        CartService cart = new ShoppingCart(inventory);
        cart.addProduct(first, 2);
        cart.addProduct(second, 1);

        // Act
        boolean removed = cart.removeProductFromCart(1);

        // Assert: only the second product remains
        assertTrue(removed);
        assertEquals(1, cart.getAllCartItems().size());
        assertEquals(2, cart.getAllCartItems().get(0)
                .getProduct().getId());
        assertEquals(1, cart.getAllCartItems().get(0).getQuantity());

        // Removing from the cart does not reduce inventory
        assertEquals(5, inventory.getInventoryItemByProductId(1)
                .getQuantityInStock());
    }

    @Test
    void calculatesTotalForMultipleProducts() {
        // Arrange
        Product first = createProduct(1, 20.00);
        Product second = createProduct(2, 10.00);

        InventoryService inventory = new InventoryManager(List.of(
                new InventoryItem(first, 5),
                new InventoryItem(second, 5)));

        CartService cart = new ShoppingCart(inventory);
        cart.addProduct(first, 2);
        cart.addProduct(second, 3);

        // Act
        double total = cart.getCartTotal();

        // Assert: (2 x 20) + (3 x 10) = 70
        assertEquals(70.00, total, 0.0001);
    }

    @Test
    void emptyCartHasNoItemsAndZeroTotal() {
        // Arrange
        CartService cart = createCart(createProduct(1, 20.00));

        // Act
        boolean empty = cart.getAllCartItems().isEmpty();
        double total = cart.getCartTotal();

        // Assert
        assertTrue(empty);
        assertEquals(0.00, total, 0.0001);
    }

    private Product createProduct(int id, double price) {
        return Product.builder()
                .id(id)
                .name("Test Part " + id)
                .description("Part for cart testing")
                .dateOfManufacture(LocalDate.of(2026, 1, 15))
                .price(price)
                .category("tires")
                .build();
    }

    private CartService createCart(Product product) {
        InventoryService inventory = new InventoryManager(List.of(
                new InventoryItem(product, 5)));

        return new ShoppingCart(inventory);
    }
}