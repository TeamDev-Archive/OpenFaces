/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2010, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

// ================================== PUBLIC API FUNCTIONS

O$.extend(O$, {
  AjaxComponent: O$.createClass(null, {
    constructor: function(render, params) {
      this.render = render;
      if (params)
        O$.extend(this, params);
    },

    run: function() {
      O$._ajaxReload(this.render, this);
    }
  }),

  _initAjax: function(id, render, params) {
    function initComponent() {
      var component = O$(id);
      if (!component) {
        setTimeout(function() {
          initComponent();
        }, 100);
        return;
      }
      O$.extend(component, new O$.AjaxComponent(render, params));
    }

    if (O$(id))
      initComponent();
    else
      setTimeout(function() {
        if (O$(id))
          initComponent();
        else
          O$.addLoadEvent(function() {
            initComponent();
          });
      }, 1);
  }

});

O$.ajax = {
  /**
   * @param source - (optional) string id or a reference to the element that invokes this request
   * @param event - (optional) event object that triggered this request
   * @param options - (optional) is an object, which the following fields:
   *    - render - (required) a space separated list of component client id(s) that should be reloaded
   *    - execute - (optional) a space separated list of component client id(s) for components whose processDecodes->...->processUpdates phases should be invoked in addition to the component(s) being reloaded
   *    - onajaxstart - (optional) the function that should be invoked before ajax request is started
   *    - onajaxend - (optional) the function that should be invoked when ajax request is fully processed
   *    - onerror - (optional) the function that should be invoked when ajax request fails to complete successfully for some reason
   *    - listener - (optional) server action listener in the form of EL, which should be executed during this Ajax request. It is written in a convention BeanName.functionName, similar to the listener attribute of <o:ajax> tag, though without the #{ and } parts.
   *    - immediate - (optional) true means that the action should be executed during Apply Request Values phase, rather than waiting until the Invoke Application phase
   *    - params - (optional) an object containing the additional request parameters
   */
  request: function(source, event, options) {
    var args = options ? options : {};

    args.actionComponent = source && typeof source != "string" ? source.id : source;
    var render = options.render ? options.render.split(" ") : undefined;
    args.execute = options.execute ? options.execute.split(" ") : undefined;
    args.onajaxstart = options.onajaxstart;
    args.onajaxend = options.onajaxend;
    args.onerror = options.onerror;
    args.listener = options.listener;
    args.immediate = options.immediate;
    if (options.params) {
      args.additionalParams = [];
      for (var param in options.params) {
        var value = options.params[param];
        args.additionalParams.push([param, value]);
      }
    }

    O$._ajaxReload(render, args);
  }
};


// ================================== IMPLEMENTATION


O$.AJAX_REQUEST_MARKER = "_openFaces_ajax";
O$.UPDATE_PORTIONS_SUFFIX = "_of_ajax_portions";
O$.SUBMIT_PARAMS_SUFFIX = "_of_ajax_submit_params";
O$.PARAM_RENDER = "_of_render";
O$.CUSTOM_JSON_PARAM = "_of_customJsonParam";
O$.SUBMITTED_COMPONENT_IDS = "_of_execute";
O$.ACTION_LISTENER = "_of_actionListener";
O$.ACTION = "_of_action";
O$.ACTION_COMPONENT = "_of_actionComponent";
O$.IMMEDIATE = "_of_immediate";

O$.TAG_AJAX_UPDATABLE = "updatable";
O$.TAG_AJAX_SCRIPT = "script";
O$.TAG_AJAX_CSS = "css";
O$.TAG_AJAX_SESSION_EXPIRED = "session_expired";
O$.TAG_AJAX_SESSION_EXPIRED_LOCATION = "session_expired_location";
O$.TAG_AJAX_EXCEPTION = "ajax_exception";
O$.TAG_AJAX_EXCEPTION_MESSAGE = "ajax_exception_message";
O$.TAG_AJAX_STYLE = "style";

O$.UPDATE_TYPE_SIMPLE = "simple";
O$.UPDATE_TYPE_PORTION = "portion";
O$.UPDATE_TYPE_STATE = "state";

O$.UPDATE_TYPE_INITIALIZATION = "initialization";

O$.TEXT_RESPONSE_PREFIX = "_openfaces_ajax_response_prefix_";
O$.TEXT_RESPONSE_SUFFIX = "_openfaces_ajax_response_suffix_";

window.OpenFaces.Ajax = {
  Page: {},
  Components: []
};

O$.setCommonAjaxEventHandler = function(eventName, func) {
  if (!eventName || !func)
    return;

  //  if (document.__commonAjaxEventHandlerInitialized && document.__commonAjaxEventHandlerInitialized[eventName])
  //    return;

  if (!OpenFaces.Ajax.Page[eventName]) {
    OpenFaces.Ajax.Page[eventName] = func;
  }
  else {
    var oldEventHandlerFunction = OpenFaces.Ajax.Page[eventName];
    OpenFaces.Ajax.Page[eventName] = function(event) {
      if (oldEventHandlerFunction(event) !== false)
        func(event);
    };
  }

  if (!document.__commonAjaxEventHandlerInitialized) {
    document.__commonAjaxEventHandlerInitialized = [];
    document.__commonAjaxEventHandlerInitialized[eventName] = true;
  }

  document.__commonAjaxEventHandlerInitialized[eventName] = true;
}


O$.setComponentAjaxEventHandler = function(eventName, func, componentId) {
  if (!eventName || !func || !componentId)
    return;

  //  if ((document.__componentsAjaxEventHandlerInitialized &&
  //       document.__componentsAjaxEventHandlerInitialized[componentId] &&
  //       document.__componentsAjaxEventHandlerInitialized[componentId][eventName]))
  //    return;


  if (!OpenFaces.Ajax.Components[componentId]) {
    OpenFaces.Ajax.Components[componentId] = [];
  }

  if (!OpenFaces.Ajax.Components[componentId][eventName]) {
    OpenFaces.Ajax.Components[componentId][eventName] = func;
  } else {
    var oldComponentEventHandlerFunction = OpenFaces.Ajax.Components[componentId][eventName];
    OpenFaces.Ajax.Components[componentId][eventName] = function(event) {
      if (oldComponentEventHandlerFunction(event)) {
        func(event);
      }
    };
  }

  if (!document.__componentsAjaxEventHandlerInitialized) {
    document.__componentsAjaxEventHandlerInitialized = [];
    document.__componentsAjaxEventHandlerInitialized[componentId] = [];
  }

  if (!document.__componentsAjaxEventHandlerInitialized[componentId]) {
    document.__componentsAjaxEventHandlerInitialized[componentId] = [];
  }

  document.__componentsAjaxEventHandlerInitialized[componentId][eventName] = true;
}


O$.reloadPage = function(loc) {
  window.location = loc;
}

