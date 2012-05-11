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
package org.openfaces.renderkit.output;

import org.openfaces.component.chart.Orientation;
import org.openfaces.component.output.LevelIndicator;
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
import javax.faces.event.AbortProcessingException;
import javax.faces.event.ComponentSystemEvent;
import javax.faces.event.ComponentSystemEventListener;
import javax.faces.event.ListenerFor;
import javax.faces.event.PostAddToViewEvent;
import java.awt.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@ListenerFor(systemEventClass = PostAddToViewEvent.class)
public class LevelIndicatorRenderer extends org.openfaces.renderkit.RendererBase implements ComponentSystemEventListener {
    protected static final String DISPLAY_AREA_SUFFIX = "::displayArea";
    protected static final String LABEL_SUFFIX = "::label";
    protected static final String SEGMENT_SUFFIX = "::segment";

    private static final Collection<String> DEFAULT_COLORS = new ArrayList<String>(Arrays.asList("#00C000", "#C0C000", "#C00000"));
    private static final Collection<Double> DEFAULT_TRANSITION_LEVELS = new ArrayList<Double>(Arrays.asList(0.70, 0.90));
    private static final String DEFAULT_DISPLAY_AREA_CLASS = "o_levelIndicator_displayArea";
    private static final String DEFAULT_SEGMENT_CLASS = "o_levelIndicator_segment";
    private static final String DEFAULT_LABEL_CLASS = "o_levelIndicator_label";
    private static final String DEFAULT_CLASS = "o_levelIndicator";
    private static final String DEFAULT_WIDTH = "250";
    private static final String DEFAULT_HEIGHT = "22";

    public void processEvent(ComponentSystemEvent event) throws AbortProcessingException {
        if (event instanceof PostAddToViewEvent)
            Resources.includeJQuery();
    }

    @Override
    public void encodeBegin(FacesContext context, UIComponent component) throws IOException {
        LevelIndicator levelIndicator = (LevelIndicator) component;

        String clientId = levelIndicator.getClientId(context);

        // Render first tag
        final ResponseWriter writer = context.getResponseWriter();
        writer.startElement("div", levelIndicator);
        writer.writeAttribute("id", clientId, "id");
        final boolean verticallyOriented = levelIndicator.getOrientation().equals(Orientation.VERTICAL);
        String defaultOrientationClass = verticallyOriented ? " vertical" : " horizontal";
        writer.writeAttribute("class", Styles.getCSSClass(context,
                levelIndicator, levelIndicator.getStyle(), DEFAULT_CLASS + defaultOrientationClass,
                levelIndicator.getStyleClass()), null);
        Rendering.writeStandardEvents(writer, levelIndicator);

        writer.startElement("div", levelIndicator);
        writer.writeAttribute("id", getComponentSubPartClientId(context, levelIndicator, DISPLAY_AREA_SUFFIX), "id");
        writer.writeAttribute("class", Styles.getCSSClass(context,
                levelIndicator, levelIndicator.getDisplayAreaStyle(), DEFAULT_DISPLAY_AREA_CLASS, levelIndicator.getDisplayAreaClass()), null);
        writer.endElement("div");

        writer.startElement("div", levelIndicator);
        writer.writeAttribute("id", getComponentSubPartClientId(context, levelIndicator, LABEL_SUFFIX), "id");
        writer.writeAttribute("class", Styles.getCSSClass(context,
                levelIndicator, levelIndicator.getLabelStyle(), DEFAULT_LABEL_CLASS, levelIndicator.getLabelClass()), null);
        writer.endElement("div");

        writer.startElement("div", levelIndicator);
        writer.writeAttribute("id", getComponentSubPartClientId(context, levelIndicator, SEGMENT_SUFFIX), "id");
        writer.writeAttribute("class", DEFAULT_SEGMENT_CLASS, "class");
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
                levelIndicator.getSegmentSize(),
                levelIndicator.getOrientation().toString(),
                levelIndicator.getFillDirection().toString(),
                DEFAULT_WIDTH,
                DEFAULT_HEIGHT,
                getColors(levelIndicator),
                getTransitionLevels(levelIndicator),
                levelIndicator.getInactiveSegmentIntensity(),
                styleClass,
                rolloverStyleClass);

        InitScript commonInitScript = new InitScript(buf, new String[]{
                Resources.utilJsURL(context),
                getLevelIndicatorJsURL(context)
        });

        Rendering.renderInitScripts(context, commonInitScript);
        Styles.renderStyleClasses(context, levelIndicator);
    }

    private double getValue(LevelIndicator levelIndicator) {
        Double value = (Double) levelIndicator.getValue();
        if (value != null) {
            if (value < 0.0 || value > 1.0) {
                throw new IllegalArgumentException("Value of LevelIndicator should be from 0.0 to 1.0");
            }

            return value;
        }

        return 0;
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

        if (resultColors.isEmpty()) {
            List<String> defaultColors = new ArrayList<String>();

            for (String colorValue : DEFAULT_COLORS) {
                final String colorString = CSSUtil.normalizeCssColor(colorValue);
                defaultColors.add(colorString);
            }

            resultColors = defaultColors;
        }

        return resultColors;
    }

    private Collection getTransitionLevels(LevelIndicator levelIndicator) {
        Collection levelsCollection = getTransitionLevelsCollection(levelIndicator);
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

        if (levelsCollection.isEmpty()) {
            levelsCollection = DEFAULT_TRANSITION_LEVELS;
        }

        return levelsCollection;
    }

    private Collection getColorsCollection(LevelIndicator levelIndicator) {
        Collection<Object> colors = new ArrayList<Object>();
        Object colorsObject = levelIndicator.getColors();

        if (colorsObject != null && colorsObject.getClass().isArray()) {
            colors = Arrays.asList((Object[]) colorsObject);
        } else if (colorsObject != null && colorsObject instanceof Collection) {
            colors = (Collection<Object>) colorsObject;
        }

        return colors;
    }

    private Collection getTransitionLevelsCollection(LevelIndicator levelIndicator) {
        Collection<Object> transitionLevels = new ArrayList<Object>();
        Object transitionLevelsObject = levelIndicator.getTransitionLevels();

        if (transitionLevelsObject != null && transitionLevelsObject.getClass().isArray()) {
            transitionLevels = Arrays.asList((Object[]) transitionLevelsObject);
        } else if (transitionLevelsObject != null && transitionLevelsObject instanceof Collection) {
            transitionLevels = (Collection<Object>) transitionLevelsObject;
        }

        return transitionLevels;
    }

    protected String getInitialStyleClass(FacesContext context, LevelIndicator levelIndicator) {
        return Styles.getCSSClass(context, levelIndicator, levelIndicator.getStyle(), StyleGroup.regularStyleGroup(), levelIndicator.getStyleClass(), getDefaultLevelIndicatorClass());
    }

    protected String getLevelIndicatorJsURL(FacesContext context) {
        return Resources.internalURL(context, "output/levelIndicator.js");
    }

    protected String getDefaultLevelIndicatorClass() {
        return DEFAULT_CLASS;
    }

    protected String getDefaultProgressClass() {
        return DEFAULT_DISPLAY_AREA_CLASS;
    }

    protected String getDefaultLabelClass() {
        return DEFAULT_LABEL_CLASS;
    }

    protected String getDefaultLampClass() {
        return DEFAULT_SEGMENT_CLASS;
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
