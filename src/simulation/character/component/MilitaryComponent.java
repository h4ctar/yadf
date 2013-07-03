package simulation.character.component;

import simulation.IRegion;
import simulation.character.IGameCharacter;

/**
 * The military component.
 * <p>
 * This component should spawn training jobs, keep the soldier in formation, initiate attacks etc...
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
