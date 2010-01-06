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

package org.openfaces.demo.beans.tabset;

import java.io.Serializable;

public class Component implements Serializable {

    private String componentImageUrl;
    private String componentName;
    private String componentInfo;

    public Component(String componentImageUrl, String componentName, String componentInfo) {
        this.componentImageUrl = componentImageUrl;
        this.componentName = componentName;
        this.componentInfo = componentInfo;
    }

    public String getComponentImageUrl() {
        return componentImageUrl;
    }

    public String getComponentName() {
        return componentName;
    }

    public String getComponentInfo() {
        return componentInfo;
    }
}
