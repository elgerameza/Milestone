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
 * Verifies checkout results and changes to inventory and cart state.
 */
class CheckoutTest {

    @Test
    void successfulCheckoutReducesStock() {
        // Arrange
        Product product = createProduct(1);
        InventoryService inventory = new InventoryManager(List.of(
                new InventoryItem(product, 5)));

        CartService cart = new ShoppingCart(inventory);
        cart.addProduct(product, 2);

        // Act
        boolean purchased = cart.checkout();

        // Assert: 5 in stock minus 2 purchased leaves 3
        assertTrue(purchased);
        assertEquals(3, inventory.getInventoryItemByProductId(1)
                .getQuantityInStock());
    }

    @Test
    void successfulCheckoutClearsCart() {
        // Arrange
        Product product = createProduct(1);
        InventoryService inventory = new InventoryManager(List.of(
                new InventoryItem(product, 5)));

        CartService cart = new ShoppingCart(inventory);
        cart.addProduct(product, 2);

        // Act
        boolean purchased = cart.checkout();

        // Assert
        assertTrue(purchased);
        assertTrue(cart.getAllCartItems().isEmpty());
        assertEquals(0.00, cart.getCartTotal(), 0.0001);
    }

    @Test
    void insufficientStockRejectsCheckoutAndKeepsCart() {
        // Arrange
        Product product = createProduct(1);
        InventoryService inventory = new InventoryManager(List.of(
                new InventoryItem(product, 5)));

        CartService cart = new ShoppingCart(inventory);
        cart.addProduct(product, 3);

        // Stock drops after the customer adds the product
        inventory.updateQuantity(1, 2);

        // Act
        boolean purchased = cart.checkout();

        // Assert
        assertFalse(purchased);
        assertEquals(2, inventory.getInventoryItemByProductId(1)
                .getQuantityInStock());
        assertEquals(1, cart.getAllCartItems().size());
        assertEquals(3, cart.getAllCartItems().get(0).getQuantity());
        assertEquals(60.00, cart.getCartTotal(), 0.0001);
    }

    @Test
    void failedCheckoutDoesNotPartiallyReduceStock() {
        // Arrange
        Product first = createProduct(1);
        Product second = createProduct(2);

        InventoryService inventory = new InventoryManager(List.of(
                new InventoryItem(first, 5),
                new InventoryItem(second, 5)));

        CartService cart = new ShoppingCart(inventory);
        cart.addProduct(first, 2);
        cart.addProduct(second, 3);

        // The first item can be purchased, but the second cannot
        inventory.updateQuantity(2, 1);

        // Act
        boolean purchased = cart.checkout();

        // Assert: neither product's stock changes during checkout
        assertFalse(purchased);
        assertEquals(5, inventory.getInventoryItemByProductId(1)
                .getQuantityInStock());
        assertEquals(1, inventory.getInventoryItemByProductId(2)
                .getQuantityInStock());

        assertEquals(2, cart.getAllCartItems().size());
        assertEquals(2, cart.getAllCartItems().get(0).getQuantity());
        assertEquals(3, cart.getAllCartItems().get(1).getQuantity());
    }

    @Test
    void emptyCartCheckoutLeavesInventoryUnchanged() {
        // Arrange
        Product product = createProduct(1);
        InventoryService inventory = new InventoryManager(List.of(
                new InventoryItem(product, 5)));

        CartService cart = new ShoppingCart(inventory);

        // Act
        boolean purchased = cart.checkout();

        // Assert
        assertFalse(purchased);
        assertTrue(cart.getAllCartItems().isEmpty());
        assertEquals(0.00, cart.getCartTotal(), 0.0001);
        assertEquals(5, inventory.getInventoryItemByProductId(1)
                .getQuantityInStock());
    }

    private Product createProduct(int id) {
        return Product.builder()
                .id(id)
                .name("Test Part " + id)
                .description("Part for checkout testing")
                .dateOfManufacture(LocalDate.of(2026, 1, 15))
                .price(20.00)
                .category("tires")
                .build();
    }
}