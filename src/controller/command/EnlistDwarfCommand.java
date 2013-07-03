package controller.command;

import simulation.Player;
import simulation.character.IGameCharacter;

/**
 * Enlist a new dwarf into the military.
 */
public class EnlistDwarfCommand extends AbstractCommand {

    /** The serial version UID. */
    private static final long serialVersionUID = 5260775713233467350L;

    /** The dwarf to enlist. */
    private final int dwarfId;

    /**
     * Constructor.
     * @param playerTmp the player
     * @param dwarfIdTmp the dwarf to enlist
     */
    public EnlistDwarfCommand(final Player playerTmp, final int dwarfIdTmp) {
        super(playerTmp);
        dwarfId = dwarfIdTmp;
    }

    @Override
    public void doCommand() {
        IGameCharacter dwarf = player.getDwarfManager().getDwarf(dwarfId);
        player.getMilitaryManager().enlistDwarf(dwarf);
    }
}
