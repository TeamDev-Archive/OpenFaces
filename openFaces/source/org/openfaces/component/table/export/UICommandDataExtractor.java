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

package org.openfaces.component.table.export;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;

/**
 * @author Natalia.Zolochevska@Teamdev.com
 */
public class UICommandDataExtractor implements ComponentDataExtractor {

    public boolean isApplicableFor(UIComponent component) {
        return component instanceof UICommand;
    }

    public Object getData(UIComponent component) {
        UICommand uiCommand = (UICommand) component;
        return uiCommand.getValue();
    }
}
