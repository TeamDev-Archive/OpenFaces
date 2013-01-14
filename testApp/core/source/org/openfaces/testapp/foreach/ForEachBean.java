/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.foreach;

import javax.faces.component.UIComponent;
import javax.faces.component.UIParameter;
import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public class ForEachBean {

    public class ListItem {
        private String key = "";
        private String value = "";

        public ListItem(String key, String value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    List<ListItem> testList = new ArrayList<ListItem>();

    public ForEachBean() {
        testList.add(new ListItem("a", "item A"));
        testList.add(new ListItem("b", "item B"));
        testList.add(new ListItem("c", "item C"));

        validationObjectList.add(new ValidationObject(null, null));
        validationObjectList.add(new ValidationObject(null, null));
        validationObjectList.add(new ValidationObject(null, null));
        validationObjectList.add(new ValidationObject(null, null));
    }

    public List<ListItem> getList() {
        return testList;
    }

    public void removeTestListItem(ActionEvent event) {
        ListItem itemId = (ListItem) getEventParameter(event, "itemId");
        testList.remove(itemId);
    }

    public Collection getEmptySet() {
        return Collections.EMPTY_SET;
    }

    public class SimpleObject {
        private String field1 = "field 1";
        private String field2 = "field 2";

        public String getField1() {
            return field1;
        }

        public String getField2() {
            return field2;
        }

        public void setField1(String value) {
            field1 = value;
        }

        public void setField2(String value) {
            field2 = value;
        }
    }

    private SimpleObject scalarData = new SimpleObject();

    public SimpleObject getScalarData() {
        return scalarData;
    }

    public static Object getEventParameter(ActionEvent event, String paramName) {

        Iterator<UIComponent> iterator = event.getComponent().getFacetsAndChildren();

        while (iterator.hasNext()) {
            Object component = iterator.next();
            if (component instanceof UIParameter) {
                UIParameter uiParameter = (UIParameter) component;
                if (paramName.equals(uiParameter.getName())) {
                    return uiParameter.getValue();
                }
            }
        }
        return null;
    }

    //validations

    public class ValidationObject {
        private Long longValue;
        private String stringValue;

        public ValidationObject(Long longValue, String stringValue) {
            this.longValue = longValue;
            this.stringValue = stringValue;
        }

        public Long getLongValue() {
            return longValue;
        }

        public void setLongValue(Long longValue) {
            this.longValue = longValue;
        }

        public String getStringValue() {
            return stringValue;
        }

        public void setStringValue(String stringValue) {
            this.stringValue = stringValue;
        }
    }

    private List<ValidationObject> validationObjectList = new ArrayList<ValidationObject>();

    public List<ValidationObject> getValidationObjectList() {
        return validationObjectList;
    }

    public void removeValidationObjectListItem(ActionEvent event) {
        ValidationObject itemId = (ValidationObject) getEventParameter(event, "itemId");
        validationObjectList.remove(itemId);
    }


    //------------------------  Ajax  ------------------------------

    public List<List<String>> getSuggestionsList() {
        List<List<String>> suggestionsList = new ArrayList<List<String>>();
        List<String> suggestions = new ArrayList<String>();
        suggestions.add("a1");
        suggestions.add("a2");
        suggestions.add("a3");
        suggestionsList.add(suggestions);
        suggestions = new ArrayList<String>();
        suggestions.add("b1");
        suggestions.add("b2");
        suggestions.add("b3");
        suggestionsList.add(suggestions);
        suggestions = new ArrayList<String>();
        suggestions.add("b1");
        suggestions.add("b2");
        suggestions.add("b3");
        suggestionsList.add(suggestions);
        return suggestionsList;
    }
}