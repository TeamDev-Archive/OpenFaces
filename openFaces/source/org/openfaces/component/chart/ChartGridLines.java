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
package org.openfaces.component.chart;

import org.openfaces.util.ValueBindings;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;
import org.openfaces.renderkit.cssparser.StyledComponent;

import javax.faces.component.UIComponent;
import javax.faces.component.UIOutput;
import javax.faces.context.FacesContext;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartGridLines extends UIOutput implements StyledComponent {
    private String style;
    private Boolean visible;
    private ChartDomain domain = ChartDomain.BOTH;

    public ChartGridLines() {
        setRendererType(null);
    }

    public ChartDomain getDomain() {
        return ValueBindings.get(this, "domain", null, domain, ChartDomain.class);
    }

    public void setDomain(ChartDomain domain) {
        this.domain = domain;
    }

    public boolean isVisible() {
        return ValueBindings.get(this, "visible", visible, true);
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    public String getTextStyle() {
        return ValueBindings.get(this, "style", style);
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
        StyledComponent[] chain = new StyledComponent[1];
        chain[0] = this;
        return chain;
    }

    public String getHint() {
        return "border";
    }

    @Override
    public void setParent(UIComponent parent) {
        super.setParent(parent);
        if (this.getParent() instanceof ChartView) {
            GridChartView view = (GridChartView) this.getParent();
            view.getGridLines().add(this);
        }

    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, style,
                visible,
                saveAttachedState(context, domain)};

    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        super.restoreState(facesContext, state[i++]);
        style = (String) state[i++];
        visible = (Boolean) state[i++];
        domain = (ChartDomain) restoreAttachedState(facesContext, state[i++]);

    }

}
