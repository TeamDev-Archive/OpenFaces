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

package org.openfaces.demo.beans.inputsecret;


import javax.faces.event.ActionEvent;
import javax.faces.event.AjaxBehaviorEvent;

public class InputSecretBean {

    private String login = "";
    private String password = "";
    private String result = "";


    public void signIn(ActionEvent e) {
        processResult();
    }

    public void signIn(AjaxBehaviorEvent e) {
        processResult();
    }

    public void processResult() {
        if (login.trim().isEmpty() || password.trim().isEmpty()) {
            result = "";
        } else {
            result = "Hello, " + login + "!";
        }
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }


    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
