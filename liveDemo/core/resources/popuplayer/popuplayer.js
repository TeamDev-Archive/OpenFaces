var pageLoaded;

function hideAllLayers() {
  if (!pageLoaded) return;
  if (O$('popupDemo:bigCamus'))
    O$('popupDemo:bigCamus').hide();
  if (O$('popupDemo:bigHemingway'))
    O$('popupDemo:bigHemingway').hide();
  if (O$('popupDemo:bigMarquez'))
    O$('popupDemo:bigMarquez').hide();
  if (O$('popupDemo:bigBrodsky'))
    O$('popupDemo:bigBrodsky').hide();
}

document.onmousemove = hideAllLayers;

window.onload = function() {
  pageLoaded = true;
}