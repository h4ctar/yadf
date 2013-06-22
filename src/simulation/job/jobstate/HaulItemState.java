package simulation.job.jobstate;

import simulation.character.Dwarf;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.job.AbstractJob;
import simulation.job.HaulJob;
import simulation.job.IJob;
import simulation.job.IJobListener;
import simulation.map.MapIndex;

/**
 * Generic job state to haul a single item, can either be completed by a specific dwarf, or will find one.
 * 
 * Launches a haul job and waits till it's done.
 */
public abstract class HaulItemState extends AbstractJobState implements IJobListener {

    /** The position to haul the item to. */
    private final MapIndex position;

    /** The haul job. */
    private HaulJob haulJob;

    /** The dwarf to do the hauling, can be null if don't care who hauls. */
    private Dwarf dwarf;

    /** The item to haul. */
    private Item item;

    /** The type of item to haul. */
    private ItemType itemType;

    /**
     * Constructor.
     * @param dwarfTmp the dwarf to do the hauling
     * @param itemTmp the item to haul
     * @param positionTmp the position to haul the item to
     * @param jobTmp the job that this state belong to
     */
    public HaulItemState(final Dwarf dwarfTmp, final Item itemTmp, final MapIndex positionTmp,
            final AbstractJob jobTmp) {
        super(jobTmp);
        dwarf = dwarfTmp;
        item = itemTmp;
        position = positionTmp;
    }

    /**
     * Constructor.
     * @param itemTmp the item to haul
     * @param positionTmp the position to haul the item to
     * @param jobTmp the job that this state belong to
     */
    public HaulItemState(final Item itemTmp, final MapIndex positionTmp, final AbstractJob jobTmp) {
        super(jobTmp);
        item = itemTmp;
        position = positionTmp;
    }

    @Override
    public String toString() {
        return "Hauling item";
    }

    /**
     * Constructor.
     * @param itemTypeTmp the item type to haul
     * @param positionTmp the position to haul the item to
     * @param jobTmp the job that this state belong to
     */
    public HaulItemState(final ItemType itemTypeTmp, final MapIndex positionTmp, final AbstractJob jobTmp) {
        super(jobTmp);
        itemType = itemTypeTmp;
        position = positionTmp;
    }

    @Override
    public void transitionInto() {
        if (dwarf == null) {
            if (item == null) {
                haulJob = new HaulJob(itemType, null, position, getJob().getPlayer());
            } else {
                haulJob = new HaulJob(item, null, position, getJob().getPlayer());
            }
        } else {
            haulJob = new HaulJob(dwarf, item, null, position);
        }
        haulJob.addListener(this);
    }

    @Override
    public void transitionOutOf() {
    }

    @Override
    public void jobDone(final IJob job) {
        assert haulJob == job;

        if (job.isDone()) {
            job.removeListener(this);
            getJob().stateDone(this);
        }
    }

    /**
     * Get the item that has been hauled.
     * @return the item that was hauled
     */
    public Item getItem() {
        return item;
    }
}
