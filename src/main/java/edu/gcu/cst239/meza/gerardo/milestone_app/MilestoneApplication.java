package edu.gcu.cst239.meza.gerardo.milestone_app;

import edu.gcu.cst239.meza.gerardo.milestone_app.actions.StartingActions;
import edu.gcu.cst239.meza.gerardo.milestone_app.services.StoreFront;

/**
 * Provides the entry point for the storefront application
 * 
 * MilestoneApplication
 */
public class MilestoneApplication {
    /**
     * Starts the storefront console application
     * 
     * @param args command line arguments
     */
    public static void main(String[] args) {

        System.out.println("Milestone application starting... ");
        StoreFront store = new StoreFront();
        StartingActions startingActions = new StartingActions(store);

        startingActions.start();
        // Create the application objects and begin the console workflow.
    }
}


