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

import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.ValueBindings;

import javax.faces.context.FacesContext;

/**
 * @author Dmitry Pikhulya
 */
public class ColumnReordering extends AbstractTableConfigurator {
    public static final String COMPONENT_TYPE = "org.openfaces.ColumnReordering";
    public static final String COMPONENT_FAMILY = "org.openfaces.ColumnReordering";

    private String draggedCellStyle;
    private String draggedCellClass;
    private Double draggedCellTransparency;
    private String autoScrollAreaStyle;
    private String autoScrollAreaClass;
    private Double autoScrollAreaTransparency;
    private String autoScrollLeftImageUrl;
    private String autoScrollRightImageUrl;
    private String dropTargetStyle;
    private String dropTargetClass;
    private String dropTargetTopImageUrl;
    private String dropTargetBottomImageUrl;


    public ColumnReordering() {
        setRendererType("org.openfaces.ColumnReorderingRenderer");
    }

    @Override
    public String getFamily() {
        return COMPONENT_FAMILY;
    }

    @Override
    public Object saveState(FacesContext context) {
        return new Object[]{
                super.saveState(context),
                draggedCellStyle,
                draggedCellClass,
                draggedCellTransparency,
                autoScrollAreaStyle,
                autoScrollAreaClass,
                autoScrollAreaTransparency,
                autoScrollLeftImageUrl,
                autoScrollRightImageUrl,
                dropTargetStyle,
                dropTargetClass,
                dropTargetTopImageUrl,
                dropTargetBottomImageUrl
        };
    }

    @Override
    public void restoreState(FacesContext context, Object stateObj) {
        Object[] state = (Object[]) stateObj;
        int i = 0;
        super.restoreState(context, state[i++]);
        draggedCellStyle = (String) state[i++];
        draggedCellClass = (String) state[i++];
        draggedCellTransparency = (Double) state[i++];
        autoScrollAreaStyle = (String) state[i++];
        autoScrollAreaClass = (String) state[i++];
        autoScrollAreaTransparency = (Double) state[i++];
        autoScrollLeftImageUrl = (String) state[i++];
        autoScrollRightImageUrl = (String) state[i++];
        dropTargetStyle = (String) state[i++];
        dropTargetClass = (String) state[i++];
        dropTargetTopImageUrl = (String) state[i++];
        dropTargetBottomImageUrl = (String) state[i++];
    }

    public String getDraggedCellStyle() {
        return ValueBindings.get(this, "draggedCellStyle", draggedCellStyle);
    }

    public void setDraggedCellStyle(String draggedCellStyle) {
        this.draggedCellStyle = draggedCellStyle;
    }

    public String getDraggedCellClass() {
        return ValueBindings.get(this, "draggedCellClass", draggedCellClass);
    }

    public void setDraggedCellClass(String draggedCellClass) {
        this.draggedCellClass = draggedCellClass;
    }

    public double getDraggedCellTransparency() {
        return ValueBindings.get(this, "draggedCellTransparency", draggedCellTransparency, 0.5);
    }

    public void setDraggedCellTransparency(double draggedCellTransparency) {
        this.draggedCellTransparency = draggedCellTransparency;
    }

    public String getAutoScrollAreaStyle() {
        return ValueBindings.get(this, "autoScrollAreaStyle", autoScrollAreaStyle);
    }

    public void setAutoScrollAreaStyle(String autoScrollAreaStyle) {
        this.autoScrollAreaStyle = autoScrollAreaStyle;
    }

    public String getAutoScrollAreaClass() {
        return ValueBindings.get(this, "autoScrollAreaClass", autoScrollAreaClass);
    }

    public void setAutoScrollAreaClass(String autoScrollAreaClass) {
        this.autoScrollAreaClass = autoScrollAreaClass;
    }

    public double getAutoScrollAreaTransparency() {
        return ValueBindings.get(this, "autoScrollAreaTransparency", autoScrollAreaTransparency, 0.5);
    }

    public void setAutoScrollAreaTransparency(double autoScrollAreaTransparency) {
        this.autoScrollAreaTransparency = autoScrollAreaTransparency;
    }

    public String getAutoScrollLeftImageUrl() {
        return ValueBindings.get(this, "autoScrollLeftImageUrl", autoScrollLeftImageUrl);
    }

    public void setAutoScrollLeftImageUrl(String autoScrollLeftImageUrl) {
        this.autoScrollLeftImageUrl = autoScrollLeftImageUrl;
    }

    public String getAutoScrollRightImageUrl() {
        return ValueBindings.get(this, "autoScrollRightImageUrl", autoScrollRightImageUrl);
    }

    public void setAutoScrollRightImageUrl(String autoScrollRightImageUrl) {
        this.autoScrollRightImageUrl = autoScrollRightImageUrl;
    }

    public String getDropTargetStyle() {
        return ValueBindings.get(this, "dropTargetStyle", dropTargetStyle);
    }

    public void setDropTargetStyle(String dropTargetStyle) {
        this.dropTargetStyle = dropTargetStyle;
    }

    public String getDropTargetClass() {
        return ValueBindings.get(this, "dropTargetClass", dropTargetClass);
    }

    public void setDropTargetClass(String dropTargetClass) {
        this.dropTargetClass = dropTargetClass;
    }

    public String getDropTargetTopImageUrl() {
        return ValueBindings.get(this, "dropTargetTopImageUrl", dropTargetTopImageUrl);
    }

    public void setDropTargetTopImageUrl(String dropTargetTopImageUrl) {
        this.dropTargetTopImageUrl = dropTargetTopImageUrl;
    }

    public String getDropTargetBottomImageUrl() {
        return ValueBindings.get(this, "dropTargetBottomImageUrl", dropTargetBottomImageUrl);
    }


    public void setDropTargetBottomImageUrl(String dropTargetBottomImageUrl) {
        this.dropTargetBottomImageUrl = dropTargetBottomImageUrl;
    }

    public void encodeOnBodyReload(FacesContext context, ScriptBuilder sb) {

    }
}
