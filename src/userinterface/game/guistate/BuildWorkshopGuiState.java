package userinterface.game.guistate;

import controller.command.BuildWorkshopCommand;

public class BuildWorkshopGuiState extends AbstractFixedSizeGuiState {

    private String workshopTypeName;

    public BuildWorkshopGuiState(String workshopTypeNameTmp) {
        workshopTypeName = workshopTypeNameTmp;
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
}
