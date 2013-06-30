package simulation.room;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

import simulation.map.MapIndex;

/**
 * The room manager.
 */
public class RoomManager implements IRoomManager {

    /** The rooms. */
    private final Set<Room> rooms = new CopyOnWriteArraySet<>();

    /** The room manager listeners. */
    private Set<IRoomManagerListener> listeners = new LinkedHashSet<>();

    @Override
    public void addRoom(final Room room) {
        rooms.add(room);
        for (IRoomManagerListener listener : listeners) {
            listener.roomAdded(room);
        }
    }

    @Override
    public void removeRoom(final Room room) {
        rooms.remove(room);
        for (IRoomManagerListener listener : listeners) {
            listener.roomRemoved(room);
        }
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
}
