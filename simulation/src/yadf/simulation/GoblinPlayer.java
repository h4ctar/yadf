package yadf.simulation;

import yadf.logger.Logger;
import yadf.simulation.character.GoblinManager;
import yadf.simulation.character.ICharacterManager;
import yadf.simulation.map.MapArea;
import yadf.simulation.map.MapIndex;

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

    @Override
    public boolean checkAreaValid(final MapArea area) {
        return true;
    }

    @Override
    public boolean checkAreaValid(final MapIndex mapIndex) {
        return true;
    }
}
