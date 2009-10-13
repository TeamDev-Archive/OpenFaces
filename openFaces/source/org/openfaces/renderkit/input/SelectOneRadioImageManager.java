package org.openfaces.renderkit.input;

import org.openfaces.util.ResourceUtil;
import org.openfaces.component.input.SelectOneRadio;
import org.openfaces.component.input.SelectOneRadioItem;

import javax.faces.context.FacesContext;

/**
 * Author: Oleg Marshalenko
 * Date: Sep 18, 2009
 * Time: 4:07:27 PM
 */
public class SelectOneRadioImageManager {

//    public static final String DEFAULT_SELECTED_IMAGE = "selectedRadioButton.png";
//    public static final String DEFAULT_UNSELECTED_IMAGE = "unselectedRadioButton.png";
//
//    public static final String DEFAULT_ROLLOVER_SELECTED_IMAGE = "rolloverSelectedRadioButton.png";
//    public static final String DEFAULT_ROLLOVER_UNSELECTED_IMAGE = "rolloverUnselectedRadioButton.png";
//
//    public static final String DEFAULT_PRESSED_SELECTED_IMAGE = "pressedSelectedRadioButton.png";
//    public static final String DEFAULT_PRESSED_UNSELECTED_IMAGE = "pressedUnselectedRadioButton.png";
//
//    public static final String DEFAULT_DISABLED_SELECTED_IMAGE = "disabledSelectedRadioButton.png";
//    public static final String DEFAULT_DISABLED_UNSELECTED_IMAGE = "disabledUnselectedRadioButton.png";


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
                                            SelectOneRadioItem selectOneRadioItem) {
        if (selectOneRadio.isDisabled() || selectOneRadioItem.isDisabled()) {
            if (selectOneRadioItem.getValue().equals(selectOneRadio.getValue())) {
                return getDisabledSelectedImageUrl(context, selectOneRadio);
            } else {
                return getDisabledUnselectedImageUrl(context, selectOneRadio);
            }
        } else {
            if (selectOneRadioItem.getValue().equals(selectOneRadio.getValue())) {
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
