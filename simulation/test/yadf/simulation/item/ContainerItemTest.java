package yadf.simulation.item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashSet;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import yadf.simulation.HumanPlayer;
import yadf.simulation.map.MapIndex;

/**
 * Test of the ContainerItem class.
 */
public class ContainerItemTest {
    /** The number of items in the barrel. */
    private static final int BARREL_CAPACITY = 20;

    /** Test item types. */
    private static final String BARREL_ITEM_TYPE_ELEMENT = "<categories><category name=\"Food\">"
            + "<itemType name=\"Bread\" weight=\"1\"/><itemType name=\"Chips\" weight=\"1\"/></category>"
            + "<category name=\"Storage\">" + "<itemType name=\"Barrel\" weight=\"4\" capacity=\""
            + BARREL_CAPACITY + "\" sprite=\"47\" contentItemTypes=\"Bread\" />" + "</category></categories>";

    /** A container element that is empty. */
    private static final String EMPTY_CONTAINER_ELEMENT = "<item type=\"Barrel\" />";

    /** The number of items in the barrel. */
    private static final int CONTENT_COUNT = 10;

    /** A container element that is not empty. */
    private static final String NOT_EMPTY_CONTAINER_ELEMENT = "<item type=\"Barrel\" contentType=\"Bread\" "
            + "contentQuantity=\"" + CONTENT_COUNT + "\" />";

    /** The mock player. */
    @Mock
    private HumanPlayer player;

    /** The mock stock manager. */
    @Mock
    private StockManager stockManager;

    /** Test item type. */
    private ItemType breadItemType;

    /** Test item type. */
    private ItemType chipsItemType;

    /** Test container item type. */
    private ItemType barrelItemType;

    /** The document builder factory. */
    private DocumentBuilderFactory documentBuilderFactory;

    /** The document builder. */
    private DocumentBuilder documentBuilder;

    /**
     * Setup the test case.
     * @throws Exception something went wrong
     */
    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        when(player.getComponent(IStockManager.class)).thenReturn(stockManager);

        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentBuilderFactory.newDocumentBuilder();

        InputStream inputStream = new ByteArrayInputStream(BARREL_ITEM_TYPE_ELEMENT.getBytes());
        Document document = documentBuilder.parse(inputStream);
        Element itemTypeElement = document.getDocumentElement();
        ItemTypeManager.getInstance().loadItemTypes(itemTypeElement);

