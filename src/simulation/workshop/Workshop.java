/**
 * yadf
 * 
 * https://sourceforge.net/projects/yadf
 * 
 * Ben Smith (bensmith87@gmail.com)
 * 
 * yadf is placed under the BSD license.
 * 
 * Copyright (c) 2012-2013, Ben Smith All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * - Redistributions of source code must retain the above copyright notice, this list of conditions and the following
 * disclaimer.
 * 
 * - Redistributions in binary form must reproduce the above copyright notice, this list of conditions and the following
 * disclaimer in the documentation and/or other materials provided with the distribution.
 * 
 * - Neither the name of the yadf project nor the names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES,
 * INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package simulation.workshop;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import logger.Logger;
import misc.MyRandom;
import simulation.AbstractGameObject;
import simulation.Player;
import simulation.item.IContainer;
import simulation.item.Item;
import simulation.job.CraftJob;
import simulation.job.IJob;
import simulation.map.MapArea;
import simulation.map.MapIndex;
import simulation.recipe.Recipe;
import simulation.recipe.RecipeManager;

/**
 * The Class Workshop.
 */
public class Workshop extends AbstractGameObject implements IContainer {

    /** The serial version UID. */
    private static final long serialVersionUID = 6679458068681849511L;

    /** The position. */
    private final MapIndex position;

    /** The workshop type. */
    private final WorkshopType workshopType;

    /** Size of a workshop. */
    public static final int WORKSHOP_SIZE = 3;

    /** The occupied. */
    private boolean occupied = false;

    /** The craft job. */
    private IJob craftJob;

    /** The orders. */
    private final List<Recipe> orders = new ArrayList<>();

    /** Listeners to changes of the workshop. */
    private final List<IWorkshopListener> listeners = new ArrayList<>();

    /**
     * Instantiates a new workshop.
     * @param workshopTypeTmp the workshop type
     * @param positionTmp the position
     */
    public Workshop(final WorkshopType workshopTypeTmp, final MapIndex positionTmp) {
        workshopType = workshopTypeTmp;
        position = positionTmp;
    }

    /**
     * Add a listener to changes to the workshop.
     * @param listener the listener to add
     */
    public void addListener(final IWorkshopListener listener) {
        listeners.add(listener);
    }

    /**
     * Cancel an order.
     * @param orderIndex the order index
     */
    public void cancelOrder(final int orderIndex) {
        if (orderIndex == 0 && craftJob != null) {
            craftJob.interrupt("Order canceled");
        } else {
            orders.remove(orderIndex);
        }
    }

    /**
     * Gets if the workshop is occupied.
     * @return the occupied
     */
    public boolean getOccupied() {
        return occupied;
    }

    /**
     * Gets the orders.
     * @return the orders
     */
    public List<Recipe> getOrders() {
        return orders;
    }

    /**
     * Gets the position of the workshop.
     * @return the position
     */
    public MapIndex getPosition() {
        return position;
    }

    /**
     * Gets a random position inside a workshop.
     * @param position the position of the workshop
     * @return the random position
     */
    public static MapIndex getRandomPostition(final MapIndex position) {
        Random random = MyRandom.getInstance();
        return position.add(random.nextInt(WORKSHOP_SIZE), random.nextInt(WORKSHOP_SIZE), 0);
    }

    /**
     * Get a random position within the workshop.
     * @return a position within the workshop
     */
    public MapIndex getRandomPosition() {
        Random random = MyRandom.getInstance();
        return position.add(random.nextInt(WORKSHOP_SIZE), random.nextInt(WORKSHOP_SIZE), 0);
    }

    /**
     * Gets the type of the workshop.
     * @return the type
     */
    public WorkshopType getType() {
        return workshopType;
    }

    /**
     * Checks if a map index is within the workshop.
     * @param index the index
     * @return true, if successful
     */
    public boolean hasIndex(final MapIndex index) {
        if (index.x >= position.x && index.x <= position.x + WORKSHOP_SIZE && index.y >= position.y
                && index.y <= position.y + WORKSHOP_SIZE && position.z == index.z) {
            return true;
        }
        return false;
    }

    /**
     * New order.
     * @param recipeName the recipe name
     */
    public void newOrder(final String recipeName) {
        Recipe recipe = RecipeManager.getInstance().getRecipe(recipeName);
        orders.add(recipe);
        notifyListeners();
    }

    /**
     * Sets if the room is occupied.
     * @param occupiedTmp the new occupied
     */
    public void setOccupied(final boolean occupiedTmp) {
        if (occupiedTmp != occupied) {
            occupied = occupiedTmp;
            notifyListeners();
        }
    }

    /**
     * Update.
     * @param player the player
     */
    public void update(final Player player) {
        if (craftJob == null) {
            if (!orders.isEmpty()) {
                craftJob = new CraftJob(this, orders.get(0));
                player.getJobManager().addJob(craftJob);
                notifyListeners();
            }
        } else {
            if (craftJob.isDone()) {
                orders.remove(0);
                craftJob = null;
                notifyListeners();
            }
        }
    }

    /**
     * Notify all the listeners that something has changed in the workshop.
     */
    private void notifyListeners() {
        for (IWorkshopListener listener : listeners) {
            listener.workshopChanged();
        }
    }

    @Override
    public boolean addItem(final Item item) {
        Logger.getInstance().log(this, "Not implemented yet", true);
        return false;
    }

    @Override
    public boolean removeItem(final Item item) {
        Logger.getInstance().log(this, "Not implemented yet", true);
        return false;
    }

    @Override
    public Item getUnusedItem(final String itemTypeName) {
        Logger.getInstance().log(this, "Not implemented yet", true);
        return null;
    }

    @Override
    public Item getUnusedItemFromCategory(final String category) {
        Logger.getInstance().log(this, "Not implemented yet", true);
        return null;
    }

    /**
     * Get the area of the workshop.
     * @return the area
     */
    public MapArea getArea() {
        return new MapArea(position, WORKSHOP_SIZE, WORKSHOP_SIZE);
    }
}
