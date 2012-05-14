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

import javax.faces.component.EditableValueHolder;

/**
 * This interface allows components with an "editable state" to handle their state properly inside of iteration components
 * such <o:dataTable> or <o:forEach>. The "editable state" components in this case are components that are not UIInput
 * components by themselves but that have some of their properties editable form the client-side UI. To achieve this,
 * the getSubmittedValue/setSubmittedValue methods of this interface must be implemented and all of the editable state should
 * be saved inside of the value object managed by these methods.
 * <p>
 * Note: this interface has to extend the EditableValueHolder interface to ensure its functionality since the standard
 * UIData iterators (such as the DataTable component) ensure proper editable state handling only for this interface. So
 * since only the "submittedValue" property from EditableValueHolder is used by implementations of interface, all other functions
 * have to be implemented but they don't carry any functionality and are not expected to be used.
 *
 * @author Dmitry Pikhulya
 */
public interface EditableStateHolder extends EditableValueHolder {
    /**
     * @return the object that carries the editable state of the component. This editable state value is automatically
     * handled by the iteration components and shouldn't be used directly by application code.
     */
    Object getSubmittedValue();

    /**
     * Sets the editable state value for this component. This method is invoked by the iteration components to restore
     * the value that was previously received with getValue method. This method shouldn't be used directly by
     * application code.
     */
    void setSubmittedValue(Object value);
}
