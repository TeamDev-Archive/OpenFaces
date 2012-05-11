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

import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import javax.portlet.ActionRequest;
import javax.portlet.PortalContext;
import javax.portlet.RenderRequest;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Eugene Goncharov
 */
public class Environment {
    private static final String KEY_UNDEFINED_BROWSER = "undefined_browser";
    private static final String KEY_EXPLORER_BROWSER = "explorer_browser";
    private static final String KEY_EXPLORER6_BROWSER = "explorer6_browser";
    private static final String KEY_OPERA_BROWSER = "opera_browser";
    private static final String KEY_MOZILLA_BROWSER = "mozilla_browser";
    private static final String KEY_SAFARI_BROWSER = "safari_browser";
    private static final String KEY_CHROME_BROWSER = "chrome_browser";

    private static final String XHTML_CONTENT_TYPE = "application/xhtml+xml";

    private static final String PARAM_FACELETS = "org.openfaces.facelets";

    private Environment() {
    }


    public static boolean isLiferay(Map requestMap) {
        boolean isLiferayRequest;
        isLiferayRequest = (requestMap != null) && requestMap.containsKey("com.liferay.portal.kernel.servlet.PortletServletRequest");

        if (!isLiferayRequest) {
            FacesContext context = FacesContext.getCurrentInstance();
            ExternalContext externalContext = context.getExternalContext();
            Object session = externalContext.getSession(false);
            isLiferayRequest = session != null &&
                    session.getClass().getName().equalsIgnoreCase("com.liferay.portlet.PortletSessionImpl");
        }


        return isLiferayRequest;
    }

    public static boolean isRI() {
        boolean jsfRI;
        try {
            Class.forName("com.sun.faces.application.ApplicationImpl");
            jsfRI = true;
        } catch (ClassNotFoundException e) {
            jsfRI = false;
        }
        return jsfRI;
    }

    public static boolean isFacelets(FacesContext context) {
        Map<String, Object> applicationMap = context.getExternalContext().getApplicationMap();
        String isFaceletsKey = Environment.class.getName() + ".isFacelets";
        Boolean faceletsFlag = (Boolean) applicationMap.get(isFaceletsKey);
        if (faceletsFlag == null) {
            boolean facelets = isFacelets_internal(context);
            faceletsFlag = facelets ? Boolean.TRUE : Boolean.FALSE;
            applicationMap.put(isFaceletsKey, faceletsFlag);
        }
        return faceletsFlag;
    }

    private static boolean isFacelets_internal(FacesContext context) {
        String faceletsParameter = context.getExternalContext().getInitParameter(PARAM_FACELETS);
        if (faceletsParameter != null && faceletsParameter.equalsIgnoreCase("true")) {
            return true;
        }

        ViewDeclarationLanguageFactory factory = (ViewDeclarationLanguageFactory) FactoryFinder.getFactory(
                FactoryFinder.VIEW_DECLARATION_LANGUAGE_FACTORY);
        UIViewRoot viewRoot = context.getViewRoot();
        String viewId = viewRoot.getViewId();
        ViewDeclarationLanguage viewDeclarationLanguage = factory.getViewDeclarationLanguage(viewId);
        return viewDeclarationLanguage.getClass().getName().toLowerCase().startsWith("facelet");
    }

    public static boolean isMyFaces() {
        boolean myFaces;
        try {
            Class.forName("org.apache.myfaces.application.ApplicationImpl");
            myFaces = true;
        } catch (ClassNotFoundException e) {
            myFaces = false;
        }
        return myFaces;
    }

    public static boolean isAjax4jsfRequest() {
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext externalContext = facesContext.getExternalContext();
        try {
            Class ajax4jsfContextClazz = Class.forName("org.ajax4jsf.context.AjaxContext");
            Method currentInstanceMethod = ajax4jsfContextClazz.getMethod("getCurrentInstance", FacesContext.class);
            Object currentInstance = currentInstanceMethod.invoke(null, facesContext);
            if (currentInstance == null)
                return false;

            boolean isFacesContextInUse = true;
            Method isAjaxRequestMethod = null;
            try {
                isAjaxRequestMethod = ajax4jsfContextClazz.getMethod("isAjaxRequest", FacesContext.class);
            } catch (NoSuchMethodException e) {
                // We will try to search for this method without FacesContext in params
            } catch (SecurityException e) {
                //
            }
            if (isAjaxRequestMethod == null) {
                isAjaxRequestMethod = ajax4jsfContextClazz.getMethod("isAjaxRequest");
                isFacesContextInUse = false;
            }
            Boolean result = (isFacesContextInUse)
                    ? (Boolean) isAjaxRequestMethod.invoke(currentInstance, facesContext)
                    : (Boolean) isAjaxRequestMethod.invoke(currentInstance);

            return result != null && result;
        } catch (ClassNotFoundException e) {
            return false;
        } catch (NoSuchMethodException e) {
            externalContext.log("Ajax4jsf support is disabled because getCurrentInstance method was not " +
                    "found in org.ajax4jsf.context.AjaxContext.", e);
            return false;
        } catch (IllegalAccessException e) {
            externalContext.log("Ajax4jsf support is disabled because exception was thrown during " +
                    "execution of getCurrentInstance method in org.ajax4jsf.context.AjaxContext", e);
            return false;
        } catch (InvocationTargetException e) {
            externalContext.log("Ajax4jsf support is disabled because exception was thrown during " +
                    "execution of getCurrentInstance method in org.ajax4jsf.context.AjaxContext", e);
            return false;
        }
    }

