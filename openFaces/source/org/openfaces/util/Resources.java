/*
 * OpenFaces - JSF Component Library 3.0
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

import org.openfaces.application.OpenFacesApplication;
import org.openfaces.org.json.JSONException;
import org.openfaces.org.json.JSONObject;
import org.openfaces.org.json.JSONTokener;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * @author Dmitry Pikhulya
 */
public class Resources {
    public static final String HEADER_JS_LIBRARIES = "OF:js_file_included";
    public static final String RENDERED_JS_LINKS = "org.openfaces.util.Rendering.renderedJsLinks";
    public static final String POSTPONE_JS_LINK_RENDERING = "org.openfaces.util.Resources.postponeJsLinkRendering";

    private static final String OPENFACES_VERSION_TXT = "/META-INF/openFacesVersion.txt";
    private static final String VERSION_PLACEHOLDER_STR = "version";

    private static final String CLDR = "cldr";
    private static final String NUMBER_LOCALE_SETTINGS = "number.js";
    private static final String PARAM_ORG_OPENFACES_JQUERY = "org.openfaces.jquery";
    public static final String META_INF_RESOURCES_ROOT = "/META-INF/resources/openfaces/";
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
     * @param userSpecifiedUrl             optional resource url as specified by the user. This can be a relative URL, or an absolute URL
     * @param defaultResourceFileName      file name for a resource which should be provided if userSpecifiedUrl is null or empty string
     * @param defaultResourceBaseClassName the class relatively to which defaultResourceFileName is specified
     * @return
     */
    public static String getURL(FacesContext context, String userSpecifiedUrl,
                                Class defaultResourceBaseClassName, String defaultResourceFileName) {
        return getURL(context, userSpecifiedUrl, defaultResourceBaseClassName, defaultResourceFileName, true);
    }

