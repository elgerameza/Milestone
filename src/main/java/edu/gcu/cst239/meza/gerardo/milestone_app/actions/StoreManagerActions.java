package edu.gcu.cst239.meza.gerardo.milestone_app.actions;

import edu.gcu.cst239.meza.gerardo.milestone_app.services.StoreFront;
import edu.gcu.cst239.meza.gerardo.milestone_app.util.InputUtilities;

/**
 * Handles actions available to a store manager, including viewing,
 * searching, adding, removing, updating, saving, and loading
 * inventory.
 */
public class StoreManagerActions {

    private final StoreFront store;

    /**
     * Creates the store-manager actions controller.
     *
     * @param store shared StoreFront application
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

            int choice = input.readInt(
                    "Choose an option: ", 0, 7);

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
                            "add a product to inventory");
                    break;

                case 4:
                    displayPlaceholder(
                            "remove a product from inventory");
                    break;

                case 5:
                    displayPlaceholder(
                            "update a product in inventory");
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