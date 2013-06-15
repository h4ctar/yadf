package simulation.character.component;

import simulation.Region;
import simulation.character.Dwarf;
import simulation.character.GameCharacter;
import simulation.job.IJobManager;
import simulation.job.SleepJob;

/**
 * Component that makes the dwarf sleep when they get tired.
 */
public class SleepComponent extends AbstractCharacterComponent implements ISleepComponent {

    /** How many simulation steps before the dwarf will want to sleep. */
    private static final long SLEEP_THRESHOLD = Region.SIMULATION_STEPS_PER_DAY;

    /** How tired the dwarf is. */
    private int sleepiness;

    /** The sleep job for the dwarf. */
    private SleepJob sleepJob;

    @Override
    public void update(final GameCharacter character, final Region region) {
        IJobManager jobManager = character.getPlayer().getJobManager();

        sleepiness++;
        if (sleepiness > SLEEP_THRESHOLD && sleepJob == null) {
            sleepJob = new SleepJob((Dwarf) character);
            jobManager.addJob(sleepJob);
        }

        if (sleepJob != null && sleepJob.isDone()) {
            sleepJob = null;
        }
    }

    @Override
    public void kill() {
    }

    @Override
    public void sleep() {
        sleepiness = 0;
    }
}
