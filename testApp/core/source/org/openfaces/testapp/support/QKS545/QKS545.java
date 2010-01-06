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

package org.openfaces.testapp.support.QKS545;

import org.openfaces.testapp.screenshot.Person;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Tatyana Matveyeva
 */
public class QKS545 {

    Logger logger = Logger.getLogger(QKS545.class.getName());

    private List<Person> data = new ArrayList<Person>();
    private List<Person> checkedUsers = new ArrayList<Person>();
    private String errorText = "Error!";
    private boolean successFlag = true;
    private boolean errorFlag = false;
    private String name;


    public QKS545() {
        data.add(new Person("name1", "progession1", "hobby1"));
        data.add(new Person("name2", "progession2", "hobby2"));
        data.add(new Person("name3", "progession3", "hobby3"));
        data.add(new Person("name4", "progession4", "hobby4"));
        data.add(new Person("name5", "progession5", "hobby5"));
    }

    public List<Person> getData() {
        return data;
    }

    public void setData(List<Person> data) {
        this.data = data;
    }


    public List getCheckedUsers() {
        return checkedUsers;
    }

    public void setCheckedUsers(List<Person> checkedUsers) {
        this.checkedUsers = checkedUsers;
    }


    public String getErrorText() {
        return errorText;
    }


    public boolean isSuccessFlag() {
        return successFlag;
    }

    public void setSuccessFlag(boolean successFlag) {
        this.successFlag = successFlag;
    }

    public boolean isErrorFlag() {
        return errorFlag;
    }

    public void setErrorFlag(boolean errorFlag) {
        this.errorFlag = errorFlag;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String deleteUsers() {
        logger.info("deleteUsers()");
        return null;
    }

    public String saveNewUser() {
        logger.info("saveNewUser()");
        return null;
    }
}
