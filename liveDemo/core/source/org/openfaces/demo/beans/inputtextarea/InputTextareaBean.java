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

package org.openfaces.demo.beans.inputtextarea;

public class InputTextareaBean {

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

    public String send() {
        if (feedback != null && !"".equals(feedback)) {
            this.successfullySent = true;
        }

        return "success";
    }
}