    public static boolean isRichFacesStateManager(StateManager stateManager) {
        return stateManager.getClass().getName().equalsIgnoreCase("org.ajax4jsf.application.AjaxStateManager");
    }

    private static Boolean exoPortal;

    public static boolean isExoPortal() {
        if (exoPortal != null)
            return exoPortal;
        try {
            Class.forName("org.exoplatform.portlet.faces.component.UIExoViewRoot");
            exoPortal = true;
        } catch (ClassNotFoundException e) {
            exoPortal = false;
        }
        return exoPortal;
    }

    public static boolean isExplorer() {
        Boolean isExplorer = (Boolean) getSessionMap().get(KEY_EXPLORER_BROWSER);
        if (isExplorer == null) {
            isExplorer = isBrowser("msie") && !isOpera();
            getSessionMap().put(KEY_EXPLORER_BROWSER, isExplorer);
        }
        return isExplorer;
    }

    public static boolean isExplorer6() {
        Boolean isExplorer6 = (Boolean) getSessionMap().get(KEY_EXPLORER6_BROWSER);
        if (isExplorer6 == null) {
            isExplorer6 = isExplorer() && isBrowser("msie 6") && !isOpera();
            getSessionMap().put(KEY_EXPLORER6_BROWSER, isExplorer6);
        }
        return isExplorer6;
    }


    public static boolean isMozilla() {
        Boolean isMozilla = (Boolean) getSessionMap().get(KEY_MOZILLA_BROWSER);
        if (isMozilla == null) {
            isMozilla = isBrowser("mozilla") && !isExplorer() && !isSafari() && !isOpera() && !isChrome();
            getSessionMap().put(KEY_MOZILLA_BROWSER, isMozilla);
        }
        return isMozilla;
    }

    private static Map<String, Object> getSessionMap() {
        return FacesContext.getCurrentInstance().getExternalContext().getSessionMap();
    }

    public static boolean isOpera() {
        Boolean isOpera = (Boolean) getSessionMap().get(KEY_OPERA_BROWSER);
        if (isOpera == null) {
            isOpera = isBrowser("opera");
            getSessionMap().put(KEY_OPERA_BROWSER, isOpera);
        }
        return isOpera;
    }

    public static boolean isSafari() {
        Boolean isSafari = (Boolean) getSessionMap().get(KEY_SAFARI_BROWSER);
        if (isSafari == null) {
            isSafari = isBrowser("safari") && !isBrowser("chrome");
            // chrome user agent contains both "safari" and "chrome" strings
            getSessionMap().put(KEY_SAFARI_BROWSER, isSafari);
        }
        return isSafari;
    }

    public static boolean isChrome() {
        Boolean isSafari = (Boolean) getSessionMap().get(KEY_CHROME_BROWSER);
        if (isSafari == null) {
            isSafari = isBrowser("chrome");
            getSessionMap().put(KEY_CHROME_BROWSER, isSafari);
        }
        return isSafari;
    }

    public static boolean isXhtmlPlusXmlContentType(FacesContext context) {
        String contentType;
        try {
            contentType = context.getResponseWriter().getContentType();
        } catch (UnsupportedOperationException e) {
            // this is the case for eXo Portal: org.exoplatform.faces.context.HtmlResponseWriter.getContentType throws UnsupportedOperationException
            return false;
        }
        boolean result = XHTML_CONTENT_TYPE.equals(contentType);
        return result;
    }

    public static boolean isMozillaXhtmlPlusXmlContentType(FacesContext context) {
        boolean result = isMozilla() && isXhtmlPlusXmlContentType(context);
        return result;
    }

    /**
     * Browser cannot be detected under certain configurations (such as Liferay Portal). So we should provide decent
     * behavior even in such situations.
     *
     * @return true, when the browser cannot be detected due to specific configuration (e.g. Liferay Portal doesn't allow
     *         retrieving user-agent). Note that this doesn't mean that the request comes from the unsupported browser.
     */
    public static boolean isUndefinedBrowser() {
        Boolean isUndefined = (Boolean) getSessionMap().get(KEY_UNDEFINED_BROWSER);
        if (isUndefined == null) {
            isUndefined = isBrowser(KEY_UNDEFINED_BROWSER);
            getSessionMap().put(KEY_UNDEFINED_BROWSER, isUndefined);
        }
        return isUndefined;
    }

    public static boolean isBrowser(String browserName) {
        FacesContext context = FacesContext.getCurrentInstance();
        String userAgent = getUserAgent(context);
        if (userAgent == null)
            userAgent = KEY_UNDEFINED_BROWSER;

        return userAgent.toLowerCase().contains(browserName.toLowerCase());
    }

