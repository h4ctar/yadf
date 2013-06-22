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
package simulation.labor;

import java.io.Serializable;

import org.w3c.dom.Element;

import simulation.item.ItemType;
import simulation.item.ItemTypeManager;

/**
 * The Class LaborType.
 */
public class LaborType implements Serializable {

    /** The serial version UID. */
    private static final long serialVersionUID = 1205819995595594739L;

    /** The name. */
    public final String name;

    /** The profession name. */
    public final String professionName;

    /** The type of tool required for this labor. */
    public final ItemType toolType;

    /** The index. */
    public final int sprite;

    /**
     * Constructor for a labor type.
     * @param laborTypeElement the DOM element to get the attributes from
     * @throws Exception something went wrong
     */
    public LaborType(final Element laborTypeElement) throws Exception {
        name = laborTypeElement.getAttribute("name");
        professionName = laborTypeElement.getAttribute("professionName");
        sprite = Integer.parseInt(laborTypeElement.getAttribute("sprite"));
        String toolTypeName = laborTypeElement.getAttribute("tool");
        if (!"".equals(toolTypeName)) {
            toolType = ItemTypeManager.getInstance().getItemType(toolTypeName);
            if (toolType == null) {
                throw new Exception("Item type does not exist: " + toolTypeName);
            }
        } else {
            toolType = null;
        }
    }

    @Override
    public String toString() {
        return name;
    }
}
