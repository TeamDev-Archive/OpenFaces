/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

function printInfo(textToOutput, divID, add) {
  var empty = getControl(divID);
  addListItemInnerHTML(empty, textToOutput, add);
  var children = empty.childNodes;
  for (var i = 0; i < children.length; i++) {
    children[i].className = 'programmed';
  }
  empty.style.color = "green";
}

function addListItemInnerHTML(el, text, add) {
  if (add) {
    el.innerHTML += "<div class='programmed'></div>";
    el.innerHTML += "<div class='programmed'>" + text + "</div>";
  } else {
    //todo: delete commented code
    //    el.innerHTML = "<div class='programmed'></div>";
    el.innerHTML = "<div class='programmed'>" + text + "</div>";

    el.style.borderLeftcolor = '';
  }
}

function getControl(id) { // todo: replace usages with O$ and remove this function
  return document.getElementById(id);
}
