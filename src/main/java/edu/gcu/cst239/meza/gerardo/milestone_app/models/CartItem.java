package edu.gcu.cst239.meza.gerardo.milestone_app.models;

import java.util.Objects;

/**
 * Associates a product with the quantity a customer intends to buy.
 * Cart quantity is separate from inventory stock.
 */
public class CartItem {

    private final Product product;
    private int quantity;

    /**
     * Creates a cart item with a positive quantity.
     *
     * @param product product selected for purchase
     * @param quantity requested purchase quantity
     */
    public CartItem(Product product, int quantity) {
        this.product = Objects.requireNonNull(
                product, "Product is required.");
        setQuantity(quantity);
    }

    /**
     * Returns the selected product.
     *
     * @return product associated with this item
     */
    public Product getProduct() {
        return product;
    }

    /**
     * Returns the purchase quantity.
     *
     * @return current cart quantity
     */
    public int getQuantity() {
        return quantity;
    }

    /**
     * Sets a positive purchase quantity.
     *
     * @param quantity new cart quantity
     * @throws IllegalArgumentException if quantity is not positive
     */
    public void setQuantity(int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException(
                    "Cart quantity must be greater than zero.");
        }

        this.quantity = quantity;
    }

    /**
     * Calculates the price of this cart line.
     *
     * @return product price multiplied by quantity
     */
    public double getSubtotal() {
        return product.getPrice() * quantity;
    }
}