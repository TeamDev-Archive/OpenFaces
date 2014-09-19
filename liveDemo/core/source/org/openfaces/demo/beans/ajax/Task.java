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

package org.openfaces.demo.beans.ajax;

import java.io.Serializable;

public class Task implements Serializable {
    private static Integer instanceCount = 0;

    public static Task createUncompletedTask(String name) {
        instanceCount++;
        return new Task(instanceCount, name);
    }

    public static Task createCompletedTask(String name) {
        instanceCount++;
        return new Task(instanceCount, name, Boolean.TRUE);
    }


    private Integer id;
    private String name;
    private Boolean completed;

    private Task(Integer id, String name) {
        this.id = id;
        this.name = name;
        this.completed = Boolean.FALSE;
    }

    private Task(Integer id, String name, Boolean completed) {
        this.id = id;
        this.name = name;
        this.completed = completed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }
}
