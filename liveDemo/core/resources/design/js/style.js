function fixheight() {
  var ie6 = false;
  if (/MSIE (\d+\.\d+);/.test(navigator.userAgent)) { //test for MSIE x.x;
    var ieversion = new Number(RegExp.$1) // capture x.x portion and store as a number
    if (ieversion < 7) {
      ie6 = true;
    }
  }

  if (document.getElementById('LeftPannel') && document.getElementById('Content')) {
    if (ie6) {
      if (document.getElementById('LeftPannel').offsetHeight > document.getElementById('Content').offsetHeight) {
        document.getElementById('Content').style.height = (document.getElementById('LeftPannel').offsetHeight - 2) + 'px';
      }
    } else {
      document.getElementById('Content').style.minHeight = (document.getElementById('LeftPannel').offsetHeight - 2) + 'px';
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