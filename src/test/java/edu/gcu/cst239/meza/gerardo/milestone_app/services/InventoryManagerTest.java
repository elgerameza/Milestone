package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

/**
 * Verifies inventory manager behavior using controlled data.
 */
class InventoryManagerTest {
    @Test
    void managersHaveIndependentStock() 
    {
        Product product = Product.builder()
                .id(101)
                .name("Test Tires")
                .description("Test tire set")
                .dateOfManufacture(LocalDate.of(2026, 1, 15))
                .price(100.00)
                .category("tires")
                .build();

        List<InventoryItem> items =
                List.of(new InventoryItem(product, 10));

        InventoryManager first = new InventoryManager(items);
        InventoryManager second = new InventoryManager(items);

        first.updateQuantity(101, 4);

        assertEquals(4,
                first.getInventoryItemByProductId(101).getQuantityInStock());
        assertEquals(10,
                second.getInventoryItemByProductId(101).getQuantityInStock());
    }
}