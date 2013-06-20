/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2013, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

O$.mockInput = function (maskEdit, minSize, maxSize, symbolForBlank, symbolForMaskValue,realPositionInInput) {

  this._minSize = minSize;
  this._maxSize = maxSize;
  this._symbolForBlank = symbolForBlank;
  this._realSize = 0;
  this._cursorPosition = 0;
  this._value = "";
  this._isNeedAdditionalSymbol = false;
  this._parent = maskEdit;
  this._realPositionInInput = realPositionInInput;
  for (var i = 0; i < minSize; i++) {
    this._value += this._symbolForBlank
  }

  O$.extend(this, {
    _cursorGoInMockInput:function (leftSide) {
      if (!leftSide) {
        if (this._isNeedAdditionalSymbolInBlank()) {
          this._addAdditionalSymbolInBlank();
          this._cursorPosition = this._realSize;

        }
      } else {
        this._cursorPosition = 0;
      }
    },

    _isKeyPress:function () {
      if (this._realSize < this._minSize) {
        var realMaxCursorPosition = this._minSize;
      } else {
        var realMaxCursorPosition = this._realSize;
      }
      if (i) {

      }
    },

    _isKeyRight:function () {
      var realMaxCursorPosition = this._realSize < this._minSize ? this._minSize : this._realSize;
      if ((this._cursorPosition + 1) <= realMaxCursorPosition) {
        this._cursorPosition++;
      } else
        return true;

      //mm//dd/yyyy///
    },

    _isKeyLeft:function () {
      if (this._cursorPosition != 0) {
        this._cursorPosition--;
      } else
        return true;
    },

    _isKeyDelete:function () {

    },

    _isKeyBackspace:function () {

    },

    _deleteSelectionText:function (startSelection, endSelection) {

    },

    _isNeedAdditionalSymbolInBlank:function () {
      return  !((this._maxSize == this._realSize) || (this._realSize < this._minSize));
    },

    _addAdditionalSymbolInBlank:function () {
      this._value += this._value += this._symbolFromMaskValue;
    },

    _deleteAdditionalSymbolInBlank:function () {
      this._value = this._value.substring(0, this._realSize);
    },

    _returnMockValue:function () {
      return this._value;
    }

  })
};
