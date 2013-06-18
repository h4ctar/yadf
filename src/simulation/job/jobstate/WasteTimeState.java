package simulation.job.jobstate;

import simulation.character.Dwarf;
import simulation.job.AbstractJob;

/**
 * A generic waste time job state.
 */
public abstract class WasteTimeState extends AbstractJobState {

    /** How long to waste. */
    private final long duration;

    public WasteTimeState(long durationTmp, Dwarf dwarf, AbstractJob jobTmp) {
        super(jobTmp);
        duration = durationTmp;
    }

    @Override
    public String toString() {
        return "Wasting time";
    }

    @Override
    public void transitionInto() {
        getJob().stateDone(this);
    }

    @Override
    public void transitionOutOf() {

    }
}
