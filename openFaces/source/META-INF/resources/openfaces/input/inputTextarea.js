/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

O$.InputTextarea = {
  
  _init: function (inputTextareaId) {
    var textArea = O$.initComponent(inputTextareaId);

    if (O$.isExplorer()) {
      textArea.style.overflowY = "visible";
    } else {
      textArea.style.overflowY = "hidden";
      textArea.style.resize = "none";
    }

    O$.addEventHandler(textArea, "keyup", function() {
      textArea.scrollTop = 0;
      if (textArea.scrollHeight > textArea.offsetHeight) {
        textArea.style.height = "auto";
      }
      while (textArea.scrollHeight > textArea.offsetHeight) {
        var rows = textArea.getAttribute("rows");
        if (rows == null)
          rows = 2;
        textArea.setAttribute("rows", parseInt(rows) + 1);
//        if (textArea.scrollHeight > textArea.offsetHeight) {
//          var additionalHeight = O$.getNumericElementStyle(textArea, "padding-top") + O$.getNumericElementStyle(textArea, "padding-bottom") +
//                  O$.getNumericElementStyle(textArea, "border-top-width") + O$.getNumericElementStyle(textArea, "border-bottom-width");
//          O$.runTransitionEffect(textArea, "height", textArea.scrollHeight + additionalHeight, 100);
//        }
      }
    });


  }
};