package simulation.job.jobstate;

import simulation.IDwarfManagerListener;
import simulation.character.Dwarf;
import simulation.job.AbstractJob;
import simulation.labor.LaborType;

/**
 * Generic state to look for a dwarf.
 */
public abstract class LookingForDwarfState extends AbstractJobState implements IDwarfManagerListener {

    /** The found dwarf. */
    private Dwarf dwarf;

    /** The required labor type. */
    private final LaborType requiredLabor;

    /**
     * Constructor.
     * @param requiredLaborTmp the required labor
     * @param jobTmp the job that this state belongs to
     */
    public LookingForDwarfState(final LaborType requiredLaborTmp, final AbstractJob jobTmp) {
        super(jobTmp);
        requiredLabor = requiredLaborTmp;
    }

    @Override
    public String toString() {
        return "Looking for dwarf";
    }

    @Override
    public void transitionInto() {
        dwarf = getJob().getPlayer().getDwarfManager().getIdleDwarf(requiredLabor);
        if (dwarf == null) {
            getJob().getPlayer().getDwarfManager().addListener(this);
        } else {
            getJob().stateDone(this);
        }
    }

    @Override
    public void transitionOutOf() {
    }

    @Override
    public void dwarfAdded(final Dwarf newDwarf) {
        acquireDwarf(newDwarf);
    }

    @Override
    public void dwarfRemoved(final Dwarf removedDwarf) {
        // do nothing
    }

    @Override
    public void dwarfNowIdle(final Dwarf idleDwarf) {
        acquireDwarf(idleDwarf);
    }

    /**
     * Attempt to acquire the dwarf, and transition to next state if successful.
     * @param dwarfTmp the dwarf to acquire
     */
    private void acquireDwarf(final Dwarf dwarfTmp) {
        if (dwarfTmp.acquireLock()) {
            dwarf = dwarfTmp;
            getJob().stateDone(this);
            getJob().getPlayer().getDwarfManager().removeListener(this);
        }
    }

    /**
     * Gets the found dwarf.
     * @return the dwarf
     */
    public Dwarf getDwarf() {
        return dwarf;
    }
}
