package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

/**
 * Verifies inventory independence, queries, mutations, and sorting.
 */
class InventoryManagerTest {

    @Test
    void managersHaveIndependentStock() {
        // Arrange
        List<InventoryItem> items = List.of(
                createItem(101, "Test Tires", "Test tire set"));

        InventoryManager first = new InventoryManager(items);
        InventoryManager second = new InventoryManager(items);

        // Act
        first.updateQuantity(101, 4);

        // Assert
        assertEquals(4, first.getInventoryItemByProductId(101)
                .getQuantityInStock());
        assertEquals(5, second.getInventoryItemByProductId(101)
                .getQuantityInStock());
    }

    @Test
    void returnsNullForUnknownProductId() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of());

        // Act
        InventoryItem result =
                manager.getInventoryItemByProductId(999);

        // Assert
        assertNull(result);
    }

    @Test
    void findsProductById() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of(
                createItem(1, "Soft Tires", "Maximum grip")));

        // Act
        InventoryItem result =
                manager.getInventoryItemByProductId(1);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getProduct().getId());
    }

    @Test
    void returnsAllInventoryItems() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of(
                createItem(1, "Soft Tires", "Maximum grip"),
                createItem(2, "Hard Tires", "Long race stints")));

        // Act
        List<InventoryItem> result = manager.getAllInventoryItems();

        // Assert
        assertEquals(2, result.size());
        assertEquals(1, result.get(0).getProduct().getId());
        assertEquals(2, result.get(1).getProduct().getId());
    }

    @Test
    void findsProductsByName() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of(
                createItem(1, "Soft Tires", "Maximum grip"),
                createItem(2, "Front Wing", "Extra downforce")));

        // Act
        List<InventoryItem> result =
                manager.searchProductsByName("Tires");

        // Assert
        assertEquals(1, result.size());
        assertEquals(1, result.get(0).getProduct().getId());
    }

    @Test
    void findsProductsByDescription() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of(
                createItem(1, "Soft Tires", "Maximum grip"),
                createItem(2, "Hard Tires", "Long race stints")));

        // Act
        List<InventoryItem> result =
                manager.searchProductsByDescription("race");

        // Assert
        assertEquals(1, result.size());
        assertEquals(2, result.get(0).getProduct().getId());
    }

    @Test
    void searchesIgnoreCapitalization() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of(
                createItem(1, "Soft Tires", "Maximum grip"),
                createItem(2, "Front Wing", "Extra downforce")));

        // Act
        List<InventoryItem> nameMatches =
                manager.searchProductsByName("SOFT");
        List<InventoryItem> descriptionMatches =
                manager.searchProductsByDescription("GRIP");

        // Assert
        assertEquals(1, nameMatches.size());
        assertEquals(1, nameMatches.get(0).getProduct().getId());

        assertEquals(1, descriptionMatches.size());
        assertEquals(1, descriptionMatches.get(0).getProduct().getId());
    }

    @Test
    void addsNewInventoryItem() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of());
        InventoryItem item = createItem(
                1, "Soft Tires", "Maximum grip");

        // Act
        boolean added = manager.addInventoryItem(item);

        // Assert
        assertTrue(added);
        assertEquals(1, manager.getAllInventoryItems().size());

        InventoryItem result = manager.getInventoryItemByProductId(1);
        assertNotNull(result);
        assertEquals("Soft Tires", result.getProduct().getName());
        assertEquals(5, result.getQuantityInStock());
    }

    @Test
    void rejectsDuplicateProductId() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of(
                createItem(1, "Soft Tires", "Maximum grip")));

        InventoryItem duplicate = createItem(
                1, "Hard Tires", "Long race stints");

        // Act
        boolean added = manager.addInventoryItem(duplicate);

        // Assert
        assertFalse(added);
        assertEquals(1, manager.getAllInventoryItems().size());
        assertEquals("Soft Tires",
                manager.getInventoryItemByProductId(1)
                        .getProduct().getName());
        assertEquals(5,
                manager.getInventoryItemByProductId(1)
                        .getQuantityInStock());
    }

    @Test
    void updatesProductAndPreservesStock() {
        // Arrange
        InventoryItem original = createItem(
                1, "Soft Tires", "Maximum grip");
        original.increaseQuantity(3);

        InventoryManager manager =
                new InventoryManager(List.of(original));

        Product updatedProduct = createItem(
                1, "Updated Tires", "Updated description")
                .getProduct();

        // Act
        boolean updated = manager.updateProduct(updatedProduct);

        // Assert
        assertTrue(updated);
        assertEquals(1, manager.getAllInventoryItems().size());

        InventoryItem result = manager.getInventoryItemByProductId(1);
        assertNotNull(result);
        assertEquals("Updated Tires", result.getProduct().getName());
        assertEquals("Updated description",
                result.getProduct().getDescription());
        assertEquals(8, result.getQuantityInStock());
    }

    @Test
    void doesNotUpdateUnknownProduct() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of(
                createItem(1, "Soft Tires", "Maximum grip")));

        Product unknownProduct = createItem(
                99, "Hard Tires", "Long race stints")
                .getProduct();

        // Act
        boolean updated = manager.updateProduct(unknownProduct);

        // Assert
        assertFalse(updated);
        assertNull(manager.getInventoryItemByProductId(99));
        assertEquals(1, manager.getAllInventoryItems().size());
        assertEquals("Soft Tires",
                manager.getInventoryItemByProductId(1)
                        .getProduct().getName());
        assertEquals(5,
                manager.getInventoryItemByProductId(1)
                        .getQuantityInStock());
    }

    @Test
    void removesExistingProduct() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of(
                createItem(1, "Soft Tires", "Maximum grip"),
                createItem(2, "Hard Tires", "Long race stints")));

        // Act
        boolean removed = manager.removeProductById(1);

        // Assert
        assertTrue(removed);
        assertNull(manager.getInventoryItemByProductId(1));
        assertEquals(1, manager.getAllInventoryItems().size());
        assertEquals(2,
                manager.getAllInventoryItems().get(0)
                        .getProduct().getId());
        assertEquals(5,
                manager.getInventoryItemByProductId(2)
                        .getQuantityInStock());
    }

    @Test
    void doesNotRemoveOtherItemsForUnknownId() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of(
                createItem(1, "Soft Tires", "Maximum grip"),
                createItem(2, "Hard Tires", "Long race stints")));

        // Act
        boolean removed = manager.removeProductById(99);

        // Assert
        assertFalse(removed);
        assertEquals(2, manager.getAllInventoryItems().size());
        assertNotNull(manager.getInventoryItemByProductId(1));
        assertNotNull(manager.getInventoryItemByProductId(2));
        assertEquals(5,
                manager.getInventoryItemByProductId(1)
                        .getQuantityInStock());
        assertEquals(5,
                manager.getInventoryItemByProductId(2)
                        .getQuantityInStock());
    }

    @Test
    void sortsInventoryByName() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of(
                createItem(1, "Soft Tires", "Maximum grip"),
                createItem(2, "Front Wing", "Extra downforce"),
                createItem(3, "Engine", "Power unit")));

        // Act
        List<InventoryItem> result =
                manager.getInventoryItemsSortedByName();

        // Assert: Engine, Front Wing, Soft Tires
        assertEquals(List.of(3, 2, 1), result.stream()
                .map(item -> item.getProduct().getId())
                .toList());
    }

    @Test
    void sortsInventoryByPrice() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of(
                createSortingItem(
                        1, 300.00, LocalDate.of(2026, 1, 1)),
                createSortingItem(
                        2, 100.00, LocalDate.of(2026, 1, 1)),
                createSortingItem(
                        3, 200.00, LocalDate.of(2026, 1, 1))));

        // Act
        List<InventoryItem> result =
                manager.getInventoryItemsSortedByPrice();

        // Assert: 100, 200, 300
        assertEquals(List.of(2, 3, 1), result.stream()
                .map(item -> item.getProduct().getId())
                .toList());
    }

    @Test
    void sortsInventoryByManufactureDate() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of(
                createSortingItem(
                        1, 100.00, LocalDate.of(2026, 3, 1)),
                createSortingItem(
                        2, 100.00, LocalDate.of(2026, 1, 1)),
                createSortingItem(
                        3, 100.00, LocalDate.of(2026, 2, 1))));

        // Act
        List<InventoryItem> result =
                manager.getInventoryItemsSortedByDate();

        // Assert: January, February, March
        assertEquals(List.of(2, 3, 1), result.stream()
                .map(item -> item.getProduct().getId())
                .toList());
    }

    @Test
    void sortingDoesNotChangeStoredOrder() {
        // Arrange
        InventoryManager manager = new InventoryManager(List.of(
                createSortingItem(
                        3, 300.00, LocalDate.of(2026, 3, 1)),
                createSortingItem(
                        1, 100.00, LocalDate.of(2026, 1, 1)),
                createSortingItem(
                        2, 200.00, LocalDate.of(2026, 2, 1))));

        // Act
        manager.getInventoryItemsSortedByName();
        manager.getInventoryItemsSortedByPrice();
        manager.getInventoryItemsSortedByDate();

        // Assert: stored order remains unchanged
        assertEquals(List.of(3, 1, 2),
                manager.getAllInventoryItems().stream()
                        .map(item -> item.getProduct().getId())
                        .toList());
    }

    private InventoryItem createItem(
            int id, String name, String description) {

        Product product = Product.builder()
                .id(id)
                .name(name)
                .description(description)
                .dateOfManufacture(LocalDate.of(2026, 1, 15))
                .price(100.00)
                .category("tires")
                .build();

        return new InventoryItem(product, 5);
    }

    private InventoryItem createSortingItem(
            int id, double price, LocalDate date) {

        Product product = Product.builder()
                .id(id)
                .name("Test Part " + id)
                .description("Part for sorting tests")
                .dateOfManufacture(date)
                .price(price)
                .category("tires")
                .build();

        return new InventoryItem(product, 5);
    }
}