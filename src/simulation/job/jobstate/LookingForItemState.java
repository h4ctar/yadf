package simulation.job.jobstate;

import simulation.item.IContainer;
import simulation.item.IItemAvailableListener;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.job.AbstractJob;

/**
 * Generic looking for item job state.
 */
public abstract class LookingForItemState extends AbstractJobState implements IItemAvailableListener {

    /** The required itemType. */
    private final ItemType itemType;

    /** The required category. */
    private final String category;

    /** The found item. */
    private Item item;

    /** Should it only look for used items. */
    private boolean used;

    /** Should it only look for placed items. */
    private boolean placed;

    /**
     * Constructor.
     * @param itemTypeTmp the type of item to look for
     * @param usedTmp true to only find used items
     * @param placedTmp true to only find placed items
     * @param jobTmp the job that this state belongs to
     */
    public LookingForItemState(final ItemType itemTypeTmp, final boolean usedTmp, final boolean placedTmp,
            final AbstractJob jobTmp) {
        super(jobTmp);
        itemType = itemTypeTmp;
        used = usedTmp;
        placed = placedTmp;
        category = null;
    }

    /**
     * Constructor.
     * @param categoryTmp the category of item to look for
     * @param usedTmp true to only find used items
     * @param placedTmp true to only find placed items
     * @param jobTmp the job that this state belongs to
     */
    public LookingForItemState(final String categoryTmp, final boolean usedTmp, final boolean placedTmp,
            final AbstractJob jobTmp) {
        super(jobTmp);
        category = categoryTmp;
        used = usedTmp;
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
            item = getJob().getPlayer().getStockManager().getItem(itemType.name, used, placed);
        } else {
            item = getJob().getPlayer().getStockManager().getItemFromCategory(category, used, placed);
        }
        if (item == null) {
            if (itemType != null) {
                getJob().getPlayer().getStockManager().addItemAvailableListener(itemType, this);
            } else {
                getJob().getPlayer().getStockManager().addItemAvailableListenerListener(category, this);
            }
        } else {
            item.setUsed(true);
            finishState();
        }
    }

    @Override
    public void itemAvailable(final Item availableItem, final IContainer container) {
        assert !availableItem.isUsed();
        assert container == getJob().getPlayer().getStockManager();
        assert (itemType != null && itemType.equals(availableItem.getType()))
                || (category != null && category.equals(availableItem.getType().category));
        item = availableItem;
        item.setUsed(true);
        if (itemType != null) {
            getJob().getPlayer().getStockManager().removeItemAvailableListener(itemType, this);
        } else {
            getJob().getPlayer().getStockManager().removeItemAvailableListener(category, this);
        }
        finishState();
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
        if (itemType != null) {
            getJob().getPlayer().getStockManager().removeItemAvailableListener(itemType, this);
        } else {
            getJob().getPlayer().getStockManager().removeItemAvailableListener(category, this);
        }
    }
}
