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