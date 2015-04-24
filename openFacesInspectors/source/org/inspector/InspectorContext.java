/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.inspector;


import org.inspector.webriver.PropertyTestConfiguration;

/**
 * @author Max Yurin
 */
public class InspectorContext {
    private static PropertyTestConfiguration properties;

    public static void createInstance(){
        properties = new PropertyTestConfiguration();
    }

    public static PropertyTestConfiguration getProperties() {
        return properties;
    }
}
