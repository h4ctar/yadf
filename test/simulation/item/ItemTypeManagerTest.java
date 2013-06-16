package simulation.item;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Collection;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import simulation.map.MapIndex;

/**
 * Test for ItemTypeManager.
 */
public class ItemTypeManagerTest {
    private static final String ITEM_TYPES = "<categories>"
            + "<category name=\"Food\">"
            + "<itemTypes>"
            + "<itemType name=\"Pork\" weight=\"1\" sprite=\"25\" />"
            + "<itemType name=\"Fish\" weight=\"1\" sprite=\"1\" />"
            + "<itemType name=\"Bread\" weight=\"1\" sprite=\"20\" />"
            + "</itemTypes>"
            + "</category>"
            + "<category name=\"Storage\">"
            + "<itemTypes>"
            + "<itemType name=\"Barrel\" weight=\"4\" capacity=\"40\" sprite=\"47\" contentCategory=\"Food,Drink\" placeable=\"true\" />"
            + "<itemType name=\"Sack\" weight=\"2\" capacity=\"40\" sprite=\"19\" contentCategory=\"Plant\" />"
            + "</itemTypes>" + "</category>" + "</categories>";

    private static final String ITEM_ELEMENT = "<item type=\"Bread\" quantity=\"10\" />";

    @Before
    public void setUp() throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();

        InputStream inputStream = new ByteArrayInputStream(ITEM_TYPES.getBytes());
        Document document = documentBuilder.parse(inputStream);
        Element root = document.getDocumentElement();
        ItemTypeManager.getInstance().loadItemTypes(root);
    }

    @After
    public void tearDown() throws Exception {
        ItemTypeManager.getInstance().unload();
    }

    @Test
    public void testGetCategoryNames() {
        Set<String> categoryNames = ItemTypeManager.getInstance().getCategoryNames();
        assertEquals("Should have 2 categories", 2, categoryNames.size());
        assertTrue("Categories should contain Food", categoryNames.contains("Food"));
        assertTrue("Categories should contain Storage", categoryNames.contains("Storage"));
    }

    @Test
    public void testGetItemType() {
        ItemType itemType = ItemTypeManager.getInstance().getItemType("Pork");
        assertEquals("Item type name should be Pork", "Pork", itemType.name);
        assertEquals("Item type capacity should be 0", 0, itemType.capacity);
        assertNull("Item type content item type names should be null", itemType.contentItemTypeNames);
        assertEquals("Item type category should be Food", "Food", itemType.category);
        assertFalse("Item type placeable should be False", itemType.placeable);
        assertEquals("Item type sprite should be 25", 25, itemType.sprite);
        assertEquals("Item type weight should be 1", 1, itemType.weight);

        itemType = ItemTypeManager.getInstance().getItemType("Barrel");
        assertEquals("Item type name should be Barrel", "Barrel", itemType.name);
        assertEquals("Item type capacity should be 40", 40, itemType.capacity);
        // assertEquals("Item type content item type names should contain Food", "Food",
        // itemType.contentItemTypeNames[0]);
        // assertEquals("Item type content item type names should contain Drink", "Drink",
        // itemType.contentItemTypeNames[1]);
        assertEquals("Item type category should be Storage", "Storage", itemType.category);
        assertTrue("Item type placeable should be True", itemType.placeable);
        assertEquals("Item type sprite should be 47", 47, itemType.sprite);
        assertEquals("Item type weight should be 4", 4, itemType.weight);
    }

    @Test
    public void testGetItemTypesFromCategory() {
        Collection<ItemType> itemTypes = ItemTypeManager.getInstance().getItemTypesFromCategory("Food");
        assertEquals("Should contain 3 item types", 3, itemTypes.size());
    }

    @Test
    public void testGetNumberOfItemTypesInCategory() {
        int number = ItemTypeManager.getInstance().getNumberOfItemTypesInCategory("Food");
        assertEquals("Should contain 3 item types", 3, number);
    }

    @Test
    public void testGetPlaceableItems() {
        Collection<ItemType> itemTypes = ItemTypeManager.getInstance().getPlaceableItems();
        assertEquals("Should contain 1 item types", 1, itemTypes.size());
        assertTrue("Should contain Barrel", itemTypes.contains(ItemTypeManager.getInstance().getItemType("Barrel")));
    }

    @Test
    public void testCreateItem() throws Exception {
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        InputStream inputStream = new ByteArrayInputStream(ITEM_ELEMENT.getBytes());
        Document document = documentBuilder.parse(inputStream);
        Element root = document.getDocumentElement();

        // From element
        Item item = ItemTypeManager.getInstance().createItem(root);
        assertEquals("Item position should be 0", new MapIndex(), item.getPosition());
        assertEquals("Item type should be Bread", "Bread", item.getType().name);

        // From item type
        MapIndex position = new MapIndex(1, 2, 3);
        item = ItemTypeManager.getInstance().createItem(position, ItemTypeManager.getInstance().getItemType("Bread"),
                null);
        assertEquals("Item position should be position", position, item.getPosition());
        assertEquals("Item type should be Bread", "Bread", item.getType().name);

        // From another item
        Item itemFrom = new Item(position, ItemTypeManager.getInstance().getItemType("Bread"), null);
        item = ItemTypeManager.getInstance().createItem(itemFrom, null);
        assertEquals("Item position should be position", position, item.getPosition());
        assertEquals("Item type should be Bread", "Bread", item.getType().name);
        assertTrue("Item should not be the same", itemFrom != item);
    }
}
