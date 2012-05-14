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
package org.openfaces.taglib.jsp.output;

import org.openfaces.taglib.internal.output.DynamicImageTag;
import org.openfaces.taglib.jsp.AbstractComponentJspTag;

import javax.el.ValueExpression;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class DynamicImageJspTag extends AbstractComponentJspTag {

    public DynamicImageJspTag() {
        super(new DynamicImageTag());
    }

    public void setData(ValueExpression data) {
        getDelegate().setPropertyValue("data", data);
    }

    public void setWidth(ValueExpression width) {
        getDelegate().setPropertyValue("width", width);
    }

    public void setHeight(ValueExpression height) {
        getDelegate().setPropertyValue("height", height);
    }

    public void setAlt(ValueExpression alt) {
        getDelegate().setPropertyValue("alt", alt);
    }

    public void setMapId(ValueExpression mapId) {
        getDelegate().setPropertyValue("mapId", mapId);
    }

    public void setMap(ValueExpression map) {
        getDelegate().setPropertyValue("map", map);
    }

}
