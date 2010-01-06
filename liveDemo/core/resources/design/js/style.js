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

function fixheight() {
  var ie6 = false;
  if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)) { //test for MSIE x.x;
    var ieversion = new Number(RegExp.$1) // capture x.x portion and store as a number
    if (ieversion < 7) {
      ie6 = true;
    }
  }

  if (O$("LeftPannel") && O$("Content")) {
    if (ie6) {
      if (O$("LeftPannel").offsetHeight > O$("Content").offsetHeight) {
        O$("Content").style.height = (O$("LeftPannel").offsetHeight - 2) + "px";
      }
    } else {
      O$("Content").style.minHeight = (O$("LeftPannel").offsetHeight - 2) + "px";
    }
  }

}

function setEventHandler(element, eventName, eventScript, useCapture) {
  if (element.addEventListener) {
    element.addEventListener(eventName, eventScript, !!useCapture);
  } else if (element.attachEvent) {
    element.attachEvent("on" + eventName, eventScript);
  }
}

setEventHandler(window, "load", fixheight);