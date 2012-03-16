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
O$._clientValidationSupport = true;
O$._validators = [];

O$.Message = function(summary, detail, severity, validator) {
  this.summary = summary;
  this.detail = detail;
  this.severity = severity;
  this.validator = validator;
}

O$.getValidators = function(input) {
  if (!input)
    return [];
  if (!O$._validators[input.id]) {
    O$._validators[input.id] = [];
  }
  return O$._validators[input.id];
}

O$.getConverters = function(input) {
  var validators = O$.getValidators(input);
  if (validators && validators.length > 0) {
    var converters = [];
    for (var i = 0; i < validators.length; i++) {
      var validator = validators[i];
      if (validator && validator.type && validator.type == "converter") {
        converters.push(validator);
      }
    }
    return converters;
  }
  else
    return null;
}

O$.getValidatorsOnly = function(input) {
  var validators = O$.getValidators(input);
  if (validators && validators.length > 0) {
    var result = [];
    for (i = 0; i < validators.length; i++) {
      var validator = validators[i];
      if ((!validator.type) || (!(validator.type && validator.type == "converter"))) {
        result.push(validator);
      }
    }
    return result;
  }
  else
    return null;
}

O$.addValidatorsById = function(inputId, validators, clientValueFunction) {
  if (!inputId || !validators)
    return;
  var input = O$.byIdOrName(inputId);
  if (!input)
    return;

  var inputValidators = O$.getValidators(input);
  inputValidators.length = 0;
  for (var i = 0, count = validators.length; i < count; i++) {
    inputValidators.push(validators[i]);
  }
  if (clientValueFunction) {
    input._clientValueFunction = clientValueFunction;
    input._clientValueFunctionExists = true;
  }

}

O$.mainValidate = function(input) {
  input.valid = true;
  O$.getMessages(input).length = 0;

  var converters = O$.getConverters(input);
  var validators = O$.getValidatorsOnly(input);

  var i;
  if (converters) {
    for (i = 0; i < converters.length; i++) {
      var converter = converters[i];
      if (!converter.validate(input))
        input.valid = false;
    }
  }

  if (input.valid && validators) {
    for (i = 0; i < validators.length; i++) {
      var validator = validators[i];
      if (!validator.validate(input))
        input.valid = false;
    }
  }

}

O$.autoValidate = function(input, fullValidation) {
  input.valid = true;
  if (!fullValidation) {
    if (input.autoValidate == false)
      return;
  }
  O$.mainValidate(input);
}

O$.validate = function(input) {
  if (!input)
    O$.assert(input, "O$.validate: Input not found.");

  O$.mainValidate(input);
  O$.updateClientMessages();
  O$.updateClientMessagesPosition();
  return input.valid != false;
}

O$.validateById = function(inputId) {
  var input = O$.byIdOrName(inputId);
  O$.assert(input, "O$.validateById: Element with" + inputId + " not found.");
  O$.mainValidate(input);
  O$.updateClientMessages();
  O$.updateClientMessagesPosition();
  return input.valid != false;
}

O$._validateRecursive = function(element, fullValidation) {
  var valid = true;
  var children = element.childNodes;
  if (children && children.length > 0) {
    for (var i = 0; i < children.length; i++) {
      var child = children[i];
      if (O$.notEmpty(O$.getValidators(child)) || (O$.getMessages(child) && O$.getMessages(child).length > 0)) {
        O$.autoValidate(child, fullValidation);
        if (!child.valid) {
          valid = false;
        }
      }
      if (!O$._validateRecursive(child, fullValidation)) {
        valid = false;
      }
    }
  }
  return valid;
}

O$.validateForm = function(form) {
  if (form._of_skipValidation) {
    return true;
  }
  if (O$.isLoadedFullPage()) {
    var valid = O$._validateRecursive(form, true);
    O$.updateClientMessages();
    return valid;
  }
  return false;
}

O$._autoValidateForm = function(form) {
  if (form._of_skipValidation) {
    return true;
  }
  if (O$.isLoadedFullPage()) {
    var valid = O$._validateRecursive(form);
    O$.updateClientMessages();
    return valid;
  }
  return false;
}

