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
package org.openfaces.util;

import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;

import javax.faces.component.UIComponent;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;

/**
 * @author Dmitry Pikhulya
 */
public class Styles {
    public static final String DEFAULT_CSS_REQUESTED = Styles.class.getName() + ".defaultCssRequested";

    private static final String GENERATED_CLASS_NAME_PREFIX = "o_class_";
    private static final String REGISTERED_STYLE_PREFIX = Styles.class.getName() + ".registeredStyle:";
    private static final String REGISTERED_STYLE_CLASSES = Styles.class.getName() + ".registeredStyleClasses";
    private static final String REGISTERED_CSS_CLASSES_PREFIX = Styles.class.getName() + ".registeredCssClasses:";
    private static final String RENDERED_STYLE_ELEMENTS_IDS = Styles.class.getName() + ".renderedStyleElementsIds";
    private static final String STYLES_ID_SUFFIX = Rendering.CLIENT_ID_SUFFIX_SEPARATOR + "styles";

    private static long styleIndexCounter = 0;

    private Styles() {
    }

    /**
     * This method check and merges two ccs classes into one
     *
     * @param class1 - first css class to merge
     * @param class2 - second
     * @return merged class name
     */
    public static String mergeClassNames(String class1, String class2) {
        boolean class1Empty = Rendering.isNullOrEmpty(class1);
        boolean class2Empty = Rendering.isNullOrEmpty(class2);
        if (class1Empty && class2Empty) {
            return null;
        }

        StringBuilder buf = new StringBuilder();
        if (!class1Empty) {
            buf.append(class1);
        }

        if (!class2Empty) {
            if (!class1Empty) {
                buf.append(' ');
            }
            buf.append(class2);
        }

        return buf.toString();
    }

    /**
     * Return component css class for regular {@link StyleGroup}
     *
     * @param context   {@link FacesContext} for the current request
     * @param component The component
     * @param style     The style to merge with
     * @return component css class
     * @see #getCSSClass(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String, StyleGroup, String, String)
     */
    public static String getCSSClass(FacesContext context, UIComponent component, String style) {
        return getCSSClass(context, component, style, (String) null);
    }

    /**
     * Return component css class for given {@link StyleGroup}
     *
     * @param context    {@link FacesContext} for the current request
     * @param component  The component
     * @param style      The style to merge with
     * @param styleGroup The {@link StyleGroup} for which retrieve css class
     * @return component css class
     * @see #getCSSClass(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String, StyleGroup, String, String)
     */
    public static String getCSSClass(FacesContext context, UIComponent component, String style, StyleGroup styleGroup) {
        return getCSSClass(context, component, style, styleGroup, null);
    }

    /**
     * Return component css class for given {@link StyleGroup} merged with given css style and css style class
     *
     * @param context    {@link FacesContext} for the current request
     * @param component  The component
     * @param style      The style to merge with
     * @param styleGroup The {@link StyleGroup} for which retrieve css class
     * @param styleClass The style class to merge with
     * @return component css class
     * @see #getCSSClass(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String, StyleGroup, String, String)
     */
    public static String getCSSClass(FacesContext context, UIComponent component, String style, StyleGroup styleGroup, String styleClass) {
        return getCSSClass(context, component, style, styleGroup, styleClass, null);
    }

    /**
     * Return component css class for given component merged with given default css class and css style
     *
     * @param context       {@link FacesContext} for the current request
     * @param component     The component
     * @param style         The style to merge with
     * @param defStyleClass The default style class to merge with
     * @return component css class
     * @see #getCSSClass(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String, StyleGroup, String, String)
     */
    public static String getCSSClass(FacesContext context, UIComponent component, String style, String defStyleClass) {
        return getCSSClass(context, component, style, StyleGroup.regularStyleGroup(), null, defStyleClass);
    }

    /**
     * Return component css class for given component merged with given css style and css style class
     *
     * @param context       {@link FacesContext} for the current request
     * @param component     The component
     * @param style         The style to merge with
     * @param defStyleClass The default style class to merge with
     * @param styleClass    The style class to merge with
     * @return component css class
     * @see #getCSSClass(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String, StyleGroup, String, String)
     */
    public static String getCSSClass(FacesContext context, UIComponent component, String style, String defStyleClass, String styleClass) {
        return getCSSClass(context, component, style, StyleGroup.regularStyleGroup(), styleClass, defStyleClass);
    }

    // todo: having so much getCSSClass method overrides can be confusing, minimize to a convenient common set

