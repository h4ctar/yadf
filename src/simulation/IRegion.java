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

    boolean checkAreaValid(MapArea mapArea);

    boolean checkIndexValid(MapIndex mapIndex);

    long addTimeListener(long duration, ITimeListener listener);

    void removeTimeListener(long notifyTime, ITimeListener listener);
}
