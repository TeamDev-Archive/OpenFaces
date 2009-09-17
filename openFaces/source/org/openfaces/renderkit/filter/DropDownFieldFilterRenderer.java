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
package org.openfaces.renderkit.filter;

import org.openfaces.component.input.DropDownField;
import org.openfaces.component.input.DropDownItem;
import org.openfaces.component.input.DropDownItems;
import org.openfaces.component.filter.Filter;
import org.openfaces.util.StyleUtil;
import org.openfaces.util.DefaultStyles;
import org.openfaces.util.StyleGroup;
import org.openfaces.util.ComponentUtil;

import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Iterator;

/**
 * @author Dmitry Pikhulya
 */
public class DropDownFieldFilterRenderer extends TextSearchFilterRenderer {
    protected void configureInputComponent(FacesContext context, Filter filter, UIInput inputComponent) {
        DropDownField field = (DropDownField) inputComponent;
        field.setOnchange(getFilterSubmissionScript(filter));
        field.setOnkeypress(getFilterOnEnterScript(filter));
        field.setStyle(filter.getStyle());
        field.setStyleClass(StyleUtil.mergeClassNames(filter.getStyleClass(), "o_fullWidth"));
        field.setListStyle("font-weight: normal;");

        field.setPromptText(filter.getPromptText());
        field.setPromptTextClass(filter.getPromptTextClass());
        field.setPromptTextStyle(filter.getPromptTextStyle());

        String rolloverItemClass = StyleUtil.getCSSClass(context,
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
            String itemStr = itemObj != null ? itemObj.toString() : "";
            DropDownItem item = createDropDownItem(context, itemStr);
            itemList.add(item);
        }
        DropDownItem allRecordsItem = createDropDownItem(context, "");
        String allRecordsCriterionName = filter.getAllRecordsText();
        HtmlOutputText outputText = ComponentUtil.createOutputText(context, allRecordsCriterionName);
        String predefinedCriterionsClass = getPredefinedCriterionClass(context, filter);
        outputText.setStyleClass(predefinedCriterionsClass);
        allRecordsItem.getChildren().add(outputText);
        itemList.add(0, allRecordsItem);
        dropDownItems.setValue(itemList);
        List<UIComponent> children = field.getChildren();
        children.clear();
        children.add(dropDownItems);
    }

    private DropDownItem createDropDownItem(FacesContext context, String text) {
        DropDownItem item = (DropDownItem) context.getApplication().createComponent(DropDownItem.COMPONENT_TYPE);
        item.setValue(text);
        return item;
    }

}
