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
package org.openfaces.testapp.datatable;

import org.openfaces.component.input.DropDownItem;
import org.openfaces.component.table.DataTable;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * @author Darya Shumilina
 */
public class TestEventsBean { // todo: this backing bean is used in many non event-related pages. review/rename

    private List<TestTableItem> tableTestCollection = new ArrayList<TestTableItem>();
    private List<TestTableItemNotSerializable> tableTestCollection_notSerializable = new ArrayList<TestTableItemNotSerializable>();
    private List emptyTestCollection = new ArrayList();
    private List<TestTableItem> dataTableMajorFeaturesTestCollection = new ArrayList<TestTableItem>();
    private List list = new ArrayList();
    private List checkedList = new ArrayList();

    private TestTableItem selectedItem;
    private DataTable testTable = new DataTable();
    private List calendarSelectionList = new ArrayList();
    private List dropDownSelectionList = new ArrayList();

    private List<TestTableItem> tomahawkCollection = new ArrayList<TestTableItem>();
    private List<TestTableItem> tomahawkDataTable = new ArrayList<TestTableItem>();
    private List<CalendarItem> calendarList = new ArrayList<CalendarItem>();
    private Point scrollPos;

    private TestTableItem selectedDropDownValue;

    public TestEventsBean() {
        scrollPos = new Point(0, 760);

        tomahawkCollection.add(new TestTableItem("col1_row1", "col2_row1", "col3_row1", "col4_row1"));
        tomahawkCollection.add(new TestTableItem("col1_row2", "col2_row2", "col3_row2", "col4_row2"));
        tomahawkCollection.add(new TestTableItem("col1_row3", "col2_row3", "col3_row3", "col4_row3"));
        tomahawkCollection.add(new TestTableItem("col1_row4", "col2_row4", "col3_row4", "col4_row4"));

        tomahawkDataTable.add(new TestTableItem("col1_row1", "col2_row1", "col3_row1", "col4_row1"));

        tableTestCollection.add(new TestTableItem("col1_row1", "col2_row1", "col3_row1", "col4_row1"));
        tableTestCollection.add(new TestTableItem("col1_row2", "col2_row2", "col3_row2", "col4_row2"));
        tableTestCollection.add(new TestTableItem("col1_row3", "col2_row3", "col3_row3", "col4_row3"));
        tableTestCollection.add(new TestTableItem("col1_row4", "col2_row4", "col3_row4", "col4_row4"));
        tableTestCollection.add(new TestTableItem("col1_row5", "col2_row5", "col3_row5", "col4_row5"));
        tableTestCollection.add(new TestTableItem("col1_row6", "col2_row6", "col3_row6", "col4_row6"));
        tableTestCollection.add(new TestTableItem("col1_row7", "col2_row7", "col3_row7", "col4_row7"));
        tableTestCollection.add(new TestTableItem("col1_row8", "col2_row8", "col3_row8", "col4_row8"));
        tableTestCollection.add(new TestTableItem("col1_row9", "col2_row9", "col3_row9", "col4_row9"));

        for (TestTableItem item : tableTestCollection) {
            tableTestCollection_notSerializable.add(new TestTableItemNotSerializable(
                    item.getFirstColumn(), item.getSecondColumn(), item.getThirdColumn(), item.getFourthColumn())
            );
        }

        calendarList.add(new CalendarItem("1", new Locale("fr"), "color: red;"));
        calendarList.add(new CalendarItem("2", new Locale("ar"), "color: blue;"));
        calendarList.add(new CalendarItem("3", new Locale("sk"), "color: magenta;"));
        calendarList.add(new CalendarItem("4", new Locale("be"), "color: green;"));
        calendarList.add(new CalendarItem("5", new Locale("mk"), "color: yellow;"));
        calendarList.add(new CalendarItem("6", new Locale("fi"), "color: teal;"));
        calendarList.add(new CalendarItem("7", new Locale("el"), "color: pink;"));
        calendarList.add(new CalendarItem("8", new Locale("cs"), "color: darkcyan;"));
        calendarList.add(new CalendarItem("9", new Locale("is"), "color: goldenrod;"));

        dataTableMajorFeaturesTestCollection.add(new TestTableItem("id_1", "criterion_1,2,3", "criterion_1,4,7", "1"));
        dataTableMajorFeaturesTestCollection.add(new TestTableItem("id_2", "criterion_1,2,3", "criterion_2,5,8", "2"));
        dataTableMajorFeaturesTestCollection.add(new TestTableItem("id_3", "criterion_1,2,3", "criterion_3,6,9", "3"));
        dataTableMajorFeaturesTestCollection.add(new TestTableItem("id_4", "criterion_4,5,6", "criterion_2,5,8", "4"));
        dataTableMajorFeaturesTestCollection.add(new TestTableItem("id_5", "criterion_4,5,6", "criterion_1,4,7", "5"));
        dataTableMajorFeaturesTestCollection.add(new TestTableItem("id_6", "criterion_4,5,6", "criterion_3,6,9", "6"));
        dataTableMajorFeaturesTestCollection.add(new TestTableItem("id_7", "criterion_7,8,9", "criterion_1,4,7", "7"));
        dataTableMajorFeaturesTestCollection.add(new TestTableItem("id_8", "criterion_7,8,9", "criterion_3,6,9", "8"));
        dataTableMajorFeaturesTestCollection.add(new TestTableItem("id_9", "criterion_7,8,9", "criterion_2,5,8", "9"));

    }

