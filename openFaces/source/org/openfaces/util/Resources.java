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

import org.openfaces.application.OpenFacesApplication;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.org.json.JSONTokener;

import javax.faces.FacesException;
import javax.faces.application.Application;
import javax.faces.application.Resource;
import javax.faces.application.ResourceHandler;
import javax.faces.application.ViewHandler;
import javax.faces.component.UIOutput;
import javax.faces.component.UIViewRoot;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.context.ResponseWriter;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;

/**
 * @author Dmitry Pikhulya
 */
public class Resources {
    public static final String HEADER_JS_LIBRARIES = "OF:js_file_included";
    private static final String RENDERED_JS_LINKS = "org.openfaces.util.Rendering.renderedJsLinks";
    public static final String POSTPONE_JS_LINK_RENDERING = "org.openfaces.util.Resources.postponeJsLinkRendering";

    private static final String OPENFACES_VERSION_TXT = "/META-INF/openFacesVersion.txt";
    private static final String VERSION_PLACEHOLDER_STR = "version";

    private static final String CLDR = "cldr";
    private static final String NUMBER_LOCALE_SETTINGS = "number.js";
    private static final String PARAM_ORG_OPENFACES_JQUERY = "org.openfaces.jquery";
    public static final String LIBRARY_NAME = "openfaces";
    public static final String UTIL_JS_PATH = "util/util.js";
    public static final String JSON_JS_PATH = "util/json2.js";
    public static final String AJAX_UTIL_JS_PATH = "util/ajaxUtil.js";


    private Resources() {
    }

    /**
     * This method returns the URL string ready for rendering into HTML based on the URL specified by the user. If
     * URL is not specified by the user explicitly then URL to a default internal resource is returned instead.
     *
     * @param userSpecifiedUrl        optional resource url as specified by the user. This can be a relative URL, or an absolute URL
     * @param defaultResourceFileName file name for a resource which should be provided if userSpecifiedUrl is null or empty string
     * @return
     */
    public static String getURL(FacesContext context, String userSpecifiedUrl, String defaultResourceFileName) {
        return getURL(context, userSpecifiedUrl, defaultResourceFileName, true);
    }

    /**
     * @param userSpecifiedUrl        optional resource url as specified by the user. This can be a relative URL, or an absolute URL
     * @param defaultResourceFileName file name for a resource which should be provided if userSpecifiedUrl is null (or empty string).
     *                                Empty string is also considered as signal for returning the default resource here because null
     *                                is auto-converted to an empty string when passed through a string binding
     * @param prependContextPath      use true here if you render the attribute yourself, and false if you use pass this URL to HtmlGraphicImage or similar component
     */
    public static String getURL(
            FacesContext context,
            String userSpecifiedUrl,
            String defaultResourceFileName,
            boolean prependContextPath) {
        boolean returnDefaultResource = userSpecifiedUrl == null || userSpecifiedUrl.length() == 0;
        String result = returnDefaultResource
                ? internalURL(context, defaultResourceFileName)
                : (prependContextPath ? applicationURL(context, userSpecifiedUrl) : userSpecifiedUrl);
        return result;
    }

    /**
     * Get path to application resource according to context and resource path
     *
     * @param context      faces context provided by application
     * @param resourcePath path to resource - either absolute (starting with a slash) in the scope of application context,
     *                     or relative to the current page
     * @return full URL to resource ready for rendering as <code>src</code> or <code>href</code> attribute's value.
     */
    public static String applicationURL(FacesContext context, String resourcePath) {
        if (resourcePath == null || resourcePath.length() == 0)
            return "";
        if (resourcePath.contains(ResourceHandler.RESOURCE_IDENTIFIER))
            return resourcePath;
        ViewHandler viewHandler = context.getApplication().getViewHandler();
        String resourceUrl = viewHandler.getResourceURL(context, resourcePath);
        String encodedResourceUrl = context.getExternalContext().encodeResourceURL(resourceUrl);
        return encodedResourceUrl;
    }

    private static String getPackagePath(Class componentClass) {
        String packageName = componentClass == null ? "" : getPackageName(componentClass);
        String packagePath = packageName.replace('.', '/');
        if (packagePath.length() > 0) {
            packagePath += "/";
        }
        return packagePath;
    }

