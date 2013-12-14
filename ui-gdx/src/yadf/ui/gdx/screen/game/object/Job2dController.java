package yadf.ui.gdx.screen.game.object;

import yadf.simulation.IGameObject;
import yadf.simulation.job.BuildWorkshopJob;
import yadf.simulation.job.IJob;
import yadf.simulation.map.MapArea;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

public class Job2dController extends Abstract2dController {

    public Job2dController(final Stage gameStage) {
        super(gameStage);
    }

    @Override
    protected Actor createGameObject2d(final IGameObject gameObject) {
        IJob job = (IJob) gameObject;
        Actor gameObject2d = null;
        if (job instanceof BuildWorkshopJob) {
            gameObject2d = new Job2d(new MapArea(job.getPosition(), 3, 3));
        }
        return gameObject2d;
    }
}
