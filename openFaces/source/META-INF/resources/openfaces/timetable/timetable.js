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

O$.Timetable = {
  DAY: "day",
  WEEK: "week",
  MONTH: "month",

  _init: function(timetableId, layeredPaneId, viewIds, initialViewType, events) {
    var timetable = O$.initComponent(timetableId, null, {
      _views: viewIds.map(O$),
      _layeredPane: O$(layeredPaneId),

      getViewType: function() {
        return this._viewType;
      },

      goViewDay: function(day){
        var viewIndex = this._viewIndexByType(O$.Timetable.DAY);

        this._views[viewIndex].setDay(day);
        this.setViewType(O$.Timetable.DAY);
      },

      setViewType: function(viewType) {
        if (viewType != O$.Timetable.DAY &&
                viewType != O$.Timetable.WEEK &&
                viewType != O$.Timetable.MONTH)
          throw "O$.Timetable.setView: illegal view parameter: \"" + viewType + "\"";
        if (this._viewType == viewType) return;

        var prevTimetableView = this._viewType ? this._viewByType(this._viewType) : null;
        if (prevTimetableView)
          prevTimetableView._removeEventElements();

        this._setViewType(viewType);

        var timetableView = this._viewByType(viewType);
        timetableView._updateEventElements();
        setTimeout(function() {
          timetableView.updateLayout();
        }, 1);

        var viewIndex = this._viewIndexByType(viewType);
        this._layeredPane.setSelectedIndex(viewIndex);
        this._fireEvent("viewtypechange");
      },

      _setViewType: function(viewType) {
        this._viewType = viewType;
        O$.setHiddenField(this, timetableId + "::view", viewType);
      },

      _viewIndexByType: function(viewType) {
        var viewIndex;
        for (var i = 0, count = this._views.length; i < count; i++) {
          var v = this._views[i];
          if (v._viewType == viewType) {
            viewIndex = i;
            break;
          }
        }
        if (viewIndex == undefined) throw "O$.Timetable._viewIndexByType: couldn't find view for type: " + viewType;
        return viewIndex;
      },

      _fireEvent: function(eventBaseName) {
        var handler = this["on" + eventBaseName];
        if (!handler)
          return;
        var event = O$.createEvent(eventBaseName);
        handler.call(this, event);
      },

      _viewByType: function(viewType) {
        for (var i = 0, count = this._views.length; i < count; i++) {
          var view = this._views[i];
          if (view._viewType == viewType)
            return view;
        }
        throw "View not found for the following type: " + viewType;
      }
    }, events);

    O$.addInternalLoadEvent(function() {
      timetable.setViewType(initialViewType);
    });

  },

  _initEventEditorDialog: function(timetableViewId, dialogId, createEventCaption, editEventCaption, centered) {
    var timetableView = O$(timetableViewId);

    function getFieldText(field) {
      if (field.getValue)
        return field.getValue();
      return field.value;
    }

    function setFieldText(field, text) {
      if (field.setValue)
        field.setValue(text);
      else
        field.value = text;
    }

    var dialog = O$.initComponent(dialogId, null, {
              _timetableView: timetableView,
              _nameField: O$.byIdOrName(dialogId + "--nameField"),
              _resourceField: O$.byIdOrName(dialogId + "--resourceField"),
              _startDateField: O$.byIdOrName(dialogId + "--startDateField"),
              _endDateField: O$.byIdOrName(dialogId + "--endDateField"),
              _startTimeField: O$.byIdOrName(dialogId + "--startTimeField"),
              _endTimeField: O$.byIdOrName(dialogId + "--endTimeField"),
              _colorField: O$.byIdOrName(dialogId + "--colorField"),
              _color: "",
              _descriptionArea: O$.byIdOrName(dialogId + "--descriptionArea"),
              _okButton: O$.byIdOrName(dialogId + "--okButton"),
              _cancelButton: O$.byIdOrName(dialogId + "--cancelButton"),
              _deleteButton: O$.byIdOrName(dialogId + "--deleteButton"),

              run: function(event, mode) {
                this._event = event;
                setFieldText(this._nameField, event.name);
                var resource = timetableView._getResourceForEvent(event);
                if (dialog._resourceField)
                  dialog._resourceField.setValue(resource ? resource.name : "");
                this._startDateField.setSelectedDate(event.start);
                if (this._endDateField)
                  this._endDateField.setSelectedDate(event.end);
                var duration = event.end.getTime() - event.start.getTime();
                setFieldText(this._startTimeField, O$.formatTime(event.start));
                if (this._endTimeField)
                  setFieldText(this._endTimeField, O$.formatTime(event.end));
                this._color = event.color;
                setFieldText(this._descriptionArea, event.description);
                this._deleteButton.style.visibility = mode == "update" ? "visible" : "hidden";
                O$.removeAllChildNodes(this._captionContent);
                this._captionContent.appendChild(document.createTextNode(mode == "update"
                        ? editEventCaption
                        : createEventCaption));

                this._okPressed = false;
                this._okButton.onclick = function(e) {
                  this._okProcessed = true;
                  O$.cancelEvent(e);
                  event.name = getFieldText(dialog._nameField);
                  var startDate = dialog._startDateField.getSelectedDate();
                  if (!startDate) {
                    dialog._startDateField.focus();
                    return;
                  }
                  O$.parseTime(getFieldText(dialog._startTimeField), startDate);
                  var endDate = dialog._endDateField ? dialog._endDateField.getSelectedDate() : null;
                  if (dialog._endTimeField) {
                    if (!endDate) {
                      dialog._endDateField.focus();
                      return;
                    }
                    O$.parseTime(getFieldText(dialog._endTimeField), endDate);
                  }
                  if (!startDate || isNaN(startDate)) {
                    dialog._startTimeField.focus();
                    return;
                  }
                  if (!fixedDurationMode && (!endDate || isNaN(endDate))) {
                    dialog._endTimeField.focus();
                    return;
                  }
                  event.setStart(startDate);
                  if (fixedDurationMode) {
                    // fixed duration mode
                    endDate = new Date();
                    endDate.setTime(startDate.getTime() + duration);
                  }
                  event.setEnd(endDate);
                  if (dialog._resourceField)
                    event.resourceId = timetableView._idsByResourceNames[dialog._resourceField.getValue()];
                  event.color = dialog._color ? dialog._color : "";
                  event.description = getFieldText(dialog._descriptionArea);
                  dialog.hide();
                  if (mode == "create")
                    timetableView.addEvent(event);
                  else
                    timetableView.updateEvent(event);
                };

                this._cancelButton.onclick = function(e) {
                  O$.cancelEvent(e);
                  dialog.hide();
                  if (mode == "create")
                    timetableView.cancelEventCreation(event);
                };

                this._deleteButton.onclick = function(e) {
                  O$.cancelEvent(e);
                  dialog.hide();
                  if (mode == "update")
                    timetableView.deleteEvent(event);
                };

                var previousHide = this.onhide;
                this.onhide = function() {
                  if (!this._okProcessed && mode == "create")
                    timetableView.cancelEventCreation(event);
                  if (dialog._textareaHeightUpdateInterval)
                    clearInterval(dialog._textareaHeightUpdateInterval);
                  if (previousHide) {
                    previousHide.apply(this);
                  }
                };

                if (event.parts) {
                  event.parts.forEach(function(part) {
                    if (part.mainElement)
                      O$.correctElementZIndex(dialog, part.mainElement, 5);
                  });
                }
                if (centered)
                  this.showCentered();
                else
                  this.show();

                function adjustTextareaHeight() {
                  var size = O$.getElementSize(dialog._descriptionArea.parentNode);
                  O$.setElementSize(dialog._descriptionArea, size);
                }

                if (O$.isExplorer() || O$.isOpera()) {
                  if (dialog._descriptionArea.style.position != "absolute") {
                    dialog._descriptionArea.style.position = "absolute";
                    var div = document.createElement("div");
                    div.style.height = "100%";
                    dialog._descriptionArea.parentNode.appendChild(div);
                  }
                  adjustTextareaHeight();
                  dialog._textareaHeightUpdateInterval = setInterval(adjustTextareaHeight, 50);
                } else
                  dialog._textareaHeightUpdateInterval = null;

              }
            });

    var fixedDurationMode = !dialog._endDateField;
    timetableView._eventEditor = dialog;

    function okByEnter(fld) {
      if (!fld)
        return;
      if (fld instanceof Array) {
        for (var i in fld) {
          var entry = fld[i];
          okByEnter(entry);
        }
        return;
      }
      fld.onkeydown = function(e) {
        var evt = O$.getEvent(e);
        if (evt.keyCode != 13)
          return;
        if (this.nodeName.toLowerCase() == "textarea") {
          if (!evt.ctrlKey)
            return;
        }
        dialog._okButton.onclick(e);
      };
    }

    okByEnter([dialog._nameField, dialog._resourceField, dialog._startDateField, dialog._endDateField,
      dialog._startTimeField, dialog._endTimeField, dialog._colorField, dialog._descriptionArea]);
  },

  _initEventEditorPage: function(timetableViewId, thisComponentId, actionDeclared, url, modeParamName, eventIdParamName,
                                 eventStartParamName, eventEndParamName, resourceIdParamName) {
    var timetableView = O$(timetableViewId);
    var thisComponent = O$(thisComponentId);
    timetableView._eventEditor = thisComponent;
    thisComponent.run = function(event, mode) {
      if (actionDeclared) {
        var params = (mode == "create") ?
                [
                  [thisComponentId + "::action", "action"],
                  [modeParamName, mode],
                  [eventStartParamName, event.startStr],
                  [eventEndParamName, event.endStr],
                  [resourceIdParamName, event.resourceId]
                ] :
                [
                  [thisComponentId + "::action", "action"],
                  [modeParamName, mode],
                  [eventIdParamName, event.id]
                ];
        O$.submitWithParams(timetableView, params);
        return;
      }
      var newPageUrl = url + "?" + modeParamName + "=" + mode + "&";
      if (mode == "create") {
        newPageUrl += eventStartParamName + "=" + encodeURIComponent(event.startStr) + "&";
        newPageUrl += eventEndParamName + "=" + encodeURIComponent(event.endStr) + "&";
        newPageUrl += resourceIdParamName + "=" + encodeURIComponent(event.resourceId);
      } else if (mode == "update") {
        newPageUrl += eventIdParamName + "=" + encodeURIComponent(event.id);
      }
      window.location = newPageUrl;
    };
  },

  _initCustomEventEditor: function(timetableViewId, thisComponentId, oncreate, onedit) {
    var timetableView = O$(timetableViewId);
    var thisComponent = O$(thisComponentId);
    timetableView._eventEditor = thisComponent;
    thisComponent.run = function(event, mode) {
      if (mode == "create")
        oncreate(timetableView, event);
      else
        onedit(timetableView, event);
    };

  },

  _initEvent: function(event) {
    O$.extend(event, {
              setStart: function(asDate, asString) {
                if (asDate) {
                  event.start = asDate;
                  event.startStr = O$.formatDateTime(asDate);
                } else
                if (asString) {
                  event.startStr = asString;
                  event.start = O$.parseDateTime(asString);
                } else
                  throw "event.setStart: either asDate parameter, or asTime parameter should be specified";
              },

              setEnd: function(asDate, asString) {
                if (asDate) {
                  event.end = asDate;
                  event.endStr = O$.formatDateTime(asDate);
                } else
                if (asString) {
                  event.endStr = asString;
                  event.end = O$.parseDateTime(asString);
                } else
                  throw "event.setEnd: either asDate parameter, or asTime parameter should be specified";
              },

              _copyFrom: function(otherEvent) {
                this.id = otherEvent.id;
                this.setStart(otherEvent.start, otherEvent.startStr);
                this.setEnd(otherEvent.end, otherEvent.endStr);
                this.name = otherEvent.name;
                this.description = otherEvent.description;
                this.resourceId = otherEvent.resourceId;
                this.color = otherEvent.color;
                if (otherEvent.customProperties) {
                  this.customProperties = new Array();
                  for (var key in otherEvent.customProperties) {
                    this.customProperties[key] = otherEvent.customProperties[key];
                  }
                }
              },

              _scrollIntoView: function() {
                /*    return; //todo: finish auto-scrolling functionality
                 var timetableView = event.mainElement._timetableView;
                 var scrollingOccurred = O$.scrollElementIntoView(event.mainElement, timetableView._getScrollingCache());
                 if (scrollingOccurred)
                 timetableView._resetScrollingCache();*/
              }

            });
    if (event.start || event.startStr)
      event.setStart(event.start, event.startStr);
    if (event.end || event.endStr)
      event.setEnd(event.end, event.endStr);
  },

  _initEventPreview: function(eventPreviewId, timetableViewId, showingDelay, popupClass, eventNameClass,
                              eventDescriptionClass, horizontalAlignment, verticalAlignment, horizontalDistance, verticalDistance) {
    var eventPreview = O$(eventPreviewId);
    var popupLayer = O$(eventPreviewId + "--popupLayer");
    O$.appendClassNames(popupLayer, [popupClass]);

    eventNameClass = O$.combineClassNames(["o_timetableEventName", eventNameClass]);
    eventDescriptionClass = O$.combineClassNames(["o_timetableEventDescription", eventDescriptionClass]);

    O$.extend(eventPreview, {
              _showingDelay: showingDelay,

              showForEvent: function(event) {
                var timetableView = event._timeTableView;//O$(timetableViewId);
                var popupParent = O$.getDefaultAbsolutePositionParent();
                if (popupLayer.parentNode != popupParent) {
                  popupParent.appendChild(popupLayer);
                }
                O$.correctElementZIndex(popupLayer, timetableView);

                var oldSpans;
                while ((oldSpans = popupLayer.getElementsByTagName("span")).length > 0) {
                  var span = oldSpans[0];
                  span.parentNode.removeChild(span);
                }

                var nameElement = document.createElement("span");
                nameElement.className = eventNameClass;
                popupLayer.appendChild(nameElement);

                var descriptionElement = document.createElement("span");
                descriptionElement.className = eventDescriptionClass;
                popupLayer.appendChild(descriptionElement);

                O$.setInnerText(nameElement, event.name, timetableView._escapeEventNames);
                O$.setInnerText(descriptionElement, event.description, timetableView._escapeEventDescriptions);

                var mainElement = event.parts[0].mainElement;
                if (!mainElement)
                  popupLayer.showCentered();
                else
                  popupLayer.showByElement(mainElement,
                          horizontalAlignment, verticalAlignment, horizontalDistance, verticalDistance);
              },

              hide: function() {
                popupLayer.hide();
              }

            });
  },


  _initEventActionBar: function(actionBarId, backgroundIntensity, userSpecifiedClass, actions,
                                actionRolloverIntensity, actionPressedIntensity) {
    var actionBar = O$(actionBarId);
    if (!actionBar) {
      var initArgs = arguments;
      // postpone initialization to avoid FF2 failure of finding actionBar element in some cases
      setTimeout(function() {
        O$.Timetable._initEventActionBar.apply(null, initArgs);
      }, 100);
      return;
    }

    O$.extend(actionBar, {
              _inactiveSegmentIntensity: backgroundIntensity,
              _userSpecifiedClass: userSpecifiedClass,
              className: O$.combineClassNames(["o_eventActionBar", userSpecifiedClass]),

              _show: function(timetableView, event, part) {
                var eventElement = part.mainElement;
                this._timetableView = timetableView;
                this._event = event;
                this._part = part;
                var userSpecifiedStyles = O$.getStyleClassProperties(this._userSpecifiedClass, ["color", "background-color"]);
                this.style.backgroundColor = userSpecifiedStyles.backgroundColor
                        ? userSpecifiedStyles.backgroundColor
                        : O$.blendColors(eventElement._color, "#ffffff", 1 - this._inactiveSegmentIntensity);
                eventElement.appendChild(this);
                this.style.height = "";
                this.style.width = "";
                var barHeight = this.offsetHeight;
                var actionsAreaHeight = this._actionsArea._getHeight();
                if (barHeight < actionsAreaHeight)
                  barHeight = actionsAreaHeight;

                timetableView._layoutActionBar(this, barHeight, eventElement);
                this.style.visibility = "visible";
                this._actionsArea.style.visibility = "visible";
                this._update();
              },

              _hide: function() {
                if (!this._event)
                  return;
                var eventElement = this._part.mainElement;
                this._lastEditedEvent = this._event;
                this._lastEditedPart = this._event;
                this._event = null;
                this._part = null;
                eventElement.removeChild(this);
                this.style.visibility = "hidden";
                this._actionsArea.style.visibility = "hidden";
              }

            });

    var actionsTable = document.createElement("table");
    actionsTable.cellSpacing = "0";
    actionsTable.cellPadding = "0";
    actionsTable.border = "0";
    actionsTable.style.fontSize = "0";
    var tbody = document.createElement("tbody");
    actionsTable.appendChild(tbody);
    var tr = document.createElement("tr");
    tbody.appendChild(tr);
    for (var i = 0, count = actions.length; i < count; i++) {
      var action = actions[i];
      var cell = document.createElement("td");
      O$.assignEvents(cell, action, true);
      O$.extend(cell, {
                vAlign: "middle",
                align: "center",
                id: action.id,
                className: action.style[0],

                _index: i,
                _action: action,
                _image: image,
                _userClickHandler: cell.onclick,

                onmousedown: function() {
                  this._timetableEvent = actionBar._event ? actionBar._event : actionBar._lastEditedEvent;
                  this._timetableEventPart = actionBar._part ? actionBar._part : actionBar._lastEditedPart;
                  if (this._timetableEventPart.mainElement._bringToFront) {
                    this._timetableEventPart.mainElement._bringToFront();
                  }
                },

                onclick: function(e) {
                  e = O$.getEvent(e);
                  e.timetableEvent = this._timetableEvent;
                  var timetableView = actionBar._timetableView;
                  e._timetableView = timetableView;
                  if (this._userClickHandler) {
                    if (this._userClickHandler(e) === false || e.returnValue === false)
                      return;
                  }

                  var eventId = this._timetableEvent.id;
                  var action = this._action;
                  if (action.scope == "page") {
                    O$.setHiddenField(actionBar._timetableView, actionBarId + "::" + this._index, eventId);
                    O$.submitEnclosingForm(actionBar._timetableView);
                  } else if (action.scope == "timetable") {
                    var timetable = timetableView._timetable || timetableView;
                    O$.Ajax._reload([timetable.id], {
                              params: [
                                [actionBarId + "::" + this._index, eventId]
                              ]
                            });
                  } else throw "Unknown action scope: " + action.scope;
                },

                _update: function() {
                  var mouseInside = this._mouseInside;
                  var pressed = this._pressed;
                  O$.setStyleMappings(this, {
                    _rolloverStyle: mouseInside ? this._action.style[1] : null,
                    _pressedStyle: pressed ? this._action.style[2] : null});
                  var userSpecifiedBackground = O$.getStyleClassProperty(this.className, "background-color");
                  if (!userSpecifiedBackground) {
                    var event = actionBar._event ? actionBar._event : actionBar._lastEditedEvent;
                    var part = actionBar._part ? actionBar._part : actionBar._lastEditedPart;
                    var intensity = pressed ? actionPressedIntensity : mouseInside ? actionRolloverIntensity : backgroundIntensity;
                    this.style.backgroundColor = O$.blendColors(part.mainElement._color, "#ffffff", 1 - intensity);
                  } else
                    this.style.backgroundColor = "";

                  var imageUrl = action.image[0];
                  if (pressed) {
                    if (action.image[2])
                      imageUrl = action.image[2];
                  } else if (mouseInside) {
                    if (action.image[1])
                      imageUrl = action.image[1];
                  }
                  this._image = imageUrl;
                }

              });

      action._cell = cell;
      tr.appendChild(cell);
      var image = document.createElement("img");
      image.src = action.image[0];
      if (action.hint)
        image.title = action.hint;
      cell.appendChild(image);
      O$.preloadImage(action.image[0]);
      O$.preloadImage(action.image[1]);
      O$.preloadImage(action.image[2]);


      function setupStateHighlighting(cell) {
        cell._mouseState = O$.setupHoverAndPressStateFunction(cell, function(mouseInside, pressed) {
          cell._mouseInside = mouseInside;
          cell._pressed = pressed;
          cell._update();
        });
      }

      setupStateHighlighting(cell);
    }
    O$.extend(actionsTable, {
              onclick: function(e) {
                O$.cancelEvent(e); // avoid passing event to the absoluteElementsParentNode
              },
              _getHeight: function() {
                if (!this._height) {
                  this._height = O$.getElementSize(this).height;
                }
                return this._height;
              },
              _getWidth: function() {
                if (!this._width) {
                  this._width = O$.getElementSize(this).width;
                }
                return this._width;
              }

            });
    actionBar._actionsArea = actionsTable;
    actionBar._actionsArea.style.position = "absolute";
    actionBar._actionsArea.style.visibility = "hidden";

    actionBar.appendChild(actionsTable);

    actionBar._update = function() {
      actionBar._actionsArea._updatePos();
      actions.forEach(function(action) {
        var cell = action._cell;
        cell._update();
      });
    };

    O$.setupHoverStateFunction(actionBar._actionsArea, function(mouseInside) {
      actionBar._actionsArea._mouseInside = mouseInside;
      if (actionBar._event)
        O$.invokeFunctionAfterDelay(actionBar._event._updateRolloverState, O$.EVENT_ROLLOVER_STATE_UPDATE_TIMEOUT);
    });


    actionBar._actionsArea._updatePos = function() {
      var actionBarSize = O$.getElementSize(actionBar);
      var actionsAreaSize = O$.getElementSize(this);
      this.style.top = "0px";
      this.style.left = actionBarSize.width - actionsAreaSize.width + "px";
      this.style.height = actionBarSize.height + "px";

      O$.correctElementZIndex(this, this._timetableView);
    };
  },

  /*
   This function is invoked from the "delete event" action button. Don't invoke this function directly.
   */
  _deleteCurrentTimetableEvent: function(event) {
    var e = O$.getEvent(event);
    var timetableEvent = e.timetableEvent;
    e._timetableView.deleteEvent(timetableEvent);
    e.returnValue = false;
  },

  _createEventContentElements: function(eventElement, eventContent, eventContentClasses) {
    eventElement._contentElements = [];

    for (var contentIndex = 0; contentIndex < eventContent.length; contentIndex++) {
      var contentPart = eventContent[contentIndex];
      var defaultContentClass = eventContentClasses[contentPart.type];
      var contentClass = O$.combineClassNames([defaultContentClass, contentPart.style]);
      var contentElement = document.createElement("span");
      if (contentClass) {
        contentElement.className = contentClass;
      }
      contentElement._contentPart = contentPart;
      eventElement.appendChild(document.createTextNode(" "));
      eventElement.appendChild(contentElement);
      eventElement._contentElements.push(contentElement);
    }
  },

  _updateEventContentElements: function(eventElement, event, timetable) {
    for (var elementIndex = 0; elementIndex < eventElement._contentElements.length; elementIndex++) {
      var contentElement = eventElement._contentElements[elementIndex];
      if (contentElement._contentPart.type == "name") {
        O$.setInnerText(contentElement, event.name, timetable._escapeEventNames);
      } else if (contentElement._contentPart.type == "description") {
        O$.setInnerText(contentElement, event.description, timetable._escapeEventDescriptions);
      } else if (contentElement._contentPart.type == "resource") {
        var resource = timetable._getResourceForEvent(event);
        var resourceName = resource ? resource.name : "";
        O$.setInnerText(contentElement, resourceName, timetable._escapeEventResources);
      } else if (contentElement._contentPart.type == "time") {
        var timeText = O$.formatTime(event.start) + " - " + O$.formatTime(event.end);
        O$.setInnerText(contentElement, timeText, false);
      } else if (contentElement._contentPart.type == "lineFeed") {
        O$.removeAllChildNodes(contentElement);
        var lineFeed = document.createElement("br");
        contentElement.appendChild(lineFeed);
      }
    }
  },

  _findEventById: function(events, id) {
    if (events._cachedEventsByIds) {
      return events._cachedEventsByIds[id];
    }
    events._cachedEventsByIds = {};
    for (var i = 0, count = events.length; i < count; i++) {
      var event = events[i];
      events._cachedEventsByIds[event.id] = id;
      if (event.id == id)
        return event;
    }
    return null;
  },

  _LazyLoadedTimetableEvents: function(preloadedEvents, preloadedStartTime, preloadedEndTime) {
    O$.Timetable._PreloadedTimetableEvents.call(this, []);

    this._setEvents = this.setEvents;
    this._getEventsForPeriod_raw = this._getEventsForPeriod;
    O$.extend(this, {
              setEvents: function(events, preloadedStartTime, preloadedEndTime) {
                this._setEvents(events);
                this._loadedTimeRangeMap = new O$._RangeMap();
                this._loadingTimeRangeMap = new O$._RangeMap();
                if (!(preloadedStartTime instanceof Date))
                  preloadedStartTime = preloadedStartTime ? O$.parseDateTime(preloadedStartTime).getTime() : null;
                if (!(preloadedEndTime instanceof Date))
                  preloadedEndTime = preloadedEndTime ? O$.parseDateTime(preloadedEndTime).getTime() : null;
                this._loadedTimeRangeMap.addRange(preloadedStartTime, preloadedEndTime);
                this._loadingTimeRangeMap.addRange(preloadedStartTime, preloadedEndTime);
              },

              _getEventsForPeriod: function(start, end, eventsLoadedCallback) {
                if (this._loadedTimeRangeMap.isRangeFullyInMap(start.getTime(), end.getTime()) ||
                        this._loadingTimeRangeMap.isRangeFullyInMap(start.getTime(), end.getTime()))
                  return this._getEventsForPeriod_raw(start, end);

                this._loadingTimeRangeMap.addRange(start.getTime(), end.getTime());
                var thisProvider = this;
                O$.Ajax.requestComponentPortions(this._timeTableView.id, ["loadEvents"],
                        JSON.stringify(
                                {startTime: O$.formatDateTime(start), endTime: O$.formatDateTime(end)},
                                ["startTime", "endTime"]),
                        function(component, portionName, portionHTML, portionScripts, portionData) {
                          var remainingElements = O$.Timetable.replaceDocumentElements(portionHTML, true);
                          if (remainingElements.hasChildNodes())
                            thisProvider._timeTableView._hiddenArea.appendChild(remainingElements);
                          O$.Ajax.executeScripts(portionScripts);

                          var newEvents = portionData.events;
                          thisProvider._loadedTimeRangeMap.addRange(start.getTime(), end.getTime());
                          thisProvider._events._cachedEventsByIds = null;
                          newEvents.forEach(function(newEvent) {
                            var existingEvent = O$.Timetable._findEventById(thisProvider._events, newEvent.id);
                            if (existingEvent)
                              existingEvent._copyFrom(newEvent);
                            else
                              thisProvider.addEvent(newEvent);
                          });
                          if (eventsLoadedCallback) {
                            //        var eventsForPeriod = this._getEventsForPeriod_raw(start, end);
                            eventsLoadedCallback();//eventsForPeriod);
                          }
                        }, function () {
                          // todo: revert addition of time range to this._loadingTimeRangeMap
                          alert('Error loading timetable events');
                        });
                return this._getEventsForPeriod_raw(start, end);
              }

            });

    this.setEvents(preloadedEvents, preloadedStartTime, preloadedEndTime);
  },

  _PreloadedTimetableEvents: function(events) {
    O$.extend(this, {
              _getEventsForPeriod: function(start, end) {
                var result = [];
                var startTime = start.getTime();
                var endTime = end.getTime();
                this._events.forEach(function(event) {
                  if (event.end.getTime() < event.start.getTime())
                    return;
                  if (event.end.getTime() <= startTime ||
                          event.start.getTime() >= endTime)
                    return;
                  result.push(event);
                });
                return result;
              },

              setEvents: function(newEvents) {
                this._events = newEvents;
                newEvents.forEach(function(event) {
                  O$.Timetable._initEvent(event);
                });
                this._events._cachedEventsByIds = null;
              },

              getEventById: function(eventId) {
                for (var i = 0, count = this._events.length; i < count; i++) {
                  var evt = this._events[i];
                  if (evt.id == eventId)
                    return evt;
                }
                return null;
              },

              addEvent: function(event) {
                if (this._events._cachedEventsByIds && event.id)
                  this._events._cachedEventsByIds[event.id] = event;
                O$.Timetable._initEvent(event);
                this._events.push(event);
              },

              deleteEvent: function(event) {
                if (this._events._cachedEventsByIds && event.id)
                  this._events._cachedEventsByIds[event.id] = undefined;

                var eventIndex = this._events.indexOf(event);
                this._events.splice(eventIndex, 1);
              }

            });

    this.setEvents(events);
  },

  compareEventsByStart: function(firstEvent, secondEvent) {
    var result = firstEvent.start - secondEvent.start;
    if (result != 0)
      return result;
    if (firstEvent.name < secondEvent.name)
      return -1;
    if (firstEvent.name > secondEvent.name)
      return 1;
    return 0;
  },

  compareEventsByEnd: function(firstEvent, secondEvent) {
    var result = firstEvent.end - secondEvent.end;
    if (result != 0)
      return result;
    if (firstEvent.name < secondEvent.name)
      return -1;
    if (firstEvent.name > secondEvent.name)
      return 1;
    return 0;
  }
};

/*
 Replaces all elements in htmlPortion if they have an appropriate counterpart with the same Id in the document. All
 elements that couldn't be placed into the document (due to lack of id or a lack of an element with the same id in the
 document) are retained in the returned "div" element. The "div" element itself is just a temporary container and is
 not a part of HTML passed as a parameter.
 */
O$.Timetable.replaceDocumentElements = function(htmlPortion, allowElementsWithNewIds) {
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
    O$.Ajax._pushElementsWithId(newElements, el);
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
};

O$.TimetableViewSwitcher = {
  _init: function(switcherId, timetableId) {
    var switcher = O$(switcherId);
    var timetable = O$(timetableId);
    var prevOnviewtypechange = timetable.onviewtypechange;
    timetable.onviewtypechange = function(e) {
      if (prevOnviewtypechange) prevOnviewtypechange.call(this, e);
      var viewType = timetable.getViewType();
      var viewIndex = timetable._viewIndexByType(viewType);
      switcher.setSelectedIndex(viewIndex);
    }
  }
};
