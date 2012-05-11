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
package org.openfaces.component.output;

import org.openfaces.component.OUIOutput;
import org.openfaces.util.ValueBindings;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.cssparser.StyleObjectModel;
import org.openfaces.renderkit.cssparser.StyledComponent;
import org.openfaces.renderkit.output.GraphicTextDefaultStyle;

import javax.faces.context.FacesContext;

/**
 *
 * GraphicText is a component that displays styled rotated text. It has API similar to the
 * standard <h:outputText> component, though unlike the <h:outputText> component, the GraphicText
 * component displays a text with an image generated on the server. This component can be useful
 * for displaying vertical headers in a table having a lot of thin columns, vertical TabSet
 * components, etc.
 * 
 * @author Darya Shumilina
 */
public class GraphicText extends OUIOutput implements StyledComponent {
    public static final String COMPONENT_TYPE = "org.openfaces.GraphicText";
    public static final String COMPONENT_FAMILY = "org.openfaces.GraphicText";

    private static final StyledComponent DEFAULT_STYLE = new GraphicTextDefaultStyle();

    public static final int LEFT_TO_RIGHT = 0;
    public static final int RIGHT_TO_LEFT = 180;
    public static final int TOP_TO_BOTTOM = 270;
    public static final int BOTTOM_TO_TOP = 90;

    private String textStyle;
    private Object value;
    private String title;
    private String lang;
    private Integer direction;

    public GraphicText() {
        setRendererType("org.openfaces.GraphicTextRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getTextStyle() {
        return ValueBindings.get(this, "textStyle", textStyle);
    }

    public void setTextStyle(String textStyle) {
        this.textStyle = textStyle;
    }

    public String getHint() {
        return null;
    }

    public Object getValue() {
        return ValueBindings.get(this, "value", value, Object.class);
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public int getDirection() {
        return ValueBindings.get(this, "direction", direction, 0);
    }

    public void setDirection(int direction) {
        this.direction = direction;
    }

    public String getTitle() {
        return ValueBindings.get(this, "title", title);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLang() {
        return ValueBindings.get(this, "lang", lang);
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public StyleObjectModel getStyleObjectModel() {
        return CSSUtil.getStyleObjectModel(getComponentsChain());
    }

    public StyledComponent[] getComponentsChain() {
        StyledComponent[] chain = new StyledComponent[2];
        chain[0] = GraphicText.DEFAULT_STYLE;
        chain[1] = this;
        return chain;
    }

    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{
                superState,
                textStyle,
                value,
                direction,
                title,
                lang
        };
    }

    public void restoreState(FacesContext context, Object object) {
        Object[] values = (Object[]) object;
        int i = 0;
        super.restoreState(context, values[i++]);
        textStyle = (String) values[i++];
        value = values[i++];
        direction = (Integer) values[i++];
        title = (String) values[i++];
        lang = (String) values[i++];
    }

}