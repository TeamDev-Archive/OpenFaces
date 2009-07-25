/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

O$.HintLabel = {
  _init: function(id, hintTimeout, hintClass, rolloverClass) {
    var label = O$(id);
    label._hint = O$(id + "::hint");
    label._hintTimeout = hintTimeout;
    label._hintClass = hintClass;

    O$.addLoadEvent(function() {
      if (label._hint) {
        var hints = new Array(document.getElementsByName(id + "::hint").length);
        for (var i = 0, count = hints.length; i < count; i++) {
          hints[i] = document.getElementsByName(id + "::hint")[i];
        }
        if (hints.length == 2) {
          var oldHint = hints[1];
          oldHint.parentNode.removeChild(oldHint);
        }

        label.parentNode.insertBefore(label._hint, label.nextSibling);
      }
    });

    O$.initComponent(id, {rollover: rolloverClass});

    var labelInnerHtml = O$.HintLabel._getInnerHtml(label);
    label._mouseOverHandler = function (e) {
      if (!label._hint) {
        var hintDiv = document.createElement("div");
        hintDiv.id = label.id + "::hint"; // required for tests
        hintDiv.innerHTML = labelInnerHtml;
        label.parentNode.appendChild(hintDiv);
        label._hint = hintDiv;
      }
      O$.HintLabel._initHint(label);

      if (label._oldMouseOver) {
        label._oldMouseOver(e);
      }
      if (labelInnerHtml == label._hint.innerHTML) { // idea is: do not show hint if hint text is the same as label text and label is not cutted
        if (!label._fullWidth) {
          label._fullWidth = O$.HintLabel._getElementWidth(label, true) * 1;
        }
        if (label._fullWidth <= label.offsetWidth && label._fullWidth <= label.parentNode.offsetWidth) return;
      }
      if (!label._hintFullWidth) {
        label._hintFullWidth = O$.HintLabel._getElementWidth(label._hint, false) * 1;
      }
      O$.HintLabel._prepareHint(label);
      label._hintTimer = setTimeout(function() {
        if (document._of_visible_hint) {
          document._of_visible_hint.style.visibility = "hidden";
        }
        document._of_visible_hint = label._hint;
        label._hint.style.visibility = "visible";
        label._hint.style.display = "block";
        O$.repaintAreaForOpera(label._hint, true);
        label._parentVisibilityChecker = setInterval(function() {
          var parent = label._hint.parentNode;
          while (parent) {
            if (parent.style && parent.style.visibility && (parent.style.visibility == "hidden" || parent.style.display == "none")) {
              label._hint._hide();
              return;
            }
            parent = parent.parentNode;
          }
        }, 200);
      }, label._hintTimeout);
    };

    O$.addLoadEvent(function() {
      label._oldMouseOver = label.onmouseover;
      O$.assignEventHandlerField(label, "onmouseover", label._mouseOverHandler);
    });


    label._mouseOutHandler = function (e) {
      if (label._oldMouseOut)
        label._oldMouseOut(e);
      if (label._hintTimer)
        clearTimeout(label._hintTimer);
    };

    O$.addLoadEvent(function() {
      label._oldMouseOut = label.onmouseout;
      O$.assignEventHandlerField(label, "onmouseout", label._mouseOutHandler);
    });
  },

  _getInnerHtml: function(label) {
    if (O$.isExplorer())
      return label.innerHTML;

    var searchString = label.innerHTML.toLowerCase();
    var scriptIndex = searchString.indexOf("<script");
    var scriptContainerIndex = searchString.lastIndexOf("<div", scriptIndex);
    return label.innerHTML.substring(0, scriptContainerIndex);
  },

  _getElementWidth: function(elt, extractScriptsFromInnerHtml) {
    var stubElt = document.createElement(elt.tagName);
    stubElt.innerHTML = (extractScriptsFromInnerHtml) ? O$.HintLabel._getInnerHtml(elt) : elt.innerHTML;
    stubElt.style.marginLeft = O$.getElementStyleProperty(elt, "margin-left");
    stubElt.style.marginRight = O$.getElementStyleProperty(elt, "margin-right");
    stubElt.style.paddingLeft = O$.getElementStyleProperty(elt, "padding-left");
    stubElt.style.paddingRight = O$.getElementStyleProperty(elt, "padding-right");
    stubElt.style.borderLeftWidth = O$.getElementStyleProperty(elt, "border-left-width");
    stubElt.style.borderRightWidth = O$.getElementStyleProperty(elt, "border-right-width");
    stubElt.style.fontFamily = O$.getElementStyleProperty(elt, "font-family");
    stubElt.style.fontSize = O$.getElementStyleProperty(elt, "font-size");
    stubElt.style.fontWeight = O$.getElementStyleProperty(elt, "font-weight");

    stubElt.style.position = "absolute";
    stubElt.style.visibility = "hidden";
    stubElt.style.left = "0px";
    stubElt.style.top = "0px";

    document.body.appendChild(stubElt);
    var calculatedWidth = stubElt.offsetWidth;
    document.body.removeChild(stubElt);

    return calculatedWidth;
  },

  _initHint: function(label) {
    var hint = label._hint;
    if (hint._initialized)
      return;
    hint._initialized = true;
    hint.className = O$.combineClassNames([label._hintClass,  label.className]);
    hint._hide = function() {
      hint.style.visibility = "hidden";
      if (label._parentVisibilityChecker)
        clearInterval(label._parentVisibilityChecker);
    };

    hint.onmouseout = function () {
      hint._hide();
    };

    hint.onmousemove = function (e) {
      var eventPoint = O$.getEventPoint(e);
      if (eventPoint.x > label._realPos.getMaxX() || eventPoint.y > label._realPos.getMaxY())
        this._hide();
    };
  },


  _prepareHint: function(label) {
    var pos = O$.getElementPos(label, true);
    pos.left += O$.getNumericStyleProperty(label, "border-left-width") + O$.getNumericStyleProperty(label, "padding-left");
    pos.top += O$.getNumericStyleProperty(label, "border-top-width") + O$.getNumericStyleProperty(label, "padding-top");
    var hint = label._hint;
    pos.left -= O$.getNumericStyleProperty(hint, "border-left-width") + O$.getNumericStyleProperty(hint, "padding-left") + O$.getNumericStyleProperty(hint, "margin-left");
    pos.top -= O$.getNumericStyleProperty(hint, "border-top-width") + O$.getNumericStyleProperty(hint, "padding-top") + O$.getNumericStyleProperty(hint, "margin-top");

    hint.style.left = pos.left + "px";
    hint.style.top = pos.top + "px";
    hint.style.width = null;

    var baseZIndex = O$.getElementZIndex(label);
    if (baseZIndex)
      hint.style.zIndex = baseZIndex + 1;

    var margins = 10;
    if (O$.isExplorer()) {
      margins = document.body.leftMargin * 1 + document.body.rightMargin * 1;
    }
    label._realPos = O$.getElementBorderRectangle(label, false);
    var scrollX = O$.getPageScrollPos().x;
    if (label._realPos.x + label._hintFullWidth > document.body.offsetWidth + scrollX) {
      hint.style.whiteSpace = "normal";
      hint.style.width = (document.body.offsetWidth - label._realPos.x - margins + scrollX) + "px";
    } else {
      hint.style.whiteSpace = "nowrap";
      //      if (hint.offsetHeight < label.offsetHeight) {
      //        hint.style.height = label.offsetHeight + "px";
      //      }
      hint.style.width = label._hintFullWidth + "px";
    }
  }

};