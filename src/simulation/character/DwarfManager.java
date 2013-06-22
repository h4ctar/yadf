package simulation.character;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import misc.NameGenerator;
import simulation.IPlayer;
import simulation.Region;
import simulation.character.component.ISkillComponent;
import simulation.labor.LaborType;
import simulation.map.MapIndex;

/**
 * The dwarf manager.
 */
public class DwarfManager implements IDwarfManager, ICharacterAvailableListener {

    /** The dwarfs. */
    private final Set<Dwarf> dwarfs = new CopyOnWriteArraySet<>();

    /** The name generator. */
    private NameGenerator nameGenerator;

    /** The player that this manager belongs to. */
    private final IPlayer player;

    /** The dwarf manager listeners, notified of add and remove of dwarfs. */
    private final Set<IDwarfManagerListener> managerListeners = new CopyOnWriteArraySet<>();

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

    /**
     * Adds a new dwarf.
     * @param position the position of the new dwarf
     */
    public void addNewDwarf(final MapIndex position) {
        Dwarf dwarf = new Dwarf(nameGenerator.compose(2), position, player);
        dwarf.addListener(this);
        dwarfs.add(dwarf);
        notifyDwarfAdded(dwarf);
    }

    /**
     * Gets a dwarf with specific ID.
     * @param id the ID of the dwarf
     * @return the dwarf
     */
    @Override
    public Dwarf getDwarf(final int id) {
        for (Dwarf dwarf : dwarfs) {
            if (dwarf.getId() == id) {
                return dwarf;
            }
        }
        return null;
    }

    /**
     * Gets the dwarf at a specific position.
     * @param position the position
     * @return the dwarf
     */
    @Override
    public Dwarf getDwarf(final MapIndex position) {
        for (Dwarf dwarf : dwarfs) {
            if (dwarf.getPosition().equals(position)) {
                return dwarf;
            }
        }
        return null;
    }

    /**
     * Gets all the dwarfs.
     * @return the dwarfs
     */
    @Override
    public Set<Dwarf> getDwarfs() {
        return dwarfs;
    }

    /**
     * Gets an idle dwarf.
     * @param requiredLabor the required labor
     * @return the idle dwarf
     */
    @Override
    public Dwarf getIdleDwarf(final LaborType requiredLabor) {
        for (Dwarf dwarf : dwarfs) {
            if (dwarf.isDead()) {
                continue;
            }
            if (dwarf.getComponent(ISkillComponent.class).canDoJob(requiredLabor) && dwarf.acquireLock()) {
                return dwarf;
            }
        }
        return null;
    }

    /**
     * Update all the dwarfs.
     * @param region the region
     */
    public void update(final Region region) {
        for (Dwarf dwarf : dwarfs) {
            dwarf.update(region);
        }
        // TODO: change this to something else
        for (Dwarf dwarf : dwarfs.toArray(new Dwarf[0])) {
            if (dwarf.getRemove()) {
                dwarfs.remove(dwarf);
                notifyDwarfRemoved(dwarf);
            }
        }
    }

    @Override
    public void addListener(final IDwarfManagerListener listener) {
        assert !managerListeners.contains(listener);
        managerListeners.add(listener);
    }

    @Override
    public void removeListener(final IDwarfManagerListener listener) {
        assert managerListeners.contains(listener);
        managerListeners.remove(listener);
    }

    /**
     * Notify all the listeners that a dwarf has been added.
     * @param dwarf the dwarf that was added
     */
    private void notifyDwarfAdded(final Dwarf dwarf) {
        for (IDwarfManagerListener listener : managerListeners) {
            listener.dwarfAdded(dwarf);
        }
    }

    /**
     * Notify all the listeners that a dwarf has been removed.
     * @param dwarf the dwarf that was removed
     */
    private void notifyDwarfRemoved(final Dwarf dwarf) {
        for (IDwarfManagerListener listener : managerListeners) {
            listener.dwarfRemoved(dwarf);
        }
    }

    /**
     * Notify all the listeners that a dwarf is not idle.
     * @param dwarf the dwarf that is now idle
     */
    private void notifyDwarfNowIdle(final IGameCharacter dwarf) {
        for (ICharacterAvailableListener listener : availableListeners) {
            listener.characterAvailable(dwarf);
            if (dwarf.isLocked()) {
                break;
            }
        }
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
}