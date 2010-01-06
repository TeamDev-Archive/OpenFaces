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
package org.openfaces.ajax.plugins;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Kharchenko
 */
public class PluginsLoader {
    private static final List<String> AVAILABLE_PLUGINS = Arrays.asList(
            "org.openfaces.ajax.plugins.tomahawk.TomahawkAjaxPlugin",
            "org.openfaces.ajax.plugins.tomahawk113.Tomahawk113AjaxPlugin");

    // Suppress instantiation
    private PluginsLoader() {
    }

    public static AjaxPluginIncludes getAvailableIncludes(FacesContext context) {
        AjaxPluginIncludes result = new AjaxPluginIncludes();
        Object request = context.getExternalContext().getRequest();
        Object response = context.getExternalContext().getResponse();
        if (!((request instanceof HttpServletRequest) && (response instanceof HttpServletResponse))) {
            return result;
        }
        for (String packName : AVAILABLE_PLUGINS) {
            AjaxPluginIncludes tempIncludes = getIncludesFromPlugin(packName, (HttpServletRequest) request, (HttpServletResponse) response);

            if (tempIncludes != null) {
                List<String> jsIncludes = tempIncludes.getJsIncludes();
                List<String> globalJsIncludes = result.getJsIncludes();
                globalJsIncludes.addAll(jsIncludes);

                List<String> cssIncludes = tempIncludes.getCssIncludes();
                List<String> globalCssIncludes = result.getCssIncludes();
                globalCssIncludes.addAll(cssIncludes);

                List<String> javascripts = tempIncludes.getScripts();
                List<String> globalJavaScripts = result.getScripts();
                globalJavaScripts.addAll(javascripts);
            }
        }

        List<String> jsIncludes = result.getJsIncludes();
        List<String> escapedJSList = new ArrayList<String>();
        for (String jsInclude : jsIncludes) {
            String library = jsInclude;
            library = library.replaceAll("\n", "");
            escapedJSList.add(library);
        }
        result.setJsIncludes(escapedJSList);

        return result;
    }


    private static AjaxPluginIncludes getIncludesFromPlugin(String packName, HttpServletRequest request, HttpServletResponse response) {
        try {
            Class<?> pluginClass = Class.forName(packName);
            AjaxPluginResponseWrapper responseWrapper = new AjaxPluginResponseWrapper(response);
            AbstractAjaxPlugin plugin = (AbstractAjaxPlugin) pluginClass.newInstance();
            return plugin.getIncludes(request, responseWrapper);
        } catch (ClassNotFoundException e) {
            return new AjaxPluginIncludes();
        } catch (IllegalAccessException e) {
            return new AjaxPluginIncludes();
        } catch (InstantiationException e) {
            return new AjaxPluginIncludes();
        }
    }
}
