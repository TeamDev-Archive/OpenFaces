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

package org.openfaces.testapp.support.QKS376;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TreeTableItems implements Serializable {
    private static int messagesCreated;

    private Object id = "message" + messagesCreated++;
    private boolean checked;
    private String name;
    private List<TreeTableItems> replies;


    public TreeTableItems(boolean checked, String name) {
        this.checked = checked;
        this.name = name;
    }


    public TreeTableItems(boolean checked, String name, List<TreeTableItems> replies) {
        this.checked = checked;
        this.name = name;
        this.replies = replies != null ? new ArrayList<TreeTableItems>(replies) : null;
    }


    public List getReplies() {
        return replies;
    }

    public void setReplies(List<TreeTableItems> replies) {
        this.replies = replies;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TreeTableItems that = (TreeTableItems) o;
        if (!id.equals(that.id)) {
            return false;
        }
        return true;
    }

    public int hashCode() {
        return id.hashCode();
    }
}
