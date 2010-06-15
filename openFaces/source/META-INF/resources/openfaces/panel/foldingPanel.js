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

O$.FoldingPanel = {
  _init: function(controlId,
                  expanded,
                  direction,
                  rolloverClass,
                  loadingMode,
                  focusable,
                  focusedClass,
                  focusedContentClass,
                  focusedCaptionClass) {
    var fp = O$.initComponent(controlId, {rollover: rolloverClass}, {
      _stateHolderId: controlId + "::state",
      _contentHolderId: controlId + "::content",
      _captionContentId: controlId + "::caption_content",
      _captionId: controlId + "::caption",

      _contentLoaded: loadingMode == "client" || expanded,
      _expanded: expanded,
      _direction: direction,

      isExpanded: function() {
        return fp._expanded;
      },

      setExpanded: function(expanded) {
        if (this._expanded == expanded)
          return;
        this._expanded = expanded;
        var stateHolder = O$(this._stateHolderId);
        stateHolder.value = expanded;
        var switchers = fp._expansionToggleButtons;
        for (var i = 0, count = switchers ? switchers.length : 0; i < count; i++) {
          var switcher = switchers[i];
          if (switcher._toggled != expanded) {
            switcher._toggled = expanded;
            switcher._updateImage();
            O$._notifyToggleStateChange(switcher);
          }
        }

        var contentHolder = O$(this._contentHolderId);
        contentHolder.style.display = this._expanded ? "block" : "none";
        if (this._expanded && (!this._contentLoaded || loadingMode == "ajaxAlways")) {
          if (loadingMode != "server") {
            O$.requestComponentPortions(this.id, ["content"], null, O$.FoldingPanel._ajaxResponseProcessor);
          } else {
            O$.submitEnclosingForm(this);
          }
          return;
        }
        if (this._direction == "left" || this._direction == "right") {
          var captionContent = O$(this._captionContentId);
          captionContent.style.display = this._expanded ? "block" : "none";
        }
        if (O$.isOpera()) {
          var body = document.getElementsByTagName("body")[0];
          body.style.visibility = "hidden";
          body.style.visibility = "visible";
        }
      },

      expand: function() {
        this.setExpanded(true);
      },

      collapse: function() {
        this.setExpanded(false);
      }
    });

    O$.FoldingPanel._processCaptionStyle(fp);

    if (focusable) {
      O$.setupArtificialFocus(fp, focusedClass);
      var contentHolder = O$(fp._contentHolderId);
      var captionContent = O$(fp._captionId);

      O$.extend(fp, {
        _prevOnfocus: fp.onfocus,
        _prevOnBlur: fp.onblur,

        _prevOnKeyDown: fp.onkeypress,
        onkeypress: function (evt) {
          var e = evt ? evt : window.event;
          if (fp._prevOnKeyDown)
            fp._prevOnKeyDown(e);
          var code = O$.isExplorer() || O$.isOpera() ? e.keyCode : e.charCode;
          switch (code) {
            case 32: // spacebar
              if (fp._expanded)
                fp.collapse();
              else
                fp.expand();
              break;
            case 43:  // +
              fp.expand();
              if (O$.isOpera())
                return false;
              break;
            case 45: // -
              fp.collapse();
              if (O$.isOpera())
                return false;
              break;
          }
        },

        onfocus: function(e) {
          if (this._prevOnfocus)
            this._prevOnfocus(e);

          if (focusedContentClass) {
            O$.setStyleMappings(contentHolder, {focused: focusedContentClass});
          }
          if (focusedCaptionClass) {
            O$.setStyleMappings(captionContent, {focused: focusedCaptionClass});
          }
        },
        onblur: function(e) {
          if (this._prevOnBlur)
            this._prevOnBlur(e);
          if (focusedContentClass) {
            O$.setStyleMappings(contentHolder, {focused: null});
          }
          if (focusedCaptionClass) {
            O$.setStyleMappings(captionContent, {focused: null});
          }
        }
      });
    }

  },

  _ajaxResponseProcessor: function(fp, portionName, portionHTML, portionScripts) {
    var oldComponent, prnt, tempDiv, newControl, oldId;
    if (portionName == "content") {
      oldComponent = O$(fp._contentHolderId);
      prnt = oldComponent.parentNode;
      tempDiv = document.createElement("div");
      tempDiv.innerHTML = portionHTML;
      newControl = tempDiv.childNodes[0];
      oldId = oldComponent.id;
      prnt.replaceChild(newControl, oldComponent);
      newControl.id = oldId;
    }
    O$.executeScripts(portionScripts);
    fp._contentLoaded = true;
  },

  _processCaptionStyle: function(fp) {
    if (!fp._caption)
      return;
    var paddings = O$.getElementStyle(fp._caption, ["padding-left", "padding-right", "padding-top", "padding-bottom"]);
    fp._captionContent.style.paddingLeft = paddings.paddingLeft;
    fp._captionContent.style.paddingRight = paddings.paddingRight;
    fp._captionContent.style.paddingTop = paddings.paddingTop;
    fp._captionContent.style.paddingBottom = paddings.paddingBottom;
    fp._caption.style.paddingLeft = fp._caption.style.paddingRight =
                                     fp._caption.style.paddingTop = fp._caption.style.paddingBottom = "0";
  }
};

