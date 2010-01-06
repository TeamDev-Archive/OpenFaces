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
package org.openfaces.component;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class ExpansionToggleButton extends ToggleCaptionButton {
    public static final String COMPONENT_TYPE = "org.openfaces.ExpansionToggleButton";
    public static final String COMPONENT_FAMILY = "org.openfaces.ExpansionToggleButton";

    private static Map<String, String> OUR_PROPERTY_MAPPINGS = new HashMap<String, String>();

    static {
        OUR_PROPERTY_MAPPINGS.put("expandedImageUrl", "toggledImageUrl");
        OUR_PROPERTY_MAPPINGS.put("expandedRolloverImageUrl", "toggledRolloverImageUrl");
        OUR_PROPERTY_MAPPINGS.put("expandedPressedImageUrl", "toggledPressedImageUrl");
        OUR_PROPERTY_MAPPINGS.put("collapsedImageUrl", "imageUrl");
        OUR_PROPERTY_MAPPINGS.put("collapsedRolloverImageUrl", "rolloverImageUrl");
        OUR_PROPERTY_MAPPINGS.put("collapsedPressedImageUrl", "pressedImageUrl");
    }

    public ExpansionToggleButton() {
        setRendererType("org.openfaces.ExpansionToggleButtonRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getExpandedImageUrl() {
        return super.getToggledImageUrl();
    }

    public void setExpandedImageUrl(String toggledImageUrl) {
        super.setToggledImageUrl(toggledImageUrl);
    }

    public String getExpandedRolloverImageUrl() {
        return super.getToggledRolloverImageUrl();
    }

    public void setExpandedRolloverImageUrl(String toggledRolloverImageUrl) {
        super.setToggledRolloverImageUrl(toggledRolloverImageUrl);
    }

    public String getExpandedPressedImageUrl() {
        return super.getToggledPressedImageUrl();
    }

    public void setExpandedPressedImageUrl(String toggledPressedImageUrl) {
        super.setToggledPressedImageUrl(toggledPressedImageUrl);
    }

    public String getCollapsedImageUrl() {
        return super.getImageUrl();
    }

    public void setCollapsedImageUrl(String imageUrl) {
        super.setImageUrl(imageUrl);
    }

    public String getCollapsedRolloverImageUrl() {
        return super.getRolloverImageUrl();
    }

    public void setCollapsedRolloverImageUrl(String rolloverImageUrl) {
        super.setRolloverImageUrl(rolloverImageUrl);
    }

    public String getCollapsedPressedImageUrl() {
        return super.getPressedImageUrl();
    }

    public void setCollapsedPressedImageUrl(String pressedImageUrl) {
        super.setPressedImageUrl(pressedImageUrl);
    }

    @Override
    protected Map<String, String> getPropertyMappings() {
        return OUR_PROPERTY_MAPPINGS;
    }

}
