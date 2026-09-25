package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import java.util.List;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

/**
 * Defines the operations for managing storefront inventory.
 *
 * Implementations own the inventory collection and provide the
 * operations used to retrieve, search, add, update, and remove
 * inventory items. No console input or output is performed.
 */
public interface InventoryService {

    /**
     * Returns a read-only copy of the current inventory.
     *
     * @return all inventory items
     */
    List<InventoryItem> getAllInventoryItems();

    /**
     * Finds an inventory item using its product identifier.
     *
     * @param productId the product identifier
     * @return the matching item, or null if no match is found
     */
    InventoryItem getInventoryItemByProductId(int productId);

    /**
     * Searches for inventory items whose product names contain the
     * supplied search term. Matching is case-insensitive.
     *
     * @param searchTerm all or part of a product name
     * @return the matching inventory items
     */
    List<InventoryItem> searchProductsByName(String searchTerm);

    /**
     * Searches for inventory items whose product descriptions contain
     * the supplied search term. Matching is case-insensitive.
     *
     * @param searchTerm all or part of a product description
     * @return the matching inventory items
     */
    List<InventoryItem> searchProductsByDescription(String searchTerm);

    /**
     * Adds a new item to the inventory. Product identifiers must be
     * unique.
     *
     * @param item the inventory item to add
     * @return true if the item was added; false if the item was null or
     *         its product identifier already exists
     */
    boolean addInventoryItem(InventoryItem item);

    /**
     * Replaces the product information associated with an existing
     * inventory item. The current inventory quantity is preserved.
     *
     * @param updatedProduct the updated product
     * @return true if the product was updated; false if it was not found
     */
    boolean updateProduct(Product updatedProduct);

    /**
     * Changes the quantity of an existing inventory item.
     *
     * @param productId       the product identifier
     * @param quantityInStock the new inventory quantity
     * @return true if the quantity was updated; false if the product was
     *         not found or the quantity was negative
     */
    boolean updateQuantity(int productId, int quantityInStock);

    /**
     * Removes the inventory item associated with a product identifier.
     *
     * @param productId the product identifier
     * @return true if the item was removed; false if it was not found
     */
    boolean removeProductById(int productId);

    /**
     * Removes every item from the inventory.
     */
    void clearInventory();
}
