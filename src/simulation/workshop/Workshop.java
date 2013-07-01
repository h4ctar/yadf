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

import simulation.AbstractGameObject;
import simulation.IPlayer;
import simulation.job.CraftJob;
import simulation.job.IJob;
import simulation.map.MapArea;
import simulation.map.MapIndex;
import simulation.recipe.Recipe;
import simulation.recipe.RecipeManager;

/**
 * The Class Workshop.
 */
// TODO: should be a container
public class Workshop extends AbstractGameObject implements IWorkshop {

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

    /** The player that this workshop belongs to. */
    private final IPlayer player;

    /**
     * Instantiates a new workshop.
     * @param playerTmp the player that this workshop belongs to
     * @param workshopTypeTmp the workshop type
     * @param positionTmp the position
     */
    public Workshop(final IPlayer playerTmp, final WorkshopType workshopTypeTmp, final MapIndex positionTmp) {
        workshopType = workshopTypeTmp;
        position = positionTmp;
        player = playerTmp;
    }

    @Override
    public void addListener(final IWorkshopListener listener) {
        listeners.add(listener);
    }

    @Override
    public void removeListener(final IWorkshopListener listener) {
        listeners.remove(listener);
    }

    @Override
    public void cancelOrder(final int orderIndex) {
        if (orderIndex == 0 && craftJob != null) {
            craftJob.interrupt("Order canceled");
        } else {
            Recipe removedOrder = orders.remove(orderIndex);
            notifyListenersOfOrderRemoved(removedOrder, orderIndex);
        }
    }

    /**
     * Gets if the workshop is occupied.
     * @return the occupied
     */
    public boolean getOccupied() {
        return occupied;
    }

    @Override
    public List<Recipe> getOrders() {
        return orders;
    }

    @Override
    public MapIndex getPosition() {
        return position;
    }

    @Override
    public WorkshopType getType() {
        return workshopType;
    }

    @Override
    public boolean hasIndex(final MapIndex index) {
        if (index.x >= position.x && index.x <= position.x + WORKSHOP_SIZE - 1 && index.y >= position.y
                && index.y <= position.y + WORKSHOP_SIZE - 1 && position.z == index.z) {
            return true;
        }
        return false;
    }

    @Override
    public void newOrder(final String recipeName) {
        Recipe recipe = RecipeManager.getInstance().getRecipe(recipeName);
        orders.add(recipe);
        notifyListenersOfOrderAdded(recipe, orders.size() - 1);
    }

    @Override
    public void setOccupied(final boolean occupiedTmp) {
        if (occupiedTmp != occupied) {
            occupied = occupiedTmp;
        }
    }

    @Override
    public void update() {
        // TODO: Remove this method
        if (craftJob == null) {
            if (!orders.isEmpty()) {
                craftJob = new CraftJob(this, orders.get(0), player);
                player.getJobManager().addJob(craftJob);
            }
        } else {
            if (craftJob.isDone()) {
                Recipe removedOrder = orders.remove(0);
                notifyListenersOfOrderRemoved(removedOrder, 0);
                craftJob = null;
            }
        }
    }

    /**
     * Notify all the listeners that an order has been added.
     * @param recipe the recipe that was added
     * @param index the index of the recipe that was added
     */
    private void notifyListenersOfOrderAdded(final Recipe recipe, final int index) {
        for (IWorkshopListener listener : listeners) {
            listener.orderAdded(recipe, index);
        }
    }

    /**
     * Notify all the listeners that an order has been removed.
     * @param recipe the recipe that was removed
     * @param index the index of the recipe that was removed
     */
    private void notifyListenersOfOrderRemoved(final Recipe recipe, final int index) {
        for (IWorkshopListener listener : listeners) {
            listener.orderRemoved(recipe, index);
        }
    }

    @Override
    public MapArea getArea() {
        return new MapArea(position, WORKSHOP_SIZE, WORKSHOP_SIZE);
    }
}
