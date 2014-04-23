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

package org.openfaces.demo.spring.beans.inputTextArea;

import org.springframework.context.annotation.Scope;

import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Named;
import java.io.Serializable;


@Named
@Scope("session")
public class InputTextareaBean implements Serializable {

    private boolean successfullySent;
    private String feedback;

    public boolean isSuccessfullySent() {
        return successfullySent;
    }

    public void setSuccessfullySent(boolean successfullySent) {
        this.successfullySent = successfullySent;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public void send(AjaxBehaviorEvent actionEvent) {
        if (feedback != null && !"".equals(feedback)) {
            this.successfullySent = true;
        }
    }
}
