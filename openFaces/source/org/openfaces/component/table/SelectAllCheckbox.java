/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.table;

import javax.faces.component.UIComponentBase;

/**
 * @author Dmitry Pikhulya
 */
public class SelectAllCheckbox extends UIComponentBase {
    public static final String COMPONENT_TYPE = "org.openfaces.SelectAllCheckbox";
    public static final String COMPONENT_FAMILY = "org.openfaces.SelectAllCheckbox";

    public SelectAllCheckbox() {
        setRendererType("org.openfaces.SelectAllCheckboxRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

}
