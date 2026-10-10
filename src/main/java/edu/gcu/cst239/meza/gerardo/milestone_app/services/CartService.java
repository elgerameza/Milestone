package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import java.util.List;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.CartItem;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

/**
 * Defines the operations supported by the shared shopping cart.
 */
public interface CartService {

    /**
     * Returns a read-only copy of the current cart.
     *
     * @return current cart items
     */
    List<CartItem> getAllCartItems();

    /**
     * Adds a quantity when sufficient stock is available.
     *
     * @param product product to add
     * @param quantity requested quantity
     * @return true when the cart changes
     */
    boolean addProduct(Product product, int quantity);

    /**
     * Changes the quantity of an existing cart item.
     *
     * @param productId product identifier
     * @param quantity requested quantity
     * @return true when the cart changes
     */
    boolean updateQuantity(int productId, int quantity);

    /**
     * Removes the cart item associated with a product identifier.
     *
     * @param productId product identifier
     * @return true when an item is removed
     */
    boolean removeProductFromCart(int productId);

    /**
     * Calculates the current cart total.
     *
     * @return total price of all cart items
     */
    double getCartTotal();

    /**
     * Completes a valid purchase, updates stock, and clears the cart.
     *
     * @return true when checkout succeeds
     */
    boolean checkout();

    /**
     * Removes every item from the cart.
     */
    void clearCart();
}