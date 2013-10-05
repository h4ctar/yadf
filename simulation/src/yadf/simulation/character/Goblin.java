package yadf.simulation.character;

import yadf.simulation.IPlayer;
import yadf.simulation.IRegion;
import yadf.simulation.map.MapIndex;

/**
 * A goblin.
 */
public class Goblin extends AbstractCharacter {

    /**
     * Constructor.
     * @param position the position of the goblin
     * @param regionTmp the region that the goblin is in
     * @param playerTmp the player that the goblin belongs to
     */
    public Goblin(final MapIndex position, final IRegion regionTmp, final IPlayer playerTmp) {
        super("Goblin", position, regionTmp, playerTmp);
    }
}
