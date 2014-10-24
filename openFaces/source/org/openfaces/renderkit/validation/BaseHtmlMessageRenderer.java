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
package org.openfaces.renderkit.validation;

import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.util.Rendering;
import org.openfaces.util.Styles;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.UIMessage;
import javax.faces.component.html.HtmlMessage;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.Iterator;

/**
 * @author Vladimir Korenev
 */
public abstract class BaseHtmlMessageRenderer extends BaseMessageRenderer {
    public static final String DIR_ATTR = "dir";
    public static final String LANG_ATTR = "lang";
    public static final String ONCLICK_ATTR = "onclick";
    public static final String ONDBLCLICK_ATTR = "ondblclick";
    public static final String ONMOUSEDOWN_ATTR = "onmousedown";
    public static final String ONMOUSEUP_ATTR = "onmouseup";
    public static final String ONMOUSEOVER_ATTR = "onmouseover";
    public static final String ONMOUSEMOVE_ATTR = "onmousemove";
    public static final String ONMOUSEOUT_ATTR = "onmouseout";
    public static final String ONKEYPRESS_ATTR = "onkeypress";
    public static final String ONKEYDOWN_ATTR = "onkeydown";
    public static final String ONKEYUP_ATTR = "onkeyup";
    public static final String ERRORSTYLE_ATTR = "errorStyle";
    public static final String ERRORCLASS_ATTR = "errorClass";
    public static final String INFOSTYLE_ATTR = "infoStyle";
    public static final String INFOCLASS_ATTR = "infoClass";
    public static final String FATALSTYLE_ATTR = "fatalStyle";
    public static final String FATALCLASS_ATTR = "fatalClass";
    public static final String WARNSTYLE_ATTR = "warnStyle";
    public static final String WARNCLASS_ATTR = "warnClass";

    private static final String[] MESSAGE_PASSTHROUGH_ATTRIBUTES_WITHOUT_TITLE_STYLE_AND_STYLE_CLASS = {
            DIR_ATTR,
            LANG_ATTR,
            ONCLICK_ATTR,
            ONDBLCLICK_ATTR,
            ONMOUSEDOWN_ATTR,
            ONMOUSEUP_ATTR,
            ONMOUSEOVER_ATTR,
            ONMOUSEMOVE_ATTR,
            ONMOUSEOUT_ATTR,
            ONKEYPRESS_ATTR,
            ONKEYDOWN_ATTR,
            ONKEYUP_ATTR};

    private static final String[] MESSAGE_PASSTHROUGH_ADDITIONAL_STYLES = {
            ERRORSTYLE_ATTR,
            ERRORCLASS_ATTR,
            WARNSTYLE_ATTR,
            WARNCLASS_ATTR,
            FATALSTYLE_ATTR,
            FATALCLASS_ATTR,
            INFOSTYLE_ATTR,
            INFOCLASS_ATTR
    };
    private static final String DISPLAY_NONE_CSS = "display: none;";

    protected void renderMessage(FacesContext facesContext,
                                 UIComponent messageComponent)
            throws IOException {

        UIComponent forComponent = getForComponent(messageComponent);
        if (forComponent == null) {
//            log.error("Could not render Message. Unable to find component '" + forAttr + "' (calling findComponent on component '" + messageComponent.getClientId(facesContext) + "'). If the provided id was correct, wrap the message and its component into an h:panelGroup or h:panelGrid.");
//      return;
        }

        startMessageElement(facesContext, messageComponent);
        Iterator messageIterator = null;
        if (forComponent != null) {
            String clientId = forComponent.getClientId(facesContext);
            messageIterator = facesContext.getMessages(clientId);
        }

        if (messageIterator != null && messageIterator.hasNext()) {
            // get first message
            FacesMessage facesMessage = (FacesMessage) messageIterator.next();

            // and render it
            renderSingleFacesMessage(facesContext, messageComponent, facesMessage);
        } else {
            String[] styleAndClass = getStyleAndStyleClass(messageComponent, FacesMessage.SEVERITY_ERROR);
            String style = Styles.mergeStyles(styleAndClass[0], DISPLAY_NONE_CSS);
            String styleClass = styleAndClass[1];
            ResponseWriter writer = facesContext.getResponseWriter();
            if (!Rendering.isDefaultAttributeValue(style)) {
                Rendering.renderHTMLAttribute(writer, "style", "style", style);
            }
            if (!Rendering.isDefaultAttributeValue(styleClass)) {
                Rendering.renderHTMLAttribute(writer, "styleClass", "styleClass", styleClass);
            }
        }

        endMessageElement(facesContext);
    }


