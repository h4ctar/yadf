package yadf.ui.gdx.screen.game.dialogwindow;

import yadf.simulation.job.IJob;
import yadf.simulation.job.IJobManager;
import yadf.simulation.job.IJobManagerListener;

import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

/**
 * The jobs dialog window.
 * <p>
 * Displays a list of the current jobs.
 */
public class JobsDialogWindow extends Dialog implements IJobManagerListener {

    /** The list of jobs. */
    private List list;

    /** The job manager. */
    private IJobManager jobManager;

    /** The skin. */
    private Skin skin;

    /**
     * Constructor.
     * @param jobManagerTmp the job manager
     * @param skinTmp the skin
     */
    public JobsDialogWindow(final IJobManager jobManagerTmp, final Skin skinTmp) {
        super("Jobs", skinTmp);

        jobManager = jobManagerTmp;
        skin = skinTmp;

        jobManager.addListener(this);
        list = new List(jobManager.getJobs().toArray(), skin);

        ScrollPane scrollPane = new ScrollPane(list);
        getContentTable().add(scrollPane).width(600).height(400);
        button("Close");
    }

    /**
     * Update the list of jobs.
     */
    private void updateList() {
        list.setItems(jobManager.getJobs().toArray());
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
