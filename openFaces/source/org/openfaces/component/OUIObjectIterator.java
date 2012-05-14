/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component;

import javax.faces.component.NamingContainer;

/**
 * This interface denotes the components those multiply their child components to display multiple entries of an
 * associated data collection. The way of specifying collection is component-specific and is not defined by this
 * interface.
 * <p/>
 * This interface defines a notion of current object Id with its <code>objectId</code> attribute. The semantics of
 * handling the current object is specific to each particular component, and it is typically used to refer to a data
 * entry currently being rendered, or an entry that generated the server action that is being processed.
 * <p/>
 * This interface extends the NamingContainer interface because all components that are rendered multiple times are
 * expected to have a compound ID based on ID of the main component (a component implementing this interface). E.g. if
 * there's a multiplied component with id "myOutputText" in a UIObjectIterator component with a client id of
 * "myIterator1", child component's client id should be formatted as follows: "myIterator1:objectId:myOutputText".
 *
 * @author Dmitry Pikhulya
 */
public interface OUIObjectIterator extends OUIComponent, NamingContainer {
    public static String OBJECT_ID_SEPARATOR = "::";

    /**
     * Selects an object with the specified id in this component. The semantics of handling the current object is specific
     * to each particular component, and the current object typically points to a data entry currently being rendered, or
     * an entry that generated the server action that is being processed.
     *
     * @param objectId id of an object to be selected
     */
    void setObjectId(String objectId);

    /**
     * @return the identifier of the object currently selected in this component.
     * @see #setObjectId
     */
    String getObjectId();
}
