package edu.gcu.cst239.meza.gerardo.milestone_app.services;

/**
 * Provides access to the shared inventory and shopping-cart services.
 */
public class StoreFront {

    private final InventoryService inventoryManager;
    private final CartService shoppingCart;

    /**
     * Creates inventory first, then creates one cart using that
     * same inventory service.
     */
    public StoreFront() {
        inventoryManager = new InventoryManager();
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