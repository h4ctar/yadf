package simulation.room;

import java.util.Set;

import simulation.map.MapIndex;

/**
 * Interface for a room manager.
 */
public interface IRoomManager {

    /**
     * Adds a room.
     * @param room the room
     */
    void addRoom(Room room);

    /**
     * Remove a room.
     * @param room the room to remove
     */
    void removeRoom(Room room);

    /**
     * Gets the room with specific ID.
     * @param roomId the room id
     * @return the room
     */
    Room getRoom(int roomId);

    /**
     * Gets the room at specific location.
     * @param index the index
     * @return the room
     */
    Room getRoom(MapIndex index);

    /**
     * Get all the rooms.
     * @return all the rooms
     */
    Set<Room> getRooms();

    /**
     * Add a new room manager listener who will be notified of rooms being added or removed.
     * @param listener the new listener
     */
    void addListener(IRoomManagerListener listener);

    /**
     * Remove a room manager listener.
     * @param listener the removed room manager listener
     */
    void removeListener(IRoomManagerListener listener);
}
