package yadf.simulation.job.jobstate;

import yadf.simulation.IGameObject;
import yadf.simulation.IGameObjectAvailableListener;
import yadf.simulation.item.IStockManager;
import yadf.simulation.item.Item;
import yadf.simulation.item.ItemType;
import yadf.simulation.job.AbstractJob;

/**
 * Generic looking for item job state.
 */
public abstract class LookingForItemState extends AbstractJobState implements IGameObjectAvailableListener {

    /** The required itemType. */
    private final ItemType itemType;

    /** The required category. */
    private final String category;

    /** The found item. */
    private Item item;

    /** Should it only look for placed items. */
    private boolean placed;

    /**
     * Constructor.
     * @param itemTypeTmp the type of item to look for
     * @param placedTmp true to only find placed items
     * @param jobTmp the job that this state belongs to
     */
    public LookingForItemState(final ItemType itemTypeTmp, final boolean placedTmp, final AbstractJob jobTmp) {
        super(jobTmp);
        itemType = itemTypeTmp;
        placed = placedTmp;
        category = null;
    }

    /**
     * Constructor.
     * @param categoryTmp the category of item to look for
     * @param placedTmp true to only find placed items
     * @param jobTmp the job that this state belongs to
     */
    public LookingForItemState(final String categoryTmp, final boolean placedTmp, final AbstractJob jobTmp) {
        super(jobTmp);
        category = categoryTmp;
        placed = placedTmp;
        itemType = null;
    }

    @Override
    public String toString() {
        return "Looking for item";
    }

    @Override
    public void start() {
        if (itemType != null) {
            item = getJob().getPlayer().getComponent(IStockManager.class).getItem(itemType.name, placed);
        } else {
            item = getJob().getPlayer().getComponent(IStockManager.class).getItemFromCategory(category, placed);
        }
        if (item == null) {
            getJob().getPlayer().getComponent(IStockManager.class).addAvailableListener(this);
        } else {
            item.setAvailable(false);
            finishState();
        }
    }

    @Override
    public void gameObjectAvailable(final IGameObject gameObject) {
        assert gameObject.isAvailable();
        Item availableItem = (Item) gameObject;
        if (itemType != null && itemType.equals(availableItem.getType())
                || (category != null && category.equals(availableItem.getType().category))) {
            item = availableItem;
            item.setAvailable(false);
            getJob().getPlayer().getComponent(IStockManager.class).removeAvailableListener(this);
            finishState();
        }
    }

    /**
     * Get the item that was found.
     * @return the item
     */
    public Item getItem() {
        return item;
    }

    @Override
    public void interrupt(final String message) {
        getJob().getPlayer().getComponent(IStockManager.class).removeAvailableListener(this);
    }
}
