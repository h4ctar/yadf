package yadf.simulation.workshop;

import yadf.simulation.AbstractGameObjectManager;

/**
 * The workshop manager.
 */
public class WorkshopManager extends AbstractGameObjectManager<IWorkshop> implements IWorkshopManager {

    @Override
    public void update() {
        for (IWorkshop workshop : getGameObjects()) {
            workshop.update();
        }
    }
}
