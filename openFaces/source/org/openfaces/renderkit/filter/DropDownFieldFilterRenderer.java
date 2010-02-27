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
package org.openfaces.renderkit.filter;

import org.openfaces.component.filter.ExpressionFilter;
import org.openfaces.component.input.DropDownField;
import org.openfaces.component.input.DropDownItem;
import org.openfaces.component.input.DropDownItems;
import org.openfaces.util.Components;
import org.openfaces.util.DefaultStyles;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.Styles;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * @author Dmitry Pikhulya
 */
public class DropDownFieldFilterRenderer extends TextSearchFilterRenderer {
    private static final String[] DROP_DOWN_FIELD_ATTRIBUTES = {
            "rolloverStyle",
            "rolloverClass",
            "focusedStyle",
            "focusedClass",
            "promptText",
            "promptTextStyle",
            "promptTextClass",
            "autoComplete",
            "listAlignment",
            "customValueAllowed",
            "suggestionMode",
            "suggestionDelay",
            "timeout",
            "horizontalGridLines",
            "listItemStyle",
            "listItemClass",
            "rolloverListItemStyle",
            "rolloverListItemClass",
            "oddListItemStyle",
            "oddListItemClass",
            "suggestionMinChars",

            "maxlength",
            "size",

            "buttonAlignment",

            "fieldStyle",
            "rolloverFieldStyle",
            "buttonStyle",
            "rolloverButtonStyle",
            "pressedButtonStyle",
            "listStyle",
            "rolloverListStyle",

            "fieldClass",
            "rolloverFieldClass",
            "buttonClass",
            "rolloverButtonClass",
            "pressedButtonClass",
            "listClass",
            "rolloverListClass",

            "buttonImageUrl"
    };

    protected void configureInputComponent(FacesContext context, ExpressionFilter filter, UIInput inputComponent) {
        DropDownField field = (DropDownField) inputComponent;
        field.setOnchange(getFilterSubmissionScript(filter));
        field.setOnkeypress(getFilterKeyPressScript(filter));
        field.setStyle(filter.getStyle());
        field.setStyleClass(Styles.mergeClassNames(filter.getStyleClass(), "o_fullWidth"));
        field.setListStyle("font-weight: normal;");

        String rolloverItemClass = Styles.getCSSClass(context,
                filter, "background: " + DefaultStyles.getSelectionBackgroundColor() +
                        " !important; color: " + DefaultStyles.getSelectionTextColor() + " !important;", StyleGroup.selectedStyleGroup(), null);
        field.setRolloverListItemClass(rolloverItemClass);

        int childrenCount = field.getChildren().size();
        if (childrenCount != 1) {
            throw new IllegalStateException("Search component of DropDownFieldFilter should have exactly one child component - " +
                    "the DropDownItems component. children.size = " + childrenCount);
        }
        Object dropDownFieldItems = field.getChildren().get(0);
        if (!(dropDownFieldItems instanceof DropDownItems)) {
            throw new IllegalStateException("Search component of DropDownFieldFilter should have exactly one child component - " +
                    "instace of DropDownItems component. But was  - " + dropDownFieldItems.toString());
        }
        DropDownItems dropDownItems = (DropDownItems) dropDownFieldItems;
        Collection<Object> possibleValuesCollection = filter.calculateAllCriterionNames(context);
        for (Iterator<Object> criterionIterator = possibleValuesCollection.iterator(); criterionIterator.hasNext();) {
            Object criterionObj = criterionIterator.next();
            if (isEmptyItem(criterionObj)) {
                criterionIterator.remove();
            }
        }

        List<Object> availableItems = new ArrayList<Object>(possibleValuesCollection);
        List<DropDownItem> itemList = new ArrayList<DropDownItem>(availableItems.size());
        for (Object itemObj : availableItems) {
            DropDownItem item = createDropDownItem(context, itemObj);
            itemList.add(item);
        }
        DropDownItem allRecordsItem = createDropDownItem(context, null);
        String allRecordsCriterionName = filter.getAllRecordsText();
        HtmlOutputText outputText = Components.createOutputText(context, allRecordsCriterionName);
        String predefinedCriterionsClass = getPredefinedCriterionClass(context, filter);
        outputText.setStyleClass(predefinedCriterionsClass);
        allRecordsItem.getChildren().add(outputText);
        itemList.add(0, allRecordsItem);
        dropDownItems.setValue(itemList);
        List<UIComponent> children = field.getChildren();
        children.clear();
        children.add(dropDownItems);
    }

    protected String[] getCopiedFilterAttributes() {
        return DROP_DOWN_FIELD_ATTRIBUTES;
    }

    private DropDownItem createDropDownItem(FacesContext context, Object value) {
        DropDownItem item = (DropDownItem) context.getApplication().createComponent(DropDownItem.COMPONENT_TYPE);
        item.setItemValue(value);
        return item;
    }

}
