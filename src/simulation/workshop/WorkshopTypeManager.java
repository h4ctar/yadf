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

import java.io.InputStream;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The Class WorkshopTypeManager.
 */
public final class WorkshopTypeManager {

    /** The instance. */
    private static WorkshopTypeManager instance;

    /**
     * Gets the single instance of WorkshopTypeManager.
     * 
     * @return single instance of WorkshopTypeManager
     */
    public static WorkshopTypeManager getInstance() {
        if (instance == null) {
            instance = new WorkshopTypeManager();
        }
        return instance;
    }

    /** The workshop types. */
    private Map<String, WorkshopType> workshopTypes;

    /**
     * Instantiates a new workshop type manager.
     */
    private WorkshopTypeManager() {

    }

    /**
     * Gets the workshop type.
     * 
     * @param workshopTypeName the workshop type name
     * @return the workshop type
     */
    public WorkshopType getWorkshopType(final String workshopTypeName) {
        return workshopTypes.get(workshopTypeName);
    }

    /**
     * Gets the workshop types.
     * 
     * @return the workshop types
     */
    public Collection<WorkshopType> getWorkshopTypes() {
        return workshopTypes.values();
    }

    /**
     * Load.
     * 
     * @throws Exception the exception
     */
    public void load() throws Exception {
        workshopTypes = new HashMap<>();
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream("workshop_types.xml");
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
        Document document = documentBuilder.parse(inputStream);
        NodeList workshopTypeNodes = document.getElementsByTagName("workshopType");
        for (int i = 0; i < workshopTypeNodes.getLength(); i++) {
            Node workshopTypeNode = workshopTypeNodes.item(i);
            Element workshopTypeElement = (Element) workshopTypeNode;
            WorkshopType workshopType = new WorkshopType(workshopTypeElement);
            workshopTypes.put(workshopType.name, workshopType);
        }
    }
}
