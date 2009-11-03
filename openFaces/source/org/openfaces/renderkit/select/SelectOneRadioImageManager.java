/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.renderkit.select;

import org.openfaces.util.ResourceUtil;
import org.openfaces.component.select.SelectOneRadio;
import org.openfaces.component.select.SelectItem;

import javax.faces.context.FacesContext;

/**
 * @author Oleg Marshalenko
 */
public class SelectOneRadioImageManager {

    public static boolean hasImages(SelectOneRadio selectOneRadio) {
        return
            isSpecified(selectOneRadio.getSelectedImageUrl())
            || isSpecified(selectOneRadio.getUnselectedImageUrl())
            || isSpecified(selectOneRadio.getRolloverSelectedImageUrl())
            || isSpecified(selectOneRadio.getRolloverUnselectedImageUrl())
            || isSpecified(selectOneRadio.getPressedSelectedImageUrl())
            || isSpecified(selectOneRadio.getPressedUnselectedImageUrl())
            || isSpecified(selectOneRadio.getDisabledSelectedImageUrl())
            || isSpecified(selectOneRadio.getDisabledUnselectedImageUrl())
            ;
    }

    public static String getCurrentImageUrl(FacesContext context, SelectOneRadio selectOneRadio,
                                            SelectItem selectItem) {
        if (selectOneRadio.isDisabled() || selectItem.isItemDisabled()) {
            if (selectItem.getItemValue().equals(selectOneRadio.getValue())) {
                return getDisabledSelectedImageUrl(context, selectOneRadio);
            } else {
                return getDisabledUnselectedImageUrl(context, selectOneRadio);
            }
        } else {
            if (selectItem.getItemValue().equals(selectOneRadio.getValue())) {
                return getSelectedImageUrl(context, selectOneRadio);
            } else {
                return getUnselectedImageUrl(context, selectOneRadio);
            }
        }
    }

    public static String getSelectedImageUrl(FacesContext context, SelectOneRadio selectOneRadio) {
        String imageUrl = selectOneRadio.getSelectedImageUrl();
        return ResourceUtil.getResourceURL(context, imageUrl, SelectOneRadioImageManager.class, null);
    }

    public static String getUnselectedImageUrl(FacesContext context, SelectOneRadio selectOneRadio) {
        String imageUrl = selectOneRadio.getUnselectedImageUrl();
        return ResourceUtil.getResourceURL(context, imageUrl, SelectOneRadioImageManager.class, null);
    }

    public static String getRolloverSelectedImageUrl(FacesContext context, SelectOneRadio selectOneRadio) {
        String imageUrl = firstSpecified(
                selectOneRadio.getRolloverSelectedImageUrl(),
                selectOneRadio.getSelectedImageUrl());

        return ResourceUtil.getResourceURL(context, imageUrl, SelectOneRadioImageManager.class, null);
    }

    public static String getRolloverUnselectedImageUrl(FacesContext context, SelectOneRadio selectOneRadio) {
        String imageUrl = firstSpecified(
                selectOneRadio.getRolloverUnselectedImageUrl(),
                selectOneRadio.getUnselectedImageUrl());

        return ResourceUtil.getResourceURL(context, imageUrl, SelectOneRadioImageManager.class, null);
    }

    public static String getPressedSelectedImageUrl(FacesContext context, SelectOneRadio selectOneRadio) {
        String imageUrl = firstSpecified(
                selectOneRadio.getPressedSelectedImageUrl(),
                selectOneRadio.getRolloverSelectedImageUrl(),
                selectOneRadio.getSelectedImageUrl());

        return ResourceUtil.getResourceURL(context, imageUrl, SelectOneRadioImageManager.class, null);
    }

    public static String getPressedUnselectedImageUrl(FacesContext context, SelectOneRadio selectOneRadio) {
        String imageUrl = firstSpecified(
                selectOneRadio.getPressedUnselectedImageUrl(),
                selectOneRadio.getRolloverUnselectedImageUrl(),
                selectOneRadio.getUnselectedImageUrl());

        return ResourceUtil.getResourceURL(context, imageUrl, SelectOneRadioImageManager.class, null);
    }

    public static String getDisabledSelectedImageUrl(FacesContext context, SelectOneRadio selectOneRadio) {
        String imageUrl = selectOneRadio.getDisabledSelectedImageUrl();
        return ResourceUtil.getResourceURL(context, imageUrl, SelectOneRadioImageManager.class, null);
    }

    public static String getDisabledUnselectedImageUrl(FacesContext context, SelectOneRadio selectOneRadio) {
        String imageUrl = selectOneRadio.getDisabledUnselectedImageUrl();
        return ResourceUtil.getResourceURL(context, imageUrl, SelectOneRadioImageManager.class, null);
    }

    private static String firstSpecified(String... strings) {
        for (String string : strings) {
            if (isSpecified(string)) {
                return string;
            }
        }

        return null;
    }

    private static boolean isSpecified(String string) {
        return string != null && string.length() > 0;
    }

}