/**
 * render - components to which an ajax request is being made
 * agrs - (optional) is an object, which consist of:
 *    onajaxend - (optional) the function that should be invoked when ajax request is fully processed
 *    execute - (optional) array of clientIds for components whose processDecodes->...->processUpdates phases should be invoked in addition to the component being reloaded
 *    onerror - (optional) the function that should be invoked when ajax request fails to complete successfully for some reason
 *    onajaxstart - (optional) the function that should be invoked before ajax request is started
 *    immediate - (optional) true means that the action should be executed during Apply Request Values phase, rather than waiting until the Invoke Application phase
 */
O$._ajaxReload = function(render, args) {
  if (!args) args = {};
  var params = args.additionalParams ? args.additionalParams : [];
  var ids = [];
  if (render instanceof Array) {
    ids = render;
  } else {
    ids[0] = render;
  }
  args.additionalParams = params;
  if (!args.delay)
    O$.sendAjaxRequestIfNoFormSubmission(ids, args);
  else {
    var delayId = args.delayId;
    if (!delayId) {
      delayId = "";
      for (var i = 0, count = ids.length; i < count; i++) {
        delayId += ids[i];
        if (i < count - 1)
          delayId += ",";
      }
    }
    O$.invokeFunctionAfterDelay(function() {
      O$.sendAjaxRequestIfNoFormSubmission(ids, args);
    }, args.delay, delayId);
  }
}

O$.requestComponentPortions = function(componentId, portionNames, customJsonParam, portionProcessor, onerror) {
  if (!componentId)
    throw "componentId should be specified";
  if (! (portionNames instanceof Array))
    throw "portionNames should be specified as an array, but specified as: " + portionNames;
  if (!portionProcessor)
    throw "O$.requestComponentPortions: portionProcessor should be specified";
  var params = [];
  O$.sendAjaxRequestIfNoFormSubmission([componentId], {portionNames: portionNames, portionProcessor: portionProcessor,
    additionalParams: params, onerror: onerror, customJsonParam: customJsonParam});
}

/*
 Sends ajax request only if no form submission is initated in the same event handler
 (e.g. a submit button invoking an ajax request in the onclick handler)
 */
O$.sendAjaxRequestIfNoFormSubmission = function() {
  var ajaxArgs = arguments;
  O$._ajaxRequestScheduled = true;
  if (O$._ajax_request_processing) {
    if (!O$._ajax_requests_queue) {
      O$._ajax_requests_queue = [];
    }
    O$._ajax_requests_queue.push(ajaxArgs);
    return;
  }
  setTimeout(function() {
    O$._ajaxRequestScheduled = false;
    if (O$._isFormSubmissionJustStated()) {
      alert("Couldn't start Ajax request for component(s) \"" + ajaxArgs[0].join(",") + "\" because ordinary form " +
            "submission has also been started simultaneously.");
      return;
    }

    O$._ajax_request_processing = true;
    O$.sendAjaxRequest.apply(null, ajaxArgs);
  }, 1);
}

/**
 * render - components for which an ajax request is being made
 *
 * args fields:
 * portionNames - null value means that the whole component should be reloaded, otherwise it should be an array of component portion names
 * portionProcessor - must be specified if portionNames is specified
 * onajaxend - (optional) the function that should be invoked when ajax request is fully processed
 * execute - (optional) array of clientIds for components whose processDecodes->...->processUpdates phases should be invoked in addition to the component being reloaded
 * additionalParams - (optional) array of parameters those should be submitted in addition to form field data parameters
 * onerror - (optional) the function that should be invoked when ajax request fails to complete successfully for some reason
 * onajaxstart -
 * immediate -
 * customJsonParam -
 * listener - (optional) server action in the form of EL, which should be executed during this ajax request
 * actionComponent - (optional) client id of a component from which this action is initiated (e.g. actual for a button in a table which needs current row's data in the action)
 *
 */
O$.sendAjaxRequest = function(render, args) {
  if (document.__sessionHasExpired
          && document.__componentsAjaxEventHandlerInitialized
          && render.every(function(element) {
              return (document.__componentsAjaxEventHandlerInitialized[element]);
            })
          && render.every(function(element) {
              return (document.__componentsAjaxEventHandlerInitialized[element]["onsessionexpired"]);
            })) {
    if (O$.processSessionExpiration(document.__sessionReloadLocation, render, null, true)) {
      if (args.onerror) {
        args.onerror();
      }
      O$._ajax_request_processing = false;
      return;
    }
  }

  if (document.__sessionHasExpired &&
      document.__commonAjaxEventHandlerInitialized &&
      document.__commonAjaxEventHandlerInitialized["onsessionexpired"]) {
    if (O$.processSessionExpiration(document.__sessionReloadLocation, render, null, true)) {
      if (args.onerror) {
        args.onerror();
      }
      O$._ajax_request_processing = false;
      return;
    }
  }

  if (!render.length) {
    O$.logError("O$.sendAjaxRequest: Array of components ids should be not empty");
  }
  var components = [];
  var i;
  for (i = 0; i < render.length; i++) {
    var id = render[i];
    var c = O$.byIdOrName(id);
    if (!c) {
      O$.logError("O$.sendAjaxRequest: couldn't find component with the specified id: \"" + id + "\"");
      return;
    }
    components.push(c);
  }

  //we need to fire validation for enclosing form (if there are no client validation support - just skip client validation (there will be server side validation only)
  //if form is not valid - no AJAX request at all
  //    if (O$._clientValidationSupport) {
  //      if (!O$.validateEnclosingForm(component)) {
  //        return;
  //      }
  //    }
  var form = O$.getParentNode(components[0], "FORM");
  O$.assert(form, "O$.sendAjaxRequest: Enclosing form not found for element with id: " + components[0].id);
  if (!components.every(function(element) {
    return (form == O$.getParentNode(element, "FORM"));
  })) {
    O$.logError("O$.sendAjaxRequest: Enclosing forms differ for components");
  }

  var ajaxObject = new O$.AjaxObject(render);
  ajaxObject._onajaxstart = args.onajaxstart;
  O$.requestStarted(ajaxObject);

  var paramsBuf = new O$.StringBuffer();
  O$.prepareFormParams(form, paramsBuf);
  paramsBuf.append("&");
  if (O$.prepareUpdates(paramsBuf, render, args.portionNames))
    paramsBuf.append("&");
  if (render != null) {
    var renderParams = "";
    for (var index = 0, componentsCount = render.length; index < componentsCount; index++) {
      if (renderParams != "")
        renderParams += ";";
      renderParams += render[index];
    }
    paramsBuf.append(O$.PARAM_RENDER).append("=").append(renderParams);
  }
  if (args.execute != null) {
    paramsBuf.append("&");
    var renderParam = "";
    for (var idx = 0, submittedComponentCount = args.execute.length; idx < submittedComponentCount; idx++) {
      if (renderParam != "")
        renderParam += ";";
      renderParam += args.execute[idx];
    }
    paramsBuf.append(O$.SUBMITTED_COMPONENT_IDS).append("=").append(renderParam);
  }
  if (args.additionalParams) {
    for (i = 0, additionalParamCount = args.additionalParams.length; i < additionalParamCount; i++) {
      var paramEntry = args.additionalParams[i];
      paramsBuf.append("&");
      paramsBuf.append(paramEntry[0]).append("=").append(paramEntry[1]);
    }
  }
  if (args.listener) {
    paramsBuf.append("&").append(O$.ACTION_LISTENER).append("=").append(args.listener);
  }
  if (args._action) {
    paramsBuf.append("&").append(O$.ACTION).append("=").append(args._action);
  }

  if (args.actionComponent) {
    paramsBuf.append("&").append(O$.ACTION_COMPONENT).append("=").append(args.actionComponent);
  }
  if (args.immediate) {
    paramsBuf.append("&").append(O$.IMMEDIATE).append("=").append(args.immediate);
  }

  if (args.customJsonParam) {
    paramsBuf.append("&");
    paramsBuf.append(O$.CUSTOM_JSON_PARAM).append("=").append(args.customJsonParam);
  }
  paramsBuf.append("&");
  paramsBuf.append(O$.AJAX_REQUEST_MARKER).append("=").append("true");


  if (args.portionProcessor) {
    ajaxObject._customProcessor = args.portionProcessor;
  }
  ajaxObject._ajaxFailedProcessor = args.onerror;

  ajaxObject._request.onreadystatechange = O$.getEventHandlerFunction("_processResponse", null, ajaxObject);
  ajaxObject._completionCallback = args.onajaxend;
  ajaxObject._requestedRender = render;

  var url = form.action;
  ajaxObject._request.open("POST", url, true);
  ajaxObject._request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
  ajaxObject._request.setRequestHeader("If-Modified-Since", "Sat, 1 Jan 2000 00:00:00 GMT");
  // this if-modified-since header is required for IE not to cache response (vital for Portlets)
  var params = paramsBuf.toString();
  ajaxObject._request.send(params);
  ajaxObject._params = params;
  ajaxObject._url = url;

  O$.saveScrollPositionIfNeeded();
}

