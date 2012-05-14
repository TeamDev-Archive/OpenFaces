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
package org.openfaces.renderkit;

import org.openfaces.component.CaptionButton;
import org.openfaces.component.ToggleCaptionButton;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Kharchenko
 */
public class ToggleCaptionButtonRenderer extends CaptionButtonRenderer {

    @Override
    public void decode(FacesContext context, UIComponent component) {
        ToggleCaptionButton img = (ToggleCaptionButton) component;
        String key = getStateFieldName(context, img);
        String value = context.getExternalContext().getRequestParameterMap().get(key);
        if (value == null)
            return;

        boolean newToggled = value.equalsIgnoreCase("true");
        if (img.isToggled() != newToggled) {
            img.setToggled(newToggled);
        }
    }

    @Override
    protected void renderAdditionalContent(FacesContext context, CaptionButton btn) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        ToggleCaptionButton tbtn = (ToggleCaptionButton) btn;
        Rendering.renderHiddenField(writer,
                getStateFieldName(context, tbtn),
                String.valueOf(tbtn.isToggled()));
    }

    @Override
    protected List<Object> getInitParams(FacesContext context, CaptionButton btn) throws IOException {
        ToggleCaptionButton tbtn = (ToggleCaptionButton) btn;
        List<Object> params = new ArrayList<Object>();
        params.add(tbtn.isToggled());
        addCaptionButtonInitParams(context, params, btn);
        params.add(getToggleImageUrl(context, tbtn));
        params.add(getToggleImageRolloverUrl(context, tbtn));
        params.add(getToggleImagePressedUrl(context, tbtn));
        return params;
    }

    @Override
    protected String getInitFunctionName() {
        return "O$._initToggleCaptionButton";
    }

    protected String getToggleImageUrl(FacesContext context, ToggleCaptionButton tbtn) {
        String imageUrl = tbtn.getToggledImageUrl();
        if (imageUrl == null)
            return getDefaultToggleImageUrl(context);
        else
            return Resources.applicationURL(context, imageUrl);
    }

    private String getToggleImageRolloverUrl(FacesContext context, ToggleCaptionButton tbtn) {
        String imageUrl = tbtn.getToggledRolloverImageUrl();
        if (imageUrl == null)
            return getDefaultToggledImageRolloverUrl(context);
        else
            return Resources.applicationURL(context, imageUrl);
    }

    private String getToggleImagePressedUrl(FacesContext context, ToggleCaptionButton tbtn) {
        String imageUrl = tbtn.getToggledPressedImageUrl();
        if (imageUrl == null)
            return getDefaultToggledImagePressedUrl(context);
        else
            return Resources.applicationURL(context, imageUrl);
    }

    protected String getDefaultToggleImageUrl(FacesContext context) {
        return null;
    }

    protected String getDefaultToggledImageRolloverUrl(FacesContext context) {
        return null;
    }

    protected String getDefaultToggledImagePressedUrl(FacesContext context) {
        return null;
    }

    private String getStateFieldName(FacesContext context, ToggleCaptionButton btn) {
        return btn.getClientId(context) + Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "toggleState";
    }
}
