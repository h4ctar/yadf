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
package simulation.recipe;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import simulation.item.ItemType;
import simulation.item.ItemTypeManager;
import simulation.workshop.WorkshopType;
import simulation.workshop.WorkshopTypeManager;

/**
 * The Class Recipe.
 */
public class Recipe implements Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = 1857760478240172942L;

    /** The item. */
    public final ItemType itemType;

    /** The name. */
    public final String name;

    /** The workshop. */
    public final WorkshopType workshopType;

    /** The skill. */
    public final String skill;

    /** The quantity. */
    public final int quantity;

    /** The resource names. */
    public final Map<ItemType, Integer> resources;

    /**
     * Instantiates a new recipe.
     * @param recipeElement the recipe element
     * @throws Exception incase something goes wrong
     */
    public Recipe(final Element recipeElement) throws Exception {
        name = recipeElement.getAttribute("name");
        String itemTypeName = recipeElement.getAttribute("item");
        itemType = ItemTypeManager.getInstance().getItemType(itemTypeName);
        if (itemType == null) {
            throw new Exception("Item type does not exist: " + itemTypeName);
        }
        String workshopTypeName = recipeElement.getAttribute("workshop");
        workshopType = WorkshopTypeManager.getInstance().getWorkshopType(workshopTypeName);
        if (workshopType == null) {
            throw new Exception("Workshop type does not exist: " + workshopTypeName);
        }
        skill = recipeElement.getAttribute("skill");
        String tempString = recipeElement.getAttribute("quantity");
        quantity = "".equals(tempString) ? 1 : Integer.parseInt(tempString);
        resources = new HashMap<>();
        NodeList resourceNodes = recipeElement.getElementsByTagName("resource");
        for (int i = 0; i < resourceNodes.getLength(); i++) {
            Node resourceNode = resourceNodes.item(i);
            Element resourceElement = (Element) resourceNode;
            String resourceName = resourceElement.getAttribute("name");
            ItemType resourceType = ItemTypeManager.getInstance().getItemType(resourceName);
            if (resourceType == null) {
                throw new Exception("Item type does not exist: " + resourceType);
            }
            tempString = resourceElement.getAttribute("quantity");
            int resourceQuantity = "".equals(tempString) ? 1 : Integer.parseInt(tempString);
            resources.put(resourceType, Integer.valueOf(resourceQuantity));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        return name;
    }
}
