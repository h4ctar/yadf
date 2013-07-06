package simulation.job.jobstate;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import simulation.item.Item;
import simulation.item.ItemType;
import simulation.job.AbstractJob;
import simulation.job.HaulJob;
import simulation.job.IJob;
import simulation.job.IJobListener;
import simulation.map.MapIndex;

/**
 * Generic state to haul resources.
 * 
 * Just launches haul jobs and waits till the're all done.
 */
public abstract class HaulResourcesState extends AbstractJobState implements IJobListener {

    /** References to the haul jobs that are scheduled for materials. */
    private final List<HaulJob> haulJobs = new ArrayList<>();

    /** The resource types and quantities required. */
    private final Map<ItemType, Integer> resourceTypes;

    /** All the resources that need to be hauled. */
    private final Set<Item> resources = new LinkedHashSet<>();

    /** The position to haul the resources to. */
    private final MapIndex position;

    /**
     * Constructor.
     * @param resourceTypesTmp all the resources that need to be hauled
     * @param positionTmp the position to haul the resources to
     * @param jobTmp the job that this state belongs to
     */
    public HaulResourcesState(final Map<ItemType, Integer> resourceTypesTmp, final MapIndex positionTmp,
            final AbstractJob jobTmp) {
        super(jobTmp);
        resourceTypes = resourceTypesTmp;
        position = positionTmp;
    }

    @Override
    public String toString() {
        return "Haul resources";
    }

    /**
     * Get the resources that have been hauled so far.
     * @return the resources that have been hauled so far
     */
    public Set<Item> getResources() {
        return resources;
    }

    @Override
    public void start() {
        for (Entry<ItemType, Integer> entry : resourceTypes.entrySet()) {
            ItemType itemType = entry.getKey();
            for (int i = 0; i < entry.getValue().intValue(); i++) {
                HaulJob haulJob = new HaulJob(itemType, getJob().getPlayer().getStockManager(), position, getJob()
                        .getPlayer());
                haulJob.addListener(this);
                haulJobs.add(haulJob);
            }
        }
    }

    @Override
    public void jobDone(final IJob job) {
        assert haulJobs.contains(job);
        if (job.isDone()) {
            job.removeListener(this);
            resources.add(((HaulJob) job).getItem());
            haulJobs.remove(job);
        }
        if (haulJobs.isEmpty()) {
            finishState();
        }
    }

    @Override
    public void jobChanged(final IJob job) {
        // do nothing
    }

    @Override
    public void interrupt(final String message) {
        for (IJob job : haulJobs) {
            job.removeListener(this);
            job.interrupt(message);
        }
    }
}
