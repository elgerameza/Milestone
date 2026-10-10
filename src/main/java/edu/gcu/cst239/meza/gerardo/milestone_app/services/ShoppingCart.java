package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.CartItem;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

/**
 * Owns the shopping-cart collection and validates additions against
 * the shared inventory service. Performs no console input or output.
 */
public class ShoppingCart implements CartService {

    private final InventoryService inventoryService;
    private final List<CartItem> cartItems;

    /**
     * Creates an empty cart using the shared inventory service.
     *
     * @param inventoryService service used to validate available stock
     */
    public ShoppingCart(InventoryService inventoryService) {
        this.inventoryService = Objects.requireNonNull(
                inventoryService, "Inventory service is required.");
        cartItems = new ArrayList<>();
    }

    /**
     * Returns a read-only list containing copies of the cart items.
     *
     * @return a snapshot of the current cart
     */
    @Override
    public List<CartItem> getAllCartItems() {
        List<CartItem> copies = new ArrayList<>();

        for (CartItem item : cartItems) {
            copies.add(new CartItem(
                    item.getProduct(), item.getQuantity()));
        }

        return List.copyOf(copies);
    }

    /**
     * Adds a positive quantity without exceeding current stock.
     * Repeated additions increase the existing cart line.
     * Adding to the cart does not reduce inventory stock.
     *
     * @param product product to add
     * @param quantity additional purchase quantity
     * @return true when added; false if invalid or stock is insufficient
     */
    @Override
    public boolean addProduct(Product product, int quantity) {
        if (product == null || quantity <= 0) {
            return false;
        }

        InventoryItem inventoryItem =
                inventoryService.getInventoryItemByProductId(
                        product.getId());

        if (inventoryItem == null) {
            return false;
        }

        CartItem existingItem = findCartItem(product.getId());

        int currentQuantity = existingItem == null
                ? 0 : existingItem.getQuantity();

        // Use long so repeated additions cannot overflow an int.
        long requestedTotal = (long) currentQuantity + quantity;

        if (requestedTotal > inventoryItem.getQuantityInStock()) {
            return false;
        }

        if (existingItem == null) {
            cartItems.add(new CartItem(
                    inventoryItem.getProduct(), quantity));
        } else {
            existingItem.setQuantity((int) requestedTotal);
        }

        return true;
    }

    /**
     * Finds a cart item by product identifier.
     *
     * @param productId product identifier
     * @return matching item, or null if absent
     */
    private CartItem findCartItem(int productId) {
        for (CartItem item : cartItems) {
            if (item.getProduct().getId() == productId) {
                return item;
            }
        }

        return null;
    }

    /**
     * Temporarily leaves quantities unchanged until the cart-update
     * feature is implemented.
     *
     * @param productId product identifier
     * @param quantity requested quantity
     * @return false because this operation is not implemented yet
     */
    @Override
    public boolean updateQuantity(int productId, int quantity) {
        return false;
    }

    /**
     * Removes the selected cart item without changing inventory stock.
     *
     * @param productId product identifier
     * @return true when removed; false if the item was not in the cart
     */
    @Override
    public boolean removeProductFromCart(int productId) {
        CartItem item = findCartItem(productId);

        if (item == null) {
            return false;
        }

        return cartItems.remove(item);
    }

    /**
     * Calculates the total of all cart-line subtotals.
     *
     * @return current cart total
     */
    @Override
    public double getCartTotal() {
        double total = 0.0;

        for (CartItem item : cartItems) {
            total += item.getSubtotal();
        }

        return total;
    }

    /**
     * Temporarily leaves inventory and cart unchanged until the
     * checkout feature is implemented.
     *
     * @return false because checkout is not implemented yet
     */
    @Override
    public boolean checkout() {
        return false;
    }

    /**
     * Clears the cart without changing inventory stock.
     */
    @Override
    public void clearCart() {
        cartItems.clear();
    }
}