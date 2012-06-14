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

import org.openfaces.util.Components;
import org.openfaces.util.ValueBindings;

import javax.el.ValueExpression;
import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class TreeColumn extends Column {
    public static final String COMPONENT_TYPE = "org.openfaces.TreeColumn";
    public static final String COMPONENT_FAMILY = "org.openfaces.TreeColumn";
    private static final String FACET_EXPANSION_TOGGLE = "expansionToggle";

    private String levelIndent;
    private String expansionToggleCellStyle;
    private String expansionToggleCellClass;
    private Boolean showAsTree;

    public TreeColumn() {
        setRendererType("org.openfaces.TreeColumnRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{super.saveState(context), levelIndent, expansionToggleCellStyle, expansionToggleCellClass,
                showAsTree};
    }

    @Override

    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        super.restoreState(context, state[0]);
        levelIndent = (String) state[1];
        expansionToggleCellStyle = (String) state[2];
        expansionToggleCellClass = (String) state[3];
        showAsTree = (Boolean) state[4];
    }

    public Object encodeParamsAsJsObject(FacesContext context) {
        ExpansionToggle expansionToggle = getExpansionToggle();
        Object result = expansionToggle.encodeExpansionDataAsJsObject(context);
        return result;
    }

    public ExpansionToggle getExpansionToggle() {
        ExpansionToggle expansionToggle = Components.getOrCreateFacet(getFacesContext(), this,
                ImageExpansionToggle.COMPONENT_TYPE, FACET_EXPANSION_TOGGLE, ImageExpansionToggle.class);

        if (expansionToggle == null) {
            expansionToggle = new ImageExpansionToggle();
            setExpansionToggle(expansionToggle);
        }
        return expansionToggle;
    }

    public String getExpandedToggleImageUrl() {
        return ((ImageExpansionToggle) getExpansionToggle()).getExpandedImageUrl();
    }

    public void setExpandedToggleImageUrl(String value) {
        ((ImageExpansionToggle) getExpansionToggle()).setExpandedImageUrl(value);
    }

    public String getCollapsedToggleImageUrl() {
        return ((ImageExpansionToggle) getExpansionToggle()).getCollapsedImageUrl();
    }

    public void setCollapsedToggleImageUrl(String value) {
        ((ImageExpansionToggle) getExpansionToggle()).setCollapsedImageUrl(value);
    }


    @Override
    public void setValueExpression(String name, ValueExpression expression) {
        if ("expandedToggleImageUrl".equals(name))
            getExpansionToggle().setValueExpression("expandedImageUrl", expression);
        if ("collapsedToggleImageUrl".equals(name))
            getExpansionToggle().setValueExpression("collapsedImageUrl", expression);
        super.setValueExpression(name, expression);
    }

    @Override
    public ValueExpression getValueExpression(String name) {
        if ("expandedToggleImageUrl".equals(name))
            return getExpansionToggle().getValueExpression("expandedImageUrl");
        if ("collapsedToggleImageUrl".equals(name))
            return getExpansionToggle().getValueExpression("collapsedImageUrl");
        return super.getValueExpression(name);
    }

    public void setExpansionToggle(ExpansionToggle toggle) {
        getFacets().put(FACET_EXPANSION_TOGGLE, toggle);
    }

    public String getLevelIndent() {
        return ValueBindings.get(this, "levelIndent", levelIndent);
    }

    public void setLevelIndent(String levelIndent) {
        this.levelIndent = levelIndent;
    }

    public String getExpansionToggleCellStyle() {
        return ValueBindings.get(this, "expansionToggleCellStyle", expansionToggleCellStyle);
    }

    public void setExpansionToggleCellStyle(String expansionToggleCellStyle) {
        this.expansionToggleCellStyle = expansionToggleCellStyle;
    }

    public String getExpansionToggleCellClass() {
        return ValueBindings.get(this, "expansionToggleCellClass", expansionToggleCellClass);
    }

    public void setExpansionToggleCellClass(String expansionToggleCellClass) {
        this.expansionToggleCellClass = expansionToggleCellClass;
    }

    public boolean getShowAsTree() {
        return ValueBindings.get(this, "showAsTree", showAsTree, true);
    }

    public void setShowAsTree(boolean showAsTree) {
        this.showAsTree = showAsTree;
    }

}
