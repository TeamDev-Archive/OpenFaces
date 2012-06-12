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
package org.openfaces.component.table.impl;

import org.openfaces.component.ContextDependentComponent;
import org.openfaces.component.table.SyntheticColumn;

import javax.faces.component.UIComponent;
import java.util.Map;

/**
 * This interface is only for internal usage from within the OpenFaces library. It shouldn't be used explicitly by any
 * application code.
 */
public interface DynamicCol extends SyntheticColumn, ContextDependentComponent {
    Map<String, UIComponent> getFacetsForProcessing();
}
