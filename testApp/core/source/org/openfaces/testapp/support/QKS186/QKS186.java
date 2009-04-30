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
package org.openfaces.testapp.support.QKS186;


import javax.faces.event.ActionEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * @author Darya Shumilina
 */
public class QKS186 {

    Logger logger = Logger.getLogger(QKS186.class.getName());

    private List<String> users;
    private List<String> listeMarches;
    private String selectedUser;

    public QKS186() {
    }

    public List getListeMarches() {
        if (selectedUser != null) {
            listeMarches = new ArrayList<String>();
            listeMarches.add("MARKET1");
            listeMarches.add("MARKET2");
            listeMarches.add("MARKET3");
        }
        return listeMarches;
    }

    public String getSelectedUser() {
        return "";
    }

    public List getUsers() {
        if (users == null) {
            users = new ArrayList<String>();
            users.add("USER1");
            users.add("USER2");
            users.add("USER3");
        }
        return users;
    }

    public void marcheSelected(ActionEvent event) {
        logger.info("It works !!!!!!!!!!!!!!!");
    }

    public void setListeMarches(List listeMarches) {
    }

    public void setSelectedMarche(Object selectedMarche) {
    }

    public void setSelectedUser(String aSelectedUser) {
        selectedUser = aSelectedUser;
    }

    public void userSelected(ActionEvent event) {
        selectedUser = "ASELECTEDUSER";
        logger.info("OK, it always worked fine   !!!!!!!!!!!!!!!");
    }

}