package yadf.simulation.job.jobstate;

import yadf.simulation.ITimeListener;
import yadf.simulation.character.IGameCharacter;
import yadf.simulation.character.component.IMovementComponent;
import yadf.simulation.character.component.StillMovementComponent;
import yadf.simulation.job.AbstractJob;

/**
 * A generic waste time job state.
 */
public abstract class WasteTimeState extends AbstractJobState implements ITimeListener {

    /** How long to waste. */
    private final long duration;

    /** The dwarf. */
    private final IGameCharacter dwarf;

    /** The time that the time should be up. */
    private long notifyTime;

    /**
     * Constructor.
     * @param durationTmp how many simulation steps to wait
     * @param dwarfTmp the dwarf
     * @param jobTmp the job that this state belongs to
     */
    public WasteTimeState(final long durationTmp, final IGameCharacter dwarfTmp, final AbstractJob jobTmp) {
        super(jobTmp);
        dwarf = dwarfTmp;
        duration = durationTmp;
    }

    @Override
    public String toString() {
        return "Wasting time";
    }

    @Override
    public void start() {
        notifyTime = dwarf.getRegion().addTimeListener(duration, this);
        dwarf.setComponent(IMovementComponent.class, new StillMovementComponent(dwarf));
    }

    @Override
    public void notifyTimeEvent() {
        finishState();
    }

    @Override
    public void interrupt(final String message) {
        dwarf.getRegion().removeTimeListener(notifyTime, this);
    }
}
