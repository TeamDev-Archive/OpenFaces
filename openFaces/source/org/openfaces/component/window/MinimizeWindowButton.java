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
package org.openfaces.component.window;

import org.openfaces.component.ToggleCaptionButton;

import java.util.Map;
import java.util.HashMap;

/**
 * @author Dmitry Pikhulya
 */
public class MinimizeWindowButton extends ToggleCaptionButton {
    public static final String COMPONENT_TYPE = "org.openfaces.MinimizeWindowButton";
    public static final String COMPONENT_FAMILY = "org.openfaces.MinimizeWindowButton";

    private static Map<String, String> OUR_PROPERTY_MAPPINGS = new HashMap<String, String>();

    static {
        OUR_PROPERTY_MAPPINGS.put("restoreImageUrl", "toggledImageUrl");
        OUR_PROPERTY_MAPPINGS.put("restoreRolloverImageUrl", "toggledRolloverImageUrl");
        OUR_PROPERTY_MAPPINGS.put("restorePressedImageUrl", "toggledPressedImageUrl");
        OUR_PROPERTY_MAPPINGS.put("minimizeImageUrl", "imageUrl");
        OUR_PROPERTY_MAPPINGS.put("minimizeRolloverImageUrl", "rolloverImageUrl");
        OUR_PROPERTY_MAPPINGS.put("minimizePressedImageUrl", "pressedImageUrl");
    }

    public MinimizeWindowButton() {
        setRendererType("org.openfaces.MinimizeWindowButtonRenderer");
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

    public String getMinimizeImageUrl() {
        return super.getImageUrl();
    }

    public void setMinimizeImageUrl(String imageUrl) {
        super.setImageUrl(imageUrl);
    }

    public String getMinimizeRolloverImageUrl() {
        return super.getRolloverImageUrl();
    }

    public void setMinimizeRolloverImageUrl(String rolloverImageUrl) {
        super.setRolloverImageUrl(rolloverImageUrl);
    }

    public String getMinimizePressedImageUrl() {
        return super.getPressedImageUrl();
    }

    public void setMinimizePressedImageUrl(String pressedImageUrl) {
        super.setPressedImageUrl(pressedImageUrl);
    }
}