O$.validateFormById = function(formId) {
  var form = O$(formId);
  if (form)
    return O$.validateForm(form);
  return false;
}

O$.validateEnclosingForm = function(element) {
  var form = O$.getParentNode(element, "FORM");
  if (form) {
    var r = O$.validateForm(form);
    return r;
  }
  return true;
}

O$.getValue = function(input) {
  if (input._clientValueFunctionExists) {
    return "" + input._clientValueFunction.call();
  }

  var value = "";

  if (input.tagName.toUpperCase() == "INPUT") {
    //validated component is input itself - so we will check input type
    if (input.type.toLowerCase == "checkbox" || input.type.toLowerCase == "radio") {
      if (input.checked)
        value = "true";
      else
        value = "false";
    }
    value = input.getValue ? input.getValue() : input.value;
  } else if (input.tagName.toUpperCase() == "SELECT") {
    if (input.type.toLowerCase() == "select-one") {
      for (var i = 0; i < input.options.length; i++) {
        if (input.options[i].selected)
          value = input.options[i].value;
      }

    } else {
      var selected = [];
      for (var i = 0; i < input.options.length; i++) {
        if (input.options[i].selected) {
          selected.push(input.options[i].value);
        }
      }
      if (selected.length > 0) {
        value = selected;
      }
    }

  } else if (input.tagName.toUpperCase() == "TEXTAREA") {
    value = input.value;
  } else if (input.tagName.toUpperCase() == "SPAN") {
    for (var i = 0, count = input.childNodes.length; i < count; i++) {
      var child = input.childNodes[i];
      if (child.nodeType != 1)
        value += child.nodeValue;
    }
  } else {
    //validated component - it is a container - for example, html table with a group of inputs
    var inputs = input.getElementsByTagName("input");
    if (inputs && inputs.length > 0) {
      var selected = [];
      for (var i = 0; i < inputs.length; i++) {
        if (inputs[i].checked) {
          selected.push(inputs[i].value);
        }
      }

      if (selected.length > 0) {
        value = selected;
      }
    }
  }

  //alert("id: " + input.id + " ; value= " + value);
  return value;
}

O$.notEmpty = function(value) {
  return value && value.length > 0;
}

O$.getMessages = function(input) {
  if (!input._of_messages) {
    try {
      input._of_messages = [];
    } catch (e) {
    }
  }
  return input._of_messages;
}

O$.getGlobalMessages = function() {
  if (!document._of_globalMessages) {
    document._of_globalMessages = [];
  }
  return document._of_globalMessages;
}

O$.getAllMessages = function(globalOnly) {
  if (!globalOnly) {
    var messages = [];
    O$.getAllMessageRecursive(document, messages);
    return messages;
  } else
    return O$.getGlobalMessages();
}

O$.getAllMessageRecursive = function(element, messages) {
  var children = element.childNodes;
  if (children && children.length > 0) {
    for (var i = 0; i < children.length; i++) {
      var child = children[i];
      O$.addMessageFromChild(messages, child);
      O$.getAllMessageRecursive(child, messages);
    }
  }
}

O$.addMessageFromChild = function(messages, child) {
  try {
    var im = O$.getMessages(child);
    for (var i = 0; i < im.length; i++) {
      messages.push(im[i]);
    }
  } catch (e) {
    //todo: to something
  }

}


O$.addMessage = function(input, summary, detail, severity, validator) {
  var messages = O$.getMessages(input);
  if (!severity)
    severity = "error";
  var message = new O$.Message(summary, detail, severity, validator);
  messages.push(message);

}

O$.addMessageById = function(inputId, summary, detail, severity) {
  var input = O$.byIdOrName(inputId);
  O$.addMessage(input, summary, detail, severity);
}

O$.addGlobalMessage = function(summary, detail, severity) {
  var messages = O$.getGlobalMessages();
  var message = new O$.Message(summary, detail, severity);
  messages.push(message);
}

O$.getClientMessageRenderers = function() {
  if (!document._of_clientMessageRenderers) {
    document._of_clientMessageRenderers = {};
  }
  return document._of_clientMessageRenderers;
}

