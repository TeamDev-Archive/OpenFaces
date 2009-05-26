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