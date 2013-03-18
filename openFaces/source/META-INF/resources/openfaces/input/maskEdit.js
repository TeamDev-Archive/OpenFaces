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

//TODO: Common: check if user able to specify his own пress etc event from JSF, (addEventListener)
//TODO: browser compatibility issues

// I mark methods which i actually like now with the comment :OK:
O$.MaskEdit = {

  _init:function (inputId, mask, blank, maskSymbolArray, rolloverClass, focusedClass, disabled, dictionary) {
    var maskEdit = O$.initComponent(inputId,
            {_rolloverClass:rolloverClass,
              _focusedClass:focusedClass
            },
            { _mask:mask,
              _blank:blank,
              _maskSymbolArray:maskSymbolArray,
              _focusedClass:focusedClass,
              _disabled:disabled,
              _dictionary:dictionary,
              _maskInputCursorPosition:0,
              _value:blank,
              _maskValue:[],
              _primaryMaskValue:[],
              _maskSeparatorPosition:[],
              _maskInputPosition:[],
              _isCursorInSeparator:false,
              _numeric:"1234567890",
              _symbol:"`~,.?!@#$%^&*(){}[]"
            }


    );
    maskEdit._mask = "########";
    maskEdit._blank = "//DD///MM///YYYY///";
    maskEdit._maskSymbolArray = ["D", "M", "Y"];
    maskEdit._dictionary = dictionary ? dictionary : "absdefghijklmnopqrstuwwxyz";

    for (var i = 0; i < maskEdit._blank.length; i++) {
      var IsMaskSymbol = false;
      for (var j in maskEdit._maskSymbolArray) { // TODO: see array.contains(obj) from JS and connect this with if (!IsMaskSymbol), remove !IsMaskSymbol at all
        if (maskEdit._blank[i] == maskEdit._maskSymbolArray[j]) {
          IsMaskSymbol = true;
        }
      }

      if (!IsMaskSymbol) {
        maskEdit._maskSeparatorPosition.push(i)
      } else {
        maskEdit._maskInputPosition.push(i);
      }
      maskEdit._maskValue.push(maskEdit._blank[i]);
      maskEdit._primaryMaskValue.push(maskEdit._blank[i]);
    }
    maskEdit._isFinishPosition = maskEdit._maskInputPosition.length <= 1;
    maskEdit.value = maskEdit._blank;
    maskEdit._cursorPosition = maskEdit._maskInputPosition[0];
    O$._selectTextRange(maskEdit, maskEdit._cursorPosition, maskEdit._cursorPosition);

    O$.extend(maskEdit, {
              onkeypress:function (e) { // :OK:
                if (this._isFinishPosition)
                  return false;

                e = e || window.event;
                if (e.ctrlKey || e.altKey || e.metaKey)
                  return;
                var pressChar = this._getChar(e || window.event);
                if (!pressChar)
                  return;

                if (this._isValidChar(pressChar)) {
                  this._maskValue[this._cursorPosition] = pressChar;
                  this.value = this._toStringMaskValue(this._maskValue);
                  if (this._maskInputCursorPosition != (this._maskInputPosition.length - 1)) {
                    this._maskInputCursorPosition++;
                    this._cursorPosition = this._maskInputPosition[this._maskInputCursorPosition];
                    O$._selectTextRange(this, this._cursorPosition, this._cursorPosition);
                  } else {
                    this._cursorPosition++;
                    this._isFinishPosition = true;
                    O$._selectTextRange(this, this._cursorPosition, this._cursorPosition);
                  }
                }
                return false;
              },

              _toStringMaskValue:function (maskValue) { // :OK:
                return maskValue.toString().replace(/\,/g, "");
              },

              _getChar:function (event) { // :OK:
                if (event.which == null) {
                  if (event.keyCode < 32) return null;
                  return String.fromCharCode(event.keyCode)
                }
                if (event.which != 0 && event.charCode != 0) {
                  if (event.which < 32) return null;
                  return String.fromCharCode(event.which);
                }
                return null;
              },
              _isOccurrenceChar:function (symbol, symbolArray) { // :OK:
                return symbolArray.indexOf(symbol) != -1;

              },

              isControlKey:function (key) {
                switch (key) {
                  case 8:
                    return this._isKeyBackspace();
                  case 36:
                    return this._isKeyHome();
                  case 35:
                    return this._isKeyEnd();
                  case 37:
                    return this._isKeyLeft();
                  case 39:
                    return this._isKeyRight();
                  case 46:
                    return this._isKeyDelete();
                  default:
                    return true;
                }
              },


              onkeydown:function (e) { // :OK:
                var key = e.keyCode;
                var isExit = this.isControlKey(key);
                console.log(this._cursorPosition);
                return isExit;
              },

              onkeyup:function (e) {  // :OK:
                return false;
              },

              getSelectionText:function () { // :OK:
                var txt;
                if (window.getSelection) {
                  txt = window.getSelection().toString();
                } else if (document.getSelection) {
                  txt = document.getSelection();
                } else if (document.selection) {
                  txt = document.selection.createRange().text;
                }
                return txt;
              },

              onclick:function (event) { // :OK:
                event = event || window.event;

                var clickCursorPosition = O$._getCaretPosition(this);
                this._setCursorPosition(clickCursorPosition, true);
                console.log(this._cursorPosition);
              },

              _isValidChar:function (pressChar) {
                var maskChar = this._mask[this._maskInputCursorPosition];
                switch (maskChar) {
                  case "l":
                    return this._isOccurrenceChar(pressChar, this._dictionary + this._dictionary.toUpperCase());
                  case "L":
                    return this._isOccurrenceChar(pressChar, this._dictionary.toUpperCase());
                  case "a":
                    return this._isOccurrenceChar(pressChar, this._dictionary + this._dictionary.toUpperCase() + this._numeric);
                  case "A":
                    return this._isOccurrenceChar(pressChar, this._dictionary.toUpperCase() + this._numeric);
                  case "#":
                    return this._isOccurrenceChar(pressChar, this._numeric);
                  case "~":
                    return this._isOccurrenceChar(pressChar, this._symbol);
                  default:
                    return false;
                }
              }
            }
    );
    O$.extend(maskEdit, {
              _isKeyDelete:function () {
                if (this._deleteSelectionText()) return false;
                if (this._setCursorPosition(this._cursorPosition + 1, false)) {
                  var oldInputPosition;
                  if (this._isFinishPosition) {
                    oldInputPosition = this._maskInputPosition[this._maskInputCursorPosition];
                  } else {
                    oldInputPosition = this._maskInputPosition[this._maskInputCursorPosition - 1];
                  }
                  this.__deleteAndChangeValue(oldInputPosition);
                  O$._selectTextRange(this, this._cursorPosition, this._cursorPosition);
                }
                return false;
              },

              _isKeyRight:function () { // :OK:
                this._setCursorPosition(this._cursorPosition + 1, false);
                O$._selectTextRange(this, this._cursorPosition, this._cursorPosition);

                return false;
              },

              _isKeyLeft:function () { // :OK:
                this._setCursorPosition(this._cursorPosition - 1, false);
                O$._selectTextRange(this, this._cursorPosition, this._cursorPosition);

                return false;

              },

              _isKeyEnd:function () { // :OK:
                this._setCursorPosition(this._maskInputPosition[this._maskInputPosition.length] + 1, false)
                O$._selectTextRange(this, this._cursorPosition, this._cursorPosition);
              },

              _isKeyHome:function () { // :OK:
                this._setCursorPosition(this._maskInputPosition[0], false)
                O$._selectTextRange(this, this._cursorPosition, this._cursorPosition);
                return false;
              },

              _isKeyBackspace:function () {
                if (this._deleteSelectionText()) return false;
                if (this._setCursorPosition(this._cursorPosition - 1, false)) {
                  var oldInputPosition = this._maskInputPosition[this._maskInputCursorPosition];
                  this.__deleteAndChangeValue(oldInputPosition);
                  O$._selectTextRange(this, this._cursorPosition, this._cursorPosition);
                }
                return false;

              },
              __deleteAndChangeValue:function (oldInputPosition) {
                this._maskValue[oldInputPosition]
                        = this._primaryMaskValue[oldInputPosition];
                this.value = this._toStringMaskValue(this._maskValue);

              },
              _deleteSelectionText:function () {
                if (this.getSelectionText()) {
                  var clickCursorPosition = O$._getCaretPosition(this);
                  for (var i = 0; i < this.getSelectionText().length - 1; i++) {
                    this._maskValue[clickCursorPosition + i] = this._primaryMaskValue[clickCursorPosition + i];

                  }
                  this.value = this._toStringMaskValue(this._maskValue);
                  O$._selectTextRange(this, this._cursorPosition, this._cursorPosition);
                  return true;
                }
              },

              getValue:function () {
                return this._value
              },

              setValue:function (newValue) {
                return this._valueMaskValidator(newValue) || this._valueBlankValidator(newValue);
              },

              getMask:function () {
                return this._mask
              },
              setMask:function (newMask) {
                this._mask = newMask;
              },

              getBlank:function () {
                return this._blank
              },

              setBlank:function (newBlank) {
                this._blank = newBlank;
              },

              _valueMaskValidator:function (mask) {
                if (!(mask.length == this._mask.length )) {
                  var bufMaskCursorPosition = this._maskInputCursorPosition;

                  for (var i = 0; i < this._mask.length - 1; i++) {
                    this._maskInputCursorPosition = i;
                    if (this._isOccurrenceChar(mask[i], this.symbol)) {
                      this._maskInputCursorPosition = bufMaskCursorPosition;
                      return false
                    }
                  }
                } else return false;
                return true;
              },

              _valueBlankValidator:function (blank) {
                if (!(blank.length == this._blank.length )) {
                  var bufMaskCursorPosition = this._maskInputCursorPosition;

                  for (var i = 0; i < this._blank.length - 1; i++) {
                    this._maskInputCursorPosition = i;
                    if (this._isOccurrenceChar(blank[i], this.symbol)) {
                      this._maskInputCursorPosition = bufMaskCursorPosition;
                      return false
                    }
                  }
                } else return false;
                return true;
              },


              ///////////////////////////////////////////////////////////////////
              //////////////////////////////////////////////////////////////////////
              /////////////////////////////////////////////////////////////
              _setCursorPosition:function (allegedPosition, carecter) {

                if (carecter) {
                  if ((allegedPosition < this._maskInputPosition[0]) ||
                          (allegedPosition > (this._maskInputPosition[this._maskInputPosition.length - 1] + 1))) {
                    O$._selectTextRange(this, this._cursorPosition, this._cursorPosition); // TODO: actually right we've decided to avoid setting of caret pos inside this method
                    event.stopPropagation ? event.stopPropagation() : (event.cancelBubble = true);
                    return;
                  }
                  for (var i in this._maskInputPosition) {
                    if (allegedPosition == this._maskInputPosition[i]) {
                      this._cursorPosition = this._maskInputPosition[i];
                      this._isFinishPosition = false;
                      this._maskInputCursorPosition = i;
                      return;
                    }
                  }
                  if (allegedPosition == this._maskInputPosition[this._maskInputPosition.length - 1] + 1) {
                    this._isFinishPosition = true;
                    this._cursorPosition = allegedPosition;
                    console.log("finish");
                    return;
                  }

                  this._isCursorInSeparator = true;
                  this._cursorPosition = allegedPosition;

                }
                else { //TODO: maybe there is a sense to put both this parts to different functions for carecter=true nad carecter=false


                  if (allegedPosition < this._cursorPosition) {
                    if (this._isCursorInSeparator) {
                      console.log(this._isCursorInSeparator);
                      for (var i = this._maskInputPosition.length - 2; i >= 0; i--) {
                        if ((this._maskInputPosition[i] < allegedPosition) && (this._maskInputPosition[i + 1] > allegedPosition)) {
                          this._maskInputCursorPosition = i;
                          this._cursorPosition = this._maskInputPosition[i];
                          this._isCursorInSeparator = false;
                          return  true;
                        }
                      }
                    }

                    //TODO: why: 'this.isFinishPosition == false' but not smt like this '!this.isFinishPosition'
                    //TODO: remove unneeded '(', ')'
                    //TODO: in such cases it's better to make:
                    /*   if (this.maskInputPositionCursor == 0){
                     ......
                     if (this.isFinishPosition ){
                     .....
                     .....
                     }
                     return false;
                     }
                     *TODO: because for any situation when you have this.maskInputPositionCursor == 0 you will return false as i understand
                     * */
                    if ((this._maskInputCursorPosition == 0) && (this._isFinishPosition == false)) return false;

                    if ((this._maskInputCursorPosition == 0) && (this._isFinishPosition == true)) {
                      this._maskInputCursorPosition = this._maskInputPosition.length - 1;
                      this._isFinishPosition = false;
                      this._cursorPosition = this._maskInputPosition[this._maskInputCursorPosition];
                      return true;
                    }

                    for (i = this._maskInputPosition.length - 1; i >= 0; i--) {
                      if (this._maskInputPosition[i] == allegedPosition) {
                        this._maskInputCursorPosition = i;
                        this._cursorPosition = allegedPosition;
                        this._isFinishPosition = false;
                        return true;
                      }
                    }


                    this._isFinishPosition = false;
                    this._maskInputCursorPosition--;
                    this._cursorPosition = this._maskInputPosition[this._maskInputCursorPosition];
                    //TODO: in common try to callect all this logic to some tree structure of Ifs with not so much return methods
                    return true;

                  } else {
                    //TODO: in common try to collect all this logic to some tree structure of Ifs with not so much return methods
                    if (this._isCursorInSeparator) {
                      console.log(this._isCursorInSeparator);
                      for (i = 1; i < this._maskInputPosition.length - 1; i++) {
                        if ((this._maskInputPosition[i - 1] < allegedPosition) && (this._maskInputPosition[i ] > allegedPosition)) {
                          this._maskInputCursorPosition = i;
                          this._cursorPosition = this._maskInputPosition[i];
                          this._isCursorInSeparator = false;
                          this._isFinishPosition = false;
                          return  true;
                        }
                      }
                    }
                    if (this._isFinishPosition) return false;

                    for (i in this._maskInputPosition) {
                      if (this._maskInputPosition[i] == allegedPosition) {
                        this._maskInputCursorPosition = i;
                        this._cursorPosition = allegedPosition;

                        return true;
                      }
                    }
                    //TODO: in common try to collect all this logic to some tree structure of Ifs with not so much return methods
                    if (allegedPosition == this._maskInputPosition[this._maskInputPosition.length - 1] + 1) {
                      this._isFinishPosition = true;
                      this._cursorPosition = allegedPosition;
                      return true;
                    }
                    if (this._maskInputCursorPosition == this._maskInputPosition.length - 1) {
                      this._isFinishPosition = true;
                      this._cursorPosition++;
                      return true;
                    }
                    this._maskInputCursorPosition++;
                    this._cursorPosition = this._maskInputPosition[this._maskInputCursorPosition];
                    return true;

                  }
                }
              }

            }
    )
    ;
  }
}
;

