package yadf.simulation.workshop;

import yadf.simulation.recipe.Recipe;

/**
 * Interface for a listener to a workshop, the're notified when an order is added or removed.
 */
public interface IWorkshopListener {
    /**
     * An order has been added.
     * @param recipe the recipe that was added
     */
    void orderAdded(Recipe recipe);

    /**
     * An order has been removed.
     * @param recipe the recipe that was removed
     */
    void orderRemoved(Recipe recipe);
}
