package yadf.simulation.workshop;

import java.util.List;

import yadf.simulation.IEntity;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;
import yadf.simulation.recipe.Recipe;

/**
 * Interface for a workshop.
 */
public interface IWorkshop extends IEntity {

    /**
     * Gets the position of the workshop.
     * @return the position
     */
    MapIndex getPosition();

    /**
     * Update.
     */
    void update();

    /**
     * Checks if a map index is within the workshop.
     * @param index the index
     * @return true, if successful
     */
    boolean hasIndex(final MapIndex index);

    /**
     * New order.
     * @param recipeName the recipe name
     */
    void newOrder(final String recipeName);

    /**
     * Cancel an order.
     * @param orderIndex the order index
     */
    void cancelOrder(final int orderIndex);

    /**
     * Gets the orders.
     * @return the orders
     */
    List<Recipe> getOrders();

    /**
     * Get the area of the workshop.
     * @return the area
     */
    MapArea getArea();

    /**
     * Gets the type of the workshop.
     * @return the type
     */
    WorkshopType getType();

    /**
     * Add a listener to changes to the workshop.
     * @param listener the listener to add
     */
    void addListener(final IWorkshopListener listener);

    /**
     * Removes a listener to changes to the workshop.
     * @param listener the listener to remove
     */
    void removeListener(final IWorkshopListener listener);
}