    /**
     * @param context      Current FacesContext
     * @param resourcePath Path to the resource file
     * @return The requested URL
     */
    public static String internalURL(FacesContext context, String resourcePath) {
        if (context == null) throw new NullPointerException("context");
        if (resourcePath == null) throw new NullPointerException("resourcePath");

        ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
        Resource resource = resourceHandler.createResource(resourcePath, LIBRARY_NAME);
        if (resource == null)
            throw new FacesException("Couldn't find resource: " + resourcePath);
        resourcePath = resource.getRequestPath();
        return resourcePath;
    }

    private static String versionString;

    /**
     * Return version of OpenFaces
     *
     * @return requested version of OpenFaces
     */
    public static String getVersionString() {
        if (versionString != null)
            return versionString;

        String buildInfo = readBuildInfo();

        String version = "";
        if (buildInfo != null) {
            int idx1 = buildInfo.indexOf(",");
            version = buildInfo.substring(0, idx1).trim();
            if (VERSION_PLACEHOLDER_STR.equals(version)) {
                long startTime = System.currentTimeMillis() / 1000;
                version = Long.toString(startTime, 36);
            }
        }

        versionString = version;
        return version;
    }

    private static String readBuildInfo() {
        InputStream versionStream = Resources.class.getResourceAsStream(OPENFACES_VERSION_TXT);
        if (versionStream == null)
            throw new IllegalStateException("Couldn't find resource file: " + OPENFACES_VERSION_TXT);

        String buildInfo = null;
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(versionStream));
        try {
            buildInfo = bufferedReader.readLine();
        } catch (IOException e) {
            Log.log("Couldn't read version string", e);
        } finally {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                //
            }
        }
        return buildInfo;
    }


    public static JSONObject getNumberFormatSettings(Locale locale) throws IOException, JSONException {
        String cldrPath = "/" + getPackagePath(Resources.class) + CLDR;

        InputStream numberStream = null;
        if (locale != null) {
            numberStream = Resources.class.getResourceAsStream(cldrPath + "/" + locale.toString() + "/" + NUMBER_LOCALE_SETTINGS);
        }
        if (numberStream == null) {
            numberStream = Resources.class.getResourceAsStream(cldrPath + "/" + NUMBER_LOCALE_SETTINGS);
        }
        BufferedReader bufferedReader = null;
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(numberStream, "UTF-8"));
            String text;
            do {
                text = bufferedReader.readLine();
            } while (text.startsWith("/*") || text.startsWith(" *") || text.trim().length() == 0);
            text = text.substring(1, text.length() - 1);
            JSONObject result = new JSONObject(new JSONTokener(text));
            return result;
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }

    }


    /**
     * Return URL of util.js file
     *
     * @param context {@link FacesContext} for the current request
     * @return requested URL of util.js file
     */
    public static String utilJsURL(FacesContext context) {
        // To be sure that default.css is included to the web page,
        // because it is also required in cases when util.js included into web page
        return internalURL(context, UTIL_JS_PATH);
    }

    public static String filtersJsURL(FacesContext context) {
        return Resources.internalURL(context, "filter/filters.js");
    }

    /**
     * Return URL of ajaxUtil.js file. Keep in mind that ajaxUtil.js depends on util.js.
     * Don't forget to include util.js as well before including this URL.
     *
     * @param context {@link FacesContext} for the current request
     * @return requested URL of ajaxUtil.js file
     */
    public static String ajaxUtilJsURL(FacesContext context) {
        return Resources.internalURL(context, AJAX_UTIL_JS_PATH);
    }

    /**
     * Return URL of json javascript file
     *
     * @param context {@link FacesContext} for the current request
     * @return requested URL of json javascript file
     */
    public static String jsonJsURL(FacesContext context) {
        return Resources.internalURL(context, JSON_JS_PATH);
    }

    /**
     * Return full package name for Class
     *
     * @param aClass The Class object
     * @return full package name for given Class
     */
    private static String getPackageName(Class aClass) {
        String className = aClass.getName();
        int lastIndexOfDot = className.lastIndexOf('.');
        if (lastIndexOfDot == -1) {
            return "";
        } else {
            return className.substring(0, lastIndexOfDot);
        }
    }

    /**
     * Register javascript library to future adding to response
     *
     * @param context   {@link FacesContext} for the current request
     * @param jsFileUrl Url for the javascript file
     */
    public static void registerJavascriptLibrary(FacesContext context, String jsFileUrl) {
        Map<Object, Object> contextAttributes = context.getAttributes();
        List<String> libraries = getRegisteredJsLibraries(contextAttributes);
        if (libraries == null) {
            libraries = new ArrayList<String>();
            contextAttributes.put(HEADER_JS_LIBRARIES, libraries);
        }

        if (libraries.contains(jsFileUrl)) return;
        libraries.add(jsFileUrl);

    }

    public static List<String> getRegisteredJsLibraries() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<Object, Object> requestMap = context.getAttributes();
        return getRegisteredJsLibraries(requestMap);
    }

    public static List<String> getRegisteredJsLibraries(Map<Object, Object> contextMap) {
        return (List<String>) contextMap.get(HEADER_JS_LIBRARIES);
    }


    /**
     * Render javascript file link, if not rendered early
     *
     * @param context {@link javax.faces.context.FacesContext} for the current request
     * @param jsFile  Javascript file to include
     * @throws IOException if an input/output error occurs
     */
    public static void renderJSLinkIfNeeded(FacesContext context, String jsFile) throws IOException {
        ResponseWriter writer = context.getResponseWriter();
        List<String> renderedJsLinks = getRenderedJsLinks(context);
        if (renderedJsLinks.contains(jsFile)) {
            return;
        }
        renderedJsLinks.add(jsFile);

        Boolean postponeJsLinkRendering = (Boolean) context.getAttributes().get(POSTPONE_JS_LINK_RENDERING);
        if (postponeJsLinkRendering != null && postponeJsLinkRendering)
            return;
        boolean fullResourceString = jsFile.startsWith("/") || jsFile.contains("://");
        if (AjaxUtil.isAjaxRequest(context)) {
            if (!fullResourceString)
                jsFile = Resources.internalURL(context, jsFile);
            registerJavascriptLibrary(context, jsFile);
        } else if (AjaxUtil.isAjax4jsfRequest()) {
            registerJavascriptLibrary(context, jsFile);
        } else {

            if (OpenFacesApplication.isConstructingView(context)) {
                if (fullResourceString)
                    throw new IllegalArgumentException("Resource name should be passed, not resource URL: " + jsFile);
                Resources.addHeaderResource(context, jsFile, Resources.LIBRARY_NAME);
            } else {
                if (!fullResourceString)
                    jsFile = Resources.internalURL(context, jsFile);
                writer.startElement("script", null);
                writer.writeAttribute("src", jsFile, null);
                writer.writeAttribute("type", "text/javascript", null);
                // write white-space to avoid creating self-closing <script/> tags
                // under certain servers, which are not correctly interpreted by browsers (JSFC-2303)
                if (Environment.isExoPortal())
                    writer.writeText(" ", null);
                writer.endElement("script");
            }
        }
    }

    /**
     * Return list of already rendered javascript links
     *
     * @param context {@link FacesContext} for the current request
     * @return list of already rendered javascript links
     */
    public static List<String> getRenderedJsLinks(FacesContext context) {
        Map<Object, Object> contextAttributes = context.getAttributes();
        Object key = renderedJsLinksKey(context);
        List<String> renderedJsLinks = (List<String>) contextAttributes.get(key);
        if (renderedJsLinks == null) {
            renderedJsLinks = new ArrayList<String>();
            contextAttributes.put(key, renderedJsLinks);
        }
        return renderedJsLinks;
    }

    public static void includeJQuery() {
        try {
            FacesContext context = FacesContext.getCurrentInstance();
            includeJQuery(context);
        } catch (IOException e) {
            throw new FacesException(e);
        }
    }

    public static Object viewBasedKey(FacesContext context, String key) {
        List tuple = new ArrayList(2);
        tuple.add(context.getViewRoot());
        tuple.add(key);
        return tuple;
    }

    // a different view can be created on the rendering phase, and links should be recalculated in that case
    // e.g. reproducible with Spring WebFlow on the app here: https://groups.google.com/a/teamdev.com/group/openfaces-forum/browse_thread/thread/a72ad22c8c47a80b/5ea4af5d4bc068ec#5ea4af5d4bc068ec
    public static Object renderedJsLinksKey(FacesContext context) {
        return viewBasedKey(context, RENDERED_JS_LINKS);
    }

    public static void includeJQuery(FacesContext context) throws IOException {
        String jQueryIncludedKey = "org.openfaces.util.Rendering.JQUERY_INCLUDED";
        Map<Object, Object> contextAttributes = context.getAttributes();
        if (contextAttributes.containsKey(jQueryIncludedKey)) return;
        contextAttributes.put(jQueryIncludedKey, true);
        String jQueryMode = Rendering.getContextParam(context, PARAM_ORG_OPENFACES_JQUERY, "embedded");
        if (jQueryMode.equals("none")) {
            // the org.openfaces.jQuery=none parameter might be required to avoid conflicts when jQuery.js is
            // already added to a page by application developer or another library
            return;
        }
        if (jQueryMode.equals("embedded"))
            addHeaderResource(context, "util/jquery-1.4.2.min.js", LIBRARY_NAME);
            /* below are the official jQuery CDNs as referenced here: http://docs.jquery.com/Downloading_jQuery */
        else if (jQueryMode.equals("jquery"))
            addHeaderResource(context, "http://code.jquery.com/jquery-1.4.2.min.js", null);
        else if (jQueryMode.equals("google"))
            addHeaderResource(context, "http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js", null);
        else if (jQueryMode.equals("microsoft"))
            addHeaderResource(context, "http://ajax.microsoft.com/ajax/jquery/jquery-1.4.2.min.js", null);
        else {
            if (!jQueryMode.startsWith("http"))
                throw new FacesException("Invalid value for the " + PARAM_ORG_OPENFACES_JQUERY + " context parameter: \"" + jQueryMode + "\" ; It should either be one of the predefined values: none, embedded, jquery, google, microsoft, or be a URL string starting with \"http\". ");
            addHeaderResource(context, jQueryMode, null);
        }

        String noConflictStr = Rendering.getContextParam(context, "org.openfaces.jQuery.noConflict");
        if (noConflictStr != null && !noConflictStr.equalsIgnoreCase("false")) {
            if (noConflictStr.equalsIgnoreCase("true"))
                Rendering.renderInitScript(context, new RawScript("jQuery.noConflict()"));
            else
                Rendering.renderInitScript(context, new RawScript("var " + noConflictStr + " = jQuery.noConflict();"));
        }
    }

    public static void addHeaderResource(FacesContext context, String resourceName, String library) {
        Application application = context.getApplication();
        String target = null;
        UIOutput output = (UIOutput) application.createComponent("javax.faces.Output");
        ResourceHandler resourceHandler = application.getResourceHandler();
        String rendererType = resourceHandler.getRendererTypeForResourceName(resourceName);
        output.setRendererType(rendererType);
        Map<String, Object> attributes = output.getAttributes();
        attributes.put("name", resourceName);
        if (library != null)
            attributes.put("library", library);
        if (target != null)
            attributes.put("target", target);
        UIViewRoot viewRoot = context.getViewRoot();
        viewRoot.addComponentResource(context, output);
    }

    public static void addHeaderInitScript(FacesContext context, Script script) {
        Application application = context.getApplication();
        String target = null;
        UIOutput output = (UIOutput) application.createComponent("javax.faces.Output");
        output.setRendererType("javax.faces.resource.Script");
        Map<String, Object> attributes = output.getAttributes();
        if (target != null)
            attributes.put("target", target);
        HtmlOutputText scriptTextComponent = (HtmlOutputText) application.createComponent(HtmlOutputText.COMPONENT_TYPE);
        scriptTextComponent.setEscape(false);
        scriptTextComponent.setValue(script.toString());
        output.getChildren().add(scriptTextComponent);
        UIViewRoot viewRoot = context.getViewRoot();
        viewRoot.addComponentResource(context, output);
    }

}
