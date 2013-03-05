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
  _init:function (inputId, mask, maskSeparator, rolloverClass, focusedClass, disabled, dictionary) {

    var maskEdit = O$(inputId);
    maskEdit.mask = "#########";
    maskEdit.maskSeparator = "//___///___///__///";
    maskEdit.maskValue = new Array();
    maskEdit.primaryMaskValue = new Array();
    maskEdit.maskSeparatorLenght = maskEdit.maskSeparator.length;
    maskEdit.maskSeparatorPosition = new Array();
    maskEdit.maskInputPosition = new Array();
    if (dictionary == null) {
      maskEdit.dictionary = "absdefghijklmnopqrstuwwxyz"
    } else {
      maskEdit.dictionary = dictionary;
    }
    for (var i = 0; i < maskEdit.maskSeparator.length; i++) {
      if (maskEdit.maskSeparator[i] != "_") {
        maskEdit.maskSeparatorPosition.push(i)
      } else {
        maskEdit.maskInputPosition.push(i);

      }
      maskEdit.maskValue.push(maskEdit.maskSeparator[i]);
      maskEdit.primaryMaskValue.push(maskEdit.maskSeparator[i]);
    }
    maskEdit.isFinishPosition = maskEdit.maskInputPosition.length <= 1;
    maskEdit.isCursorIsSeparator = false;

    maskEdit.maskInputPositionCursor = 0;
    maskEdit.value = maskEdit.maskSeparator;
    maskEdit.cursorPosition = maskEdit.maskInputPosition[0];

    maskEdit.numeric = "1234567890";
    maskEdit.symbol = "`~,.?!@#$%^&*(){}[]";
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
                var pressChar = this.getChar(e || window.event);
                if (!pressChar)
                  return;

                if (this._isValidChar(pressChar)) {
                  this.maskValue[this.cursorPosition] = pressChar;
                  this.value = this.toStringMaskValue(this.maskValue);
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
              toStringMaskValue:function (maskValue) {
                return maskValue.toString().replace(/\,/g, "");
              },
              getChar:function (event) {
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
                    return this.isKeyBackspace();
                    break;
                  case 36:
                    return this.isKeyHome();
                    break;
                  case 35:
                    return this.isKeyEnd();
                    break;
                  case 37:
                    return this.isKeyLeft();
                    break;
                  case 39:
                    return this.isKeyRight();
                    break;
                  case 46:
                    return this.isKeyDelete();
                    break;
                  default:
                    return true;
                }
              },

              setCaretToPos:function (input, pos) {
                this.setCursorPosition(input, pos, pos);
              },

              onkeydown:function (e) {
                var key = e.keyCode;
                return  this.isControlKey(key);
              },

              onkeyup:function (e) {
                return false;
              },

              getSelectionText:function () {

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

                if (clickCursorPosition < this.maskInputPosition[0]) {
                  this.setCaretToPos(this, this.cursorPosition);
                  event.stopPropagation ? event.stopPropagation() : (event.cancelBubble = true);
                  return;

                } else if (clickCursorPosition > (this.maskInputPosition[this.maskInputPosition.length - 1] + 1)) {
                  this.setCaretToPos(this, this.cursorPosition);
                  event.stopPropagation ? event.stopPropagation() : (event.cancelBubble = true);
                  return;
                }

                for (var i in this.maskInputPosition) {
                  if (clickCursorPosition == this.maskInputPosition[i]) {
                    this.cursorPosition = this.maskInputPosition[i];
                    this.maskInputPositionCursor = i;

                    return;
                  }
                }
                if (clickCursorPosition == this.maskInputPosition[this.maskInputPosition.length - 1]) {
                  this.isFinishPosition = true;
                  this.cursorPosition = clickCursorPosition;
                  return;
                }

                this.isCursorIsSeparator = true;

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
              },

              isKeyDelete:function () {
                if (this.getSelectionText()) {
                  var clickCursorPosition = this.doGetCaretPosition(this);
                  for (var i = 0; i < this.getSelectionText().length - 1; i++) {
                    this.maskValue[clickCursorPosition + i] = this.primaryMaskValue[clickCursorPosition + i];

                  }
                  this.value = this.toStringMaskValue(this.maskValue);
                  this.setCaretToPos(this, this.cursorPosition);
                  return false;
                }
                if (this.isFinishPosition) return false;
                if (this.maskInputPositionCursor != (this.maskInputPosition.length - 1)) {

                  this.maskValue[ this.maskInputPosition[this.maskInputPositionCursor]] = "_";
                  this.maskInputPositionCursor++;
                  this.value = this.toStringMaskValue(this.maskValue);
                  this.cursorPosition = this.maskInputPosition[this.maskInputPositionCursor];
                  this.setCaretToPos(this, this.cursorPosition);

                } else {
                  this.maskValue[ this.maskInputPosition[this.maskInputPositionCursor]] = "_";
                  this.value = this.toStringMaskValue(this.maskValue);
                  this.cursorPosition++;
                  this.isFinishPosition = true;
                  this.setCaretToPos(this, this.cursorPosition);
                }
                return false;
              },

              isKeyRight:function () {
                if (this.isFinishPosition) {
                  return false;
                }

                if (this.isCursorIsSeparator) {
                  for (var i in this.maskInputPosition) {
                    if (this.cursorPosition < this.maskInputPosition[i]) {
                      this.cursorPosition = this.maskInputPosition[i];
                      this.setCaretToPos(this, this.cursorPosition);
                      if (i == this.maskInputPosition.length - 1) {
                        this.isFinishPosition = true;

                      }
                      this.isCursorIsSeparator = false;
                      return false;
                    }
                  }
                }

                if (this.maskInputPositionCursor != (this.maskInputPosition.length - 1)) {
                  if ((this.maskInputPositionCursor != (this.maskInputPosition.length - 1)))
                    this.maskInputPositionCursor++;
                  this.cursorPosition = this.maskInputPosition[this.maskInputPositionCursor];
                  this.setCaretToPos(this, this.cursorPosition);
                  return false;
                }
                if (this.maskInputPositionCursor = (this.maskInputPosition.length)) {
                  this.cursorPosition++;
                  this.setCaretToPos(this, this.cursorPosition);
                  this.isFinishPosition = true;
                  return false;

                }
                return false;
              },

              isKeyLeft:function () {
                if (this.isCursorIsSeparator) {
                  for (var i in this.maskInputPosition) {
                    if (this.cursorPosition > this.maskInputPosition[i]) {
                      if (this.cursorPosition < this.maskInputPosition[i + 1]) {
                        this.cursorPosition = this.maskInputPosition[i];
                        this.setCaretToPos(this, this.cursorPosition);
                        this.isCursorIsSeparator = false;
                        return false;
                      }
                    }
                  }
                }

                if (this.isFinishPosition) {
                  this.cursorPosition--;
                  this.setCaretToPos(this, this.cursorPosition);
                  this.isFinishPosition = false;
                  return false;
                }
                if (this.maskInputPositionCursor != 0) {
                  this.maskInputPositionCursor--;
                  this.cursorPosition = this.maskInputPosition[this.maskInputPositionCursor];
                  this.setCaretToPos(this, this.cursorPosition);
                }
                return false;

              },
              isKeyEnd:function () {
                this.cursorPosition = this.maskInputPosition[this.maskInputPosition.length - 1] + 1;
                this.isFinishPosition = true;
                this.maskInputPositionCursor = this.maskInputPosition.length - 1;
                this.setCaretToPos(this, this.cursorPosition);
              },

              isKeyHome:function () {
                this.cursorPosition = this.maskInputPosition[0];
                this.maskInputPositionCursor = 0;
                this.setCaretToPos(this, this.cursorPosition);
                this.isFinishPosition = false;
                return false;
              },

              isKeyBackspace:function () {
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
              }

            }
    )
    ;
  }
}
;

