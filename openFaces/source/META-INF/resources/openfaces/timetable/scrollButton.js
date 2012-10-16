/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
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
    _scrollTarget = function(delta) {
      //      console.log("scroll view = " +scrollButton._targetElement.stopScrolling);
        // _targetElement.scroll(delta);
        /*
         for (var i = 0; i<monthTable._expandedDayView.eventBlock.childNodes.length;i++){
         var position = O$.getNumericElementStyle(monthTable._expandedDayView.eventBlock.childNodes[i], "top");
         monthTable._expandedDayView.eventBlock.childNodes[i].style.top = (position-button._scrollDelta)+"px";
         } */
      if (direction=="up")
        scrollButton._targetElement.scrollContent(-delta);
      if (direction=="down")
        scrollButton._targetElement.scrollContent(delta);

      if (!scrollButton._targetElement.stopScrolling)
        setTimeout( function() { _scrollTarget(1) }, 10 );
    }
    var scrollButton = O$.initComponent(scrollButtonId, buttonStyleParams,
      {
        _direction: direction,
        _targetElement : O$(scrollableElementId)
      },
      {
        onmouseover: function(e) {
          this.className = buttonStyleParams.rolloverButtonClass;
          if (this._targetElement.stopScrolling) {
            this._targetElement.stopScrolling = false;
            _scrollTarget(1);
          }
        },
        onmouseout: function(e) {
          this._targetElement.stopScrolling = true;
          this.className = buttonStyleParams.buttonClass;
        }

      }
    );
    scrollButton._targetElement.stopScrolling = true;
  }




}