    public static boolean isMozillaFF2(FacesContext context) {
        String userAgent = getUserAgent(context);
        if (userAgent == null)
            userAgent = " ";
        return userAgent.contains("Firefox/2.");
    }

    private static String getUserAgent(FacesContext context) {
        Map<String, String> requestHeaderMap = context.getExternalContext().getRequestHeaderMap();
        String userAgent = requestHeaderMap.get("user-agent");
        return userAgent;
    }

    /**
     * Returns Mojarra version as can be found in the appropriate manifest.mf file, or an empty string if no
     * Mojarra manifest files were found
     */
    public static String getMojarraVersion(FacesContext context) {
        Map<String, Object> applicationMap = context.getExternalContext().getApplicationMap();
        String key = Environment.class.getName() + ".MOJARRA_VERSION";
        String version = (String) applicationMap.get(key);
        if (version == null) {
            List<Map<String, String>> manifestAttributes = Environment.findManifestFiles("Mojarra");
            List<String> versionStringsFound = new ArrayList<String>();
            for (Map<String, String> thisFileAttributes : manifestAttributes) {
                String v = thisFileAttributes.get("Implementation-Version");
                versionStringsFound.add(v);
            }
            // It seems there are cases when several implementations might be found in the same class-path, where one
            // version overrides the other. It doesn't seem to be possible to detect that actual precedence order, so
            // we just take the highest version assuming that application is set up properly in this case.
            Collections.sort(versionStringsFound);
            int versionCount = versionStringsFound.size();
            version = versionCount > 0 ? versionStringsFound.get(versionCount - 1) : "";
            applicationMap.put(key, version);
        }
        return version;
    }

    private static List<Map<String, String>> findManifestFiles(String bundleNameSubstring) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Set<ClassLoader> classLoaders = new HashSet<ClassLoader>();
        classLoaders.add(Environment.class.getClassLoader());

        for (ClassLoader classLoader : classLoaders) {
            List<Map<String, String>> manifestFiles = findManifestFiles(classLoader, bundleNameSubstring);
            result.addAll(manifestFiles);
        }
        return result;
    }

    private static List<Map<String, String>> findManifestFiles(ClassLoader classLoader, String bundleNameSubstring) {
        List<Map<String, String>> result = new ArrayList<Map<String, String>>();
        Enumeration<URL> resources;
        try {
            resources = classLoader.getResources("META-INF/MANIFEST.MF");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        while (resources.hasMoreElements()) {
            URL url = resources.nextElement();
            Map<String, String> thisFileAttributes = null;
            try {
                thisFileAttributes = readManifestAttributes(url);
            } catch (IOException e) {
                continue;
            }
            String bundleName = thisFileAttributes.get("Bundle-Name");
            if (bundleName == null || !bundleName.contains(bundleNameSubstring)) continue;

            result.add(thisFileAttributes);
        }
        return result;
    }

    static Map<String, String> readManifestAttributes(URL url) throws IOException {
        Map<String, String> attributes = new HashMap<String, String>();

        InputStream inputStream = url.openStream();
        InputStreamReader isr = new InputStreamReader(inputStream);
        LineNumberReader reader = new LineNumberReader(isr);
        try {
            List<String> lines = new ArrayList<String>();
            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
            while (lines.size() > 0) {
                String[] attribute = readManifestAttribute(lines);
                if (attribute != null)
                    attributes.put(attribute[0], attribute[1]);
            }
        } finally {
            reader.close();
        }

        return attributes;
    }

    static String[] readManifestAttribute(List<String> lines) {
        while (lines.size() > 0) {
            String line = lines.remove(0);
            int separatorIndex = line.indexOf(":");
            if (separatorIndex == -1) continue;
            String name = line.substring(0, separatorIndex);
            String value = line.substring(separatorIndex + 1);
            if (value.startsWith(" ")) value = value.substring(1);
            while (lines.size() > 0) {
                line = lines.remove(0);
                if (!line.startsWith(" ")) {
                    lines.add(0, line);
                    break;
                }
                value += line.substring(1);
            }
            return new String[] {name, value};
        }
        return null;
    }
    public static boolean isGateInPortal(FacesContext context) {
        ExternalContext externalContext = context.getExternalContext();
        Map<String, Object> applicationMap = externalContext.getApplicationMap();
        String gateInPortalKey = Environment.class.getName() + ".isGateInPortal.gateInPortal";
        Boolean gateInPortal = (Boolean) applicationMap.get(gateInPortalKey);
        if (gateInPortal == null)
            try {
                Object request = externalContext.getRequest();
                PortalContext portalContext;
                if (request instanceof RenderRequest)
                    portalContext = ((RenderRequest) request).getPortalContext();
                else if (request instanceof ActionRequest)
                    portalContext = ((ActionRequest) request).getPortalContext();
                else
                    portalContext = null;
                gateInPortal = portalContext != null && portalContext.getPortalInfo().contains("GateIn");
            } catch (NoClassDefFoundError e) {
                gateInPortal = false;
            }
        applicationMap.put(gateInPortalKey, gateInPortal);
        return gateInPortal;
    }

}
