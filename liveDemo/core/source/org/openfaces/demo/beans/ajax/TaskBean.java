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

package org.openfaces.demo.beans.ajax;

import org.openfaces.util.Faces;

import javax.faces.event.AjaxBehaviorEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static org.openfaces.demo.beans.ajax.Task.createCompletedTask;
import static org.openfaces.demo.beans.ajax.Task.createUncompletedTask;

public class TaskBean implements Serializable {

    private List<Task> tasks = new ArrayList<Task>() {{
        add(createUncompletedTask("Buy a new Bentley"));
        add(createUncompletedTask("Cleanup hard drive"));
        add(createCompletedTask("Throw away television"));
        add(createUncompletedTask("Become a billionaire"));
        add(createUncompletedTask("Find a better task manager"));
    }};

    private String filter;
    private String newTaskName;


    public String getFilter() {
        return filter;
    }

    public void setFilter(String filter) {
        this.filter = filter;
    }

    public String getNewTaskName() {
        return newTaskName;
    }

    public void setNewTaskName(String newTaskName) {
        this.newTaskName = newTaskName;
    }

    public void addTask(AjaxBehaviorEvent event) {
        Task newTask = createUncompletedTask(newTaskName);
        tasks.add(newTask);
        newTaskName = "";
    }

    public void deleteTask(AjaxBehaviorEvent event) {
        Task task = Faces.var("task", Task.class);
        tasks.remove(task);
    }

    public List<Task> getTasks() {
        return filterTasks(getUncompletedTasks(), filter);
    }

    public List<Task> getDoneTasks() {
        return filterTasks(getCompletedTasks(), filter);
    }

    private List<Task> getCompletedTasks() {
        List<Task> completedTasks = new ArrayList<Task>();
        for (Task task : tasks) {
            if (task.getCompleted()) {
                completedTasks.add(task);
            }
        }
        return completedTasks;
    }


    private List<Task> getUncompletedTasks() {
        List<Task> uncompletedTasks = new ArrayList<Task>();
        for (Task task : tasks) {
            if (!task.getCompleted()) {
                uncompletedTasks.add(task);
            }
        }
        return uncompletedTasks;
    }

    private List<Task> filterTasks(List<Task> tasks, String filter) {
        List<Task> filteredTasks = new ArrayList<Task>(tasks.size());
        if (filter == null || filter.isEmpty()) {
            filteredTasks.addAll(tasks);
        } else {
            for (Task task : tasks) {
                if (task.getName().toLowerCase().contains(filter.toLowerCase())) {
                    filteredTasks.add(task);
                }
            }
        }
        return filteredTasks;
    }

    public Boolean getRenderDoneList() {
        return !getDoneTasks().isEmpty();
    }
}
