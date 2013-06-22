package simulation.job;

import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import simulation.IPlayer;
import simulation.character.IDwarfManager;
import simulation.character.IGameCharacter;
import simulation.character.component.IMovementComponent;
import simulation.character.component.WalkMovementComponent;
import simulation.item.ContainerItem;
import simulation.item.IStockManager;
import simulation.item.Item;
import simulation.item.ItemType;
import simulation.map.MapIndex;

public class HaulJobTest extends TestCase {

    private HaulJob haulJob;

    @Mock
    private IPlayer player;

    @Mock
    private IDwarfManager dwarfManager;

    @Mock
    private IStockManager stockManager;

    @Mock
    private IGameCharacter character;

    @Mock
    private Item item;

    @Mock
    private ContainerItem container;

    private final MapIndex dropPosition = new MapIndex(10, 0, 0);

    private ItemType itemType;

    @Override
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(item.getPosition()).thenReturn(new MapIndex(0, 0, 0));
        when(player.getDwarfManager()).thenReturn(dwarfManager);
        when(player.getStockManager()).thenReturn(stockManager);
        // when(dwarfManager.getIdleDwarf(requiredLabor)).thenReturn(character);
        // when(character.setComponent(eq(IMovementComponent.class), any(WalkMovementComponent.class)));
    }

    @Override
    @After
    public void tearDown() {
    }

    @Test
    public void testCharacterItemHaulJob() {
        haulJob = new HaulJob(character, item, null, dropPosition);
        assertEquals("Walking to position", haulJob.getStatus());
        verify(character).setComponent(eq(IMovementComponent.class), any(WalkMovementComponent.class));
    }

    @Test
    public void testItemHaulJob() {
        haulJob = new HaulJob(item, null, dropPosition, player);
    }

    @Test
    public void testItemTypeHaulJob() {
        haulJob = new HaulJob(itemType, null, dropPosition, player);
    }
}
