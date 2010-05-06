/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
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

import javax.faces.event.ActionEvent;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TaskBean implements Serializable {
    private List<Task> tasks;
    private String filter;
    private String newTaskName;

    public TaskBean() {
        initTasks();
    }

    private void initTasks() {
        tasks = new ArrayList<Task>();
        tasks.add(Task.createUncompletedTask("Buy a new Bentley"));
        tasks.add(Task.createUncompletedTask("Cleanup hard drive"));
        tasks.add(Task.createCompletedTask("Throw away television"));
        tasks.add(Task.createUncompletedTask("Become a billionaire"));
        tasks.add(Task.createUncompletedTask("Find a better task manager"));
    }

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

    public void addTask(ActionEvent event) {
        Task newTask = Task.createUncompletedTask(newTaskName);
        tasks.add(newTask);
        newTaskName = "";
    }

    public void deleteTask(ActionEvent event) {
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
        if (filter == null || "".equals(filter)) {
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
