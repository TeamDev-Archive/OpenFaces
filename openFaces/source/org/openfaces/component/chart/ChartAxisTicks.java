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
public class ChartAxisTicks extends UIOutput implements StyledComponent, HasLabels {
    private ChartLabels labels;
    private String style;

    public ChartAxisTicks() {
        setRendererType(null);
    }

    @Override
    public void setParent(UIComponent parent) {
        super.setParent(parent);
        if (this.getParent() instanceof ChartAxis) {
            ChartAxis axis = (ChartAxis) this.getParent();
            axis.setTicks(this);
        }
    }

    public ChartLabels getLabels() {
        return labels;
    }

    public void setLabels(ChartLabels labels) {
        this.labels = labels;
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
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, style,
                saveAttachedState(context, labels)
        };
    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;

        super.restoreState(facesContext, state[i++]);
        style = (String) state[i++];
        labels = (ChartLabels) restoreAttachedState(facesContext, state[i++]);
    }
}
