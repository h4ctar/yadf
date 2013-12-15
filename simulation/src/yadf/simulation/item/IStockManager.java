package yadf.simulation.item;

import yadf.simulation.IPlayerComponent;

public interface IStockManager extends IItemManager, IPlayerComponent {

    IItemManager getUnstoredItemManager();

    IStockpileManager getStockpileManager();
}