        breadItemType = ItemTypeManager.getInstance().getItemType("Bread");
        chipsItemType = ItemTypeManager.getInstance().getItemType("Chips");
        barrelItemType = ItemTypeManager.getInstance().getItemType("Barrel");
    }

    /**
     * Test construct from element.
     * @throws Exception something went wrong
     */
    @Test
    public void testConstructFromElement() throws Exception {
        // An empty container
        InputStream inputStream = new ByteArrayInputStream(EMPTY_CONTAINER_ELEMENT.getBytes());
        Document document = documentBuilder.parse(inputStream);
        Element itemElement = document.getDocumentElement();

        ContainerItem containerItem = new ContainerItem(itemElement);
        assertEquals("Barrel", containerItem.getType().name);
        assertFalse(containerItem.isPlaced());
        assertNull(containerItem.player);
        assertFalse(containerItem.isUsed());
        assertNull(containerItem.getContentItemType());
        assertTrue(containerItem.isEmpty());
        assertFalse(containerItem.isFull());
        assertEquals(0, containerItem.getItems().size());

        // A container with items in it
        inputStream = new ByteArrayInputStream(NOT_EMPTY_CONTAINER_ELEMENT.getBytes());
        document = documentBuilder.parse(inputStream);
        itemElement = document.getDocumentElement();

        containerItem = new ContainerItem(itemElement);
        assertEquals("Barrel", containerItem.getType().name);
        assertFalse(containerItem.isPlaced());
        assertNull(containerItem.player);
        assertFalse(containerItem.isUsed());
        assertEquals("Bread", containerItem.getContentItemType().name);
        assertFalse(containerItem.isEmpty());
        assertFalse(containerItem.isFull());
        assertEquals(CONTENT_COUNT, containerItem.getItems().size());
    }

    /**
     * Test getUnusedItem.
     */
    @Test
    public void testGetUnusedItem() {
        ContainerItem containerItem = new ContainerItem(new MapIndex(), barrelItemType, player);
        assertTrue(containerItem.isEmpty());

        // Add two items
        Item item1 = new Item(new MapIndex(), breadItemType, player);
        item1.setUsed(false);
        containerItem.addItem(item1);
        Item item2 = new Item(new MapIndex(), breadItemType, player);
        item2.setUsed(true);
        containerItem.addItem(item2);
        assertEquals(2, containerItem.getItems().size());

        // Get an item that is unused and exists
        Item item3 = containerItem.getItem("Bread", false, false);
        assertSame(item1, item3);
        assertEquals(2, containerItem.getItems().size());

        // Get an item that does not exist
        Item item4 = containerItem.getItem("Pizza", false, false);
        assertNull(item4);
        assertEquals(2, containerItem.getItems().size());

        // Get an item that is already marked as used
        containerItem.removeItem(item3);
        assertEquals(1, containerItem.getItems().size());
        Item item5 = containerItem.getItem("Bread", false, false);
        assertNull(item5);
        assertEquals(1, containerItem.getItems().size());
    }

    /**
     * Test getUnusedItemFromCategory.
     */
    @Test
    public void testGetUnusedItemFromCategory() {
        ContainerItem containerItem = new ContainerItem(new MapIndex(), barrelItemType, player);
        assertTrue(containerItem.isEmpty());

        // Add two items
        Item item1 = new Item(new MapIndex(), breadItemType, player);
        item1.setUsed(false);
        containerItem.addItem(item1);
        Item item2 = new Item(new MapIndex(), breadItemType, player);
        item2.setUsed(true);
        containerItem.addItem(item2);
        assertEquals(2, containerItem.getItems().size());

        // Get an item that is unused and exists
        Item item3 = containerItem.getItemFromCategory("Food", false, false);
        assertSame(item1, item3);
        assertEquals(2, containerItem.getItems().size());

        // Get an item that does not exist
        Item item4 = containerItem.getItemFromCategory("Drink", false, false);
        assertNull(item4);
        assertEquals(2, containerItem.getItems().size());

        // Get an item that is already marked as used
        containerItem.removeItem(item3);
        assertEquals(1, containerItem.getItems().size());
        Item item5 = containerItem.getItemFromCategory("Food", false, false);
        assertNull(item5);
        assertEquals(1, containerItem.getItems().size());
    }

    /**
     * Test addItem.
     */
    @Test
    public void testAddItem() {
        ContainerItem containerItem = new ContainerItem(new MapIndex(), barrelItemType, player);
        assertTrue(containerItem.isEmpty());
        // The container should be listening to all items
        // verify(stockManager, times(1)).addListener(containerItem);

        // Add item to empty container
        Item item1 = new Item(new MapIndex(), breadItemType, player);
        containerItem.addItem(item1);
        assertEquals(1, containerItem.getItems().size());
        assertEquals(breadItemType, containerItem.getContentItemType());
        // The container should stop listening to all item types, and now only listen to the bread item type
        // verify(stockManager, times(1)).removeListener(containerItem);
        verify(stockManager, times(1)).addItemAvailableListener(breadItemType, containerItem);
        // TODO: The container should have created heaps of jobs

        // Add another item of same type
        Item item2 = new Item(new MapIndex(), breadItemType, player);
        containerItem.addItem(item2);
        assertEquals(2, containerItem.getItems().size());
        assertEquals(breadItemType, containerItem.getContentItemType());
        // The container should not change how it's listening
        // verify(stockManager, times(1)).removeListener(containerItem);
        verify(stockManager, times(1)).addItemAvailableListener(breadItemType, containerItem);

        // Add an item that is of different type
        Item item3 = new Item(new MapIndex(), chipsItemType, player);
        containerItem.addItem(item3);
        assertEquals(2, containerItem.getItems().size());
        assertEquals(breadItemType, containerItem.getContentItemType());
        // The container should not change how it's listening
        // verify(stockManager, times(1)).removeListener(containerItem);
        verify(stockManager, times(1)).addItemAvailableListener(breadItemType, containerItem);

        // Add an item that is in different location
        Item item4 = new Item(new MapIndex(1, 1, 1), breadItemType, player);
        containerItem.addItem(item4);
        assertEquals(2, containerItem.getItems().size());
        assertEquals(breadItemType, containerItem.getContentItemType());
        // The container should not change how it's listening
        // verify(stockManager, times(1)).removeListener(containerItem);
        verify(stockManager, times(1)).addItemAvailableListener(breadItemType, containerItem);

        // Add items to fill the container
        for (int i = 0; i < BARREL_CAPACITY - 2; i++) {
            Item item = new Item(new MapIndex(), breadItemType, player);
            containerItem.addItem(item);
        }
        assertEquals(BARREL_CAPACITY, containerItem.getItems().size());
        assertTrue(containerItem.isFull());
        // The container should stop listening as it's now full
        // verify(stockManager, times(2)).removeListener(containerItem);
        verify(stockManager, times(1)).addItemAvailableListener(breadItemType, containerItem);

        // Add an item to a full container
        Item item5 = new Item(new MapIndex(), breadItemType, player);
        containerItem.addItem(item5);
        assertEquals(BARREL_CAPACITY, containerItem.getItems().size());
        assertTrue(containerItem.isFull());
        // The container should not change how it's listening
        // verify(stockManager, times(2)).removeListener(containerItem);
        verify(stockManager, times(1)).addItemAvailableListener(breadItemType, containerItem);
    }

    /**
     * Test removeItem.
     */
    @Test
    public void testRemoveItem() {
        ContainerItem containerItem = new ContainerItem(new MapIndex(), barrelItemType, player);
        assertTrue(containerItem.isEmpty());
        // The container should be listening to all items
        // verify(stockManager, times(1)).addListener(containerItem);

        // Add two items to container
        Item item1 = new Item(new MapIndex(), breadItemType, player);
        containerItem.addItem(item1);
        assertEquals(1, containerItem.getItems().size());
        assertEquals(breadItemType, containerItem.getContentItemType());
        Item item2 = new Item(new MapIndex(), breadItemType, player);
        containerItem.addItem(item2);
        assertEquals(2, containerItem.getItems().size());
        // The container should stop listening to all item types, and now only listen to the bread item type
        // verify(stockManager, times(1)).addListener(containerItem);
        // verify(stockManager, times(1)).removeListener(containerItem);
        verify(stockManager, times(1)).addItemAvailableListener(breadItemType, containerItem);

        // Remove item from container
        boolean itemRemoved = containerItem.removeItem(item1);
        assertTrue(itemRemoved);
        assertEquals(1, containerItem.getItems().size());
        assertEquals(breadItemType, containerItem.getContentItemType());
        // The container should not change how it's listening
        // verify(stockManager, times(1)).addListener(containerItem);
        // verify(stockManager, times(1)).removeListener(containerItem);
        verify(stockManager, times(1)).addItemAvailableListener(breadItemType, containerItem);

        // Remove item that's not in container
        Item item3 = new Item(new MapIndex(), breadItemType, player);
        itemRemoved = containerItem.removeItem(item3);
        assertFalse(itemRemoved);
        assertEquals(1, containerItem.getItems().size());
        assertEquals(breadItemType, containerItem.getContentItemType());
        // The container should not change how it's listening
        // verify(stockManager, times(1)).addListener(containerItem);
        // verify(stockManager, times(1)).removeListener(containerItem);
        verify(stockManager, times(1)).addItemAvailableListener(breadItemType, containerItem);

        // Remove last item from container
        itemRemoved = containerItem.removeItem(item2);
        assertTrue(itemRemoved);
        assertEquals(0, containerItem.getItems().size());
        assertTrue(containerItem.isEmpty());
        assertNull(containerItem.getContentItemType());
        // The container should now be listening to all item types
        // verify(stockManager, times(2)).addListener(containerItem);
        // verify(stockManager, times(1)).removeListener(containerItem);
        verify(stockManager, times(1)).addItemAvailableListener(breadItemType, containerItem);
    }

    /**
     * Test canBeStored.
     */
    @Test
    public void testCanBeStored() {
        ContainerItem containerItem = new ContainerItem(new MapIndex(), barrelItemType, player);
        assertTrue(containerItem.isEmpty());

        Set<ItemType> set1 = new HashSet<>();
        set1.add(barrelItemType);
        Set<ItemType> set2 = new HashSet<>();
        set2.add(breadItemType);
        Set<ItemType> set3 = new HashSet<>();
        set3.add(barrelItemType);
        set3.add(breadItemType);

        // Empty container
        assertTrue(containerItem.canBeStored(set1));
        assertFalse(containerItem.canBeStored(set2));
        assertTrue(containerItem.canBeStored(set3));

        // Not empty container
        Item item = new Item(new MapIndex(), breadItemType, player);
        containerItem.addItem(item);
        assertFalse(containerItem.canBeStored(set1));
        assertTrue(containerItem.canBeStored(set2));
        assertTrue(containerItem.canBeStored(set3));
    }

    /**
     * Test itemAdded. When a new item is added to the stock manager.
     */
    @Test
    public void testItemAdded() {
        ContainerItem containerItem = new ContainerItem(new MapIndex(), barrelItemType, player);
        Item item = new Item(new MapIndex(), breadItemType, player);

        // The container is empty, and the stock manager has no unstored items
        when(stockManager.getItemQuantity(breadItemType)).thenReturn(1);
        containerItem.itemAvailable(item, null);
        assertNull(containerItem.getContentItemType());

        // The container is empty, but the stock manager has heaps of unstored items
        when(stockManager.getItemQuantity(breadItemType)).thenReturn(50);
        containerItem.itemAvailable(item, null);
        assertEquals(breadItemType, containerItem.getContentItemType());
    }

    /**
     * Test jobChanged.
     */
}
