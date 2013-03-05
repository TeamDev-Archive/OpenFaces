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
    //TODO:1 - This values have to be passed from server
    //TODO:2 - Such init attributes have to be initialised inside O$.initComponent method
    maskEdit.mask = "#########";
    maskEdit.maskSeparator = "+(380) ___ ___ __._ (life,mts)"; // TODO: BadName maskBlank or something like that
    maskEdit.maskValue = new Array(); // TODO: 4 - usually we use []
    maskEdit.primaryMaskValue = new Array();
    maskEdit.maskSeparatorLenght = maskEdit.maskSeparator.length;
    maskEdit.maskSeparatorPosition = new Array();
    maskEdit.maskInputPosition = new Array();
    if (dictionary == null) { // TODO: 3 - why not?:  maskEdit.dictionary = dictionary ?  dictionary : "absdefghijklmnopqrstuwwxyz";
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
    maskEdit.isFinishPosition = maskEdit.maskInputPosition.length <= 1; // ??


    maskEdit.maskInputPositionCursor = 0;
    maskEdit.value = maskEdit.maskSeparator;
    maskEdit.cursorPosition = maskEdit.maskInputPosition[0];
    setCaretToPos(maskEdit, maskEdit.cursorPosition);
    maskEdit.numeric = "1234567890";
    maskEdit.symbol = "`~,.?!@#$%^&*(){}[]";

    //TODO: look  O$._selectTextRange from util.js
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
              //TODO: start inside methods with '_'
              toStringMaskValue:function (maskValue) {
                return maskValue.toString().replace(/\,/g, "");
              },
              //TODO: start inside methods with '_'
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
              //TODO: hm there is function setSelectionRange(input, selectionStart, selectionEnd) above ?
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
              //TODO: hm there is function setSelectionRange(input, selectionStart, selectionEnd) above ?
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
                // TODO: var txt ?
                if (window.getSelection) {
                  txt = window.getSelection().toString();
                } else if (document.getSelection) {
                  txt = document.getSelection();
                } else if (document.selection) {
                  txt = document.selection.createRange().text;
                }
                return txt;
              },

              onclick:function () {
                var clickCursorPosition = this.doGetCaretPosition(this);
                console.log(this.maskInputPosition);
                console.log(clickCursorPosition);
                //TODO: why not one if with "||" ???
                if (clickCursorPosition < this.maskInputPosition[1]) {
                  return;
                }
                if (clickCursorPosition > this.maskInputPosition[this.maskInputPosition.length]) {
                  return;
                }

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
              // TODO: it will be useful to group all control keys workaround inside other 'extend'
              isKeyDelete:function () {
                if (this.getSelectionText()) {

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

