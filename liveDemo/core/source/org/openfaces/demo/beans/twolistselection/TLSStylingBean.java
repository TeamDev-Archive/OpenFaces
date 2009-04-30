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
package org.openfaces.demo.beans.twolistselection;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

public class TLSStylingBean implements Serializable {
    private static final String DEFAULT_STYLE = "width: 100%;";
    private static final String STYLE = "width: 100%; background-color: #ffffff;";
    private static final String BUTTON_STYLE = "font-family: Tahoma; color: #005ca3; width: 90px; font-size: 10px;";
    private static final String CAPTION_STYLE = "font-family: Tahoma; background-color: #ffffff; color: #ff8a01; white-space: nowrap;";
    private static final String LIST_STYLE = "font-family: Tahoma; background-color: #e5e5e5; color: #005ca3;";

    private static final String ALLOW_ADD_REMOVE_ALL = "Allow Add/Remove All";
    private static final String ORDERING_AND_SORTING = "Items Ordering and Sorting";
    private static final String BUTTON_TEXTS_CUSTOMIZATION = "Button Texts Customization";
    private static final String CAPTIONS_CUSTOMIZATION = "List Captions Customization";
    private static final String STYLES_CUSTOMIZATION = "Styles Customization";

    private List<SelectItem> customizations = Arrays.asList(
            new SelectItem(ALLOW_ADD_REMOVE_ALL),
            new SelectItem(ORDERING_AND_SORTING),
            new SelectItem(BUTTON_TEXTS_CUSTOMIZATION),
            new SelectItem(CAPTIONS_CUSTOMIZATION),
            new SelectItem(STYLES_CUSTOMIZATION));
    private List<String> selectedCustomizations = Arrays.asList(
            ALLOW_ADD_REMOVE_ALL,
            ORDERING_AND_SORTING);

    public List<SelectItem> getCustomizations() {
        return customizations;
    }

    public void setCustomizations(List<SelectItem> customizations) {
        this.customizations = customizations;
    }

    public List<String> getSelectedCustomizations() {
        return selectedCustomizations;
    }

    public void setSelectedCustomizations(List<String> selectedCustomizations) {
        this.selectedCustomizations = selectedCustomizations;
    }

    public boolean getAllowAddRemoveAll() {
        return selectedCustomizations.contains(ALLOW_ADD_REMOVE_ALL);
    }

    public boolean getAllowOrdering() {
        return selectedCustomizations.contains(ORDERING_AND_SORTING);
    }

    public boolean getAllowSorting() {
        return selectedCustomizations.contains(ORDERING_AND_SORTING);
    }

    public String getAddAllText() {
        return selectedCustomizations.contains(BUTTON_TEXTS_CUSTOMIZATION) ? "Add All" : ">>";
    }

    public String getAddText() {
        return selectedCustomizations.contains(BUTTON_TEXTS_CUSTOMIZATION) ? "Add Selected" : ">";
    }

    public String getRemoveAllText() {
        return selectedCustomizations.contains(BUTTON_TEXTS_CUSTOMIZATION) ? "Remove All" : "<<";
    }

    public String getRemoveText() {
        return selectedCustomizations.contains(BUTTON_TEXTS_CUSTOMIZATION) ? "Remove Selected" : "<";
    }

    public String getMoveDownText() {
        return selectedCustomizations.contains(BUTTON_TEXTS_CUSTOMIZATION) ? "Move Down" : "Down";
    }

    public String getMoveUpText() {
        return selectedCustomizations.contains(BUTTON_TEXTS_CUSTOMIZATION) ? "Move Up" : "Up";
    }

    public String getLeftCaption() {
        return selectedCustomizations.contains(CAPTIONS_CUSTOMIZATION) ? "All Tasks" : "Available Tasks";
    }

    public String getRightCaption() {
        return selectedCustomizations.contains(CAPTIONS_CUSTOMIZATION) ? "Chosen Tasks" : "Selected Tasks";
    }

    public String getButtonStyle() {
        return selectedCustomizations.contains(STYLES_CUSTOMIZATION) ? BUTTON_STYLE : null;
    }

    public String getStyle() {
        return selectedCustomizations.contains(STYLES_CUSTOMIZATION) ? STYLE : DEFAULT_STYLE;
    }

    public String getCaptionStyle() {
        return selectedCustomizations.contains(STYLES_CUSTOMIZATION) ? CAPTION_STYLE : null;
    }

    public String getListStyle() {
        return selectedCustomizations.contains(STYLES_CUSTOMIZATION) ? LIST_STYLE : null;
    }


}