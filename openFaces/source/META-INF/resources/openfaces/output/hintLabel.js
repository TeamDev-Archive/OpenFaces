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

O$.HintLabel = {
  _init: function(id, hintTimeout, hintClass, rolloverClass, customHintSpecified) {
    var label = O$.initComponent(id, {rollover: rolloverClass}, {
      _hint: O$(id + "::hint"),
      _hintTimeout: hintTimeout,
      _hintClass: hintClass,
      _customHintSpecified: customHintSpecified
    });

    O$.addLoadEvent(function() {
      if (label._hint) {
        var hints = [];
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

    var labelInnerHtml = O$.HintLabel._getInnerHtml(label);

    O$.addLoadEvent(function() {
      label._oldMouseOver = label.onmouseover;
      O$.assignEventHandlerField(label, "onmouseover", function (e) {
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
        if (!label._customHintSpecified) { // idea is: do not show hint if "hint" attribute is not specified text and label is not cut
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
      });

      label._oldMouseOut = label.onmouseout;
      O$.assignEventHandlerField(label, "onmouseout", function (e) {
        if (label._oldMouseOut)
          label._oldMouseOut(e);
        if (label._hintTimer)
          clearTimeout(label._hintTimer);
      });
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

    ["marginLeft", "marginRight", "paddingLeft", "paddingRight", "borderLeftWidth", "borderRightWidth", "fontFamily",
      "fontSize", "fontWeight"].forEach(function(property) {
      stubElt.style[property] = O$.getElementStyle(elt, property);
    });

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
    pos.x += O$.getNumericElementStyle(label, "border-left-width") + O$.getNumericElementStyle(label, "padding-left");
    pos.y += O$.getNumericElementStyle(label, "border-top-width") + O$.getNumericElementStyle(label, "padding-top");
    var hint = label._hint;
    pos.x -= O$.getNumericElementStyle(hint, "border-left-width") + O$.getNumericElementStyle(hint, "padding-left") + O$.getNumericElementStyle(hint, "margin-left");
    pos.y -= O$.getNumericElementStyle(hint, "border-top-width") + O$.getNumericElementStyle(hint, "padding-top") + O$.getNumericElementStyle(hint, "margin-top");

    hint.style.left = pos.x + "px";
    hint.style.top = pos.y + "px";
    hint.style.width = null;

    var baseZIndex = O$.getElementZIndex(label);
    if (baseZIndex)
      hint.style.zIndex = baseZIndex + 1;

    var hintLabelWidth = O$.getStyleClassProperty(label._hintClass, "width");

    var margins = 10;
    if (O$.isExplorer()) {
      margins = document.body.leftMargin * 1 + document.body.rightMargin * 1;
    }
    label._realPos = O$.getElementBorderRectangle(label, false);
    var scrollX = O$.getPageScrollPos().x;
    if (label._realPos.x + label._hintFullWidth > document.body.offsetWidth + scrollX) {
      //The line was commented out because the calculated width affected on width of custom styles
      if(hintLabelWidth!=undefined){
        hint.style.whiteSpace = "normal";
      }else{
        hint.style.whiteSpace = "normal";
        hint.style.width = (document.body.offsetWidth - label._realPos.x - margins + scrollX) + "px";
      }
    } else {
      if(hintLabelWidth!=undefined){
        hint.style.whiteSpace = "normal";
      }else{
        hint.style.whiteSpace = "nowrap";
        hint.style.width = label._hintFullWidth + "px";
      }
    }
  }

};