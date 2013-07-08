package simulation.job.jobstate;

import simulation.character.ICharacterAvailableListener;
import simulation.character.IGameCharacter;
import simulation.job.AbstractJob;

/**
 * A generic waiting for dwarf job state.
 * 
 * Waits until a dwarf becomes free and acquires a lock on them.
 */
public abstract class WaitingForDwarfState extends AbstractJobState implements ICharacterAvailableListener {

    /** The dwarf we're waiting for. */
    private final IGameCharacter dwarf;

    /**
     * Constructor.
     * @param character the dwarf we're waiting for
     * @param jobTmp the job that this state belongs to
     */
    public WaitingForDwarfState(final IGameCharacter character, final AbstractJob jobTmp) {
        super(jobTmp);
        dwarf = character;
    }

    @Override
    public String toString() {
        return "Waiting for dwarf";
    }

    @Override
    public void start() {
        if (dwarf.isFree()) {
            dwarf.setJob(getJob());
            finishState();
        } else {
            dwarf.addListener(this);
        }
    }

    @Override
    public void characterAvailable(final IGameCharacter character) {
        assert character == dwarf;
        assert dwarf.isFree();
        dwarf.setJob(getJob());
        dwarf.removeListener(this);
        finishState();
    }

    @Override
    public void interrupt(final String message) {
        dwarf.removeListener(this);
    }
}
