package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;

/**
 * Provides access to shared inventory, cart, and file operations.
 */
public class StoreFront {

    private final InventoryService inventoryManager;
    private final CartService shoppingCart;
    private final FileService fileService;

    private final Path productsPath = Path.of(
            "src", "main", "resources", "products.json");

    private final Path categoriesPath = Path.of(
            "src", "main", "resources", "categories.json");

    /**
     * Loads inventory from JSON and creates the shared cart.
     *
     * @throws IOException if a JSON file cannot be read
     */
    public StoreFront() throws IOException {
        fileService = new FileService();

        List<InventoryItem> items = fileService.loadInventory(
                productsPath,
                categoriesPath);

        inventoryManager = new InventoryManager(items);
        shoppingCart = new ShoppingCart(inventoryManager);
    }

    /**
     * Saves current product information to JSON.
     *
     * @throws IOException if the file cannot be written
     */
    public void saveInventory() throws IOException {
        fileService.saveInventory(
                inventoryManager.getAllInventoryItems(),
                productsPath,
                categoriesPath);
    }

    /**
     * Reloads inventory after all file data passes validation.
     * Stock resets to 10 per product, and the cart is cleared.
     *
     * @throws IOException if a JSON file cannot be read
     */
    public void loadInventory() throws IOException {
        List<InventoryItem> items = fileService.loadInventory(
                productsPath,
                categoriesPath);

        inventoryManager.clearInventory();

        for (InventoryItem item : items) {
            inventoryManager.addInventoryItem(item);
        }

        shoppingCart.clearCart();
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