O$._ajaxRequestsInProgress = 0;

O$.requestStarted = function(ajaxObject) {
  O$._ajaxRequestsInProgress++;

  for (var i = 0; i < ajaxObject._targetIds.length; i++) {
    var targetId = ajaxObject._targetIds[i];
    var ajaxSettingsForComponent = OpenFaces.Ajax.Components[targetId];
    if (ajaxSettingsForComponent && ajaxSettingsForComponent.onajaxstart) {
      var ajaxstartEvent = O$.createEvent("ajaxstart");
      try {
        ajaxSettingsForComponent.onajaxstart(ajaxstartEvent);
      } catch(ex) {
        O$.requestFinished(ajaxObject);
        throw ex;
      }

    }
  }

  if (OpenFaces.Ajax.Page.onajaxstart) {
    var ajaxstartEvent = O$.createEvent("ajaxstart");

    try {
      OpenFaces.Ajax.Page.onajaxstart(ajaxstartEvent);
    } catch(ex) {
      O$.requestFinished(ajaxObject);
      throw ex;
    }

  }
  var ajaxstartEventLocal = O$.createEvent("ajaxstart");
  if (ajaxObject._onajaxstart) {
    try {
      ajaxObject._onajaxstart(ajaxstartEventLocal);
    } catch(ex) {
      O$.requestFinished(ajaxObject);
      throw ex;
    }
  }

  if (O$._ajaxRequestsInProgress == 1) {
    O$.showAjaxProgressMessage();
  }
};

O$.requestFinished = function(ajaxObject) {
  O$._ajaxRequestsInProgress--;
  for (var i = 0; i < ajaxObject._targetIds.length; i++) {
    var targetId = ajaxObject._targetIds[i];
    var ajaxSettingsForComponent = OpenFaces.Ajax.Components[targetId];
    if (ajaxSettingsForComponent && ajaxSettingsForComponent.onajaxend) {
      var ajaxendEvent = O$.createEvent("ajaxend");

      try {
        ajaxSettingsForComponent.onajaxend(ajaxendEvent);
      } catch(ex) {
        if (!ajaxObject._clientSideException) {
          ajaxObject._clientSideExceptions = [];
        }
        ajaxObject._clientSideExceptions.push(ex);
      }

    }
  }

  if (OpenFaces.Ajax.Page.onajaxend) {
    var ajaxendEvent = O$.createEvent("ajaxend");

    try {
      OpenFaces.Ajax.Page.onajaxend(ajaxendEvent);
    } catch(ex) {
      if (!ajaxObject._clientSideException) {
        ajaxObject._clientSideExceptions = [];
      }
      ajaxObject._clientSideExceptions.push(ex);
    }

  }

  if (O$._ajax_requests_queue && ajaxObject._clientSideExceptions) {
    if (ajaxObject._clientSideExceptions && ajaxObject._clientSideExceptions.length >= 0) {
      var exception = ajaxObject._clientSideExceptions.shift();
      throw exception;
    }
  }

  var newRequestStarted = false;

  if (O$._ajax_requests_queue) {
    var ajaxArgs = O$._ajax_requests_queue.shift();

    if (ajaxArgs) {
      O$._ajax_request_processing = true;
      O$.sendAjaxRequest.apply(null, ajaxArgs);
      newRequestStarted = true;
    }
  }


  if (O$._ajaxRequestsInProgress == 0) {
    O$.hideAjaxProgressMessage();
    O$.restoreScrollPositionIfNeeded();
  }

  setTimeout(function() {
    if (!newRequestStarted)
      O$._ajax_request_processing = false;
  }, 1);


  if (ajaxObject._clientSideExceptions && ajaxObject._clientSideExceptions.length >= 0) {
    var exception = ajaxObject._clientSideExceptions.shift();
    throw exception;
  }
};

O$.setAjaxCleanupRequired = function(ajaxCleanupRequired) {
  document._ajaxCleanupRequired = ajaxCleanupRequired;
};

