package yadf.simulation.character.component;

import java.util.Set;

import yadf.simulation.IPlayer;
import yadf.simulation.IRegion;
import yadf.simulation.character.ICharacterManager;
import yadf.simulation.character.IGameCharacter;

/**
 * The lookout component.
 * <p>
 * Looks for an enemy and then notifies any listeners.
 */
public class LookoutComponent extends AbstractCharacterComponent implements ILookoutComponent {

    /** How far can the soldier see. */
    private static final int VIEW_DISTANCE = 20;

    /** The enemy that was spotted. */
    private IGameCharacter enemy;

    /**
     * Constructor.
     * @param soldier the dwarf that is looking for an enemy
     */
    public LookoutComponent(final IGameCharacter soldier) {
        super(soldier);
    }

    @Override
    public void update(final IRegion region) {
        IGameCharacter soldier = getCharacter();
        Set<IPlayer> players = soldier.getRegion().getPlayers();
        // TODO: change to get getMilitaryManager().getEnemys()
        for (IPlayer player : players) {
            if (player == soldier.getPlayer()) {
                continue;
            }
            IGameCharacter enemyTmp = player.getComponent(ICharacterManager.class).getCharacter(soldier.getPosition(),
                    VIEW_DISTANCE);
            if (enemyTmp != null) {
                enemy = enemyTmp;
                notifyListeners();
                break;
            }
        }
    }

    /**
     * Get the spotted enemy.
     * @return the enemy
     */
    public IGameCharacter getEnemy() {
        return enemy;
    }

    @Override
    public void kill() {
        // do nothing
    }
}
