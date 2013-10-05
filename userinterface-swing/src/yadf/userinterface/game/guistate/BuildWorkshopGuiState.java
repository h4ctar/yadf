package yadf.userinterface.game.guistate;

import yadf.controller.command.BuildWorkshopCommand;

/**
 * GUI state to build a workshop.
 */
public class BuildWorkshopGuiState extends AbstractFixedSizeGuiState {

    /** The type of workshop to build. */
    private String workshopTypeName;

    /**
     * Constructor.
     * @param workshopTypeNameTmp the type of workshop to build
     */
    public BuildWorkshopGuiState(final String workshopTypeNameTmp) {
        workshopTypeName = workshopTypeNameTmp;
    }

    @Override
    public String toString() {
        return "Build " + workshopTypeName;
    }

    @Override
    protected void doClickAction() {
        controller.addCommand(new BuildWorkshopCommand(player, position, workshopTypeName));
    }

    @Override
    protected int getWidth() {
        return 3;
    }

    @Override
    protected int getHeight() {
        return 3;
    }

    @Override
    protected boolean checkAreaValid() {
        return true;
    }
}
