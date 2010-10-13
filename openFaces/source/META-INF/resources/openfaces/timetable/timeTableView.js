/*
 * OpenFaces - JSF Component Library 3.0
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

    function eventToJSON(event){
      var result = JSON.stringify(event, properties);
      if (event.customProperties !== undefined){
        //add custom properties
        //associative arrays are not supported by stringify method
        var customPropertiesJSON = "{";
        for (var key in event.customProperties) {
          customPropertiesJSON = customPropertiesJSON + JSON.stringify(key) + ":" +
                  JSON.stringify(event.customProperties[key]) + ",";
        }
        customPropertiesJSON =   customPropertiesJSON.slice(0, customPropertiesJSON.length-1) + "}"; //remove last ',';
        result = result.slice(0, result.length-1); //remove last '{';
        result = result + ", customProperties: "+ customPropertiesJSON+"}"
      }
      return result;
    }

    function arrayToJSON(name, source) {
      var result = [];
      for (var i = 0; i < source.length; i++)
        result[i] = eventToJSON(source[i]);
      return JSON.stringify(name) + ":[" + result.join(",") + "]";
    }

    var events = [
      arrayToJSON("addedEvents", this._addedEvents),
      arrayToJSON("editedEvents", this._editedEvents),
      arrayToJSON("removedEventIds", this._removedEventIds)
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

  timeTableView._showEventActionBar = function(event, part) {
    var eventElement = part.mainElement;
    var actionBar = this._getEventActionBar();
    actionBar._event = event;
    actionBar._part = part;
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
      var eventZIndex = timeTableView._baseZIndex + (elementIndex + 1) * 5;
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

  timeTableView._hideEventActionBar = function() {
    var actionBar = this._getEventActionBar();
    if (!actionBar._event)
      return;
    var eventElement = actionBar._part.mainElement;
    actionBar._lastEditedEvent = actionBar._event;
    actionBar._lastEditedPart = actionBar._event;
    actionBar._event = null;
    actionBar._part = null;
    eventElement.removeChild(actionBar);
    actionBar.style.visibility = "hidden";
    actionBar._actionsArea.style.visibility = "hidden";
  };


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


  timeTableView._splitIntoParts = function(event) {
    var parts = [];
    var start = this._startTime < event.start ? event.start : this._startTime;
    var end = this._endTime < event.end ? this._endTime : event.end;
    var partStart = start;
    var partEnd = O$.incDay(new Date(partStart.getFullYear(), partStart.getMonth(), partStart.getDate()));
    var i = 0;
    do{
      var part = {start:partStart, end: (partEnd < end) ? partEnd : end, index: i++, event: event};
      parts.push(part);
      partStart = O$.cloneDate(partEnd);
      partEnd = O$.incDay(O$.cloneDate(partEnd));
    } while (partStart < end);

    parts[0].first = true;
    parts[parts.length - 1].last = true;

    return parts;
  };

  timeTableView._addEventElements = function(event) {
    var parts = timeTableView._splitIntoParts(event);
    event.parts = parts;

    event._attachAreas = function() {
      event._areas = [];
      for (var areaIndex = 0, areaCount = eventAreaSettings.length; areaIndex < areaCount; areaIndex++) {
        var areaSettings = eventAreaSettings[areaIndex];
        var areaId = areaSettings.id;
        var areaClientId = timeTableView.id + "::" + event.id + ":" + areaId;
        var area = O$(areaClientId);
        if (!area)
          continue;
        area.onmousedown = O$.stopEvent;
        area.onclick = O$.stopEvent;
        event._areas.push(area);

        area._insideElement = O$.isAlignmentInsideOfElement(areaSettings.horizontalAlignment, areaSettings.verticalAlignment);
        area._inTail = O$.isAlignmentInTail(areaSettings.verticalAlignment);

        area._settings = areaSettings;
        area._settings = areaSettings;
        area._updatePos = function() {
          var eventElement = (this._inTail) ? event.parts[event.parts.length - 1].mainElement : event.parts[0].mainElement;
          if (!eventElement){
            return;
          }
          if (this._insideElement) {
            var firstEventChild = eventElement.firstChild;
            if (firstEventChild) {
              // insert as a first element to allow configuring areas as floating elements by the element's top edge using
              // area styling attributes, e.g. "position: static; float: right"
              eventElement.insertBefore(this, firstEventChild);
            } else
              eventElement.appendChild(this);
          } else
            absoluteElementsParentNode.appendChild(this);
          O$.alignPopupByElement(this, eventElement, this._settings.horizontalAlignment, this._settings.verticalAlignment, 0, 0, true, true);
        };
      }
    };

    event._updateAreaPositions = function(forceInsideAreasUpdate) {
      for (var areaIndex = 0, areaCount = this._areas.length; areaIndex < areaCount; areaIndex++) {
        var area = this._areas[areaIndex];
        if (!area._insideElement || forceInsideAreasUpdate)
          area._updatePos();
      }
    };
    event._updateAreaZIndexes = function() {
      for (var areaIndex = 0, areaCount = this._areas.length; areaIndex < areaCount; areaIndex++) {
        var area = this._areas[areaIndex];
        var eventElement = (area._inTail) ? event.parts[event.parts.length - 1].mainElement : event.parts[0].mainElement;
        if (eventElement){
          var eventZIndex = O$.getNumericElementStyle(eventElement, "z-index");
          area.style.zIndex = eventZIndex + 1;
        }
      }
    };

    event._attachAreas();

    for (var i = 0; i < event.parts.length; i++) {
      timeTableView._addEventElement(event, event.parts[i]);
    }

    event.updatePresentation = function(transitionPeriod) {
      var newParts = timeTableView._splitIntoParts(event, false);

      if (newParts.length != event.parts.length) {
        var eventElementsLength = timeTableView._eventElements.length;
        var draggablePart;
        for (var i = 0; i < event.parts.length; i++) {
          var part = event.parts[i];
          if (part.mainElement._draggingInProgress) {
            draggablePart = part;
          } else {
            timeTableView._removeEventElement(event, part);
          }
        }
        eventElementsLength = timeTableView._eventElements.length;
        event.parts = newParts;

        var draggablePartIndex = -1;
        if (draggablePart) {
          draggablePartIndex = event._getDraggablePartIndex();
        }
        for (i = 0; i < event.parts.length; i++) {
          part = event.parts[i];
          if (i == draggablePartIndex) {
            draggablePart.start = part.start;
            draggablePart.end = part.end;
            draggablePart.index = i;
            event.parts[i] = draggablePart;
          } else {
            timeTableView._addEventElement(event, part);
          }
        }
        if (draggablePart) {
          draggablePart.mainElement._update(0);
        }

        timeTableView._updateEventZIndexes();
        event._updateAreaPositions(true);
        event._updateRolloverState();

      } else {
        for (i = 0; i < event.parts.length; i++) {
          var oldPart = event.parts[i];
          var newPart = newParts[i];
          oldPart.start = newPart.start;
          oldPart.end = newPart.end;
          oldPart.mainElement._update(transitionPeriod);
        }
        event._updateAreaPositions(true);
      }

    };


    event._removeEventElements = function(refreshAreasAfterReload) {
      parts.forEach(function(part) {
        timeTableView._removeEventElement(event, part);
      });
      var areas = event._areas;
      areas.forEach(function(area) {
        if (area.parentNode == null)
          return; // don't add an area back to document if it was already removed by Ajax
        timeTableView._hiddenArea.appendChild(area);
      });
      if (refreshAreasAfterReload) {
        event._attachAreas();
      }
    };

    var eventPreview = timeTableView._getEventPreview();
    event._setMouseInside = function(value) {
      if (event._mouseInside != value && event._isEventUpdateNotAllowed()) {
        return;
      }
      event._mouseInside = value;
      for (var i = 0; i < event.parts.length; i++) {
        var part = event.parts[i];
        var eventElement = event.parts[i].mainElement;

        if (value) {
          O$.setStyleMappings(eventElement, {_rolloverStyle: timeTableView._rolloverEventClass});
          event._updateAreaPositionsAndBorder();
          if (part.last) {
            timeTableView._showEventActionBar(event, part);
          }

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
          event._updateAreaPositionsAndBorder();
          timeTableView._hideEventActionBar();

          if (eventPreview) {
            eventPreview.hide();
          }

          if (eventElement._onmouseout) {
            eventElement._onmouseout(O$.createEvent("mouseout"));
          }
        }
      }
    };


  };

  timeTableView._addEventElement = function(event, part) {
    var eventElement = document.createElement("div");
    if (!timeTableView._eventElements) {
      timeTableView._eventElements = [];
    }
    timeTableView._eventElements.push(eventElement);
    eventElement._event = event;
    part.mainElement = eventElement;

    O$.Timetable._createEventContentElements(eventElement, eventContent, eventContentClasses);


    O$.assignEvents(eventElement, uiEvent, true, {timetableEvent: event});
    eventElement._onmouseover = eventElement.onmouseover;
    eventElement._onmouseout = eventElement.onmouseout;
    eventElement.onmouseover = eventElement.onmouseout = null;

    var eventClass = event.type != "reserved" ? eventStyleClass : reservedTimeEventClass;
    if (O$.isExplorer())
      O$.combineClassNames([eventClass, "o_explicitly_transparent_background"]);
    eventElement.className = eventClass;
    eventElement.style.margin = "0"; // margin in CSS is for calculating eventsLeftOffset/eventsRightOffset only -- it should be reset to avoid unwanted effects
    eventElement.style.zIndex = timeTableView._baseZIndex + 5;

    eventElement._backgroundElement = document.createElement("div");
    eventElement._backgroundElement.className = eventBackgroundStyleClassName;
    eventElement._backgroundElement.style.zIndex = timeTableView._baseZIndex + 4;
    part.backgroundElement = eventElement._backgroundElement;


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

  timeTableView._removeEventElement = function(event, part) {
    if (!part.mainElement)
      return;

    var index = O$.findValueInArray(part.mainElement, timeTableView._eventElements);
    timeTableView._eventElements.splice(index, 1);
    if (timeTableView._getEventActionBar()._event == event)
      timeTableView._hideEventActionBar();

    if (part.mainElement._removeNodes)
      part.mainElement._removeNodes();
    part.mainElement.parentNode.removeChild(part.mainElement);
    var backgroundElement = part.mainElement._backgroundElement;
    backgroundElement.parentNode.removeChild(backgroundElement);
    part.mainElement._event = null;
    part.mainElement = null;
    part.backgroundElement = null;
  };

  timeTableView.cancelEventCreation = function(event) {
    event._creationInProgress = undefined;
    if (event._removeEventElements){
      event._removeEventElements();
    }
  };

  timeTableView.addEvent = function(event) {
    event.setStart(event.start, event.startStr);
    event.setEnd(event.end, event.endStr);
    if (event._creationInProgress) {
      event._creationInProgress = undefined;
      event._removeEventElements();
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
    O$.Ajax.requestComponentPortions(this.id, ["saveEventChanges"], JSON.stringify(
    {reloadAllEvents: !!reloadAllEvents, startTime: O$.formatDateTime(reloadStartTime), endTime: O$.formatDateTime(reloadEndTime)},
            ["reloadAllEvents", "startTime", "endTime"]), function(
            component, portionName, portionHTML, portionScripts, portionData) {
      var remainingElements = O$.Timetable.replaceDocumentElements(portionHTML, true);
      if (remainingElements.hasChildNodes())
        hiddenArea.appendChild(remainingElements);
      O$.Ajax.executeScripts(portionScripts);

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
            addedEvent._attachAreas();
            addedEvent._updateAreaPositions();
          }
        }
        if (portionData.editedEvents) {
          var editedCount = portionData.editedEvents.length;
          O$.assertEquals(timeTableView._editedEvents.length, editedCount, "editedEventCount should be same as timeTableView._editedEvents.length");
          for (var editedIdx = 0; editedIdx < editedCount; editedIdx++) {
            var editedEvent = timeTableView._editedEvents[editedIdx];
            editedEvent._copyFrom(portionData.editedEvents[editedIdx]);
            editedEvent._attachAreas();
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