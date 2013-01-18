/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.demo.beans.ajax;

import org.openfaces.component.HorizontalAlignment;
import org.openfaces.component.VerticalAlignment;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class AjaxSettings {
    private String text = "Loading...";
    private String style = "border: 1px solid gray; background: white;";
    private HorizontalAlignment horizontalAlignment = HorizontalAlignment.RIGHT;
    private VerticalAlignment verticalAlignment = VerticalAlignment.TOP;
    private boolean fillBackground = false;
    private double transparency = 0;
    private int transparencyTransitionPeriod = 0;
    private double backgroundTransparency = 0.6;
    private String backgroundStyle = "background: #d8d8d8";
    private int backgroundTransparencyTransitionPeriod = 180;

    private HorizontalAlignmentConverter horizontalAlignmentConverter = new HorizontalAlignmentConverter();
    private VerticalAlignmentConverter verticalAlignmentConverter = new VerticalAlignmentConverter();

    public String getWindowCaption() {
        return "Ajax Settings";
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getStyle() {
        return style;
    }

    public void setStyle(String style) {
        this.style = style;
    }

    public HorizontalAlignment getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(HorizontalAlignment horizontalAlignment) {
        this.horizontalAlignment = horizontalAlignment;
    }

    public VerticalAlignment getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(VerticalAlignment verticalAlignment) {
        this.verticalAlignment = verticalAlignment;
    }

    public boolean isFillBackground() {
        return fillBackground;
    }

    public void setFillBackground(boolean fillBackground) {
        this.fillBackground = fillBackground;
    }

    public double getTransparency() {
        return transparency;
    }

    public void setTransparency(double transparency) {
        this.transparency = transparency;
    }

    public int getTransparencyTransitionPeriod() {
        return transparencyTransitionPeriod;
    }

    public void setTransparencyTransitionPeriod(int transparencyTransitionPeriod) {
        this.transparencyTransitionPeriod = transparencyTransitionPeriod;
    }

    public double getBackgroundTransparency() {
        return backgroundTransparency;
    }

    public void setBackgroundTransparency(double backgroundTransparency) {
        this.backgroundTransparency = backgroundTransparency;
    }

    public int getBackgroundTransparencyTransitionPeriod() {
        return backgroundTransparencyTransitionPeriod;
    }

    public void setBackgroundTransparencyTransitionPeriod(int backgroundTransparencyTransitionPeriod) {
        this.backgroundTransparencyTransitionPeriod = backgroundTransparencyTransitionPeriod;
    }

    public String getBackgroundStyle() {
        return backgroundStyle;
    }

    public void setBackgroundStyle(String backgroundStyle) {
        this.backgroundStyle = backgroundStyle;
    }

    public List<SelectItem> getHorizontalAlignmentOptions() {
        ArrayList<SelectItem> items = new ArrayList<SelectItem>();
        items.add(new SelectItem(HorizontalAlignment.LEFT, HorizontalAlignment.LEFT.toString(), null));
        items.add(new SelectItem(HorizontalAlignment.CENTER, HorizontalAlignment.CENTER.toString(), null));
        items.add(new SelectItem(HorizontalAlignment.RIGHT, HorizontalAlignment.RIGHT.toString(), null));
        return items;
    }

    public List<SelectItem> getVerticalAlignmentOptions() {
        ArrayList<SelectItem> items = new ArrayList<SelectItem>();
        items.add(new SelectItem(VerticalAlignment.TOP, VerticalAlignment.TOP.toString(), null));
        items.add(new SelectItem(VerticalAlignment.CENTER, VerticalAlignment.CENTER.toString(), null));
        items.add(new SelectItem(VerticalAlignment.BOTTOM, VerticalAlignment.BOTTOM.toString(), null));
        return items;
    }

    public HorizontalAlignmentConverter getHorizontalAlignmentConverter() {
        return horizontalAlignmentConverter;
    }

    public VerticalAlignmentConverter getVerticalAlignmentConverter() {
        return verticalAlignmentConverter;
    }

    private static class HorizontalAlignmentConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return HorizontalAlignment.valueOf(value.toUpperCase());
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            return value.toString();
        }
    }

    private static class VerticalAlignmentConverter implements Converter, Serializable {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return VerticalAlignment.valueOf(value.toUpperCase());
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            return value.toString();
        }
    }

}
