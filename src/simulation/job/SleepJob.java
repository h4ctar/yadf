package simulation.job;

import java.util.Set;

import simulation.Player;
import simulation.Region;
import simulation.character.Dwarf;
import simulation.character.IMovementComponent;
import simulation.character.ISleepComponent;
import simulation.character.component.WalkMovementComponent;
import simulation.item.Item;
import simulation.room.Room;

/**
 * The dwarf looks for somewhere to sleep, walks there, then sleeps.
 */
public class SleepJob extends AbstractJob {

    /** The serial version UID. */
    private static final long serialVersionUID = 6230915816367520292L;

    private static final long SLEEP_DURATION = Region.SIMULATION_STEPS_PER_HOUR;

    /**
     * All the possible states that the job can be in.
     */
    enum State {
        /** Waiting for the dwarf to become free. */
        WAIT_FOR_DWARF,
        /** The dwarf is looking for somewhere to sleep. */
        LOOK_FOR_SLEEP_LOCATION,
        /** The dwarf is walking to sleep place. */
        WALK_TO_SLEEP_LOCATION,
        /** The consume. */
        SLEEP
    }

    /** The current state of the job. */
    private State state = State.WAIT_FOR_DWARF;

    /** The dwarf that wants to sleep. */
    private final Dwarf dwarf;

    private Item bed;

    private WalkMovementComponent walkComponent;

    private WasteTimeJob wasteTimeJob;

    private boolean done;

    public SleepJob(final Dwarf dwarfTmp) {
        dwarf = dwarfTmp;
    }

    @Override
    public String getStatus() {
        switch (state) {
        case WAIT_FOR_DWARF:
            return "Waiting for the dwarf to become free";
        case LOOK_FOR_SLEEP_LOCATION:
            return "Looking for somewhere to sleep";
        case WALK_TO_SLEEP_LOCATION:
            return "Walking to sleep place";
        case SLEEP:
            return "Sleeping";
        default:
            return null;
        }
    }

    @Override
    public void interrupt(final String message) {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isDone() {
        return done;
    }

    @Override
    public void update(final Player player, final Region region) {
        if (isDone()) {
            return;
        }

        switch (state) {
        case WAIT_FOR_DWARF:
            if (dwarf.acquireLock()) {
                state = State.LOOK_FOR_SLEEP_LOCATION;
            }
            break;

        case LOOK_FOR_SLEEP_LOCATION:
            Set<Room> rooms = player.getRooms();
            for (Room room : rooms) {
                // TODO: do I have my own room?
                if (room.getType().equals("Dormitory")) {
                    bed = room.getUnusedItem("Bed");
                    if (bed != null) {
                        bed.setUsed(true);
                        break;
                    }
                }
            }
            if (bed != null) {
                walkComponent = new WalkMovementComponent(bed.getPosition(), false);
                dwarf.setComponent(IMovementComponent.class, walkComponent);
                state = State.WALK_TO_SLEEP_LOCATION;
            } else {
                wasteTimeJob = new WasteTimeJob(dwarf, SLEEP_DURATION);
                state = State.SLEEP;
            }
            break;

        case WALK_TO_SLEEP_LOCATION:
            if (walkComponent.isNoPath()) {
                interrupt("No path to bed");
                return;
            }
            if (walkComponent.isArrived()) {
                wasteTimeJob = new WasteTimeJob(dwarf, SLEEP_DURATION);
                state = State.SLEEP;
            }
            break;

        case SLEEP:
            wasteTimeJob.update(player, region);
            if (wasteTimeJob.isDone()) {
                dwarf.releaseLock();
                dwarf.getComponent(ISleepComponent.class).sleep();
                if (bed != null) {
                    bed.setUsed(false);
                }
                done = true;
            }
            break;

        default:
            break;
        }
    }
}
