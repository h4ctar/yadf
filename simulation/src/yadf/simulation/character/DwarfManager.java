package yadf.simulation.character;

import yadf.misc.NameGenerator;
import yadf.simulation.IPlayer;
import yadf.simulation.IRegion;
import yadf.simulation.map.MapIndex;

/**
 * The dwarf manager. There should be one dwarf manager per player per region.
 */
public class DwarfManager extends AbstractCharacterManager {

    /** The name generator. */
    private NameGenerator nameGenerator;

    /** The player that this manager belongs to. */
    private final IPlayer player;

    /**
     * Constructor.
     * @param playerTmp the player that this manager belongs to.
     */
    public DwarfManager(final IPlayer playerTmp) {
        player = playerTmp;
        try {
            nameGenerator = new NameGenerator("elven.txt");
        } catch (Exception e) {
            e.printStackTrace();
            nameGenerator = null;
        }
    }

    /**
     * Adds a new dwarf.
     * @param position the position of the new dwarf
     * @param region the region the dwarf is in
     */
    public void addNewDwarf(final MapIndex position, final IRegion region) {
        Dwarf dwarf = new Dwarf(nameGenerator.compose(2), position, region, player);
        dwarf.addGameObjectListener(this);
        addGameObject(dwarf);
    }
}
