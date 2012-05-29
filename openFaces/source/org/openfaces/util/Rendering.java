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
package org.openfaces.util;

import org.openfaces.application.DynamicResource;
import org.openfaces.application.OpenFacesApplication;
import org.openfaces.application.OpenFacesResourceHandler;
import org.openfaces.application.ViewExpiredExceptionHandler;
import org.openfaces.component.OUIClientAction;
import org.openfaces.component.OUICommand;
import org.openfaces.component.OUIComponent;
import org.openfaces.component.OUIInput;
import org.openfaces.component.ajax.AjaxInitializer;
import org.openfaces.component.output.GraphicText;
import org.openfaces.org.json.JSONArray;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.org.json.JSONString;
import org.openfaces.renderkit.DefaultImageDataModel;
import org.openfaces.renderkit.ImageDataModel;
import org.openfaces.renderkit.cssparser.CSSUtil;
import org.openfaces.renderkit.output.DynamicImageRenderer;

import javax.el.ELContext;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.Resource;
import javax.faces.component.EditableValueHolder;
import javax.faces.component.UIComponent;
import javax.faces.component.UIForm;
import javax.faces.component.UIInput;
import javax.faces.component.UIParameter;
import javax.faces.component.UIViewRoot;
import javax.faces.component.ValueHolder;
import javax.faces.component.behavior.ClientBehavior;
import javax.faces.component.behavior.ClientBehaviorContext;
import javax.faces.component.behavior.ClientBehaviorHolder;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.context.PartialViewContext;
import javax.faces.context.ResponseWriter;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.imageio.ImageIO;
import javax.portlet.PortletSession;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Array;
import java.text.DateFormatSymbols;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * This class contain methods for working with components tree and client-side javascript
 *
 * @author Dmitry Pikhulya
 */
public class Rendering {
    public static final String CLIENT_ID_SUFFIX_SEPARATOR = "::";
    public static final String SERVER_ID_SUFFIX_SEPARATOR = "--";

    public static final String DEFAULT_FOCUSED_STYLE = "border: 1px dotted black;";
    public static final String ON_LOAD_SCRIPTS_KEY = UtilPhaseListener.class.getName() + ".loadScripts";
    private static final Class<?> A4J_AJAX_BEHAVIOR_CLASS;

    static {
        Class<?> cls;
        try {
            cls = Class.forName("org.ajax4jsf.component.behavior.AjaxBehavior");
        } catch (ClassNotFoundException e) {
            cls = null;
        }
        A4J_AJAX_BEHAVIOR_CLASS = cls;
    }

    private Rendering() {
    }

    /**
     * This method render all children of a given component
     *
     * @param context   {@link FacesContext} for the current request
     * @param component The component, which children will be rendered
     * @throws IOException if an input/output error occurs while rendering
     */
    public static void renderChildren(FacesContext context, UIComponent component) throws IOException {
        if (component.getChildCount() == 0)
            return;

        List<UIComponent> components = component.getChildren();
        renderComponents(context, components);
    }

    /**
     * This method calls {@link javax.faces.component.UIComponent#encodeAll(javax.faces.context.FacesContext)} with a given context for all
     * passed components
     *
     * @param context    {@link FacesContext} for the current request
     * @param components The list of components to be rendered
     * @throws IOException if an input/output error occurs while rendering
     */
    public static void renderComponents(FacesContext context, List<UIComponent> components) throws IOException {
        for (UIComponent child : components) {
            child.encodeAll(context);
        }
    }

    /**
     * Log OpenFaces warning to external context
     *
     * @param context {@link FacesContext} for the current request
     * @param message The message to log
     */
    public static void logWarning(FacesContext context, String message) {
        Log.log(context, "OpenFaces library warning: " + message);
    }

    /**
     * Write attribute and replace <code>styleClass</code> attribute with <code>style</code>
     *
     * @param writer            The character-based output
     * @param attrName          Attribute name to be added
     * @param value             Attribute value to be added
     * @param componentProperty Name of the property or attribute (if any) of the
     *                          {@link UIComponent} associated with the containing element,
     *                          to which this generated attribute corresponds
     * @throws IOException if an input/output error occurs
     */
    public static void renderHTMLAttribute(ResponseWriter writer, String componentProperty, String attrName,
                                           Object value) throws IOException {
        if (!Rendering.isDefaultAttributeValue(value)) {
            // render JSF "styleClass" attribute as "class"
            String htmlAttrName = attrName.equals("styleClass") ? "class" : attrName;
            writer.writeAttribute(htmlAttrName, value, componentProperty);
        }
    }

    /**
     * Write javascript start html tag
     *
     * @param writer    The character-based output
     * @param component The {@link UIComponent} (if any) to which
     *                  this element corresponds
     * @throws IOException if an input/output error occurs
     */
    public static void renderJavascriptStart(ResponseWriter writer, UIComponent component) throws IOException {
        writer.startElement("script", component);
        writer.writeAttribute("type", "text/javascript", null);
        writer.write("\n");
    }

    /**
     * Write javascript start html tag
     *
     * @param writer The character-based output
     * @throws IOException if an input/output error occurs
     */
    public static void renderJavascriptEnd(ResponseWriter writer) throws IOException {
        writer.write("\n");
        writer.endElement("script");
    }

    private static List<String> allButEmpty(String[] strings) {
        List<String> result = new ArrayList<String>();
        for (String string : strings) {
            if (string != null && string.trim().length() > 0)
                result.add(string);
        }
        return result;
    }

    /**
     * Render javascript initiation of DateTimeFormatObject
     *
     * @param locale The locale for DateTimeFormat
     * @throws IOException if an input/output error occurs
     */
    public static void registerDateTimeFormatObject(Locale locale) throws IOException {
        ScriptBuilder sb = new ScriptBuilder();

        DateFormatSymbols dfs = new DateFormatSymbols(locale);
        sb.functionCall("O$.initDateTimeFormatObject",
                allButEmpty(dfs.getMonths()),
                allButEmpty(dfs.getShortMonths()),
                allButEmpty(dfs.getWeekdays()),
                allButEmpty(dfs.getShortWeekdays()),
                locale);

        FacesContext context = FacesContext.getCurrentInstance();
        renderInitScript(context, sb,
                Resources.utilJsURL(context),
                Resources.internalURL(context, "input/DateTimeFormat.js"));
    }