    protected void renderSingleFacesMessage(FacesContext facesContext,
                                            UIComponent messageComponent,
                                            FacesMessage message)
            throws IOException {
        message.rendered();

        // determine style and style class
        String[] styleAndClass = getStyleAndStyleClass(messageComponent, message.getSeverity());
        String style = styleAndClass[0];
        String styleClass = styleAndClass[1];

        String summary = message.getSummary();
        String detail = message.getDetail();

        String title = getTitle(messageComponent);
        boolean tooltip = isTooltip(messageComponent);

        if (title == null && tooltip) {
            title = summary;
        }

        ResponseWriter writer = facesContext.getResponseWriter();

        if (!Rendering.isDefaultAttributeValue(title)) {
            Rendering.renderHTMLAttribute(writer, "title", "title", title);
        }
        if (!Rendering.isDefaultAttributeValue(style)) {
            Rendering.renderHTMLAttribute(writer, "style", "style", style);
        }
        if (!Rendering.isDefaultAttributeValue(styleClass)) {
            Rendering.renderHTMLAttribute(writer, "styleClass", "styleClass", styleClass);
        }


        boolean showSummary = isShowSummary(messageComponent) && (summary != null);
        boolean showDetail = isShowDetail(messageComponent) && (detail != null);

        String escapeStr = (String) messageComponent.getAttributes().get("escape");
        boolean escape = escapeStr != null && !Boolean.valueOf(escapeStr);

        if (showSummary && !(title == null && tooltip)) {
            if (escape) {
                writer.write(summary);
            } else {
                writer.writeText(summary, null);
            }
            if (showDetail) {
                writer.writeText(" ", null);
            }
        }

        if (showDetail) {
             if (escape) {
                writer.write(detail);
            } else {
                writer.writeText(detail, null);
            }
        }
    }

