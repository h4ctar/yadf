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
package simulation.item;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import org.w3c.dom.Element;

/**
 * The Class ItemType.
 */
public class ItemType implements Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = -2274409976593807781L;

    /** The name. */
    public final String name;

    /** The category. */
    public final String category;

    /** The weight. */
    public final int weight;

    /** The placeable. */
    public final boolean placeable;

    /** The sprite. */
    public final int sprite;

    /** How many content items this item can hold if it's a container, 0 if it's not a container. */
    public final int capacity;

    /** The types of items that can be stored if it's a container. */
    public final Set<String> contentItemTypeNames = new HashSet<>();

    /**
     * Instantiates a new item type.
     * 
     * @param itemTypeElement the item type element
     * @param categoryTmp the category that the item is in
     */
    public ItemType(final Element itemTypeElement, final String categoryTmp) {
        category = categoryTmp;
        String tempString = itemTypeElement.getAttribute("sprite");
        sprite = "".equals(tempString) ? 0 : Integer.parseInt(tempString);
        name = itemTypeElement.getAttribute("name");
        weight = Integer.parseInt(itemTypeElement.getAttribute("weight"));
        placeable = Boolean.parseBoolean(itemTypeElement.getAttribute("placeable"));
        tempString = itemTypeElement.getAttribute("capacity");
        capacity = "".equals(tempString) ? 0 : Integer.parseInt(tempString);
        tempString = itemTypeElement.getAttribute("contentItemTypes");
        for (String contentItemTypeName : tempString.split(",")) {
            contentItemTypeNames.add(contentItemTypeName);
        }
    }

    @Override
    public boolean equals(final Object other) {
        if (other instanceof ItemType) {
            return ((ItemType) other).name.equals(name);
        }

        if (other instanceof String) {
            return ((String) other).equals(name);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public String toString() {
        return name;
    }
}