    /**
     * Return component css class for given component and given {@link StyleGroup} merged with given css style and css style class
     *
     * @param context       {@link FacesContext} for the current request
     * @param component     The component
     * @param style         The style to merge with
     * @param defStyleClass The default style class to merge with
     * @param styleGroup    The {@link StyleGroup} for which retrieve css class
     * @param styleClass    The style class to merge with
     * @return component css class
     * @see #getCSSClass(javax.faces.context.FacesContext, javax.faces.component.UIComponent, String, StyleGroup, String, String)
     */
    public static String getCSSClass(
            FacesContext context,
            UIComponent component, String style, StyleGroup styleGroup, String styleClass, String defStyleClass
    ) {
        String classNames = defStyleClass;
        if (style != null) {
            String classForStyle = registerCssClass(context, style, styleGroup, component);
            classNames = mergeClassNames(classNames, classForStyle);
        }
        classNames = mergeClassNames(classNames, styleClass);
        return classNames;
    }

    /**
     * Renders default style in-place unless user has defined style or styleClass attributes
     */
    public static String getCSSClass_dontCascade(
            FacesContext context, UIComponent component,
            String style, StyleGroup styleGroup,
            String styleClass,
            String defaultStyle) {
        if (Rendering.isNullOrEmpty(style) && Rendering.isNullOrEmpty(styleClass)) {
            return getCSSClass(context, component, defaultStyle, styleGroup, null);
        } else {
            return getCSSClass(context, component, style, styleGroup, styleClass);
        }
    }

    //todo: review the need for this method. Main points:
    //  - there's already a family of methods with similar functionality (getCSSClass). The only difference is: there's default _style_ in getStyleClassesStr, while  getCSSClass have default _style class_
    //  - default style is not included in the cascade when user's style is specified

    public static String getStyleClassesStr(FacesContext context, UIComponent component, String style, String styleClass,
                                            String defaultStyle, StyleGroup styleGroup) {
        if (Rendering.isNullOrEmpty(style) && Rendering.isNullOrEmpty(styleClass)) {
            return getCSSClass(context, component, defaultStyle, styleGroup, null);
        } else {
            return getCSSClass(context, component, style, styleGroup, styleClass);
        }
    }

    /**
     * Return registered css style classes
     *
     * @param context {@link FacesContext} for the current request
     * @return registered css style classes
     */
    public static Map getRegisteredStyleClassesMap(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        Map<String, Object> requestMap = externalContext.getRequestMap();
        Map component2styleMap = (Map) requestMap.get(REGISTERED_STYLE_CLASSES);
        if (component2styleMap == null) {
            component2styleMap = new HashMap();
            requestMap.put(REGISTERED_STYLE_CLASSES, component2styleMap);
        }
        return component2styleMap;
    }


    /**
     * Return registered css style classes for given component
     *
     * @param context   {@link FacesContext} for the current request
     * @param component The component for retrieve css style classes
     * @return registered css style classes
     */
    public static Map getComponentStylesMap(FacesContext context, UIComponent component) {
        Map component2styleMap = getRegisteredStyleClassesMap(context);
        Map componentStylesMap = (Map) component2styleMap.get(component);
        if (componentStylesMap == null) {
            componentStylesMap = new HashMap();
            component2styleMap.put(component, componentStylesMap);
        }
        return componentStylesMap;
    }

    /**
     * Transform css style to css class and register css class for given component for given {@link StyleGroup}
     *
     * @param context    {@link FacesContext} for the current request
     * @param style      The style to be registered
     * @param styleGroup The {@link StyleGroup} for which style is registered
     * @param component  The component for register css style classes
     * @return generated css style class name
     */
    public static String registerCssClass(FacesContext context,
                                          String style,
                                          StyleGroup styleGroup,
                                          UIComponent component) {
        if (style == null || style.length() == 0)
            return null;

        Map componentStylesMap = getComponentStylesMap(context, component);
        String alreadyAddedStyleKey = REGISTERED_STYLE_PREFIX + styleGroup + style;

        String alreadyAddedClassName = (String) componentStylesMap.get(alreadyAddedStyleKey);
        if (alreadyAddedClassName != null)
            return alreadyAddedClassName;

        String styleListKey = REGISTERED_CSS_CLASSES_PREFIX + styleGroup;
        List<String> cssClassesList = (List) componentStylesMap.get(styleListKey);
        if (cssClassesList == null) {
            cssClassesList = new ArrayList<String>();
            componentStylesMap.put(styleListKey, cssClassesList);
        }

        String className = GENERATED_CLASS_NAME_PREFIX + getNextStyleIndex();

        cssClassesList.add("." + className + "{" + style + "}");
        componentStylesMap.put(alreadyAddedStyleKey, className);

        return className;
    }

    public static synchronized long getNextStyleIndex() {
        styleIndexCounter++;
        if (styleIndexCounter < 0)
            styleIndexCounter = 0;
        return styleIndexCounter;
    }

