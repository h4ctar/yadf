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

    /**
     * Constructor.
     * @param itemTypeTmp the type of item to look for
     * @param jobTmp the job that this state belongs to
     */
    public LookingForItemState(final ItemType itemTypeTmp, final AbstractJob jobTmp) {
        super(jobTmp);
        itemType = itemTypeTmp;
        category = null;
    }

    /**
     * Constructor.
     * @param categoryTmp the category of item to look for
     * @param jobTmp the job that this state belongs to
     */
    public LookingForItemState(final String categoryTmp, final AbstractJob jobTmp) {
        super(jobTmp);
        category = categoryTmp;
        itemType = null;
    }

    @Override
    public String toString() {
        return "Looking for item";
    }

    @Override
    public void start() {
        if (itemType != null) {
            item = getJob().getPlayer().getStockManager().getUnusedItem(itemType.name);
        } else {
            item = getJob().getPlayer().getStockManager().getUnusedItemFromCategory(category);
        }
        if (item == null) {
            if (itemType != null) {
                getJob().getPlayer().getStockManager().addListener(itemType, this);
            } else {
                getJob().getPlayer().getStockManager().addListener(category, this);
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
            getJob().getPlayer().getStockManager().removeListener(itemType, this);
        } else {
            getJob().getPlayer().getStockManager().removeListener(category, this);
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
            getJob().getPlayer().getStockManager().removeListener(itemType, this);
        } else {
            getJob().getPlayer().getStockManager().removeListener(category, this);
        }
    }
}
