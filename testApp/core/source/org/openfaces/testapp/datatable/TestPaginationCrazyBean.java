/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2015, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */
package org.openfaces.testapp.datatable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Darya Shumilina
 */
public class TestPaginationCrazyBean { // todo: this backing bean is used in many non event-related pages. review/rename


    private static final long serialVersionUID = -855625904411046277L;

    private List<Employee> rows;
    private int rowSize = 100;

    private int pageSize = 20;
    private String buttonLabel = "Pagination to Scrollbar";

    public TestPaginationCrazyBean() {
        init();
    }

    private void init() {
        if (rows == null) {
            rows = new ArrayList<Employee>();

            for (int i = 0; i < rowSize; i++) {
                Employee e = new Employee();
                e.setId(i);
                e.setFirstName("FirstName_" + i);
                e.setLastName("LastName_" + i);
                rows.add(e);
            }
        }
    }

    public List<Employee> getRows() {
        init();
        return rows;
    }

    public void setRows(List<Employee> rows) {
        this.rows = rows;
    }


    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public String getRealPageSize() {
        return String.valueOf(pageSize);
    }

    public void setRealPageSize(String pageSize) {
        this.pageSize = Integer.parseInt(pageSize);
    }


    public String getButtonLabel() {
        return buttonLabel;
    }

    public void setButtonLabel(String buttonLabel) {
        this.buttonLabel = buttonLabel;
    }

    public void changeToScrollbar() {
        System.out.println("Current PageSize: " + pageSize);
        if (this.pageSize <= 20) {
            this.pageSize = rowSize;
            this.buttonLabel = "Scrollbar to Pagination";
        } else {
            this.pageSize = 20;
            this.buttonLabel = "Pagination to Scrollbar";
        }
        System.out.println("Updated PageSize: " + pageSize);
    }

    public class Employee {

        private int id = 0;
        private String firstName = "";
        private String lastName = "";
        private String gender = "Male";

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

    }
}