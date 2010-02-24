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

package org.openfaces.renderkit.select;

import javax.faces.context.FacesContext;

import org.openfaces.component.select.SelectBooleanCheckbox;
import org.openfaces.util.Resources;

public class SelectBooleanCheckboxImageManager {

    public static final String DEFAULT_SELECTED_IMAGE = "selectedCheckbox.png";
    public static final String DEFAULT_UNSELECTED_IMAGE = "unselectedCheckbox.png";
    public static final String DEFAULT_UNDEFINED_IMAGE = "undefinedCheckbox.png";

    public static final String DEFAULT_ROLLOVER_SELECTED_IMAGE = "rolloverSelectedCheckbox.png";
    public static final String DEFAULT_ROLLOVER_UNSELECTED_IMAGE = "rolloverUnselectedCheckbox.png";
    public static final String DEFAULT_ROLLOVER_UNDEFINED_IMAGE = "rolloverUndefinedCheckbox.png";

    public static final String DEFAULT_PRESSED_SELECTED_IMAGE = "pressedSelectedCheckbox.png";
    public static final String DEFAULT_PRESSED_UNSELECTED_IMAGE = "pressedUnselectedCheckbox.png";
    public static final String DEFAULT_PRESSED_UNDEFINED_IMAGE = "pressedUndefinedCheckbox.png";

    public static final String DEFAULT_DISABLED_SELECTED_IMAGE = "disabledSelectedCheckbox.png";
    public static final String DEFAULT_DISABLED_UNSELECTED_IMAGE = "disabledUnselectedCheckbox.png";
    public static final String DEFAULT_DISABLED_UNDEFINED_IMAGE = "disabledUndefinedCheckbox.png";


    public static boolean hasImages(SelectBooleanCheckbox checkbox) {
        return
            isSpecified(checkbox.getSelectedImageUrl())
            || isSpecified(checkbox.getUnselectedImageUrl())
            || isSpecified(checkbox.getUndefinedImageUrl())
            || isSpecified(checkbox.getRolloverSelectedImageUrl())
            || isSpecified(checkbox.getRolloverUnselectedImageUrl())
            || isSpecified(checkbox.getRolloverUndefinedImageUrl())
            || isSpecified(checkbox.getPressedSelectedImageUrl())
            || isSpecified(checkbox.getPressedUnselectedImageUrl())
            || isSpecified(checkbox.getPressedUndefinedImageUrl())
            || isSpecified(checkbox.getDisabledSelectedImageUrl())
            || isSpecified(checkbox.getDisabledUnselectedImageUrl())
            || isSpecified(checkbox.getDisabledUndefinedImageUrl())
            ;
    }

    public static String getCurrentImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        if (checkbox.isDisabled()) {
            if (checkbox.isDefined()) {
                if (checkbox.isSelected()) {
                    return getDisabledSelectedImageUrl(context, checkbox);
                } else {
                    return getDisabledUnselectedImageUrl(context, checkbox);
                }
            } else {
                return getDisabledUndefinedImageUrl(context, checkbox);
            }
        } else {
            if (checkbox.isDefined()) {
                if (checkbox.isSelected()) {
                    return getSelectedImageUrl(context, checkbox);
                } else {
                    return getUnselectedImageUrl(context, checkbox);
                }
            } else {
                return getUndefinedImageUrl(context, checkbox);
            }
        }
    }

    public static String getSelectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = checkbox.getSelectedImageUrl();
        return Resources.getURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_SELECTED_IMAGE);
    }

    public static String getUnselectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = checkbox.getUnselectedImageUrl();
        return Resources.getURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_UNSELECTED_IMAGE);
    }

    public static String getUndefinedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = checkbox.getUndefinedImageUrl();
        return Resources.getURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_UNDEFINED_IMAGE);
    }

    public static String getRolloverSelectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = firstSpecified(
                checkbox.getRolloverSelectedImageUrl(),
                checkbox.getSelectedImageUrl());

        return Resources.getURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_ROLLOVER_SELECTED_IMAGE);
    }

    public static String getRolloverUnselectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = firstSpecified(
                checkbox.getRolloverUnselectedImageUrl(),
                checkbox.getUnselectedImageUrl());

        return Resources.getURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_ROLLOVER_UNSELECTED_IMAGE);
    }

    public static String getRolloverUndefinedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = firstSpecified(
                checkbox.getRolloverUndefinedImageUrl(),
                checkbox.getUndefinedImageUrl());

        return Resources.getURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_ROLLOVER_UNDEFINED_IMAGE);
    }

    public static String getPressedSelectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = firstSpecified(
                checkbox.getPressedSelectedImageUrl(),
                checkbox.getRolloverSelectedImageUrl(),
                checkbox.getSelectedImageUrl());

        return Resources.getURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_PRESSED_SELECTED_IMAGE);
    }

    public static String getPressedUnselectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = firstSpecified(
                checkbox.getPressedUnselectedImageUrl(),
                checkbox.getRolloverUnselectedImageUrl(),
                checkbox.getUnselectedImageUrl());

        return Resources.getURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_PRESSED_UNSELECTED_IMAGE);
    }

    public static String getPressedUndefinedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = firstSpecified(
                checkbox.getPressedUndefinedImageUrl(),
                checkbox.getRolloverUndefinedImageUrl(),
                checkbox.getUndefinedImageUrl());

        return Resources.getURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_PRESSED_UNDEFINED_IMAGE);
    }

    public static String getDisabledSelectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = checkbox.getDisabledSelectedImageUrl();
        return Resources.getURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_DISABLED_SELECTED_IMAGE);
    }

    public static String getDisabledUnselectedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = checkbox.getDisabledUnselectedImageUrl();
        return Resources.getURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_DISABLED_UNSELECTED_IMAGE);
    }

    public static String getDisabledUndefinedImageUrl(FacesContext context, SelectBooleanCheckbox checkbox) {
        String imageUrl = checkbox.getDisabledUndefinedImageUrl();
        return Resources.getURL(context, imageUrl, SelectBooleanCheckboxImageManager.class, DEFAULT_DISABLED_UNDEFINED_IMAGE);
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
