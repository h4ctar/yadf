package simulation.map;

/**
 * Interface for a map listener.
 */
public interface IMapListener {

    /**
     * A block on the map has changed.
     * @param mapIndex the block that changed
     */
    void mapChanged(MapIndex mapIndex);
}
