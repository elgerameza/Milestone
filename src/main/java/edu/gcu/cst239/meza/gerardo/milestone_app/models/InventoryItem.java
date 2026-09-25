package edu.gcu.cst239.meza.gerardo.milestone_app.models;

import java.util.Objects;

/**
 * 
 * Combines a product with its current inventory quantity
 */
public class InventoryItem {
 
    private final Product product;
    private int quantityInStock;
 
    /**
     * Create an inventory item
     * 
     * @param product prodcut represented by this inventory item
     * @param quantityInStock product quantity stock
     */
    public InventoryItem(
            Product product,
            int quantityInStock) {
 
        this.product = Objects.requireNonNull(
                product,
                "Product is required.");
 
        if (quantityInStock < 0) {
            throw new IllegalArgumentException(
                    "Quantity cannot be negative.");
        }
 
        this.quantityInStock = quantityInStock;
    }
    /**
     * Returns the product 
     * 
     * @return associated product
     */
    public Product getProduct() {
        return product;
    }
    /**
     * Returns the current inventory quantity
     * 
     * @return quantity currently on stock
     */
    public int getQuantityInStock() {
        return quantityInStock;
    }
    /**
     * Increase the inventory quantity
     * 
     * @param amount amount to add
     */
    public void increaseQuantity(int amount) {
        requirePositiveAmount(amount);
        quantityInStock += amount;
    }
    /**
     * Decrease the inventory quantity
     * 
     * 
     * @param amount amount to remove
     * @return true if the quantity was decreased, false if inssuficient inventory was available
     */
    public boolean decreaseQuantity(int amount) {
        requirePositiveAmount(amount);
 
        if (amount > quantityInStock) {
            return false;
        }
 
        quantityInStock -= amount;
        return true;
    }
    private void requirePositiveAmount(int amount) {
        if (amount <= 0) {
            throw new IllegalArgumentException(
                    "Amount must be greater than zero.");
        }
    }
 
    @Override
    public String toString() {
        return product +
                ", quantityInStock=" + quantityInStock;
    }
}


