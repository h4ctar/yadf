package yadf.ui.gdx.screen.game.window;

import yadf.simulation.job.IJob;
import yadf.simulation.job.IJobManager;
import yadf.simulation.job.IJobManagerListener;

import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * The Jobs Window.
 * <p>
 * Displays a list of the current jobs.
 */
public class JobsWindow extends AbstractWindow implements IJobManagerListener {

    /** The list of jobs. */
    private List jobsList;

    /** The job manager. */
    private IJobManager jobManager;

    /**
     * Constructor.
     * @param jobManagerTmp the job manager
     * @param skin the skin
     */
    public JobsWindow(final IJobManager jobManagerTmp, final Skin skin) {
        super("Jobs", skin);

        jobManager = jobManagerTmp;

        jobManager.addListener(this);
        jobsList = new List(jobManager.getJobs().toArray(), skin);
        ScrollPane scrollPane = new ScrollPane(jobsList);
        add(scrollPane).width(600).height(400);
        pack();
    }

    /**
     * Update the list of jobs.
     */
    private void updateList() {
        jobsList.setItems(jobManager.getJobs().toArray());
    }

    @Override
    public void jobAdded(final IJob job, final int index) {
        updateList();
    }

    @Override
    public void jobRemoved(final IJob job, final int index) {
        updateList();
    }
}
