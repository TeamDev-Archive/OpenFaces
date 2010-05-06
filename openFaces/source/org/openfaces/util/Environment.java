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

import org.openfaces.ajax.AjaxViewHandler;

import javax.faces.FactoryFinder;
import javax.faces.application.StateManager;
import javax.faces.component.UIViewRoot;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewDeclarationLanguage;
import javax.faces.view.ViewDeclarationLanguageFactory;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author Eugene Goncharov
 */
public class Environment {
    public static final String PARAM_ENVIRONMENT_TRINIDAD_SUPPORT = "org.openfaces.environment.trinidadSupport";

    private static final String KEY_UNDEFINED_BROWSER = "undefined_browser";
    private static final String KEY_EXPLORER_BROWSER = "explorer_browser";
    private static final String KEY_OPERA_BROWSER = "opera_browser";
    private static final String KEY_MOZILLA_BROWSER = "mozilla_browser";
    private static final String KEY_SAFARI_BROWSER = "safari_browser";

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
        String isFaceletsKey = AjaxViewHandler.class.getName() + ".isFacelets";
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

    public static boolean isTrinidad() {
        Map applicationMap = FacesContext.getCurrentInstance().getExternalContext().getApplicationMap();
        Boolean trinidadSupport = (Boolean) applicationMap.get(PARAM_ENVIRONMENT_TRINIDAD_SUPPORT);
        return trinidadSupport != null && trinidadSupport.equals(Boolean.TRUE);
    }

    public static boolean isExplorer() {
        Boolean isExplorer = (Boolean) getSessionMap().get(KEY_EXPLORER_BROWSER);
        if (isExplorer == null) {
            isExplorer = isBrowser("msie") && !isOpera();
            getSessionMap().put(KEY_EXPLORER_BROWSER, isExplorer);
        }
        return isExplorer;
    }

    public static boolean isMozilla() {
        Boolean isMozilla = (Boolean) getSessionMap().get(KEY_MOZILLA_BROWSER);
        if (isMozilla == null) {
            isMozilla = isBrowser("mozilla") && !isExplorer() && !isSafari() && !isOpera();
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
            isSafari = isBrowser("safari");
            getSessionMap().put(KEY_SAFARI_BROWSER, isSafari);
        }
        return isSafari;
    }

    public static boolean isChrome() {
        Boolean isSafari = (Boolean) getSessionMap().get(KEY_SAFARI_BROWSER);
        if (isSafari == null) {
            isSafari = isBrowser("chrome");
            getSessionMap().put(KEY_SAFARI_BROWSER, isSafari);
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
        Boolean isUndefined = (Boolean) getSessionMap().get(KEY_SAFARI_BROWSER);
        if (isUndefined == null) {
            isUndefined = isBrowser(KEY_UNDEFINED_BROWSER);
            getSessionMap().put(KEY_SAFARI_BROWSER, isUndefined);
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
        RequestFacade request = RequestFacade.getInstance(context.getExternalContext().getRequest());
        String userAgent = request.getHeader("user-agent");
        return userAgent;
    }

}
