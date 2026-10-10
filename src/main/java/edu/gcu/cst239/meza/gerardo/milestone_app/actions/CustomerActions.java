package edu.gcu.cst239.meza.gerardo.milestone_app.actions;

import java.util.List;

import edu.gcu.cst239.meza.gerardo.milestone_app.models.InventoryItem;
import edu.gcu.cst239.meza.gerardo.milestone_app.services.InventoryService;
import edu.gcu.cst239.meza.gerardo.milestone_app.services.StoreFront;
import edu.gcu.cst239.meza.gerardo.milestone_app.util.InputUtilities;

/**
 * Handles the actions available to customers, including browsing
 * products and managing their shopping cart.
 */
public class CustomerActions {

    private final StoreFront store;

    /**
     * Creates the customer-actions controller.
     *
     * @param store shared StoreFront application
     */
    public CustomerActions(StoreFront store) {
        this.store = store;
    }

    /**
     * Displays and handles the customer-actions menu.
     */
    public void handleCustomerActions() {
        InputUtilities input = new InputUtilities();
        boolean exitRequested = false;

        while (!exitRequested) {
            System.out.println();
            System.out.println("Customer Menu:");
            System.out.println("1. View Products");
            System.out.println(
                    "2. Search for product by name or description");
            System.out.println("3. Add Product to Cart");
            System.out.println("4. Remove Product from Cart");
            System.out.println("5. View Cart");
            System.out.println("6. Checkout");
            System.out.println("0. Exit");

            int choice = input.readInt(
                    "Choose an option: ", 0, 6);

            switch (choice) {
                case 1:
                    displayProducts(input);
                    break;

                case 2:
                    searchProducts(input);
                    break;

                case 3:
                    displayPlaceholder(
                            "add a product to the cart");
                    break;

                case 4:
                    displayPlaceholder(
                            "remove a product from the cart");
                    break;

                case 5:
                    displayPlaceholder("view the cart");
                    break;

                case 6:
                    displayPlaceholder("checkout");
                    break;

                case 0:
                    exitRequested = true;
                    break;

                default:
                    // This should not occur because readInt checks the range.
                    System.out.println(
                            "Invalid selection. Please try again.");
            }
        }
    }
    /**
     * Displays in-stock products in the customer's selected order.
     *
     * @param input utility used to read console input
     */
    private void displayProducts(InputUtilities input) {
        System.out.println();
        System.out.println("--- Browse Products ---");
        System.out.println("1. Sort by name");
        System.out.println("2. Sort by manufacture date");
        System.out.println("3. Sort by price");
        System.out.println("0. Return to customer menu");

        int sortChoice = input.readInt("Choose an option: ", 0, 3);

        if (sortChoice == 0) {
            return;
        }

        InventoryService inventoryService = store.getInventoryManager();
        List<InventoryItem> items;

        switch (sortChoice) {
            case 1:
                items = inventoryService.getInventoryItemsSortedByName();
                break;

            case 2:
                items = inventoryService.getInventoryItemsSortedByDate();
                break;

            case 3:
                items = inventoryService.getInventoryItemsSortedByPrice();
                break;

            default:
                return;
        }

        System.out.println();
        System.out.println("--- Available Products ---");

        boolean availableProductsFound = false;

        for (InventoryItem item : items) {
            if (item.getQuantityInStock() > 0) {
                System.out.println(item);
                availableProductsFound = true;
            }
        }

        if (!availableProductsFound) {
            System.out.println("No products are currently available.");
        }
    }
    
    /**
     * Searches products by name or description through the inventory
     * service and displays every match with its availability.
     *
     * @param input utility used to read console input
     */
    private void searchProducts(InputUtilities input) {
        System.out.println();
        System.out.println("--- Search Products ---");
        System.out.println("1. Search by name");
        System.out.println("2. Search by description");
        System.out.println("0. Return to customer menu");

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

            if (item.getQuantityInStock() > 0) {
                System.out.println(
                        "Availability: In stock ("
                                + item.getQuantityInStock()
                                + " available)");
            } else {
                System.out.println("Availability: Out of stock");
            }

            System.out.println();
        }
    }

    /**
     * Displays the temporary Milestone 1 placeholder message.
     *
     * @param action description of the selected action
     */
    private void displayPlaceholder(String action) {
        System.out.println("You chose to " + action + ".");
        System.out.println(
                "This is milestone #1. "
                        + "No actual functionality has been implemented yet.");
    }
}