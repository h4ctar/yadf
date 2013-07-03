package simulation.character.component;

import simulation.IRegion;
import simulation.character.IGameCharacter;
import simulation.map.MapIndex;

/**
 * The guard movement component.
 * <p>
 * The soldier will move around a target position.
 */
public class GuardMovementComponent extends AbstractMoveComponent implements IMovementComponent {

    /**
     * Constructor.
     * @param soldier the soldier
     * @param target the target position to guard
     */
    public GuardMovementComponent(final IGameCharacter soldier, final MapIndex target) {
        super(soldier);
    }

    @Override
    public void update(final IRegion region) {
        // TODO: make the soldier walk idle but stay neer the target
    }

    @Override
    public void kill() {
        // do nothing
    }
}
