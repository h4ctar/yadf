package simulation.room;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import simulation.IGameObject;
import simulation.IGameObjectListener;
import simulation.map.MapIndex;

/**
 * The room manager.
 */
public class RoomManager implements IRoomManager, IGameObjectListener {

    /** The rooms. */
    private final Set<Room> rooms = new CopyOnWriteArraySet<>();

    /** The room manager listeners. */
    private Set<IRoomManagerListener> listeners = new LinkedHashSet<>();

    @Override
    public void addRoom(final Room room) {
        assert !rooms.contains(room);
        rooms.add(room);
        for (IRoomManagerListener listener : listeners) {
            listener.roomAdded(room);
        }
        room.addGameObjectListener(this);
    }

    @Override
    public void removeRoom(final Room room) {
        assert rooms.contains(room);
        rooms.remove(room);
        for (IRoomManagerListener listener : listeners) {
            listener.roomRemoved(room);
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
    public void addListener(final IRoomManagerListener listener) {
        assert !listeners.contains(listener);
        listeners.add(listener);
    }

    @Override
    public void removeListener(final IRoomManagerListener listener) {
        assert listeners.contains(listener);
        listeners.remove(listener);
    }

    @Override
    public void gameObjectDeleted(IGameObject gameObject) {
        assert rooms.contains(gameObject);
        removeRoom((Room) gameObject);
    }
}
