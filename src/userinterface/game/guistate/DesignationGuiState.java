package userinterface.game.guistate;

import simulation.job.designation.DesignationType;
import controller.command.DesignationCommand;

public class DesignationGuiState extends AbstractVariableSizeGuiState {

    private DesignationType designationType;

    public DesignationGuiState(final DesignationType designationTypeTmp) {
        designationType = designationTypeTmp;
    }

    @Override
    protected void doReleaseAction() {
        controller.addCommand(new DesignationCommand(absSelection, designationType, true, player));
    }
}
