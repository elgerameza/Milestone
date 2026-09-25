package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

/**
 * Manages the collection of products available in the formula-car
 * parts inventory.
 *
 * InventoryManager performs inventory operations without directly
 * reading from or writing to the console.
 *
 * @author Gerardo Meza
 * @version 1.0
 */
public class InventoryManager implements InventoryService {

    private final List<InventoryItem> inventory;

    /**
     * Creates the inventory manager and initializes its inventory.
     */
    public InventoryManager() {
        inventory = new ArrayList<>();
        initializeInventory();
    }

    /**
     * Creates the initial formula-car parts inventory.
     */
    private void initializeInventory() {
        Product softTireSet = Product.builder()
                .id(1)
                .name("Soft Compound Tire Set")
                .description(
                        "Set of four soft-compound tires designed for maximum grip")
                .dateOfManufacture(LocalDate.of(2026, 8, 10))
                .price(2400.00)
                .category("tires")
                .build();

        Product mediumTireSet = Product.builder()
                .id(2)
                .name("Medium Compound Tire Set")
                .description(
                        "Set of four medium-compound tires balancing grip and durability")
                .dateOfManufacture(LocalDate.of(2026, 8, 12))
                .price(2300.00)
                .category("tires")
                .build();

        Product hardTireSet = Product.builder()
                .id(3)
                .name("Hard Compound Tire Set")
                .description(
                        "Set of four hard-compound tires designed for long race stints")
                .dateOfManufacture(LocalDate.of(2026, 8, 14))
                .price(2200.00)
                .category("tires")
                .build();

        Product formulaCarEngine = Product.builder()
                .id(4)
                .name("Formula Car Engine")
                .description(
                        "High-performance engine designed for a formula racing car")
                .dateOfManufacture(LocalDate.of(2026, 7, 20))
                .price(500000.00)
                .category("power-unit")
                .build();

        Product frontWingAssembly = Product.builder()
                .id(5)
                .name("Front Wing Assembly")
                .description(
                        "Adjustable front wing assembly designed to generate front-end downforce")
                .dateOfManufacture(LocalDate.of(2026, 8, 5))
                .price(150000.00)
                .category("aerodynamics")
                .build();

        inventory.add(new InventoryItem(softTireSet, 6));
        inventory.add(new InventoryItem(mediumTireSet, 6));
        inventory.add(new InventoryItem(hardTireSet, 4));
        inventory.add(new InventoryItem(formulaCarEngine, 2));
        inventory.add(new InventoryItem(frontWingAssembly, 3));
    }

    /**
     * Returns a read-only copy of the current inventory.
     *
     * @return all inventory items
     */
    @Override
    public List<InventoryItem> getAllInventoryItems() {
        return List.copyOf(inventory);
    }

    /**
     * Finds an inventory item using its product identifier.
     *
     * @param productId product identifier
     * @return matching inventory item, or null when no match exists
     */
    @Override
    public InventoryItem getInventoryItemByProductId(int productId) {
        for (InventoryItem item : inventory) {
            if (item.getProduct().getId() == productId) {
                return item;
            }
        }

        return null;
    }

    /**
     * Searches product names using case-insensitive partial matching.
     *
     * @param searchTerm all or part of a product name
     * @return matching inventory items
     */
    @Override
    public List<InventoryItem> searchProductsByName(
            String searchTerm) {

        List<InventoryItem> matches = new ArrayList<>();

        if (searchTerm == null || searchTerm.isBlank()) {
            return matches;
        }

        String normalizedTerm =
                searchTerm.trim().toLowerCase(Locale.ROOT);

        for (InventoryItem item : inventory) {
            String productName =
                    item.getProduct().getName();

            if (productName.toLowerCase(Locale.ROOT)
                    .contains(normalizedTerm)) {

                matches.add(item);
            }
        }

        return matches;
    }

    /**
     * Searches product descriptions using case-insensitive partial
     * matching.
     *
     * @param searchTerm all or part of a product description
     * @return matching inventory items
     */
    @Override
    public List<InventoryItem> searchProductsByDescription(
            String searchTerm) {

        List<InventoryItem> matches = new ArrayList<>();

        if (searchTerm == null || searchTerm.isBlank()) {
            return matches;
        }

        String normalizedTerm =
                searchTerm.trim().toLowerCase(Locale.ROOT);

        for (InventoryItem item : inventory) {
            String productDescription =
                    item.getProduct().getDescription();

            if (productDescription.toLowerCase(Locale.ROOT)
                    .contains(normalizedTerm)) {

                matches.add(item);
            }
        }

        return matches;
    }

    /**
     * Adds an inventory item.
     *
     * @param item inventory item to add
     * @return true when the item is added
     */
    @Override
    public boolean addInventoryItem(InventoryItem item) {
        throw new UnsupportedOperationException(
                "Method will be implemented in a later branch.");
    }

    /**
     * Updates an existing product.
     *
     * @param updatedProduct updated product information
     * @return true when the product is updated
     */
    @Override
    public boolean updateProduct(Product updatedProduct) {
        throw new UnsupportedOperationException(
                "Method will be implemented in a later branch.");
    }

    /**
     * Updates the quantity of an inventory item.
     *
     * @param productId product identifier
     * @param quantityInStock new inventory quantity
     * @return true when the quantity is updated
     */
    @Override
    public boolean updateQuantity(
            int productId,
            int quantityInStock) {

        throw new UnsupportedOperationException(
                "Method will be implemented in a later branch.");
    }

    /**
     * Removes an inventory item using its product identifier.
     *
     * @param productId product identifier
     * @return true when the item is removed
     */
    @Override
    public boolean removeProductById(int productId) {
        throw new UnsupportedOperationException(
                "Method will be implemented in a later branch.");
    }

    /**
     * Removes all inventory items.
     */
    @Override
    public void clearInventory() {
        throw new UnsupportedOperationException(
                "Method will be implemented in a later branch.");
    }
}