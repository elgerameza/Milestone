package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

/**
 * Manages inventory without console input or file operations.
 *
 * @author Gerardo Meza
 */
public class InventoryManager implements InventoryService {

    private final List<InventoryItem> inventory;

    /**
     * Creates an empty inventory.
     */
    public InventoryManager() {
        inventory = new ArrayList<>();
    }

    /**
     * Creates inventory from supplied items.
     * Copies each item so stock quantities are independent.
     *
     * @param initialInventory inventory items to copy
     */
    public InventoryManager(
            Collection<InventoryItem> initialInventory) {

        Objects.requireNonNull(
                initialInventory,
                "Initial inventory is required.");

        inventory = new ArrayList<>();

        for (InventoryItem item : initialInventory) {
            Objects.requireNonNull(
                    item,
                    "Inventory item is required.");

            inventory.add(new InventoryItem(
                    item.getProduct(),
                    item.getQuantityInStock()));
        }
    }

    /**
     * Returns a read-only copy of the inventory list.
     */
    @Override
    public List<InventoryItem> getAllInventoryItems() {
        return List.copyOf(inventory);
    }

    /**
     * Returns inventory sorted by product name.
     */
    @Override
    public List<InventoryItem> getInventoryItemsSortedByName() {
        List<InventoryItem> sortedItems =
                new ArrayList<>(inventory);

        sortedItems.sort(Comparator.comparing(
                item -> item.getProduct().getName(),
                String.CASE_INSENSITIVE_ORDER));

        return sortedItems;
    }

    /**
     * Returns inventory sorted by manufacture date.
     */
    @Override
    public List<InventoryItem> getInventoryItemsSortedByDate() {
        List<InventoryItem> sortedItems =
                new ArrayList<>(inventory);

        sortedItems.sort(Comparator.comparing(
                item -> item.getProduct().getDateOfManufacture()));

        return sortedItems;
    }

    /**
     * Returns inventory sorted by price.
     */
    @Override
    public List<InventoryItem> getInventoryItemsSortedByPrice() {
        List<InventoryItem> sortedItems =
                new ArrayList<>(inventory);

        sortedItems.sort(Comparator.comparingDouble(
                item -> item.getProduct().getPrice()));

        return sortedItems;
    }

    /**
     * Finds an inventory item by product ID.
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
     * Searches product names without case sensitivity.
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
            String productName = item.getProduct().getName();

            if (productName.toLowerCase(Locale.ROOT)
                    .contains(normalizedTerm)) {

                matches.add(item);
            }
        }

        return matches;
    }

    /**
     * Searches product descriptions without case sensitivity.
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
            String description =
                    item.getProduct().getDescription();

            if (description.toLowerCase(Locale.ROOT)
                    .contains(normalizedTerm)) {

                matches.add(item);
            }
        }

        return matches;
    }

    /**
     * Adds an inventory item if its product ID is unique.
     */
    @Override
    public boolean addInventoryItem(InventoryItem item) {
        if (item == null || item.getProduct() == null) {
            return false;
        }

        int productId = item.getProduct().getId();

        if (getInventoryItemByProductId(productId) != null) {
            return false;
        }

        return inventory.add(item);
    }

    /**
     * Updates product information while preserving stock.
     */
    @Override
    public boolean updateProduct(Product updatedProduct) {
        if (updatedProduct == null) {
            return false;
        }

        for (int index = 0; index < inventory.size(); index++) {
            InventoryItem currentItem = inventory.get(index);

            if (currentItem.getProduct().getId()
                    == updatedProduct.getId()) {

                InventoryItem updatedItem = new InventoryItem(
                        updatedProduct,
                        currentItem.getQuantityInStock());

                inventory.set(index, updatedItem);
                return true;
            }
        }

        return false;
    }

    /**
     * Updates an existing item's stock quantity.
     */
    @Override
    public boolean updateQuantity(
            int productId,
            int quantityInStock) {

        if (quantityInStock < 0) {
            return false;
        }

        InventoryItem item =
                getInventoryItemByProductId(productId);

        if (item == null) {
            return false;
        }

        int currentQuantity = item.getQuantityInStock();

        if (quantityInStock == currentQuantity) {
            return true;
        }

        if (quantityInStock > currentQuantity) {
            item.increaseQuantity(
                    quantityInStock - currentQuantity);
        } else {
            item.decreaseQuantity(
                    currentQuantity - quantityInStock);
        }

        return true;
    }

    /**
     * Removes a product by ID.
     */
    @Override
    public boolean removeProductById(int productId) {
        InventoryItem item =
                getInventoryItemByProductId(productId);

        if (item == null) {
            return false;
        }

        return inventory.remove(item);
    }

    /**
     * Removes every inventory item.
     */
    @Override
    public void clearInventory() {
        inventory.clear();
    }
}