package controller.command;

import simulation.Player;
import simulation.map.MapIndex;

public class MilitaryMoveCommand extends AbstractCommand {

    /** The serial version UID. */
    private static final long serialVersionUID = -5244165300756387429L;
    private MapIndex target;

    public MilitaryMoveCommand(Player playerTmp, MapIndex targetTmp) {
        super(playerTmp);
        target = targetTmp;
    }

    @Override
    public void doCommand() {
        player.getMilitaryManager().militaryStation(target);
    }
}
