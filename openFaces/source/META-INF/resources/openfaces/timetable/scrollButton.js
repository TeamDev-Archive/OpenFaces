/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

O$.ScrollButton = {

  _init: function(scrollButtonId, scrollableElementId, direction ,buttonStyleParams) {
    var scrollButton = O$.initComponent(scrollButtonId, buttonStyleParams,
      {
        _direction: direction,
        _targetElement : O$(scrollableElementId)
      },
      {
        onmouseover: function(e) {
          if (this._targetElement.stopScrolling) {
            this._targetElement.stopScrolling = false;
            if (scrollButton._direction=="up")
              _scrollTarget(1);
            if (scrollButton._direction=="down")
              _scrollTarget(-1);
          }
        },
        onmouseout: function(e) {
          this._targetElement.stopScrolling = true;
        }
      }
    );
    scrollButton._targetElement.stopScrolling = true;
    var _scrollTarget = function(delta) {
      scrollButton._targetElement.scrollContent(delta);
      if (!scrollButton._targetElement.stopScrolling)
        setTimeout( function() { _scrollTarget(delta) }, 10 );
    }
  }




}