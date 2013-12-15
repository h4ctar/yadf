package yadf.simulation;

import yadf.logger.Logger;
import yadf.simulation.character.GoblinManager;
import yadf.simulation.character.ICharacterManager;

/**
 * The goblin player, spawns goblins.
 */
public class GoblinPlayer extends AbstractPlayer {

    /** The region that this player is currently in. */
    private IRegion region;

    /**
     * Constructor.
     * @param regionTmp the region
     */
    public GoblinPlayer(final Region regionTmp) {
        super("Goblin");
        region = regionTmp;
        setComponent(ICharacterManager.class, new GoblinManager(this, region));
    }

    /**
     * Setup.
     */
    public void setup() {
        Logger.getInstance().log(this, "Setting up");
    }
}