    /**
     * Render the hidden field
     *
     * @param writer    The character-based output
     * @param idAndName The id and name of hidden field
     * @param value     The value of hidden field
     * @throws IOException if an input/output error occurs
     */
    public static void renderHiddenField(ResponseWriter writer, String idAndName, String value) throws IOException {
        writer.startElement("input", null);
        writer.writeAttribute("type", "hidden", null);
        writer.writeAttribute("id", idAndName, null);
        writer.writeAttribute("name", idAndName, null);
        if (value != null)
            writer.writeAttribute("value", value, null);
        writer.endElement("input");
    }

    /**
     * Retrieve boolean value of the component attribute or default
     *
     * @param component    The component
     * @param attrName     The attribute name
     * @param defaultValue The default value to return if such attribute doesn't exist
     * @return boolean value of a given attribute or default value if not found
     */
    public static boolean getBooleanAttribute(UIComponent component, String attrName, boolean defaultValue) {
        Boolean b = (Boolean) component.getAttributes().get(attrName);
        return b != null ? b : defaultValue;
    }

    /**
     * See JSF Spec. 8.5 DataTable 8-1
     *
     * @param value The value to check
     * @return true if value is null or default value, false otherwise
     */
    public static boolean isDefaultAttributeValue(Object value) {
        if (value == null) {
            return true;
        } else if (value instanceof Boolean) {
            return !(Boolean) value;
        } else if (value instanceof Number) {
            if (value instanceof Integer) {
                return ((Number) value).intValue() == Integer.MIN_VALUE;
            } else if (value instanceof Double) {
                return ((Number) value).doubleValue() == Double.MIN_VALUE;
            } else if (value instanceof Long) {
                return ((Number) value).longValue() == Long.MIN_VALUE;
            } else if (value instanceof Byte) {
                return ((Number) value).byteValue() == Byte.MIN_VALUE;
            } else if (value instanceof Float) {
                return ((Number) value).floatValue() == Float.MIN_VALUE;
            } else if (value instanceof Short) {
                return ((Number) value).shortValue() == Short.MIN_VALUE;
            }
        }
        return false;
    }

    static String escapeStringForJS(String str) {
        if (str == null)
            return "";

        int len = str.length();
        StringBuilder buf = new StringBuilder(len << 2);
        for (int i = 0; i < len; i++) {
            char chr = str.charAt(i);
            switch (chr) {
                case '\\':
                    buf.append("\\x5c");
                    break;
                case '\'':
                    buf.append("\\x27");
                    break;
                case '\"':
                    buf.append("\\x22");
                    break;
                case '\n':
                    buf.append("\\n");
                    break;
                case '[':
                    buf.append("\\x5b");
                    break;
                case ']':
                    buf.append("\\x5d");
                    break;
                case '\r':
                    buf.append("\\r");
                    break;
                case '<':
                    buf.append("\\x3C");
                    break;
                default:
                    if (((int) chr) >= 0x80) {
                        String hex = Integer.toString(chr, 16);
                        buf.append("\\u");
                        switch (hex.length()) {
                            case 2:
                                buf.append("00");
                                break;
                            case 3:
                                buf.append('0');
                        }
                        buf.append(hex);
                    } else {
                        buf.append(chr);
                    }
            }
        }

        return buf.toString();
    }

    /**
     * Render component id attribute if it is not null and not starts with {@link UIViewRoot#UNIQUE_ID_PREFIX}
     *
     * @param writer    The character-based output
     * @param component The component, which id will be written
     * @param context   {@link FacesContext} for the current request
     * @throws IOException if an input/output error occurs
     */
    public static void writeIdIfNecessary(ResponseWriter writer, UIComponent component,
                                          FacesContext context) throws IOException {
        if (component.getId() != null && !component.getId().startsWith(UIViewRoot.UNIQUE_ID_PREFIX)) {
            writer.writeAttribute("id", component.getClientId(context), null);
        }
    }

    /**
     * Retrieve array of converted submitted values from {@link javax.faces.component.UISelectMany} component
     *
     * @param context        {@link FacesContext} for the current request
     * @param selectMany     The submitted component
     * @param submittedValue The submitted value
     * @return converted value
     * @throws ConverterException if submittedValue is not a String array
     */
    public static Object getConvertedUISelectManyValue(FacesContext context,
                                                       UIInput selectMany,
                                                       Object submittedValue)
            throws ConverterException {
        if (submittedValue == null) {
            return null;
        }
        if (!(submittedValue instanceof String[])) {
            throw new ConverterException("Submitted value of type String[] expected");
        }
        return getConvertedUISelectManyValue(context,
                selectMany,
                (String[]) submittedValue);
    }

    private static Object getConvertedUISelectManyValue(FacesContext context,
                                                        UIInput component,
                                                        String[] submittedValue) {
        // Attention!
        // This code is duplicated in jsfapi component package.
        // If you change something here please do the same in the other class!

        if (submittedValue == null) throw new NullPointerException("submittedValue");

        ValueExpression ve = component.getValueExpression("value");
        Class valueType = null;
        Class arrayComponentType = null;
        ELContext elContext = context.getELContext();
        if (ve != null) {
            valueType = ve.getType(elContext);
            if (valueType != null && valueType.isArray()) {
                arrayComponentType = valueType.getComponentType();
            }
        }

        Converter converter = component.getConverter();
        if (converter == null) {
            if (valueType == null) {
                // No converter, and no idea of expected type
                // --> return the submitted String array
                return submittedValue;
            }

            if (List.class.isAssignableFrom(valueType)) {
                // expected type is a List
                // --> according to javadoc of UISelectMany we assume that the element type
                //     is java.lang.String, and copy the String array to a new List
                int len = submittedValue.length;
                List<String> lst = new ArrayList<String>(len);
                lst.addAll(Arrays.asList(submittedValue).subList(0, len));
                return lst;
            }

            if (arrayComponentType == null) {
                throw new IllegalArgumentException("ValueExpression for UISelectMany must be of type List or Array");
            }

            if (String.class.equals(arrayComponentType)) return submittedValue; // No conversion needed for String type
            if (Object.class.equals(arrayComponentType)) return submittedValue; // No conversion for Object class

            try {
                converter = context.getApplication().createConverter(arrayComponentType);
            } catch (FacesException e) {
                return submittedValue;
            }
        }

        // Now, we have a converter...
        // We determine the type of the component array after converting one of it's elements
        if (ve != null) {
            valueType = ve.getType(elContext);
            if (valueType != null && valueType.isArray()) {
                if (submittedValue.length > 0) {
                    arrayComponentType = converter.getAsObject(context, component, submittedValue[0]).getClass();
                }
            }
        }

        if (valueType == null) {
            // ...but have no idea of expected type
            // --> so let's convert it to an Object array
            int len = submittedValue.length;
            Object[] convertedValues = (Object[]) Array.newInstance(
                    arrayComponentType == null ? Object.class : arrayComponentType, len);
            for (int i = 0; i < len; i++) {
                convertedValues[i]
                        = converter.getAsObject(context, component, submittedValue[i]);
            }
            return convertedValues;
        }

        if (List.class.isAssignableFrom(valueType)) {
            // Curious case: According to specs we should assume, that the element type
            // of this List is java.lang.String. But there is a Converter set for this
            // component. Because the user must know what he is doing, we will convert the values.
            int len = submittedValue.length;
            List<Object> lst = new ArrayList<Object>(len);
            for (int i = 0; i < len; i++) {
                lst.add(converter.getAsObject(context, component, submittedValue[i]));
            }
            return lst;
        }

        if (arrayComponentType == null) {
            throw new IllegalArgumentException("ValueExpression for UISelectMany must be of type List or Array");
        }

        if (arrayComponentType.isPrimitive()) {
            // primitive array
            int len = submittedValue.length;
            Object convertedValues = Array.newInstance(arrayComponentType, len);
            for (int i = 0; i < len; i++) {
                Array.set(convertedValues, i,
                        converter.getAsObject(context, component, submittedValue[i]));
            }
            return convertedValues;
        } else {
            // Object array
            int len = submittedValue.length;
            ArrayList convertedValues = new ArrayList(len);
            for (int i = 0; i < len; i++) {
                convertedValues.add(i, converter.getAsObject(context, component, submittedValue[i]));
            }
            return convertedValues.toArray((Object[]) Array.newInstance(arrayComponentType, len));
        }
    }

