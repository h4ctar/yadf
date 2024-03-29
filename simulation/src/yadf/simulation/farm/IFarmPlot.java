package yadf.simulation.farm;

import yadf.simulation.map.MapIndex;

/**
 * Interface for a farm plot.
 */
public interface IFarmPlot {

    /**
     * Get the position of the farm plot.
     * @return the position
     */
    MapIndex getPosition();
}
