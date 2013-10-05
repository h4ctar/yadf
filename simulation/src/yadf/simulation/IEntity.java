package yadf.simulation;

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
}
