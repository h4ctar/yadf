package yadf.controller.command;

import yadf.simulation.IPlayer;
import yadf.simulation.job.IJob;
import yadf.simulation.job.IJobManager;

/**
 * Command to cancel a job.
 */
public class CancelJobCommand extends AbstractCommand {

    /** The serial version UID. */
    private static final long serialVersionUID = 4134567588301774783L;

    /** The ID of the job to cancel. */
    private final int jobId;

    /**
     * Constructor.
     * @param player the player
     * @param jobIdTmp the job ID
     */
    public CancelJobCommand(final IPlayer player, final int jobIdTmp) {
        super(player);
        jobId = jobIdTmp;
    }

    @Override
    public void doCommand() {
        IJob job = player.getComponent(IJobManager.class).getJob(jobId);
        job.interrupt("God canceled it");
    }
}
