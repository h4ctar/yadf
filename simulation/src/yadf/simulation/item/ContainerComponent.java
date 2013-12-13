package yadf.simulation.item;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

import yadf.simulation.AbstractGameObject;
import yadf.simulation.IGameObject;
import yadf.simulation.IGameObjectListener;
import yadf.simulation.IGameObjectManagerListener;

/**
 * This is a component that will contain an implementation of the IContainer interface, other classes that implement the
 * IContainer interface can contain a field of this type and delegate all the methods to it.
 * <p>
 * This is basically a hack to get around no multiple inheritance.
 */
public class ContainerComponent extends AbstractGameObject implements IContainer, IItemAvailableListener,
        IGameObjectListener {

    /** The content items. */
    private final List<Item> items = new ArrayList<>();

    /** The container that this component belongs to. */
    private final IContainer container;

    /** Listeners for item additions and removals. */
    private final List<IGameObjectManagerListener> managerListeners = new ArrayList<>();

    /** Listeners for available items. */
    private final Map<ItemType, Set<IItemAvailableListener>> itemAvailableListeners = new ConcurrentHashMap<>();

    /**
     * Constructor.
     * @param containerTmp the container that this component belongs to
     */
    public ContainerComponent(final IContainer containerTmp) {
        container = containerTmp;
    }

    @Override
    public boolean addItem(final Item item) {
        boolean itemAdded = items.add(item);
        if (itemAdded) {
            notifyItemAdded(item);
            if (!item.used) {
                notifyItemAvailable(item);
            }
            item.addGameObjectListener(this);
        }
        return itemAdded;
    }

    @Override
    public boolean removeItem(final Item item) {
        int index = items.indexOf(item);
        boolean itemRemoved = items.remove(item);
        if (itemRemoved) {
            // Only notify when an item is directly removed from this container, not if its removed from any of the sub
            // containers
            item.removeGameObjectListener(this);
            notifyItemRemoved(item, index);
        }
        if (!itemRemoved) {
            for (Item containerTmp : getItems()) {
                if (containerTmp instanceof IContainer) {
                    if (((IContainer) containerTmp).removeItem(item)) {
                        itemRemoved = true;
                        break;
                    }
                }
            }
        }
        return itemRemoved;
    }

    @Override
    public List<Item> getItems() {
        return items;
    }

    @Override
    public Item getItem(final String itemTypeName, final boolean used, final boolean placed) {
        Item foundItem = null;
        for (Item item : items) {
            if (item.getType().name.equals(itemTypeName) && item.isUsed() == used && item.isPlaced() == placed
                    && !item.isDeleted()) {
                foundItem = item;
                break;
            }
            if (item instanceof IContainer) {
                Item contentItem = ((IContainer) item).getItem(itemTypeName, used, placed);
                if (contentItem != null) {
                    return contentItem;
                }
            }
        }
        return foundItem;
    }

    @Override
    public Item getItemFromCategory(final String category, final boolean used, final boolean placed) {
        Item foundItem = null;
        for (Item item : items) {
            if (item.getType().category.equals(category) && !item.isDeleted() && item.isUsed() == used
                    && item.isPlaced() == placed) {
                foundItem = item;
                break;
            }
            if (item instanceof IContainer) {
                Item contentItem = ((IContainer) item).getItemFromCategory(category, used, placed);
                if (contentItem != null) {
                    return contentItem;
                }
            }
        }
        return foundItem;
    }

    @Override
    public int getItemQuantity(final ItemType itemType) {
        int count = 0;
        for (Item item : getItems()) {
            if (item.getType().equals(itemType)) {
                count++;
            }
            if (item instanceof IContainer) {
                count += ((IContainer) item).getItemQuantity(itemType);
            }
        }
        return count;
    }

    @Override
    public int getItemQuantity(final String category) {
        int count = 0;
        for (Item item : items) {
            if (item.getType().category.equals(category)) {
                count++;
            }
            if (item instanceof IContainer) {
                count += ((IContainer) item).getItemQuantity(category);
            }
        }
        return count;
    }

    @Override
    public void addGameObjectManagerListener(final IGameObjectManagerListener listener) {
        assert !managerListeners.contains(listener);
        managerListeners.add(listener);
    }

    @Override
    public void removeGameObjectManagerListener(final IGameObjectManagerListener listener) {
        assert managerListeners.contains(listener);
        managerListeners.remove(listener);
    }

    @Override
    public void addItemAvailableListener(final ItemType itemType, final IItemAvailableListener listener) {
        if (!itemAvailableListeners.containsKey(itemType)) {
            itemAvailableListeners.put(itemType, new CopyOnWriteArraySet<IItemAvailableListener>());
        }
        assert !itemAvailableListeners.get(itemType).contains(listener);
        itemAvailableListeners.get(itemType).add(listener);
    }

    @Override
    public void addItemAvailableListenerListener(final String category, final IItemAvailableListener listener) {
        for (ItemType itemType : ItemTypeManager.getInstance().getItemTypes()) {
            if (itemType.category.equals(category)) {
                addItemAvailableListener(itemType, listener);
            }
        }
    }

    @Override
    public void removeItemAvailableListener(final ItemType itemType, final IItemAvailableListener listener) {
        assert itemAvailableListeners.containsKey(itemType);
        assert itemAvailableListeners.get(itemType).contains(listener);
        Set<IItemAvailableListener> listeners = itemAvailableListeners.get(itemType);
        listeners.remove(listener);
        if (listeners.isEmpty()) {
            itemAvailableListeners.remove(itemType);
        }
    }

    @Override
    public void removeItemAvailableListener(final String category, final IItemAvailableListener listener) {
        List<ItemType> itemTypes = ItemTypeManager.getInstance().getItemTypesFromCategory(category);
        for (ItemType itemType : itemTypes) {
            removeItemAvailableListener(itemType, listener);
        }
    }

    @Override
    public void itemAvailable(final Item availableItem, final IContainer containerTmp) {
        notifyItemAvailable(availableItem);
    }

    /**
     * Notify listeners that an item has been added.
     * @param item the new item
     */
    private void notifyItemAdded(final Item item) {
        for (IGameObjectManagerListener listener : managerListeners) {
            listener.gameObjectAdded(item, items.indexOf(item));
        }
    }

    /**
     * Notify listeners that an item has been removed.
     * @param item the removed item
     * @param index the index of the removed item
     */
    private void notifyItemRemoved(final Item item, final int index) {
        for (IGameObjectManagerListener listener : managerListeners) {
            listener.gameObjectRemoved(item, index);
        }
    }

    /**
     * Notify all listeners that an item has become available.
     * @param item the listeners of this item type will be notified with this item
     */
    private void notifyItemAvailable(final Item item) {
        if (itemAvailableListeners.containsKey(item.getType())) {
            for (IItemAvailableListener listener : itemAvailableListeners.get(item.getType())) {
                listener.itemAvailable(item, container);
                if (item.isUsed()) {
                    break;
                }
            }
        }
    }

    @Override
    public void gameObjectDeleted(final IGameObject gameObject) {
        assert items.contains(gameObject);
        removeItem((Item) gameObject);
    }

    @Override
    public void gameObjectChanged(final IGameObject gameObject) {
        Item item = (Item) gameObject;
        if (!item.used) {
            notifyItemAvailable(item);
        }
    }
}
