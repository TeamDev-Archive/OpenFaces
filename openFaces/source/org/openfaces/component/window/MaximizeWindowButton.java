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
package org.openfaces.component.window;

import org.openfaces.component.ToggleCaptionButton;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class MaximizeWindowButton extends ToggleCaptionButton {
    public static final String COMPONENT_TYPE = "org.openfaces.MaximizeWindowButton";
    public static final String COMPONENT_FAMILY = "org.openfaces.MaximizeWindowButton";

    private static Map<String, String> OUR_PROPERTY_MAPPINGS = new HashMap<String, String>();

    static {
        OUR_PROPERTY_MAPPINGS.put("restoreImageUrl", "toggledImageUrl");
        OUR_PROPERTY_MAPPINGS.put("restoreRolloverImageUrl", "toggledRolloverImageUrl");
        OUR_PROPERTY_MAPPINGS.put("restorePressedImageUrl", "toggledPressedImageUrl");
        OUR_PROPERTY_MAPPINGS.put("maximizeImageUrl", "imageUrl");
        OUR_PROPERTY_MAPPINGS.put("maximizeRolloverImageUrl", "rolloverImageUrl");
        OUR_PROPERTY_MAPPINGS.put("maximizePressedImageUrl", "pressedImageUrl");
    }

    public MaximizeWindowButton() {
        setRendererType("org.openfaces.MaximizeWindowButtonRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    protected Map<String, String> getPropertyMappings() {
        return OUR_PROPERTY_MAPPINGS;
    }

    public String getRestoreImageUrl() {
        return super.getToggledImageUrl();
    }

    public void setRestoreImageUrl(String toggledImageUrl) {
        super.setToggledImageUrl(toggledImageUrl);
    }

    public String getRestoreRolloverImageUrl() {
        return super.getToggledRolloverImageUrl();
    }

    public void setRestoreRolloverImageUrl(String toggledRolloverImageUrl) {
        super.setToggledRolloverImageUrl(toggledRolloverImageUrl);
    }

    public String getRestorePressedImageUrl() {
        return super.getToggledPressedImageUrl();
    }

    public void setRestorePressedImageUrl(String toggledPressedImageUrl) {
        super.setToggledPressedImageUrl(toggledPressedImageUrl);
    }

    public String getMaximizeImageUrl() {
        return super.getImageUrl();
    }

    public void setMaximizeImageUrl(String imageUrl) {
        super.setImageUrl(imageUrl);
    }

    public String getMaximizeRolloverImageUrl() {
        return super.getRolloverImageUrl();
    }

    public void setMaximizeRolloverImageUrl(String rolloverImageUrl) {
        super.setRolloverImageUrl(rolloverImageUrl);
    }

    public String getMaximizePressedImageUrl() {
        return super.getPressedImageUrl();
    }

    public void setMaximizePressedImageUrl(String pressedImageUrl) {
        super.setPressedImageUrl(pressedImageUrl);
    }

}
