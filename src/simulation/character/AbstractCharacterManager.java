package simulation.character;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import simulation.IGameObjectManagerListener;
import simulation.character.component.ISkillComponent;
import simulation.labor.LaborType;
import simulation.map.MapIndex;

/**
 * An abstract character manager to provide implementations of the boring stuff.
 */
public abstract class AbstractCharacterManager implements ICharacterManager, ICharacterAvailableListener {

    /** The dwarfs. */
    private final Set<IGameCharacter> characters = new CopyOnWriteArraySet<>();

    /** The dwarf manager listeners, notified of add and remove of dwarfs. */
    private final Set<IGameObjectManagerListener> managerListeners = new CopyOnWriteArraySet<>();

    /** The dwarf manager listeners, notified of add and remove of dwarfs. */
    private final Set<ICharacterAvailableListener> availableListeners = new CopyOnWriteArraySet<>();

    @Override
    public void addGameObjectManagerListener(final IGameObjectManagerListener listener) {
        assert !managerListeners.contains(listener);
        managerListeners.add(listener);
    }

    @Override
    public void removeGameObjectManagerListener(final IGameObjectManagerListener listener) {
        assert managerListeners.contains(listener);
        managerListeners.remove(listener);
    }

    @Override
    public void addListener(final ICharacterAvailableListener listener) {
        assert !availableListeners.contains(listener);
        availableListeners.add(listener);
    }

    @Override
    public void removeListener(final ICharacterAvailableListener listener) {
        assert availableListeners.contains(listener);
        availableListeners.remove(listener);
    }

    /**
     * Notify all the listeners that a dwarf has been added.
     * @param dwarf the dwarf that was added
     */
    protected void notifyDwarfAdded(final IGameCharacter dwarf) {
        for (IGameObjectManagerListener listener : managerListeners) {
            listener.gameObjectAdded(dwarf);
        }
    }

    /**
     * Notify all the listeners that a dwarf is not idle.
     * @param dwarf the dwarf that is now idle
     */
    protected void notifyDwarfNowIdle(final IGameCharacter dwarf) {
        for (ICharacterAvailableListener listener : availableListeners) {
            listener.characterAvailable(dwarf);
            if (!dwarf.isFree()) {
                break;
            }
        }
    }

    @Override
    public void characterAvailable(final IGameCharacter character) {
        notifyDwarfNowIdle(character);
    }

    @Override
    public IGameCharacter getCharacter(final int id) {
        for (IGameCharacter dwarf : characters) {
            if (dwarf.getId() == id) {
                return dwarf;
            }
        }
        return null;
    }

    @Override
    public IGameCharacter getCharacter(final MapIndex position) {
        IGameCharacter foundDwarf = null;
        for (IGameCharacter dwarf : characters) {
            if (dwarf.getPosition().equals(position)) {
                foundDwarf = dwarf;
                break;
            }
        }
        return foundDwarf;
    }

    @Override
    public IGameCharacter getCharacter(final MapIndex position, final int radius) {
        IGameCharacter foundDwarf = null;
        for (IGameCharacter dwarf : characters) {
            if (!dwarf.isDead() && dwarf.getPosition().distance(position) <= radius) {
                foundDwarf = dwarf;
                break;
            }
        }
        return foundDwarf;
    }

    /**
     * Gets all the dwarfs.
     * @return the dwarfs
     */
    @Override
    public Set<IGameCharacter> getCharacters() {
        return characters;
    }

    /**
     * Gets an idle dwarf.
     * @param requiredLabor the required labor
     * @return the idle dwarf
     */
    @Override
    public IGameCharacter getIdleCharacter(final LaborType requiredLabor) {
        for (IGameCharacter dwarf : characters) {
            if (dwarf.isDead()) {
                continue;
            }
            if (dwarf.getComponent(ISkillComponent.class).canDoJob(requiredLabor) && dwarf.isFree()) {
                return dwarf;
            }
        }
        return null;
    }

    @Override
    public void update() {
        for (IGameCharacter dwarf : characters) {
            dwarf.update();
        }
    }
}