    private void endMessageElement(FacesContext facesContext) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.endElement("span");
    }

    private void startMessageElement(FacesContext facesContext, UIComponent messageComponent) throws IOException {
        ResponseWriter writer = facesContext.getResponseWriter();
        writer.startElement("span", messageComponent);
        writer.writeAttribute("id", messageComponent.getClientId(facesContext), null);

        for (String attrName : MESSAGE_PASSTHROUGH_ATTRIBUTES_WITHOUT_TITLE_STYLE_AND_STYLE_CLASS) {
            Object value = messageComponent.getAttributes().get(attrName);
            Rendering.renderHTMLAttribute(writer, attrName, attrName, value);
        }
    }

    protected String[] getStyleAndStyleClass(UIComponent message,
                                             FacesMessage.Severity severity) {
        String style = null;
        String styleClass = null;
        if (severity == FacesMessage.SEVERITY_INFO) {
            style = getInfoStyle(message);
            styleClass = getInfoClass(message);
        } else if (severity == FacesMessage.SEVERITY_WARN) {
            style = getWarnStyle(message);
            styleClass = getWarnClass(message);
        } else if (severity == FacesMessage.SEVERITY_ERROR) {
            style = getErrorStyle(message);
            styleClass = getErrorClass(message);
        } else if (severity == FacesMessage.SEVERITY_FATAL) {
            style = getFatalStyle(message);
            styleClass = getFatalClass(message);
        }

        if (style == null) {
            style = getStyle(message);
        }

        if (styleClass == null) {
            styleClass = getStyleClass(message);
        }

        Styles.checkCSSStyleForSemicolon(style);

        return new String[]{style, styleClass};
    }

    protected String getInfoStyle(UIComponent component) {
        if (component instanceof HtmlMessage) {
            return ((HtmlMessage) component).getInfoStyle();
        } else {
            return (String) component.getAttributes().get("infoStyle");
        }
    }

    protected String getInfoClass(UIComponent component) {
        if (component instanceof HtmlMessage) {
            return ((HtmlMessage) component).getInfoClass();
        } else {
            return (String) component.getAttributes().get("infoClass");
        }
    }

    protected String getWarnStyle(UIComponent component) {
        if (component instanceof HtmlMessage) {
            return ((HtmlMessage) component).getWarnStyle();
        } else {
            return (String) component.getAttributes().get("warnStyle");
        }
    }

    protected String getWarnClass(UIComponent component) {
        if (component instanceof HtmlMessage) {
            return ((HtmlMessage) component).getWarnClass();
        } else {
            return (String) component.getAttributes().get("warnClass");
        }
    }

    protected String getErrorStyle(UIComponent component) {
        if (component instanceof HtmlMessage) {
            return ((HtmlMessage) component).getErrorStyle();
        } else {
            return (String) component.getAttributes().get("errorStyle");
        }
    }

    protected String getErrorClass(UIComponent component) {
        if (component instanceof HtmlMessage) {
            return ((HtmlMessage) component).getErrorClass();
        } else {
            return (String) component.getAttributes().get("errorClass");
        }
    }

    protected String getFatalStyle(UIComponent component) {
        if (component instanceof HtmlMessage) {
            return ((HtmlMessage) component).getFatalStyle();
        } else {
            return (String) component.getAttributes().get("fatalStyle");
        }
    }

    protected String getFatalClass(UIComponent component) {
        if (component instanceof HtmlMessage) {
            return ((HtmlMessage) component).getFatalClass();
        } else {
            return (String) component.getAttributes().get("fatalClass");
        }
    }

    protected String getStyle(UIComponent component) {
        if (component instanceof HtmlMessage) {
            return ((HtmlMessage) component).getStyle();
        } else {
            return (String) component.getAttributes().get("style");
        }
    }

    protected String getStyleClass(UIComponent component) {
        if (component instanceof HtmlMessage) {
            return ((HtmlMessage) component).getStyleClass();
        } else {
            return (String) component.getAttributes().get("styleClass");
        }
    }

    protected String getTitle(UIComponent component) {
        if (component instanceof HtmlMessage) {
            return ((HtmlMessage) component).getTitle();
        } else {
            return (String) component.getAttributes().get("title");
        }
    }

    protected boolean isTooltip(UIComponent component) {
        if (component instanceof HtmlMessage) {
            return ((HtmlMessage) component).isTooltip();
        } else {
            return Rendering.getBooleanAttribute(component, "tooltip", false);
        }
    }

    protected boolean isShowSummary(UIComponent component) {
        if (component instanceof UIMessage) {
            return ((UIMessage) component).isShowSummary();
        } else {
            return Rendering.getBooleanAttribute(component, "showSummary", false);
        }
    }

    protected boolean isShowDetail(UIComponent component) {
        if (component instanceof UIMessage) {
            return ((UIMessage) component).isShowDetail();
        } else {
            return Rendering.getBooleanAttribute(component, "showDetail", false);
        }
    }

    protected final JSONObject getAdditionalParams(UIComponent messageComponent) {
        JSONObject params = new JSONObject();

        boolean atLeastOneParameter = false;
        for (String attrName : MESSAGE_PASSTHROUGH_ATTRIBUTES_WITHOUT_TITLE_STYLE_AND_STYLE_CLASS) {
            Object value = messageComponent.getAttributes().get(attrName);
            if (value instanceof String) {
                atLeastOneParameter = true;
                try {
                    params.put(attrName, value);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return atLeastOneParameter ? params : null;
    }

    protected final JSONObject getAdditionalStyles(UIComponent messageComponent) {
        JSONObject params = new JSONObject();

        boolean atLeastOneParameter = false;
        for (String attrName : MESSAGE_PASSTHROUGH_ADDITIONAL_STYLES) {
            Object value = messageComponent.getAttributes().get(attrName);
            if (value instanceof String) {
                atLeastOneParameter = true;
                try {
                    params.put(attrName, value);
                } catch (JSONException e) {
                    throw new RuntimeException(e);
                }
            }
        }

        return atLeastOneParameter ? params : null;
    }
}
