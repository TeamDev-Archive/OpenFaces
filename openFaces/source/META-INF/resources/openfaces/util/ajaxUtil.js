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

// ================================== PUBLIC API FUNCTIONS

O$.extend(O$, {
  AjaxComponent: O$.createClass(null, {
    constructor: function(render, params) {
      this.render = render;
      if (params)
        O$.extend(this, params);
    },

    run: function() {
      O$.Ajax._reload(this.render, this, this);
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

O$.setAjaxCleanupRequired = function(ajaxCleanupRequired) {
  document._ajaxCleanupRequired = ajaxCleanupRequired;
};

O$.ajax = {
  /**
   * @param source - (optional) string id or a reference to the element that invokes this request
   * @param event - (optional) event object that triggered this request
   * @param options - (optional) is an object, which the following fields:
   *    - render - (required) a space separated list of component client id(s) that should be reloaded
   *    - execute - (optional) a space separated list of component client id(s) for components whose processDecodes->...->processUpdates phases should be invoked in addition to the component(s) being reloaded
   *    - onajaxstart - (optional) the function that should be invoked before ajax request is started
   *    - onajaxend - (optional) the function that should be invoked when ajax request is fully processed
   *    - onsuccess - (optional) the function that should be invoked when ajax request completes without errors
   *    - onerror - (optional) the function that should be invoked when ajax request fails to complete successfully for some reason
   *    - listener - (optional) server action listener in the form of EL, which should be executed during this Ajax request. It is written in a convention BeanName.functionName, similar to the listener attribute of <o:ajax> tag, though without the # { and } parts.
   *    - immediate - (optional) true means that the action should be executed during Apply Request Values phase, rather than waiting until the Invoke Application phase
   *    - params - (optional) an object containing the additional request parameters
   */
  request: function(source, event, options) {
    var args = options ? options : {};

    args._source = source;
    args._event = event;
    var render = options.render ? options.render.split(" ") : undefined;
    args.execute = options.execute ? options.execute.split(" ") : undefined;
    args.onajaxstart = options.onajaxstart;
    args.onajaxend = options.onajaxend;
    args.onsuccess = options.onsuccess;
    args.onerror = options.onerror;
    args.onsuccess = options.onsuccess;
    args.onevent = options.onevent;
    args.listener = options.listener;
    args.immediate = options.immediate;
    args.executeRenderedComponents = options.executeRenderedComponents;
    var paramsObject = options.params;
    if (paramsObject) {
      args.params = [];
      for (var paramName in paramsObject) {
        var paramValue = paramsObject[paramName];
        args.params.push([paramName, paramValue]);
      }
    }

    O$.Ajax._reload(render, args, source, event);
  }
};


// ================================== IMPLEMENTATION


O$.AJAX_REQUEST_MARKER = "_openFaces_ajax";
O$.UPDATE_PORTIONS_SUFFIX = "_of_ajax_portions";
O$.CUSTOM_JSON_PARAM = "_of_customJsonParam";
O$.EXECUTE_RENDERED_COMPONENTS = "_of_executeRenderedComponents";

window.OpenFaces.Ajax = {
  _ajaxEndHandlers: [],
  _addAjaxEndHandler: function(handler) {
    this._ajaxEndHandlers.push(handler);
  },
  _removeAjaxEndHandler: function(handler) {
    var idx = this._ajaxEndHandlers.indexOf(handler);
    if (idx == -1) throw "O$.Ajax._removeAjaxEndHandler: this event handler is not registered";
    this._ajaxEndHandlers.splice(idx, 1);
  },

  onajaxend: function(event) {
    this._ajaxEndHandlers.forEach(function(handler) {
      handler(event);
    });
  },
  Page: {},
  Components: [],

  setCommonAjaxEventHandler: function(eventName, func) {
    if (!eventName || !func)
      return;

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
  },

  _attachHandler: function(id, eventName, handler) {
    var el = O$(id) || (O$.stringStartsWith(id, ":") ? O$(id.substring(1)) : null);
    if (!el) {
      O$.logError("<o:ajax> couldn't find a component/HTML node to attach to by id of \"" + id + "\"");
      return;
    }
    if (!eventName) eventName = "click";
    el["on" + eventName] = handler;
  },

  reloadPage: function(loc) {
    window.location = loc;
  },

  /**
   * render - components to which an ajax request is being made
   * agrs - (optional) is an object, which consist of:
   *    onajaxend - (optional) the function that should be invoked when ajax request is fully processed
   *    execute - (optional) array of clientIds for components whose processDecodes->...->processUpdates phases should be invoked in addition to the component being reloaded
   *    onerror - (optional) the function that should be invoked when ajax request fails to complete successfully for some reason
   *    onsuccess - (optional) the function that should be invoked when Ajax request completes without errors
   *    onajaxstart - (optional) the function that should be invoked before ajax request is started
   *    immediate - (optional) true means that the action should be executed during Apply Request Values phase, rather than waiting until the Invoke Application phase
   */
  _reload: function(render, args, source, event) {
    render.forEach(function(componentId){
      O$._invokeComponentAjaxReloadStart(componentId);
      var oldAjaxEndFunc = args.onajaxend;
      args.onajaxend = function (){
        if (oldAjaxEndFunc){
          oldAjaxEndFunc();
        }
        O$._invokeComponentAjaxReloadEnd(componentId);
      }
    });

    if (!args) args = {};
    if (source)
      args._source = source;
    if (event)
      args._event = event;

    var params = args.params ? args.params : [];
    var ids = [];
    if (render instanceof Array) {
      ids = render;
    } else {
      ids[0] = render;
    }
    args.params = params;
    if (!args.delay)
      O$.Ajax.sendRequestIfNoFormSubmission(ids, args);
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
        O$.Ajax.sendRequestIfNoFormSubmission(ids, args);
      }, args.delay, delayId);
    }
  },
  requestComponentPortions: function(componentId, portionNames, customJsonParam, portionProcessor, onerror, skipExecute, additionalParams ) {
    var args = arguments;
    if (!O$.isLoadedFullPage()) {
      O$.addLoadEvent(function() {
        O$.Ajax.requestComponentPortions.apply(null, args);
      });
      return;
    }
    if (!componentId)
      throw "componentId should be specified";
    if (! (portionNames instanceof Array))
      throw "portionNames should be specified as an array, but specified as: " + portionNames;
    if (!portionProcessor)
      throw "O$.Ajax.requestComponentPortions: portionProcessor should be specified";
    var params = skipExecute ? [["_of_skipExecute", "true"]] : [];
    if (additionalParams){
      params.push(additionalParams);
    }
    O$.Ajax.sendRequestIfNoFormSubmission([componentId], {portionNames: portionNames, portionProcessor: portionProcessor,
      params: params, onerror: onerror, customJsonParam: customJsonParam});
  },
  /*
   Sends ajax request only if no form submission is initated in the same event handler
   (e.g. a submit button invoking an ajax request in the onclick handler)
   */
  sendRequestIfNoFormSubmission: function() {
    var ajaxArgs = arguments;
    var evt = ajaxArgs[1]._event;
    if (evt && O$.isExplorer()) {
      // using event object after setTimeout in IE causes JavaScript error (like "member not found" when accessing event.type),
      // so we're just cloning all properties here
      var evtCopy = {};
      O$.extend(evtCopy, evt);
      ajaxArgs[1]._event = evtCopy;
    }
    setTimeout(function() {
      if (O$.isAjaxInLockedState())
        return;
      O$.Ajax.sendRequest.apply(null, ajaxArgs);
    }, 1);
  },

  _runScript: function(script, libs) {
    var runScriptState = {
      _postExecuteHandlers: []
    };
    runScriptState.postExecuteHandler = function(handler) {
        runScriptState._postExecuteHandlers.push(handler);
    };
    O$.Ajax._currentlyScheduledScript = runScriptState;
    libs.forEach(O$.Ajax.loadLibrary);
    function runScriptWhenReady() {
      if (libs.every(O$.Ajax.isLibraryLoaded)) {
        script();
        if (O$.Ajax._currentlyScheduledScript == runScriptState) {
          O$.Ajax._currentlyScheduledScript = null;
          for (var i =0; i < runScriptState._postExecuteHandlers.length; i++) {
            runScriptState._postExecuteHandlers[i]();
          }
        }
      } else
        setTimeout(runScriptWhenReady, 100);
    }
    runScriptWhenReady();
  },

  /**
   * render - components for which an ajax request is being made
   *
   * args fields:
   * portionNames - null value means that the whole component should be reloaded, otherwise it should be an array of component portion names
   * portionProcessor - must be specified if portionNames is specified
   * onajaxend - (optional) the function that should be invoked when ajax request is fully processed
   * execute - (optional) array of clientIds for components whose processDecodes->...->processUpdates phases should be invoked in addition to the component being reloaded
   * params - (optional) array of parameters those should be submitted in addition to form field data parameters
   * onerror - (optional) the function that should be invoked when ajax request fails to complete successfully for some reason
   * onajaxstart -
   * immediate -
   * customJsonParam -
   * listener - (optional) server action in the form of EL, which should be executed during this ajax request
   * actionTriggerParam - (optional) client id of a component from which this action is initiated (e.g. actual for a button in a table which needs current row's data in the action)
   *
   */
  sendRequest: function(render, args) {
    if (window._of_ajax_onsessionexpired) {
      if (OpenFaces.Ajax.Page.onsessionexpired) {
        var sessionExpiredEvent = O$.createEvent("sessionexpired");
        OpenFaces.Ajax.Page.onsessionexpired(sessionExpiredEvent);
      }
      return;
    }

    var source = args._source;
    var evt = args._event;
    var params = {};
    var render = render.join(" ");
    params[O$.AJAX_REQUEST_MARKER] = "true";
    if (args.params) {
      if (args.params instanceof Array) {
        args.params.forEach(function(param) {
          var paramName = param[0];
          var paramValue = param[1];
          params[paramName] = paramValue;
        });
      } else {
        for (var param in args.params) {
          var value = args.params[param];
          params[param] = value;
        }

      }
    }
    if (args.portionNames) {
      var buf = new O$.StringBuffer();
      var commaNeeded = false;
      args.portionNames.forEach(function(portionName) {
        var encodedPortionName = O$.Ajax.escapeSymbol(portionName, ",");
        if (commaNeeded) buf.append(",");
        buf.append(encodedPortionName);
        commaNeeded = true;
      });
      params[O$.UPDATE_PORTIONS_SUFFIX] = buf.toString();
    }
    if (args.customJsonParam) {
      params[O$.CUSTOM_JSON_PARAM] = args.customJsonParam;
    }
    var execute = args.execute ? args.execute.join(" ") : undefined;
    if (args.immediate) {
      if (!params) params = {};
      params[O$.IMMEDIATE] = args.immediate;
    }
    if (args._action) {
      if (!params) params = {};
      params[O$.ACTION] = args._action;
    }
    if (args.listener) {
      if (!params) params = {};
      params[O$.ACTION_LISTENER] = args.listener;
    }
    params[O$.ACTION_COMPONENT] = source && typeof source != "string" ? source.id : source;
    if (args.actionTriggerParam) {
      if (!params) params = {};
      params[args.actionTriggerParam] = args.actionTriggerParam;
    }
    if (args.executeRenderedComponents != null) {
      if (!params) params = {};
      params[O$.EXECUTE_RENDERED_COMPONENTS] = args.executeRenderedComponents;
    }

    function fireEvent(eventName, event) {
      if (args[eventName])
        args[eventName].call(null, event);
      if (OpenFaces.Ajax.Page[eventName])
        OpenFaces.Ajax.Page[eventName](event);
    }
    var ajaxResult;
    var validationError;

    function success(data) {
      if (O$.Ajax._currentlyScheduledScript) {
        O$.Ajax._currentlyScheduledScript.postExecuteHandler(function() {success(data)});
        return;
      }
      var successEvent = O$.createEvent("success");
      successEvent.data = data;
      successEvent.validationError = validationError;
      fireEvent("onsuccess", successEvent);
    }
    function ajaxEnd(data) {
      setTimeout(destroyMemoryLeaks, 1);
      if (O$.Ajax._currentlyScheduledScript) {
        O$.Ajax._currentlyScheduledScript.postExecuteHandler(function() {ajaxEnd(data)});
        return;
      }
      var ajaxendEvent = O$.createEvent("ajaxend");
      ajaxendEvent.ajaxResult = ajaxResult;
      ajaxendEvent.validationError = validationError;
      ajaxendEvent.data = data;
      fireEvent("onajaxend", ajaxendEvent);
      function destroyMemoryLeaks() {
        if (!(options._of_skipExecute || options._of_ajax_portions)) {
          for (var render in parentOfReloadedComponents) {
            var parentOfReloadedComponent = parentOfReloadedComponents[render];
            var reloadedComponent = getNotDomComponentById(render, parentOfReloadedComponent);
            if (reloadedComponent != null) {
              if (reloadedComponent._unloadableComponents) {
                var componentNotInDOM = (reloadedComponent.parentNode != parentOfReloadedComponent);
                var length = reloadedComponent._unloadableComponents.length;
                for (var index = 0; index < length; index++) {
                  if (reloadedComponent._unloadableComponents[0].onComponentUnload) {
                    reloadedComponent._unloadableComponents[0].onComponentUnload()
                  }
                  var tempEl = reloadedComponent._unloadableComponents[0];
                  if (!O$.removeThisComponentFromAllDocument(tempEl)) {
                    O$.removeThisComponentFromParentsAbove(tempEl, reloadedComponent);
                    if (componentNotInDOM) {
                      O$.removeThisComponentFromParentsAbove(tempEl, parentOfReloadedComponent);
                    }
                  }
                  if (tempEl == reloadedComponent._unloadableComponents[0]) {
                    O$.removeThisComponentFromParentsAbove(tempEl, reloadedComponent);
                    if (componentNotInDOM) {
                      O$.removeThisComponentFromParentsAbove(tempEl, parentOfReloadedComponent);
                    }
                  }
                }
              }
              if (reloadedComponent.onComponentUnload) {
                reloadedComponent.onComponentUnload();
                O$.removeThisComponentFromParentsAbove(reloadedComponent, parentOfReloadedComponent);
              }

              if (O$.isExplorer()) {
                if (document._ajaxCleanupRequired)
                  O$.destroyAllFunctions(reloadedComponent);
              }
            }
          }
        }
        function getNotDomComponentById(idOfElement, parentOfElementInDom) {
          if (!parentOfElementInDom._unloadableComponents)
            return null;
          var elementsWithSameId = [];
          parentOfElementInDom._unloadableComponents.forEach(function(comp) {
            if (comp.id == idOfElement) {
              elementsWithSameId.push(comp);
            }
          });
          var components = elementsWithSameId.filter(function (element) {
            return (element.parentNode != parentOfElementInDom);
          });
          return (components.length == 0) ? null : components[0];
        }
      }
    }
    function eventHandler(data) {
      if (args.onevent)
        args.onevent.call(null, data);
      if (data.status == "complete") {
        setTimeout(function() {source._openFaces_ajax_inProgress = false;}, 1);
        validationError = undefined;
        var atLeastOnePortionProcessed = false;
        var xml = data.responseXML;
        if (!xml) {alert("Error while performing Ajax request: No xml response received -- check server logs."); return;}
        var rootTags = xml.getElementsByTagName("partial-response");
        if (rootTags.length == 0) {
          alert("Malformed Ajax XML response: couldn't find the <partial-response> tag");
          return;
        }
        var childNodes = rootTags[0].childNodes;
        function processExtension(extensionNode) {
          var ln = extensionNode.getAttribute("ln");
          if (ln != "openfaces") return;
          atLeastOnePortionProcessed = true;
          var extensionType = extensionNode.getAttribute("type");

          if (extensionType == "portionData") {
            var portionName = extensionNode.getAttribute("portion");
            var portionText = extensionNode.getAttribute("text");
            var portionDataStr = extensionNode.getAttribute("data");
            var jsLibsStr = extensionNode.getAttribute("jsLibs");
            var portionScripts = extensionNode.getAttribute("scripts");
            var jsLibs = eval(jsLibsStr);
            var portionData = portionDataStr ? eval("(" + portionDataStr + ")") : null;
            var component = O$(render);
            O$.Ajax._runScript(function() {
              if (!args.portionProcessor) throw "OpenFaces Ajax portion node received but no portionProcessor was specified by the request invoker";
              args.portionProcessor(component, portionName, portionText, portionScripts, portionData);
            }, jsLibs);
          } else if (extensionType == "sessionExpiration") {
            window._of_ajax_onsessionexpired = true;
            var html = extensionNode.getAttribute("text");
            jsLibsStr = extensionNode.getAttribute("jsLibs");
            portionScripts = extensionNode.getAttribute("scripts");
            jsLibs = eval(jsLibsStr);
            O$.Ajax._runScript(function() {
              O$.Ajax._processUpdateOnExpirationOrError(html, portionScripts);
              if (OpenFaces.Ajax.Page.onsessionexpired) {
                var sessionExpiredEvent = O$.createEvent("sessionexpired");
                OpenFaces.Ajax.Page.onsessionexpired(sessionExpiredEvent);
              }
            }, jsLibs);
          } else if (extensionType == "ajaxResult") {
            var ajaxResultStr = extensionNode.getAttribute("ajaxResult");
            try {
              eval("ajaxResult = " + ajaxResultStr);
            } catch (e) {
              ajaxResult = ajaxResultStr;
            }
          } else if (extensionType == "validationError") {
              var validationErrorStr = extensionNode.getAttribute("validationError");
              validationError = (validationErrorStr == "true");
          } else
            throw "Unknown OpenFaces Ajax extension node type: " + extensionType;
        }
        for (var i = 0, count = childNodes.length; i < count; i++) {
          var childNode = childNodes[i];
          if (childNode.nodeName == "extension")
            processExtension(childNode);
          if (childNode.nodeName == "changes")
            for (var j = 0, jCount = childNode.childNodes.length; j < jCount; j++) {
              var change = childNode.childNodes[j];
              if (change.nodeName == "extension")
                processExtension(change);
            }
        }

        if (args.portionProcessor && !atLeastOnePortionProcessed) {
          alert("Error while performing Ajax request: no openfaces extension blocks found in Ajax response.");
        }
      }
      if (data.status == "success") {
        // checking onerrorTriggered here because "success" notification can be made for "serverError" kind of error
        // (see also the note in errorHandler function below)
        if (!onerrorTriggered) {
          success(data);
        }

        ajaxEnd(data);
      }
    }
    var onerrorTriggered = false;
    function errorHandler(data) {
      var errorEvent = O$.createEvent("error");
      errorEvent.data = data;
      onerrorTriggered = true;

      fireEvent("onerror", errorEvent);

      if (data.status != "serverError") {
        // jsf.js handles "serverError" specially -- it will send onevent with "success" status after the onerror event,
        // so ajaxEnd will be invoked then and there's no need to repeat it here in this case
        ajaxEnd(data);
      }
    }
    var options = {
      execute: execute,
      render: render,
      onevent: eventHandler,
      onerror: errorHandler
    };
    if (params)
      O$.extend(options, params);
    if (!source) {
      if (document.forms.length == 0)
        throw "There should be a <h:form> component on a page for Ajax to work";
      var firstSeparator = render ? render.indexOf(" ") : -1;
      var render1 = firstSeparator != -1 ? render.substring(0, firstSeparator) : render;
      firstSeparator = execute ? render.indexOf(" ") : -1;
      var execute1 = firstSeparator != -1 ? execute.substring(0, firstSeparator) : execute;
      var executeElement = O$(execute1) || O$(render1);
      var frm = O$.getParentNode(executeElement, "FORM");
      if (!frm) frm = document.forms[0];
      var frmChildren = frm.childNodes;
      source = frm.firstChild;
      for (var ci = 0, count = frmChildren.length; ci < count; ci++) {
        var child = frmChildren[ci];
        if (!O$.stringStartsWith(child.nodeName, "#")) {
          source = child;
          break;
        }
      }

    }
    if (!source.id) {
      if (O$.Ajax._autoGeneratedId == undefined) O$.Ajax._autoGeneratedId = 1;
      source.id = "_of_ajax_auto_id_" + O$.Ajax._autoGeneratedId++;
    }
    source._openFaces_ajax_inProgress = true;
    var parentOfReloadedComponents = {};
    render.split(" ").forEach(function (render) {
      if (render != "") {
        var renderEl = O$(render);
        if (renderEl != null)
          parentOfReloadedComponents[render] = renderEl.parentNode;
      }
    });

    if (window.RichFaces && RichFaces.ajax && RichFaces.ajax.jsfRequest) {
      // account for RichFaces 4 M2's broken jsf.ajax.request functionality -- invoke the original method instead of
      // the overridden one. todo: check with RF4 release
      RichFaces.ajax.jsfRequest(source, evt, options);
    } else {
      jsf.ajax.request(source, evt, options);
    }

    var ajaxstartEvent = O$.createEvent("ajaxstart");
    fireEvent("onajaxstart", ajaxstartEvent);

    O$.saveScrollPositionIfNeeded();
  },

  escapeSymbol: function(portionName, escapedChars) {
    var res = new O$.StringBuffer();
    for (var i = 0, count = portionName.length; i < count; i++) {
      var currChar = portionName.charAt(i);
      if (currChar == "\\") {
        res.append("\\");
      } else {
        var index = escapedChars.indexOf(currChar);
        if (index != -1) {
          var fullCharCode = new String(escapedChars[index].charCodeAt() + 10000);
          res.append("\\" + fullCharCode.substr(1, fullCharCode.length));
          continue;
        }
      }
      res.append(currChar);
    }
    return res.toString();
  },


  executeScripts: function(source) {
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
      script = O$.trim(script);
      var cdataPrefix = "//<![CDATA[";
      var cdataSuffix = "//]]>";
      if (O$.stringStartsWith(script, cdataPrefix)) script = script.substring(cdataPrefix.length);
      if (O$.stringEndsWith(script, cdataSuffix)) script = script.substring(0, script.length - cdataSuffix.length);
      try {
        window.eval(script);
      } catch (e) {
        alert("Exception '" + e.message + "' while executing script on Ajax request: \n" + script);
        throw e;
      }
      idx2 += "</script>".length;
      result = result.substring(idx2);
      O$.Ajax.executeScripts(result);
    }
  },

  _requestsInProgress: 0,

  setMessageHTML: function(messageHTML, horizAlignment, vertAlignment, transparency, opacityTransitionPeriod,
                                   blockingLayer, useForNonOpenFacesRequests) {
    if (O$.isChrome() || O$.isSafari()) {
      messageHTML = messageHTML.replace("&", "&amp;");
    }
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
    msg._includeNonOpenFacesRequests = useForNonOpenFacesRequests;
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

    if (!O$._ajaxProgressInitialized) {
      O$.addLoadEvent(function() {
        var msg = document._ajaxInProgressMessage;
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

      O$._ajaxProgressInitialized = true;
      if (O$.Ajax._requestsInProgress == undefined) O$.Ajax._requestsInProgress = 0;
      if (window.jsf)
        jsf.ajax.addOnEvent(function(data) {
          if (document._ajaxInProgressMessage._includeNonOpenFacesRequests || (data.source && data.source._openFaces_ajax_inProgress)) {
            if (data.status == "begin") {
              if (++O$.Ajax._requestsInProgress == 1) {
                O$.Ajax.showProgressMessage();
              }
            }
            if (data.status == "complete") {
              if (--O$.Ajax._requestsInProgress == 0) {
                O$.Ajax.hideProgressMessage();
                O$.restoreScrollPositionIfNeeded();
              }
            }
          }
        });
      if (O$.Ajax._requestsInProgress > 0)
        O$.Ajax.showProgressMessage();
    }

  },

  showProgressMessage: function() {
    var msg = document._ajaxInProgressMessage;
    if (!msg)
      return;
    var simulateFixedPos = true;//O$.isExplorer();
    if (simulateFixedPos)
      O$.Ajax.updateProgressMessagePos();

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
      O$.Ajax.updateProgressMessagePos();
    }

    if (document.body.style.cursor != "progress") {
      document.body.oldCursor = document.body.style.cursor;
    }
    document.body.style.cursor = "progress";

    if (simulateFixedPos) {
      O$.addEventHandler(window, "scroll", O$.Ajax.updateProgressMessagePos);
      O$.addEventHandler(window, "resize", O$.Ajax.updateProgressMessagePos);
    }
  },

  hideProgressMessage: function() {
    var msg = document._ajaxInProgressMessage;
    if(!msg) return;
    var simulateFixedPos = true;//O$.isExplorer();
    if (simulateFixedPos) {
      O$.removeEventHandler(window, "scroll", O$.Ajax.updateProgressMessagePos);
      O$.removeEventHandler(window, "resize", O$.Ajax.updateProgressMessagePos);
    }
    document.body.style.cursor = document.body.oldCursor;
    msg.style.display = "none";
    if (msg._blockingLayer)
      msg._blockingLayer.style.display = "none";
  },

  updateProgressMessagePos: function() {
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
  },

  _pageReloadConfirmation: function(confirmationId, location) {
    var confirmation = O$(confirmationId);
    if (document._ajaxInProgressMessage._blockingLayer)
      O$.correctElementZIndex(confirmation, document._ajaxInProgressMessage._blockingLayer);
    confirmation.runConfirmedFunction(function() {
      O$.Ajax.reloadPage(location);
    });
  },

  _pushElementsWithId: function(destElements, element) {
    if (!element)
      return;
    if (element.id) {
      destElements.push(element);
      return;
    }

    for (var childIndex = 0, childCount = element.childNodes.length; childIndex < childCount; childIndex++) {
      var subElement = element.childNodes[childIndex];
      O$.Ajax._pushElementsWithId(destElements, subElement);
    }
  },

  _processUpdateOnExpirationOrError: function (updHTML, updScripts) {
    var tempDiv = document.createElement("div");
    tempDiv.innerHTML = updHTML;

    var newElements = [];
    for (var childIndex = 0, childCount = tempDiv.childNodes.length; childIndex < childCount; childIndex++) {
      var newElement = tempDiv.childNodes[childIndex];
      O$.Ajax._pushElementsWithId(newElements, newElement);
    }

    newElements.forEach(function(newElement) {
      O$.assert(newElement.id, "_processSimpleUpdate: newElement without id encountered");
      var parent = document.body;
      parent.insertBefore(newElement, parent.lastChild);
    });

    eval(updScripts);
  },

  canonifyLibraryName: function(lib) {
    var hostIndex = lib.indexOf("://");
    if (hostIndex != -1) {
      var startIdx = lib.indexOf("/", hostIndex + 3);
      lib = lib.substring(startIdx);
    }
    var jSessionIdIndex = lib.indexOf(";jsessionid");
    if (jSessionIdIndex != -1)
      lib = lib.substring(0, jSessionIdIndex);
    return lib;
  },

  markLibraryLoaded: function(lib) {
    lib = O$.Ajax.canonifyLibraryName(lib);
    window["_of_loadedLibrary:" + lib] = true;
  },

  loadLibrary: function(fileUrl) {
    if (!fileUrl) return;
    var found = O$.Ajax.isLibraryLoaded(fileUrl);
    if (!found) {
      var newScript = document.createElement("script");
      newScript.type = "text/javascript";
      newScript.src = fileUrl;
      newScript.onload = function() {
        O$.Ajax.markLibraryLoaded(fileUrl);
      };
      newScript.onreadystatechange = function() {
        if (this.readyState == "complete")
          O$.Ajax.markLibraryLoaded(fileUrl);
        else if (O$.isExplorer() && this.readyState == "loaded") {
          // IE for some reason sometimes reports the "loaded" state and skips the "complete" one, so we're processing
          // this one too (after making a spare timeout just in case)
          setTimeout(function() {
            O$.Ajax.markLibraryLoaded(fileUrl);
          }, 50);
        }
      };
      var head = document.getElementsByTagName("head")[0];
      head.appendChild(newScript);
    }
  },

  _markPreloadedLibraries: function() {
    if (O$._preloadedLibrariesMarked) return;
    O$._preloadedLibrariesMarked = true;
    var scriptElements = document.getElementsByTagName("script");
    for (var i = 0, count = scriptElements.length; i < count; i++) {
      var scriptElement = scriptElements[i];
      if (scriptElement.src)
        O$.Ajax.markLibraryLoaded(scriptElement.src);
    }
  },

  isLibraryLoaded: function(lib) {
    O$.Ajax._markPreloadedLibraries();
    lib = O$.Ajax.canonifyLibraryName(lib);
    var result = eval("window['_of_loadedLibrary:" + lib + "']");
    return result;
  }

};

O$.addLoadEvent(function() {
  O$.Ajax._markPreloadedLibraries();
});

O$.destroyAllFunctions = function(elt) {
  if (!elt || elt.nodeName == "#text")
    return;

  // Clear methods added with help O$.extend() because of circular references
  if (elt.customPropertiesForIE) {
    for (var index = 0; index < elt.customPropertiesForIE.length; index++) {
      elt[elt.customPropertiesForIE[index]] = null;
    }
    elt.customPropertiesForIE = [];
  }

  // Break the potential circular references
  for (var member in elt) {
    if (member.indexOf("_") != 0 && member.indexOf("on") != 0)
      continue;
    if (!elt[member])
      continue;
    if (typeof elt[member] === "function")
      (function() {
        elt[member] = null;
      })();
    else
      elt[member] = null;
  }

  var childNodes = elt.childNodes;
  if (childNodes) {
    var length = childNodes.length;
    for (var index = 0; index < length; index++) {
      O$.destroyAllFunctions(childNodes[index]);
    }
  }
};

O$._fireSessionExpiredEvent = function () {
  if (document._attachedEvents) {
    for (var i = 0; i < document._attachedEvents.length; i++) {
      var event = document._attachedEvents[i];
      if (event.eventName == "sessionexpired" || event.eventName == "onsessionexpired") {
        event.functionScript();
      }
    }
  }
}

O$._addComponentAjaxReloadHandler = function (component, ajaxStart, ajaxEnd){
  if (!O$._componentAjaxHandlers)
    O$._componentAjaxHandlers = [];
  for (var i = 0; i < O$._componentAjaxHandlers.length; i ++){
   if (O$._componentAjaxHandlers[i].componentId == component.id){
     return
   }
  }
  var ajaxHandler = {};
  ajaxHandler.componentId = component.id;
  ajaxHandler.ajaxStart = ajaxStart;
  ajaxHandler.ajaxEnd = ajaxEnd;
  O$._componentAjaxHandlers.push(ajaxHandler);
}

O$._invokeComponentAjaxReloadEnd = function (componentId){
  if (!O$._componentAjaxHandlers) return;
  O$._componentAjaxHandlers.forEach(function(ajaxHandler) {
    if (ajaxHandler.componentId == componentId){
      if (ajaxHandler.ajaxEnd)
        ajaxHandler.ajaxEnd.call(O$(componentId));;
    }
  });
}

O$._invokeComponentAjaxReloadStart = function (componentId){
  if (!O$._componentAjaxHandlers) return;
  O$._componentAjaxHandlers.forEach(function(ajaxHandler) {
    if (ajaxHandler.componentId == componentId){
      if (ajaxHandler.ajaxStart)
        ajaxHandler.ajaxStart.call(O$(componentId));
    }
  });
}

