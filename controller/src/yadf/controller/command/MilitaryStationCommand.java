package yadf.controller.command;

import yadf.simulation.IPlayer;
import yadf.simulation.map.MapIndex;
import yadf.simulation.military.IMilitaryManager;

/**
 * The station military command.
 */
public class MilitaryStationCommand extends AbstractCommand {

    /** The serial version UID. */
    private static final long serialVersionUID = -5244165300756387429L;

    /** Where to station the military. */
    private MapIndex target;

    /**
     * Constructor.
     * @param player the player
     * @param targetTmp where to station the military
     */
    public MilitaryStationCommand(final IPlayer player, final MapIndex targetTmp) {
        super(player);
        target = targetTmp;
    }

    @Override
    public void doCommand() {
        player.getComponent(IMilitaryManager.class).station(target);
    }
}