    /**
     * Render style classes for given component
     *
     * @param context   {@link FacesContext} for the current request
     * @param component The component, which styles are rendered
     * @throws IOException if an input/output error occurs while rendering
     * @see #renderStyleClasses(javax.faces.context.FacesContext, javax.faces.component.UIComponent, boolean, boolean)
     */
    public static void renderStyleClasses(FacesContext context, UIComponent component) throws IOException {
        renderStyleClasses(context, component, false, false);
    }

    /**
     * Render style classes for given component
     *
     * @param context             {@link FacesContext} for the current request
     * @param component           The component, which styles are rendered
     * @param forcedStyleAsScript The parameter, indicating way of rendering styles
     * @throws IOException if an input/output error occurs while rendering
     */
    public static void renderStyleClasses(FacesContext context, UIComponent component,
                                          boolean forcedStyleAsScript, boolean forceStyleAsOnloadScript) throws IOException {
        requestDefaultCss(context);
        List<String> cssClasses = getAllStyleClassesForComponent(context, component);
        if (cssClasses != null && cssClasses.size() > 0) {
            String stylesId = component.getClientId(context) + STYLES_ID_SUFFIX;
            Set<String> elementsIds = getRenderedStyleElementsIds(context);
            int suffix = 1;
            while (elementsIds.contains(stylesId)) {
                stylesId = component.getClientId(context) + STYLES_ID_SUFFIX + suffix++;
//        logWarning(context, "Style elements with id \"" + stylesId + "\" already rendered. Component: " + component);

                // warning was removed because there are cases when renderStyleClasses should be invoked several times per component - see AbstractTableRenderer as an example
            }
            elementsIds.add(stylesId);
            if (Environment.isExplorer() || Environment.isMozillaXhtmlPlusXmlContentType(context)
                    || forcedStyleAsScript) {
                // Case for fixing JSFC-2341. Style tags added to DOM using JavaScript are ignored by Mozilla with
                // "application/xhtml+xml" content-type. So to fix this, the styles are added using stylesheet API.
                // This way of adding styles works on all browsers except Safari 2, however adding styles in this way
                // is slightly slower than just adding <style> tags to DOM, so that's why this approach is not used
                // under Chrome and Opera
                //
                // OF-28: IE limits the number of <style> tags (and stylesheets in general) to 31! So we're using
                // programmatic script addition which uses the single stylesheet created dynamically to address this limitation
                writeCssClassesAsScriptElement(context, cssClasses, forceStyleAsOnloadScript);
            } else {
                writeCssClassesAsStyleElement(context, component, stylesId, cssClasses);
            }
        }
        markStylesRenderedForComponent(context, component);
    }

    /**
     * Render javascript, which add css rules
     *
     * @param context  {@link FacesContext} for the current request
     * @param cssRules The list of css rules for rendering
     * @throws IOException if an input/output error occurs while rendering
     */
    public static void writeCssClassesAsScriptElement(FacesContext context, List<String> cssRules, boolean asOnloadScript) throws IOException {
        ScriptBuilder styleRegistrationScript = new ScriptBuilder().functionCall("O$.addCssRules", cssRules).semicolon();
        if (asOnloadScript)
            Rendering.appendOnLoadScript(context, styleRegistrationScript);
        else
            Rendering.renderInitScript(context, styleRegistrationScript, Resources.getUtilJsURL(context));
    }

    /**
     * Render inline styles
     *
     * @param context    {@link FacesContext} for the current request
     * @param cssClasses The list of css rules for rendering
     * @throws IOException if an input/output error occurs while rendering
     */
    public static void writeCssClassesAsStyleElement(
            FacesContext context,
            UIComponent component,
            String stylesId,
            List<String> cssClasses) throws IOException {
        ResponseWriter responseWriter = context.getResponseWriter();
        responseWriter.startElement("style", component);
        responseWriter.writeAttribute("type", "text/css", null);
        responseWriter.writeAttribute("id", stylesId, null);
        StringBuilder result = new StringBuilder();
        for (String cssRule : cssClasses) {
            result.append(cssRule).append('\n');
        }
        responseWriter.writeText(result.toString(), null);
        responseWriter.endElement("style");
    }

    /**
     * Remove all component registered styles
     *
     * @param context   {@link FacesContext} for the current request
     * @param component The component, which styles are removed
     */
    public static void markStylesRenderedForComponent(FacesContext context, UIComponent component) {
        getRegisteredStyleClassesMap(context).remove(component);
    }

