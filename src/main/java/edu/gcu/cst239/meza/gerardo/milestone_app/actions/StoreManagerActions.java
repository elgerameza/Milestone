package edu.gcu.cst239.meza.gerardo.milestone_app.actions;

import java.time.LocalDate;
import java.util.List;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;
import edu.gcu.cst239.meza.gerardo.milestone_app.models.Product;
import edu.gcu.cst239.meza.gerardo.milestone_app.services.InventoryService;
import edu.gcu.cst239.meza.gerardo.milestone_app.services.StoreFront;
import edu.gcu.cst239.meza.gerardo.milestone_app.util.InputUtilities;

/**
 * Handles console interaction for store-manager inventory operations.
 * Business and collection operations are delegated to InventoryService.
 */
public class StoreManagerActions {

    private final StoreFront store;

    /**
     * Creates the store-manager actions controller.
     *
     * @param store shared storefront application
     */
    public StoreManagerActions(StoreFront store) {
        this.store = store;
    }

    /**
     * Displays and handles the store-manager menu.
     */
    public void handleManagerActions() {
        InputUtilities input = new InputUtilities();
        boolean exitRequested = false;

        while (!exitRequested) {
            System.out.println();
            System.out.println("Store Manager Menu:");
            System.out.println("1. View Products");
            System.out.println(
                    "2. Search for a product by name or description");
            System.out.println("3. Add Product to Inventory");
            System.out.println("4. Remove Product from Inventory");
            System.out.println("5. Update Product in Inventory");
            System.out.println("6. Save Inventory to File");
            System.out.println("7. Load Inventory from File");
            System.out.println("0. Exit");

            int choice = input.readInt("Choose an option: ", 0, 7);

            switch (choice) {
                case 1:
                    displayProducts();
                    break;

                case 2:
                    searchProducts(input);
                    break;

                case 3:
                    addProduct(input);
                    break;

                case 4:
                    removeProduct(input);
                    break;

                case 5:
                    updateProduct(input);
                    break;

                case 6:
                    displayPlaceholder(
                            "save inventory to a file");
                    break;

                case 7:
                    displayPlaceholder(
                            "load inventory from a file");
                    break;

                case 0:
                    exitRequested = true;
                    break;

                default:
                    System.out.println(
                            "Invalid selection. Please try again.");
            }
        }
    }

    /**
     * Displays every inventory product and its current stock quantity.
     * Inventory is retrieved through the shared inventory service.
     */
    private void displayProducts() {
        InventoryService inventoryService = store.getInventoryManager();
        List<InventoryItem> items =
                inventoryService.getAllInventoryItems();

        if (items.isEmpty()) {
            System.out.println("The inventory is empty.");
            return;
        }

        System.out.println();
        System.out.println("--- Formula-Car Parts Inventory ---");

        for (InventoryItem item : items) {
            System.out.println(item);
        }
    }

    /**
     * Lets the manager search inventory by product name or description
     * and displays every matching item.
     *
     * @param input utility used to read console input
     */
    private void searchProducts(InputUtilities input) {
        System.out.println();
        System.out.println("--- Search Inventory ---");
        System.out.println("1. Search by name");
        System.out.println("2. Search by description");
        System.out.println("0. Return to manager menu");

        int searchType = input.readInt("Choose an option: ", 0, 2);

        if (searchType == 0) {
            return;
        }

        String searchTerm = input.readString(
                "Enter a search term: ").trim();

        if (searchTerm.isBlank()) {
            System.out.println("The search term cannot be blank.");
            return;
        }

        InventoryService inventoryService = store.getInventoryManager();
        List<InventoryItem> matches;

        if (searchType == 1) {
            matches = inventoryService.searchProductsByName(searchTerm);
        } else {
            matches = inventoryService.searchProductsByDescription(
                    searchTerm);
        }

        if (matches.isEmpty()) {
            System.out.println("No products matched your search.");
            return;
        }

        System.out.println();
        System.out.println("--- Search Results ---");

        for (InventoryItem item : matches) {
            System.out.println(item);
        }
    }

