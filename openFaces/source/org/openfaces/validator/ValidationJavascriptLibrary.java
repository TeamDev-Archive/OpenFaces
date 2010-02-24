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
package org.openfaces.validator;

import org.openfaces.util.Resources;

import javax.faces.context.FacesContext;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ValidationJavascriptLibrary {
    private String name;
    private Class ownerClass;

    public ValidationJavascriptLibrary(String name, Class ownerClass) {
        this.name = name;
        this.ownerClass = ownerClass;
    }

    public Class getOwnerClass() {
        return ownerClass;
    }

    public void setOwnerClass(Class ownerClass) {
        this.ownerClass = ownerClass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl(FacesContext context) {
        return Resources.getInternalURL(context, ownerClass, name);
    }
}
