/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

var clickAddTask = function() {
  O$("form:addTask").click();
};

function executeOnEnter(event, func) {
  if (event.keyCode == 13) {
    func();
  }
}

function showAddTaskButton() {
  O$("form:addTask").style.display = "block";
}

function hideAddTaskButton() {
  O$("form:addTask").style.display = "none";
}

function addTaskFocus() {
  showAddTaskButton();
}

function addTaskBlur() {
  if (O$("form:newTask").value == "") {
    hideAddTaskButton();
  }
}