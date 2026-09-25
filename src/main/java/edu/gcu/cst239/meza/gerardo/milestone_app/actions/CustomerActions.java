package edu.gcu.cst239.meza.gerardo.milestone_app.actions;

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
                    displayPlaceholder("view products");
                    break;

                case 2:
                    displayPlaceholder(
                            "search for a product by name or description");
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