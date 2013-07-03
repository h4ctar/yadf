package simulation.military;

import java.util.LinkedHashSet;
import java.util.Set;

import simulation.character.IGameCharacter;
import simulation.character.component.IMilitaryComponent;
import simulation.character.component.MilitaryComponent;
import simulation.job.MilitaryStationJob;
import simulation.map.MapIndex;

/**
 * The military manager.
 */
public class MilitaryManager implements IMilitaryManager {

    /** The dwarves that are enlisted. */
    private Set<IGameCharacter> soldiers = new LinkedHashSet<>();

    /** The listeners. */
    private Set<IMilitaryManagerListener> listeners = new LinkedHashSet<>();

    /**
     * Constructor.
     */
    public MilitaryManager() {

    }

    @Override
    public void enlistDwarf(final IGameCharacter dwarf) {
        assert !soldiers.contains(dwarf);
        soldiers.add(dwarf);
        dwarf.setComponent(IMilitaryComponent.class, new MilitaryComponent(dwarf));
        for (IMilitaryManagerListener listener : listeners) {
            listener.soldierAdded();
        }
    }

    @Override
    public Set<IGameCharacter> getSoldiers() {
        return soldiers;
    }

    @Override
    public void militaryStation(final MapIndex target) {
        // TODO: remember all the station jobs so they can be interrupted or something
        for (IGameCharacter soldier : soldiers) {
            soldier.getPlayer().getJobManager().addJob(new MilitaryStationJob(target, soldier));
        }
    }

    @Override
    public void addMilitaryManagerListener(final IMilitaryManagerListener listener) {
        assert !listeners.contains(listener);
        listeners.add(listener);
    }

    @Override
    public void removeMilitaryManagerListener(final IMilitaryManagerListener listener) {
        assert listeners.contains(listener);
        listeners.remove(listener);
    }
}
