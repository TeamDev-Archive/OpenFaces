/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.renderkit.timetable;

import org.openfaces.component.timetable.ScrollButton;
import org.openfaces.org.json.JSONObject;
import org.openfaces.renderkit.TableUtil;
import org.openfaces.util.InitScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;

public class ScrollButtonRenderer extends org.openfaces.renderkit.RendererBase {

    private static String BUTTON_SUFFIX = "::button";



    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        super.encodeBegin(context, component);
        encodeButton(context, component, "up");
    }

    protected void encodeButton(FacesContext context, UIComponent component, String buttonOrientation ) throws IOException {
        System.out.println("Encoding Button" + component.getParent());
        FacesContext currentInstance = FacesContext.getCurrentInstance();

        ScrollButton scrollButton = (ScrollButton) component;

        ResponseWriter writer = context.getResponseWriter();

        writeIdAttribute(context,scrollButton);
        writer.writeAttribute("nowrap", "nowrap", null);

        writer.writeAttribute("align", "center", null);
        writer.writeAttribute("valign", "middle", null);

        String imageUrl;
        String buttonImageUrl = (String) scrollButton.getAttributes().get("buttonImageUrl");
        if (buttonOrientation.equals("up"))
            imageUrl = Resources.getURL(context, buttonImageUrl, null, "input/dropButton.gif");
        else
            imageUrl = Resources.getURL(context, buttonImageUrl, null, "input/dropButton.gif");
        writer.startElement("img", scrollButton);
        writeIdAttribute(context,scrollButton);
        writer.writeAttribute("src", imageUrl, null);
        writer.endElement("img");
        InitScript initScript = renderInitScript(currentInstance,scrollButton);
        Rendering.renderInitScripts(currentInstance, initScript);

    }

    protected InitScript renderInitScript(FacesContext context, ScrollButton scrollButton) throws IOException {
        ScriptBuilder buf = new ScriptBuilder();
        JSONObject stylingParams = getStylingParamsObj(context, scrollButton);
        buf.initScript(context, scrollButton, "O$.ScrollButton._init",
                scrollButton.getParent().getClientId(context),
                scrollButton.getScrollDirection().toString(),
                stylingParams
                /*Styles.getStyleClassesStr(context, dropDownField, dropDownField.getRolloverListItemStyle(),
                        dropDownField.getRolloverListItemClass(), DefaultStyles.getDefaultSelectionStyle(), StyleGroup.rolloverStyleGroup()),*/
        );
        return new InitScript(buf.toString(), new String[]{
                TableUtil.getTableUtilJsURL(context),
                Resources.internalURL(context, "timetable/scrollButton.js")
        });
    }

    private JSONObject getStylingParamsObj(FacesContext context, ScrollButton scrollButton){
        JSONObject stylingParams = new JSONObject();
        Styles.addStyleJsonParam(context, scrollButton, stylingParams, "buttonClass",
                scrollButton.getButtonStyle(), scrollButton.getButtonClass());
        Styles.addStyleJsonParam(context, scrollButton, stylingParams, "rolloverButtonClass",
                scrollButton.getRolloverButtonStyle(), scrollButton.getRolloverButtonClass());
        return stylingParams;
    }


}
