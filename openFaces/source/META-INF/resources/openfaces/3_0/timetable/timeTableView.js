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

O$.TimeTableView = {};

O$.TimeTableView._init = function(componentId,
                                  day,
                                  locale,
                                  dateFormat,
                                  preloadedEventParams,
                                  resources,
                                  eventAreaSettings,
                                  editable,
                                  onchange,
                                  editingOptions,
                                  stylingParams,
                                  uiEvent) {

  var timeTableView = O$(componentId);
  O$.initComponent(componentId, {rollover: stylingParams.rolloverClass});

  var eventProvider = new O$.Timetable._LazyLoadedTimetableEvents(
          preloadedEventParams.events,
          preloadedEventParams.from,
          preloadedEventParams.to);
  eventProvider._timeTableView = timeTableView;
  timeTableView._eventProvider = eventProvider;

  if (!editingOptions)
    editingOptions = {};
  if (editingOptions.eventResourceEditable === undefined) editingOptions.eventResourceEditable = true;
  if (editingOptions.eventDurationEditable === undefined) editingOptions.eventDurationEditable = true;
  if (editingOptions.defaultEventDuration === undefined || editingOptions.defaultEventDuration <= 0) editingOptions.defaultEventDuration = 30;
  if (editingOptions.autoSaveChanges === undefined) editingOptions.autoSaveChanges = true;

  var reservedTimeEventColor = stylingParams.reservedTimeEventColor ? stylingParams.reservedTimeEventColor : "#b0b0b0";
  var reservedTimeEventClass = O$.combineClassNames(["o_reservedTimeEvent", stylingParams.reservedTimeEventClass]);
  timeTableView._reservedTimeEventClass = reservedTimeEventClass;

  if (!uiEvent)
    uiEvent = {};

  var eventStyleClass = O$.combineClassNames(["o_timetableEvent", timeTableView.DEFAULT_EVENT_CLASS_NAME, uiEvent.style]);
  timeTableView._eventStyleClass = eventStyleClass;
  var rolloverEventClass = O$.combineClassNames(["o_rolloverTimetableEvent", timeTableView.DEFAULT_EVENT_CLASS_NAME, uiEvent.rolloverStyle]);
  timeTableView._rolloverEventClass = rolloverEventClass;

  var eventBackgroundStyleClassName = "o_timetableEventBackground";
  var defaultEventColor = stylingParams.defaultEventColor ? stylingParams.defaultEventColor : "#006ebb";

  var eventBackgroundIntensity = uiEvent.backgroundIntensity !== undefined ? uiEvent.backgroundIntensity : 0.25;
  var eventBackgroundTransparency = uiEvent.backgroundTransparency !== undefined ? uiEvent.backgroundTransparency : 0.2;
  timeTableView._eventBackgroundTransparency = eventBackgroundTransparency;

  timeTableView._scroller = O$(timeTableView.id + "::scroller");
  var table = O$(componentId + "::table");
  timeTableView._table = table;

  var eventContent = uiEvent.content ? uiEvent.content : timeTableView.DEFAULT_EVENT_CONTENT;

  var eventNameClass = O$.combineClassNames(["o_timetableEventName", uiEvent.nameStyle]);
  var eventDescriptionClass = O$.combineClassNames(["o_timetableEventDescription", uiEvent.descriptionStyle]);
  var eventResourceClass = O$.combineClassNames(["o_timetableEventResource", uiEvent.resourceStyle]);
  var eventTimeClass = O$.combineClassNames([timeTableView.DEFAULT_EVENT_NAME_CLASS_NAME, uiEvent.timeStyle]);
  var eventContentClasses = {
    name : eventNameClass,
    description : eventDescriptionClass,
    resource : eventResourceClass,
    time: eventTimeClass
  };

  timeTableView._useResourceSeparation = resources.length > 0;
  timeTableView._getResourceForEvent = function(event) {
    if (!event.resourceId || !this._useResourceSeparation)
      return null;
    var resource = this._resourcesByIds[event.resourceId];
    return resource;
  };

  var hiddenArea = O$(componentId + "::hiddenArea");
  timeTableView._hiddenArea = hiddenArea;

  // RichFaces-OpenFaces-JSON compatibility workaround
  // Since some of RichFaces components use JavaScript defining Array.prototype.toJSON method,
  // JSON.stringify([1, 2]) call will produce '"[1,2]"' string instead of expected '["1","2"]'.
  // Therefore perform manual array conversion here.
  timeTableView._updateTimetableChangesField = function() {

    var properties = ["id", "name", "description", "resourceId", "startStr", "endStr", "color", "type"];

    function arrayToJSON(name, source, properties) {
      var result = [];
      for (var i = 0; i < source.length; i++)
        result[i] = JSON.stringify(source[i], properties);
      return JSON.stringify(name) + ":[" + result.join(",") + "]";
    }

    var events = [
      arrayToJSON("addedEvents", this._addedEvents, properties),
      arrayToJSON("editedEvents", this._editedEvents, properties),
      arrayToJSON("removedEventIds", this._removedEventIds, properties)
    ];
    var changesAsString = "{" + events.join(",") + "}";
    O$.setHiddenField(this, this.id + "::timetableChanges", changesAsString);
  };

  var ieEventHandlerLayer = O$.isExplorer() ? O$.createAbsolutePositionedElement(timeTableView._scroller) : null;
  var absoluteElementsParentNode = O$.createAbsolutePositionedElement(timeTableView._scroller);
  timeTableView._absoluteElementsParentNode = absoluteElementsParentNode;
  absoluteElementsParentNode.style.overflow = "hidden";
  absoluteElementsParentNode._updatePos = function() {
    var tableRect = O$.getElementBorderRectangle(table, true);
    var rect = new O$.Rectangle(0, 0, tableRect.width, tableRect.height);
    O$.setElementBorderRectangle(absoluteElementsParentNode, rect);
    if (ieEventHandlerLayer)
      O$.setElementBorderRectangle(ieEventHandlerLayer, rect);
  };
  absoluteElementsParentNode.onclick = function(e) {
    var clickPoint = O$.getEventPoint(e);
    var cell = table._cellFromPoint(clickPoint.x, clickPoint.y, false);
    if (cell && cell.onclick)
      cell.onclick(e);
  };
  if (ieEventHandlerLayer) {
    O$.fixIEEventsForTransparentLayer(ieEventHandlerLayer);
    ieEventHandlerLayer.onclick = function(e) {
      absoluteElementsParentNode.onclick(e);
    };
  }

  timeTableView._showEventActionBar = function(event) {
    var eventElement = event.mainElement;
    var actionBar = this._getEventActionBar();
    actionBar._event = event;
    var userSpecifiedStyles = O$.getStyleClassProperties(actionBar._userSpecifiedClass, ["color", "background-color"]);
    actionBar.style.backgroundColor = userSpecifiedStyles.backgroundColor
            ? userSpecifiedStyles.backgroundColor
            : O$.blendColors(eventElement._color, "#ffffff", 1 - actionBar._inactiveSegmentIntensity);
    eventElement.appendChild(actionBar);
    actionBar.style.height = "";
    actionBar.style.width = "";
    var barHeight = actionBar.offsetHeight;
    var actionsAreaHeight = actionBar._actionsArea._getHeight();
    if (barHeight < actionsAreaHeight)
      barHeight = actionsAreaHeight;

    this._layoutActionBar(actionBar, barHeight, eventElement);
    actionBar.style.visibility = "visible";
    actionBar._actionsArea.style.visibility = "visible";
    actionBar._update();
  };

  timeTableView.getDay = function() {
    return this._day;
  };

  timeTableView.setDay = function(day) {
    if (O$._datesEqual(this._day, day))
      return;
    if (this._onDayChange) {
      this._onDayChange(day);
    }

    var dtf = O$.getDateTimeFormatObject(locale);
    O$.setHiddenField(this, this.id + "::day", dtf.format(day, "dd/MM/yyyy"));

    this._day = day;

    this._updateStartEndTime();

    this._events = this._eventProvider._getEventsForPeriod(this._startTime, this._endTime, function() {
      timeTableView._updateEventElements(true, true);
    });

    this._updateEventElements();

    this._updateTimeForCells();
  };

  timeTableView._baseZIndex = O$.getElementZIndex(timeTableView);
  timeTableView._maxZIndex = timeTableView._baseZIndex + 10;
  timeTableView._updateEventZIndexes = function() {
    if (!this._eventElements)
      return;
    for (var elementIndex = 0, elementCount = this._eventElements.length; elementIndex < elementCount; elementIndex++) {
      var eventElement = this._eventElements[elementIndex];
      var eventZIndex = this._baseZIndex + (elementIndex + 1) * 5;
      eventElement.style.zIndex = eventZIndex;
      eventElement._backgroundElement.style.zIndex = eventZIndex - 1;
      if (eventElement._updateZIndex)
        eventElement._updateZIndex(eventZIndex);
    }
    this._maxZIndex = eventZIndex + 10;
  };

  timeTableView._addEvent = function(startTime, resourceId) {
    var endTime = O$.cloneDateTime(startTime);
    endTime.setMinutes(startTime.getMinutes() + editingOptions.defaultEventDuration);
    var event = {
      name: "",
      resourceId: resourceId,
      start: startTime,
      end: endTime,
      color: null,
      description: ""
    };
    O$.Timetable._initEvent(event);
    event._creationInProgress = true;
    timeTableView._getEventEditor().run(event, "create");
    return event;

  };

  timeTableView.updateLayout = function() {
    this._cachedPositions = {};
    this._declaredTimeTableViewHeight = O$.getStyleClassProperty(this.className, "height");
    if (this._declaredTimeTableViewHeight === undefined) {
      this._scroller.style.overflow = "visible";
      this._scroller.style.overflowX = "visible";
      this._scroller.style.overflowY = "hidden";
      this._scroller.style.height = "auto";
    } else {
      if (O$.isOpera()) {
        this._scroller.style.overflow = "scroll"; // Opera < 9.5 doesn't understand specifying just overflow-y
      }
    }

    this._updateHeightForFF();
    this._accountForScrollerWidth();
    this._absoluteElementsParentNode._updatePos();
    this._updateEventsPresentation();

  };

  O$.addInternalLoadEvent(function() {
    timeTableView.updateLayout(); // update positions after layout changes that might have had place during loading
  });

  O$.addEventHandler(window, "resize", function() {
    timeTableView.updateLayout();
  });

  timeTableView._updateHeightForFF = function() {
    // FireFox's scroller height includes the entire table without truncating it according to
    // user-specified height in day table, so we should truncate height ourselves
    if (!O$.isMozillaFF())
      return;

    if (this._declaredTimeTableViewHeight === undefined)
      return;
    var scrollerHeight = this._declaredTimeTableViewHeight;
    if (!O$.stringEndsWith(this._declaredTimeTableViewHeight, "%")) {
      var height = O$.calculateNumericCSSValue(this._declaredTimeTableViewHeight);
      if (height) {
        var offset = O$.getElementPos(this._scroller).y - O$.getElementPos(this).y;
        height -= offset;
        scrollerHeight = height + "px";
      }
    }
    this._scroller.style.height = scrollerHeight;
  };

  timeTableView._getEventActionBar = function() {
    if (!this._eventActionBar) {
      this._eventActionBar = O$(this.id + ":_eventActionBar");
      if (!this._eventActionBar)
        return null;
      this._eventActionBar.style.position = "absolute";
      this._eventActionBar.style.visibility = "hidden";

    }
    return this._eventActionBar;
  };

  timeTableView._getEventPreview = function() {
    if (!this._eventPreview) {
      this._eventPreview = O$(this.id + ":_eventPreview");
    }
    return this._eventPreview;
  };

  //1044
  timeTableView._hideEventActionBar = function() {
    var actionBar = this._getEventActionBar();
    if (!actionBar._event)
      return;
    var eventElement = actionBar._event.mainElement;
    actionBar._lastEditedEvent = actionBar._event;
    actionBar._event = null;
    eventElement.removeChild(actionBar);
    actionBar.style.visibility = "hidden";
    actionBar._actionsArea.style.visibility = "hidden";
  };


  //1254
  timeTableView._putTimetableChanges = function(addedEvents, changedEvents, removedEventIds, initialAssignment) {
    if (!this._addedEvents) this._addedEvents = [];
    if (!this._editedEvents) this._editedEvents = [];
    if (!this._removedEventIds) this._removedEventIds = [];
    if (addedEvents)
      this._addedEvents = this._addedEvents.concat(addedEvents);
    if (changedEvents) {
      for (var editedIdx = 0, editedCount = changedEvents.length; editedIdx < editedCount; editedIdx++) {
        var editedEvent = changedEvents[editedIdx];
        if (O$.findValueInArray(editedEvent, this._addedEvents) != -1)
          continue;
        if (O$.findValueInArray(editedEvent, this._editedEvents) == -1) {
          this._editedEvents.push(editedEvent);
        }
      }
    }
    if (removedEventIds) {
      function eventIndexById(events, eventId) {
        for (var i = 0, count = events.length; i < count; i++) {
          var event = events[i];
          if (event.id == eventId)
            return i;
        }
        return -1;
      }

      for (var removedIdx = 0, removedCount = removedEventIds.length; removedIdx < removedCount; removedIdx++) {
        var removedEventId = removedEventIds[removedIdx];
        var addedEventIndex = eventIndexById(this._addedEvents, removedEventId);
        if (addedEventIndex != -1) {
          this._addedEvents.splice(addedEventIndex, 1);
          continue;
        }
        var editedEventIndex = eventIndexById(this._editedEvents, removedEventId);
        if (editedEventIndex != -1)
          this._editedEvents.splice(editedEventIndex, 1);
        this._removedEventIds.push(removedEventId);
      }
    }

    this._updateTimetableChangesField();

    if (this.onchange) {
      var e = O$.createEvent("change");
      e.addedEvents = addedEvents ? addedEvents : {};
      e.changedEvents = changedEvents ? changedEvents : {};
      e.removedEventIds = removedEventIds ? removedEventIds : {};

      if (this.onchange(e) === false)
        return;
    }

    if (editingOptions.autoSaveChanges && !initialAssignment)
      this.saveChanges();
  };

  timeTableView._addEventElement = function(event) {
    var eventElement = document.createElement("div");
    eventElement._event = event;
    event.mainElement = eventElement;

    O$.Timetable._createEventContentElements(eventElement, eventContent, eventContentClasses);

    eventElement._attachAreas = function() {
      eventElement._areas = [];
      for (var areaIndex = 0, areaCount = eventAreaSettings.length; areaIndex < areaCount; areaIndex++) {
        var areaSettings = eventAreaSettings[areaIndex];
        var areaId = areaSettings.id;
        var areaClientId = timeTableView.id + "::" + event.id + ":" + areaId;
        var area = O$(areaClientId);
        if (!area)
          continue;
        area.onmousedown = O$.stopEvent;
        area.onclick = O$.stopEvent;
        eventElement._areas.push(area);

        var putAreaInElement = O$.isAlignmentInsideOfElement(areaSettings.horizontalAlignment, areaSettings.verticalAlignment);
        if (putAreaInElement) {
          var firstEventChild = eventElement.firstChild;
          if (firstEventChild) {
            // insert as a first element to allow configuring areas as floating elements by the element's top edge using
            // area styling attributes, e.g. "position: static; float: right"
            eventElement.insertBefore(area, firstEventChild);
          } else
            eventElement.appendChild(area);
        } else
          absoluteElementsParentNode.appendChild(area);
        area._insideElement = putAreaInElement;
        area._settings = areaSettings;
        area._updatePos = function() {
          O$.alignPopupByElement(this, eventElement, this._settings.horizontalAlignment, this._settings.verticalAlignment, 0, 0, true, true);
        };
      }
    };
    eventElement._attachAreas();

    eventElement._updateAreaPositions = function(forceInsideAreasUpdate) {
      for (var areaIndex = 0, areaCount = this._areas.length; areaIndex < areaCount; areaIndex++) {
        var area = this._areas[areaIndex];
        if (!area._insideElement || forceInsideAreasUpdate)
          area._updatePos();
      }
    };
    eventElement._updateAreaZIndexes = function(eventZIndex) {
      if (eventZIndex === undefined)
        eventZIndex = O$.getNumericElementStyle(event.mainElement, "z-index");

      for (var areaIndex = 0, areaCount = this._areas.length; areaIndex < areaCount; areaIndex++) {
        var area = this._areas[areaIndex];
        area.style.zIndex = eventZIndex + 1;
      }
    };

    O$.assignEvents(eventElement, uiEvent, true, {timetableEvent: event});
    eventElement._onmouseover = eventElement.onmouseover;
    eventElement._onmouseout = eventElement.onmouseout;
    eventElement.onmouseover = eventElement.onmouseout = null;

    var eventClass = event.type != "reserved" ? eventStyleClass : reservedTimeEventClass;
    if (O$.isExplorer())
      O$.combineClassNames([eventClass, "o_explicitly_transparent_background"]);
    eventElement.className = eventClass;
    eventElement.style.margin = "0"; // margin in CSS is for calculating eventsLeftOffset/eventsRightOffset only -- it should be reset to avoid unwanted effects
    eventElement.style.zIndex = this._baseZIndex + 5;

    eventElement._backgroundElement = document.createElement("div");
    eventElement._backgroundElement.className = eventBackgroundStyleClassName;
    eventElement._backgroundElement.style.zIndex = this._baseZIndex + 4;
    event.backgroundElement = eventElement._backgroundElement;


    eventElement._updateShape = function() {
      if (event.type != "reserved") {
        O$.Timetable._updateEventContentElements(eventElement, event, timeTableView);
      }

      var calculatedEventColor = event.color ? event.color : defaultEventColor;
      if (event.type == "reserved") {
        calculatedEventColor = reservedTimeEventColor;
      }
      eventElement._color = calculatedEventColor;
      eventElement._backgroundColor = O$.blendColors(eventElement._color, "#ffffff", 1 - eventBackgroundIntensity);

      var userSpecifiedStyles = O$.getStyleClassProperties(
              event.type != "reserved"
                      ? (uiEvent.style ? uiEvent.style : stylingParams.eventClass)
                      : stylingParams.reservedTimeEventClass,
              ["color", "background-color", "border-color"]);
      eventElement._backgroundElement.style.backgroundColor = userSpecifiedStyles.backgroundColor
              ? userSpecifiedStyles.backgroundColor : eventElement._backgroundColor;
      var elementStyles = O$.getElementStyle(eventElement, ["border-radius", "-moz-border-radius-topleft", "-webkit-border-top-left-radius"]);
      eventElement._backgroundElement.style.borderRadius = elementStyles.borderRadius;
      eventElement._backgroundElement.style.MozBorderRadius = elementStyles.MozBorderRadiusTopleft;
      eventElement._backgroundElement.style.WebkitBorderRadius = elementStyles.WebkitBorderTopLeftRadius;

      O$.setOpacityLevel(eventElement._backgroundElement, 1 - eventBackgroundTransparency);
      eventElement.style.color = userSpecifiedStyles.color ? userSpecifiedStyles.color : eventElement._color;
      eventElement.style.borderColor = userSpecifiedStyles.borderColor ? userSpecifiedStyles.borderColor : eventElement._color;
    };

    var eventPreview = timeTableView._getEventPreview();
    event._setMouseInside = function(value) {
      if (event._mouseInside == value)
        return;
      event._mouseInside = value;
      if (event._isEventUpdateNotAllowed())
        return;
      if (value) {
        O$.setStyleMappings(eventElement, {_rolloverStyle: rolloverEventClass});
        eventElement._updateAreaPositionsAndBorder();
        timeTableView._showEventActionBar(event);

        if (eventPreview) {
          setTimeout(function() {
            if (event._isEventPreviewAllowed())
              eventPreview.showForEvent(event);
          }, eventPreview._showingDelay);
        }
        if (eventElement._onmouseover) {
          eventElement._onmouseover(O$.createEvent("mouseover"));
        }
      } else {
        O$.setStyleMappings(eventElement, {_rolloverStyle: null});
        eventElement._updateAreaPositionsAndBorder();
        timeTableView._hideEventActionBar();

        if (eventPreview) {
          eventPreview.hide();
        }
        if (eventElement._onmouseout) {
          eventElement._onmouseout(O$.createEvent("mouseout"));
        }
      }
    };

    O$.setupHoverStateFunction(eventElement, function(mouseInside) {
      if (event._creationInProgress)
        return;
      eventElement._mouseInside = mouseInside;
      O$.invokeFunctionAfterDelay(event._updateRolloverState, timeTableView.EVENT_ROLLOVER_STATE_UPDATE_TIMEOUT);
    });

    if (editable) {
      eventElement.onclick = function(e) {
        O$.breakEvent(e);
        if (event._isEventUpdateNotAllowed())
          return;
        event._beforeUpdate();
        timeTableView._getEventEditor().run(event, "update");
      };
    }

    if (eventElement.oncreate)
      eventElement.oncreate(O$.createEvent("create"));
    absoluteElementsParentNode.appendChild(eventElement);
    absoluteElementsParentNode.appendChild(eventElement._backgroundElement);

    return eventElement;
  };

  timeTableView._removeEventElement = function(event) {
    if (!event.mainElement)
      return;
    if (timeTableView._getEventActionBar()._event == event)
      timeTableView._hideEventActionBar();

    if (event.mainElement._removeNodes)
      event.mainElement._removeNodes();
    event.mainElement.parentNode.removeChild(event.mainElement);
    var backgroundElement = event.mainElement._backgroundElement;
    backgroundElement.parentNode.removeChild(backgroundElement);
    event.mainElement._event = null;
    event.mainElement = null;
    event.backgroundElement = null;
  };

  timeTableView.cancelEventCreation = function(event) {
    event._creationInProgress = undefined;
    this._removeEventElement(event);
  };

  timeTableView.addEvent = function(event) {
    event.setStart(event.start, event.startStr);
    event.setEnd(event.end, event.endStr);
    if (event._creationInProgress) {
      event._creationInProgress = undefined;
      this._removeEventElement(event);
    }

    eventProvider.addEvent(event);
    this._updateEventElements(true);
    this._putTimetableChanges([event], null, null);
  };

  timeTableView.deleteEvent = function(event) {
    eventProvider.deleteEvent(event);
    this._updateEventElements(true);
    this._putTimetableChanges(null, null, [event.id]);
  };

  timeTableView.getEventById = function(eventId) {
    return eventProvider.getEventById(eventId);
  };

  timeTableView.updateEvent = function(event) {
    event.setStart(event.start, event.startStr);
    event.setEnd(event.end, event.endStr);

    if (!(this._updateCellEventElements === undefined)) {
      if (event._oldStart) {
        this._updateCellEventElements(event._oldStart);
      }
      this._updateCellEventElements(event.start);
    }

    event.updatePresentation();
    this._putTimetableChanges(null, [event], null);
  };

  timeTableView.refreshEvents = function(serverAction) {
    this._saveChanges(true, serverAction, this._startTime, this._endTime);
  };

  timeTableView.saveChanges = function() {
    this._saveChanges(false, null, this._startTime, this._endTime);
  };

  timeTableView._saveChanges = function(reloadAllEvents, serverAction, reloadStartTime, reloadEndTime) {
    O$.requestComponentPortions(this.id, ["saveEventChanges"], JSON.stringify(
    {reloadAllEvents: !!reloadAllEvents, startTime: O$.formatDateTime(reloadStartTime), endTime: O$.formatDateTime(reloadEndTime)},
            ["reloadAllEvents", "startTime", "endTime"]), function(
            component, portionName, portionHTML, portionScripts, portionData) {
      var remainingElements = O$.replaceDocumentElements(portionHTML, true);
      if (remainingElements.hasChildNodes())
        hiddenArea.appendChild(remainingElements);
      O$.executeScripts(portionScripts);

      if (portionData.reloadedEvents) {
        eventProvider.setEvents(portionData.reloadedEvents, reloadStartTime, reloadEndTime);
        timeTableView._updateEventElements(true, true);
      } else {
        if (portionData.addedEvents) {
          var addedCount = portionData.addedEvents.length;
          O$.assertEquals(timeTableView._addedEvents.length, addedCount, "addedEventCount should be same as timeTableView._addedEvents.length");
          for (var addedIdx = 0; addedIdx < addedCount; addedIdx++) {
            var addedEvent = timeTableView._addedEvents[addedIdx];
            addedEvent._copyFrom(portionData.addedEvents[addedIdx]);
            addedEvent.updatePresentation(0 /*dragAndDropCancelingPeriod*/);
            addedEvent.mainElement._attachAreas();
            addedEvent.mainElement._updateAreaPositions();
          }
        }
        if (portionData.editedEvents) {
          var editedCount = portionData.editedEvents.length;
          O$.assertEquals(timeTableView._editedEvents.length, editedCount, "editedEventCount should be same as timeTableView._editedEvents.length");
          for (var editedIdx = 0; editedIdx < editedCount; editedIdx++) {
            var editedEvent = timeTableView._editedEvents[editedIdx];
            editedEvent._copyFrom(portionData.editedEvents[editedIdx]);
            editedEvent.mainElement._attachAreas();
            editedEvent.updatePresentation(0 /*dragAndDropCancelingPeriod*/);
          }
        }
      }

      timeTableView._addedEvents = [];
      timeTableView._editedEvents = [];
      timeTableView._removedEventIds = [];
      timeTableView._updateTimetableChangesField();
    }, function () {
      alert('Error saving the last timetable change');
    }, serverAction);
  };


};