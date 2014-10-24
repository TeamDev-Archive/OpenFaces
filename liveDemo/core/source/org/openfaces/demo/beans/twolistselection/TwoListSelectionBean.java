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

package org.openfaces.demo.beans.twolistselection;

import javax.faces.model.SelectItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TwoListSelectionBean implements Serializable {
    private List<SelectItem> tasks = new ArrayList<SelectItem>();
    private List<Task> internalTasks = new ArrayList<Task>();
    private String[] selectedTasks = {};
    private List<Task> displayableTasks;

    public TwoListSelectionBean() {
        internalTasks.add(new Task("1", "Task Name 1", "Task Description 1"));
        internalTasks.add(new Task("2", "Task Name 2", "Task Description 2"));
        internalTasks.add(new Task("3", "Task Name 3", "Task Description 3"));
        internalTasks.add(new Task("4", "Task Name 4", "Task Description 4"));
        internalTasks.add(new Task("5", "Task Name 5", "Task Description 5"));
        internalTasks.add(new Task("6", "Task Name 6", "Task Description 6"));

        for (Task task : internalTasks) {
            tasks.add(new SelectItem(task.getId(), task.getTaskName(), task.getTaskDescription()));
        }

        selectedTasks = new String[]{"2", "5"};
        updateDisplayable();
    }

    public List<SelectItem> getTasks() {
        return tasks;
    }

    public String[] getSelectedTasks() {
        return selectedTasks;
    }

    public void setSelectedTasks(String[] selectedTasks) {
        this.selectedTasks = selectedTasks;
        updateDisplayable();
    }

    private void updateDisplayable() {
        displayableTasks = new ArrayList<Task>();
        if (selectedTasks != null && selectedTasks.length > 0) {
            for (String selectedTask : selectedTasks) {
                int id = Integer.parseInt(selectedTask);
                displayableTasks.add(internalTasks.get(id - 1));
            }
        }
    }

    public List<Task> getDisplayableTasks() {
        return displayableTasks;
    }
}
