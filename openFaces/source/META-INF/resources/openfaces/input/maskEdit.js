/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2012, TeamDev Ltd.
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
  _init: function(inputId, mask) {
    mask = {
      emptySymbols: '',
      format: '',
      regex:   /^-?\d+(\.\d+)?$/
  }
    var inputField = O$(inputId);
    inputField.defaultValue = mask.format;
    if (inputField.value.length<0){
      inputField.value = mask.format;
    }
    O$.extend(inputField,{
      isCharacterKey: function(key) {
        return ( key >= 31 && key < 128 );
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
      //TODO: chrome only solution add support of other browsers
      onkeypress: function (e){
        var key = e.keyCode;
        if (this.isControlKey(key)) {

        }else if (this.isCharacterKey(key)) {
          var ch = String.fromCharCode( e.charCode);
          var str = this.value + ch;
          var pos = str.length;
          if ( mask.regex.test(str+"0") && (pos <= mask.format.length || mask.format.length == 0) ) {
            if ( mask.emptySymbols.indexOf(mask.format.charAt(pos - 1)) == -1 ) {
              str = this.value + mask.format.charAt(pos - 1) + ch;
            }
            this.value = str;
          }
          return false;
        }
      }
    })

  }
};

