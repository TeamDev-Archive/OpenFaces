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

function setStyle(obj, style, value) {
  getRef(obj).style[style] = value;
}

function getRef(obj) {
  return (typeof obj == "string") ?
         document.getElementById(obj) : obj;
}

function setStyleClass(obj, name) {
  if (name == "mouseOverActiveDays")
    obj.style.background = "#888888";
  else
    obj.style.background = "#bbccdd";
  //  obj.className = name;
}
