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
package org.openfaces.component.chart;

import org.openfaces.util.ValueBindings;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;
import org.openfaces.renderkit.cssparser.StyledComponent;

import javax.faces.component.UICommand;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.event.FacesEvent;

/**
 * @author Ekaterina Shliakhovetskaya
 */
public class ChartTitle extends UICommand implements StyledComponent {
    private String text;
    private String style;
    private String url;
    private String tooltip;

    public ChartTitle() {
        setRendererType(null);
    }

    public String getTooltip() {
        return ValueBindings.get(this, "tooltip", tooltip);
    }

    public void setTooltip(String tooltip) {
        this.tooltip = tooltip;
    }

    public String getUrl() {
        return ValueBindings.get(this, "url", url);
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getText() {
        return ValueBindings.get(this, "text", text);
    }

    public void setText(String text) {
        this.text = text;
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

    @Override
    public void setParent(UIComponent uiComponent) {
        super.setParent(uiComponent);
        if (this.getParent() instanceof Chart) {
            Chart chart = (Chart) this.getParent();
            chart.setTitle(this);
        }
    }

    public StyleObjectModel getStyleObjectModel() {
        return CSSUtil.getStyleObjectModel(getComponentsChain());
    }

    public StyledComponent[] getComponentsChain() {
        StyledComponent[] chain = new StyledComponent[3];
        chain[0] = Chart.DEFAULT_CHART_STYLE;
        chain[1] = (StyledComponent) this.getParent();
        chain[2] = this;
        return chain;
    }

    public String getHint() {
        return null;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                text,
                style,
                url,
                tooltip,
        };

    }

    @Override
    public void restoreState(FacesContext facesContext, Object object) {
        Object[] state = (Object[]) object;
        int i = 0;
        Chart chart = (Chart) this.getParent();
        chart.setTitle(this);
        super.restoreState(facesContext, state[i++]);
        text = (String) state[i++];
        style = (String) state[i++];
        url = (String) state[i++];
        tooltip = (String) state[i++];
    }

    public FacesEvent decodeAction(String fieldValue) {
        this.queueEvent(new ActionEvent(this));
        return null;
    }


}