O$.setAjaxMessageHTML = function(messageHTML, horizAlignment, vertAlignment, transparency, opacityTransitionPeriod, blockingLayer) {
  if (document._ajaxInProgressMessage) {
    return;
  }
  if (O$._ajaxRequestsInProgress > 0)
    O$.showAjaxProgressMessage();  

  var msg = document.createElement("div");
  msg._horizAlignment = horizAlignment || O$.RIGHT;
  msg._vertAlignment = vertAlignment || O$.TOP;
  var simulateFixedPos = true;//O$.isExplorer();
  msg.style.position = simulateFixedPos ? "absolute" : "fixed";
  msg.style.zIndex = 1000;
  msg.style.left = 0;
  msg.style.top = 0;
  msg.style.visibility = "hidden";
  msg.innerHTML = messageHTML;
  msg._opacity = 1 - transparency;
  msg._opacityTransitionPeriod = opacityTransitionPeriod;
  document._ajaxInProgressMessage = msg;

  if (blockingLayer) {
    msg._blockingLayer = function() {
      var div = document.createElement("div");
      div.className = blockingLayer.className ? blockingLayer.className : "";
      div.style.visibility = "hidden";
      var opacity = blockingLayer.transparency != undefined ? 1 - blockingLayer.transparency : 0;
      div._opacity = opacity;
      div._opacityTransitionPeriod = blockingLayer.transparencyTransitionPeriod != undefined ? blockingLayer.transparencyTransitionPeriod : 150;
      O$.setOpacityLevel(div, 0);
      div.updatePos = function() {
        var rect = O$.getVisibleAreaRectangle();
        div.style.zIndex = O$.getElementZIndex(msg) - 1;
        O$.setElementBorderRectangle(div, rect);
      };
      return div;
    }();
  }

  O$.addLoadEvent(function() {
    var prnt = O$.getDefaultAbsolutePositionParent();

    prnt.appendChild(msg);
    if (document._ajaxInProgressMessage._blockingLayer)
      prnt.appendChild(document._ajaxInProgressMessage._blockingLayer);
    setTimeout(
            function() {
              if (msg.style.visibility == "hidden") {
                msg.style.display = "none";
                msg.style.visibility = "";
              }
              if (msg._blockingLayer && msg._blockingLayer.style.visibility == "hidden") {
                msg._blockingLayer.style.display = "none";
                msg._blockingLayer.style.visibility = "";
              }
            }, 1000);
  });
};

O$.showAjaxProgressMessage = function() {
  var msg = document._ajaxInProgressMessage;
  if (!msg)
    return;
  var simulateFixedPos = true;//O$.isExplorer();
  if (simulateFixedPos)
    O$.updateAjaxInProgressMessagePos();

  function show(elt, opacity, opacityTransitionPeriod) {
    O$.setOpacityLevel(elt, 0);
    if (elt.style.visibility == "hidden")
      elt.style.visibility = "";
    else
      elt.style.display = "";
    O$.runTransitionEffect(elt, "opacity", opacity, opacityTransitionPeriod, 10);
  }

  show(msg, msg._opacity, msg._opacityTransitionPeriod);
  if (msg._blockingLayer) {
    O$.setOpacityLevel(msg._blockingLayer, 0);
    show(msg._blockingLayer, msg._blockingLayer._opacity, msg._blockingLayer._opacityTransitionPeriod);
    O$.updateAjaxInProgressMessagePos();
  }

  if (document.body.style.cursor != "progress") {
    document.body.oldCursor = document.body.style.cursor;
  }
  document.body.style.cursor = "progress";

  if (simulateFixedPos) {
    O$.addEventHandler(window, "scroll", O$.updateAjaxInProgressMessagePos);
    O$.addEventHandler(window, "resize", O$.updateAjaxInProgressMessagePos);
  }
};

O$.hideAjaxProgressMessage = function() {
  var msg = document._ajaxInProgressMessage;
  if(!msg) return;
  var simulateFixedPos = true;//O$.isExplorer();
  if (simulateFixedPos) {
    O$.removeEventHandler(window, "scroll", O$.updateAjaxInProgressMessagePos);
    O$.removeEventHandler(window, "resize", O$.updateAjaxInProgressMessagePos);
  }
  document.body.style.cursor = document.body.oldCursor;
  msg.style.display = "none";
  if (msg._blockingLayer)
    msg._blockingLayer.style.display = "none";
};

O$.updateAjaxInProgressMessagePos = function() {
  var msg = document._ajaxInProgressMessage;
  if (msg.style.display == "none") {
    // replace hiding with "visibility" instead of "display" to allow element size measurement,
    // but don't actually show yet to prevent early misplaced display 
    msg.style.visibility = "hidden";
    msg.style.display = "";
  }

  O$.alignPopupByElement(msg, window, msg._horizAlignment, msg._vertAlignment, 0, 0);
  if (msg._blockingLayer)
    msg._blockingLayer.updatePos();
};

O$._pageReloadConfirmation = function(confirmationId, location) {
  var confirmation = O$(confirmationId);
  if (document._ajaxInProgressMessage._blockingLayer)
    O$.correctElementZIndex(confirmation, document._ajaxInProgressMessage._blockingLayer);
  confirmation.runConfirmedFunction(function(){
    O$.reloadPage(location);
  });
};

