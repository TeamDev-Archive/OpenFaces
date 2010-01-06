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
package org.openfaces.renderkit.chart;

import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;
import org.openfaces.renderkit.cssparser.StyledComponent;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartDefaultStyle implements StyledComponent {

    private String style = "background: white; font-family: Verdana; font-size:10px; color:gray; font-style:normal; font-weight:normal;";

    public String getTextStyle() {
        return style;
    }

    public void setTextStyle(String style) {
        this.style = style;
    }

    public void setStyle(String style) {
        setTextStyle(style);
    }

    public String getStyle() {
        return getTextStyle();
    }

    public StyleObjectModel getStyleObjectModel() {
        return CSSUtil.getStyleObjectModel(getComponentsChain());
    }

    public StyledComponent[] getComponentsChain() {
        return new StyledComponent[]{this};
    }

    public String getHint() {
        return null;
    }
}