    /**
     * Return ids of elements with rendered style
     *
     * @param context {@link FacesContext} for the current request
     * @return set of elements ids
     */
    public static Set<String> getRenderedStyleElementsIds(FacesContext context) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        Set<String> renderedStyleElementsIds = (Set<String>) requestMap.get(RENDERED_STYLE_ELEMENTS_IDS);
        if (renderedStyleElementsIds == null) {
            renderedStyleElementsIds = new HashSet<String>();
            requestMap.put(RENDERED_STYLE_ELEMENTS_IDS, renderedStyleElementsIds);
        }
        return renderedStyleElementsIds;
    }

    /**
     * Return list of all components css style classes
     *
     * @param context   {@link FacesContext} for the current request
     * @param component The component to analyse
     * @return list of all components css style classes
     */
    public static List<String> getAllStyleClassesForComponent(FacesContext context, UIComponent component) {
        Map stylesMap = getComponentStylesMap(context, component);
        List<String> result = new ArrayList<String>();

        for (Iterator<String> iterator = getClassKeyIterator(); iterator.hasNext();) {
            Object key = iterator.next();
            Object requestList = stylesMap.get(key.toString());
            if (requestList != null) {
                result.addAll((List<String>) requestList);
            }
        }
        return result;
    }

    /**
     * Return iterator to iterate over all style groups
     *
     * @return iterator to iterate over all style groups
     */
    public static Iterator<String> getClassKeyIterator() {
        List<String> newClassKeys = new ArrayList<String>();
        SortedSet<StyleGroup> allGroups = StyleGroup.getAllGroups();
        for (StyleGroup group : allGroups) {
            newClassKeys.add(REGISTERED_CSS_CLASSES_PREFIX + group);
        }
        return newClassKeys.iterator();
    }

    /**
     * Add default css to request
     *
     * @param context {@link FacesContext} for the current request
     */
    public static void requestDefaultCss(FacesContext context) {
        context.getExternalContext().getRequestMap().put(DEFAULT_CSS_REQUESTED, Boolean.TRUE);
    }

    /**
     * Check css style declaration for semicolumn ending and add it, if nessesary
     *
     * @param style The css style declaration to check
     * @return css style with semicolumn ending
     */
    public static String checkCSSStyleForSemicolumn(String style) {
        if (style == null) {
            return style;
        }
        if (!style.endsWith(";")) {
            StringBuilder stringBuffer = new StringBuilder();
            stringBuffer.append(style).append(";");
            style = stringBuffer.toString();
        }
        return style;
    }

    /**
     * Merge css styles
     *
     * @param style1 The first css style for merge
     * @param style2 The second css style for merge
     * @return merged css style declaration
     */
    public static String mergeStyles(String style1, String style2) {
        if (Rendering.isNullOrEmpty(style1))
            return style2;
        if (Rendering.isNullOrEmpty(style2))
            return style1;
        style1 = style1.trim();
        style2 = style2.trim();
        if (!style1.endsWith(";"))
            style1 = style1 + ";";
        return style1 + " " + style2;
    }

    /**
     * Add style parameter to JSON object for regular {@link StyleGroup}
     *
     * @param context       {@link FacesContext} for the current request
     * @param component     The component, which style will be added
     * @param paramsObject  The JSON object, to which style parameter will be added
     * @param jsonFieldName The key
     * @param style         The style to merge with component style
     * @param styleClass    The style class to merge with component style class
     * @see #addStyleJsonParam(javax.faces.context.FacesContext, javax.faces.component.UIComponent, org.openfaces.org.json.JSONObject, String, String, String, StyleGroup)
     */
    public static void addStyleJsonParam(FacesContext context, UIComponent component, JSONObject paramsObject,
                                         String jsonFieldName, String style, String styleClass) {
        addStyleJsonParam(context, component, paramsObject, jsonFieldName, style, styleClass, StyleGroup.regularStyleGroup());
    }

    /**
     * Add style parameter to JSON object
     *
     * @param context       {@link FacesContext} for the current request
     * @param component     The component, which style will be added
     * @param paramsObject  The JSON object, to which style parameter will be added
     * @param jsonFieldName The key
     * @param style         The style to merge with component style
     * @param styleClass    The style class to merge with component style class
     * @param styleGroup    The {@link StyleGroup} for which retrieve css class
     * @see #addStyleJsonParam(javax.faces.context.FacesContext, javax.faces.component.UIComponent, org.openfaces.org.json.JSONObject, String, String, String, StyleGroup)
     */
    public static void addStyleJsonParam(FacesContext context, UIComponent component, JSONObject paramsObject,
                                         String jsonFieldName, String style, String styleClass, StyleGroup styleGroup) {
        String clsName = Styles.getCSSClass(context, component, style, styleGroup, styleClass);
        if (clsName == null)
            return;
        try {
            paramsObject.put(jsonFieldName, clsName);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

}
