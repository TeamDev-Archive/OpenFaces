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

package org.openfaces.testapp.support.QKS247;

import org.openfaces.component.filter.ExpressionFilterCriterion;
import org.openfaces.component.table.DataTable;
import org.openfaces.testapp.screenshot.Person;

import java.util.ArrayList;
import java.util.List;


public class SimpleSearchHandlerBean {

    private int maxRowsInTable = 5;
    private List<Person> facilityModules = new ArrayList<Person>();
    private int rowCount;
    private int pageIndex = 0;
    private DataTable dataTable;
    private Person selectedRow;
    private int totalFilteredRecords = 0;
    private ExpressionFilterCriterion mboFilterValue = null;
    private ExpressionFilterCriterion facilityFilterValue = null;
    private ExpressionFilterCriterion vendorFilterValue = null;
    private ExpressionFilterCriterion productFamilyFilterValue = null;
    private ExpressionFilterCriterion areaFilterValue = null;
    private ExpressionFilterCriterion categoryFilterValue = null;
    private ExpressionFilterCriterion moduleFilterValue = null;
    private ExpressionFilterCriterion statusFilterValue = null;
    private String simpleSearchCriteria;


    public String getSimpleSearchCriteria() {
        return simpleSearchCriteria;
    }


    public void setSimpleSearchCriteria(String simpleSearchCriteria) {
        this.simpleSearchCriteria = simpleSearchCriteria;
    }

    public ExpressionFilterCriterion getMboFilterValue() {
        return mboFilterValue;
    }

    public void setMboFilterValue(ExpressionFilterCriterion mboFilterValue) {
        this.mboFilterValue = mboFilterValue;
    }

    public ExpressionFilterCriterion getFacilityFilterValue() {
        return facilityFilterValue;
    }

    public void setFacilityFilterValue(ExpressionFilterCriterion facilityFilterValue) {
        this.facilityFilterValue = facilityFilterValue;
    }

    public ExpressionFilterCriterion getVendorFilterValue() {
        return vendorFilterValue;
    }

    public void setVendorFilterValue(ExpressionFilterCriterion vendorFilterValue) {
        this.vendorFilterValue = vendorFilterValue;
    }

    public ExpressionFilterCriterion getProductFamilyFilterValue() {
        return productFamilyFilterValue;
    }

    public void setProductFamilyFilterValue(ExpressionFilterCriterion productFamilyFilterValue) {
        this.productFamilyFilterValue = productFamilyFilterValue;
    }

    public ExpressionFilterCriterion getAreaFilterValue() {
        return areaFilterValue;
    }

    public void setAreaFilterValue(ExpressionFilterCriterion areaFilterValue) {
        this.areaFilterValue = areaFilterValue;
    }

    public ExpressionFilterCriterion getCategoryFilterValue() {
        return categoryFilterValue;
    }

    public void setCategoryFilterValue(ExpressionFilterCriterion categoryFilterValue) {
        this.categoryFilterValue = categoryFilterValue;
    }

    public ExpressionFilterCriterion getModuleFilterValue() {
        return moduleFilterValue;
    }

    public void setModuleFilterValue(ExpressionFilterCriterion moduleFilterValue) {
        this.moduleFilterValue = moduleFilterValue;
    }

    public ExpressionFilterCriterion getStatusFilterValue() {
        return statusFilterValue;
    }

    public void setStatusFilterValue(ExpressionFilterCriterion statusFilterValue) {
        this.statusFilterValue = statusFilterValue;
    }

    public int getTotalFilteredRecords() {
        totalFilteredRecords = dataTable.getRowCount();
        return totalFilteredRecords;
    }

    public void setTotalFilteredRecords(int totalFilteredRecords) {
        this.totalFilteredRecords = totalFilteredRecords;
    }

    public Person getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(Person selectedRow) {
        this.selectedRow = selectedRow;
    }

    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public int getRowCount() {
        rowCount = facilityModules.size();
        return rowCount;
    }

    public void setRowCount(int rowCount) {
        this.rowCount = rowCount;
    }

    public SimpleSearchHandlerBean() {
        facilityModules.add(new Person(
                "John Smith",
                "Programmer",
                "Bowling, cinema"
        ));
        facilityModules.add(new Person(
                "James Erratic",
                "Technical writer",
                "Walking and thinking"
        ));
        facilityModules.add(new Person(
                "Bushy Toy",
                "Sales manager",
                "Painting"
        ));
        facilityModules.add(new Person(
                "Christina Strange",
                "Programmer",
                "Sitting on armchair"
        ));
        facilityModules.add(new Person(
                "William Green",
                "Manager",
                "Sky-jumping"
        ));
        facilityModules.add(new Person(
                "Albert Ordinary",
                "Technical writer",
                "Watching TV"
        ));
        facilityModules.add(new Person(
                "Chris Lee",
                "Architect",
                "Ancient history"
        ));
        facilityModules.add(new Person(
                "Jane White",
                "Programmer",
                "Spent time in clubs"
        ));
        facilityModules.add(new Person(
                "Jimmy Cheerful",
                "Technical writer",
                "Snowboard, sky-jumping"
        ));
        facilityModules.add(new Person(
                "Andrew Ambitious",
                "Sales manager",
                "Gardening"
        ));
        facilityModules.add(new Person(
                "Bob Forgetive",
                "Designer",
                "Playing the piano"
        ));
        facilityModules.add(new Person(
                "Michael Equable",
                "Programmer",
                "Bowling, cinema"
        ));
        facilityModules.add(new Person(
                "Ike Adolescent",
                "Programmer",
                "Playing the piano"
        ));
        facilityModules.add(new Person(
                "Mary Honey",
                "Designer",
                "Needlework"
        ));
        facilityModules.add(new Person(
                "Den Glamourous",
                "Designer",
                "Painting"
        ));
        facilityModules.add(new Person(
                "Larry Smart",
                "Programmer",
                "Mind games"
        ));
        facilityModules.add(new Person(
                "Gary Efficient",
                "Programmer",
                "Bowling, cinema"
        ));
        facilityModules.add(new Person(
                "George Mediocrity",
                "Designer",
                "Cooking"
        ));
        facilityModules.add(new Person(
                "Walter Charitable",
                "Programmer",
                "History, archaeology"
        ));
        facilityModules.add(new Person(
                "Clayton Major",
                "Manager",
                "Just sleeping"
        ));
        facilityModules.add(new Person(
                "Christian Smile",
                "Manager",
                "Just sleeping"
        ));
        facilityModules.add(new Person(
                "Diana Ironist",
                "Designer",
                "Music"
        ));
        facilityModules.add(new Person(
                "Joe Tricky",
                "Programmer",
                "Skating, snowboarding"
        ));
        facilityModules.add(new Person(
                "Dean Genius",
                "Manager",
                "Cooking"
        ));
    }


    public int getMaxRowsInTable() {
        return maxRowsInTable;
    }

    public List<Person> getFacilityModules() {
        return facilityModules;
    }

    public String actionEdit() {
        return null;
    }

    public String actionSearch() {
        return null;
    }

    public String actionCancel() {
        return null;
    }


}
