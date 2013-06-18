package simulation.job.jobstate;

import simulation.character.GameCharacter;
import simulation.character.ICharacterListener;
import simulation.job.AbstractJob;

/**
 * A generic waiting for dwarf job state.
 * 
 * Waits until a dwarf becomes free and acquires a lock on them.
 */
public abstract class WaitingForDwarfState extends AbstractJobState implements ICharacterListener {

    /** The dwarf we're waiting for. */
    private final GameCharacter dwarf;

    /**
     * Constructor.
     * @param dwarfTmp the dwarf we're waiting for
     * @param jobTmp the job that this state belongs to
     */
    public WaitingForDwarfState(final GameCharacter dwarfTmp, final AbstractJob jobTmp) {
        super(jobTmp);
        dwarf = dwarfTmp;
        dwarf.addListener(this);
    }

    @Override
    public String toString() {
        return "Waiting for dwarf";
    }

    @Override
    public void transitionInto() {
    }

    @Override
    public void transitionOutOf() {
        dwarf.removeListener(this);
    }

    @Override
    public void characterChanged(final GameCharacter character) {
        assert character == dwarf;
        if (dwarf.acquireLock()) {
            getJob().stateDone(this);
        }
    }
}
