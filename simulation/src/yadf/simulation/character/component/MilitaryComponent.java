package yadf.simulation.character.component;

import yadf.simulation.IRegion;
import yadf.simulation.character.IGameCharacter;

/**
 * The military component.
 * <p>
 * This component should spawn training jobs.
 */
public class MilitaryComponent extends AbstractCharacterComponent implements IMilitaryComponent {

    /**
     * Constructor.
     * @param characterTmp the character
     */
    public MilitaryComponent(final IGameCharacter characterTmp) {
        super(characterTmp);
    }

    @Override
    public void update(final IRegion region) {
    }

    @Override
    public void kill() {
    }
}
