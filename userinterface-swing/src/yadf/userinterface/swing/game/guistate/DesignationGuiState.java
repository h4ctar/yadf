package yadf.userinterface.swing.game.guistate;

import java.awt.event.MouseEvent;

import yadf.controller.command.DesignationCommand;
import yadf.simulation.job.designation.DesignationType;

/**
 * GUI state to modify a designation.
 */
public class DesignationGuiState extends AbstractVariableSizeGuiState {

    /** The designation type. */
    private DesignationType designationType;

    /**
     * Constructor.
     * @param designationTypeTmp the designation type
     */
    public DesignationGuiState(final DesignationType designationTypeTmp) {
        designationType = designationTypeTmp;
    }

    @Override
    protected void doReleaseAction() {
        boolean add = button == MouseEvent.BUTTON1;
        controller.addCommand(new DesignationCommand(absSelection, designationType, add, player));
    }

    @Override
    protected boolean checkAreaValid() {
        return false;
    }

    @Override
    public String toString() {
        return "Designate " + designationType.name();
    }
}
