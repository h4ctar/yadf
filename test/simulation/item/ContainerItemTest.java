package simulation.item;

import static org.junit.Assert.fail;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * Test of the ContainerItem class.
 */
public class ContainerItemTest {
    /** A normal DOM item element. */
    private final static String NORMAL_ITEM_ELEMENT = "<item type=\"Pick\" />";

    /** A container element that is empty. */
    private final static String EMPTY_CONTAINER_ELEMENT = "<item type=\"Barrel\" />";
    private final static String NON_EMPTY_CONTAINER_ELEMENT = "<item type=\"Barrel\" contentType=\"Bread\" contentQuantity=\"10\" />";

    private DocumentBuilderFactory documentBuilderFactory;
    private DocumentBuilder documentBuilder;

    @Before
    public void setUp() throws Exception {
        documentBuilderFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentBuilderFactory.newDocumentBuilder();
    }

    @Test
    public void testConstructFromElement() throws Exception {
        InputStream inputStream = new ByteArrayInputStream(NORMAL_ITEM_ELEMENT.getBytes());
        Document document = documentBuilder.parse(inputStream);
        Element itemElement = document.getDocumentElement();

        ContainerItem containerItem = new ContainerItem(itemElement);
        fail("Not yet implemented");
    }
}
