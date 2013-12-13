package yadf.simulation.job.jobstate;

import yadf.simulation.IGameObject;
import yadf.simulation.IGameObjectListener;
import yadf.simulation.character.IGameCharacter;
import yadf.simulation.job.AbstractJob;

/**
 * A generic waiting for dwarf job state.
 * 
 * Waits until a dwarf becomes free and acquires a lock on them.
 */
public abstract class WaitingForDwarfState extends AbstractJobState implements IGameObjectListener {

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
        if (dwarf.isAvailable()) {
            dwarf.setJob(getJob());
            finishState();
        } else {
            dwarf.addGameObjectListener(this);
        }
    }

    @Override
    public void interrupt(final String message) {
        dwarf.removeGameObjectListener(this);
    }

    @Override
    public void gameObjectDeleted(final IGameObject gameObject) {
        interrupt("Dwarf died");
    }

    /**
     * The game object has changed.
     * @param gameObject the game object that changed
     */
    public void gameObjectChanged(final IGameObject gameObject) {
        assert gameObject == dwarf;
        if (dwarf.isAvailable()) {
            dwarf.setJob(getJob());
            dwarf.removeGameObjectListener(this);
            finishState();
        }
    }
}