O$.getClientMessageRenderersWithVisibleBubble = function() {
  if (!document.clientMessageRenderersWithVisibleBubble) {
    document.clientMessageRenderersWithVisibleBubble = {};
  }
  return document.clientMessageRenderersWithVisibleBubble;
}

O$.getMessagesRenderers = function() {
  if (!document._of_messagesRenderers) {
    document._of_messagesRenderers = [];
  }
  return document._of_messagesRenderers;
}

O$.addClientMessageRenderer = function(clientMessageRenderer) {
  O$.getClientMessageRenderers()[clientMessageRenderer.clientId + "_" + clientMessageRenderer.forId] = clientMessageRenderer;
}

O$.addMessagesRenderer = function(messagesRenderer) {
  var messagesRenderers = O$.getMessagesRenderers();
  messagesRenderers.push(messagesRenderer);
}

O$.updateClientMessages = function(forId) {
  if (!forId) {
    var messagesRenderers = O$.getMessagesRenderers();
    for (var i = 0; i < messagesRenderers.length; i++) {
      messagesRenderers[i].update();
    }
  }

  var clientMessageRenderers = O$.getClientMessageRenderers();
  for (var key in clientMessageRenderers) {
    var clientMessageRenderer = clientMessageRenderers[key];
    if (!forId || clientMessageRenderer.forId == forId)
      clientMessageRenderer.update();
  }
}

O$.updateClientMessagesPosition = function() {
  var clientFloatingIconMessageRenderers = O$.getClientMessageRenderersWithVisibleBubble();
  for (var key in clientFloatingIconMessageRenderers) {
    clientFloatingIconMessageRenderers[key].position();
  }
}

O$.addOnSubmitEvent = function(func, frmId) {
  // var element = O$(elementId);
  // O$.assert(element, "O$.addOnSubmitEvent: Element with id: " + elementId + " not found.");
  //var frm = O$.getParentNode(element, "FORM");
  //O$.assert(frm, "O$.addOnSubmitEvent: Enclosing form not found for element with id: " + elementId);
  var frm = O$(frmId);
  O$.assert(frm, "O$.addOnSubmitEvent: Form not found. id: " + frmId);
  var oldonsubmit = frm.onsubmit;

  if (typeof frm.onsubmit != "function") {
    frm.onsubmit = function() {
      return func(frm);
    };
  } else {
    frm.onsubmit = function() {
      oldonsubmit();
      return func(frm);
    };
  }

}


O$.isLong = function(value) {
  if (!/^[+-]?\d+$/.test(value)) {
    return false;
  }
  var ivalue = parseInt(value);
  if (ivalue > 0x7fffffffffffffff) {
    return false;
  }
  return ivalue >= -0x8000000000000000;

}

O$.getLong = function(value) {
  if (!O$.isLong(value))
    return null;
  return parseInt(value);
}

O$.isInt = function(value) {
  if (!/^[+-]?\d+$/.test(value)) {
    return false;
  }
  var ivalue = parseInt(value);
  if (ivalue > 0x7fffffff) {
    return false;
  }
  return ivalue >= -0x80000000;

}


O$.isBigInt = function(value) {
  return /^[+-]?\d+$/.test(value);

};


O$.isBigDecimal = function(value) {
  if (isNaN(value)) {
    return false;
  }
  fvalue = parseFloat(value);
  return true;
}

O$.isShort = function(value) {
  if (!/^[+-]?\d+$/.test(value)) {
    return false;
  }
  var ivalue = parseInt(value);
  if (ivalue > 32767) {
    return false;
  }
  return ivalue >= -32768;

}

O$.isByte = function(value) {
  if (!/^[+-]?\d+$/.test(value)) {
    return false;
  }
  var ivalue = parseInt(value);
  if (ivalue > 127) {
    return false;
  }
  return ivalue >= -128;

}

O$.isFloat = function(value) {
  if (isNaN(value)) {
    return false;
  }
  var fvalue = parseFloat(value);
  if (fvalue > 3.4028235e+38) {
    return false;
  }
  return fvalue >= -3.4028235e+38;

};

O$.isDouble = function(value) {
  if (isNaN(value)) {
    return false;
  }
  var fvalue = parseFloat(value);
  if (fvalue > 1.7976931348623157e+308) {
    return false;
  }
  return fvalue >= -1.7976931348623157e+308;

}

