package edu.gcu.cst239.meza.gerardo.milestone_app.services;

import java.util.List;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;

public class InventoryManager implements InventoryService{

    @Override
    public List<InventoryItem> getAllInventoryItems() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getAllInventoryItems'");
    }

    @Override
    public InventoryItem getInventoryItemByProductId(int productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getInventoryItemByProductId'");
    }

    @Override
    public List<InventoryItem> searchProductsByName(String searchTerm) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchProductsByName'");
    }

    @Override
    public List<InventoryItem> searchProductsByDescription(String searchTerm) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'searchProductsByDescription'");
    }

    @Override
    public boolean addInventoryItem(InventoryItem item) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addInventoryItem'");
    }

    @Override
    public boolean updateProduct(Product updatedProduct) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateProduct'");
    }

    @Override
    public boolean updateQuantity(int productId, int quantityInStock) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'updateQuantity'");
    }

    @Override
    public boolean removeProductById(int productId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeProductById'");
    }

    @Override
    public void clearInventory() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'clearInventory'");
    }
    
}
