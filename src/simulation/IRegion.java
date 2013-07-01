package simulation;

import simulation.map.MapArea;
import simulation.map.MapIndex;
import simulation.map.RegionMap;
import simulation.tree.ITreeManager;

/**
 * Interface for a region.
 */
public interface IRegion {

    /**
     * Gets the map.
     * @return the map
     */
    RegionMap getMap();

    /**
     * Get the tree manager.
     * @return the tree manager
     */
    ITreeManager getTreeManager();

    /**
     * Checks if the location is valid for a new stockpile or building or similar thing that needs a flat area without
     * trees or items.
     * @param mapArea The area that the new stockpile will occupy
     * @return True if the area is a valid location else false
     */
    boolean checkAreaValid(MapArea mapArea);

    /**
     * Check index valid.
     * @param mapIndex the map index
     * @return true, if successful
     */
    boolean checkIndexValid(MapIndex mapIndex);

    /**
     * Add a time listener.
     * @param duration how long until they want to be notified
     * @param listener the listener to add
     * @return the time which they will be notified
     */
    long addTimeListener(long duration, ITimeListener listener);

    /**
     * Remove a time listener.
     * @param notifyTime the time which they were being notified
     * @param listener the listener to remove
     */
    void removeTimeListener(long notifyTime, ITimeListener listener);
}
