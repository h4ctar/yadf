package yadf.simulation;

import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;

/**
 * Interface for an entity.
 */
public interface IEntity extends IGameObject {

    /**
     * Get the position of the entity.
     * @return the position
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
     * Checks if a map index is within the entity.
     * @param index the index
     * @return true, if successful
     */
    boolean containsIndex(final MapIndex index);
}
