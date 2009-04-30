/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.component.ajax;

import org.openfaces.component.ValueBindings;
import org.openfaces.util.ResourceUtil;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;

/**
 * @author Eugene Goncharov
 */
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

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public DefaultProgressMessage() {
        setRendererType("org.openfaces.DefaultProgressMessageRenderer");
    }

    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, text, imageUrl, style, styleClass};
    }

    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        super.restoreState(context, stateArray[0]);
        text = (String) stateArray[1];
        imageUrl = (String) stateArray[2];
        style = (String) stateArray[3];
        styleClass = (String) stateArray[4];
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
            imageUrl = ResourceUtil.getInternalResourceURL(context, DefaultProgressMessage.class, "loading.gif");
        } else {
            imageUrl = ResourceUtil.getApplicationResourceURL(context, imageUrl);
        }

        if (ajaxMessage == null) {
            ajaxMessage = externalContext.getInitParameter("org.openfaces.ajaxMessageHTML");
            if (ajaxMessage == null) {
                ajaxMessage = MessageFormat.format(DEFAULT_AJAX_IN_PROGRESS_MESSAGE_HTML, styleClass, style, imageUrl, text);
            }
        }
        return ajaxMessage;
    }

}
