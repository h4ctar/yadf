package simulation.job.jobstate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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

    /** The resource names. */
    public final Map<ItemType, Integer> resources;

    /** The position to haul the resources to. */
    private final MapIndex position;

    /**
     * Constructor.
     * @param resourcesTmp all the resources that need to be hauled
     * @param positionTmp the position to haul the resources to
     * @param jobTmp the job that this state belongs to
     */
    public HaulResourcesState(final Map<ItemType, Integer> resourcesTmp, final MapIndex positionTmp,
            final AbstractJob jobTmp) {
        super(jobTmp);
        resources = resourcesTmp;
        position = positionTmp;
    }

    @Override
    public String toString() {
        return "Haul resources";
    }

    @Override
    public void transitionInto() {
        for (Entry<ItemType, Integer> entry : resources.entrySet()) {
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
    public void transitionOutOf() {
        for (IJob job : haulJobs) {
            job.removeListener(this);
        }
    }

    @Override
    public void jobDone(final IJob job) {
        assert haulJobs.contains(job);

        if (job.isDone()) {
            job.removeListener(this);
            haulJobs.remove(job);
        }
        if (haulJobs.isEmpty()) {
            getJob().stateDone(this);
        }
    }
}
