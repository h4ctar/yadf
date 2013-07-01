package controller.command;

import simulation.Player;
import simulation.workshop.IWorkshop;

/**
 * Command to delete a workshop.
 */
public class DeleteWorkshopCommand extends AbstractCommand {

    /** The serial version UID. */
    private static final long serialVersionUID = 6706293486524959536L;

    /** The workshop id. */
    private final int workshopId;

    /**
     * Instantiates a new delete workshop command.
     * 
     * @param player the player
     * @param workshopIdTmp the workshop id
     */
    public DeleteWorkshopCommand(final Player player, final int workshopIdTmp) {
        super(player);
        workshopId = workshopIdTmp;
    }

    @Override
    public void doCommand() {
        IWorkshop workshop = player.getWorkshopManager().getWorkshop(workshopId);
        workshop.delete();
    }
}
