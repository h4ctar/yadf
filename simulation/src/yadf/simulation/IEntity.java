package yadf.simulation;

import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;

/**
 * Interface for an entity.
 */
public interface IEntity extends IGameObject {

    /**
     * Gets the position of this entity.
     * @return A reference to the position
     */
    MapIndex getPosition();

    /**
     * Sets the position.
     * @param positionTmp the new position
     */
    void setPosition(final MapIndex positionTmp);

    /**
     * Get the area of the entity.
     * @return the area
     */
    MapArea getArea();

    /**
     * Get the width of the entity.
     * @return the width
     */
    int getWidth();

    /**
     * Get the height of the entity.
     * @return the height
     */
    int getHeight();

    /**
     * Checks if a map index is within the entity.
     * @param index the index
     * @return true, if successful
     */
    boolean hasIndex(final MapIndex index);
}