    public List<TestTableItem> getTableTestCollection() {
        return tableTestCollection;
    }

    public List<TestTableItemNotSerializable> getTableTestCollection_notSerializable() {
        return tableTestCollection_notSerializable;
    }


    public List getList() {
        return list;
    }

    public void setList(List list) {
        this.list = list;
    }

    public List getCheckedList() {
        return checkedList;
    }

    public void setCheckedList(List checkedList) {
        this.checkedList = checkedList;
    }

    public TestTableItem getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(TestTableItem selectedItem) {
        this.selectedItem = selectedItem;
    }

    public List<DropDownItem> getDropDownList() {
        List<DropDownItem> tempDropDownList = new ArrayList<DropDownItem>();
        for (TestTableItem temp : tableTestCollection) {
            DropDownItem tempDropDownItem = new DropDownItem();
            tempDropDownItem.setValue(temp.getSecondColumn());
            tempDropDownList.add(tempDropDownItem);
        }
        return tempDropDownList;
    }

    public void setDropDownList(List dropDownList) {
    }

    public DataTable getTestTable() {
        return testTable;
    }

    public void setTestTable(DataTable testTable) {
        this.testTable = testTable;
    }

    public int getCurrentRowIndex() {
        return getTestTable().getRowIndex();
    }

    public List getCalendarSelectionList() {
        return calendarSelectionList;
    }

    public void setCalendarSelectionList(List calendarSelectionList) {
        this.calendarSelectionList = calendarSelectionList;
    }

    public List getDropDownSelectionList() {
        return dropDownSelectionList;
    }

    public void setDropDownSelectionList(List dropDownSelectionList) {
        this.dropDownSelectionList = dropDownSelectionList;
    }

    public List<CalendarItem> getCalendarList() {
        return calendarList;
    }

    public void setCalendarList(List<CalendarItem> calendarList) {
        this.calendarList = calendarList;
    }

    public List<TestTableItem> getTomahawkCollection() {
        return tomahawkCollection;
    }

    public void setTomahawkCollection(List<TestTableItem> tomahawkCollection) {
        this.tomahawkCollection = tomahawkCollection;
    }

    public List<TestTableItem> getTomahawkDataTable() {
        return tomahawkDataTable;
    }

    public void setTomahawkDataTable(List<TestTableItem> tomahawkDataTable) {
        this.tomahawkDataTable = tomahawkDataTable;
    }

    public Point getScrollPos() {
        return scrollPos;
    }

    public void setScrollPos(Point scrollPos) {
        this.scrollPos = scrollPos;
    }

    public List<TestTableItem> getDataTableMajorFeaturesTestCollection() {
        return dataTableMajorFeaturesTestCollection;
    }

    public void setDataTableMajorFeaturesTestCollection(List<TestTableItem> dataTableMajorFeaturesTestCollection) {
        this.dataTableMajorFeaturesTestCollection = dataTableMajorFeaturesTestCollection;
    }

    public List getEmptyTestCollection() {
        return emptyTestCollection;
    }

    public TestTableItem getSelectedDropDownValue() {
        return selectedDropDownValue;
    }

    public void setSelectedDropDownValue(TestTableItem selectedDropDownValue) {
        this.selectedDropDownValue = selectedDropDownValue;
    }
}