package edu.gcu.cst239.meza.gerardo.milestone_app.actions;

import edu.gcu.cst239.meza.gerardo.milestone_app.services.StoreFront;
import edu.gcu.cst239.meza.gerardo.milestone_app.util.InputUtilities;

/**
 * Provides the initial role selection for the StoreFront
 * application. When started, the user is greeted and asked whether
 * they wish to use the application as a customer or as a store
 * manager. Based on the selection, control is handed off to either
 * {@link CustomerActions} or {@link StoreManagerActions}. 
 */
public class StartingActions {

    private final StoreFront store;
    /**
     * 
     * @param store shared storefront application
     */
    public StartingActions(StoreFront store) {
        this.store = store;
    }

    /**
     * Starts the initial prompt asking the user how they would like
     * to use the application. This method never returns control
     * unless the user chooses to exit the program from the sub menus.
     */
    public void start() {
        InputUtilities input = new InputUtilities();
        boolean exitRequested = false;
        while (!exitRequested) {
            System.out.println();
            System.out.println("Welcome to the StoreFront application!");
            System.out.println("How would you like to use the app?");
            System.out.println("1. Customer");
            System.out.println("2. Store manager");
            System.out.println("3. Exit");
            int choice = input.readInt("Enter 1, 2 or 3: ", 1, 3);
            switch (choice) {
                case 1:
                    System.out.println("You are using the app as a customer.");
                    CustomerActions customerActions = new CustomerActions(store);
                    customerActions.handleCustomerActions();
                    break;
                case 2:
                    System.out.println("You are using the app as a store manager.");
                    StoreManagerActions managerActions = new StoreManagerActions(store);
                    managerActions.handleManagerActions();
                    break;
                case 3:
                    exitRequested = true;
                    break;
                default:
                    // This case should never occur because readInt enforces range
                    System.out.println("Invalid selection. Please try again.");
            }
        }
    }
}
