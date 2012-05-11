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
package org.openfaces.component;

import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;
import java.util.Collections;
import java.util.Map;

/**
 * @author Kharchenko
 */
public class ToggleCaptionButton extends CaptionButton {
    public static final String COMPONENT_TYPE = "org.openfaces.ToggleCaptionButton";
    public static final String COMPONENT_FAMILY = "org.openfaces.ToggleCaptionButton";

    private String toggledImageUrl;
    private String toggledRolloverImageUrl;
    private String toggledPressedImageUrl;

    private boolean toggled;

    public ToggleCaptionButton() {
        setRendererType("org.openfaces.ToggleCaptionButtonRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getToggledImageUrl() {
        return ValueBindings.get(this, "toggledImageUrl", toggledImageUrl);
    }

    public void setToggledImageUrl(String toggledImageUrl) {
        this.toggledImageUrl = toggledImageUrl;
    }

    public String getToggledRolloverImageUrl() {
        return ValueBindings.get(this, "toggledRolloverImageUrl", toggledRolloverImageUrl);
    }

    public void setToggledRolloverImageUrl(String toggledRolloverImageUrl) {
        this.toggledRolloverImageUrl = toggledRolloverImageUrl;
    }

    public String getToggledPressedImageUrl() {
        return ValueBindings.get(this, "toggledPressedImageUrl", toggledPressedImageUrl);
    }

    public void setToggledPressedImageUrl(String toggledPressedImageUrl) {
        this.toggledPressedImageUrl = toggledPressedImageUrl;
    }

    public boolean isToggled() {
        return toggled;
    }

    public void setToggled(boolean toggled) {
        this.toggled = toggled;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                toggledImageUrl,
                toggledRolloverImageUrl,
                toggledPressedImageUrl,
                toggled
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] values = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);
        toggledImageUrl = (String) values[i++];
        toggledRolloverImageUrl = (String) values[i++];
        toggledPressedImageUrl = (String) values[i++];
        toggled = (Boolean) values[i++];
    }

    protected Map<String, String> getPropertyMappings() {
        return Collections.emptyMap();
    }

    @Override
    public void setValueExpression(String name, ValueExpression binding) {
        String delegateName = getPropertyMappings().get(name);
        if (delegateName != null)
            super.setValueExpression(delegateName, binding);
        else
            super.setValueExpression(name, binding);
    }

    @Override
    public ValueExpression getValueExpression(String name) {
        String delegateName = getPropertyMappings().get(name);
        if (delegateName != null)
            return super.getValueExpression(delegateName);
        else
            return super.getValueExpression(name);
    }
}
