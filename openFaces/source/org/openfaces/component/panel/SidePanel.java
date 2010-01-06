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
package org.openfaces.component.panel;

import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;
import static java.lang.Boolean.valueOf;

/**
 * @author Alexey Tarasyuk
 */
public class SidePanel extends AbstractPanelWithCaption {
    public static final String COMPONENT_TYPE = "org.openfaces.SidePanel";
    public static final String COMPONENT_FAMILY = "org.openfaces.SidePanel";

    private String size;
    private String minSize;
    private String maxSize;
    private SidePanelAlignment alignment;
    private String splitterStyle;
    private String splitterClass;
    private String splitterRolloverStyle;
    private String splitterRolloverClass;
    private Boolean resizable;
    private Boolean collapsible;
    private Boolean collapsed;

    private String onsplitterdrag;
    private String oncollapse;
    private String onrestore;
    private String onmaximize;


    public SidePanel() {
        setRendererType("org.openfaces.SidePanelRenderer");
    }

    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    public String getSize() {
        return ValueBindings.get(this, "size", size, "50%");
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getMinSize() {
        return ValueBindings.get(this, "minSize", minSize);
    }

    public void setMinSize(String minSize) {
        this.minSize = minSize;
    }

    public String getMaxSize() {
        return ValueBindings.get(this, "maxSize", maxSize);
    }

    public void setMaxSize(String maxSize) {
        this.maxSize = maxSize;
    }

    public SidePanelAlignment getAlignment() {
        return ValueBindings.get(this, "alignment", alignment,
                SidePanelAlignment.LEFT, SidePanelAlignment.class);
    }

    public void setAlignment(SidePanelAlignment alignment) {
        this.alignment = alignment;
    }

    public String getSplitterStyle() {
        return ValueBindings.get(this, "splitterStyle", splitterStyle);
    }

    public void setSplitterStyle(String splitterStyle) {
        this.splitterStyle = splitterStyle;
    }

    public String getSplitterClass() {
        return ValueBindings.get(this, "splitterClass", splitterClass);
    }

    public void setSplitterClass(String splitterClass) {
        this.splitterClass = splitterClass;
    }

    public String getSplitterRolloverStyle() {
        return ValueBindings.get(this, "splitterRolloverStyle", splitterRolloverStyle);
    }

    public void setSplitterRolloverStyle(String splitterRolloverStyle) {
        this.splitterRolloverStyle = splitterRolloverStyle;
    }

    public String getSplitterRolloverClass() {
        return ValueBindings.get(this, "splitterRolloverClass", splitterRolloverClass);
    }

    public void setSplitterRolloverClass(String splitterRolloverClass) {
        this.splitterRolloverClass = splitterRolloverClass;
    }

    public boolean isResizable() {
        return ValueBindings.get(this, "resizable", resizable, true);
    }

    public void setResizable(boolean resizable) {
        this.resizable = resizable;
    }

    public boolean isCollapsible() {
        return ValueBindings.get(this, "collapsible", collapsible, true);
    }

    public void setCollapsible(boolean collapsible) {
        this.collapsible = collapsible;
    }

    public boolean getCollapsed() {
        return ValueBindings.get(this, "collapsed", collapsed, false);
    }

    public void setCollapsed(boolean collapsed) {
        this.collapsed = valueOf(collapsed);
    }

    public String getOnsplitterdrag() {
        return ValueBindings.get(this, "onsplitterdrag", onsplitterdrag);
    }

    public void setOnsplitterdrag(String onsplitterdrag) {
        this.onsplitterdrag = onsplitterdrag;
    }

    public String getOncollapse() {
        return ValueBindings.get(this, "oncollapse", oncollapse);
    }

    public void setOncollapse(String oncollapse) {
        this.oncollapse = oncollapse;
    }

    public String getOnrestore() {
        return ValueBindings.get(this, "onrestore", onrestore);
    }

    public void setOnrestore(String onrestore) {
        this.onrestore = onrestore;
    }

    public String getOnmaximize() {
        return ValueBindings.get(this, "onmaximize", onmaximize);
    }

    public void setOnmaximize(String onmaximize) {
        this.onmaximize = onmaximize;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),

                size,
                minSize,
                maxSize,
                alignment,
                splitterStyle,
                splitterRolloverStyle,
                splitterClass,
                splitterRolloverClass,
                resizable,
                collapsible,
                collapsed,

                onsplitterdrag,
                oncollapse,
                onrestore
        };
    }

    @Override
    public void restoreState(FacesContext context, Object state) {
        Object values[] = (Object[]) state;
        int i = 0;
        super.restoreState(context, values[i++]);

        size = (String) values[i++];
        minSize = (String) values[i++];
        maxSize = (String) values[i++];
        alignment = (SidePanelAlignment) values[i++];
        splitterStyle = (String) values[i++];
        splitterRolloverStyle = (String) values[i++];
        splitterClass = (String) values[i++];
        splitterRolloverClass = (String) values[i++];
        resizable = (Boolean) values[i++];
        collapsible = (Boolean) values[i++];
        collapsed = (Boolean) values[i++];

        onsplitterdrag = (String) values[i++];
        oncollapse = (String) values[i++];
        onrestore = (String) values[i];
    }
}
