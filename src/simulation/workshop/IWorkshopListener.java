package simulation.workshop;

import simulation.recipe.Recipe;

public interface IWorkshopListener {
    void orderAdded(Recipe recipe, int index);

    void orderRemoved(Recipe recipe, int index);
}
