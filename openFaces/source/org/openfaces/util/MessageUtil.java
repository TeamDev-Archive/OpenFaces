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

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import java.text.MessageFormat;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

/**
 * V. Korenev
 *
 * @author Manfred Geiler
 */
public class MessageUtil {
    public static final String ADDITIONAL_BUNDLE = "org.openfaces.Messages";
    private static final String DETAIL_SUFFIX = "_detail";

    public static FacesMessage getMessage(FacesContext context,
                                          FacesMessage.Severity severity,
                                          String[] messageIds,
                                          Object[] args) {
        Locale locale = context.getViewRoot().getLocale();
        String summaryPattern = getFirstString(context, locale, messageIds, false);
        String detailPattern = getFirstString(context, locale, messageIds, true);
        String summary = null;
        String detail = null;
        if (summaryPattern == null && detailPattern == null) {
            Log.log(context, "No message with id \"" + messageIds[0] + "\" found in any bundle");
            summary = messageIds[0];
        } else {
            if (summaryPattern != null) {
                MessageFormat format = new MessageFormat(summaryPattern, locale);
                summary = format.format(args);
            }
            if (detailPattern != null) {
                MessageFormat format = new MessageFormat(detailPattern, locale);
                detail = format.format(args);
            }
        }
        return new FacesMessage(severity, summary, detail);
    }

    public static FacesMessage getMessage(FacesContext context,
                                          FacesMessage.Severity severity,
                                          String messageId,
                                          Object args[]) {
        return getMessage(context, severity, new String[]{messageId}, args);
    }

    private static String getFirstString(FacesContext context, Locale locale, String[] keys, boolean detail) {
        for (String key : keys) {
            if (detail) {
                key += DETAIL_SUFFIX;
            }
            ResourceBundle appBundle = getApplicationBundle(context, locale);
            String appString = getBundleString(appBundle, key);
            if (appString != null) {
                return appString;
            } else {

                ResourceBundle additionalBundle = getAdditionalBundle(context, locale);
                String additionalBundleString = getBundleString(additionalBundle, key);
                if (additionalBundleString != null) {
                    return additionalBundleString;
                } else {
                    ResourceBundle defBundle = getDefaultBundle(context, locale);
                    String defString = getBundleString(defBundle, key);
                    if (defString != null) {
                        return defString;
                    }
                }
            }
        }
        return null;
    }

    private static String getBundleString(ResourceBundle bundle, String key) {
        try {
            return bundle == null ? null : bundle.getString(key);
        }
        catch (MissingResourceException e) {
            return null;
        }
    }


    private static ResourceBundle getApplicationBundle(FacesContext context, Locale locale) {
        String bundleName = context.getApplication().getMessageBundle();
        if (bundleName != null) {
            return getBundle(context, locale, bundleName);
        } else {
            return null;
        }
    }

    private static ResourceBundle getDefaultBundle(FacesContext context, Locale locale) {
        return getBundle(context, locale, FacesMessage.FACES_MESSAGES);
    }

    private static ResourceBundle getAdditionalBundle(FacesContext context, Locale locale) {
        return getBundle(context, locale, ADDITIONAL_BUNDLE);
    }

    private static ResourceBundle getBundle(FacesContext context,
                                            Locale locale,
                                            String bundleName) {
        try {
            return ResourceBundle.getBundle(bundleName, locale, context.getClass().getClassLoader());
        }
        catch (MissingResourceException e1) {
            try {
                return ResourceBundle.getBundle(bundleName, locale);
            }
            catch (MissingResourceException e2) {
                try {
                    return ResourceBundle.getBundle(bundleName, locale, Thread.currentThread().getContextClassLoader());
                }
                catch (MissingResourceException e3) {
                    Log.log(context, "Resource bundle \"" + bundleName + "\" could not be found");
                    return null;
                }
            }
        }
    }
}