    /**
     * Collects product information and a starting stock quantity,
     * then delegates adding the item to the inventory service.
     *
     * @param input utility used to read console input
     */
    private void addProduct(InputUtilities input) {
        InventoryService inventoryService = store.getInventoryManager();

        System.out.println();
        System.out.println("--- Add Product to Inventory ---");

        int productId = input.readInt(
                "Enter product ID: ", 1, Integer.MAX_VALUE);

        if (inventoryService.getInventoryItemByProductId(productId)
                != null) {
            System.out.println(
                    "A product with that ID already exists. Nothing was added.");
            return;
        }

        String name = readRequiredText(
                input, "Enter product name: ");

        String description = readRequiredText(
                input, "Enter product description: ");

        LocalDate dateOfManufacture = input.readLocalDate(
                "Enter manufacture date: ", "uuuu-MM-dd");

        double price = input.readDouble(
                "Enter product price: ", 0.0, Double.MAX_VALUE);

        String category = readRequiredText(
                input, "Enter product category: ");

        int quantity = input.readInt(
                "Enter starting stock quantity: ", 0, Integer.MAX_VALUE);

        try {
            Product product = Product.builder()
                    .id(productId)
                    .name(name)
                    .description(description)
                    .dateOfManufacture(dateOfManufacture)
                    .price(price)
                    .category(category)
                    .build();

            InventoryItem item = new InventoryItem(product, quantity);

            if (inventoryService.addInventoryItem(item)) {
                System.out.println("Product added successfully.");
            } else {
                System.out.println(
                        "Product could not be added. Inventory was not changed.");
            }
        } catch (IllegalArgumentException exception) {
            System.out.println(
                    "Product could not be added: "
                            + exception.getMessage());
        }
    }

    /**
     * Prompts until the user enters nonblank text.
     *
     * @param input utility used to read console input
     * @param prompt message describing the requested value
     * @return trimmed, nonblank text
     */
    private String readRequiredText(
            InputUtilities input, String prompt) {

        while (true) {
            String value = input.readString(prompt).trim();

            if (!value.isBlank()) {
                return value;
            }

            System.out.println("This field cannot be blank.");
        }
    }

    /**
     * Collects a product ID and delegates inventory removal
     * to the shared inventory service.
     *
     * @param input utility used to read console input
     */
    private void removeProduct(InputUtilities input) {
        System.out.println();
        System.out.println("--- Remove Product from Inventory ---");

        int productId = input.readInt(
                "Enter product ID to remove: ", 1, Integer.MAX_VALUE);

        InventoryService inventoryService = store.getInventoryManager();

        if (inventoryService.removeProductById(productId)) {
            System.out.println("Product removed successfully.");
        } else {
            System.out.println(
                    "Product ID was not found. Inventory was not changed.");
        }
    }

    /**
     * Updates either product information or stock quantity for an
     * existing product. Product information updates preserve stock.
     *
     * @param input utility used to read console input
     */
    private void updateProduct(InputUtilities input) {
        InventoryService inventoryService = store.getInventoryManager();

        System.out.println();
        System.out.println("--- Update Product in Inventory ---");

        int productId = input.readInt(
                "Enter product ID to update: ", 1, Integer.MAX_VALUE);

        InventoryItem currentItem =
                inventoryService.getInventoryItemByProductId(productId);

        if (currentItem == null) {
            System.out.println(
                    "Product ID was not found. Inventory was not changed.");
            return;
        }

        System.out.println("Current item:");
        System.out.println(currentItem);
        System.out.println();
        System.out.println("1. Update product information");
        System.out.println("2. Update stock quantity");
        System.out.println("0. Return to manager menu");

        int updateType = input.readInt("Choose an option: ", 0, 2);

        if (updateType == 0) {
            return;
        }

        if (updateType == 2) {
            int quantity = input.readInt("Enter new stock quantity: ");

            if (inventoryService.updateQuantity(productId, quantity)) {
                System.out.println("Stock quantity updated successfully.");
            } else {
                System.out.println(
                        "Update failed. Quantity cannot be negative, "
                                + "and the product must exist.");
            }

            return;
        }

        String name = readRequiredText(
                input, "Enter new product name: ");

        String description = readRequiredText(
                input, "Enter new product description: ");

        LocalDate dateOfManufacture = input.readLocalDate(
                "Enter new manufacture date: ", "uuuu-MM-dd");

        double price = input.readDouble(
                "Enter new product price: ", 0.0, Double.MAX_VALUE);

        String category = readRequiredText(
                input, "Enter new product category: ");

        try {
            Product updatedProduct = Product.builder()
                    .id(productId)
                    .name(name)
                    .description(description)
                    .dateOfManufacture(dateOfManufacture)
                    .price(price)
                    .category(category)
                    .build();

            if (inventoryService.updateProduct(updatedProduct)) {
                System.out.println(
                        "Product information updated successfully. "
                                + "Stock quantity was preserved.");
            } else {
                System.out.println(
                        "Product could not be updated. Inventory was not changed.");
            }
        } catch (IllegalArgumentException exception) {
            System.out.println(
                    "Product could not be updated: "
                            + exception.getMessage());
        }
    }
    /**
     * Displays a message for an operation that is not implemented yet.
     *
     * @param action description of the selected action
     */
    private void displayPlaceholder(String action) {
        System.out.println("You chose to " + action + ".");
        System.out.println(
                "This feature has not been implemented yet.");
    }
}