// O$.AjaxObject class
O$.AjaxObject = function(render) {
  this._targetIds = render;
  this._mode = undefined;
  this._loc = undefined;
  if (window.XMLHttpRequest) {
    this._request = new XMLHttpRequest();
  } else {
    this._request = new ActiveXObject("Microsoft.XMLHTTP");
    // todo: catch exception here and report some meaningful error
  }

  this._processResponse = function() {
    var request = this._request;
    if (request.readyState != 4)
      return;

    var errorMessage = request.getResponseHeader("Error-Message");
    if (errorMessage) {
      O$.processErrorDuringAjaxRequest(errorMessage, this._targetIds, this);
    }

    var requestStatus;
    try {
      requestStatus = request.status;
    }
    catch(exception) {
      if (exception.name == "NS_ERROR_NOT_AVAILABLE") {
        alert("An attempt to connect to the server failed. Please, check your network connection.");
        O$.requestFinished(this);
        return;
      }
    }

    if (requestStatus == 0) {
      alert("An attempt to connect to the server failed. Please, check your network connection.");
      O$.requestFinished(this);
      return;
    }

    if (requestStatus == 12029) {
      alert("An attempt to connect to the server failed. Please, check your network connection.");
      O$.requestFinished(this);
      return;
    }
    if (requestStatus == 12030) {
      alert("The connection with the server has been terminated. Please, check your network connection.");
      O$.requestFinished(this);
      return;
    }
    if (requestStatus == 12031) {
      alert("The connection with the server has been reset. Please, check your network connection.");
      O$.requestFinished(this);
      return;
    }

    if (requestStatus != 200) {
      O$.requestFinished(this);

      if (requestStatus == 500) {
        alert("Error while performing Ajax request: 500 (internal server error). See server logs for details.");
      } else if (requestStatus == 302) { // by the spec, 302 responses should be handled transparently by XMLHttpRequest
        alert("Error while performing Ajax request: 302 (this usually means a bug in the browser). If you're using Opera 9.0.x, please upgrade to Opera 9.1 or higher.");
      } else {
        alert("Error while performing Ajax request: \n" + requestStatus);
      }
      // todo: fire onerror event and invoke error handler
      return;
    }

    //test if there were no validation error during processing request on the server-side
    if (O$.validationErrorDuringProcessing(request)) {
      O$.requestFinished(this);
      O$.submitByIds(this._targetIds);
      return;
    }


    try {
      //set flag - ajax update in progress
      document._of_page_is_already_initialized = true;
      if (request.responseXML) {
        var responseXML = request.responseXML;
        this._jsIncludes = responseXML.getElementsByTagName(O$.TAG_AJAX_SCRIPT);
        this._updatables = responseXML.getElementsByTagName(O$.TAG_AJAX_UPDATABLE);
        this._styles = responseXML.getElementsByTagName(O$.TAG_AJAX_STYLE);
        this._cssFiles = responseXML.getElementsByTagName(O$.TAG_AJAX_CSS);
      }
      if (!this._updatables || this._updatables.length == 0) {
        var responseText = request.responseText;
        var startIndex = responseText.indexOf(O$.TEXT_RESPONSE_PREFIX);
        if (startIndex == -1) {
          O$.requestFinished(this);
          var errorMessage = "Error occured on the server while processing Ajax request. Check server logs. (response length: " + responseText.length + ")";
          //          O$.log("<b>" + errorMessage + "<br/>Original parameter list: " + this._params + "<br/>Original URL: " + this._url + "<br/>Response follows:><br/></b><xmp>" + responseText + "</xmp>");

          alert(errorMessage);
          return;
        }
        startIndex += O$.TEXT_RESPONSE_PREFIX.length;
        var endIndex = responseText.lastIndexOf(O$.TEXT_RESPONSE_SUFFIX);
        if (endIndex == -1) {
          O$.requestFinished(this);
          alert("Error while performing Ajax request: illegal response - couldn't find data end marker. Response length: " + responseText.length);
          return;
        }
        var clippedResponseText = responseText.substring(startIndex, endIndex);
        var responseObj;
        eval("responseObj = " + clippedResponseText);
        this._jsIncludes = responseObj[O$.TAG_AJAX_SCRIPT];
        this._updatables = responseObj[O$.TAG_AJAX_UPDATABLE];
        this._styles = responseObj[O$.TAG_AJAX_STYLE];
        this._cssFiles = responseObj[O$.TAG_AJAX_CSS];
        this._sessionExpired = responseObj[O$.TAG_AJAX_SESSION_EXPIRED];
        this._sessionExpiredLocation = responseObj[O$.TAG_AJAX_SESSION_EXPIRED_LOCATION];
        this._exception = responseObj[O$.TAG_AJAX_EXCEPTION];
        this._exceptionMessage = responseObj[O$.TAG_AJAX_EXCEPTION_MESSAGE];
      }

      var serverException = this._exception ? this._exception[0].value : undefined;
      if (serverException) {
        var exceptionMessage = serverException + ": " + this._exceptionMessage[0].value;
        O$.processErrorDuringAjaxRequest(exceptionMessage, this._targetIds, this);
      }

      O$.processStylesIncludes(this._styles);
      O$.processCSSFilesIncludes(this._cssFiles);
      //substitute document.write method to prevent error if foreign js library contains code that executes on library
      //loading and invokes document.write method to render some HTML. Otherwise document.write() method rewrites all
      //currently rendered in page content with content passed to it on AJAX response
      O$.substituteDocumentWrite();
      try {
        O$.processJSIncludes(this._jsIncludes);
      } catch (e) {
        O$.restoreDocumentWrite();
        throw e;
      }
    } catch (e) {
      O$.requestFinished(this);
      throw e;
    }

    var loc = request.getResponseHeader("Location");
    var expiredMessage = request.getResponseHeader("Ajax-Expired");

    if (!expiredMessage && this._sessionExpired) {
      expiredMessage = this._sessionExpired[0].value;
    }

    if (!loc && this._sessionExpiredLocation) {
      loc = this._sessionExpiredLocation[0].value;
    }


    if (expiredMessage) {
      this._mode = "Expiration-Handling";
      this._loc = loc;
      document.__sessionHasExpired = true;
      document.__sessionReloadLocation = loc;
    }

    this._processUpdatesWhenReady();


    document._of_page_is_already_initialized = null;
  };
  this._processUpdatesWhenReady = function() {
    try {
      var jsIncludes = this._jsIncludes;
      if (!O$.areRequiredLibrariesLoaded(jsIncludes)) {
        setTimeout(O$.getEventHandlerFunction("_processUpdatesWhenReady", null, this), 50);
        return;
      }
      //restore document.write() method to original. used in pair with O$.substituteDocumentWrite() method.
      O$.restoreDocumentWrite();

      this._processUpdates();
    } catch (e) {
      if (!this._clientSideExceptions) {
        O$.requestFinished(this);
      }
      throw e;
    }

    if (!this._delayedSimpleUpdate)
      O$.requestFinished(this);


    this._jsIncludes = undefined;
    this._updatables = undefined;
    this._styles = undefined;
    this._cssFiles = undefined;
    this._request = false;
  };

  this._processUpdates = function() {
    var updatables = this._updatables;
    if (!updatables || updatables.length == 0)
      return;
    var rtLibrary = undefined;

    // Cache state update part
    var updateStateId;
    var updateStateHTML;
    for (var i = 0; i < updatables.length; i++) {
      var upd = updatables[i];
      var updType = upd.tagName ? upd.getAttribute("type") : upd.type;

      if (updType == O$.UPDATE_TYPE_STATE) {
        var updId = upd.tagName ? upd.getAttribute("id") : upd.id;
        var updHTML = upd.tagName ? upd.getAttribute("value") : upd.value;
        if (O$.isSafari()) {  //unescape ampersands for Safari
          updHTML = updHTML.replace(/&#38;/g, "&");
          updHTML = updHTML.replace(/&amp;/g, "&");
        }
        updateStateId = updId;
        updateStateHTML = updHTML;
        break;
      }
    }

    var simpleUpdate = false;
    for (var i = 0; i < updatables.length; i++) {
      var upd = updatables[i];
      var updType = upd.tagName ? upd.getAttribute("type") : upd.type;
      var updId = upd.tagName ? upd.getAttribute("id") : upd.id;
      var updHTML = upd.tagName ? upd.getAttribute("value") : upd.value;
      var updScripts = upd.tagName ? upd.getAttribute("scripts") : upd.scripts;
      var updData = upd.tagName ? upd.getAttribute("data") : upd.data;

      // runtime generated library with initialization scripts for all components presented on page
      if (updType == O$.UPDATE_TYPE_INITIALIZATION) {
        rtLibrary = updHTML;
      }
      if (O$.isSafari()) {  // unescape ampersands for Safari
        updHTML = updHTML.replace(/&#38;/g, "&");
        updHTML = updHTML.replace(/&amp;/g, "&");
      }

      if (updType == O$.UPDATE_TYPE_SIMPLE) {
        if (this._mode == "Expiration-Handling") {
          this._processUpdateOnExpirationOrError(updId, updHTML, updScripts, updateStateHTML);
        } else {
          this._processSimpleUpdate(updId, updHTML, updScripts, updateStateHTML);
        }
        simpleUpdate = true;
      } else if (updType == O$.UPDATE_TYPE_PORTION) {
        if (this._mode == "Expiration-Handling") {
          this._processUpdateOnExpirationOrError(updId, updHTML, updScripts, updateStateHTML);
        } else {
          this._processPortionUpdate(updId, updHTML, updScripts, updData);
        }
      }
    }

    if (!simpleUpdate && updateStateHTML && !(this._mode == "Expiration-Handling")) {
      O$.processStateUpdate(updateStateId, updateStateHTML)
    }

    if (rtLibrary) {
      O$.processJSInclude(rtLibrary);
    }

    if (this._completionCallback)
      this._completionCallback(this._requestedRender);
    if (O$.Ajax.onajaxend)
      O$.Ajax.onajaxend();
  };

  this._processUpdateOnExpirationOrError = function (updId, updHTML, updScripts, updateStateHTML) {
    if (document.__sessionExpirationUpd) {
      return;
    }
    var tempDiv = document.createElement("div");
    tempDiv.innerHTML = updHTML;

    var newElements = [];
    for (var childIndex = 0, childCount = tempDiv.childNodes.length; childIndex < childCount; childIndex++) {
      var newElement = tempDiv.childNodes[childIndex];
      O$.ajax_pushElementsWithId(newElements, newElement);
    }

    for (var childIndex = 0, childCount = newElements.length; childIndex < childCount; childIndex++) {
      var newElement = newElements[childIndex];
      O$.assert(newElement.id, "_processSimpleUpdate: newElement without id encountered");
      var parent = document.body;
      parent.insertBefore(newElement, parent.lastChild);


      if (O$.isOpera()) { // needed for Opera8.5 only (JSFC-1170)
        var oldClassName = parent.className;
        parent.className = parent.className + " _non_existing_class_name_of__123_";
        parent.className = oldClassName;
      }

    }

    //todo: insert runtime js library invokation with all initial scripts (instead of scripts passing to ajax response) here.
    //todo: replace O$.executeScripts() method with runtime js library invokation
    O$.executeScripts(updScripts);

    O$.processStateUpdate(updId, updateStateHTML);

    if (this._mode == "Expiration-Handling") {
      O$.processSessionExpiration(this._loc, this._targetIds, this, false);
      O$.requestFinished(this);
    }

    document.__sessionExpirationUpd = true;
  };
  this._processSimpleUpdate = function(updId, updHTML, updScripts, updateStateHTML) {

    var replacedElement = O$(updId);

    if (replacedElement.onComponentUnload) {
      var delayedUpdate = replacedElement.onComponentUnload();
      if (delayedUpdate) {
        replacedElement.onComponentUnload = null;
        var this_ = this;
        var delayedCompletionCallback = this._completionCallback;
        var delayedCompletionCallbackComponentId = this._requestedRender;
        var delayedSimpleUpdate = this._processSimpleUpdate;
        this._delayedSimpleUpdate = true;
        setTimeout(function () {
          delayedSimpleUpdate.apply(this_, [updId, updHTML, updScripts, updateStateHTML]);
          if (delayedCompletionCallback)
            delayedCompletionCallback(delayedCompletionCallbackComponentId);
          O$.requestFinished(this_);
        }, 1);
        this._completionCallback = null;
        return;
      }
    }

    var tempDiv = O$.replaceDocumentElements(updHTML);

    //todo: insert runtime js library invokation with all initial scripts (instead of scripts passing to ajax response) here.
    //todo: replace O$.executeScripts() method with runtime js library invokation
    O$.executeScripts(updScripts);

    O$.processStateUpdate(updId, updateStateHTML);
  };

  this._processPortionUpdate = function(portionName, portionHTML, portionScripts, portionDataStr) {
    var componentId = this._targetIds;
    var component = O$(componentId);
    O$.assert(component, "Couldn't find component by id: " + componentId);
    var portionData = portionDataStr ? eval("(" + portionDataStr + ")") : null;
    this._customProcessor(component, portionName, portionHTML, portionScripts, portionData);
  };

};

/*
 Replaces all elements in htmlPortion if they have an appropriate counterpart with the same Id in the document. All
 elements that couldn't be placed into the document (due to lack of id or a lack of an element with the same id in the
 document) are retained in the returned "div" element. The "div" element itself is just a temporary container and is
 not a part of HTML passed as a parameter.
 */
O$.replaceDocumentElements = function(htmlPortion, allowElementsWithNewIds) {
  var tempDiv = document.createElement("div");
  try {
    tempDiv.innerHTML = htmlPortion;
  } catch (e) {
    O$.log("ERROR: O$.replaceDocumentElements: couldn't set innerHTML for tempDiv. error message: " + e.message + "; htmlPortion: " + htmlPortion);
//    O$.logError("O$.replaceDocumentElements: couldn't set innerHTML for tempDiv. error message: " + e.message + "; htmlPortion: " + htmlPortion);
    throw e;
  }

  var newElements = [];
  for (var i = 0, count = tempDiv.childNodes.length; i < count; i++) {
    var el = tempDiv.childNodes[i];
    O$.ajax_pushElementsWithId(newElements, el);
  }
  for (var childIndex = 0, childCount = newElements.length; childIndex < childCount; childIndex++) {
    var newElement = newElements[childIndex];
    O$.assert(newElement.id, "_processSimpleUpdate: newElement without id encountered");
    var elementId = newElement.id;
    var oldElement = O$(elementId);
    if (!oldElement) {
      if (!allowElementsWithNewIds) {
        O$.logError("Couldn't find component to replace: " + elementId + "; incoming HTML for this component: " + newElement.innerHTML);
      }
      continue;
    }
    var parent = oldElement.parentNode;

    if (O$.isExplorer()) {
      if (typeof oldElement._cleanUp == "function") {
        oldElement._cleanUp();
      }
      if (document._ajaxCleanupRequired)
        setTimeout(function() {
//          O$.destroyAllFunctions(oldElement);
        }, 1);
    }

    parent.replaceChild(newElement, oldElement);

    if (O$.isOpera()) { // needed for Opera8.5 only (JSFC-1170)
      var oldClassName = parent.className;
      parent.className = parent.className + " _non_existing_class_name_of__123_";
      parent.className = oldClassName;
    }

    oldElement = null;
  }
  return tempDiv;
}

O$.processSessionExpiration = function(loc, ajaxRender, ajaxObject, isHandlingWasDelayed) {

  if (ajaxRender) {
    if (ajaxRender.every(function(element) {
      return !!OpenFaces.Ajax.Components[element];
    })
            && ajaxRender.every(function(element) {
      return !!OpenFaces.Ajax.Components[ajaxRender].onsessionexpired;
    })) {
      var sessionexpiredEvent = O$.createEvent("sessionexpired");

      try {
        for (var i = 0; i < ajaxRender.length; i++) {
          var ajaxComponentId = ajaxRender[i];
          OpenFaces.Ajax.Components[ajaxComponentId].onsessionexpired(sessionexpiredEvent);
        }
      } catch(ex) {
        if (!isHandlingWasDelayed) {
          if (!ajaxObject._clientSideException) {
            ajaxObject._clientSideExceptions = [];
          }
          ajaxObject._clientSideExceptions.push(ex);
          return true;
        } else {
          O$._ajax_request_processing = false;
          throw ex;
        }
      }

      return true;
    }
  }

  if (OpenFaces.Ajax.Page.onsessionexpired) {
    var sessionexpiredEvent = O$.createEvent("sessionexpired");

    try {
      OpenFaces.Ajax.Page.onsessionexpired(sessionexpiredEvent);
    } catch(ex) {
      if (!isHandlingWasDelayed) {
        if (!ajaxObject._clientSideException) {
          ajaxObject._clientSideExceptions = [];
        }
        ajaxObject._clientSideExceptions.push(ex);

        return true;
      } else {
        O$._ajax_request_processing = false;
        throw ex;
      }
    }

    return true;
  }

  return false;
}

O$.processErrorDuringAjaxRequest = function(errorMessage, ajaxRender, ajaxObject) {
  if (ajaxRender) {
    if (ajaxRender.every(function(element) {
      return (OpenFaces.Ajax.Components[element]);
    })
            && ajaxRender.every(function(element) {
      return (OpenFaces.Ajax.Components[ajaxRender].onerror);
    })) {
      var errorEvent = O$.createEvent("error");
      var result = false;
      try {
        result = true;
        for (var i = 0; i < ajaxRender.length; i++) {
          var ajaxComponentId = ajaxRender[i];
          result = result & OpenFaces.Ajax.Components[ajaxComponentId].onerror(errorEvent);
        }
      } catch(ex) {
        if (!ajaxObject._clientSideException) {
          ajaxObject._clientSideExceptions = [];
        }
        ajaxObject._clientSideExceptions.push(ex);
        O$.requestFinished(ajaxObject);
        return;
      }

      if (result) {
        O$.showDefaultAlertAfterException(errorMessage, ajaxObject);
      }
      return;
    }
  }

  if (OpenFaces.Ajax.Page.onerror) {
    var errorEvent = O$.createEvent("error");
    var onerrorResult = false;
    try {
      onerrorResult = OpenFaces.Ajax.Page.onerror(errorEvent);
    } catch(ex) {
      if (!ajaxObject._clientSideException) {
        ajaxObject._clientSideExceptions = [];
      }
      ajaxObject._clientSideExceptions.push(ex);
      O$.requestFinished(ajaxObject);
      return;
    }

    if (onerrorResult) {
      O$.showDefaultAlertAfterException(errorMessage, ajaxObject);
    }
    return;
  }

  O$.showDefaultAlertAfterException(errorMessage, ajaxObject);
}

O$.processStateUpdate = function(updId, updHTML) {
  if (!updHTML) return;

  var updatedElement = O$(updId);

  var prnt = updatedElement.parentNode;
  var tempDiv = document.createElement("div");
  tempDiv.id = "stateUpdateDiv";
  tempDiv.innerHTML = updHTML;

  var stateField = tempDiv.childNodes[0];
  O$.assert(stateField, "No state field found in updateable");
  O$.assert(tempDiv.childNodes.length == 1, "There should be only one element in the state field array");
  var stateFieldId = stateField.id;
  var stateOnPage = O$(stateFieldId);
  if (stateOnPage) {
    stateOnPage.parentNode.replaceChild(stateField, stateOnPage);
    stateOnPage.innerHTML = "";
  }
  else {
    // Try to add hidden field into updated element
    var tagName = updatedElement.tagName.toUpperCase();
    if (tagName == "TABLE")
    {
      var firstTD = O$.getFirstChildWithName(updatedElement, "TD");
      firstTD.appendChild(stateField);
    }
    else if (updatedElement.childNodes) // check that element already has children to filter one tag elements
    {
      updatedElement.appendChild(stateField);
    }
    else
    {
      prnt.appendChild(stateField);
    }
  }
  tempDiv.innerHTML = "";
}

O$.getFirstChildWithName = function(node, tagName) {
  tagName = tagName.toUpperCase();
  var children = node.childNodes;
  for (var i = 0; i < children.length; i++)
  {
    var childrenTagName = children[i].tagName;
    if (childrenTagName && tagName == childrenTagName.toUpperCase()) {
      return children[i];
    }
    var deepResult = O$.getFirstChildWithName(children[i], tagName);
    if (deepResult)
    {
      return deepResult;
    }
  }

  return null;
}

O$.ajax_pushElementsWithId = function(destElements, element) {
  if (!element)
    return;
  if (element.id) {
    destElements.push(element);
    return;
  }

  for (var childIndex = 0, childCount = element.childNodes.length; childIndex < childCount; childIndex++) {
    var subElement = element.childNodes[childIndex];
    O$.ajax_pushElementsWithId(destElements, subElement);
  }

}

O$.processJSIncludes = function(jsIncludes) {
  if (jsIncludes) {
    for (var i = 0; i < jsIncludes.length; i++) {
      var jsIncludeElement = jsIncludes[i];
      var include = jsIncludeElement.tagName ? jsIncludeElement.getAttribute("value") : jsIncludeElement.value;
      O$.processJSInclude(include);
    }
  }
}

O$.processJSInclude = function(jsInclude) {
  var found = O$.isLibraryLoaded(jsInclude);
  if (!found) {
    var newScript = document.createElement("script");
    newScript.type = "text/javascript";
    newScript.src = jsInclude;
    var head = document.getElementsByTagName("head")[0];
    head.appendChild(newScript);
  }
}
O$.processCSSFilesIncludes = function(cssFiles) {
  if (!cssFiles)
    return;
  var head = document.getElementsByTagName("head")[0];
  for (var i = 0; i < cssFiles.length; i++) {
    var cssFileElement = cssFiles[i];
    var cssFile = cssFileElement.tagName ? cssFileElement.getAttribute("value") : cssFileElement.value;
    var newCSSFile = document.createElement("link");
    newCSSFile.type = "text/css";
    newCSSFile.href = cssFile;
    newCSSFile.rel = "stylesheet";
    head.appendChild(newCSSFile);
  }
}

O$.processStylesIncludes = function(styles) {
  if (!styles)
    return;

  for (var i = 0; i < styles.length; i++) {
    var styleElement = styles[i];
    var rule = styleElement.tagName ? styleElement.getAttribute("value") : styleElement.value;
    O$.addCssRule(rule);
  }
}

O$.executeScripts = function(source) { // should not be invoked because all init scripts are moved to runtime generated js library (as for now) // todo: review -- is this really so currently?
  if (!source || source.length == 0) return;
  var idx1 = source.indexOf("<script");
  var result;
  if (idx1 > -1) {
    result = source.substring(idx1);
    idx1 = result.indexOf(">");
    idx1 += 1;

    var idx2 = result.indexOf("</script>");
    var script = result.substring(idx1, idx2);
    script = script.replace(/<!--/g, "");
    script = script.replace(/\/\/-->/g, "");
    try {
      window.eval(script);
    } catch (e) {
      alert("Exception '" + e.message + "' while executing script on Ajax request: \n" + script);
      throw e;
    }
    idx2 += "</script>".length;
    result = result.substring(idx2);
    O$.executeScripts(result);
  }
}

O$.prepareUpdates = function(buf, componentId, portionName) {
  if (!portionName || portionName.length <= 0) return false;

  buf.append(O$.UPDATE_PORTIONS_SUFFIX).append("=");
  for (var i = 0, count = portionName.length; i < count; i++) {
    var encodedPortionName = encodeURIComponent(portionName[i]);
    buf.append(O$.escapeSymbol(encodedPortionName, [","]));
    if (i < count - 1) {
      buf.append(",");
    }
  }
  return true;
}

O$.escapeSymbol = function(portionName, param) {
  var res = new O$.StringBuffer();
  for (var i = 0, count = portionName.length; i < count; i++) {
    var temp = portionName.charAt(i);
    if (temp == "\\") {
      res.append("\\");
    } else {
      var index = param.indexOf(temp);
      if (index != -1) {
        var fullCharCode = new String(param[index].charCodeAt() + 10000);
        res.append("\\" + fullCharCode.substr(1, fullCharCode.length));
        continue;
      }
    }
    res.append(temp);
  }
  return res.toString();
}

O$.prepareFormParams = function(form, buf) {
  var elements = form.elements;
  for (var i = 0, count = elements.length; i < count; i++) {
    var element = elements[i];
    var elementName = element.name;
    if (!elementName)
      continue;
    var elementType = element.type;
    if (elementType)
      elementType = elementType.toLowerCase();
    if (!elementType || elementType == "image" || elementType == "button" || elementType == "submit")
      continue;

    if (elementType == "checkbox" || elementType == "radio") {
      if (!element.checked)
        continue;
    }

    var paramName = encodeURIComponent(elementName);
    var paramValue = encodeURIComponent(element.value);
    buf.append(paramName).append("=").append(paramValue);
    if (i < count - 1) {
      buf.append("&");
    }
  }
}

O$.areRequiredLibrariesLoaded = function(libs) {
  if (!libs) return true;
  for (var i = 0; i < libs.length; i++) {
    var libElement = libs[i];
    var lib = libElement.tagName ? libElement.getAttribute("value") : libElement.value;
    var loaded = O$.isLibraryLoaded(lib);
    if (!loaded) return false;
  }
  return true;
}

O$.isLibraryLoaded = function(lib) {
  var result = eval("window['_of_loadedLibrary:" + lib + "']");
  return result;
}

O$.validationErrorDuringProcessing = function(request) {
  if (!request)
    return true;
  return request.responseText && request.responseText.indexOf("validation error") > -1;

}

O$.substituteDocumentWrite = function() {
  document._oldWrite = document.write;
  document._tempAjaxStr = "";
  document.write = function(str) {
    document._tempAjaxStr += str;
  };
}

O$.restoreDocumentWrite = function(placeHolderId) {
  if (document._tempAjaxStr.length > 0) {
    var div = document.createElement("div");
    div.innerHTML = document._tempAjaxStr;
    var childNodes = div.childNodes;
    var i;
    if (!placeHolderId) {
      for (i = 0; i < childNodes.length; i++) {
        document.body.appendChild(childNodes[i]);
      }
    } else {
      var placeholderEl = O$(placeHolderId);
      if (placeholderEl) {
        var parentEl = placeholderEl.parentNode;
        for (i = 0; i < childNodes.length; i++) {
          parentEl.insertBefore(childNodes[i], placeholderEl);
        }
        parentEl.removeChild(placeholderEl);
      }
    }
  }
  document.write = document._oldWrite;
  document._tempAjaxStr = undefined;
}

O$.updateViewId = function(viewId) {
  if (document.getElementsByName('javax.faces.ViewState')) {
    for (var facesViewStateFieldIndex = 0; facesViewStateFieldIndex < document.getElementsByName('javax.faces.ViewState').length; facesViewStateFieldIndex++) {
      if (document.getElementsByName('javax.faces.ViewState')[facesViewStateFieldIndex]) {
        document.getElementsByName('javax.faces.ViewState')[facesViewStateFieldIndex].value = viewId;
      }
    }
  }
}

O$.showDefaultAlertAfterException = function(errorMessage, ajaxObject) {
  alert("An error occured on the server.\nError message:\n\"" + errorMessage + "\".\nPlease see server logs for the full stacktrace.");
  O$.requestFinished(ajaxObject);
}

O$.destroyAllFunctions = function(elt) {
  if (!elt || elt.nodeName == "#text")
    return;

  var elementId = elt.id;

  // Break the potential circular references
  for (var member in elt) {
    if (member.indexOf("_") != 0 && member.indexOf("on") != 0)
      continue;
    if (!elt[member])
      continue;
    if (elementId && (typeof elt[member] === "function"))
      (function() {
        var e = O$(elementId);
        e[member] = null;
      })();
    else
      elt[member] = null;
  }

  var attachedEvents = O$._attachedEvents;
  if (elementId && attachedEvents) {
    var allAttachedEvents = attachedEvents[elementId];
    if (allAttachedEvents) {
      for (var eventsIndex = 0, eventCount = allAttachedEvents.length; eventsIndex < eventCount; eventsIndex++) {
        var eventObject = allAttachedEvents[eventsIndex];
        O$.removeEventHandler(elt, eventObject.eventName, eventObject.functionScript);
      }
      attachedEvents[elementId] = null;
    }
  }

  var childNodes = elt.childNodes;
  if (childNodes) {
    var length = childNodes.length;
    for (var index = 0; index < length; index++) {
      O$.destroyAllFunctions(childNodes[index]);
    }
  }
}