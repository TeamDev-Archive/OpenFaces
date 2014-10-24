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
package org.openfaces.component.table;

import org.openfaces.org.json.JSONArray;
import org.openfaces.util.Resources;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class ImageExpansionToggle extends ExpansionToggle {
    public static final String COMPONENT_TYPE = "org.openfaces.ImageExpansionToggle";
    public static final String COMPONENT_FAMILY = "org.openfaces.ImageExpansionToggle";

    private String expandedImageUrl;
    private String collapsedImageUrl;

    public ImageExpansionToggle() {
        setRendererType("org.openfaces.ImageExpansionToggleRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        Object superState = super.saveState(context);
        return new Object[]{superState, expandedImageUrl, collapsedImageUrl};
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object[] stateArray = (Object[]) state;
        super.restoreState(context, stateArray[0]);
        expandedImageUrl = (String) stateArray[1];
        collapsedImageUrl = (String) stateArray[2];
    }

    public String getExpandedImageUrl() {
        return ValueBindings.get(this, "expandedImageUrl", expandedImageUrl);
    }

    public void setExpandedImageUrl(String expandedImageUrl) {
        this.expandedImageUrl = expandedImageUrl;
    }

    public String getCollapsedImageUrl() {
        return ValueBindings.get(this, "collapsedImageUrl", collapsedImageUrl);
    }

    public void setCollapsedImageUrl(String collapsedImageUrl) {
        this.collapsedImageUrl = collapsedImageUrl;
    }

    public String getToggleImageUrl(FacesContext context, boolean nodeExpanded) {
        String userUrl = nodeExpanded ? getExpandedImageUrl() : getCollapsedImageUrl();
        String defaultImagePath = nodeExpanded ? "table/minus.gif" : "table/plus.gif";
        String url = Resources.getURL(context, userUrl, defaultImagePath);
        return url;
    }

    @Override
    public Object encodeExpansionDataAsJsObject(FacesContext context) {
        JSONArray result = new JSONArray();
        result.put(getToggleImageUrl(context, true));
        result.put(getToggleImageUrl(context, false));
        return result;
    }


}
