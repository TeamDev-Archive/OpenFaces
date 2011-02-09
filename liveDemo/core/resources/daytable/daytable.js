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

function updateLayout() {
  var dayTable = O$("form:dayTable");
  if (dayTable != null) {
    if (dayTable.updateLayout) {
      dayTable.updateLayout();
    } else {
      setTimeout(updateLayout, 1500);
    }
  }}

function addBorder(btn, r, g, b) {
  function formatColor(r, g, b) {
    function intTo2DigitHex(intValue) {
      var str = intValue.toString(16);
      if (str.length < 2)
        str = "0" + str;
      return str;
    }

    return "#" + intTo2DigitHex(r) + intTo2DigitHex(g) + intTo2DigitHex(b);
  }
  var lineStyle = "1px solid " + formatColor(r, g, b);
  btn.style.border = lineStyle;
  btn.style.border = lineStyle;
}

function removeBorder(btn) {
  btn.style.border = "0px none white";
}