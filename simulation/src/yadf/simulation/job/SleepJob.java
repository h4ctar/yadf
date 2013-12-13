package yadf.simulation.job;

import java.util.List;

import yadf.simulation.IRegion;
import yadf.simulation.character.IGameCharacter;
import yadf.simulation.character.component.ISleepComponent;
import yadf.simulation.item.Item;
import yadf.simulation.job.jobstate.IJobState;
import yadf.simulation.job.jobstate.WalkToPositionState;
import yadf.simulation.job.jobstate.WasteTimeState;
import yadf.simulation.map.MapIndex;
import yadf.simulation.room.IRoomManager;
import yadf.simulation.room.Room;

/**
 * The dwarf looks for somewhere to sleep, walks there, then sleeps.
 */
public class SleepJob extends AbstractJob {

    /** How many simulation steps the dwarf should sleep for. */
    private static final long SLEEP_DURATION = IRegion.SIMULATION_STEPS_PER_HOUR;

    /** The dwarf that wants to sleep. */
    private final IGameCharacter dwarf;

    /** The bed that the dwarf sleeps in, null if he can't find one. */
    private Item bed;

    /**
     * Constructor.
     * @param dwarfTmp the dwarf that want's to sleep
     */
    public SleepJob(final IGameCharacter dwarfTmp) {
        super(dwarfTmp.getPlayer());
        dwarf = dwarfTmp;
        setJobState(new WaitingForDwarfState());
    }

    @Override
    public String toString() {
        return "Sleep";
    }

    @Override
    public MapIndex getPosition() {
        return dwarf.getPosition();
    }

    @Override
    public void interrupt(final String message) {
        super.interrupt(message);
        if (dwarf != null) {
            dwarf.setAvailable(true);
        }
        if (bed != null) {
            bed.setAvailable(true);
        }
    }

    /**
     * The waiting for dwarf job state.
     */
    private class WaitingForDwarfState extends yadf.simulation.job.jobstate.WaitingForDwarfState {

        /**
         * Constructor.
         */
        public WaitingForDwarfState() {
            super(dwarf, SleepJob.this);
        }

        @Override
        protected void doFinalActions() {
            List<Room> rooms = getPlayer().getComponent(IRoomManager.class).getRooms();
            for (Room room : rooms) {
                // TODO: do I have my own room?
                if (room.getType().equals("Dormitory")) {
                    bed = room.getItem("Bed", false, true);
                    if (bed != null) {
                        bed.setAvailable(false);
                        break;
                    }
                }
            }
        }

        @Override
        public IJobState getNextState() {
            IJobState nextState;
            if (bed != null) {
                nextState = new WalkToBedState();
            } else {
                nextState = new SleepState();
            }
            return nextState;
        }
    }

    /**
     * The walk to bed job state.
     */
    private class WalkToBedState extends WalkToPositionState {

        /**
         * Constructor.
         */
        public WalkToBedState() {
            super(bed.getPosition(), dwarf, false, SleepJob.this);
        }

        @Override
        public IJobState getNextState() {
            return new SleepState();
        }
    }

    /**
     * The sleep job state.
     */
    private class SleepState extends WasteTimeState {

        /**
         * Constructor.
         */
        public SleepState() {
            super(SLEEP_DURATION, dwarf, SleepJob.this);
        }

        @Override
        protected void doFinalActions() {
            dwarf.getComponent(ISleepComponent.class).sleep();
            dwarf.setAvailable(true);
            if (bed != null) {
                bed.setAvailable(true);
            }
        }

        @Override
        public IJobState getNextState() {
            return null;
        }
    }
}
