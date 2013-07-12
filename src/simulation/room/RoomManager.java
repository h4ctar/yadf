package simulation.room;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import simulation.IGameObjectListener;
import simulation.IGameObjectManagerListener;
import simulation.map.MapIndex;

/**
 * The room manager.
 */
public class RoomManager implements IRoomManager, IGameObjectListener {

    /** The rooms. */
    private final Set<Room> rooms = new CopyOnWriteArraySet<>();

    /** The room manager listeners. */
    private Set<IGameObjectManagerListener> listeners = new LinkedHashSet<>();

    @Override
    public void addRoom(final Room room) {
        assert !rooms.contains(room);
        rooms.add(room);
        for (IGameObjectManagerListener listener : listeners) {
            listener.gameObjectAdded(room);
        }
        room.addGameObjectListener(this);
    }

    @Override
    public void removeRoom(final Room room) {
        assert rooms.contains(room);
        rooms.remove(room);
        for (IGameObjectManagerListener listener : listeners) {
            listener.gameObjectRemoved(room);
        }
        room.removeGameObjectListener(this);
    }

    @Override
    public Room getRoom(final int roomId) {
        for (Room room : rooms) {
            if (room.getId() == roomId) {
                return room;
            }
        }
        return null;
    }

    @Override
    public Room getRoom(final MapIndex index) {
        Room foundRoom = null;
        for (Room room : rooms) {
            if (room.getArea().containesIndex(index)) {
                foundRoom = room;
                break;
            }
        }
        return foundRoom;
    }

    @Override
    public Set<Room> getRooms() {
        return rooms;
    }

    @Override
    public void addGameObjectManagerListener(final IGameObjectManagerListener listener) {
        assert !listeners.contains(listener);
        listeners.add(listener);
    }

    @Override
    public void removeGameObjectManagerListener(final IGameObjectManagerListener listener) {
        assert listeners.contains(listener);
        listeners.remove(listener);
    }

    @Override
    public void gameObjectDeleted(final Object gameObject) {
        assert rooms.contains(gameObject);
        removeRoom((Room) gameObject);
    }

    @Override
    public void update() {
        // nothing to do
    }
}
