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

O$.FoldingPanel = {
  _init: function(controlId,
                  expanded,
                  direction,
                  rolloverClass,
                  contentPreloaded,
                  useAjax,
                  focusable,
                  focusedClass,
                  focusedContentClass,
                  focusedCaptionClass) {
    var fp = O$(controlId);

    fp._stateHolderId = controlId + "::state";
    fp._contentHolderId = controlId + "::content";
    fp._captionContentId = controlId + "::caption_content";
    fp._captionId = controlId + "::caption";
    fp._useAjax = useAjax;

    fp._contentLoaded = contentPreloaded || expanded;

    O$.initComponent(controlId, {rollover: rolloverClass});
    O$.FoldingPanel._processCaptionStyle(fp);

    fp._expanded = expanded;
    fp._direction = direction;

    fp.isExpanded = function() {
      return fp._expanded;
    };

    if (focusable) {
      O$.setupArtificialFocus(fp, focusedClass);
      var contentHolder = O$(fp._contentHolderId);
      var captionContent = O$(fp._captionId);

      fp._prevOnfocus = fp.onfocus;
      fp._prevOnBlur = fp.onblur;

      fp._prevOnKeyDown = fp.onkeypress;
      fp.onkeypress = function (evt) {
        var e = evt ? evt : window.event;
        if (fp._prevOnKeyDown)
          fp._prevOnKeyDown(e);
        var code = O$.isExplorer() || O$.isOpera() ? e.keyCode : e.charCode;
        switch (code) {
          case 32: // white space
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
      };

      fp.onfocus = function(e) {
        if (this._prevOnfocus)
          this._prevOnfocus(e);

        if (focusedContentClass) {
          O$.setStyleMappings(contentHolder, {focused: focusedContentClass});
        }
        if (focusedCaptionClass) {
          O$.setStyleMappings(captionContent, {focused: focusedCaptionClass});
        }
      };
      fp.onblur = function(e) {
        if (this._prevOnBlur)
          this._prevOnBlur(e);
        if (focusedContentClass) {
          O$.setStyleMappings(contentHolder, {focused: null});
        }
        if (focusedCaptionClass) {
          O$.setStyleMappings(captionContent, {focused: null});
        }
      };
    }

    fp.setExpanded = function(expanded) {
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
      if (this._expanded && !this._contentLoaded) {
        if (this._useAjax) {
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
    };

    fp.expand = function() {
      this.setExpanded(true);
    };

    fp.collapse = function() {
      this.setExpanded(false);
    };

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

