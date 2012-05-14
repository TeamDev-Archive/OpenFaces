/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.testapp.screenshot;

import org.openfaces.component.table.DataTable;

import java.util.ArrayList;
import java.util.List;


public class PeopleListBean {

    private List<Person> person = new ArrayList<Person>();
    private DataTable dataTable;
    private List selectedRows = new ArrayList();
    private Person selectedRow;
    private String selectedPerson;
    private int pageIndex;

    public PeopleListBean() {

        person.add(new Person(
                "John Smith",
                "Programmer",
                "Bowling, cinema"
        ));
        person.add(new Person(
                "James Erratic",
                "Technical writer",
                "Walking and thinking"
        ));
        person.add(new Person(
                "Bushy Toy",
                "Sales manager",
                "Painting"
        ));
        person.add(new Person(
                "Christina Strange",
                "Programmer",
                "Sitting on armchair"
        ));
        person.add(new Person(
                "William Green",
                "Manager",
                "Sky-jumping"
        ));
        person.add(new Person(
                "Albert Ordinary",
                "Technical writer",
                "Watching TV"
        ));
        person.add(new Person(
                "Chris Lee",
                "Architect",
                "Ancient history"
        ));
        person.add(new Person(
                "Jane White",
                "Programmer",
                "Spent time in clubs"
        ));
        person.add(new Person(
                "Jimmy Cheerful",
                "Technical writer",
                "Snowboard, sky-jumping"
        ));
        person.add(new Person(
                "Andrew Ambitious",
                "Sales manager",
                "Gardening"
        ));
        person.add(new Person(
                "Bob Forgetive",
                "Designer",
                "Playing the piano"
        ));
        person.add(new Person(
                "Michael Equable",
                "Programmer",
                "Bowling, cinema"
        ));
        person.add(new Person(
                "Ike Adolescent",
                "Programmer",
                "Playing the piano"
        ));
        person.add(new Person(
                "Mary Honey",
                "Designer",
                "Needlework"
        ));
        person.add(new Person(
                "Den Glamourous",
                "Designer",
                "Painting"
        ));
        person.add(new Person(
                "Larry Smart",
                "Programmer",
                "Mind games"
        ));
        person.add(new Person(
                "Gary Efficient",
                "Programmer",
                "Bowling, cinema"
        ));
        person.add(new Person(
                "George Mediocrity",
                "Designer",
                "Cooking"
        ));
        person.add(new Person(
                "Walter Charitable",
                "Programmer",
                "History, archaeology"
        ));
        person.add(new Person(
                "Clayton Major",
                "Manager",
                "Just sleeping"
        ));
        person.add(new Person(
                "Christian Smile",
                "Manager",
                "Just sleeping"
        ));
        person.add(new Person(
                "Diana Ironist",
                "Designer",
                "Music"
        ));
        person.add(new Person(
                "Joe Tricky",
                "Programmer",
                "Skating, snowboarding"
        ));
        person.add(new Person(
                "Dean Genius",
                "Manager",
                "Cooking"
        ));
    }


    public List getSelectedRows() {
        return selectedRows;
    }

    public void setSelectedRows(List selectedRows) {
        this.selectedRows = selectedRows;
    }


    public Person getSelectedRow() {
        return selectedRow;
    }

    public void setSelectedRow(Person selectedRow) {
        this.selectedRow = selectedRow;
    }

    public String refreshDetail() {
        selectedPerson = selectedRow.getName();
        return null;
    }


    public String getSelectedPerson() {
        return selectedPerson;
    }

    public void setSelectedPerson(String selectedPerson) {
        this.selectedPerson = selectedPerson;
    }

    public List<Person> getPerson() {
        return person;
    }


    public DataTable getDataTable() {
        return dataTable;
    }

    public void setDataTable(DataTable dataTable) {
        this.dataTable = dataTable;
    }

    public String sortDataTable() {
        dataTable.setSortColumnId("hobby");
        dataTable.setSortAscending(false);
        return "";

    }

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }
}
