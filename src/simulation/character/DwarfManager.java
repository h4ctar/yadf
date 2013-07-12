package simulation.character;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import misc.NameGenerator;
import simulation.IGameObjectManagerListener;
import simulation.IPlayer;
import simulation.IRegion;
import simulation.character.component.ISkillComponent;
import simulation.labor.LaborType;
import simulation.map.MapIndex;

/**
 * The dwarf manager. There should be one dwarf manager per player per region.
 */
public class DwarfManager implements ICharacterManager, ICharacterAvailableListener {

    /** The dwarfs. */
    private final Set<IGameCharacter> dwarfs = new CopyOnWriteArraySet<>();

    /** The name generator. */
    private NameGenerator nameGenerator;

    /** The player that this manager belongs to. */
    private final IPlayer player;

    /** The dwarf manager listeners, notified of add and remove of dwarfs. */
    private final Set<IGameObjectManagerListener> managerListeners = new CopyOnWriteArraySet<>();

    /** The dwarf manager listeners, notified of add and remove of dwarfs. */
    private final Set<ICharacterAvailableListener> availableListeners = new CopyOnWriteArraySet<>();

    /**
     * Constructor.
     * @param playerTmp the player that this manager belongs to.
     */
    public DwarfManager(final IPlayer playerTmp) {
        player = playerTmp;
        try {
            nameGenerator = new NameGenerator("elven.txt");
        } catch (Exception e) {
            e.printStackTrace();
            nameGenerator = null;
        }
    }

    @Override
    public void addNewDwarf(final MapIndex position, final IRegion region) {
        Dwarf dwarf = new Dwarf(nameGenerator.compose(2), position, region, player);
        dwarf.addListener(this);
        dwarfs.add(dwarf);
        notifyDwarfAdded(dwarf);
    }

    @Override
    public IGameCharacter getDwarf(final int id) {
        for (IGameCharacter dwarf : dwarfs) {
            if (dwarf.getId() == id) {
                return dwarf;
            }
        }
        return null;
    }

    @Override
    public IGameCharacter getDwarf(final MapIndex position) {
        IGameCharacter foundDwarf = null;
        for (IGameCharacter dwarf : dwarfs) {
            if (dwarf.getPosition().equals(position)) {
                foundDwarf = dwarf;
                break;
            }
        }
        return foundDwarf;
    }

    @Override
    public IGameCharacter getDwarf(final MapIndex position, final int radius) {
        IGameCharacter foundDwarf = null;
        for (IGameCharacter dwarf : dwarfs) {
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
    public Set<IGameCharacter> getDwarfs() {
        return dwarfs;
    }

    /**
     * Gets an idle dwarf.
     * @param requiredLabor the required labor
     * @return the idle dwarf
     */
    @Override
    public IGameCharacter getIdleDwarf(final LaborType requiredLabor) {
        for (IGameCharacter dwarf : dwarfs) {
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
        for (IGameCharacter dwarf : dwarfs) {
            dwarf.update();
        }
    }

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
    public void characterAvailable(final IGameCharacter character) {
        notifyDwarfNowIdle(character);
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
    private void notifyDwarfAdded(final Dwarf dwarf) {
        for (IGameObjectManagerListener listener : managerListeners) {
            listener.gameObjectAdded(dwarf);
        }
    }

    /**
     * Notify all the listeners that a dwarf is not idle.
     * @param dwarf the dwarf that is now idle
     */
    private void notifyDwarfNowIdle(final IGameCharacter dwarf) {
        for (ICharacterAvailableListener listener : availableListeners) {
            listener.characterAvailable(dwarf);
            if (!dwarf.isFree()) {
                break;
            }
        }
    }
}
