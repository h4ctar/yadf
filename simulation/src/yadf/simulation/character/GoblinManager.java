package yadf.simulation.character;

import yadf.simulation.IPlayer;
import yadf.simulation.IRegion;
import yadf.simulation.ITimeListener;
import yadf.simulation.map.MapIndex;

/**
 * The goblin manager, should spawn goblins when it feels like it.
 */
public class GoblinManager extends AbstractCharacterManager implements ITimeListener {

    /** The player that this manager belongs to. */
    private final IPlayer player;

    /** The region the manager is managing goblins for. */
    private final IRegion region;

    /**
     * Constructor.
     * @param playerTmp the player that this manager belongs to
     * @param regionTmp the region the manager is managing goblins for
     */
    public GoblinManager(final IPlayer playerTmp, final IRegion regionTmp) {
        player = playerTmp;
        region = regionTmp;
        region.addTimeListener(IRegion.SIMULATION_STEPS_PER_DAY, this);
    }

    @Override
    public void notifyTimeEvent() {
        MapIndex position = region.getRandomSurfacePosition();
        Goblin goblin = new Goblin(position, region, player);
        addGameObject(goblin);
        region.addTimeListener(IRegion.SIMULATION_STEPS_PER_DAY, this);
    }
}
