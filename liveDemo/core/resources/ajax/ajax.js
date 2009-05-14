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

function removeClassName(element, className) {
  if (element.className) {
    var arrList = element.className.split(' ');
    var classNameInLowerCase = className.toLowerCase();
    for (var i = 0; i < arrList.length; i++)
    {
      if (arrList[i].toLowerCase() == classNameInLowerCase)
      {
        arrList.splice(i, 1);
        i--;
      }
    }
    element.className = arrList.join(' ');
  }
}


function addClassName(element, className)
{
  if (element.className) {
    var arrList = element.className.split(' ');
    var classNameInLowerCase = className.toLowerCase();
    for (var i = 0; i < arrList.length; i++) {
      if (arrList[i].toLowerCase() == classNameInLowerCase) {
        arrList.splice(i, 1);
        i--;
      }
    }
    arrList[arrList.length] = className;
    element.className = arrList.join(' ');
  } else {
    element.className = className;
  }
}


function check(element) {
  var childnotes = element.childNodes;
  for (var i = 0; i < childnotes.length; i++) {
    if (childnotes[i].type == "checkbox") {
      var checkbox = childnotes[i];
      var checked = !checkbox.checked;
      checkbox.checked = checked;
      if (checked) {
        addClassName(element, "checked", true);
      } else {
        removeClassName(element, "checked");
      }
      O$.reloadComponents([ 'form:taskList', 'form:doneList', 'form:doneListCaption' ], {
        requestDelay: 500,
        immediate :true
      });
      break;
    }
  }
}

var clickAddTask = function() {
  document.getElementById('form:addTask').click();
};

function executeOnEnter(event, func) {
  if (event.keyCode == 13) {
    func();
  }
}

function showAddTaskButton() {
  document.getElementById('form:addTask').style.display = 'block';
}

function hideAddTaskButton() {
  document.getElementById('form:addTask').style.display = 'none';
}

function addTaskFocus() {
  showAddTaskButton();
}

function addTaskBlur() {
  if (document.getElementById("form:newTask").value == '') {
    hideAddTaskButton();
  }
}