/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.tabset;

import org.openfaces.component.input.DateChooser;
import org.openfaces.component.panel.SubPanel;
import org.openfaces.component.select.TabSetItem;
import org.openfaces.event.SelectionChangeEvent;

import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.event.ValueChangeEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Andrew Palval
 */
public class TabSetBean implements Serializable {

    Logger logger = Logger.getLogger(TabSetBean.class.getName());

    private List<String> tabSetWithSubmitTest = Arrays.asList(new String[]{"Blue", "Yellow"});
    private int tabSetWithSubmitSelectedIndex;
    private boolean valueChangeListenerAttributeWithoutSubmitFuncTest;
    public static boolean testValueChangeListener;
    public static boolean testSelectionChangeListener;

    private boolean selectionChangeListenerAttributeWithoutSubmitFuncTest;

    public List<String> getTabSetWithSubmitTest() {
        return tabSetWithSubmitTest;
    }

    public int getTabSetWithSubmitSelectedIndex() {
        return tabSetWithSubmitSelectedIndex;
    }

    public void setTabSetWithSubmitSelectedIndex(int tabSetWithSubmitSelectedIndex) {
        this.tabSetWithSubmitSelectedIndex = tabSetWithSubmitSelectedIndex;
    }

    public String getTabSetWithSubmitSelectedValue() {
        return tabSetWithSubmitTest.get(tabSetWithSubmitSelectedIndex);
    }

    public boolean isValueChangeListenerAttributeWithoutSubmitFuncTest() {
        return valueChangeListenerAttributeWithoutSubmitFuncTest;
    }

    public void setValueChangeListenerAttributeWithoutSubmitFuncTest(boolean valueChangeListenerAttributeWithoutSubmitFuncTest) {
        this.valueChangeListenerAttributeWithoutSubmitFuncTest = valueChangeListenerAttributeWithoutSubmitFuncTest;
    }

    public boolean isTestValueChangeListener() {
        return testValueChangeListener;
    }

    public boolean isTestSelectionChangeListener() {
        return testSelectionChangeListener;
    }

    public boolean isSelectionChangeListenerAttributeWithoutSubmitFuncTest() {
        return selectionChangeListenerAttributeWithoutSubmitFuncTest;
    }

    public void setSelectionChangeListenerAttributeWithoutSubmitFuncTest(boolean selectionChangeListenerAttributeWithoutSubmitFuncTest) {
        this.selectionChangeListenerAttributeWithoutSubmitFuncTest = selectionChangeListenerAttributeWithoutSubmitFuncTest;
    }

    private static class TestConverter implements Converter {
        public Object getAsObject(FacesContext context, UIComponent component, String value) throws ConverterException {
            return value;
        }

        public String getAsString(FacesContext context, UIComponent component, Object value) throws ConverterException {
            if (value == null) return "null";
            return value.toString();
        }
    }

    private static TestConverter TEST_CONVERTER = new TestConverter();

    private Collection<TabSetItem> items;
    private Collection<SubPanel> subPanels;
    private Object selectedValue;

    private int selectedIndex;
    private int selectedIndex1;

    private String value1;
    private String value2;

    public String getValue2() {
        return value2;
    }

    public void setValue2(String value2) {
        this.value2 = value2;
    }

    public String getValue1() {
        return value1;
    }

    public void setValue1(String value1) {
        this.value1 = value1;
    }

    public TestConverter getTestConverter() {
        return TEST_CONVERTER;
    }


    public int getSelectedIndex1() {
        return selectedIndex1;
    }

    public void setSelectedIndex1(int selectedIndex1) {
        this.selectedIndex1 = selectedIndex1;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }

    public Object getSelectedValue() {
        return selectedValue;
    }

    public void setSelectedValue(Object selectedValue) {
        this.selectedValue = selectedValue;
    }

    public void customValidator(FacesContext context, UIComponent component, Object object) {
        if (!"item 2".equals(object))
            return;
        context.addMessage(component.getClientId(context), new FacesMessage("custom validator error (summary)", "custom validator error (detail)"));
    }


    public void selectionChanged(SelectionChangeEvent e) {
        selectionChangeListenerAttributeWithoutSubmitFuncTest = !selectionChangeListenerAttributeWithoutSubmitFuncTest;
    }

    public void valueChanged(ValueChangeEvent e) {
        logger.info("!!! Value changed: " + e.getOldValue() + " -> " + e.getNewValue());
    }

    public Collection<TabSetItem> getItems() {
        if (items == null) {
            items = new ArrayList<TabSetItem>();
            TabSetItem item;
            HtmlOutputText tabValue;

            item = new TabSetItem();
            item.setItemValue("tServer");
            tabValue = new HtmlOutputText();
            tabValue.setValue("Server");
            item.getChildren().add(tabValue);
            items.add(item);

            item = new TabSetItem();
            item.setItemValue("tDeveloper");
            tabValue = new HtmlOutputText();
            tabValue.setValue("Developer");
            item.getChildren().add(tabValue);
            items.add(item);

            item = new TabSetItem();
            item.setItemValue("tManager");
            tabValue = new HtmlOutputText();
            tabValue.setValue("Manager");
            item.getChildren().add(tabValue);
            items.add(item);

            item = new TabSetItem();
            item.setItemValue("tGuest");
            tabValue = new HtmlOutputText();
            tabValue.setValue("Guest");
            item.getChildren().add(tabValue);
            items.add(item);
        }
        return items;
    }

    public void setItems(Collection<TabSetItem> items) {
        this.items = items;
    }

    public Collection<SubPanel> getSubPanels() {
        if (subPanels == null) {
            subPanels = new ArrayList<SubPanel>();
            SubPanel item;
            HtmlOutputText tabValue;
            HtmlOutputText containerValue;

            item = new SubPanel();
            tabValue = new HtmlOutputText();
            tabValue.setId("otext1");
            tabValue.setValue("tab1");
            item.setId("tabbbedPaneItem1");
            item.getFacets().put("caption", tabValue);
            containerValue = new HtmlOutputText();
            containerValue.setId("otext2");
            containerValue.setValue("content 1");
            item.getChildren().add(containerValue);
            subPanels.add(item);

            item = new SubPanel();
            tabValue = new HtmlOutputText();
            tabValue.setId("otext3");
            tabValue.setValue("tab2");
            item.setId("tabbbedPaneItem2");
            item.getFacets().put("caption", tabValue);
            DateChooser dateChooser = new DateChooser();
            dateChooser.setId("dc");
            item.getChildren().add(dateChooser);
            subPanels.add(item);

            item = new SubPanel();
            tabValue = new HtmlOutputText();
            tabValue.setId("otext4");
            tabValue.setValue("tab3");
            item.setId("tabbbedPaneItem3");
            item.getFacets().put("caption", tabValue);
            containerValue = new HtmlOutputText();
            containerValue.setId("otext5");
            containerValue.setValue("content 3");
            item.getChildren().add(containerValue);
            subPanels.add(item);
        }
        return subPanels;
    }

    public void setSubPanels(Collection<SubPanel> subPanels) {
        this.subPanels = subPanels;
    }

    public void valueChangedAttributeWithoutSubmitFunctTest(ValueChangeEvent event) {
        valueChangeListenerAttributeWithoutSubmitFuncTest = !valueChangeListenerAttributeWithoutSubmitFuncTest;
    }

    public void selectionChangedAttributeWithoutSubmitFunctTest(ValueChangeEvent event) {
        selectionChangeListenerAttributeWithoutSubmitFuncTest = !selectionChangeListenerAttributeWithoutSubmitFuncTest;
    }

}
