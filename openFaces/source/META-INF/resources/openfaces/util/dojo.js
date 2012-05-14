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

/*
Here's a list of modifications from the original version:
MOD-1 :  Commented row in format function, where value should be multiplied 100 if consist '%'
MOD-2 :  Commented row, where value should be divided 100 if consist '%'
* */
O$.Dojo = {

  Number : {
    format : function(value, options) {
      //options = O$.Dojo.mixin({}, options || {});
      var customs = options.customs;

      var pattern = options.pattern || customs[(options.type || "decimal") + "Format"];
      if (isNaN(value) || Math.abs(value) == Infinity) {
        return null;
      }
      return O$.Dojo.Number._applyPattern(value, pattern, options);
    },
    _numberPatternRE : /[#0,]*[#0](?:\.0*#*)?/,
    _applyPattern : function(value, pattern, options) {
      options = options || {};
      var group = options.customs.group;
      var decimal = options.customs.decimal;
      var patterns = pattern.split(";");
      var positivePattern = patterns[0];
      pattern = patterns[(value < 0) ? 1 : 0] || ("-" + positivePattern);
      if (pattern.indexOf("%") != -1) {
         // <MOD-1> code changed
        //value *= 100;
        //</MOD-1>
      } else {
        if (pattern.indexOf("‰") != -1) {
          value *= 1000;
        } else {
          if (pattern.indexOf("¤") != -1) {
            group = options.customs.currencyGroup || group;
            decimal = options.customs.currencyDecimal || decimal;
            pattern = pattern.replace(/\u00a4{1,3}/, function(_d) {
              var _e = ["symbol","currency","displayName"][_d.length - 1];
              return options[_e] || options.currency || "";
            });
          } else {
            if (pattern.indexOf("E") != -1) {
              throw new Error("exponential notation not supported");
            }
          }
        }
      }
      var numberRE = O$.Dojo.Number._numberPatternRE;
      var numberREMatch = positivePattern.match(numberRE);
      if (!numberREMatch) {
        throw new Error("unable to find a number expression in pattern: " + pattern);
      }
      if (options.fractional === false) {
        options.places = 0;
      }
      return pattern.replace(numberRE, O$.Dojo.Number._formatAbsolute(value, numberREMatch[0], {decimal:decimal,group:group,places:options.places,round:options.round}));
    },

    round:function(value, places, increment) {
      var _14 = 10 / (increment || 10);
      var _15 = (_14 * +value).toFixed(places) / _14;
      if ((0.9).toFixed() == 0) {
        var d = Math.pow(10, -places || 0),a = Math.abs(value);
        if (!value || a >= d || a * Math.pow(10, places + 1) < 5) {
          d = 0;
        }
        return _15 + (value > 0 ? d : -d);
      } else {
        return _15;
      }
    },
    _formatAbsolute : function(_1b, _1c, _1d) {
      _1d = _1d || {};
      if (_1d.places === true) {
        _1d.places = 0;
      }
      if (_1d.places === Infinity) {
        _1d.places = 6;
      }
      var _1e = _1c.split(".");
      var _1f = (_1d.places >= 0) ? _1d.places : (_1e[1] && _1e[1].length) || 0;
      if (!(_1d.round < 0)) {
        _1b = O$.Dojo.Number.round(_1b, _1f, _1d.round);
      }
      var _20 = String(Math.abs(_1b)).split(".");
      var _21 = _20[1] || "";
      if (_1d.places) {
        var _22 = O$.Dojo.isString(_1d.places) && _1d.places.indexOf(",");
        if (_22) {
          _1d.places = _1d.places.substring(_22 + 1);
        }
        _20[1] = O$.Dojo.String.pad(_21.substr(0, _1d.places), _1d.places, "0", true);
      } else {
        if (_1e[1] && _1d.places !== 0) {
          var pad = _1e[1].lastIndexOf("0") + 1;
          if (pad > _21.length) {
            _20[1] = O$.Dojo.String.pad(_21, pad, "0", true);
          }
          var _24 = _1e[1].length;
          if (_24 < _21.length) {
            _20[1] = _21.substr(0, _24);
          }
        } else {
          if (_20[1]) {
            _20.pop();
          }
        }
      }
      var _25 = _1e[0].replace(",", "");
      pad = _25.indexOf("0");
      if (pad != -1) {
        pad = _25.length - pad;
        if (pad > _20[0].length) {
          _20[0] = O$.Dojo.String.pad(_20[0], pad);
        }
        if (_25.indexOf("#") == -1) {
          _20[0] = _20[0].substr(_20[0].length - pad);
        }
      }
      var _26 = _1e[0].lastIndexOf(",");
      var _27,_28;
      if (_26 != -1) {
        _27 = _1e[0].length - _26 - 1;
        var _29 = _1e[0].substr(0, _26);
        _26 = _29.lastIndexOf(",");
        if (_26 != -1) {
          _28 = _29.length - _26 - 1;
        }
      }
      var _2a = [];
      for (var _2b = _20[0]; _2b;) {
        var off = _2b.length - _27;
        _2a.push((off > 0) ? _2b.substr(off) : _2b);
        _2b = (off > 0) ? _2b.slice(0, off) : "";
        if (_28) {
          _27 = _28;
          delete _28;
        }
      }
      _20[0] = _2a.reverse().join(_1d.group || ",");
      return _20.join(_1d.decimal || ".");
    },
    regexp : function(options) {
      return O$.Dojo.Number._parseInfo(options).regexp;
    },
    _parseInfo : function(options) {
      options = options || {};
      var _30 = options.customs;
      var _31 = options.pattern || _30[(options.type || "decimal") + "Format"];
      var _32 = _30.group;
      var _33 = _30.decimal;
      var _34 = 1;
      if (_31.indexOf("%") != -1) {
        // <MOD-2> code changed
        //_34 /= 100;
        // </MOD-2>
      } else {
        if (_31.indexOf("‰") != -1) {
          _34 /= 1000;
        } else {
          var _35 = _31.indexOf("¤") != -1;
          if (_35) {
            _32 = _30.currencyGroup || _32;
            _33 = _30.currencyDecimal || _33;
          }
        }
      }
      var _36 = _31.split(";");
      if (_36.length == 1) {
        _36.push("-" + _36[0]);
      }
      var re = O$.Dojo.RegExp.buildGroupRE(_36, function(_38) {
        _38 = "(?:" + O$.Dojo.RegExp.escapeString(_38, ".") + ")";
        return _38.replace(O$.Dojo.Number._numberPatternRE, function(_39) {
          var _3a = {signed:false,separator:options.strict ? _32 : [_32,""],fractional:options.fractional,decimal:_33,exponent:false};
          var _3b = _39.split(".");
          var _3c = options.places;
          if (_3b.length == 1 || _3c === 0) {
            _3a.fractional = false;
          } else {
            if (_3c === undefined) {
              _3c = options.pattern ? _3b[1].lastIndexOf("0") + 1 : Infinity;
            }
            if (_3c && options.fractional == undefined) {
              _3a.fractional = true;
            }
            if (!options.places && (_3c < _3b[1].length)) {
              _3c += "," + _3b[1].length;
            }
            _3a.places = _3c;
          }
          var _3d = _3b[0].split(",");
          if (_3d.length > 1) {
            _3a.groupSize = _3d.pop().length;
            if (_3d.length > 1) {
              _3a.groupSize2 = _3d.pop().length;
            }
          }
          return "(" + O$.Dojo.Number._realNumberRegexp(_3a) + ")";
        });
      }, true);
      if (_35) {
        re = re.replace(/([\s\xa0]*)(\u00a4{1,3})([\s\xa0]*)/g, function(_3e, _3f, _40, _41) {
          var _42 = ["symbol","currency","displayName"][_40.length - 1];
          var _43 = O$.Dojo.RegExp.escapeString(options[_42] || options.currency || "");
          _3f = _3f ? "[\\s\\xa0]" : "";
          _41 = _41 ? "[\\s\\xa0]" : "";
          if (!options.strict) {
            if (_3f) {
              _3f += "*";
            }
            if (_41) {
              _41 += "*";
            }
            return "(?:" + _3f + _43 + _41 + ")?";
          }
          return _3f + _43 + _41;
        });
      }
      return {regexp:re.replace(/[\xa0 ]/g, "[\\s\\xa0]"),group:_32,decimal:_33,factor:_34};
    },
    parse : function(expression, options) {
      var _46 = O$.Dojo.Number._parseInfo(options);
      var _47 = (new RegExp("^" + _46.regexp + "$")).exec(expression);
      if (!_47) {
        return NaN;
      }
      var _48 = _47[1];
      if (!_47[1]) {
        if (!_47[2]) {
          return NaN;
        }
        _48 = _47[2];
        _46.factor *= -1;
      }
      _48 = _48.replace(new RegExp("[" + _46.group + "\\s\\xa0" + "]", "g"), "").replace(_46.decimal, ".");
      return _48 * _46.factor;
    },
    _realNumberRegexp : function(_49) {
      _49 = _49 || {};
      if (!("places" in _49)) {
        _49.places = Infinity;
      }
      if (typeof _49.decimal != "string") {
        _49.decimal = ".";
      }
      if (!("fractional" in _49) || /^0/.test(_49.places)) {
        _49.fractional = [true,false];
      }
      if (!("exponent" in _49)) {
        _49.exponent = [true,false];
      }
      if (!("eSigned" in _49)) {
        _49.eSigned = [true,false];
      }
      var _4a = O$.Dojo.Number._integerRegexp(_49);
      var _4b = O$.Dojo.RegExp.buildGroupRE(_49.fractional, function(q) {
        var re = "";
        if (q && (_49.places !== 0)) {
          re = "\\" + _49.decimal;
          if (_49.places == Infinity) {
            re = "(?:" + re + "\\d+)?";
          } else {
            re += "\\d{" + _49.places + "}";
          }
        }
        return re;
      }, true);
      var _4e = O$.Dojo.RegExp.buildGroupRE(_49.exponent, function(q) {
        if (q) {
          return "([eE]" + O$.Dojo.Number._integerRegexp({signed:_49.eSigned}) + ")";
        }
        return "";
      });
      var _50 = _4a + _4b;
      if (_4b) {
        _50 = "(?:(?:" + _50 + ")|(?:" + _4b + "))";
      }
      return _50 + _4e;
    },
    _integerRegexp : function(_51) {
      _51 = _51 || {};
      if (!("signed" in _51)) {
        _51.signed = [true,false];
      }
      if (!("separator" in _51)) {
        _51.separator = "";
      } else {
        if (!("groupSize" in _51)) {
          _51.groupSize = 3;
        }
      }
      var _52 = O$.Dojo.RegExp.buildGroupRE(_51.signed, function(q) {
        return q ? "[-+]" : "";
      }, true);
      var _54 = O$.Dojo.RegExp.buildGroupRE(_51.separator, function(sep) {
        if (!sep) {
          return "(?:\\d+)";
        }
        sep = O$.Dojo.RegExp.escapeString(sep);
        if (sep == " ") {
          sep = "\\s";
        } else {
          if (sep == " ") {
            sep = "\\s\\xa0";
          }
        }
        var grp = _51.groupSize,_57 = _51.groupSize2;
        if (_57) {
          var _58 = "(?:0|[1-9]\\d{0," + (_57 - 1) + "}(?:[" + sep + "]\\d{" + _57 + "})*[" + sep + "]\\d{" + grp + "})";
          return ((grp - _57) > 0) ? "(?:" + _58 + "|(?:0|[1-9]\\d{0," + (grp - 1) + "}))" : _58;
        }
        return "(?:0|[1-9]\\d{0," + (grp - 1) + "}(?:[" + sep + "]\\d{" + grp + "})*)";
      }, true);
      return _52 + _54;
    }
  },
  String : {

    rep : function(str, num) {
      if (num <= 0 || !str) {
        return "";
      }
      var _3 = [];
      for (; ;) {
        if (num & 1) {
          _3.push(str);
        }
        if (!(num >>= 1)) {
          break;
        }
        str += str;
      }
      return _3.join("");
    },
    pad : function(text, size, ch, end) {
      if (!ch) {
        ch = "0";
      }
      var _8 = String(text),_9 = O$.Dojo.String.rep(ch, Math.ceil((size - _8.length) / ch.length));
      return end ? _8 + _9 : _9 + _8;
    }
  },

  RegExp : {

    escapeString : function(str, except) {
      return str.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, function(ch) {
        if (except && except.indexOf(ch) != -1) {
          return ch;
        }
        return "\\" + ch;
      });
    },
    buildGroupRE : function(arr, re, nonCapture) {
      if (!(arr instanceof Array)) {
        return re(arr);
      }
      var b = [];
      for (var i = 0; i < arr.length; i++) {
        b.push(re(arr[i]));
      }
      return O$.Dojo.RegExp.group(b.join("|"), nonCapture);
    },
    group : function(expression, nonCapture) {
      return "(" + (nonCapture ? "?:" : "") + expression + ")";
    }
  },

  isString : function(/*anything*/ it) {
    return !!arguments.length && it != null && (typeof it == "string" || it instanceof String); // Boolean
  }/*,

  mixin : function(obj, props) {
    if (!obj) {
      obj = {};
    }
    for (var i = 1, l = arguments.length; i < l; i++) {
      O$.Dojo._mixin(obj, arguments[i]);
    }
    return obj; // Object
  },

  _tobj : {},
  _mixin : function(obj,  props) {
    var tobj = O$.Dojo._tobj;
    for (var x in props) {
      if (tobj[x] === undefined || tobj[x] != props[x]) {
        obj[x] = props[x];
      }
    }
    // IE doesn't recognize custom toStrings in for..in
    if (O$.isExplorer() && props) {
      var p = props.toString;
      if (typeof p == "function" && p != obj.toString && p != tobj.toString &&
          p != "\nfunction toString() {\n    [native code]\n}\n") {
        obj.toString = props.toString;
      }
    }
    return obj; // Object
  }*/



}