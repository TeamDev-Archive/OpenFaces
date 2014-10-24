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

import javax.faces.component.UIComponent;
import java.lang.reflect.Field;

/**
 * @author Natalia.Zolochevska@Teamdev.com
 */
public class UIInstructionsDataExtractor implements ComponentDataExtractor{

    private static final String UI_INSTRUCTIONS_CLASS_NAME = "com.sun.faces.facelets.compiler.UIInstructions";

    public boolean isApplicableFor(UIComponent component) {
        return component.getClass().getName().equals(UI_INSTRUCTIONS_CLASS_NAME);
    }

    public Object getData(UIComponent component) {
        try {
            Class uiInstructionsClass = component.getClass();
            Field txtField = uiInstructionsClass.getDeclaredField("txt");
            txtField.setAccessible(true);
            return String.valueOf(txtField.get(component));
        } catch (Exception e) {
            return "";
        }
    }
}