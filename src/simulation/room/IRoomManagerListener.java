package simulation.room;

/**
 * Interface for a room manager listener who will be notified whenever a room is added or removed from the room manager.
 */
public interface IRoomManagerListener {

    /**
     * A room has been added.
     * @param room the new room
     */
    void roomAdded(Room room);

    /**
     * A room has been removed.
     * @param room the removed room
     */
    void roomRemoved(Room room);
}
