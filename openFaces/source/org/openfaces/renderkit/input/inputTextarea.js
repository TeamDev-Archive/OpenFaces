/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
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
    var inputtextarea = O$(inputTextareaId);

    if (O$.isExplorer()){
      inputtextarea.style.overflowY = "visible";
    } else {
      inputtextarea.style.overflowY = "hidden";
      inputtextarea.style.resize = "none";
    }

    O$.addEventHandler(inputtextarea, "keyup", function() {
      inputtextarea.scrollTop = 0;
      while (inputtextarea.scrollHeight > inputtextarea.offsetHeight) {
        var rows = inputtextarea.getAttribute("rows");
        if (rows == null)
          rows = 2;
        inputtextarea.setAttribute("rows", parseInt(rows) + 1);
      }
    });


  }
};