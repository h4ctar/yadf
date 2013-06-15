package simulation;

import simulation.stock.IStockManager;

public interface IPlayer {
    IStockManager getStockManager();

    void addListener(IPlayerListener listener);
}