    public static Converter getConverter(FacesContext context, ValueHolder component) {
        Converter converter = component.getConverter();
        if (converter == null) {
            ValueExpression ve = ((UIComponent) component).getValueExpression("value");
            if (ve == null)
                return null;
            Class valueType = ve.getType(context.getELContext());
            converter = getConverterForType(context, valueType);
        }
        return converter;
    }

    public static Converter getConverterForType(FacesContext context, Class valueType) {
        if (valueType == null || valueType == String.class || valueType == Object.class)
            return null;
        Application application = context.getApplication();
        return application.createConverter(valueType);
    }

    /**
     * Converts string to object with associated converter
     *
     * @param context   {@link FacesContext} for the current request
     * @param component The component, which converter will be used
     * @param str       The string to convert
     * @return converted object or string if converter was not found
     */
    public static Object convertFromString(FacesContext context, ValueHolder component, String str) {
        Converter converter = getConverter(context, component);
        if (converter == null)
            return str;
        Object result = converter.getAsObject(context, (UIComponent) component, str);
        return result;
    }

    /**
     * Return converted to string value of component
     *
     * @param context   {@link FacesContext} for the current request
     * @param component The component, which value will be converted
     * @return converted value on this component
     */
    public static String getStringValue(FacesContext context, ValueHolder component) {
        if (component instanceof EditableValueHolder) {
            Object submittedValue = ((EditableValueHolder) component).getSubmittedValue();
            if (submittedValue != null)
                return (String) submittedValue;
        }
        return convertToString(context, component, component.getValue());
    }

    /**
     * Converts object to string with associated converter
     *
     * @param context   {@link FacesContext} for the current request
     * @param component The component, which converter will be used
     * @param value     The object to convert
     * @return converted string
     */
    public static String convertToString(FacesContext context, ValueHolder component, Object value) {
        Converter converter = getConverter(context, component);

        if (converter == null)
            return (value != null) ? value.toString() : "";

        if (value != null)
            return converter.getAsString(context, (UIComponent) component, value);
        else
            return "";
    }

    /**
     * Appends javascript to be executed on onLoad phase on client browser
     *
     * @param context {@link FacesContext} for the current request
     * @param script  The script to append
     */
    public static void appendOnLoadScript(FacesContext context, Script script) {
        if (script == null) return;
        String scriptStr = script.toString();
        if (scriptStr.length() == 0)
            return;
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        StringBuilder buf = (StringBuilder) requestMap.get(ON_LOAD_SCRIPTS_KEY);
        if (buf == null) {
            buf = new StringBuilder();
            requestMap.put(ON_LOAD_SCRIPTS_KEY, buf);
        }
        buf.append(scriptStr);
    }

    /**
     * Renders the specified JavaScript code into the response and ensures including the specified JavaScript files to
     * the rendered page prior to the rendered script.
     *
     * @param context {@link FacesContext} for the current request
     * @param script  The javascript to be rendered
     * @param jsFiles The collection of javascript files to be added
     * @throws IOException if an input/output error occurs
     */
    public static void renderInitScript(FacesContext context, Script script, String... jsFiles) throws IOException {
        String initScript = script.toString();
        if (initScript == null || initScript.trim().length() == 0) {
            return;
        }
        if (jsFiles != null)
            for (String jsFile : jsFiles) {
                if (jsFile == null) continue;
                Resources.renderJSLinkIfNeeded(context, jsFile);
            }
        if (OpenFacesApplication.isConstructingView(context)) {
            Resources.addHeaderInitScript(context, script);
            return;
        }

        PartialViewContext partialViewContext = context.getPartialViewContext();
        if (partialViewContext.isAjaxRequest() && /*Environment.isMozilla() && */
                (!AjaxUtil.isAjaxPortionRequest(context) || ViewExpiredExceptionHandler.isExpiredView(context))) {
            // JSF 2.0.2 (Mojarra) Ajax doesn't auto-execute in-place scripts under FireFox 3.6.3 transitional mode,
            // so we're simulating this here
            if (partialViewContext.isAjaxRequest()) {
                List<InitScript> initScripts = getAjaxInitScripts(context);
                initScripts.add(new InitScript(script, jsFiles));
            }
        } else {
            ResponseWriter writer = context.getResponseWriter();
            renderJavascriptStart(writer, null);
            writer.writeText(initScript, null);
            renderJavascriptEnd(writer);
        }
    }

