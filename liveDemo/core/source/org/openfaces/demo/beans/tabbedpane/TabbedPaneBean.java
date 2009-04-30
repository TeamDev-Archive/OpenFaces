/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.demo.beans.tabbedpane;

import org.openfaces.component.select.TabAlignment;
import org.openfaces.component.select.TabPlacement;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TabbedPaneBean implements Serializable {
    private List<SelectItem> currentAlignments = new ArrayList<SelectItem>();
    private List<SelectItem> topBottomAlignment = new ArrayList<SelectItem>();
    private List<SelectItem> rightLeftAlignment = new ArrayList<SelectItem>();
    private List<SelectItem> placements = new ArrayList<SelectItem>();

    private TabAlignment selectedAlignment;
    private TabPlacement selectedPlacement;
    private Converter tabPositionConverter = new TabPositionConverter();
    private Converter tabAlignmentConverter = new TabAlignmentConverter();

    public TabbedPaneBean() {
        placements.add(new SelectItem(TabPlacement.TOP, "top"));
        placements.add(new SelectItem(TabPlacement.RIGHT, "right"));
        placements.add(new SelectItem(TabPlacement.BOTTOM, "bottom"));
        placements.add(new SelectItem(TabPlacement.LEFT, "left"));

        selectedPlacement = TabPlacement.TOP;

        topBottomAlignment.add(new SelectItem(TabAlignment.TOP_OR_LEFT, "top"));
        topBottomAlignment.add(new SelectItem(TabAlignment.BOTTOM_OR_RIGHT, "bottom"));

        rightLeftAlignment.add(new SelectItem(TabAlignment.TOP_OR_LEFT, "left"));
        rightLeftAlignment.add(new SelectItem(TabAlignment.BOTTOM_OR_RIGHT, "right"));

        currentAlignments.addAll(getCurrentAlignments());

        selectedAlignment = (TabAlignment) (getCurrentAlignments().get(0)).getValue();
    }

    public List<SelectItem> getPlacements() {
        return placements;
    }

    public TabAlignment getSelectedAlignment() {
        return selectedAlignment;
    }

    public TabPlacement getSelectedPlacement() {
        return selectedPlacement;
    }

    public void setSelectedAlignment(TabAlignment selectedAlignment) {
        this.selectedAlignment = selectedAlignment;
    }

    public void setSelectedPlacement(TabPlacement selectedPlacement) {
        this.selectedPlacement = selectedPlacement;
    }

    public Converter getTabPositionConverter() {
        return tabPositionConverter;
    }

    public Converter getTabAlignmentConverter() {
        return tabAlignmentConverter;
    }

    public List<SelectItem> getCurrentAlignments() {
        if (selectedPlacement.equals(TabPlacement.TOP) || selectedPlacement.equals(TabPlacement.BOTTOM))
            currentAlignments = rightLeftAlignment;

        if (selectedPlacement.equals(TabPlacement.LEFT) || selectedPlacement.equals(TabPlacement.RIGHT))
            currentAlignments = topBottomAlignment;

        return currentAlignments;
    }

    private static class TabPositionConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return valueByString(TabPlacement.class, value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            return value.toString();
        }
    }

    private static class TabAlignmentConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return valueByString(TabAlignment.class, value);
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            return value.toString();
        }
    }

    private static <T extends Enum> T valueByString(Class<T> enumClass, String str) {
        if (str == null || str.length() == 0)
            return null;
        for (T value : enumClass.getEnumConstants()) {
            String valueAsString = value.toString();
            if (valueAsString.equals(str))
                return value;
        }
        throw new IllegalArgumentException("str: " + str);
    }

}