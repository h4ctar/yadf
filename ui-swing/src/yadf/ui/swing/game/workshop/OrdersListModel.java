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
package yadf.ui.swing.game.workshop;

import java.util.ArrayList;
import java.util.List;

import javax.swing.AbstractListModel;

import yadf.simulation.recipe.Recipe;
import yadf.simulation.workshop.IWorkshop;
import yadf.simulation.workshop.IWorkshopListener;

/**
 * The Class OrdersListModel.
 */
class OrdersListModel extends AbstractListModel<String> implements IWorkshopListener {

    /** The serial version UID. */
    private static final long serialVersionUID = -221041635977890239L;

    /** The workshop. */
    private IWorkshop workshop;

    /** A cache of all the orders. */
    private List<String> orders;

    /**
     * Sets the workshop.
     * @param workshopTmp the new workshop
     */
    public void setWorkshop(final IWorkshop workshopTmp) {
        if (workshop != null) {
            workshop.removeListener(this);
        }
        workshop = workshopTmp;
        workshop.addListener(this);
        orders = new ArrayList<>();
        for (Recipe recipe : workshop.getOrders()) {
            orders.add(recipe.toString());
        }
    }

    @Override
    public String getElementAt(final int row) {
        String element = null;
        if (orders != null) {
            element = orders.get(row);
        }
        return element;
    }

    @Override
    public int getSize() {
        if (workshop == null) {
            return 0;
        }
        return workshop.getOrders().size();
    }

    @Override
    public void orderAdded(final Recipe recipe, final int index) {
        orders.add(index, recipe.toString());
        fireContentsChanged(this, index, index);
    }

    @Override
    public void orderRemoved(final Recipe recipe, final int index) {
        orders.remove(index);
        fireContentsChanged(this, index, index);
    }
}
