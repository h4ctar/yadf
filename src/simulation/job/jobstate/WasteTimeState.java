package simulation.job.jobstate;

import simulation.ITimeListener;
import simulation.character.Dwarf;
import simulation.job.AbstractJob;

/**
 * A generic waste time job state.
 */
public abstract class WasteTimeState extends AbstractJobState implements ITimeListener {

    /** How long to waste. */
    private final long duration;

    /** The dwarf. */
    private final Dwarf dwarf;

    /**
     * Constructor.
     * @param durationTmp how many simulation steps to wait
     * @param dwarfTmp the dwarf
     * @param jobTmp the job that this state belongs to
     */
    public WasteTimeState(final long durationTmp, final Dwarf dwarfTmp, final AbstractJob jobTmp) {
        super(jobTmp);
        dwarf = dwarfTmp;
        duration = durationTmp;
    }

    @Override
    public String toString() {
        return "Wasting time";
    }

    @Override
    public void transitionInto() {
        dwarf.getPlayer().getRegion().addTimeListener(duration, this);
    }

    @Override
    public void transitionOutOf() {

    }

    @Override
    public void notifyTimeEvent() {
        getJob().stateDone(this);
    }
}
