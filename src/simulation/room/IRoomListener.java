package simulation.room;

import simulation.item.Item;

public interface IRoomListener {
    void itemAdded(Item item);

    void itemRemoved(Item item);
}
