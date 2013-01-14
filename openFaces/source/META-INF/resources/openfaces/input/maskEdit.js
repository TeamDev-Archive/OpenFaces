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
  _init: function(inputId, mask, validator) {
    mask = {
      emptySymbols: '_',
      format: '__/__/__',
      //regex:   /^-?\d+(\.\d+)?$/
      regex:   /^\d+$/
  }
    var maskEdit = O$(inputId);
    maskEdit.defaultValue = mask.format;
    if (maskEdit.value.length<0){
      maskEdit.value = mask.format;
    }
    maskEdit._validateValue = validator;
    //inputField._value = inputField.value;
    O$.extend(maskEdit,{
      isCharacterKey: function(key) {
        return ( key >= 31 && key < 128 );
      },

      onKeyPress: function(event, obj) {
        if(
                !(event.code == 9) // tab
                        && !(event.shift && event.code == 9) // shift + tab
                        && !(event.code == 13) // enter
                        && !(event.ctrl && event.code == 67) // ctrl + c
                        && !(event.ctrl && event.code == 86) // ctrl + v
                        && !(event.ctrl && event.code == 88) // ctrl + x
                ) {
          event.stop();
        }
      },

      //TODO: move to O$._getCaretPosition
      _setCaretPosition: function(pos){
        if(this.setSelectionRange)
        {
          this.focus();
          this.setSelectionRange(pos,pos);
        }
        else if (this.createTextRange) {
          var range = this.createTextRange();
          range.collapse(true);
          range.moveEnd('character', pos);
          range.moveStart('character', pos);
          range.select();
        }
      },

      _isSymbolEditable: function(pos){
        return mask.emptySymbols.indexOf(mask.format.charAt(pos)) != -1;
      },

      _getLeftEditableSymbolPos: function(index){
        var result = index-1;
        while (result>0){
          if ( this._isSymbolEditable(result) )
            return result;
          result--;
        }
        return 0;
      },

      _getRightEditableSymbolPos: function(index){
        var result = index+1;
        while (result<mask.format.length){
          if ( this._isSymbolEditable(result) != -1 )
            return result;
          result++;
        }
        return mask.format.length;
      },



      isControlKey : function(key) {
        switch(key) {
          case 8:
            return true;
            break;
          case 36:
            return true;
            break;
          case 35:
            return true;
            break;
          case 37:
            return true;
          case 38:
            return true;
            break;
          case 39:
            return true;
            break;
          case 40:
            return true;
            break;
          case 46:
            return true;
            break;
          default:
            return false;
        }
      },

      _setCharAt : function(str,index,chr) {
        if (mask.format.length==0){
          return str.substr(0,index) + chr + str.substr(index);
        }
        if(index > str.length-1) return str;
        return str.substr(0,index) + chr + str.substr(index+1);
      },

      _controlBackspace : function (){
        var pos = this._getLeftEditableSymbolPos(O$._getCaretPosition(this));
        var str = this.value;
        str = this._setCharAt(str, pos , mask.format[pos]);
        this.value = str;
        this._setCaretPosition(pos);
      },

      _controlDelete : function (){
        var caretPos = O$._getCaretPosition(this);
        var pos = this._getRightEditableSymbolPos(caretPos);
        if (this._isSymbolEditable(caretPos))
          pos = caretPos;
        var str = this.value;
        str = this._setCharAt(str, pos , mask.format[pos]);
        this.value = str;
        this._setCaretPosition(pos);
      },

      //TODO: chrome only solution add support of other browsers
      onkeydown: function (e){
        var key = e.keyCode;
        if (this.isControlKey(key)) {
          switch(key) {
            case 8:
              this._controlBackspace();
              return false;
              break;
            case 46:
              this._controlDelete();
              return false;
              break;
            default:
              return true;
          }
        }else if (this.isCharacterKey(key)) {
          var ch = String.fromCharCode( e.keyCode);
          var str = this.value;
          var pos = O$._getCaretPosition(this);
          if ( !this._isSymbolEditable(pos)) {
            pos = this._getRightEditableSymbolPos(pos);
          }
          str = this._setCharAt(str, pos, ch);
          if ( this._validateValue(str) && (pos <= mask.format.length || mask.format.length == 0) ) {
            this.value = str;
            this._setCaretPosition(this._getRightEditableSymbolPos(pos));
          }
          return false;
        }
      }
    })

  }
};

