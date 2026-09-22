package edu.gcu.cst239.meza.gerardo.milestone_app.models;

import java.util.Objects;

public class InventoryItem {
 
    private final Product product;
    private int quantityInStock;
 
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
 
    public Product getProduct() {
        return product;
    }
 
    public int getQuantityInStock() {
        return quantityInStock;
    }
 
    public void increaseQuantity(int amount) {
        requirePositiveAmount(amount);
        quantityInStock += amount;
    }
 
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


