package yadf.simulation.character;

import yadf.simulation.AbstractGameObjectManager;
import yadf.simulation.character.component.ISkillComponent;
import yadf.simulation.labor.LaborType;
import yadf.simulation.map.MapIndex;

/**
 * An abstract character manager to provide implementations of the boring stuff.
 */
public abstract class AbstractCharacterManager extends AbstractGameObjectManager<IGameCharacter> implements
        ICharacterManager {

    @Override
    public IGameCharacter getCharacter(final MapIndex position, final int radius) {
        IGameCharacter foundDwarf = null;
        for (IGameCharacter dwarf : getGameObjects()) {
            if (!dwarf.isDead() && dwarf.getPosition().distance(position) <= radius) {
                foundDwarf = dwarf;
                break;
            }
        }
        return foundDwarf;
    }

    /**
     * Gets an idle dwarf.
     * @param requiredLabor the required labor
     * @return the idle dwarf
     */
    @Override
    public IGameCharacter getIdleCharacter(final LaborType requiredLabor) {
        for (IGameCharacter dwarf : getGameObjects()) {
            if (dwarf.isDead()) {
                continue;
            }
            if (dwarf.getComponent(ISkillComponent.class).canDoJob(requiredLabor) && dwarf.isAvailable()) {
                return dwarf;
            }
        }
        return null;
    }

    @Override
    public void update() {
        for (IGameCharacter dwarf : getGameObjects()) {
            dwarf.update();
        }
    }
}