    public static List<InitScript> getAjaxInitScripts(FacesContext context) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        String key = Rendering.class.getName() + ".ajaxInitScripts";
        List<InitScript> list = (List<InitScript>) requestMap.get(key);
        if (list == null) {
            list = new ArrayList<InitScript>();
            requestMap.put(key, list);
        }
        return list;
    }

    /**
     * Combine javascript scripts into one script and links to javascript files and render them
     *
     * @param context     {@link FacesContext} for the current request
     * @param initScripts The javascript files to combine and render
     * @throws IOException if an input/output error occurs
     */
    public static void renderInitScripts(FacesContext context, InitScript... initScripts) throws IOException {
        String combinedInitScript = null;
        Set<String> combinedJsFiles = new LinkedHashSet<String>();
        for (InitScript initScript : initScripts) {
            Script script = initScript.getScript();
            String[] jsFiles = initScript.getJsFiles();
            combinedInitScript = joinScripts(combinedInitScript, script.toString());
            combinedJsFiles.addAll(Arrays.asList(jsFiles));
        }

        renderInitScript(context,
                new RawScript(combinedInitScript),
                combinedJsFiles.toArray(new String[combinedJsFiles.size()]));
    }

    /**
     * Render javascript for preloading images
     *
     * @param context            {@link FacesContext} for the current request
     * @param imageUrls          The list of images to preload
     * @param prependContextPath true means that the resulting url should be prefixed with context root. This is the case
     *                           when the returned URL is rendered without any modifications. Passing false to this
     *                           parameter is required in cases when the returned URL is passed to some component which
     *                           expects application URL, so the component will prepend the URL with context root itself.
     * @throws IOException if an input/output error occurs
     */
    public static void renderPreloadImagesScript(
            FacesContext context,
            List<String> imageUrls,
            boolean prependContextPath
    ) throws IOException {
        List<String> preparedImageUrls = new ArrayList<String>();
        for (String imageUrl : imageUrls) {
            String preparedUrl = prependContextPath
                    ? Resources.applicationURL(context, imageUrl)
                    : imageUrl;
            preparedImageUrls.add(preparedUrl);
        }

        Rendering.renderInitScript(context,
                new ScriptBuilder().functionCall("O$.preloadImages", preparedImageUrls).semicolon(),
                Resources.utilJsURL(context));
    }

    /**
     * Return true if component is inside form component, throw exception otherwise
     *
     * @param component The component to check
     */
    public static void ensureComponentInsideForm(UIComponent component) {
        UIForm form = Components.findForm(component);
        if (form == null) {
            FacesContext context = FacesContext.getCurrentInstance();
            throw new FacesException("The following component has been detected to be located outside of the form. " +
                    "Please place it inside of <h:form> tag, or any equivalent JSF form tag. " +
                    "Component id: " + component.getClientId(context) + " ; Component class: " + component.getClass().getName());
        }
    }

    /**
     * Checks the string for null and emptiness.
     *
     * @param string The string to check
     * @return true, if string is null or empty, false otherwise
     */
    public static boolean isNullOrEmpty(String string) {
        return string == null || string.trim().length() == 0;
    }

    /**
     * Join two javascripts into one
     *
     * @param script1 The first script to join
     * @param script2 The second script to join
     * @return joined resulting javascript
     */
    public static String joinScripts(String script1, String script2) {
        if (script1 == null)
            return script2;
        if (script2 == null)
            return script1;
        script1 = script1.trim();
        String result = script1.endsWith(";") ? script1 + script2 : script1 + ";" + script2;
        return result;
    }

    /**
     * Write {@link HTML#NBSP_ENTITY} to writer
     *
     * @param writer The character-based output
     * @throws IOException if an input/output error occurs
     */
    public static void writeNonBreakableSpace(ResponseWriter writer) throws IOException {
        writer.write(HTML.NBSP_ENTITY);
    }

    /**
     * Write style and class html attributes
     *
     * @param writer     The character-based output
     * @param style      The value of css style attribute to render
     * @param styleClass The value of css style class attribute to render
     * @throws IOException if an input/output error occurs
     */
    public static void writeStyleAndClassAttributes(ResponseWriter writer, String style, String styleClass) throws IOException {
        if (!Rendering.isNullOrEmpty(style))
            writer.writeAttribute("style", style, null);
        if (!Rendering.isNullOrEmpty(styleClass))
            writer.writeAttribute("class", styleClass, null);
    }

    public static void writeStyleAndClassAttributes(ResponseWriter writer, OUIComponent component) throws IOException {
        writeStyleAndClassAttributes(writer, component.getStyle(), component.getStyleClass());
    }

    /**
     * Write style and class html attributes
     *
     * @param writer            The character-based output
     * @param style             The value of css style attribute to render
     * @param styleClass        The value of css style class attribute to render
     * @param defaultStyleClass The default css style class to merge with a given css style
     * @throws IOException if an input/output error occurs
     * @see #writeStyleAndClassAttributes(javax.faces.context.ResponseWriter, String, String)
     */
    public static void writeStyleAndClassAttributes(
            ResponseWriter writer, String style, String styleClass, String defaultStyleClass) throws IOException {
        String resultStyleClass = Styles.mergeClassNames(styleClass, defaultStyleClass);
        writeStyleAndClassAttributes(writer, style, resultStyleClass);
    }

    public static void writeStyleAndClassAttributes(
            ResponseWriter writer, UIComponent component, String styleName, String defaultStyleClass) throws IOException {
        String styleValue = (String) component.getAttributes().get(styleName + "Style");
        String classValue = (String) component.getAttributes().get(styleName + "Class");
        writeStyleAndClassAttributes(writer, styleValue, classValue, defaultStyleClass);
    }

    /**
     * Render component css style class attribute
     *
     * @param writer    The character-based output
     * @param component The component, which attribute renders
     * @throws IOException if an input/output error occurs
     */
    public static void writeComponentClassAttribute(ResponseWriter writer, OUIComponent component) throws IOException {
        writeComponentClassAttribute(writer, component, null);
    }

    /**
     * Render component css style class attribute
     *
     * @param writer       The character-based output
     * @param component    The component, which attribute renders
     * @param defaultClass The default style class to merge with
     * @throws IOException if an input/output error occurs
     */
    public static void writeComponentClassAttribute(ResponseWriter writer, OUIComponent component, String defaultClass) throws IOException {
        String cssClass = Styles.getCSSClass(FacesContext.getCurrentInstance(),
                (UIComponent) component, component.getStyle(), defaultClass, component.getStyleClass());
        if (cssClass != null)
            writer.writeAttribute("class", cssClass, null);
    }

    /**
     * Render image html tag with hack for ie transparency
     *
     * @param writer    The character-based output
     * @param context   {@link FacesContext} for the current request
     * @param component The {@link UIComponent} (if any) to which this element corresponds
     * @param extension The image extension
     * @param model     The data model of image - array of byte
     * @param size      The array of integers, where first element is width of image and second is a height
     * @throws IOException if an input/output error occurs
     */
    public static void startWriteIMG(ResponseWriter writer, FacesContext context,
                                     UIComponent component, String extension,
                                     ImageDataModel model, int[] size) throws IOException {
        Components.generateIdIfNotSpecified(component);
        writeNewLine(writer);
        writer.startElement("img", component);

        String imageUrl;
        if (model != null) {
            DynamicImagePool imagePool = getImagePool(context);
            String id = imagePool.putModel(model);
            writer.writeAttribute("id", component.getClientId(context), null);

            String imagePath = OpenFacesResourceHandler.DYNAMIC_RESOURCE_IDENTIFIER + "/" + id + "." + extension;
            Resource resource = new DynamicResource(imagePath);
            imageUrl = resource.getRequestPath();
        } else {
            imageUrl = "";
        }

        if (Environment.isExplorer() && component instanceof GraphicText) {
            writeAttribute(writer, "src", getClearGif(context));
            writeAttribute(writer, "style", "filter:progid:DXImageTransform.Microsoft.AlphaImageLoader(src='" + imageUrl + "', sizingMethod='scale');");
            if (size != null) {
                writeAttribute(writer, "width", String.valueOf(size[0]));
                writeAttribute(writer, "height", String.valueOf(size[1]));
            }
        } else {
            writeAttribute(writer, "src", imageUrl);
        }
    }

    /**
     * Return URL to clear.gif image
     *
     * @param context {@link FacesContext} for the current request
     * @return URL to clear.gif image
     */
    private static String getClearGif(FacesContext context) {
        return Resources.internalURL(context, "clear.gif");
    }

    /**
     * Return the current session's in-memory image pool
     *
     * @param context {@link FacesContext} for the current request
     * @return the current session's in-memory image pool
     */
    public static DynamicImagePool getImagePool(FacesContext context) {
        if (AjaxUtil.isPortletRequest(context)) {
            Object session = context.getExternalContext().getSession(false);
            synchronized (session) {
                PortletSession portletSession = ((PortletSession) session);
                DynamicImagePool imagePool = (DynamicImagePool) portletSession.getAttribute(
                        DynamicImageRenderer.IMAGE_POOL, PortletSession.APPLICATION_SCOPE);
                if (imagePool == null) {
                    imagePool = new DynamicImagePool();
                    portletSession.setAttribute(DynamicImageRenderer.IMAGE_POOL, imagePool, PortletSession.APPLICATION_SCOPE);
                }
                return imagePool;
            }
        }

        Map<String, Object> sessionMap = context.getExternalContext().getSessionMap();
        synchronized (sessionMap) {
            DynamicImagePool imagePool = (DynamicImagePool) sessionMap.get(DynamicImageRenderer.IMAGE_POOL);
            if (imagePool == null) {
                imagePool = new DynamicImagePool();
                sessionMap.put(DynamicImageRenderer.IMAGE_POOL, imagePool);
            }
            return imagePool;
        }
    }

    /**
     * Create image data model from array of bytes or {@link java.awt.image.RenderedImage}
     *
     * @param data The data to create model
     * @return created image data model or null
     */
    public static ImageDataModel getDataModel(Object data) {
        if (data == null)
            return null;

        ImageDataModel model = null;

        if (data instanceof byte[])
            model = getDataModel((byte[]) data);
        else if (data instanceof RenderedImage)
            model = getDataModel((RenderedImage) data);
//    else // todo: throw meaningful exception describing allowed types here
//      throw new IllegalArgumentException("");

        return model;
    }

    /**
     * Create image data model from array of bytes
     *
     * @param data The array of bytes, representing image
     * @return image model
     */
    public static ImageDataModel getDataModel(byte[] data) {
        if (data == null)
            return null;

        DefaultImageDataModel model = new DefaultImageDataModel();
        model.setData(data);

        return model;
    }

    /**
     * Create image data model from {@link java.awt.image.RenderedImage}
     *
     * @param data The {@link java.awt.image.RenderedImage}, representing png image
     * @return image model
     */
    public static ImageDataModel getDataModel(RenderedImage data) {
        if (data == null)
            return null;

        byte[] byteArray = encodeAsPNG(data);
        DefaultImageDataModel model = new DefaultImageDataModel();
        model.setData(byteArray);
        return model;
    }

    /**
     * Encode png image as array of bytes
     *
     * @param data The png image
     * @return the array of bytes, representing a given image
     */
    public static byte[] encodeAsPNG(RenderedImage data) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        ImageIO.setUseCache(false); // fixes "Can't create cache file!" exception under RI 1.2_09
        try {
            ImageIO.write(data, "png", stream);
        } catch (IOException e) {
            throw new RuntimeException("Error during encoding to PNG", e);
        }
        byte[] byteArray = stream.toByteArray();
        return byteArray;
    }

    /**
     * Format color string and add it to json parameters
     *
     * @param paramsObject The json parameters
     * @param paramName    The name of parameter
     * @param paramValue   The value of parameter
     * @see #addJsonParam(org.openfaces.org.json.JSONObject, String, Object, Object)
     */
    public static void addJsonParam(JSONObject paramsObject, String paramName, Color paramValue) {
        String colorStr = CSSUtil.formatColor(paramValue);
        addJsonParam(paramsObject, paramName, colorStr, null);
    }

    /**
     * Add object to json parameters
     *
     * @param paramsObject The json parameters
     * @param paramName    The name of parameter
     * @param paramValue   The value of parameter
     * @see #addJsonParam(org.openfaces.org.json.JSONObject, String, Object, Object)
     */
    public static void addJsonParam(JSONObject paramsObject, String paramName, Object paramValue) {
        addJsonParam(paramsObject, paramName, paramValue, null);
    }

    /**
     * Add object to json parameters
     *
     * @param paramsObject The json parameters
     * @param paramName    The name of parameter
     * @param paramValue   The value of parameter
     * @param defaultValue The default value to compare
     */
    public static void addJsonParam(JSONObject paramsObject, String paramName, Object paramValue, Object defaultValue) {
        if (paramValue instanceof Object[]) {
            Object[] params = ((Object[]) paramValue);
            JSONArray array = new JSONArray();
            for (Object param : params) {
                array.put(param);
            }
            paramValue = array;
        }
        if (paramValue instanceof List) {
            List params = ((List) paramValue);
            JSONArray array = new JSONArray();
            for (Object param : params) {
                array.put(param);
            }
            paramValue = array;
        }
        if (paramValue != null && !paramValue.equals(defaultValue))
            try {
                if (paramValue instanceof JSONObject || paramValue instanceof JSONArray || paramValue instanceof JSONString)
                    paramsObject.put(paramName, paramValue);
                else
                    paramsObject.put(paramName, paramValue.toString());
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
    }

    /**
     * Add double parameter to json parameters
     *
     * @param paramsObject The json parameters
     * @param paramName    The name of parameter
     * @param paramValue   The value of parameter
     * @param defaultValue The default value to compare
     */
    public static void addJsonParam(JSONObject paramsObject, String paramName, double paramValue, double defaultValue) {
        if (Math.abs(paramValue - defaultValue) > 0.0000001)
            try {
                paramsObject.put(paramName, paramValue);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
    }

    /**
     * Add double parameter to json parameters
     *
     * @param paramsObject The json parameters
     * @param paramName    The name of parameter
     * @param paramValue   The value of parameter
     */
    public static void addJsonParam(JSONObject paramsObject, String paramName, double paramValue) {
        try {
            paramsObject.put(paramName, paramValue);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * Add boolean parameter to json parameters
     *
     * @param paramsObject The json parameters
     * @param paramName    The name of parameter
     * @param paramValue   The value of parameter
     * @param impliedValue A value which doesn't have to be added, which means that this value will be implied on the receiving side
     */
    public static void addJsonParam(JSONObject paramsObject, String paramName, boolean paramValue, boolean impliedValue) {
        if (paramValue != impliedValue)
            try {
                paramsObject.put(paramName, paramValue);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
    }

    /**
     * Add integer parameter to json parameters
     *
     * @param paramsObject The json parameters
     * @param paramName    The name of parameter
     * @param paramValue   The value of parameter
     * @param defaultValue The default value to compare
     */
    public static void addJsonParam(JSONObject paramsObject, String paramName, int paramValue, int defaultValue) {
        if (paramValue != defaultValue)
            try {
                paramsObject.put(paramName, paramValue);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
    }

    /**
     * Add integer parameter to json parameters
     *
     * @param paramsObject The json parameters
     * @param paramName    The name of parameter
     * @param paramValue   The value of parameter
     */
    public static void addJsonParam(JSONObject paramsObject, String paramName, int paramValue) {
        try {
            paramsObject.put(paramName, paramValue);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Check if the component has ajax4jsf support
     *
     * @param component The component to check
     * @return true, if component has ajax4jsf support
     */
    public static boolean isComponentWithA4jAjax(UIComponent component) {
        return getA4jAjaxForComponent(component) != null;
    }

    /**
     * Find ajax support component in children and facets of component
     *
     * @param component The component to check
     * @return {@link UIComponent} of ajax support, if component has support, null, otherwise
     */
    public static ClientBehavior getA4jAjaxForComponent(UIComponent component) {
        if (!(component instanceof ClientBehaviorHolder) || A4J_AJAX_BEHAVIOR_CLASS == null)
            return null;
        Collection<List<ClientBehavior>> behaviors = ((ClientBehaviorHolder) component).getClientBehaviors().values();
        for (List<ClientBehavior> behavior : behaviors) {
            try {
                if (A4J_AJAX_BEHAVIOR_CLASS.isAssignableFrom(behavior.getClass()))
                    return (ClientBehavior) behavior;
            } catch (Throwable e) {
                return null; // a component can't have A4j support if AjaxBehavior class can't be found
            }
        }
        return null;
    }

    /**
     * Render javascript initialization of component and css style and style classes
     *
     * @param context   {@link FacesContext} for the current request
     * @param component The component, which init script is rendered
     * @throws IOException if an input/output error occurs
     * @see #encodeInitComponentCall(javax.faces.context.FacesContext, org.openfaces.component.OUIComponent, boolean)
     */
    public static void encodeInitComponentCall(FacesContext context, OUIComponent component) throws IOException {
        encodeInitComponentCall(context, component, false);
    }

    /**
     * Render javascript initialization of component and css style and style classes
     *
     * @param context         {@link FacesContext} for the current request
     * @param component       The component, which init script is rendered
     * @param skipIfNotNeeded
     * @throws IOException if an input/output error occurs
     */
    public static void encodeInitComponentCall(
            FacesContext context,
            OUIComponent component,
            boolean skipIfNotNeeded) throws IOException {
        String rolloverClass = Styles.getCSSClass(context,
                (UIComponent) component,
                component.getRolloverStyle(), StyleGroup.rolloverStyleGroup(), component.getRolloverClass());
        if (rolloverClass == null && skipIfNotNeeded)
            return;
        UIComponent uiComponent = (UIComponent) component;
        JSONObject styleParams = new JSONObject();
        if (rolloverClass != null)
            try {
                styleParams.put("rollover", rolloverClass);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        ScriptBuilder buf = new ScriptBuilder().initScript(context, uiComponent,
                "O$.initComponent", styleParams).semicolon();
        renderInitScript(context, buf, Resources.utilJsURL(context));
        Styles.renderStyleClasses(context, uiComponent);
    }

    /**
     * Return rollover css class for component
     *
     * @param context   {@link FacesContext} for the current request
     * @param component The component
     * @return rollover css class for component
     */
    public static String getRolloverClass(FacesContext context, OUIComponent component) {
        return Styles.getCSSClass(context, (UIComponent) component,
                component.getRolloverStyle(), StyleGroup.rolloverStyleGroup(), component.getRolloverClass()
        );
    }

    /**
     * Return focused css class for component
     *
     * @param context   {@link FacesContext} for the current request
     * @param component The component
     * @return focused css class for component
     */
    public static String getFocusedClass(FacesContext context, OUIInput component) {
        String defaultClass = Styles.getCSSClass(context, (UIComponent) component, DEFAULT_FOCUSED_STYLE, StyleGroup.selectedStyleGroup(0));
        String cssClass = Styles.getCSSClass(context,
                (UIComponent) component,
                component.getFocusedStyle(), StyleGroup.selectedStyleGroup(1), component.getFocusedClass(), defaultClass
        );

        return cssClass;
    }

    /**
     * Render all children of the component, which are instances of {@link OUIClientAction}
     *
     * @param context   {@link FacesContext} for the current request
     * @param component The component
     * @throws IOException if an input/output error occurs
     */
    public static void encodeClientActions(FacesContext context, UIComponent component) throws IOException {
        List<UIComponent> children = component.getChildren();
        for (UIComponent child : children) {
            if (child instanceof OUIClientAction) {
                child.encodeAll(context);
            }
        }
    }

    /**
     * Return event handlers for a given events
     *
     * @param component  The component, which event handlers retrieve
     * @param eventNames The event names
     * @return {@link org.openfaces.org.json.JSONObject JSONObject} with events and their handlers
     */
    public static JSONObject getEventsParam(UIComponent component, String... eventNames) {
        JSONObject events = new JSONObject();
        for (String eventName : eventNames) {
            String eventHandlerScript =
                    "change".equals(eventName)
                            ? Rendering.getEventHandlerScript(component, eventName, "valueChange")
                            : Rendering.getEventHandlerScript(component, eventName);
            if (Rendering.isNullOrEmpty(eventHandlerScript))
                continue;
            try {
                events.put("on" + eventName, eventHandlerScript);
            } catch (JSONException e) {
                throw new RuntimeException(e);
            }
        }
        return events;
    }


    /**
     * Write newline symbol to writer
     *
     * @param writer The character-based output
     * @throws java.io.IOException if an input/output error occurs
     */
    public static void writeNewLine(ResponseWriter writer) throws IOException {
        writer.writeText("\n", null);
    }

    /**
     * Check value for nullable and write it to writer
     *
     * @param value  The value of attribute
     * @param name   The name of attribute
     * @param writer The character-based output
     * @throws java.io.IOException if an input/output error occurs
     */
    public static void writeAttribute(ResponseWriter writer, String name, String value) throws IOException {
        if (value != null)
            writer.writeAttribute(name, value, null);
    }

    /**
     * Renders the specified list of component's attributes as is for all non-null attributes.
     */
    public static void writeAttributes(ResponseWriter writer, UIComponent component, String... attributes) throws IOException {
        for (String name : attributes) {
            Object value = component.getAttributes().get(name);
            if (value != null)
                writer.writeAttribute(name, value, name);
        }
    }

    public static String writeIdAttribute(FacesContext context, UIComponent component) throws IOException {
        String clientId = component.getClientId(context);
        context.getResponseWriter().writeAttribute("id", clientId, null);
        return clientId;
    }

    public static void writeNameAttribute(FacesContext context, UIComponent component) throws IOException {
        String clientId = component.getClientId(context);
        context.getResponseWriter().writeAttribute("name", clientId, null);
    }

    public static void writeAttribute(ResponseWriter writer, String name, int value, int emptyValue) throws IOException {
        if (value != emptyValue)
            writer.writeAttribute(name, String.valueOf(value), null);
    }


    public static void writeStandardEvents(ResponseWriter writer, OUIInput component) throws IOException {
        if (component.isDisabled())
            return;

        writeStandardEvents(writer, (OUIComponent) component);
    }

    public static void writeStandardEvents(ResponseWriter writer, OUIComponent component) throws IOException {
        writeStandardEvents(writer, component, false);
    }

    public static void writeStandardEvents(ResponseWriter writer, OUIComponent component, boolean skipOnclick) throws IOException {
        UIComponent c = (UIComponent) component;
        if (!skipOnclick)
            writeAttribute(writer, "onclick", getEventHandlerScript(c, "click", "action"));
        writeAttribute(writer, "ondblclick", getEventHandlerScript(c, "dblclick"));
        writeAttribute(writer, "onmousedown", getEventHandlerScript(c, "mousedown"));
        writeAttribute(writer, "onmouseup", getEventHandlerScript(c, "mouseup"));
        writeAttribute(writer, "onmousemove", getEventHandlerScript(c, "mousemove"));
        writeAttribute(writer, "onmouseout", getEventHandlerScript(c, "mouseout"));
        writeAttribute(writer, "onmouseover", getEventHandlerScript(c, "mouseover"));
        writeAttribute(writer, "oncontextmenu", getEventHandlerScript(c, "contextmenu"));

        writeAttribute(writer, "onfocus", getEventHandlerScript(c, "focus"));
        writeAttribute(writer, "onblur", getEventHandlerScript(c, "blur"));
        writeAttribute(writer, "onkeypress", getEventHandlerScript(c, "keypress"));
        writeAttribute(writer, "onkeydown", getEventHandlerScript(c, "keydown"));
        writeAttribute(writer, "onkeyup", getEventHandlerScript(c, "keyup"));
    }

    public static String getEventHandlerScript(UIComponent component, String event) {
        return getEventHandlerScript(component, event, null);
    }

    public static String getChangeHandlerScript(UIComponent component) {
        return getEventHandlerScript(component, "change", "valueChange");
    }

    public static String getEventHandlerScript(UIComponent component, String event, String logicalEvent) {
        return getEventHandlerScript(component, component, event, logicalEvent);
    }

    public static String getEventHandlerScript(UIComponent component, UIComponent sourceComponent, String event, String logicalEvent) {
        String script = (String) component.getAttributes().get("on" + event);
        List<ClientBehavior> behaviors = null;
        List<ClientBehavior> behaviors2 = null;
        if (component instanceof ClientBehaviorHolder) {
            Map<String, List<ClientBehavior>> clientBehaviors = ((ClientBehaviorHolder) component).getClientBehaviors();
            behaviors = clientBehaviors.get(event);
            if (logicalEvent != null)
                behaviors2 = clientBehaviors.get(logicalEvent);
        }


        if (script == null &&
                (behaviors == null || behaviors.size() == 0) &&
                (behaviors2 == null || behaviors2.size() == 0)) return null;
        StringBuilder b = new StringBuilder();
        if (script != null) {
            b.append(script);
            if ((behaviors != null && behaviors.size() > 0) || (behaviors2 != null && behaviors2.size() > 0))
                if (!script.trim().endsWith(";")) b.append("; ");
        }
        FacesContext context = FacesContext.getCurrentInstance();
        if (behaviors != null)
            appendBehaviorScripts(context, b, behaviors, component, sourceComponent, event);
        if (behaviors2 != null)
            appendBehaviorScripts(context, b, behaviors2, component, sourceComponent, logicalEvent);
        return b.toString();
    }

    private static void appendBehaviorScripts(
            FacesContext context,
            StringBuilder stringBuilder,
            List<ClientBehavior> behaviors,
            UIComponent component,
            UIComponent sourceComponent,
            String event) {
        for (ClientBehavior behavior : behaviors) {
            String sourceId = sourceComponent.getClientId(context);
            ClientBehaviorContext behaviorContext = ClientBehaviorContext.createClientBehaviorContext(
                    context, component, event, sourceId,
                    getBehaviorParameters(sourceComponent));
            String behaviorScript = behavior.getScript(behaviorContext);
            stringBuilder.append(behaviorScript);
            if (!behaviorScript.trim().endsWith(";")) stringBuilder.append(";");
        }
    }

    public static Collection<ClientBehaviorContext.Parameter> getBehaviorParameters(UIComponent sourceComponent) {
        List<ClientBehaviorContext.Parameter> result = new ArrayList<ClientBehaviorContext.Parameter>();
        List<UIParameter> uiParameters = Components.findChildrenWithClass(sourceComponent, UIParameter.class);
        for (UIParameter uiParameter : uiParameters) {
            String name = uiParameter.getName();
            if (name == null || name.equals("")) continue;
            Object value = uiParameter.getValue();
            result.add(new ClientBehaviorContext.Parameter(name, value));
        }
        return result;
    }

    public static void decodeBehaviors(FacesContext context, UIComponent component) {
        if (!(component instanceof ClientBehaviorHolder)) return;

        Map<String, List<ClientBehavior>> behaviorsMap = ((ClientBehaviorHolder) component).getClientBehaviors();
        if (behaviorsMap.size() == 0) return;

        String clientId = component.getClientId();
        Map<String, String> requestParams = context.getExternalContext().getRequestParameterMap();
        String event = requestParams.get("javax.faces.behavior.event");
        String source = requestParams.get("javax.faces.source");
        if (event == null || source == null || !source.equals(clientId)) return;

        List<ClientBehavior> behaviors = behaviorsMap.get(event);
        for (ClientBehavior behavior : behaviors) {
            behavior.decode(context, component);
        }
    }

    public static String getEventWithOnPrefix(FacesContext context, OUIClientAction component, String componentName) {
        String event = component.getEvent();
        if (event != null) {
            if (event.startsWith("on"))
                throw new FacesException((componentName != null ? componentName + "'s " : "") +
                        "\"event\" attribute value shouldn't start with \"on\" prefix, for example you should use \"click\" instead of \"onclick\", " +
                        "but the following value was found: \"" + event + "\"; Component's client-id: " + ((UIComponent) component).getClientId(context));
            event = "on" + event;
        }
        return event;
    }

    public static boolean getBooleanContextParam(FacesContext context, String webXmlContextParam) {
        return getBooleanContextParam(context, webXmlContextParam, false);
    }

    public static boolean getBooleanContextParam(FacesContext context, String webXmlContextParam, boolean defaultValue) {
        String applicationMapKey = "org.openfaces.util.Rendering._contextParam:" + webXmlContextParam;
        Map<String, Object> applicationMap = context.getExternalContext().getApplicationMap();

        Boolean result = (Boolean) applicationMap.get(applicationMapKey);
        if (result == null) {
            ExternalContext externalContext = context.getExternalContext();
            String paramStr = externalContext.getInitParameter(webXmlContextParam);
            if (paramStr == null)
                result = defaultValue;
            else {
                paramStr = paramStr.trim();
                if (paramStr.equalsIgnoreCase("true"))
                    result = Boolean.TRUE;
                else if (paramStr.equalsIgnoreCase("false"))
                    result = Boolean.FALSE;
                else {
                    externalContext.log("Unrecognized value specified for context parameter named " + webXmlContextParam + ": it must be either true or false");
                    result = Boolean.FALSE;
                }
            }
            applicationMap.put(applicationMapKey, result);
        }
        return result;
    }

    public static String getContextParam(FacesContext context, String webXmlContextParam) {
        return getContextParam(context, webXmlContextParam, null);
    }

    public static String getContextParam(FacesContext context, String webXmlContextParam, String defaultValue) {
        String applicationMapKey = "org.openfaces.util.Rendering._contextParam:" + webXmlContextParam;
        Map<String, Object> applicationMap = context.getExternalContext().getApplicationMap();

        String result = (String) applicationMap.get(applicationMapKey);
        if (result == null) {
            ExternalContext externalContext = context.getExternalContext();
            String paramStr = externalContext.getInitParameter(webXmlContextParam);
            if (paramStr == null)
                result = defaultValue;
            else
                result = paramStr;
            applicationMap.put(applicationMapKey, result);
        }
        return result;
    }

    public static boolean isExplicitIdSpecified(UIComponent component) {
        String id = component.getId();
        if (id == null) return false;

        if (id.startsWith(UIViewRoot.UNIQUE_ID_PREFIX))
            return false;

        return true;
    }

    public static boolean writeEventsWithAjaxSupport(FacesContext context, ResponseWriter writer, OUICommand command) throws IOException {
        return writeEventsWithAjaxSupport(context, writer, command, null);
    }

    public static boolean writeEventsWithAjaxSupport(
            FacesContext context,
            ResponseWriter writer,
            OUICommand command,
            String submitIfNoAjax
    ) throws IOException {
        String userClickHandler = Rendering.getEventHandlerScript(command, "click", "action");
        Script componentClickHandler = null;
        Iterable<String> render = command.getRender();
        Iterable<String> execute = command.getExecute();
        boolean ajaxJsRequired = false;
        if (render != null || (execute != null && execute.iterator().hasNext())) {
            if (render == null)
                throw new FacesException("'execute' attribute can't be specified without the 'render' attribute. Component id: " + command.getId());

            AjaxInitializer initializer = new AjaxInitializer();
            componentClickHandler = new ScriptBuilder().functionCall("O$.Ajax._reload",
                    initializer.getRenderArray(context, command, render),
                    initializer.getAjaxParams(context, command),
                    new RawScript("this"),
                    new RawScript("event")).semicolon().append("return false;");
            ajaxJsRequired = true;
        } else {
            if (submitIfNoAjax != null) {
                componentClickHandler = new FunctionCallScript("O$.submitWithParam", command, submitIfNoAjax, true);
            }
        }
        String clickHandler = Rendering.joinScripts(
                userClickHandler,
                componentClickHandler != null ? componentClickHandler.toString() : null);
        if (!Rendering.isNullOrEmpty(clickHandler))
            writer.writeAttribute("onclick", clickHandler, null);
        Rendering.writeStandardEvents(writer, command, true);
        return ajaxJsRequired;
    }
}