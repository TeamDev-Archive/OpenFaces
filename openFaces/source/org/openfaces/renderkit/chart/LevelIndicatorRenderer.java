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
package org.openfaces.renderkit.chart;

import org.openfaces.component.chart.LevelIndicator;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.util.InitScript;
import org.openfaces.util.Rendering;
import org.openfaces.util.Resources;
import org.openfaces.util.ScriptBuilder;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class LevelIndicatorRenderer extends org.openfaces.renderkit.RendererBase {
    protected static final String INDICATOR_SUFFIX = "::indicator";
    protected static final String LABEL_SUFFIX = "::label";
    protected static final String INDICATOR_SEGMENT_SUFFIX = "::indicatorSegment";

    private static final String DEFAULT_CLASS = "o_levelIndicator";
    private static final String DEFAULT_INDICATOR_CLASS = "o_levelIndicator_indicator";
    private static final String DEFAULT_LABEL_CLASS = "o_levelIndicator_label";
    private static final String DEFAULT_INDICATOR_SEGMENT_CLASS = "o_levelIndicator_indicatorSegment";
    private static final String DEFAULT_WIDTH = "100";
    private static final String DEFAULT_HEIGHT = "30";

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        LevelIndicator levelIndicator = (LevelIndicator) component;
        Resources.includeJQuery(context);

        String clientId = levelIndicator.getClientId(context);

        // Render first tag
        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", levelIndicator);
        writer.writeAttribute("id", clientId, "id");
        writer.writeAttribute("class", Styles.getCSSClass(context,
                levelIndicator, levelIndicator.getStyle(), DEFAULT_CLASS, levelIndicator.getStyleClass()), null);
        Rendering.writeStandardEvents(writer, levelIndicator);

        writer.startElement("div", levelIndicator);
        writer.writeAttribute("id", getComponentSubPartClientId(context, levelIndicator, INDICATOR_SUFFIX), "id");
        writer.writeAttribute("class", Styles.getCSSClass(context,
                levelIndicator, levelIndicator.getIndicatorStyle(), DEFAULT_INDICATOR_CLASS, levelIndicator.getIndicatorClass()), null);
        writer.endElement("div");

        writer.startElement("div", levelIndicator);
        writer.writeAttribute("id", getComponentSubPartClientId(context, levelIndicator, LABEL_SUFFIX), "id");
        writer.writeAttribute("class", Styles.getCSSClass(context,
                levelIndicator, levelIndicator.getLabelStyle(), DEFAULT_LABEL_CLASS, levelIndicator.getLabelClass()), null);
        writer.endElement("div");

        writer.startElement("div", levelIndicator);
        writer.writeAttribute("id", getComponentSubPartClientId(context, levelIndicator, INDICATOR_SEGMENT_SUFFIX), "id");
        writer.writeAttribute("class", DEFAULT_INDICATOR_SEGMENT_CLASS, "class");
        writer.writeAttribute("style", "display:none;", "style");
        writer.endElement("div");

        writer.endElement("div");

        encodeScriptsAndStyles(context, levelIndicator);
    }

    protected void encodeScriptsAndStyles(FacesContext context, UIComponent component) throws IOException {
        LevelIndicator levelIndicator = (LevelIndicator) component;

        String styleClass = getInitialStyleClass(context, levelIndicator);
        String rolloverStyleClass = Styles.getCSSClass(context, levelIndicator, levelIndicator.getRolloverStyle(),
                StyleGroup.rolloverStyleGroup(), levelIndicator.getRolloverClass());

        ScriptBuilder buf = new ScriptBuilder().initScript(context, levelIndicator,
                "O$.LevelIndicator._init",
                getValue(levelIndicator),
                levelIndicator.getIndicatorSegmentSize(),
                levelIndicator.getOrientation().toString(),
                levelIndicator.getFillDirection().toString(),
                CSSUtil.getCustomAttributeStyleModel(levelIndicator.getStyle()).getWidth(),
                CSSUtil.getCustomAttributeStyleModel(levelIndicator.getStyle()).getHeight(),
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                getColors(levelIndicator),
                getTransitionLevels(levelIndicator),
                levelIndicator.getColorBlendIntensity(),
                styleClass,
                rolloverStyleClass);

        InitScript commonInitScript = new InitScript(buf, new String[]{
                Resources.getUtilJsURL(context),
                getLevelIndicatorJsURL(context)
        });

        Rendering.renderInitScripts(context, commonInitScript);
        Styles.renderStyleClasses(context, levelIndicator);
    }

    private Double getValue(LevelIndicator levelIndicator) {
        final boolean indicatorValueSpecified = levelIndicator.getValue() != null;
        boolean isLevelIndicatorValueValidDouble = indicatorValueSpecified && (levelIndicator.getValue() instanceof Double);

        if (indicatorValueSpecified && !isLevelIndicatorValueValidDouble
                && levelIndicator.getValue() instanceof String) {
            try {
                return Double.parseDouble((String) levelIndicator.getValue());
            } catch (NumberFormatException e) {
                isLevelIndicatorValueValidDouble = false;
            }
        } else {
            return (Double) levelIndicator.getValue();
        }

        if (indicatorValueSpecified && !isLevelIndicatorValueValidDouble) {
            throw new IllegalStateException("Level Indicator value is not valid. Value attribute should be defined as type java.lang.Double, but was " + levelIndicator.getValue().getClass().getName() + ":" + levelIndicator.getValue());
        }

        return 0d;
    }

    private Collection getColors(LevelIndicator levelIndicator) {
        final Collection colorsCollection = getColorsCollection(levelIndicator);
        Collection<String> resultColors = new ArrayList<String>();

        for (Object color : colorsCollection) {
            if (color instanceof Color) {
                String colorString = CSSUtil.formatColor((Color) color);
                resultColors.add(colorString);
            } else if (color instanceof String) {
                try {
                    final String colorString = CSSUtil.normalizeCssColor((String) color);
                    resultColors.add(colorString);
                } catch (Exception e) {
                    throw new IllegalArgumentException("'colors' attribute of <o:levelIndicator> tag should contain either an array or a collection of color representation strings or java.awt.Color objects, but the following value encountered: " + color);
                }
            } else {
                throw new IllegalArgumentException("'colors' attribute of <o:levelIndicator> tag should contain either an array or a collection of color representation strings or java.awt.Color objects, but a value of the following type encountered: " + color.getClass().getName());
            }
        }

        return resultColors;
    }

    private Collection getColorsCollection(LevelIndicator levelIndicator) {
        Collection<Object> colors;
        Object colorsObject = levelIndicator.getColors();

        if (colorsObject.getClass().isArray()) {
            colors = Arrays.asList((Object[]) colorsObject);
        } else if (colorsObject instanceof Collection)
            colors = (Collection<Object>) colorsObject;
        else
            throw new IllegalArgumentException("'colors' attribute of <o:levelIndicator> tag should contain either an array or a collection of color representation strings, but a value of the following type encountered: " + colorsObject.getClass().getName());

        return colors;
    }

    private Collection getTransitionLevels(LevelIndicator levelIndicator) {
        final Collection levelsCollection = getTransitionLevelsCollection(levelIndicator);
        Double previousValue = 0.0d;

        for (Object level : levelsCollection) {
            if (!(level instanceof Double)) {
                throw new IllegalArgumentException("'transitionLevels' attribute of <o:levelIndicator> tag should contain either an array or a collection of double values, but the following value encountered: " + level);
            } else {
                Double levelValue = (Double) level;
                if (levelValue < 0 || levelValue > 1) {
                    throw new IllegalArgumentException("'transitionLevels' attribute of <o:levelIndicator> tag should contain double values from 0.0 to 1.0");
                }

                if (previousValue > levelValue) {
                    throw new IllegalArgumentException("'transitionLevels' attribute of <o:levelIndicator> tag should contain double values in ascending order");
                }

                previousValue = levelValue;
            }
        }

        return levelsCollection;
    }

    private Collection getTransitionLevelsCollection(LevelIndicator levelIndicator) {
        Collection<Object> transitionLevels;
        Object transitionLevelsObject = levelIndicator.getTransitionLevels();

        if (transitionLevelsObject.getClass().isArray()) {
            transitionLevels = Arrays.asList((Object[]) transitionLevelsObject);
        } else if (transitionLevelsObject instanceof Collection)
            transitionLevels = (Collection<Object>) transitionLevelsObject;
        else
            throw new IllegalArgumentException("'transitionLevels' attribute of <o:levelIndicator> tag should contain either an array or a collection of double values, but a value of the following type encountered: " + transitionLevelsObject.getClass().getName());

        return transitionLevels;
    }

    protected String getInitialStyleClass(FacesContext context, LevelIndicator levelIndicator) {
        return Styles.getCSSClass(context, levelIndicator, levelIndicator.getStyle(), StyleGroup.regularStyleGroup(), levelIndicator.getStyleClass(), getDefaultLevelIndicatorClass());
    }

    protected String getLevelIndicatorJsURL(FacesContext context) {
        return Resources.getInternalURL(context, LevelIndicatorRenderer.class, "levelIndicator.js");
    }

    protected String getDefaultLevelIndicatorClass() {
        return DEFAULT_CLASS;
    }

    protected String getDefaultProgressClass() {
        return DEFAULT_INDICATOR_CLASS;
    }

    protected String getDefaultLabelClass() {
        return DEFAULT_LABEL_CLASS;
    }

    protected String getDefaultLampClass() {
        return DEFAULT_INDICATOR_SEGMENT_CLASS;
    }


    protected String getComponentSubPartClientId(FacesContext context, LevelIndicator levelIndicator, String suffix) {
        String clientId = levelIndicator.getClientId(context);
        return clientId + suffix;
    }

    @Override
    public void encodeChildren(FacesContext context, UIComponent component) throws IOException {
        super.encodeChildren(context, component);
    }

    @Override
    public void encodeEnd(FacesContext context, UIComponent component) throws IOException {
        super.encodeEnd(context, component);
    }
}
