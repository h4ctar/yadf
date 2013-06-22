package simulation.job.jobstate;

import simulation.character.IGameCharacter;
import simulation.character.component.ICharacterComponent;
import simulation.character.component.ICharacterComponentListener;
import simulation.character.component.IMovementComponent;
import simulation.character.component.WalkMovementComponent;
import simulation.job.AbstractJob;
import simulation.map.MapIndex;

/**
 * A generic walk to location state.
 */
public abstract class WalkToPositionState extends AbstractJobState implements ICharacterComponentListener {

    /** The character that is walking to the position. */
    private final IGameCharacter character;

    /** The position to walk to. */
    private final MapIndex position;

    /** The walk component. */
    private WalkMovementComponent walkComponent;

    /** Walk to a map node that is one off the target node. */
    private final boolean oneOff;

    /**
     * Constructor.
     * @param positionTmp the position to walk to.
     * @param characterTmp the character that is walking to the position
     * @param oneOffTmp Walk to a map node that is one off the target node
     * @param jobTmp the job that this state belongs to
     */
    public WalkToPositionState(final MapIndex positionTmp, final IGameCharacter characterTmp,
            final boolean oneOffTmp, final AbstractJob jobTmp) {
        super(jobTmp);
        position = positionTmp;
        character = characterTmp;
        oneOff = oneOffTmp;
    }

    @Override
    public String toString() {
        return "Walking to position";
    }

    @Override
    public void transitionInto() {
        walkComponent = new WalkMovementComponent(character, position, oneOff);
        walkComponent.addListener(this);
        character.setComponent(IMovementComponent.class, walkComponent);
    }

    @Override
    public void transitionOutOf() {
        walkComponent.removeListener(this);
    }

    @Override
    public void componentChanged(final ICharacterComponent component) {
        assert component == walkComponent;
        if (walkComponent.isArrived()) {
            getJob().stateDone(this);
        } else if (walkComponent.isNoPath()) {
            getJob().stateInterrupted(this, "No path to position");
        }
    }
}
