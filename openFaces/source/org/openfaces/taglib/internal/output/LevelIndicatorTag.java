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
package org.openfaces.taglib.internal.output;

import org.openfaces.component.FillDirection;
import org.openfaces.component.chart.Orientation;
import org.openfaces.component.output.LevelIndicator;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.taglib.internal.OUIOutputTag;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collection;

public class LevelIndicatorTag extends OUIOutputTag {

    public String getComponentType() {
        return LevelIndicator.COMPONENT_TYPE;
    }

    public String getRendererType() {
        return "org.openfaces.LevelIndicatorRenderer";
    }

    @Override
    public void setComponentProperties(FacesContext context, UIComponent component) {
        super.setComponentProperties(context, component);

        setDoubleProperty(component, "value");
        setStringProperty(component, "displayAreaStyle");
        setStringProperty(component, "displayAreaClass");
        setStringProperty(component, "labelStyle");
        setStringProperty(component, "labelClass");
        setIntProperty(component, "segmentSize");

        setEnumerationProperty(component, "orientation", Orientation.class);
        setEnumerationProperty(component, "fillDirection", FillDirection.class);

        setObjectProperty(component, "colors");
        setObjectProperty(component, "transitionLevels");

        String colors = getPropertyValue("colors");
        if (colors != null && colors.length() > 0) {
            if (isValueReference(colors))
                setObjectProperty(component, "colors", colors);
            else {
                validateAndSetColorsProperty(component, colors);
            }
        }

        String transitionLevels = getPropertyValue("transitionLevels");
        if (transitionLevels != null && transitionLevels.length() > 0) {
            if (isValueReference(transitionLevels))
                setObjectProperty(component, "transitionLevels", transitionLevels);
            else {
                validateAndSetTransitionLevelsProperty(component, transitionLevels);
            }
        }

        setDoubleProperty(component, "inactiveSegmentIntensity");
    }

    private void validateAndSetColorsProperty(UIComponent component, String colorsPropertyValue) {
        LevelIndicator levelIndicator = (LevelIndicator) component;
        Collection<String> colors = new ArrayList<String>();
        String[] colorsArray = colorsPropertyValue.split(",");

        for (String color : colorsArray) {
            try {
                colors.add(CSSUtil.normalizeCssColor(color));
            } catch (Exception e) {
                throw new IllegalArgumentException("'colors' attribute of <o:levelIndicator> tag should contain either an array or a collection of color representation strings, but the following value encountered: " + color);
            }
        }

        levelIndicator.setColors(colors);
    }

    private void validateAndSetTransitionLevelsProperty(UIComponent component, String transitionLevelsPropertyValue) {
        LevelIndicator levelIndicator = (LevelIndicator) component;

        String[] transitionLevels = transitionLevelsPropertyValue.split(",");
        Collection<Double> levels = new ArrayList<Double>();

        for (String level : transitionLevels) {
            try {
                final double levelDoubleValue = Double.parseDouble(level);
                levels.add(levelDoubleValue);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("'transitionLevels' attribute of <o:levelIndicator> tag should contain either an array or a collection of double values, but the following value encountered: " + level);
            }
        }

        levelIndicator.setTransitionLevels(levels);
    }
}
