/*
 * OpenFaces - JSF Component Library 3.0
 * Copyright (C) 2007-2011, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

function showFullSizeImage(event) {
  var target;
  if (!event) {
    event = window.event;
  }

  if (event.target) {
    target = event.target;
  }
  else if (event.srcElement) {
    target = event.srcElement;
  }
  if (target.nodeType == 3) { // defeat Safari bug
    target = target.parentNode;
  }

  O$('form:full_size_image_win').hide();
  var parentWindow = O$("form:full_size_image_win");
  O$("full_size_img").src = target.src;
  O$("form:full_size_image_win::content").style.overflow = "visible";
  parentWindow.style.height = getNaturalHeight(target) + 30 + "px";
  parentWindow.style.width = getNaturalWeight(target) + 4 + "px";
  O$('form:full_size_image_win').show();
}

function getNaturalHeight(img) {
  if (img.naturalHeight) {
    return img.naturalHeight;
  } else {
    lgi = new Image();
    lgi.src = img.src;
    return lgi.height;
  }
}

function getNaturalWeight(img) {
  if (img.naturalWidth) {
    return img.naturalWidth;
  } else {
    lgi = new Image();
    lgi.src = img.src;
    return lgi.width;
  }
}

function loadDocumentationSite() {
  O$('form:documentationSite_win').show();
  O$('form:documentationSite_win::content').style.overflow = "visible";
  O$('documentationSiteContent').src = "http://www.openfaces.org/documentation/";
}

function showBrowserWindow() {
  O$('form:webBrowser_win').show();
  O$('form:webBrowser_win::content').style.overflow = "visible";
  O$('browserContent').src = "http://www.youtube.com/embed/TJh1c_eFRBs";
  O$('form:browserUrlInput').value = "http://www.youtube.com/embed/TJh1c_eFRBs";
}

function submitBrowserUrl(event) {
  var keynum;

  if (window.event) { // IE
    keynum = event.keyCode;
  }
  else if (event.which) { // Netscape/Firefox/Opera
    keynum = event.which;
  }

  if (keynum == 13) {
    loadUrl();
  }
}

function loadUrl() {
  var url = O$('form:browserUrlInput').value;
  var protocol = "http://";
  if (url.indexOf(protocol) != 0) {
    url = protocol + url;
  }
  O$('browserContent').src = url;
}

function stopEvent(e) {
  var evt = e ? e : event;

  if (evt.preventDefault) {
    evt.preventDefault();
  }
  evt.returnValue = false;

  if (evt.stopPropagation)
    evt.stopPropagation();
  evt.cancelBubble = true;
}

<!--  *** Calculator javascript ****************************** -->
var Calculator = {
  state : false,
  operation : false,
  value : "",
  calculated : false,

  floatValue : function() {
    return parseFloat(this.stringValue());
  },

  stringValue : function() {
    return this.value == "" || this.value == "." ? '0' : this.value;
  },

  formatValue : function(val) {
    val = "" + val;
    if (val.length >= 13) {
      val = parseFloat(val).toPrecision(7);
      val = parseFloat(val);
    }

    return val;
  },

  clearAll : function() {
    this.value = "";
    this.clearInput();
    this.state = false;
    this.operation = false;
    this.calculated = false;
    this.focus();
  },

  clearInput : function () {
    O$("form:calcInputField").value = this.formatValue(this.stringValue());
  },

  typeNumber : function (character) {
    if (this.calculated) {
      this.clearAll();
    }
    if ((character == '.' && this.value.indexOf('.') != -1) || this.value.length >= 13) {
      return;
    }
    this.value = this.value + character;
    O$("form:calcInputField").value = this.formatValue(this.stringValue());
    this.calculated = false;
    this.focus();
  },

  typeOperation : function (character) {
    if (character == '+' || character == '-' || character == '/' || character == '*') {
      if (this.operation && !this.calculated) {
        this.calculate();
      } else if (!this.state) {
        this.state = this.floatValue();
      }
      this.operation = character;
      this.value = "";
      this.calculated = false;
    }
    this.focus();
  },

  calculate : function () {
    if (this.operation) {
      if (this.value == "") {
        this.value = this.state;
      }
      if (this.operation == '+') {
        this.state = this.state + this.floatValue();
      }
      if (this.operation == '-') {
        this.state = this.state - this.floatValue();
      }
      if (this.operation == '*') {
        this.state = this.state * this.floatValue();
      }
      if (this.operation == '/') {
        if (this.stringValue() == '0') {
          O$("form:calcInputField").value = "Division by zero";
          return;
        }
        this.state = this.state / this.floatValue();
      }
      O$("form:calcInputField").value = this.formatValue(this.state);
      this.calculated = true;
    }
    this.focus();
  },
  focus : function() {
    O$('form:calcInputField').focus();
  }
};

function checkRequestKeys(evt) {
  var e = evt ? evt : window.event;
  if (e.keyCode == 27) { // Escape
    Calculator.clearAll();
  }
  if (e.keyCode == 48 || e.keyCode == 96) { // 0
    Calculator.typeNumber('0');
  }
  if (e.keyCode == 49 || e.keyCode == 97) { // 1
    Calculator.typeNumber('1');

  }
  if (e.keyCode == 50 || e.keyCode == 98) { // 2
    Calculator.typeNumber('2');
  }
  if (e.keyCode == 51 || e.keyCode == 99) { // 3
    Calculator.typeNumber('3');
  }
  if (e.keyCode == 52 || e.keyCode == 100) { // 4
    Calculator.typeNumber('4');
  }
  if (e.keyCode == 53 || e.keyCode == 101) { // 5
    Calculator.typeNumber('5');
  }
  if (e.keyCode == 54 || e.keyCode == 102) { // 6
    Calculator.typeNumber('6');
  }
  if (e.keyCode == 55 || e.keyCode == 103) { // 7
    Calculator.typeNumber('7');
  }
  if (e.keyCode == 56 || e.keyCode == 104) { // 8
    Calculator.typeNumber('8');
  }
  if (e.keyCode == 57 || e.keyCode == 105) { // 9
    Calculator.typeNumber('9');
  }
  if (e.keyCode == 106) { // multiply
    Calculator.typeOperation('*');
  }
  if (e.keyCode == 107) { // add
    Calculator.typeOperation('+');
  }
  if (e.keyCode == 109) { // subtract
    Calculator.typeOperation('-');
  }
  if (e.keyCode == 110) { // decimal point
    Calculator.typeNumber('.');
  }
  if (e.keyCode == 111) { // divide
    Calculator.typeOperation('/');
  }
  if (e.keyCode == 13) { // Enter
    Calculator.calculate();
  }
  return false;
}
