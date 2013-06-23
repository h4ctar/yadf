package simulation.job.jobstate;

import simulation.ITimeListener;
import simulation.character.Dwarf;
import simulation.character.component.IMovementComponent;
import simulation.character.component.StillMovementComponent;
import simulation.job.AbstractJob;

/**
 * A generic waste time job state.
 */
public abstract class WasteTimeState extends AbstractJobState implements ITimeListener {

    /** How long to waste. */
    private final long duration;

    /** The dwarf. */
    private final Dwarf dwarf;

    private long notifyTime;

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
        notifyTime = dwarf.getPlayer().getRegion().addTimeListener(duration, this);
        dwarf.setComponent(IMovementComponent.class, new StillMovementComponent(dwarf));
    }

    @Override
    public void transitionOutOf() {
    }

    @Override
    public void notifyTimeEvent() {
        getJob().stateDone(this);
    }

    @Override
    public void interrupt(final String message) {
        dwarf.getPlayer().getRegion().removeTimeListener(notifyTime, this);
    }
}
