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

  //TODO: Common: check if user able to specify his own onKeyDown/onKeyPress etc event from JSF,
  //TODO: browser compatibility issues
  //TODO: client API: js methods for common access such as: getMask, setMask, setValue, getValue,
  // I mark methods which i actually like now with the comment :OK:
O$.MaskEdit = {

  _init:function (inputId, mask, blank, maskSymbolArray, rolloverClass, focusedClass, disabled, dictionary) {
    var maskEdit = O$.initComponent(inputId, {}, {
              //TODO: almost half of this value is internal values specify them with '_' at start
              mask:mask,
              blank:blank,
              maskSymbolArray:maskSymbolArray,
              rolloverClass:rolloverClass,      //TODO: Style classes can be passed directly to initComponent method see InitComponent signature
              focusedClass:focusedClass,
              disabled:disabled,
              dictionary:dictionary,
              maskInputPositionCursor:0, // TODO: maskInputCursorPosition
              value:blank,
              maskValue:[],
              primaryMaskValue:[],
              maskSeparatorPosition:[],
              maskInputPosition:[],
              isCursorIsSeparator:false,    //TODO: rename this
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
      for (var j in maskEdit.maskSymbolArray) { // TODO: see array.contains(obj) from JS and connect this with if (!IsMaskSymbol), remove !IsMaskSymbol at all
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
              onkeypress:function (e) { // :OK:
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
                    break; //TODO: we don't need 'break' if we call 'return' so remove them code will become better
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

              _isOccurrenceChar:function (symbol, symbolArray) { // :OK:
                return symbolArray.indexOf(symbol) != -1;

              },

              isControlKey:function (key) {
                switch (key) {
                  case 8:
                    return this._isKeyBackspace();
                    //TODO: we don't need 'break' if we call 'return' so remove them code will become better
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
              setCaretToPos:function (input, pos) { // :OK:
                this.setCursorPosition(input, pos, pos);
              },

              onkeydown:function (e) { // :OK:
                var key = e.keyCode;
                var isExit = this.isControlKey(key);
                console.log(this.cursorPosition);
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
                event = event || window.event;  // TODO:  Q: for what we need this?

                var clickCursorPosition = this.doGetCaretPosition(this);
                this._getCursorPosition(clickCursorPosition, true);
                console.log(this.cursorPosition);

              },

              doGetCaretPosition:function (ctrl) {  // TODO: see O$._getCaretPosition = function(field) from util.js
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
    O$.extend(maskEdit, { //TODO: now it's much more better!!!!
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
                }
                return false;
              },

              _isKeyRight:function () { // :OK:
                this._getCursorPosition(this.cursorPosition + 1, false);
                this.setCaretToPos(this, this.cursorPosition);

                return false;
              },

              _isKeyLeft:function () { // :OK:
                this._getCursorPosition(this.cursorPosition - 1, false);
                this.setCaretToPos(this, this.cursorPosition);

                return false;

              },

              _isKeyEnd:function () { // :OK:
                this._getCursorPosition(this.maskInputPosition[this.maskInputPosition.length] + 1, false)
                this.setCaretToPos(this, this.cursorPosition);
              },

              _isKeyHome:function () { // :OK:
                this._getCursorPosition(this.maskInputPosition[0], false)
                this.setCaretToPos(this, this.cursorPosition);
                return false;
              },

              _isKeyBackspace:function () { // TODO: There are still to much logics in this method separate some part to some other function and put delete function near backspace function
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
              ///////////////////////////////////////////////////////////////////
              //////////////////////////////////////////////////////////////////////
              /////////////////////////////////////////////////////////////
              _getCursorPosition:function (allegedPosition, carecter) { //TODO: why this method called "get" if it is "set" cursor position
                var i; //TODO: is it optimisation? why declaring of loop variable separated from the loop by itself?
                if (carecter) {
                  if ((allegedPosition < this.maskInputPosition[0]) ||
                          (allegedPosition > (this.maskInputPosition[this.maskInputPosition.length - 1] + 1))) {
                    this.setCaretToPos(this, this.cursorPosition); // TODO: actually right we've decided to avoid setting of caret pos inside this method
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
                  if (allegedPosition == this.maskInputPosition[this.maskInputPosition.length - 1] + 1) {
                    this.isFinishPosition = true;
                    this.cursorPosition = allegedPosition;
                    console.log("finish");
                    return;
                  }

                  this.isCursorIsSeparator = true;
                  this.cursorPosition = allegedPosition;

                }
                else { //TODO: maybe there is a sense to put both this parts to different functions for carecter=true nad carecter=false


                  if (allegedPosition < this.cursorPosition) {
                    if (this.isCursorIsSeparator) {
                      console.log(this.isCursorIsSeparator);
                      for (i = this.maskInputPosition.length - 2; i >= 0; i--) { //TODO: -2 ??? magic numbers - at list write some comment or make this code self documented
                        if ((this.maskInputPosition[i] < allegedPosition) && (this.maskInputPosition[i + 1] > allegedPosition)) {
                          this.maskInputPositionCursor = i;
                          this.cursorPosition = this.maskInputPosition[i];
                          this.isCursorIsSeparator = false;
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
                    if ((this.maskInputPositionCursor == 0) && (this.isFinishPosition == false)) return false;

                    if ((this.maskInputPositionCursor == 0) && (this.isFinishPosition == true)) {
                      this.maskInputPositionCursor = this.maskInputPosition.length - 1;
                      this.isFinishPosition = false;
                      this.cursorPosition = this.maskInputPosition[this.maskInputPositionCursor];
                      return false;
                    }

                    for (i = this.maskInputPosition.length - 1; i >= 0; i--) {
                      if (this.maskInputPosition[i] == allegedPosition) {
                        this.maskInputPositionCursor = i;
                        this.cursorPosition = allegedPosition;
                        this.isFinishPosition = false;
                        return true;
                      }
                    }


                    this.isFinishPosition = false;
                    this.maskInputPositionCursor--;
                    this.cursorPosition = this.maskInputPosition[this.maskInputPositionCursor];
                    //TODO: in common try to callect all this logic to some tree structure of Ifs with not so much return methods
                    return false;

                  } else {
                    //TODO: in common try to collect all this logic to some tree structure of Ifs with not so much return methods
                    if (this.isCursorIsSeparator) {
                      console.log(this.isCursorIsSeparator);
                      for (i = 1; i < this.maskInputPosition.length - 1; i++) {
                        if ((this.maskInputPosition[i - 1] < allegedPosition) && (this.maskInputPosition[i ] > allegedPosition)) {
                          this.maskInputPositionCursor = i;
                          this.cursorPosition = this.maskInputPosition[i];
                          this.isCursorIsSeparator = false;
                          this.isFinishPosition = false;
                          return  false;
                        }
                      }
                    }
                    if (this.isFinishPosition) return false;

                    for (i in this.maskInputPosition) {
                      if (this.maskInputPosition[i] == allegedPosition) {
                        this.maskInputPositionCursor = i;
                        this.cursorPosition = allegedPosition;

                        return true;
                      }
                    }
                    //TODO: in common try to collect all this logic to some tree structure of Ifs with not so much return methods
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

