package simulation.character.component;

import simulation.IRegion;
import simulation.character.IGameCharacter;
import simulation.job.IJobManager;
import simulation.job.SleepJob;

/**
 * Component that makes the dwarf sleep when they get tired.
 */
public class SleepComponent extends AbstractCharacterComponent implements ISleepComponent {

    /** How many simulation steps before the dwarf will want to sleep. */
    private static final long SLEEP_THRESHOLD = IRegion.SIMULATION_STEPS_PER_DAY;

    /** How tired the dwarf is. */
    private int sleepiness;

    /** The sleep job for the dwarf. */
    private SleepJob sleepJob;

    /**
     * Constructor.
     * @param characterTmp the character that this component belongs to
     */
    public SleepComponent(final IGameCharacter characterTmp) {
        super(characterTmp);
    }

    @Override
    public void update(final IRegion region) {
        IJobManager jobManager = getCharacter().getPlayer().getJobManager();

        sleepiness++;
        if (sleepiness > SLEEP_THRESHOLD && sleepJob == null) {
            sleepJob = new SleepJob(getCharacter());
            jobManager.addJob(sleepJob);
        }

        if (sleepJob != null && sleepJob.isDone()) {
            sleepJob = null;
        }
    }

    @Override
    public void kill() {
        if (sleepJob != null) {
            sleepJob.interrupt("Character died");
        }
    }

    @Override
    public void sleep() {
        sleepiness = 0;
    }
}
