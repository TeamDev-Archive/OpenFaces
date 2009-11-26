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

//DateChooser suffixes:
var buttonSuffix = '::button';
var todaySuffix = '--popup--calendar::today';
var noneSuffix = '--popup--calendar::none';
var calendarBodySuffix = '--popup--calendar::body';
var monthSuffix = "--popup--calendar--month";
var yearSuffix = "--popup--calendar--year";

function singleSelectionChanged(dataTableID, divID) { // todo: reimplement this with TableInspector instead of client-side script
  var elDataTable = document.getElementById(dataTableID);
  var tbody = elDataTable.childNodes[1];
  var childerNumber = tbody.childNodes.length;
  for (var i = 0; i < childerNumber; i++) {
    var currentRow = tbody.childNodes[i];
    if (currentRow.tagName && currentRow.tagName && currentRow.tagName.toLowerCase() == "tr") {
      if (currentRow.className && currentRow.className != "o_hiddenRow") // selection style is applied to <tr> or embedded <td> tags depending on browser and other conditions
        showSelectedIndex(i, divID);
      else {
        var firstRowChild = currentRow.childNodes[0];
        if (firstRowChild) {
          if (firstRowChild.className && firstRowChild.className.indexOf("o_class") != -1) {
            showSelectedIndex(i, divID);
            break;
          }
        }
      }
    }
  }
}

function showSelectedIndex(index, divID) {
  var empty = getControl(divID);
  listItemInnerHTML(empty, index);
  var children = empty.childNodes;
  for (var i = 0; i < children.length; i++) {
    children[i].className = "programmed";
  }
  empty.style.color = "green";
}

function listItemInnerHTML(el, text) {
  el.innerHTML = "<div class='programmed'>" + text + "</div>";
}

function multipleSelectionChanged(dataTableID, divID) {// todo: reimplement this with TableInspector instead of client-side script
  var elDataTable = getControl(dataTableID);
  var tbody = elDataTable.childNodes[1];
  var childerNumber = tbody.childNodes.length;
  var result = " ";
  for (var i = 0; i < childerNumber; i++) {
    var currentRow = tbody.childNodes[i];
    if (currentRow.tagName && currentRow.tagName.toLowerCase() == "tr") {
      if (currentRow.className && currentRow.className != "o_hiddenRow") // selection style is applied to <tr> or embedded <td> tags depending on browser and other conditions
        result += " " + i;
      else {
        var firstRowChild = currentRow.childNodes[0];
        if (firstRowChild) {
          if (firstRowChild.className) {
            result += " " + i;
          }
        }
      }
    }
  }
  showSelectedIndex(result, divID);
}

function printInfo(textToOutput, divID, add) {
  var empty = getControl(divID);
  addListItemInnerHTML(empty, textToOutput, add);
  var children = empty.childNodes;
  for (var i = 0; i < children.length; i++) {
    children[i].className = "programmed";
  }
  empty.style.color = "green";
}

function logJSEvents(textToOutput, divID) {
  printInfo(textToOutput, divID, true);
}

function addListItemInnerHTML(el, text, add) {
  if (add) {
    el.innerHTML += "<div class='programmed'></div>";
    el.innerHTML += "<div class='programmed'>" + text + "</div>";
  } else {
    el.innerHTML = "<div class='programmed'>" + text + "</div>";
    el.style.borderLeftcolor = '';
  }
}

function getControl(id) { // todo: replace usages with O$ and remove this function
  return document.getElementById(id);
}

function determineDateRange(calendarId, selectedClassName, divToPrint) {
  var calendar = getControl(calendarId + '::body');
  var calendarRows = calendar.childNodes;
  var dates = "";

  // loop starts with 1 because zero row is day names
  for (var i = 1; i < calendarRows.length; i++) {
    var row = calendarRows[i];
    var week = row.childNodes;
    for (var j = 0; j < week.length; j++) {
      var date = week[j].childNodes[0];
      var dateClass = date.className;
      if (dateClass.indexOf(selectedClassName) != -1)
        dates += date.textContent + " ";
    }
  }
  printInfo(dates, divToPrint, true);
}

/*to use this function define in DateChooser following: selectedDayStyle="color: red; background: yellow;"*/
function printDateChooserSelectedDate(dateChooserID, outputDiv) {
  var mousedownEvt = O$.createEvent('mousedown');
  getControl(dateChooserID + buttonSuffix).onmousedown(mousedownEvt);
  var dateChooser = getControl(dateChooserID + calendarBodySuffix);
  var weeks = dateChooser.childNodes;
  var date = 'null';

  for (var i = 1; i < weeks.length; i++) {
    var days = weeks[i].childNodes;
    for (var j = 0; j < days.length; j++) {
      var day = days[j].childNodes[0];
      var color = O$.getElementStyle(day, 'color');
      var bgColor = O$.getElementStyle(day, 'background-color');
      if ((color == '"red"' && bgColor == '"yellow"') || (color == 'rgb(255, 0, 0)' && bgColor == 'rgb(255, 255, 0)')) {
        var dayText;
        if (day.textContent.length == 1)
          dayText = '0' + day.textContent;
        else
          dayText = day.textContent;
        date = dayText + ' ' + getControl(dateChooserID + monthSuffix).textContent + ', ' + getControl(dateChooserID + yearSuffix).textContent;
        break;
      }
    }
  }
  printInfo(date, outputDiv, false);
  getControl(dateChooserID + buttonSuffix).onmousedown(mousedownEvt);
}

function printFirstDayOfWeek(elementId, outputDiv, isDateChooser) {
  if (isDateChooser) {
    var mousedownEvt = O$.createEvent('mousedown');
    getControl(elementId + buttonSuffix).onmousedown(mousedownEvt);
  }
  var calendarId;
  if (isDateChooser) {
    calendarId = elementId + calendarBodySuffix;
  } else {
    calendarId = elementId + "::body";
  }
  printInfo(getControl(calendarId).childNodes[0].childNodes[0].textContent, outputDiv, false);

}
