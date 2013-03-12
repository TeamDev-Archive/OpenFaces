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

/**
 * Created with IntelliJ IDEA.
 * User: dmitry.kashcheiev
 * Date: 12/19/12
 * Time: 6:30 PM
 * To change this template use File | Settings | File Templates.
 */

O$.MaskEdit = {

  _init:function (inputId, mask, blank, maskSymbolArray, rolloverClass, focusedClass, disabled, dictionary) {
    var maskEdit = O$.initComponent(inputId, {}, {
              mask:mask,
              blank:blank,
              maskSymbolArray:maskSymbolArray,
              rolloverClass:rolloverClass,
              focusedClass:focusedClass,
              disabled:disabled,
              dictionary:dictionary,
              maskInputPositionCursor:0,
              value:blank,
              maskValue:[],
              primaryMaskValue:[],
              maskSeparatorPosition:[],
              maskInputPosition:[],
              isCursorIsSeparator:false,
              numeric:"1234567890",
              symbol:"`~,.?!@#$%^&*(){}[]"
            }


    );
    maskEdit.mask = "########";
    maskEdit.blank = "//DD///MM///YYYY///";
    maskEdit.maskSymbolArray = ["D", "M", "Y"];

    maskEdit.maskValue = [];
    maskEdit.primaryMaskValue = [];
    maskEdit.maskSeparatorPosition = [];
    maskEdit.maskInputPosition = [];
    maskEdit.dictionary = dictionary ? dictionary : "absdefghijklmnopqrstuwwxyz";

    for (var i = 0; i < maskEdit.blank.length; i++) {
      var IsMaskSymbol = false;
      for (var j in maskEdit.maskSymbolArray) {
        if (maskEdit.blank[i] == maskEdit.maskSymbolArray[j]) {
          IsMaskSymbol = true;
        }
      }

      if (!IsMaskSymbol) {
        maskEdit.maskSeparatorPosition.push(i)
      } else {
        maskEdit.maskInputPosition.push(i);
      }
      maskEdit.maskValue.push(maskEdit.blank[i]);
      maskEdit.primaryMaskValue.push(maskEdit.blank[i]);
    }

    maskEdit.isFinishPosition = maskEdit.maskInputPosition.length <= 1;// ??
    maskEdit.value = maskEdit.blank;
    maskEdit.cursorPosition = maskEdit.maskInputPosition[0];

    //TODO: look  O$._selectTextRange from util.js
    setCaretToPos(maskEdit, maskEdit.cursorPosition);
    function setSelectionRange(input, selectionStart, selectionEnd) {
      if (input.setSelectionRange) {
        input.focus();
        input.setSelectionRange(selectionStart, selectionEnd);
      }
      else if (input.createTextRange) {
        var range = input.createTextRange();
        range.collapse(true);
        range.moveEnd('character', selectionEnd);
        range.moveStart('character', selectionStart);
        range.select();
      }
    }

    //TODO: look  O$._selectTextRange from util.js
    function setCaretToPos(input, pos) {
      setSelectionRange(input, pos, pos);
    }

    O$.extend(maskEdit, {
              onkeypress:function (e) {
                if (this.isFinishPosition)
                  return false;

                e = e || window.event;
                if (e.ctrlKey || e.altKey || e.metaKey)
                  return;
                var pressChar = this._getChar(e || window.event);
                if (!pressChar)
                  return;

                if (this._isValidChar(pressChar)) {
                  this.maskValue[this.cursorPosition] = pressChar;
                  this.value = this._toStringMaskValue(this.maskValue);
                  if (this.maskInputPositionCursor != (this.maskInputPosition.length - 1)) {
                    this.maskInputPositionCursor++;
                    this.cursorPosition = this.maskInputPosition[this.maskInputPositionCursor];
                    this.setCaretToPos(this, this.cursorPosition);
                  } else {
                    this.cursorPosition++;
                    this.isFinishPosition = true;
                    this.setCaretToPos(this, this.cursorPosition);
                  }
                }
                return false;
              },

              _toStringMaskValue:function (maskValue) {
                return maskValue.toString().replace(/\,/g, "");
              },

              _getChar:function (event) {
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
              //TODO: hm there is function setSelectionRange(input, selectionStart, selectionEnd) above ?
              //yes, but if I call this function "setSelectionRange" , I override built function
              setCursorPosition:function (input, selectionStart, selectionEnd) {

                if (input.setSelectionRange) {
                  input.focus();
                  input.setSelectionRange(selectionStart, selectionEnd);
                }
                else if (input.createTextRange) {
                  var range = input.createTextRange();
                  range.collapse(true);
                  range.moveEnd('character', selectionEnd);
                  range.moveStart('character', selectionStart);
                  range.select();
                }
              },
              _isValidChar:function (pressChar) {
                var maskChar = this.mask[this.maskInputPositionCursor];
                switch (maskChar) {
                  case "l":
                    return this._isOccurrenceChar(pressChar, this.dictionary + this.dictionary.toUpperCase());
                    break;
                  case "L":
                    return this._isOccurrenceChar(pressChar, this.dictionary.toUpperCase());
                    break;
                  case "a":
                    return this._isOccurrenceChar(pressChar, this.dictionary + this.dictionary.toUpperCase() + this.numeric);
                    break;
                  case "A":
                    return this._isOccurrenceChar(pressChar, this.dictionary.toUpperCase() + this.numeric);
                  case "#":
                    return this._isOccurrenceChar(pressChar, this.numeric);
                    break;
                  case "~":
                    return this._isOccurrenceChar(pressChar, this.symbol);
                    break;
                  default:
                    return false;
                }
              },

              _isOccurrenceChar:function (symbol, symbolArray) {
                return symbolArray.indexOf(symbol) != -1;

              },

              isControlKey:function (key) {
                switch (key) {
                  case 8:
                    return this._isKeyBackspace();

                    break;
                  case 36:
                    return this._isKeyHome();
                    break;
                  case 35:
                    return this._isKeyEnd();
                    break;
                  case 37:
                    return this._isKeyLeft();
                    break;
                  case 39:
                    return this._isKeyRight();
                    break;
                  case 46:
                    return this._isKeyDelete();
                    break;
                  default:
                    return true;
                }
              },
              //TODO: hm there is function setSelectionRange(input, selectionStart, selectionEnd) above ?
              setCaretToPos:function (input, pos) {
                this.setCursorPosition(input, pos, pos);
              },

              onkeydown:function (e) {
                var key = e.keyCode;
                var isExit = this.isControlKey(key);
                console.log(this.cursorPosition);
                return isExit;
              },

              onkeyup:function (e) {
                return false;
              },

              getSelectionText:function () {
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

              onclick:function (event) {
                event = event || window.event;

                var clickCursorPosition = this.doGetCaretPosition(this);
                this._getCursorPosition(clickCursorPosition, true);
                console.log(this.cursorPosition);

              },

              doGetCaretPosition:function (ctrl) {
                var CaretPos = 0;
                if (document.selection) {
                  ctrl.focus();
                  var Sel = document.selection.createRange();
                  Sel.moveStart('character', -ctrl.value.length);
                  CaretPos = Sel.text.length;

                }
                else if (ctrl.selectionStart || ctrl.selectionStart == '0')
                  CaretPos = ctrl.selectionStart;

                return CaretPos;
              }
            }


    );
    O$.extend(maskEdit, {
              _isKeyDelete:function () {
                if (this.getSelectionText()) {
                  var clickCursorPosition = this.doGetCaretPosition(this);
                  for (var i = 0; i < this.getSelectionText().length - 1; i++) {
                    this.maskValue[clickCursorPosition + i] = this.primaryMaskValue[clickCursorPosition + i];

                  }
                  this.value = this.toStringMaskValue(this.maskValue);
                  this.setCaretToPos(this, this.cursorPosition);
                  return false;
                }
                if (this._getCursorPosition(this.cursorPosition + 1, false)) {
                  this.maskInputPosition[this.maskInputPositionCursor] = this.primaryMaskValue[this.cursorPosition];
                  this._getCursorPosition(this.cursorPosition + 1, false)
                }
                return false;
              },

              _isKeyRight:function () {
                this._getCursorPosition(this.cursorPosition + 1, false);
                this.setCaretToPos(this, this.cursorPosition);

                return false;
              },

              _isKeyLeft:function () {
                this._getCursorPosition(this.cursorPosition - 1, false);
                this.setCaretToPos(this, this.cursorPosition);

                return false;

              },

              _isKeyEnd:function () {
                this._getCursorPosition(this.maskInputPosition[this.maskInputPosition.length] + 1, false)
                this.setCaretToPos(this, this.cursorPosition);
              },

              _isKeyHome:function () {
                this._getCursorPosition(this.maskInputPosition[0], false)
                this.setCaretToPos(this, this.cursorPosition);
                return false;
              },

              _isKeyBackspace:function () {
                if (this.isFinishPosition) {
                  this.maskValue[ this.maskInputPosition[this.maskInputPositionCursor]] = "_";
                  this.value = this.toStringMaskValue(this.maskValue);
                  this.cursorPosition--;
                  this.setCaretToPos(this, this.cursorPosition);
                  this.isFinishPosition = false;
                  return false;
                }
                if (this.maskInputPositionCursor != 0) {
                  this.maskInputPositionCursor--;
                  this.maskValue[ this.maskInputPosition[this.maskInputPositionCursor]] = "_";
                  this.value = this.toStringMaskValue(this.maskValue);
                  this.cursorPosition = this.maskInputPosition[this.maskInputPositionCursor];
                  this.setCaretToPos(this, this.cursorPosition);

                }
                return false;
              },
              _getCursorPosition:function (allegedPosition, carecter) {
                var i;
                if (carecter) {
                  if ((allegedPosition < this.maskInputPosition[0]) ||
                          (allegedPosition > (this.maskInputPosition[this.maskInputPosition.length - 1] + 1))) {
                    this.setCaretToPos(this, this.cursorPosition);
                    event.stopPropagation ? event.stopPropagation() : (event.cancelBubble = true);
                    return;
                  }
                  for (i in this.maskInputPosition) {
                    if (allegedPosition == this.maskInputPosition[i]) {
                      this.cursorPosition = this.maskInputPosition[i];
                      this.isFinishPosition = false;
                      this.maskInputPositionCursor = i;
                      return;
                    }
                  }
                  if (allegedPosition == this.maskInputPosition[this.maskInputPosition.length - 1]) {
                    this.isFinishPosition = true;
                    this.cursorPosition = allegedPosition;
                    return;
                  }

                  this.isCursorIsSeparator = true;
                  this.cursorPosition = allegedPosition;

                }
                else {


                  if (allegedPosition < this.cursorPosition) {
                    if (this.maskInputPositionCursor == 0) return false;

                    for (i = this.maskInputPosition.length - 1; i >= 0; i--) {
                      if (this.maskInputPosition[i] == allegedPosition) {
                        this.maskInputPositionCursor = i;
                        this.cursorPosition = allegedPosition;
                        this.isFinishPosition = false;

                        return true;
                      }
                    }
                    if (this.isCursorIsSeparator) {
                      console.log(this.isCursorIsSeparator);
                      for (i = this.maskInputPosition.length - 2; i >= 0; i--) {
                        if ((this.maskInputPosition[i] < allegedPosition) && (this.maskInputPosition[i + 1] > allegedPosition)) {
                          this.maskInputPositionCursor = i;
                          this.cursorPosition = this.maskInputPosition[i];
                          this.isCursorIsSeparator = false;
                          return  false;
                        }

                      }
                    }

                    this.isFinishPosition = false;
                    this.maskInputPositionCursor--;
                    this.cursorPosition = this.maskInputPosition[this.maskInputPositionCursor];
                    return false;
                  } else {
                    if (this.isFinishPosition) return false;

                    for (i in this.maskInputPosition) {
                      if (this.maskInputPosition[i] == allegedPosition) {
                        this.maskInputPositionCursor = i;
                        this.cursorPosition = allegedPosition;

                        return true;
                      }
                    }
                    if (allegedPosition == this.maskInputPosition[this.maskInputPosition.length - 1] + 1) {
                      this.isFinishPosition = true;
                      this.cursorPosition = allegedPosition;
                      return false;
                    }
                    if (this.maskInputPositionCursor == this.maskInputPosition.length - 1) {
                      this.isFinishPosition = true;
                      this.cursorPosition++;
                      return false;
                    }
                    this.maskInputPositionCursor++;
                    this.cursorPosition = this.maskInputPosition[this.maskInputPositionCursor];
                    return false;

                  }
                }
              }

            }
    )
    ;
  }
}
;

