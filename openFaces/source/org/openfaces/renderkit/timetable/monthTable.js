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

// ========== implementation

O$.MonthTable = {};

O$.MonthTable.EVENT_ROLLOVER_STATE_UPDATE_TIMEOUT = 1;

O$.MonthTable.DEFAULT_EVENT_CONTENT = [ { type : "time"}, { type : "name" }, { type : "resource" } ];

O$.MonthTable._init = function(componentId,
                               day,
                               locale,
                               dateFormat,
                               scrollOffset,
                               preloadedEventParams,
                               resources,
                               eventAreaSettings,
                               editable,
                               onchange,
                               editingOptions,
                               stylingParams,
                               uiEvent,
                               calendarOptions
        ) {

  var monthTable = O$(componentId);

  if (O$.isExplorer()) {
    if (!monthTable._initScheduled) {
      monthTable._initScheduled = true;
      var initArgs = arguments;
      // postpone initialization to avoid IE failure during page loading
      O$.addInternalLoadEvent(function() {
        O$.MonthTable._init.apply(null, initArgs);
      });
      return;
    }
  }

  O$.initComponent(componentId, {rollover: stylingParams.rolloverClass});

  var eventProvider = new O$.MonthTable._LazyLoadedTimetableEvents(
          preloadedEventParams.events,
          preloadedEventParams.from,
          preloadedEventParams.to);
  eventProvider._monthTable = monthTable;
  var dateTimeFormat = O$.getDateTimeFormatObject(locale);

  if (!editingOptions)
    editingOptions = {};
  if (editingOptions.eventResourceEditable === undefined) editingOptions.eventResourceEditable = true;
  if (editingOptions.eventDurationEditable === undefined) editingOptions.eventDurationEditable = true;
  if (editingOptions.defaultEventDuration === undefined || editingOptions.defaultEventDuration <= 0) editingOptions.defaultEventDuration = 30;
  if (editingOptions.autoSaveChanges === undefined) editingOptions.autoSaveChanges = true;
  if (editingOptions.overlappedEventsAllowed === undefined) editingOptions.overlappedEventsAllowed = true;

  var reservedTimeEventColor = stylingParams.reservedTimeEventColor ? stylingParams.reservedTimeEventColor : "#b0b0b0";
  var reservedTimeEventClass = O$.combineClassNames(["o_reservedTimeEvent", stylingParams.reservedTimeEventClass]);
  var moreLinkElementClass = O$.combineClassNames(["o_moreLinkElement", stylingParams.moreLinkElementClass]);
  var moreLinkClass = O$.combineClassNames(["o_moreLink", stylingParams.moreLinkClass]);
  var moreLinkText = stylingParams.moreLinkText ? stylingParams.moreLinkText : "More...";

  if (!uiEvent)
    uiEvent = {};

  var weekdayHeadersRowClass = O$.combineClassNames(["o_weekdayHeadersRow", stylingParams.weekdayHeadersRowClass]);
  var weekdayStyle = O$.combineClassNames(["o_weekdayText", stylingParams.weekdayClass]);
  var weekdayPattern = stylingParams.weekdayPattern ? stylingParams.weekdayPattern : "EEEE";

  var eventStyleClass = O$.combineClassNames(["o_timetableEvent", "o_monthTableEvent", uiEvent.style]);
  var rolloverEventClass = O$.combineClassNames(["o_rolloverTimetableEvent", "o_monthTableEvent", uiEvent.rolloverStyle]);

  var eventNameClass = O$.combineClassNames(["o_monthTableEventName", uiEvent.nameStyle]);
  var eventDescriptionClass = O$.combineClassNames(["o_timetableEventDescription", uiEvent.descriptionStyle]);
  var eventResourceClass = O$.combineClassNames(["o_timetableEventResource", uiEvent.resourceStyle]);
  var eventTimeClass = O$.combineClassNames(["o_timetableEventTime", uiEvent.timeStyle]);
  var eventContentClasses = {
    name : eventNameClass,
    description : eventDescriptionClass,
    resource : eventResourceClass,
    time: eventTimeClass
  };

  var eventBackgroundStyleClassName = "o_timetableEventBackground";
  var defaultEventColor = stylingParams.defaultEventColor ? stylingParams.defaultEventColor : "#006ebb";
  var escapeEventNames = uiEvent.escapeName !== undefined ? uiEvent.escapeName : true;
  monthTable._escapeEventNames = escapeEventNames;
  var escapeEventDescriptions = uiEvent.escapeDescription !== undefined ? uiEvent.escapeDescription : true;
  monthTable._escapeEventDescriptions = escapeEventDescriptions;
  var escapeEventResources = uiEvent.escapeResource !== undefined ? uiEvent.escapeResource : true;
  monthTable._escapeEventResources = escapeEventResources;
  var eventContent = uiEvent.content ? uiEvent.content : O$.MonthTable.DEFAULT_EVENT_CONTENT;

  var eventBackgroundIntensity = uiEvent.backgroundIntensity !== undefined ? uiEvent.backgroundIntensity : 0.25;
  var eventBackgroundTransparency = uiEvent.backgroundTransparency !== undefined ? uiEvent.backgroundTransparency : 0.2;

  var weekdayHeadersRowSeparator = stylingParams.weekdayHeadersRowSeparator ? stylingParams.weekdayHeadersRowSeparator : "1px solid gray";
  var weekdayColumnSeparator = stylingParams.weekdayColumnSeparator ? stylingParams.weekdayColumnSeparator : "1px solid silver";
  var rowSeparator = stylingParams.rowSeparator ? stylingParams.rowSeparator : "1px solid #b0b0b0";
  var columnSeparator = stylingParams.columnSeparator ? stylingParams.columnSeparator : "1px solid #b0b0b0";
  var dayHeaderRowClass = O$.combineClassNames(["o_monthTableDayHeaderRow", stylingParams.dayHeaderRowClass]);
  var timetableViewRowClass = O$.combineClassNames(["o_monthTableRow", stylingParams.rowClass]);

  var weekdayHeaderCellClass = O$.combineClassNames(["o_monthTableWeekdayHeaderCell", stylingParams.weekdayHeaderCellClass]);
  var weekendWeekdayHeaderCellClass = O$.combineClassNames([weekdayHeaderCellClass, "o_monthTableWeekendWeekdayHeaderCell", stylingParams.weekendWeekdayHeaderCellClass]);
  var cellHeaderClass = O$.combineClassNames(["o_monthTableCellHeader", stylingParams.cellHeaderClass]);
  var cellClass = O$.combineClassNames(["o_monthTableCell", stylingParams.cellClass]);
  var todayCellHeaderClass = O$.combineClassNames([/*cellHeaderClass,*/ "o_monthTableTodayCellHeader", stylingParams.todayCellHeaderClass]);
  var todayCellClass = O$.combineClassNames([/*cellClass,*/ "o_monthTableTodayCell", stylingParams.todayCellClass]);
  var weekendCellHeaderClass = O$.combineClassNames([/*cellHeaderClass,*/ "o_monthTableWeekendCellHeader", stylingParams.weekendCellHeaderClass]);
  var weekendCellClass = O$.combineClassNames([/*cellClass,*/ "o_monthTableWeekendCell", stylingParams.weekendCellClass]);
  var inactiveMonthCellHeaderClass = O$.combineClassNames([/*cellHeaderClass,*/ "o_monthTableInactiveMonthCellHeader", stylingParams.inactiveMonthCellHeaderClass]);
  var inactiveMonthCellClass = O$.combineClassNames([/*cellClass,*/ "o_monthTableInactiveMonthCell", stylingParams.inactiveMonthCellClass]);
  monthTable._weekdayHeaderCellClass = weekdayHeaderCellClass;
  monthTable._weekendWeekdayHeaderCellClass = weekendWeekdayHeaderCellClass;
  monthTable._cellHeaderClass = cellHeaderClass;
  monthTable._cellClass = cellClass;
  monthTable._todayCellHeaderClass = todayCellHeaderClass;
  monthTable._todayCellClass = todayCellClass;
  monthTable._weekendCellHeaderClass = weekendCellHeaderClass;
  monthTable._weekendCellClass = weekendCellClass;
  monthTable._inactiveMonthCellHeaderClass = inactiveMonthCellHeaderClass;
  monthTable._inactiveMonthCellClass = inactiveMonthCellClass;

  var eventElementHeight = O$.calculateNumericCSSValue(O$.getStyleClassProperty(eventStyleClass, "height"));
  var eventsLeftOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(eventStyleClass, "marginLeft"));
  var eventsRightOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(eventStyleClass, "marginRight"));
  var reservedEventsLeftOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(reservedTimeEventClass, "marginLeft"));
  var reservedEventsRightOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(reservedTimeEventClass, "marginRight"));
  var moreLinkElementHeight = O$.calculateNumericCSSValue(O$.getStyleClassProperty(moreLinkElementClass, "height"));

  var table = O$(componentId + "::table");
  monthTable._table = table;

  var weekdayHeadersTable = O$(componentId + "::weekdayHeaders");
  monthTable._scroller = O$(monthTable.id + "::scroller");

  var hiddenArea = O$(componentId + "::hiddenArea");
  monthTable._hiddenArea = hiddenArea;
  var useResourceSeparation = resources.length > 0;

  var firstDayOfWeek = (calendarOptions && calendarOptions.firstDayOfWeek) ? calendarOptions.firstDayOfWeek : 0;

  var columns = [];
  var weekdayHeaderColumns = [];

  for (var weekDay = 0; weekDay < 7; weekDay++) {
    columns.push({});
    weekdayHeaderColumns.push({});
  }
  weekdayHeaderColumns.push({className: "o_defaultScrollBarWidth"});

  if (useResourceSeparation) {
    monthTable._resourcesByIds = {};
    monthTable._idsByResourceNames = {};
    for (var resourceIndex = 0; resourceIndex < resources.length; resourceIndex++) {
      var resource = resources[resourceIndex];
      monthTable._resourcesByIds[resource.id] = resource;
      monthTable._idsByResourceNames[resource.name] = resource.id;
    }
  }

  monthTable._getResourceForEvent = function(event) {
    if (!event.resourceId || !useResourceSeparation)
      return null;
    var resource = monthTable._resourcesByIds[event.resourceId];
    return resource;
  };

  var forceUsingCellStyles = true; // allow such styles as text-align to be applied to row's cells

  O$.Tables._init(table, {
    columns: columns,
    gridLines: [rowSeparator, columnSeparator, null, null, null, null, null, null, null, null, null],
    forceUsingCellStyles: forceUsingCellStyles
  });

  O$.Tables._init(weekdayHeadersTable, {
    columns: weekdayHeaderColumns,
    gridLines: [rowSeparator, weekdayColumnSeparator, null, null, null, null, null, null, null, null, null],
    body: {rowClassName: weekdayHeadersRowClass},
    forceUsingCellStyles: forceUsingCellStyles
  });
  weekdayHeadersTable.style.borderBottom = weekdayHeadersRowSeparator;
  weekdayHeadersTable.body._overrideVerticalGridline(weekdayHeaderColumns.length - 2, O$.isExplorer6() ? "1px solid white" : "1px solid transparent");

  for (var week = 0; week < 6; week++) {
    table.body._newRow().className = dayHeaderRowClass; // day header row
    table.body._newRow().className = timetableViewRowClass; // day content row
  }

  var rows = table.body._getRows();

  for (var rowIndex = 0; rowIndex < rows.length; rowIndex++) {
    var row = rows[rowIndex];
    var cells = row._cells;
    for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
      var cell = cells[cellIndex];
      cell.onclick = function() {
        // onclick event can be fired on drag end under IE
        if (editable)
          var newEventTime = this._cellDay;
          monthTable._addEvent(newEventTime, this._resource ? this._resource.id : null);
      };
    }
    cells[cells.length - 1]._last = true;
  }

  // workaround to show *horizontal* gridlines
  table.body._overrideVerticalGridline(0, columnSeparator);

  monthTable._getEventEditor = function() {
    if (!editable)
      return null;
    return monthTable._eventEditor;
  };

  var ieEventHandlerLayer = O$.isExplorer() ? O$.createAbsolutePositionedElement(monthTable._scroller) : null;
  var absoluteElementsParentNode = O$.createAbsolutePositionedElement(monthTable._scroller);
  monthTable._absoluteElementsParentNode = absoluteElementsParentNode;
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

  for (var rowIndex = 1; rowIndex < rows.length; rowIndex+=2) {
    var row = rows[rowIndex];
    var cells = row._cells;
    for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
      var cell = cells[cellIndex];
      addMoreLink(cell);
    }
  }

  function clearAllCellEvents() {
    var rows = monthTable._table.body._getRows();
    for (var rowIndex = 1; rowIndex < rows.length; rowIndex+=2) {
      var row = rows[rowIndex];
      var cells = row._cells;
      for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
        var cell = cells[cellIndex];
        if (cell._cellEvents) {
          for (var i = 0; i < cell._cellEvents.length; i++) {
            var oldCellEvent = cell._cellEvents[i];
            removeEventElement(oldCellEvent);
          }
        }
        cell._cellEvents = [];
      }
    }
  }

  function updateCellEventElements(day) {
    var result = [];

    var cell = O$.MonthTable.getCellForDay(monthTable, day);

    if (!cell) {
      return result;      
    }

    if (cell._cellEvents) {
      for (var i = 0; i < cell._cellEvents.length; i++) {
        var oldCellEvent = cell._cellEvents[i];
        removeEventElement(oldCellEvent);
      }
    }

    cell._cellEvents = [];

    var cellEvents = O$.MonthTable.getDayEvents(monthTable._monthEvents, day);

    for (var cellEventIndex = 0; cellEventIndex < cellEvents.length; cellEventIndex++) {
      var cellEvent = cellEvents[cellEventIndex];
      cellEvent._cell = cell;
      cellEvent._cellEventIndex = cellEventIndex;
      cell._cellEvents.push(cellEvent);
    }

    cell._moreLinkData = null;

    for (var cellEventIndex = 0; cellEventIndex < cellEvents.length; cellEventIndex++) {
      var cellEvent = cellEvents[cellEventIndex];
      var newEventElement = addEventElement(cellEvent);
      newEventElement._attachAreas();
      result.push(newEventElement);
    }
    cell._moreLinkElement._update();
    cell._moreLinkData = null;

    return result;
  }

  function addMoreLink(cell) {
    var moreLinkElement = document.createElement("div");
    moreLinkElement.className = moreLinkElementClass;
    moreLinkElement.style.zIndex = monthTable._baseZIndex + 5;
    moreLinkElement._cell = cell;
    cell._moreLinkElement = moreLinkElement;
    var link = document.createElement("a");
    link.className = moreLinkClass;
    link.setAttribute("href", "javascript:");
    O$.setInnerText(link, moreLinkText);
    moreLinkElement.appendChild(link);

    link.onmousedown = O$.stopEvent;
    link.onclick = O$.stopEvent;

    moreLinkElement._updatePos = function() {
      if (!cell._moreLinkData) {
        this.style.display = "none";
        return;
      }

      this.style.display = "";
      var cellBoundaries = O$.getElementBorderRectangle(cell, true);

      var topY = cell._moreLinkData.topY;
      var bottomY = topY + moreLinkElementHeight;

      var x1 = cellBoundaries.getMinX();
      var x2 = cellBoundaries.getMaxX();
      if (O$.isExplorer() && O$.isStrictMode() && cell._last) {
        var scroller = O$(monthTable.id + "::scroller");
        var scrollerWidth = scroller.offsetWidth - scroller.clientWidth;
        x2 -= scrollerWidth;
      }

      var rect = new O$.Rectangle(Math.round(x1), Math.round(topY),
              Math.round(x2 - x1), Math.round(bottomY - topY));
      this._rect = rect;

      O$.setElementBorderRectangle(moreLinkElement, moreLinkElement._rect);
    };

    moreLinkElement._update = function() {
      this._updatePos();
    }

    absoluteElementsParentNode.appendChild(moreLinkElement);
    return moreLinkElement;
  }

  function addEventElement(event) {
    var eventElement = document.createElement("div");
    eventElement._event = event;
    event.mainElement = eventElement;
    eventElement._monthTable = monthTable;

    O$.Timetable._createEventContentElements(eventElement, eventContent, eventContentClasses);

    eventElement._attachAreas = function() {
      eventElement._areas = [];
      for (var areaIndex = 0, areaCount = eventAreaSettings.length; areaIndex < areaCount; areaIndex++) {
        var areaSettings = eventAreaSettings[areaIndex];
        var areaId = areaSettings.id;
        var areaClientId = monthTable.id + "::" + event.id + ":" + areaId;
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
    }
    eventElement._attachAreas();

    eventElement._updateAreaPositions = function(forceInsideAreasUpdate) {
      for (var areaIndex = 0, areaCount = this._areas.length; areaIndex < areaCount; areaIndex++) {
        var area = this._areas[areaIndex];
        if (!area._insideElement || forceInsideAreasUpdate)
          area._updatePos();
      }
    }

    eventElement._updateAreaZIndexes = function(eventZIndex) {
      if (eventZIndex === undefined)
        eventZIndex = O$.getNumericElementStyle(event.mainElement, "z-index");

      for (var areaIndex = 0, areaCount = this._areas.length; areaIndex < areaCount; areaIndex++) {
        var area = this._areas[areaIndex];
        area.style.zIndex = eventZIndex + 1;
      }
    }

    O$.assignEvents(eventElement, uiEvent, true, {timetableEvent: event});
    eventElement._onmouseover = eventElement.onmouseover;
    eventElement._onmouseout = eventElement.onmouseout;
    eventElement.onmouseover = eventElement.onmouseout = null;

    var eventClass = event.type != "reserved" ? eventStyleClass : reservedTimeEventClass;
    if (O$.isExplorer())
      O$.combineClassNames([eventClass, "o_explicitly_transparent_background"]);
    eventElement.className = eventClass;
    eventElement.style.margin = "0"; // margin in CSS is for calculating eventsLeftOffset/eventsRightOffset only -- it should be reset to avoid unwanted effects
    eventElement.style.zIndex = monthTable._baseZIndex + 5;

    eventElement._backgroundElement = document.createElement("div");
    eventElement._backgroundElement.className = eventBackgroundStyleClassName;
    eventElement._backgroundElement.style.zIndex = monthTable._baseZIndex + 4;
    event.backgroundElement = eventElement._backgroundElement;

    event._updateRolloverState = function() {
      var eventElement = event.mainElement;
      if (!eventElement) {
        // this can be the case because _updateRolloverState is invoked by time-out, so if mouseOver/mouseOut happens
        // just before element is replaced with Ajax, this call will be made when there's no original element anymore
        return;
      }
      var actionBar = getEventActionBar();
      event._setMouseInside(eventElement._mouseInside
              || (actionBar._event == event && actionBar._actionsArea._mouseInside)
              );
    };

    eventElement._update = function() {
      this._updatePos();
      if (event.type != "reserved") {
        O$.Timetable._updateEventContentElements(eventElement, event, monthTable);
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

    eventElement._updatePos = function() {
      if (event.resourceId) {
        var resource = monthTable._getResourceForEvent(event);
        if (!resource) {
          this.style.display = "none";
          return;
        }
      }
      this.style.display = "";

      var cell = event._cell;
      var cellEventIndex = event._cellEventIndex;
      var cellBoundaries = O$.getElementBorderRectangle(cell, true);

      var topY = cellBoundaries.getMinY() + eventElementHeight * cellEventIndex;
      var bottomY = topY + eventElementHeight;

      var lastForCell = (cellEventIndex == cell._cellEvents.length - 1);
      var maxY = lastForCell ? cellBoundaries.getMaxY() : cellBoundaries.getMaxY() - moreLinkElementHeight;

      if (cell._moreLinkData) {
        this.style.display = "none";
        return;
      }

      if (bottomY > maxY) {
        cell._moreLinkData = { topY: topY };
        this.style.display = "none";
        return;
      }
      
      var x1 = cellBoundaries.getMinX() + (event.type != "reserved" ? eventsLeftOffset : reservedEventsLeftOffset);
      var x2 = cellBoundaries.getMaxX() - (event.type != "reserved" ? eventsRightOffset : reservedEventsRightOffset);
      if (O$.isExplorer() && O$.isStrictMode() && cell._last) {
        var scroller = O$(monthTable.id + "::scroller");
        var scrollerWidth = scroller.offsetWidth - scroller.clientWidth;
        x2 -= scrollerWidth;
      }
      var rect = new O$.Rectangle(Math.round(x1), Math.round(topY),
              Math.round(x2 - x1), Math.round(bottomY - topY));
      this._rect = rect;
      var backgroundElement = this._backgroundElement;

      O$.setElementBorderRectangle(eventElement, eventElement._rect);
      O$.setElementBorderRectangle(backgroundElement, eventElement._rect);
    };

    var eventPreview = getEventPreview();
    event._setMouseInside = function(value) {
      if (event._mouseInside == value)
        return;
      event._mouseInside = value;
      if (event.type == "reserved")
        return;
      if (value) {
        O$.setStyleMappings(eventElement, {_rolloverStyle: rolloverEventClass});
        eventElement._updateAreaPositions(true);
        showEventActionBar(event);

        if (eventPreview) {
          setTimeout(function() {
            if (event._mouseInside)
              eventPreview.showForEvent(event);
          }, eventPreview._showingDelay);
        }
        if (eventElement._onmouseover) {
          eventElement._onmouseover(O$.createEvent("mouseover"));
        }
      } else {
        O$.setStyleMappings(eventElement, {_rolloverStyle: null});
        eventElement._updateAreaPositions(true);
        hideEventActionBar();

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
      O$.invokeFunctionAfterDelay(event._updateRolloverState, O$.MonthTable.EVENT_ROLLOVER_STATE_UPDATE_TIMEOUT);
    });

    if (editable) {
      eventElement.onclick = function(e) {
        O$.breakEvent(e);
        if (event.type == "reserved")
          return;

        event._oldStart = event.start;
        monthTable._getEventEditor().run(event, "update");
      };
    }

    if (eventElement.oncreate)
      eventElement.oncreate(O$.createEvent("create"));
    absoluteElementsParentNode.appendChild(eventElement);
    absoluteElementsParentNode.appendChild(eventElement._backgroundElement);

    eventElement._update();
    return eventElement;
  }

  function removeEventElement(event) {
    if (!event.mainElement)
      return;
    if (getEventActionBar()._event == event)
      hideEventActionBar();

    var areas = event.mainElement._areas;
    for (var i = 0, count = areas.length; i < count; i++) {
      var area = areas[i];
      hiddenArea.appendChild(area);
    }

    if (event.mainElement._removeNodes)
      event.mainElement._removeNodes();
    event.mainElement.parentNode.removeChild(event.mainElement);
    var backgroundElement = event.mainElement._backgroundElement;
    backgroundElement.parentNode.removeChild(backgroundElement);
    event.mainElement._event = null;
    event.mainElement = null;
    event.backgroundElement = null;
  }

  function getEventActionBar() {
    if (!monthTable._eventActionBar) {
      monthTable._eventActionBar = O$(monthTable.id + ":_eventActionBar");
      if (!monthTable._eventActionBar)
        return null;
      monthTable._eventActionBar.style.position = "absolute";
      monthTable._eventActionBar.style.visibility = "hidden";

    }
    return monthTable._eventActionBar;
  }

  function getEventPreview() {
    if (!monthTable._eventPreview) {
      monthTable._eventPreview = O$(monthTable.id + ":_eventPreview");
    }
    return monthTable._eventPreview;
  }

  function showEventActionBar(event) {
    var eventElement = event.mainElement;
    var actionBar = getEventActionBar();
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
    var barWidth = actionBar._actionsArea._getWidth();
    O$.setElementSize(actionBar, {width: barWidth, height: barHeight});
    actionBar.style.right = "0px";
    actionBar.style.top = (eventElement._rect.height - barHeight) / 2 + "px";
    actionBar.style.visibility = "visible";
    actionBar._actionsArea.style.visibility = "visible";
    actionBar._update();
  }

  function hideEventActionBar() {
    var actionBar = getEventActionBar();
    if (!actionBar._event)
      return;
    var eventElement = actionBar._event.mainElement;
    actionBar._lastEditedEvent = actionBar._event;
    actionBar._event = null;
    eventElement.removeChild(actionBar);
    actionBar.style.visibility = "hidden";
    actionBar._actionsArea.style.visibility = "hidden";
  }

  monthTable.cancelEventCreation = function(event) {
    event._creationInProgress = undefined;
    removeEventElement(event);
  };

  monthTable.addEvent = function(event) {
    event.setStart(event.start, event.startStr);
    event.setEnd(event.end, event.endStr);
    if (event._creationInProgress) {
      event._creationInProgress = undefined;
      removeEventElement(event);
    }

    eventProvider.addEvent(event);
    monthTable._updateEventElements(true);
    putTimetableChanges([event], null, null);
  };

  monthTable.deleteEvent = function(event) {
    eventProvider.deleteEvent(event);
    monthTable._updateEventElements(true);
    putTimetableChanges(null, null, [event.id]);
  };

  monthTable.getEventById = function(eventId) {
    return eventProvider.getEventById(eventId);
  };

  monthTable.updateEvent = function(event) {
    event.setStart(event.start, event.startStr);
    event.setEnd(event.end, event.endStr);

    if (event._oldStart) {
      updateCellEventElements(event._oldStart);
    }
    updateCellEventElements(event.start);

    event.updatePresentation();
    putTimetableChanges(null, [event], null);
  };

  monthTable.refreshEvents = function(serverAction) {
    this._saveChanges(true, serverAction, monthTable._startTime, monthTable._endTime);
  };

  monthTable.saveChanges = function() {
    this._saveChanges(false, null, monthTable._startTime, monthTable._endTime);
  };

  monthTable._saveChanges = function(reloadAllEvents, serverAction, reloadStartTime, reloadEndTime) {
    O$.requestComponentPortions(monthTable.id, ["saveEventChanges"], JSON.stringify(
    {reloadAllEvents: !!reloadAllEvents, startTime: O$.formatDateTime(reloadStartTime), endTime: O$.formatDateTime(reloadEndTime)},
            ["reloadAllEvents", "startTime", "endTime"]), function(
            component, portionName, portionHTML, portionScripts, portionData) {
      var remainingElements = O$.replaceDocumentElements(portionHTML, true);
      if (remainingElements.hasChildNodes())
        hiddenArea.appendChild(remainingElements);
      O$.executeScripts(portionScripts);

      if (portionData.reloadedEvents) {
        eventProvider.setEvents(portionData.reloadedEvents, reloadStartTime, reloadEndTime);
        monthTable._updateEventElements(true, true);
      } else {
        if (portionData.addedEvents) {
          var addedCount = portionData.addedEvents.length;
          O$.assertEquals(monthTable._addedEvents.length, addedCount, "addedEventCount should be same as monthTable._addedEvents.length");
          for (var addedIdx = 0; addedIdx < addedCount; addedIdx++) {
            var addedEvent = monthTable._addedEvents[addedIdx];
            addedEvent._copyFrom(portionData.addedEvents[addedIdx]);
            addedEvent.updatePresentation(0 /*dragAndDropCancelingPeriod*/);
            addedEvent.mainElement._attachAreas();
            addedEvent.mainElement._updateAreaPositions();
          }
        }
        if (portionData.editedEvents) {
          var editedCount = portionData.editedEvents.length;
          O$.assertEquals(monthTable._editedEvents.length, editedCount, "editedEventCount should be same as monthTable._editedEvents.length");
          for (var editedIdx = 0; editedIdx < editedCount; editedIdx++) {
            var editedEvent = monthTable._editedEvents[editedIdx];
            editedEvent._copyFrom(portionData.editedEvents[editedIdx]);
            editedEvent.mainElement._attachAreas();
            editedEvent.updatePresentation(0 /*dragAndDropCancelingPeriod*/);
          }
        }
      }

      monthTable._addedEvents = [];
      monthTable._editedEvents = [];
      monthTable._removedEventIds = [];
      updateTimetableChangesField();
    }, function () {
      alert('Error saving the last timetable change');
    }, serverAction);
  };
  

  monthTable.getDay = function() {
    return monthTable._day;
  };

  monthTable.setDay = function(day) {
    if (O$._datesEqual(monthTable._day, day))
      return;
    if (monthTable._onDayChange) {
      monthTable._onDayChange(day);
    }

    var dtf = O$.getDateTimeFormatObject(locale);
    O$.setHiddenField(monthTable, monthTable.id + "::day", dtf.format(day, "dd/MM/yyyy"));

    monthTable._day = day;
    var startTime = O$.MonthTable.getFirstDay(day, firstDayOfWeek);
    monthTable._startTime = startTime;
    var endTime = O$.MonthTable.getFirstDayOut(day, firstDayOfWeek);
    monthTable._endTime = endTime;

    O$.MonthTable.updateCellDays(monthTable);

    monthTable._monthEvents = eventProvider._getEventsForPeriod(monthTable._startTime, monthTable._endTime, function() {
      monthTable._updateEventElements(true, true);
    });

    monthTable._updateEventElements();

    var weekdayHeaderRow = weekdayHeadersTable.body._getRows()[0];

    var weekdayHeaderCellDay = monthTable._startTime;
    for (var weekdayHeaderCellIndex = 0; weekdayHeaderCellIndex < weekdayHeaderRow._cells.length - 1; weekdayHeaderCellIndex++) {
      var weekdayHeaderCell = weekdayHeaderRow._cells[weekdayHeaderCellIndex];
      var weekdayHeaderCellClassName = monthTable._weekdayHeaderCellClass;
      if (weekdayHeaderCellDay.getDay() == 0 || weekdayHeaderCellDay.getDay() == 6) {
        weekdayHeaderCellClassName = monthTable._weekendWeekdayHeaderCellClass;       
      }
      weekdayHeaderCell.className = weekdayHeaderCellClassName;
      O$.removeAllChildNodes(weekdayHeaderCell);
      var weekdayHeaderCellText = dtf.format(weekdayHeaderCellDay, weekdayPattern);
      var weekdayHeaderCellStyledText = O$.createStyledText(weekdayHeaderCellText, weekdayStyle);
      weekdayHeaderCell.appendChild(weekdayHeaderCellStyledText);
      weekdayHeaderCellDay = O$.incDay(weekdayHeaderCellDay);
    }

  };

  monthTable._updateEventElements = function(reacquireDayEvents, refreshAreasAfterReload) {
    monthTable._baseZIndex = O$.getElementZIndex(monthTable);
    
    monthTable._eventElements = [];
    if (reacquireDayEvents)
      monthTable._monthEvents = eventProvider._getEventsForPeriod(monthTable._startTime, monthTable._endTime, function() {
        monthTable._updateEventElements(true, true);
      });

    clearAllCellEvents();
    
    for (var cellDate = monthTable._startTime; cellDate < monthTable._endTime; cellDate = O$.MonthTable.__incDay(cellDate)) {
      var newEventElements = updateCellEventElements(cellDate);
      for (var i = 0; i < newEventElements.length; i++) {
        monthTable._eventElements.push(newEventElements[i]);
      }
    }

    monthTable._updateEventZIndexes();
  };

  monthTable._baseZIndex = O$.getElementZIndex(monthTable);
  monthTable._maxZIndex = monthTable._baseZIndex + 10;
  monthTable._updateEventZIndexes = function() {
    if (!monthTable._eventElements)
      return;
    for (var elementIndex = 0, elementCount = monthTable._eventElements.length; elementIndex < elementCount; elementIndex++) {
      var eventElement = monthTable._eventElements[elementIndex];
      var eventZIndex = monthTable._baseZIndex + (elementIndex + 1) * 5;
      eventElement.style.zIndex = eventZIndex;
      eventElement._backgroundElement.style.zIndex = eventZIndex - 1;
      if (eventElement._updateZIndex)
        eventElement._updateZIndex(eventZIndex);
    }
    monthTable._maxZIndex = eventZIndex + 10;
  };

  monthTable.previousMonth = function() {
    var prevDay = new Date(monthTable._day.getFullYear(), monthTable._day.getMonth() - 1, monthTable._day.getDate());
    monthTable.setDay(prevDay);
  };

  monthTable.nextMonth = function() {
    var nextDay = new Date(monthTable._day.getFullYear(), monthTable._day.getMonth() + 1, monthTable._day.getDate());
    if ((nextDay.getMonth() - monthTable._day.getMonth()) % 12 != 1) {
      nextDay = new Date(monthTable._day.getFullYear(), monthTable._day.getMonth() + 2, 0);
    }
    monthTable.setDay(nextDay);
  };

  monthTable.currentMonth = function() {
    var today = new Date();
    monthTable.setDay(today);
  };

  var dtf = O$.getDateTimeFormatObject(locale);
  monthTable.setDay(dtf.parse(day, "dd/MM/yyyy"));

  monthTable._addEvent = function(startTime, resourceId) {
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
    monthTable._getEventEditor().run(event, "create");
  };

  monthTable.updateLayout = function() {
    monthTable._cachedPositions = {};
    monthTable._declaredMonthTableHeight = O$.getStyleClassProperty(monthTable.className, "height");
    if (monthTable._declaredMonthTableHeight === undefined) {
      monthTable._scroller.style.overflow = "visible";
      monthTable._scroller.style.overflowX = "visible";
      monthTable._scroller.style.overflowY = "hidden";
      monthTable._scroller.style.height = "auto";
    } else {
      if (O$.isOpera()) {
        monthTable._scroller.style.overflow = "scroll"; // Opera < 9.5 doesn't understand specifying just overflow-y
      }
    }

    updateHeightForFF();
    accountForScrollerWidth();
    absoluteElementsParentNode._updatePos();

    var rows = monthTable._table.body._getRows();
    for (var rowIndex = 1; rowIndex < rows.length; rowIndex+=2) {
      var row = rows[rowIndex];
      var cells = row._cells;
      for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
        var cell = cells[cellIndex];
        cell._moreLinkData = null;
        for (var eventIndex = 0, eventCount = cell._cellEvents.length; eventIndex < eventCount; eventIndex++) {
          var event = cell._cellEvents[eventIndex];
          event.updatePresentation();
        }
        if (cell._moreLinkElement) {
          cell._moreLinkElement._update();
        }
        cell._moreLinkData = null;
      }
    }

 };

  O$.addEventHandler(window, "resize", function() {
    monthTable.updateLayout();
  });
  O$.addInternalLoadEvent(function() {
    monthTable.updateLayout(); // update positions after layout changes that might have had place during loading

    var maxScrollOffset = monthTable._scroller.scrollHeight - O$.getElementSize(monthTable._scroller).height;
    if (maxScrollOffset < 0)
      maxScrollOffset = 0;
    if (scrollOffset > maxScrollOffset)
      scrollOffset = maxScrollOffset;
    monthTable._scroller.scrollTop = scrollOffset;
    O$.addEventHandler(monthTable._scroller, "scroll", function() {
      O$.setHiddenField(monthTable, monthTable.id + "::scrollPos", monthTable._scroller.scrollTop);
    });
  });

  function updateHeightForFF() {
    // FireFox's scroller height includes the entire table without truncating it according to
    // user-specified height in day table, so we should truncate height ourselves
    if (!O$.isMozillaFF())
      return;

    if (monthTable._declaredMonthTableHeight === undefined)
      return;
    var scrollerHeight = monthTable._declaredMonthTableHeight;
    if (!O$.stringEndsWith(monthTable._declaredMonthTableHeight, "%")) {
      var height = O$.calculateNumericCSSValue(monthTable._declaredMonthTableHeight);
      if (height) {
        var offset = O$.getElementPos(monthTable._scroller).y - O$.getElementPos(monthTable).y;
        height -= offset;
        scrollerHeight = height + "px";
      }
    }
    monthTable._scroller.style.height = scrollerHeight;
  }

  function accountForScrollerWidth() {
    var firstDataRow = table.body._getRows()[0];
    var visibleDataWidth = O$.getElementBorderRectangle(firstDataRow._cells[firstDataRow._cells.length - 1]).getMaxX() - O$.getElementPos(firstDataRow._cells[0]).x;

    var weekdayHeaderRow = weekdayHeadersTable.body._getRows()[0];
    var totalWeekdayHeaderWidth = O$.getElementBorderRectangle(weekdayHeaderRow._cells[weekdayHeaderRow._cells.length - 1]).getMaxX() - O$.getElementPos(weekdayHeaderRow._cells[0]).x;
    var lastHeaderColWidth = totalWeekdayHeaderWidth - visibleDataWidth;
    if (lastHeaderColWidth < 0) // can be the case under Mozilla3 because O$.getElementBorderRectangle returns non-rounded coordinates, which may result in values like -0.2499...
      lastHeaderColWidth = 0;
    if (lastHeaderColWidth == 0 && O$.isOpera())
      lastHeaderColWidth = 1;
    weekdayHeaderRow._cells[weekdayHeaderRow._cells.length - 1].style.width = lastHeaderColWidth + "px";
  }

  updateHeightForFF();

  function putTimetableChanges(addedEvents, changedEvents, removedEventIds, initialAssignment) {
    if (!monthTable._addedEvents) monthTable._addedEvents = [];
    if (!monthTable._editedEvents) monthTable._editedEvents = [];
    if (!monthTable._removedEventIds) monthTable._removedEventIds = [];
    if (addedEvents)
      monthTable._addedEvents = monthTable._addedEvents.concat(addedEvents);
    if (changedEvents) {
      for (var editedIdx = 0, editedCount = changedEvents.length; editedIdx < editedCount; editedIdx++) {
        var editedEvent = changedEvents[editedIdx];
        if (O$.findValueInArray(editedEvent, monthTable._addedEvents) != -1)
          continue;
        if (O$.findValueInArray(editedEvent, monthTable._editedEvents) == -1) {
          monthTable._editedEvents.push(editedEvent);
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
        var addedEventIndex = eventIndexById(monthTable._addedEvents, removedEventId);
        if (addedEventIndex != -1) {
          monthTable._addedEvents.splice(addedEventIndex, 1);
          continue;
        }
        var editedEventIndex = eventIndexById(monthTable._editedEvents, removedEventId);
        if (editedEventIndex != -1)
          monthTable._editedEvents.splice(editedEventIndex, 1);
        monthTable._removedEventIds.push(removedEventId);
      }
    }

    updateTimetableChangesField();

    if (monthTable.onchange) {
      var e = O$.createEvent("change");
      e.addedEvents = addedEvents ? addedEvents : {};
      e.changedEvents = changedEvents ? changedEvents : {};
      e.removedEventIds = removedEventIds ? removedEventIds : {};

      if (monthTable.onchange(e) === false)
        return;
    }

    if (editingOptions.autoSaveChanges && !initialAssignment)
      monthTable.saveChanges();
  }

  // RichFaces-OpenFaces-JSON compatibility workaround
  // Since some of RichFaces components use JavaScript defining Array.prototype.toJSON method,
  // JSON.stringify([1, 2]) call will produce '"[1,2]"' string instead of expected '["1","2"]'.
  // Therefore perform manual array conversion here.
  function updateTimetableChangesField() {

    var properties = ["id", "name", "description", "resourceId", "startStr", "endStr", "color", "type"];

    function arrayToJSON(name, source, properties) {
      var result = [];
      for (var i = 0; i < source.length; i++)
        result[i] = JSON.stringify(source[i], properties);
      return JSON.stringify(name) + ":[" + result.join(",") + "]";
    }

    var events = [
      arrayToJSON("addedEvents", monthTable._addedEvents, properties),
      arrayToJSON("editedEvents", monthTable._editedEvents, properties),
      arrayToJSON("removedEventIds", monthTable._removedEventIds, properties)
    ];
    var changesAsString = "{" + events.join(",") + "}";
    O$.setHiddenField(monthTable, monthTable.id + "::timetableChanges", changesAsString);
  }

  putTimetableChanges(null, null, null, true);
  O$.assignEvents(monthTable, {onchange: onchange}, true);

};

O$.MonthTable._findEventById = function(events, id) {
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
};

O$.MonthTable._LazyLoadedTimetableEvents = function(preloadedEvents, preloadedStartTime, preloadedEndTime) {
  O$.MonthTable._PreloadedTimetableEvents.call(this, []);

  this._setEvents = this.setEvents;
  this.setEvents = function(events, preloadedStartTime, preloadedEndTime) {
    this._setEvents(events);
    this._loadedTimeRangeMap = new O$._RangeMap();
    this._loadingTimeRangeMap = new O$._RangeMap();
    if (!(preloadedStartTime instanceof Date))
      preloadedStartTime = preloadedStartTime ? O$.parseDateTime(preloadedStartTime).getTime() : null;
    if (!(preloadedEndTime instanceof Date))
      preloadedEndTime = preloadedEndTime ? O$.parseDateTime(preloadedEndTime).getTime() : null;
    this._loadedTimeRangeMap.addRange(preloadedStartTime, preloadedEndTime);
    this._loadingTimeRangeMap.addRange(preloadedStartTime, preloadedEndTime);
  };
  this.setEvents(preloadedEvents, preloadedStartTime, preloadedEndTime);

  this._getEventsForPeriod_raw = this._getEventsForPeriod;
  this._getEventsForPeriod = function(start, end, eventsLoadedCallback) {
    if (this._loadedTimeRangeMap.isRangeFullyInMap(start.getTime(), end.getTime()) ||
        this._loadingTimeRangeMap.isRangeFullyInMap(start.getTime(), end.getTime()))
      return this._getEventsForPeriod_raw(start, end);

    this._loadingTimeRangeMap.addRange(start.getTime(), end.getTime());
    var thisProvider = this;
    O$.requestComponentPortions(this._monthTable.id, ["loadEvents"],
            JSON.stringify(
            {startTime: O$.formatDateTime(start), endTime: O$.formatDateTime(end)},
                    ["startTime", "endTime"]),
            function(component, portionName, portionHTML, portionScripts, portionData) {
              var remainingElements = O$.replaceDocumentElements(portionHTML, true);
              if (remainingElements.hasChildNodes())
                thisProvider._monthTable._hiddenArea.appendChild(remainingElements);
              O$.executeScripts(portionScripts);

              var newEvents = portionData.events;
              thisProvider._loadedTimeRangeMap.addRange(start.getTime(), end.getTime());
              thisProvider._events._cachedEventsByIds = null;
              for (var i = 0, count = newEvents.length; i < count; i++) {
                var newEvent = newEvents[i];
                var existingEvent = O$._findEventById(thisProvider._events, newEvent.id);
                if (existingEvent)
                  existingEvent._copyFrom(newEvent);
                else
                  thisProvider.addEvent(newEvent);
              }
              if (eventsLoadedCallback) {
                //        var eventsForPeriod = this._getEventsForPeriod_raw(start, end);
                eventsLoadedCallback();//eventsForPeriod);
              }
            }, function () {
      // todo: revert addition of time range to this._loadingTimeRangeMap
      alert('Error loading timetable events');
    });
    return this._getEventsForPeriod_raw(start, end);
  };
};

O$.MonthTable._PreloadedTimetableEvents = function(events) {

  this._getEventsForPeriod = function(start, end) {
    var result = [];
    var startTime = start.getTime();
    var endTime = end.getTime();
    for (var eventIndex = 0, eventCount = this._events.length; eventIndex < eventCount; eventIndex++) {
      var event = this._events[eventIndex];
      if (event.end.getTime() < event.start.getTime())
        continue;
      if (event.end.getTime() <= startTime ||
          event.start.getTime() >= endTime)
        continue;
      result.push(event);
    }
    return result;
  };

  this.setEvents = function(newEvents) {
    this._events = newEvents;
    for (var eventIndex = 0, eventCount = newEvents.length; eventIndex < eventCount; eventIndex++) {
      var event = newEvents[eventIndex];
      O$.Timetable._initEvent(event);
    }
    this._events._cachedEventsByIds = null;
  };

  this.getEventById = function(eventId) {
    for (var i = 0, count = this._events.length; i < count; i++) {
      var evt = this._events[i];
      if (evt.id == eventId)
        return evt;
    }
    return null;
  };

  this.addEvent = function(event) {
    if (this._events._cachedEventsByIds && event.id)
      this._events._cachedEventsByIds[event.id] = event;
    O$.Timetable._initEvent(event);
    this._events.push(event);
  };
  this.deleteEvent = function(event) {
    if (this._events._cachedEventsByIds && event.id)
      this._events._cachedEventsByIds[event.id] = undefined;

    var eventIndex = O$.findValueInArray(event, this._events);
    this._events.splice(eventIndex, 1);
  };

  this.setEvents(events);
};


O$.MonthTable.getFirstDay = function(day, firstDayOfWeek) {
  var firstDayOfMonth = O$.cloneDate(day);
  firstDayOfMonth.setDate(1);

  var dayOfWeek = firstDayOfMonth.getDay();
  var decrement = dayOfWeek - firstDayOfWeek;
  if (decrement < 0) {
    decrement += 7;
  }
  var result = O$.MonthTable.__incDay(firstDayOfMonth, -decrement);
  return result;
};

O$.MonthTable.getFirstDayOut = function(day, firstDayOfWeek) {
  var lastDayOfMonth = new Date(day.getFullYear(), day.getMonth() + 1, 0);
  var lastDayOfMonthWeekday = lastDayOfMonth.getDay();
  var increment = firstDayOfWeek + 7 - lastDayOfMonthWeekday;
  var result = new Date(lastDayOfMonth.getFullYear(), lastDayOfMonth.getMonth(), lastDayOfMonth.getDate() + increment);
  return result;
};

O$.MonthTable.getNextMonthStart = function(date) {
  return new Date(date.getFullYear(), date.getMonth() + 1, 1);
};

O$.MonthTable.compareEvents = function(firstEvent, secondEvent) {
  var result = firstEvent.start - secondEvent.start;
  if (result != 0)
    return result;
  if (firstEvent.name < secondEvent.name)
    return -1;
  if (firstEvent.name > secondEvent.name)
    return 1;
  return 0;
};

O$.MonthTable.getDayEvents = function(allEvents, day) {
  var result = [];
  for (var i = 0, count = allEvents.length; i < count; i++) {
    var currEvent = allEvents[i];
    if (currEvent.type != "reserved") {
      var start = currEvent.start;
      if (O$._datesEqual(start, day)) {
        result.push(currEvent);
      }
    }
  }
  result.sort(O$.MonthTable.compareEvents);
  return result;
};

O$.MonthTable.updateCellDays = function(monthTable) {
  var numberOfWeeks = Math.round((monthTable._endTime - monthTable._startTime) / (7 * 24 * 60 * 60 * 1000));
  var visibleRowCount = 2 * numberOfWeeks;
  O$.MonthTable.setVisibleRowCount(monthTable._table, visibleRowCount);

  var cellDay = monthTable._startTime;
  var rows = monthTable._table.body._getRows();
  for (var rowIndex = 0; rowIndex < visibleRowCount; rowIndex+=2) {
    var dayHeaderRow = rows[rowIndex];
    var dayContentRow = rows[rowIndex+1];
    var dayHeaderCells = dayHeaderRow._cells;
    var dayContentCells = dayContentRow._cells;
    var cellCount = dayHeaderCells.length;

    var today = new Date();

    for (var cellIndex = 0; cellIndex < cellCount; cellIndex++) {
      var dayHeaderCell = dayHeaderCells[cellIndex];
      var dayContentCell = dayContentCells[cellIndex];

      var cellHeaderClasses = [ monthTable._cellHeaderClass ];
      var cellClasses = [ monthTable._cellClass ];

      if (cellDay.getDay() == 0 || cellDay.getDay() == 6) {
        cellHeaderClasses.push(monthTable._weekendCellHeaderClass);
        cellClasses.push(monthTable._weekendCellClass);
      }

      if (cellDay.getMonth() != monthTable._day.getMonth()) {
        cellHeaderClasses.push(monthTable._inactiveMonthCellHeaderClass);
        cellClasses.push(monthTable._inactiveMonthCellClass);
      }

      if (O$._datesEqual(cellDay, today)) {
        cellHeaderClasses.push(monthTable._todayCellHeaderClass);
        cellClasses.push(monthTable._todayCellClass);
      }

      var cellHeaderClass = O$.combineClassNames(cellHeaderClasses);
      var cellClass = O$.combineClassNames(cellClasses);

      dayHeaderCell.className = cellHeaderClass;
      dayContentCell.className = cellClass;
      dayHeaderCell._cellDay = cellDay;
      dayContentCell._cellDay = cellDay;
      O$.removeAllChildNodes(dayHeaderCell);
      var text = document.createTextNode(cellDay.getDate());
      dayHeaderCell.appendChild(text);
      cellDay = O$.MonthTable.__incDay(cellDay);
    }
  }
};

O$.MonthTable.getCellForDay = function(monthTable, day) {
  var rows = monthTable._table.body._getRows();
  for (var rowIndex = 1; rowIndex < rows.length; rowIndex+=2) {
    var row = rows[rowIndex];
    var cells = row._cells;
    for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
      var cell = cells[cellIndex];
      var cellDay = cell._cellDay;
      if (O$._datesEqual(cellDay, day)) {
        return cell;
      }
    }
  }
  return null;
};

// increment the date without setting time to 12:00
O$.MonthTable.__incDay = function(date, increment) {
  var newDate = O$.cloneDateTime(date);
  if (increment === 0) {
    return newDate;
  }
  if (!increment) {
    increment = 1;
  }
  newDate.setDate(newDate.getDate() + increment);
  return newDate;
};


O$.MonthTable.setVisibleRowCount = function(table, count) {
  var rows = table.body._getRows();
  var rowCount = Math.min(count, rows.length);
  for (var visibleRowIndex = 0; visibleRowIndex < rowCount; visibleRowIndex++) {
    rows[visibleRowIndex].style.display="";
  }
  for (var hiddenRowIndex = rowCount; hiddenRowIndex < rows.length; hiddenRowIndex++) {
    rows[hiddenRowIndex].style.display="none";
  }
}
