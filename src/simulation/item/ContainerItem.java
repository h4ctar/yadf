package simulation.item;

import java.util.ArrayList;
import java.util.List;

import logger.Logger;

import org.w3c.dom.Element;

import simulation.Player;
import simulation.job.HaulJob;
import simulation.job.IJob;
import simulation.job.IJobListener;
import simulation.map.MapIndex;
import simulation.stock.IStockManagerListener;

/**
 * An item that can contain other items, i.e. barrel or chest.
 */
public class ContainerItem extends Item implements IContainer, IJobListener, IStockManagerListener {

    /** The serial version UID. */
    private static final long serialVersionUID = 4639675681496031393L;

    /** The contents of this item if it's a container. */
    private final List<Item> contentItems = new ArrayList<>();

    /** The type of item that this item is storing if it's a container. */
    private ItemType contentItemType;

    /** The haul jobs created to haul content items to this item if it's a container. */
    private final List<HaulJob> haulJobs = new ArrayList<>();

    /**
     * Constructor from a DOM element. This item will not belong to a player. Only useful for temporary items that can
     * be cloned.
     * @param itemElement the DOM element to get attributes from
     * @throws Exception something went wrong
     */
    public ContainerItem(final Element itemElement) throws Exception {
        super(itemElement);
        String contentTypeName = itemElement.getAttribute("contentType");
        contentItemType = ItemTypeManager.getInstance().getItemType(contentTypeName);
        String tempString = itemElement.getAttribute("contentQuantity");
        int quantity = "".equals(tempString) ? 0 : Integer.parseInt(tempString);
        for (int i = 0; i < quantity; i++) {
            Item contentItem = new Item(new MapIndex(), contentItemType, player);
            contentItems.add(contentItem);
        }
        if (player != null) {
            player.getStockManager().addListener(this);
        }
    }

    /**
     * Create an item from an item type.
     * @param position the position of the new item
     * @param itemTypeTmp the type of the new item
     * @param playerTmp the player that the item will belong to
     */
    public ContainerItem(final MapIndex position, final ItemType itemTypeTmp, final Player playerTmp) {
        super(position, itemTypeTmp, playerTmp);
        if (player != null) {
            player.getStockManager().addListener(this);
        }
    }

    /**
     * Create an item from another item, i.e. copy constructor.
     * @param item the item to clone
     * @param playerTmp the player that the new item will belong to
     */
    public ContainerItem(final ContainerItem item, final Player playerTmp) {
        super(item, playerTmp);
        for (Item contentItem : item.contentItems) {
            Item newContentItem = new Item(contentItem, playerTmp);
            contentItems.add(newContentItem);
        }
        contentItemType = item.contentItemType;
        if (player != null) {
            player.getStockManager().addListener(this);
        }
    }

    @Override
    public Item getUnusedItem(final String itemTypeName) {
        if (contentItemType != null && contentItemType.equals(itemTypeName)) {
            for (Item contentItem : contentItems) {
                if (!contentItem.isUsed() && !contentItem.getRemove()) {
                    return contentItem;
                }
            }
        }
        return null;
    }

    @Override
    public Item getUnusedItemFromCategory(final String category) {
        if (contentItemType != null && contentItemType.category.equals(category)) {
            for (Item contentItem : contentItems) {
                if (!contentItem.isUsed() && !contentItem.getRemove()) {
                    return contentItem;
                }
            }
        }
        return null;
    }

    @Override
    public boolean removeItem(final Item item) {
        Logger.getInstance().log(this, "Removing item - itemType: " + item.getType());
        return contentItems.remove(item);
    }

    @Override
    public boolean addItem(final Item item) {
        Logger.getInstance().log(this, "Adding item - itemType: " + item.getType());
        boolean itemAdded = false;
        if (!item.getPosition().equals(position)) {
            Logger.getInstance().log(this,
                    "Item could not be added to container as they are not in the same location");
        } else if (contentItems.size() < itemType.capacity) {
            contentItems.add(item);
            itemAdded = true;
        } else {
            Logger.getInstance().log(this, "Item could not be added to container as the container is full");
        }
        return itemAdded;
    }

    /**
     * Create haul tasks to fill up the stockpile.
     */
    private void createHaulJobs() {
        if (contentItemType != null) {
            for (int i = contentItems.size() + haulJobs.size(); i < itemType.capacity; i++) {
                Logger.getInstance().log(this, "Create haul jobs");
                Item item = player.getStockManager().getUnstoredItem(contentItemType);
                if (item != null) {
                    item.setUsed(true);
                    HaulJob haulJob = new HaulJob(item, this, position);
                    haulJob.addListener(this);
                    player.getJobManager().addJob(haulJob);
                    haulJobs.add(haulJob);
                } else {
                    break;
                }
            }
        }
    }

    @Override
    public void jobChanged(final IJob job) {
        if (job.isDone()) {
            if (haulJobs.remove(job)) {
                HaulJob haulJob = (HaulJob) job;
                Logger.getInstance().log(this,
                        "Haul job is finished, job removed - itemType: " + haulJob.getItem().getType());
                haulJob.getItem().setUsed(false);
            } else {
                Logger.getInstance().log(this, "Job should be in the haulJobs list, something went wrong");
            }
        }
    }

    @Override
    public void stockManagerChanged() {
        if (contentItemType == null || contentItems.isEmpty()) {
            selectContentItemType();
        }
        // TODO: should only listen to stockmanager adding new items of type contentItemType
        if (contentItemType != null) {
            createHaulJobs();
        }
    }

    /**
     * Allocate this container to an item type based on what unstored items need to be stored.
     */
    private void selectContentItemType() {
        // TODO: this also needs to look at items in stockpiles that arn't in a container
        System.out.println("select content item type");
        // A haul job has already found an item
        for (HaulJob haulJob : haulJobs) {
            if (haulJob.getItem() != null) {
                Logger.getInstance().log(this, "A haul job has already found an item");
                return;
            }
        }

        int maxQuantity = 0;
        ItemType maxItemType = null;
        for (String contentItemTypeName : itemType.contentItemTypeNames) {
            int quantity = player.getStockManager().getItemQuantity(contentItemTypeName);
            if (quantity > maxQuantity) {
                maxQuantity = quantity;
                maxItemType = contentItemType;
            }
        }
        if (maxQuantity > 2) {
            Logger.getInstance().log(this,
                    "Content type of " + itemType + " is set to " + maxItemType + ", " + maxQuantity + " found");
            contentItemType = maxItemType;
        } else {
            contentItemType = null;
        }
    }

    /**
     * Get the type of item that this container stores.
     * @return the item type
     */
    public ItemType getContentItemType() {
        return contentItemType;
    }

    @Override
    public void setPosition(final MapIndex positionTmp) {
        super.setPosition(positionTmp);
        for (Item item : contentItems) {
            item.setPosition(positionTmp);
        }
    }

    @Override
    public boolean canBeStored(final List<ItemType> itemTypes) {
        boolean storable = false;
        if (!getRemove() && !placed) {
            if (contentItemType == null && itemTypes.contains(itemType)) {
                storable = true;
            } else if (itemTypes.contains(contentItemType)) {
                storable = true;
            }
        }
        return storable;
    }

    /**
     * Get all the items that are in this container.
     * @return the items
     */
    public List<Item> getContentItems() {
        return contentItems;
    }
}
