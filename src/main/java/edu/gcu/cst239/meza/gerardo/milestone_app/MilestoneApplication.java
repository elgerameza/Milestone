package edu.gcu.cst239.meza.gerardo.milestone_app;

import java.io.IOException;

import edu.gcu.cst239.meza.gerardo.milestone_app.actions.StartingActions;
import edu.gcu.cst239.meza.gerardo.milestone_app.services.StoreFront;

/**
 * Provides the entry point for the storefront application.
 */
public class MilestoneApplication {

    /**
     * Loads store data and starts the console application.
     *
     * @param args command line arguments
     */
    public static void main(String[] args) {
        System.out.println("Milestone application starting...");

        try {
            StoreFront store = new StoreFront();

            int productCount = store.getInventoryManager()
                    .getAllInventoryItems()
                    .size();

            System.out.println(
                    "Loaded " + productCount + " products from JSON.");

            StartingActions startingActions =
                    new StartingActions(store);

            startingActions.start();

        } catch (IOException | IllegalArgumentException exception) {
            System.err.println(
                    "Unable to load store data: "
                            + exception.getMessage());
        }
    }
}