O$.matchRegExp = function(value, regExp) {
  return regExp.test(value);
}

O$.addNotValidatedInputs = function(ids) {
  if (!ids)
    return;
  if (ids.length == 0)
    return;
  for (var i = ids.length - 1; i >= 0; i--) {
    var element = O$(ids[i]);
    O$.assert(element, "O$.addNotValidatedInputs: Element not found. id: " + ids[i]);
    if (element)
      element.autoValidate = false;
  }
}

O$.addNotValidatedInput = function(id) {
  if (!id)
    return;
  var element = O$(id);
  O$.assert(element, "O$.addNotValidatedInput: Element not found. id: " + id);
  if (element)
    element.autoValidate = false;
}

O$.submitWithoutValidation = function(element) {
  var form = O$.getParentNode(element, "FORM");
  O$.assert(form, "O$.submitWithoutValidation: Enclosing form not found.");
  if (form) {
    form._of_skipValidation = true;
    if (form.onsubmit)
      if (!form.onsubmit()) {
        form._of_skipValidation = false;
        return;
      }
    form.submit();
  }
}

O$.switchOnValidation = function(element) {
  var form = O$.getParentNode(element, "FORM");
  O$.assert(form, "O$.switchOnValidation: Enclosing form not found.");
  if (form) {
    form._of_skipValidation = false;
  }
}

O$.switchOffValidation = function(element) {
  var form = O$.getParentNode(element, "FORM");
  O$.assert(form, "O$.switchOffValidation: Enclosing form not found.");
  if (form) {
    form._of_skipValidation = true;
  }
}

O$.addRegularUpdate = function(timeout) {
  if (!timeout)
    timeout = 0;
  window.setTimeout("O$.positionsUpdate();", timeout);
}

O$.positionsUpdate = function() {
  O$.updateClientMessagesPosition();
  O$.addRegularUpdate(500);
}

O$._presentationExistsForAllComponents = function() {
  O$._presentationExistsForAllComponentsField = true;
}

O$._isPresentationExistsForAllComponents = function() {
  return O$._presentationExistsForAllComponentsField;
}

O$._presentationExistsForComponent = function(componentId) {
  O$._getComponentsWithPresentation().push(componentId);
}

O$._getComponentsWithPresentation = function() {
  var componentsWithPresentation = O$._componentsWithPresentation;
  if (!componentsWithPresentation) {
    componentsWithPresentation = [];
    O$._componentsWithPresentation = componentsWithPresentation;
  }
  return componentsWithPresentation;
}

O$.resetFormValidation = function(form) {
  if (!form._of_skipValidation) {
    O$._resetValidationRecursive(form);
    O$.updateClientMessages();
  }
};
O$.resetFormValidationById = function(formId) {
  var form = O$(formId);
  if (form) O$.resetFormValidation(form);
};

O$.resetEnclosingFormValidation = function(element) {
  var form = O$.getParentNode(element, "FORM");
  if (form) {
    O$.resetFormValidation(form);
  }
}

O$._resetValidationRecursive = function(element) {
  var children = element.childNodes;
  if (children && children.length > 0) {
    for (var i = 0; i < children.length; i++) {
      var child = children[i];
      if (O$.notEmpty(O$.getValidators(child)) || (O$.getMessages(child) && O$.getMessages(child).length > 0)) {
        O$.getMessages(child).length = 0;
      }
      O$._resetValidationRecursive(child)
    }
  }
}

O$._isComponentHasPresentation = function(componentId) {
  if (O$._isPresentationExistsForAllComponents()) {
    return true;
  }
  var componentsWithPresentation = O$._getComponentsWithPresentation();
  for (var i = 0; i < componentsWithPresentation.length; i++) {
    if (componentId == componentsWithPresentation[i]) {
      return true;
    }
  }
  return false;
}

O$._scheduleUpdateMessagesPosition = function() {
  setTimeout(function() {
    O$.updateClientMessagesPosition();
    O$._scheduleUpdateMessagesPosition();
  }, 150);
}

O$.invokeOnce(O$._scheduleUpdateMessagesPosition, "O$._scheduleUpdateMessagesPosition");