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

O$.mockInput = {
  _init:function (maskEdit, minSize, maxSize, symbolForMaskValue, symbolForBlank, realPositionInInput) {
    var mockInput = {
      _minSize:minSize,
      _maxSize:maxSize,
      _symbolForBlank:symbolForBlank,
      _realSize:0,
      _cursorPosition:0,
      _realPositionInInput:0,
      _value:[],
      _symbolForMaskValue:symbolForMaskValue,
      _parent:maskEdit
    };
    for (var i = 0; i < minSize; i++) {
      mockInput._value[i] = mockInput._symbolForBlank;
    }

    O$.extend(mockInput, {
              _cursorGoInMockInput:function (isLeftSide) {
                if (this._isNeedAdditionalSymbolInBlank()) {
                  this._addAdditionalSymbolInBlank();
                }

                if (!isLeftSide) {
                  var realMaxCursorPosition = null;
                  if (this._realSize < this._minSize) {
                    this._cursorPosition = this._value.length - 1;
                    this._selectMaskEditTextRangeFromMockInput(this._cursorPosition);
                  } else {
                    this._cursorPosition = this._realSize;
                    this._selectMaskEditTextRangeFromMockInput(this._cursorPosition);
                  }


                } else {
                  this._cursorPosition = 0;
                }
                this._realPositionInInput = this._parent._cursorPosition + this._parent._incWithCursorPos();
              },

              _isKeyPress:function (pressChar) {
                var realMaxCursorPosition = null;
                realMaxCursorPosition = this._realSize < this._minSize ? this._minSize : this._maxSize;
                if ((this._cursorPosition) < realMaxCursorPosition) {
                  if (this._parent._isValidChar(pressChar, this._symbolForMaskValue)) {
                    this._value[this._cursorPosition] = pressChar;

                    this._cursorPosition++;
                    this._realSize = this._value.length;
                    if (this._isNeedAdditionalSymbolInBlank()) {
                      this._addAdditionalSymbolInBlank();
                    }
                    this._parent._setNewDynamicMask();
                    this._selectMaskEditTextRangeFromMockInput(this._cursorPosition);
                    realMaxCursorPosition = this._realSize < this._minSize ? this._minSize : this._maxSize;
                    if (!(this._cursorPosition < realMaxCursorPosition)) {
                      if (this._isNeedAdditionalSymbolInBlank()) {
                        this._deleteAdditionalSymbolInBlank();
                      }
                      return true;
                    }
                  }

                }
                return false

              },

              _isKeyRight:function () {
                var realMaxCursorPosition = null;
                if (this._realSize < this._minSize) {
                  realMaxCursorPosition = this._minSize - 1;
                } else {
                  realMaxCursorPosition = this._realSize;
                }

                if (this._cursorPosition < realMaxCursorPosition) {
                  this._cursorPosition++;
                  this._selectMaskEditTextRangeFromMockInput(this._cursorPosition);
                  return false;
                }
                if (this._isNeedAdditionalSymbolInBlank()) {
                  this._deleteAdditionalSymbolInBlank();
                }
                return true;
              },

              _isKeyLeft:function () {
                if (this._cursorPosition > 0) {
                  this._cursorPosition--;
                  this._selectMaskEditTextRangeFromMockInput(this._cursorPosition);
                  return false
                }
                if (this._isNeedAdditionalSymbolInBlank()) {
                  this._deleteAdditionalSymbolInBlank();
                }
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
                this._value[this._value.length] = this._symbolForBlank;
                this._parent._setNewDynamicMask();
              },

              _deleteAdditionalSymbolInBlank:function () {
                this._value.push();
              },

              _returnMockValue:function () {
                return this._value;
              },

              _realCursorPosition:function () {
                return this._parent._cursorPosition;
              },

              _selectMaskEditTextRangeFromMockInput:function (size) {
                var realPositionInInput = this._realPositionInInput + size;
                this._parent._selectMaskEditTextRange(realPositionInInput, realPositionInInput)
              },

              _getIncWithCursorPos:function () {
                var realMaxCursorPosition = null;
                if (this._realSize < this._minSize) {
                  realMaxCursorPosition = this._minSize;
                } else {
                  realMaxCursorPosition = this._realSize;
                }

                return realMaxCursorPosition;
              }

            }
    )
    ;
    return mockInput;
  }
}
;
