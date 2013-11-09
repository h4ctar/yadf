package yadf.ui.gdx.screen.game;

import yadf.controller.AbstractController;
import yadf.simulation.IPlayer;
import yadf.simulation.job.designation.DesignationType;
import yadf.ui.gdx.screen.TileCamera;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

public class DesignateControls extends Table {

    private IControlsController controlsController;

    private IInteractorManager interactorManager;

    private IPlayer player;

    private TileCamera camera;

    private AbstractController controller;

    public DesignateControls(Skin skin, IControlsController controlsControllerTmp,
            IInteractorManager interactorManagerTmp, IPlayer playerTmp, TileCamera cameraTmp,
            AbstractController controllerTmp) {
        controlsController = controlsControllerTmp;
        interactorManager = interactorManagerTmp;
        player = playerTmp;
        camera = cameraTmp;
        controller = controllerTmp;

        setFillParent(true);
        align(Align.top | Align.left);
        pad(10);

        for (DesignationType designationType : DesignationType.values()) {
            TextButton designateButton = new TextButton(designationType.name, skin);
            designateButton.addListener(new DesignateButtonListener(designationType));
            add(designateButton).width(140).spaceBottom(10);
            row();
        }

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new CancelButtonListener());
        add(cancelButton).width(140);
    }

    /**
     * The listener for all of the designate buttons.
     */
    private final class DesignateButtonListener extends ClickListener {

        private DesignationType designationType;

        public DesignateButtonListener(DesignationType designationTypeTmp) {
            designationType = designationTypeTmp;
        }

        @Override
        public void clicked(InputEvent event, float x, float y) {
            interactorManager.installInteractor(new DesignateInteractor(designationType, player, camera,
                    controller));
        }
    }

    /**
     * The listener for the cancel button.
     */
    private final class CancelButtonListener extends ClickListener {

        @Override
        public void clicked(InputEvent event, float x, float y) {
            controlsController.cancelCurrentControls();
        }
    }
}
