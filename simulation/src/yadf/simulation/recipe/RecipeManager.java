package yadf.simulation.recipe;

import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import yadf.simulation.workshop.WorkshopType;

/**
 * Manager to load and store all the recipes.
 */
public final class RecipeManager {
    /** Instance of this singleton. */
    private static RecipeManager instance;

    /**
     * Returns the singleton instance.
     * @return the instance
     */
    public static RecipeManager getInstance() {
        if (instance == null) {
            instance = new RecipeManager();
        }
        return instance;
    }

    /** The recipes, keyed by recipe name. */
    public final Map<String, Recipe> recipes = new HashMap<>();

    /** The recipes, keyed by workshop type. */
    public final Map<WorkshopType, Set<Recipe>> recipesByWorkshop = new HashMap<>();

    /**
     * Constructor.
     */
    private RecipeManager() {
    }

    /**
     * Get a recipe that matches a name.
     * @param recipeName the recipes name
     * @return the recipe
     */
    public Recipe getRecipe(final String recipeName) {
        return recipes.get(recipeName);
    }

    /**
     * Find recipes for a workshop.
     * 
     * @param workshopType the workshop type
     * @return the list
     */
    public Set<Recipe> getRecipesForWorkshop(final WorkshopType workshopType) {
        return recipesByWorkshop.get(workshopType);
    }

    /**
     * Loads all the recipes from an XML file.
     * @throws Exception if the load goes bad
     */
    public void load() throws Exception {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("recipes.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputStream);
        NodeList recipeNodes = document.getElementsByTagName("recipe");
        for (int i = 0; i < recipeNodes.getLength(); i++) {
            Node recipeNode = recipeNodes.item(i);
            Element recipeElement = (Element) recipeNode;
            Recipe recipe = new Recipe(recipeElement);
            recipes.put(recipe.name, recipe);
            if (!recipesByWorkshop.containsKey(recipe.workshopType)) {
                recipesByWorkshop.put(recipe.workshopType, new LinkedHashSet<Recipe>());
            }
            recipesByWorkshop.get(recipe.workshopType).add(recipe);
        }
    }
}
