/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2014, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

package org.openfaces.demo.beans.inputsecret;

import javax.faces.context.FacesContext;
import java.util.Map;

/**
 * @author Andre Shapovalov
 */
public class InputSecretBean {

    private String login;
    private String password;
    private String confirmPassword;
    private String message;

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

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getMessage() {
        return createMessage();
    }

    public void setMessage(String message) {
        this.message = message;
    }

    private String createMessage(){
        if (isDefined(password) && isDefined(confirmPassword) && isDefined(login)){
            if (password.equals(confirmPassword)){
                return "Hello, Dear " + login + "!";
            }
            return "Password does not match the confirm password.";
        }
        return "Please, fill in all fields.";
    }

    private boolean isDefined(String input){
        return !(input == null || input.isEmpty());
    }
}