    /**
     * @param userSpecifiedUrl             optional resource url as specified by the user. This can be a relative URL, or an absolute URL
     * @param defaultResourceFileName      file name for a resource which should be provided if userSpecifiedUrl is null (or empty string).
     *                                     Empty string is also considered as signal for returning the default resource here because null
     *                                     is auto-converted to an empty string when passed through a string binding
     * @param defaultResourceBaseClassName the class relatively to which defaultResourceFileName is specified
     * @param prependContextPath           use true here if you render the attribute yourself, and false if you use pass this URL to HtmlGraphicImage or similar component
     */
    public static String getURL(
            FacesContext context,
            String userSpecifiedUrl,
            Class defaultResourceBaseClassName,
            String defaultResourceFileName,
            boolean prependContextPath) {
        boolean returnDefaultResource = userSpecifiedUrl == null || userSpecifiedUrl.length() == 0;
        String result = returnDefaultResource
                ? getInternalURL(context, defaultResourceBaseClassName, defaultResourceFileName, prependContextPath)
                : (prependContextPath ? getApplicationURL(context, userSpecifiedUrl) : userSpecifiedUrl);
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
    public static String getApplicationURL(FacesContext context, String resourcePath) {
        if (resourcePath == null || resourcePath.length() == 0)
            return "";
        if (resourcePath.contains(ResourceHandler.RESOURCE_IDENTIFIER))
            return resourcePath;
        ViewHandler viewHandler = context.getApplication().getViewHandler();
        String resourceUrl = viewHandler.getResourceURL(context, resourcePath);
        String encodedResourceUrl = context.getExternalContext().encodeResourceURL(resourceUrl);
        return encodedResourceUrl;
    }

    /**
     * A method which is a facade for JSF 1.2 and JSF 2.0 implementations of resource name generation. It has
     * different implementations in the appropriate OpenFaces branches.
     */
    public static String getInternalURL(FacesContext context, String resourceName) {
        return getInternalURL(context, null, resourceName, true);
    }

    public static String getInternalURL(FacesContext context, Class componentClass, String resourceName) {
        return getInternalURL(context, componentClass, resourceName, true);
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
     * @param context            Current FacesContext
     * @param componentClass     Class, relative to which the resourcePath is specified
     * @param resourcePath       Path to the resource file
     * @param prependContextPath true means that the resulting url should be prefixed with context root. This is the case
     *                           when the returned URL is rendered without any modifications. Passing false to this
     *                           parameter is required in cases when the returned URL is passed to some component which
     *                           expects application URL, so the component will prepend the URL with context root itself.
     * @return The requested URL
     */
    public static String getInternalURL(
            FacesContext context,
            Class componentClass,
            String resourcePath,
            boolean prependContextPath) {
        if (context == null) throw new NullPointerException("context");
        if (resourcePath == null) throw new NullPointerException("resourcePath");

        if (componentClass == null) {
            ResourceHandler resourceHandler = context.getApplication().getResourceHandler();
            Resource resource = resourceHandler.createResource(resourcePath, LIBRARY_NAME);
            resourcePath = resource.getRequestPath();
            return resourcePath;
        }
        String packagePath = getPackagePath(componentClass);
        if (resourcePath.startsWith("/")) {
            packagePath = "";
            resourcePath = resourcePath.substring(1);
        }

        String versionString = getVersionString();
        int extensionIndex = resourcePath.lastIndexOf(".");
        String urlRelativeToContextRoot = ResourceFilter_.INTERNAL_RESOURCE_PATH + packagePath +
                resourcePath.substring(0, extensionIndex) + "-" + versionString + resourcePath.substring(extensionIndex);

        if (!prependContextPath)
            return urlRelativeToContextRoot;

        return getApplicationURL(context, urlRelativeToContextRoot);
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
            } else if (version.contains("EA")) {
                int idx2 = buildInfo.indexOf(",", idx1 + 1);
                String buildStr = buildInfo.substring(idx1 + 1, idx2).trim();
                String buildNoPrefix1 = "build.nightly-";
                String buildNoPrefix2 = "build.";
                if (buildStr.startsWith(buildNoPrefix1)) {
                    version += "." + buildStr.substring(buildNoPrefix1.length());
                } else if (buildStr.startsWith(buildNoPrefix2)) {
                    version += ".b" + buildStr.substring(buildNoPrefix2.length());
                }
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
    public static String getUtilJsURL(FacesContext context) {
        // To be sure that default.css is included to the web page,
        // because it is also required in cases when util.js included into web page
        Styles.requestDefaultCss(context);
        return getInternalURL(context, UTIL_JS_PATH);
    }

    public static String getFiltersJsURL(FacesContext context) {
        return Resources.getInternalURL(context, "filter/filters.js");
    }

    /**
     * Return URL of ajaxUtil.js file. Keep in mind that ajaxUtil.js depends on util.js.
     * Don't forget to include util.js as well before including this URL.
     *
     * @param context {@link FacesContext} for the current request
     * @return requested URL of ajaxUtil.js file
     */
    public static String getAjaxUtilJsURL(FacesContext context) {
        return Resources.getInternalURL(context, AJAX_UTIL_JS_PATH);
    }

    /**
     * Return URL of json javascript file
     *
     * @param context {@link FacesContext} for the current request
     * @return requested URL of json javascript file
     */
    public static String getJsonJsURL(FacesContext context) {
        return Resources.getInternalURL(context, JSON_JS_PATH);
    }

    /**
     * Return full package name for Class
     *
     * @param aClass The Class object
     * @return full package name for given Class
     */
    public static String getPackageName(Class aClass) {
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
     * @param context        {@link FacesContext} for the current request
     * @param baseClass      Class, relative to which the resourcePath is specified
     * @param relativeJsPath Path to the javascript file
     */
    public static void registerJavascriptLibrary(FacesContext context, Class baseClass, String relativeJsPath) {
        String jsFileUrl = getInternalURL(context, baseClass, relativeJsPath);
        registerJavascriptLibrary(context, jsFileUrl);
    }

    /**
     * Register javascript library to future adding to response
     *
     * @param context   {@link FacesContext} for the current request
     * @param jsFileUrl Url for the javascript file
     */
    public static void registerJavascriptLibrary(FacesContext context, String jsFileUrl) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        List<String> libraries = getRegisteredJsLibraries(requestMap);
        if (libraries == null) {
            libraries = new ArrayList<String>();
            requestMap.put(HEADER_JS_LIBRARIES, libraries);
        }

        if (libraries.contains(jsFileUrl)) return;
        libraries.add(jsFileUrl);

    }

    public static void processHeadResources(FacesContext context) {
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();

        Class richfacesContextClass = null;
        try {
            richfacesContextClass = Class.forName("org.ajax4jsf.context.AjaxContext");
        } catch (ClassNotFoundException e) {
            // Just checking for class presence. It's normal that a class can be absent.
        }

        String ajax4jsfScriptParameter = (String) ReflectionUtil.getStaticFieldValue(richfacesContextClass, "SCRIPTS_PARAMETER");
        String ajax4jsfStylesParameter = (String) ReflectionUtil.getStaticFieldValue(richfacesContextClass, "STYLES_PARAMETER");
        String headEventsParameter = (String) ReflectionUtil.getStaticFieldValue(richfacesContextClass, "HEAD_EVENTS_PARAMETER");

        if (ajax4jsfStylesParameter != null) {
            Set<String> styles = (Set<String>) requestMap.get(ajax4jsfStylesParameter);
            String defaultCssUrl = ((HttpServletRequest) context.getExternalContext().getRequest()).getContextPath()
                    + ResourceFilter_.INTERNAL_RESOURCE_PATH + "org/openfaces/renderkit/default" + "-" + getVersionString() + ".css";
            if (styles == null) {
                styles = new LinkedHashSet<String>();
            }
            styles.add(defaultCssUrl);
            requestMap.put(ajax4jsfStylesParameter, styles);
        }

        if (ajax4jsfScriptParameter != null) {
            Set<String> libraries = (Set<String>) requestMap.get(ajax4jsfScriptParameter);
            List<String> ourLibraries = getRegisteredJsLibraries(requestMap);

            if (libraries == null) {
                libraries = new LinkedHashSet<String>();
            }

            if (ourLibraries != null) {
                libraries.addAll(ourLibraries);
            }

            requestMap.put(ajax4jsfScriptParameter, libraries);
        }

        if (headEventsParameter != null) {
            List<String> ourLibraries = getRegisteredJsLibraries(requestMap);
            final Node[] headerResources = (Node[]) requestMap.get(headEventsParameter);

            if (headerResources != null && ourLibraries != null) {
                final Node[] ourHeaderNodes = prepareHeaderNodes(ourLibraries);
                final Node[] mergedNodes = mergeHeadResourceNodes(ourHeaderNodes, headerResources);

                requestMap.put(headEventsParameter, mergedNodes);
            }
        }
    }

    public static List<String> getRegisteredJsLibraries() {
        FacesContext context = FacesContext.getCurrentInstance();
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        return getRegisteredJsLibraries(requestMap);
    }

    public static List<String> getRegisteredJsLibraries(Map<String, Object> requestMap) {
        return (List<String>) requestMap.get(HEADER_JS_LIBRARIES);
    }


    private static void mergeHeadResourceNode(List<Node> nodes, Set<String> renderedScripts, Node node) {
        boolean shouldAdd = true;

        String nodeName = node.getNodeName();
        if ("script".equals(nodeName) || "SCRIPT".equals(nodeName)) {
            if (node.getFirstChild() == null) {
                //no text content etc.

                NamedNodeMap attributes = node.getAttributes();
                if (attributes != null) {
                    Node item = attributes.getNamedItem("src");
                    if (item == null) {
                        attributes.getNamedItem("SRC");
                    }

                    if (item != null) {
                        String src = item.getNodeValue();
                        if (src != null) {
                            if (renderedScripts.contains(src)) {
                                shouldAdd = false;
                            } else {
                                renderedScripts.add(src);
                            }
                        }
                    }
                }
            }
        }

        if (shouldAdd) {
            nodes.add(node);
        }
    }

    private static Node[] mergeHeadResourceNodes(Node[] headerJsNodes, Node[] richFacesHeaderNodes) {
        List<Node> result = new ArrayList<Node>();

        Set<String> scripts = new HashSet<String>();

        for (Node node : richFacesHeaderNodes) {
            mergeHeadResourceNode(result, scripts, node);
        }

        for (Node node : headerJsNodes) {
            mergeHeadResourceNode(result, scripts, node);
        }

        return result.toArray(new Node[result.size()]);
    }

    private static Node[] prepareHeaderNodes(List<String> headerLibraries) {
        try {
            Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
            Node node = document.createElement("head");
            document.appendChild(node);

            for (String headerLibrary : headerLibraries) {
                Element element = document.createElement("script");
                element.setAttribute("src", headerLibrary);
                element.setAttribute("type", "text/javascript");

                node.appendChild(element);
            }

            NodeList childNodes = node.getChildNodes();
            Node[] list = new Node[childNodes.getLength()];
            for (int i = 0; i < list.length; i++) {
                list[i] = childNodes.item(i);
            }

            return list;
        } catch (ParserConfigurationException e) {
            throw new FacesException(e.getLocalizedMessage(), e);
        }
    }

    public static boolean isHeaderIncludesRegistered(ServletRequest servletRequest) {
        if (AjaxUtil.isAjaxRequest(RequestFacade.getInstance(servletRequest))) return false;
        for (Iterator<String> iterator = Styles.getClassKeyIterator(); iterator.hasNext();) {
            String key = iterator.next();
            if (servletRequest.getAttribute(key) != null) {
                return true;
            }
        }

        return servletRequest.getAttribute(Rendering.ON_LOAD_SCRIPTS_KEY) != null ||
                servletRequest.getAttribute(HEADER_JS_LIBRARIES) != null ||
                servletRequest.getAttribute(Styles.DEFAULT_CSS_REQUESTED) != null;
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

        Boolean postponeJsLinkRendering = (Boolean) context.getExternalContext().getRequestMap().get(POSTPONE_JS_LINK_RENDERING);
        if (postponeJsLinkRendering != null && postponeJsLinkRendering)
            return;
        if (AjaxUtil.isAjaxRequest(context)) {
            registerJavascriptLibrary(context, jsFile);
        } else if (AjaxUtil.isAjax4jsfRequest()) {
            registerJavascriptLibrary(context, jsFile);
        } else {
            boolean fullResourceString = jsFile.startsWith("/") || jsFile.contains("://");
            if (OpenFacesApplication.isConstructingView(context)) {
                if (fullResourceString)
                    throw new IllegalArgumentException("Resource name should be passed, not resource URL: " + jsFile);
                Resources.addHeaderResource(context, jsFile, Resources.LIBRARY_NAME);
            } else {
                if (!fullResourceString)
                    jsFile = Resources.getInternalURL(context, jsFile);
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
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        List<String> renderedJsLinks = (List<String>) requestMap.get(RENDERED_JS_LINKS);
        if (renderedJsLinks == null) {
            renderedJsLinks = new ArrayList<String>();
            requestMap.put(RENDERED_JS_LINKS, renderedJsLinks);
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

    public static void includeJQuery(FacesContext context) throws IOException {
        String jQueryIncludedKey = "org.openfaces.util.Rendering.JQUERY_INCLUDED";
        Map<String, Object> requestMap = context.getExternalContext().getRequestMap();
        if (requestMap.containsKey(jQueryIncludedKey)) return;
        requestMap.put(jQueryIncludedKey, true);
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
