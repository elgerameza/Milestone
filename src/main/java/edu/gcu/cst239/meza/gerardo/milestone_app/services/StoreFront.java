package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;

/**
 * Provides access to the shared inventory and shopping-cart services.
 */
public class StoreFront {

    private final InventoryService inventoryManager;
    private final CartService shoppingCart;

    /**
     * Loads inventory from JSON, then creates a cart using
     * the same inventory service.
     *
     * @throws IOException if a JSON file cannot be read
     */
    public StoreFront() throws IOException {
        FileService fileService = new FileService();

        Path productsPath = Path.of(
                "src", "main", "resources", "products.json");

        Path categoriesPath = Path.of(
                "src", "main", "resources", "categories.json");

        List<InventoryItem> items = fileService.loadInventory(
                productsPath,
                categoriesPath);

        inventoryManager = new InventoryManager(items);
        shoppingCart = new ShoppingCart(inventoryManager);
    }

    /**
     * Returns the shared inventory service.
     *
     * @return inventory service
     */
    public InventoryService getInventoryManager() {
        return inventoryManager;
    }

    /**
     * Returns the shared shopping-cart service.
     *
     * @return cart service
     */
    public CartService getShoppingCart() {
        return shoppingCart;
    }
}