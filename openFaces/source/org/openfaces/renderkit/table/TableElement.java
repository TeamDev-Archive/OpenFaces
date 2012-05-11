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
package org.openfaces.renderkit.table;

import javax.faces.context.FacesContext;
import java.io.IOException;

/**
 * @author Dmitry Pikhulya
 */
public abstract class TableElement {
    private TableElement parent;

    protected TableElement() {
    }

    protected TableElement(TableElement parent) {
        this.parent = parent;
    }

    public abstract void render(FacesContext context, HeaderCell.AdditionalContentWriter additionalContentWriter) throws IOException;

    public TableElement getParent() {
        return parent;
    }

    public void setParent(TableElement parent) {
        this.parent = parent;
    }

    public <T extends TableElement> T getParent(Class<T> c) {
        if (parent == null) return null;
        if (c.isAssignableFrom(parent.getClass()))
            return (T) parent;
        else
            return parent.getParent(c);
    }
}
