package simulation.room;

import java.util.List;

import simulation.IGameObjectManager;
import simulation.IPlayerComponent;
import simulation.map.MapIndex;

/**
 * Interface for a room manager.
 */
public interface IRoomManager extends IGameObjectManager, IPlayerComponent {

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
    List<Room> getRooms();
}
