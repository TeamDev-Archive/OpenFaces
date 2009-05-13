window.onload = fixheight;

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
      document.getElementById('Content').style.height = (document.getElementById('LeftPannel').offsetHeight - 2) + 'px';
    } else {
      document.getElementById('Content').style.minHeight = (document.getElementById('LeftPannel').offsetHeight - 2) + 'px';
    }
  }
}