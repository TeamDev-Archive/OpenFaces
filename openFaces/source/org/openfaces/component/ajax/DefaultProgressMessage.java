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
package org.openfaces.component.ajax;

import org.openfaces.component.HorizontalAlignment;
import org.openfaces.component.VerticalAlignment;
import org.openfaces.util.Resources;
import org.openfaces.util.ValueBindings;

import javax.faces.application.ResourceDependencies;
import javax.faces.application.ResourceDependency;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;

/**
 * @author Eugene Goncharov
 */
@ResourceDependencies({
        @ResourceDependency(name = "default.css", library = "openfaces")
})
public class DefaultProgressMessage extends ProgressMessage {
    public static final String COMPONENT_TYPE = "org.openfaces.DefaultProgressMessage";
    public static final String COMPONENT_FAMILY = "org.openfaces.DefaultProgressMessage";

    private static final String DEFAULT_AJAX_MESSAGE_TEXT = "Loading...";
    private static final String DEFAULT_AJAX_IN_PROGRESS_MESSAGE_HTML =
            "<table class=''o_ajax_message {0}'' style=''{1}'' cellpadding=''0'' cellspacing=''0''>" +
                    "<tr><td valign=''middle'' style=''padding-right: 5px''><img src=''{2}''/></td><td valign=''middle''>{3}</td></tr></table>";

    private String ajaxMessage;

    private String text;
    private String imageUrl;
    private String style;
    private String styleClass;
    private HorizontalAlignment horizontalAlignment;
    private VerticalAlignment verticalAlignment;
    private Double transparency;
    private Integer transparencyTransitionPeriod;
    private Boolean fillBackground;
    private Double backgroundTransparency;
    private Integer backgroundTransparencyTransitionPeriod;
    private String backgroundStyle;
    private String backgroundClass;
    private ProgressMessageMode mode;

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public DefaultProgressMessage() {
        setRendererType("org.openfaces.DefaultProgressMessageRenderer");
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState,
                text,
                imageUrl,
                style,
                styleClass,
                horizontalAlignment,
                verticalAlignment,
                transparency,
                transparencyTransitionPeriod,
                fillBackground,
                backgroundTransparency,
                backgroundTransparencyTransitionPeriod,
                backgroundStyle,
                backgroundClass,
                mode
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        int i = 0;
        super.restoreState(context, stateArray[i++]);
        text = (String) stateArray[i++];
        imageUrl = (String) stateArray[i++];
        style = (String) stateArray[i++];
        styleClass = (String) stateArray[i++];
        horizontalAlignment = (HorizontalAlignment) stateArray[i++];
        verticalAlignment = (VerticalAlignment) stateArray[i++];
        transparency = (Double) stateArray[i++];
        transparencyTransitionPeriod = (Integer) stateArray[i++];
        fillBackground = (Boolean) stateArray[i++];
        backgroundTransparency = (Double) stateArray[i++];
        backgroundTransparencyTransitionPeriod = (Integer) stateArray[i++];
        backgroundStyle = (String) stateArray[i++];
        backgroundClass = (String) stateArray[i++];
        mode = (ProgressMessageMode) stateArray[i++];
    }

    public String getText() {
        return ValueBindings.get(this, "text", text);
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getImageUrl() {
        return ValueBindings.get(this, "imageUrl", imageUrl);
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getStyle() {
        return ValueBindings.get(this, "style", style);
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public String getStyleClass() {
        return ValueBindings.get(this, "styleClass", styleClass);
    }

    public void setStyleClass(String styleClass) {
        this.styleClass = styleClass;
    }

    public String getAjaxMessageHTML() {
        return getAjaxMessageHTML(getStyle(), getStyleClass(), getText(), getImageUrl());
    }

    private String getAjaxMessageHTML(String style, String styleClass, String text, String imageUrl) {
        FacesContext context = FacesContext.getCurrentInstance();
        if (style == null) {
            style = " ";
        }
        if (styleClass == null) {
            styleClass = " ";
        }
        if (text == null) {
            text = DEFAULT_AJAX_MESSAGE_TEXT;
        }

        ExternalContext externalContext = context.getExternalContext();

        if (imageUrl == null) {
            imageUrl = Resources.internalURL(context, "loading.gif");
        } else {
            imageUrl = Resources.applicationURL(context, imageUrl);
        }

        if (ajaxMessage == null) {
            ajaxMessage = externalContext.getInitParameter("org.openfaces.ajaxMessageHTML");
            if (ajaxMessage == null) {
                ajaxMessage = MessageFormat.format(DEFAULT_AJAX_IN_PROGRESS_MESSAGE_HTML, styleClass, style, imageUrl, text);
            }
        }
        return ajaxMessage;
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return ValueBindings.get(this, "horizontalAlignment", horizontalAlignment, HorizontalAlignment.RIGHT, HorizontalAlignment.class);
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public VerticalAlignment getVerticalAlignment() {
        return ValueBindings.get(this, "verticalAlignment", verticalAlignment, VerticalAlignment.TOP, VerticalAlignment.class);
    }

    public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public double getTransparency() {
        return ValueBindings.get(this, "transparency", transparency, 0);
    }

    public void setTransparency(double transparency) {
        this.transparency = transparency;
    }

    public int getTransparencyTransitionPeriod() {
        return ValueBindings.get(this, "transparencyTransitionPeriod", transparencyTransitionPeriod, 0);
    }

    public void setTransparencyTransitionPeriod(int transparencyTransitionPeriod) {
        this.transparencyTransitionPeriod = transparencyTransitionPeriod;
    }

    public boolean getFillBackground() {
        return ValueBindings.get(this, "fillBackground", fillBackground, false);
    }

    public void setFillBackground(boolean fillBackground) {
        this.fillBackground = fillBackground;
    }

    public double getBackgroundTransparency() {
        return ValueBindings.get(this, "backgroundTransparency", backgroundTransparency, 0.6);
    }

    public void setBackgroundTransparency(double backgroundTransparency) {
        this.backgroundTransparency = backgroundTransparency;
    }

    public int getBackgroundTransparencyTransitionPeriod() {
        return ValueBindings.get(this, "backgroundTransparencyTransitionPeriod", backgroundTransparencyTransitionPeriod, 150);
    }

    public void setBackgroundTransparencyTransitionPeriod(int backgroundTransparencyTransitionPeriod) {
        this.backgroundTransparencyTransitionPeriod = backgroundTransparencyTransitionPeriod;
    }

    public String getBackgroundStyle() {
        return ValueBindings.get(this, "backgroundStyle", backgroundStyle);
    }

    public void setBackgroundStyle(String backgroundStyle) {
        this.backgroundStyle = backgroundStyle;
    }

    public String getBackgroundClass() {
        return ValueBindings.get(this, "backgroundClass", backgroundClass);
    }

    public void setBackgroundClass(String backgroundClass) {
        this.backgroundClass = backgroundClass;
    }

    public ProgressMessageMode getMode() {
        return ValueBindings.get(this, "mode", mode, ProgressMessageMode.ALL, ProgressMessageMode.class);
    }

    public void setMode(ProgressMessageMode mode) {
        this.mode = mode;
    }
}
