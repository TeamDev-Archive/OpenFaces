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

import org.openfaces.ajax.Tag;
import org.openfaces.util.StringInspector;

import javax.servlet.http.HttpServletRequest;

/**
 * Ajax support for third-party components libraries.
 * <p/>
 * Each plugin that extends this class should have <code>AjaxPlugin</code> class name and should be implemented in
 * <code>org.openfaces.ajax.plugins.[components_library]</code> package.
 *
 * @author Kharchenko
 */
public abstract class AbstractAjaxPlugin {
    private static final String SCRIPT = "<script";

    public abstract AjaxPluginIncludes getIncludes(HttpServletRequest request, AjaxPluginResponseWrapper responseWrapper);

    protected AjaxPluginIncludes extractIncludes(String strResult) {
        AjaxPluginIncludes result = new AjaxPluginIncludes();
        if (strResult == null || strResult.length() == 0)
            return result;
        StringInspector strResultInspector = new StringInspector(strResult);
        int headStartIdx = strResultInspector.indexOfIgnoreCase(Tag.HEAD.start);
        int headEndIdx = strResultInspector.indexOfIgnoreCase(Tag.HEAD.end);
        if (headEndIdx == -1 || headStartIdx == -1) {
            return result;
        }

        StringInspector strHead = strResultInspector.substring(headStartIdx + Tag.HEAD.start.length(), headEndIdx);
        int scriptStartIdx;
        // process javascript libraries and javascripts (init scripts)
        final String scriptEndStr = Tag.SCRIPT.end;
        while ((scriptStartIdx = strHead.indexOfIgnoreCase(SCRIPT)) > -1) {
            int scriptEndIdx = strHead.indexOfIgnoreCase(scriptEndStr) + scriptEndStr.length();
            StringInspector script;
            boolean endOfString = scriptEndIdx > strHead.toString().length() - 1;
            if (endOfString) {
                script = strHead.substring(scriptStartIdx);
            } else {
                script = strHead.substring(scriptStartIdx, scriptEndIdx);
            }
            String tempRes = getJsLibrarySourceString(script.toString());
            if (tempRes.length() > 0)
                result.getJsIncludes().add(tempRes);
            else {
                tempRes = getPureJavaScript(script);
                if (tempRes.length() > 0) {
                    result.getScripts().add(tempRes);
                }
            }

            if (endOfString) {
                strHead = strHead.substring(0, scriptStartIdx);
            } else {
                strHead = strHead.substring(0, scriptStartIdx).concatenate(strHead.substring(scriptEndIdx));
            }
        }

        // process CSS links
        while ((scriptStartIdx = strHead.indexOfIgnoreCase("<link")) > -1) {
            final String hrefStr = "href=\"";
            int hrefIdxStart = strHead.indexOfIgnoreCase(hrefStr, scriptStartIdx + 1);
            if (hrefIdxStart > -1) {
                int hrefIdxEnd = strHead.toString().indexOf("\"", hrefIdxStart + hrefStr.length());
                if (hrefIdxEnd > -1) {
                    final String cssLink = strHead.toString().substring(hrefIdxStart + hrefStr.length(), hrefIdxEnd);
                    if (cssLink.length() > 0) {
                        result.getCssIncludes().add(cssLink);
                    }
                    strHead = strHead.substring(hrefIdxEnd);
                }
            }
        }
        return result;
    }

    protected String getJsLibrarySourceString(String declaration) {
        String srcStr = "src=\"";
        int startSrcIndex = declaration.indexOf(srcStr);
        if (startSrcIndex == -1) return "";
        int endSrcIndex = declaration.indexOf("\"", startSrcIndex + srcStr.length());
        if (endSrcIndex == -1) return "";
        return declaration.substring(startSrcIndex + srcStr.length(), endSrcIndex);
    }

    protected String getPureJavaScript(StringInspector declaration) {
        StringBuffer result = new StringBuffer();
        int startIdx = declaration.indexOfIgnoreCase(SCRIPT);
        int endIdx = declaration.indexOfIgnoreCase(Tag.SCRIPT.end);
        if (startIdx == -1 || endIdx == -1) return declaration.toString();
        int endScriptInit = declaration.toString().indexOf(">", startIdx + 1);
        if (startIdx > 0) {
            result.append(declaration.substring(0, startIdx));
            result.append("\n");
            declaration = declaration.substring(startIdx);
            //re-read indices
            startIdx = declaration.indexOfIgnoreCase(SCRIPT);
            endIdx = declaration.indexOfIgnoreCase(Tag.SCRIPT.end);
            if (startIdx != -1)
                endScriptInit = declaration.toString().indexOf(">", startIdx + 1);
        }
        if (endScriptInit == -1) return declaration.toString();
        while (startIdx > -1) {
            result.append(declaration.substring(endScriptInit + 1, endIdx));
            result.append("\n");
            declaration = declaration.substring(endIdx + Tag.SCRIPT.end.length());
            //re-read indices
            startIdx = declaration.indexOfIgnoreCase(SCRIPT);
            endIdx = declaration.indexOfIgnoreCase(Tag.SCRIPT.end);
            if (startIdx > -1)
                endScriptInit = declaration.toString().indexOf(">", startIdx + 1);
        }
        if (declaration.toString().length() > 0) {
            result.append(declaration);
            result.append("\n");
        }
        return result.toString().replaceAll("<!--", "").replaceAll("//-->", "");
    }
}