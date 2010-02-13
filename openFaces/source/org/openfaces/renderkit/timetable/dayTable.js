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

//  dayTable.updateLayout();

// ========== implementation

O$.EVENT_ROLLOVER_STATE_UPDATE_TIMEOUT = 1;

O$._initDayTable = function(componentId,
                            day, locale, dateFormat, startTimeStr, endTimeStr, scrollTimeStr,
                            preloadedEventParams, resources, eventAreaSettings,
                            editable, onchange, editingOptions,
                            stylingParams,
                            uiEvent,
                            timePattern,
                            timeSuffixPattern,
                            majorTimeInterval,
                            minorTimeInterval,
                            showTimeForMinorIntervals
        ) {
  var dayTable = O$(componentId);
  if (O$.isExplorer()) {
    if (!dayTable._initScheduled) {
      dayTable._initScheduled = true;
      var initArgs = arguments;
      // postpone initialization to avoid IE failure during page loading
      O$.addInternalLoadEvent(function() {
        O$._initDayTable.apply(null, initArgs);
      });
      return;
    }
  }

  O$.initComponent(componentId, {rollover: stylingParams.rolloverClass});

  var eventProvider = new O$._LazyLoadedTimetableEvents(
          preloadedEventParams.events,
          preloadedEventParams.from,
          preloadedEventParams.to);
  eventProvider._dayTable = dayTable;
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

  var shortestEventTimeWhileResizing = 1000 * 60 * minorTimeInterval;

  if (!uiEvent)
    uiEvent = {};
  if (!startTimeStr)
    startTimeStr = "00:00";
  if (!endTimeStr || endTimeStr == "00:00")
    endTimeStr = "24:00";
  var startTime = O$.parseTime(startTimeStr);
  var endTime = O$.parseTime(endTimeStr);
  if (!scrollTimeStr)
    scrollTimeStr = startTimeStr;
  var scrollTime = O$.parseTime(scrollTimeStr);
  var startTimeInMinutes = startTime.getHours() * 60 + startTime.getMinutes();
  var endTimeInMinutes = (endTime.getHours() == 0 ? 24 : endTime.getHours()) * 60 + endTime.getMinutes();

  var eventResizeHandleHeight = O$.calculateNumericCSSValue("6px");

  var showTimeAgainstMark = stylingParams.timeTextPosition && stylingParams.timeTextPosition == "againstMark";

  var resourceHeadersRowClass = O$.combineClassNames(["o_resourceHeadersRow", stylingParams.resourceHeadersRowClass]);
  var dayTableRowClass = O$.combineClassNames(["o_dayTableRowClass", stylingParams.rowClass]);

  var timeColumnClass = O$.combineClassNames(["o_timeColumn", stylingParams.timeColumnClass]);
  var majorTimeStyle = O$.combineClassNames(["o_majorTimeText", stylingParams.majorTimeClass]);
  var minorTimeStyle = O$.combineClassNames(["o_minorTimeText", stylingParams.minorTimeClass]);
  var timeSuffixStyle = O$.combineClassNames(["o_timeSuffixText", stylingParams.timeSuffixClass]);

  var eventStyleClass = O$.combineClassNames(["o_timetableEvent", uiEvent.style]);
  var rolloverEventClass = O$.combineClassNames(["o_rolloverTimetableEvent", uiEvent.rolloverStyle]);
  var eventNameClass = O$.combineClassNames(["o_timetableEventName", uiEvent.nameStyle]);
  var eventDescriptionClass = O$.combineClassNames(["o_timetableEventDescription", uiEvent.descriptionStyle]);

  var eventBackgroundStyleClassName = "o_timetableEventBackground";
  var defaultEventColor = stylingParams.defaultEventColor ? stylingParams.defaultEventColor : "#006ebb";
  var escapeEventNames = uiEvent.escapeName !== undefined ? uiEvent.escapeName : true;
  dayTable._escapeEventNames = escapeEventNames;
  var escapeEventDescriptions = uiEvent.escapeDescription !== undefined ? uiEvent.escapeDescription : true;
  dayTable._escapeEventDescriptions = escapeEventDescriptions;

  var eventBackgroundIntensity = uiEvent.backgroundIntensity !== undefined ? uiEvent.backgroundIntensity : 0.25;
  var eventBackgroundTransparency = uiEvent.backgroundTransparency !== undefined ? uiEvent.backgroundTransparency : 0.2;
  var dragAndDropTransitionPeriod = stylingParams.dragAndDropTransitionPeriod !== undefined ? stylingParams.dragAndDropTransitionPeriod : 70;
  var dragAndDropCancelingPeriod = stylingParams.dragAndDropCancelingPeriod !== undefined ? stylingParams.dragAndDropCancelingPeriod : 200;
  var undroppableStateTransitionPeriod = stylingParams.undroppableStateTransitionPeriod !== undefined ? stylingParams.undroppableStateTransitionPeriod : 250;
  var undroppableEventTransparency = stylingParams.undroppableEventTransparency !== undefined ? stylingParams.undroppableEventTransparency : 0.5;

  var resourceHeadersRowSeparator = stylingParams.resourceHeadersRowSeparator ? stylingParams.resourceHeadersRowSeparator : "1px solid gray";
  var resourceColumnSeparator = stylingParams.resourceColumnSeparator ? stylingParams.resourceColumnSeparator : "1px dotted silver";
  var timeColumnSeparator = stylingParams.timeColumnSeparator ? stylingParams.timeColumnSeparator : "2px solid gray";
  var primaryRowSeparator = stylingParams.primaryRowSeparator ? stylingParams.primaryRowSeparator : "1px solid #b0b0b0";
  var secondaryRowSeparator = stylingParams.secondaryRowSeparator ? stylingParams.secondaryRowSeparator : "1px solid #e4e4e4";
  var timeColumnPrimaryRowSeparator = stylingParams.timeColumnPrimaryRowSeparator ? stylingParams.timeColumnPrimaryRowSeparator : "1px solid #b0b0b0";
  var timeColumnSecondaryRowSeparator = stylingParams.timeColumnSecondaryRowSeparator ? stylingParams.timeColumnSecondaryRowSeparator : "1px solid #e4e4e4";

  var eventsLeftOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(eventStyleClass, "marginLeft"));
  var eventsRightOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(eventStyleClass, "marginRight"));
  var reservedEventsLeftOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(reservedTimeEventClass, "marginLeft"));
  var reservedEventsRightOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(reservedTimeEventClass, "marginRight"));
  var table = O$(componentId + "::table");
  var resourceHeadersTable = O$(componentId + "::resourceHeaders");
  dayTable._scroller = O$(dayTable.id + "::scroller");

  var hiddenArea = O$(componentId + "::hiddenArea");
  dayTable._hiddenArea = hiddenArea;
  var useResourceSeparation = resources.length > 0;
  var columns = [
    {
      className: timeColumnClass
    }
  ];
  var headerColumns = [
    {
      className: timeColumnClass
    }
  ];
  if (useResourceSeparation) {
    dayTable._resourcesByIds = {};
    dayTable._idsByResourceNames = {};
    for (var resourceIndex = 0; resourceIndex < resources.length; resourceIndex++) {
      var resource = resources[resourceIndex];
      dayTable._resourcesByIds[resource.id] = resource;
      dayTable._idsByResourceNames[resource.name] = resource.id;
      resource._colIndex = resourceIndex + 1;
      columns.push({});
      headerColumns.push({});
    }
    headerColumns.push({className: "o_defaultScrollBarWidth"});
  } else {
    columns.push({});
  }
  dayTable._getResourceForEvent = function(event) {
    if (!event.resourceId || !useResourceSeparation)
      return null;
    var resource = dayTable._resourcesByIds[event.resourceId];
    return resource;
  };

  var rowHeight = O$.getStyleClassProperty(dayTableRowClass, "height");
  var duplicatedRows = showTimeAgainstMark;
  if (duplicatedRows && rowHeight) {
    var heightPx = O$.calculateNumericCSSValue(rowHeight);
    var additionalRowHeightClass = O$.createCssClass("height: " + Math.ceil(heightPx / 2) + "px ! important");
    dayTableRowClass = O$.combineClassNames([dayTableRowClass, additionalRowHeightClass]);
  }

  var forceUsingCellStyles = true; // allow such styles as text-align to be applied to row's cells

  O$.Tables._init(table, {
    columns: columns,
    gridLines: [primaryRowSeparator, resourceColumnSeparator, null, null, null, null, null, null, null, null, null],
    body: {rowClassName: dayTableRowClass},
    forceUsingCellStyles: forceUsingCellStyles
  });

  if (useResourceSeparation) {
    O$.Tables._init(resourceHeadersTable, {
      columns: headerColumns,
      gridLines: [primaryRowSeparator, resourceColumnSeparator, null, null, null, null, null, null, null, null, null],
      rowStyles: {bodyRowClass: resourceHeadersRowClass},
      forceUsingCellStyles: forceUsingCellStyles
    });
    resourceHeadersTable.style.borderBottom = resourceHeadersRowSeparator;
  }

  if (minorTimeInterval >= majorTimeInterval) {
    // Minor time interval should be less than a major one. Swap them as a fallback.
    var maxInterval = minorTimeInterval;
    minorTimeInterval = majorTimeInterval;
    majorTimeInterval = maxInterval;
  }

  var minorPerMajorIntervals = Math.round(majorTimeInterval / minorTimeInterval);
  if (Math.abs(majorTimeInterval / minorTimeInterval - minorPerMajorIntervals) > 0) {
    // Major time interval is not a multiple of a minor one. Make it a nearest divisible of the major one as a fallback.
    minorTimeInterval = majorTimeInterval / minorPerMajorIntervals;
  }

  var intervalCount = (endTimeInMinutes - startTimeInMinutes) / minorTimeInterval;
  var roundedIntervalCount = Math.round(intervalCount);
  if (Math.abs(intervalCount - roundedIntervalCount) > 0) {
    // The entire time span from start time to end time should be divisible by a minor time interval.
    // The fallback here is to move end time to the nearest minor interval boundary before the specified end time.
    endTimeInMinutes = roundedIntervalCount * minorTimeInterval;
  }
  intervalCount = roundedIntervalCount;

  var now = new Date();
  var minutesPerRow = showTimeAgainstMark ? minorTimeInterval / 2 : minorTimeInterval;
  for (var timeInMinutes = startTimeInMinutes, intervalIndex = 0;
       timeInMinutes < endTimeInMinutes;
       timeInMinutes += minorTimeInterval,intervalIndex++) {
    var isMajorMark = (!showTimeAgainstMark ? intervalIndex : intervalIndex + 1) % minorPerMajorIntervals == 0;
    var row = table.body._createRow();
    var row2 = showTimeAgainstMark ? table.body._createRow() : null;
    if (row2) row2._row = row;
    row._intervalStartMinutes = timeInMinutes;
    if (row2) row2._intervalStartMinutes = timeInMinutes + minorTimeInterval / 2;
    row._updateTime = function(day) {
      this._time = O$.cloneDate(day);
      this._time.setHours(Math.floor(this._intervalStartMinutes / 60), this._intervalStartMinutes % 60, 0, 0);
    };
    if (row2) row2._updateTime = row._updateTime;
    row._updateTime(now);


    var timeHeader = showTimeAgainstMark ? row2.firstChild : row.firstChild;
    var headerCellSpan = 1;
    if (showTimeAgainstMark && intervalIndex != intervalCount - 1)
      headerCellSpan = 2;
    if (headerCellSpan != 1)
      timeHeader.rowSpan = headerCellSpan;
    if (intervalIndex == 0 && showTimeAgainstMark) {
      var emptyTimeHeader = row.firstChild;
      O$.appendClassNames(emptyTimeHeader, ["o_tinyText"]);
    }
    if (showTimeAgainstMark && intervalIndex == intervalCount - 1) {
      O$.appendClassNames(timeHeader, ["o_tinyText"]);
    }

    timeHeader._aHeaderCell = true;
    if (isMajorMark || showTimeForMinorIntervals) {
      if (showTimeAgainstMark)
        timeHeader.style.verticalAlign = "middle";
      var excludeTrailingMarkInscription = showTimeAgainstMark && headerCellSpan != 2;
      var markTime = row._time;
      if (showTimeAgainstMark) {
        markTime = O$.cloneDateTime(markTime);
        markTime.setMinutes(markTime.getMinutes() + minorTimeInterval);
      }

      if (!excludeTrailingMarkInscription) {
        var timeText = dateTimeFormat.format(markTime, timePattern);
        var timeSuffixText = dateTimeFormat.format(markTime, timeSuffixPattern);

        var timeStyle = isMajorMark ? majorTimeStyle : minorTimeStyle;
        var combinedTimeSuffixStyle = O$.combineClassNames([timeStyle, timeSuffixStyle]);
        timeHeader.appendChild(O$.createStyledText(timeText, timeStyle));
        timeHeader.appendChild(O$.createStyledText(timeSuffixText, combinedTimeSuffixStyle));
      }
    }

    var newRows = [row];
    if (row2)
      newRows.push(row2);
    table.body._addRows(newRows);

    var cells = row._cells;
    var cells2 = row2 ? row2._cells : null;
    for (var cellIndex = 1, cellCount = cells.length; cellIndex < cellCount; cellIndex++) {
      var cell = cells[cellIndex];
      var cell2 = cells2 ? cells2[cellIndex] : null;

      cell._resource = resources[cellIndex - 1];
      cell.onclick = function() {
        // onclick event can be fired on drag end under IE
        if (dayTable._draggingInProgress)
          return;
        if (editable)
          dayTable._addEvent(this._row._time, this._resource ? this._resource.id : null);
      };

      if (cell2) {
        cell2._cell = cell;
        cell2.onclick = function() {
          this._cell.onclick();
        };
      }
    }

    if (showTimeAgainstMark && intervalIndex > 0)
      row.removeChild(row.firstChild);
  }

  table.body._getBorderBottomForCell = function(rowIndex, colIndex, cell) {
    var correctedRowIndex = rowIndex + cell.rowSpan - 1;
    if (duplicatedRows && correctedRowIndex % 2 == 0)
      return "none";
    if (duplicatedRows)
      correctedRowIndex = (correctedRowIndex - 1) / 2;
    if (colIndex == 0) {
      return (correctedRowIndex % minorPerMajorIntervals) == minorPerMajorIntervals - 1 ? timeColumnPrimaryRowSeparator : timeColumnSecondaryRowSeparator;
    } else {
      return (correctedRowIndex % minorPerMajorIntervals) == minorPerMajorIntervals - 1 ? primaryRowSeparator : secondaryRowSeparator;
    }
  };

  table.body._overrideVerticalGridline(0, timeColumnSeparator);
  if (useResourceSeparation) {
    resourceHeadersTable.body._overrideVerticalGridline(0, timeColumnSeparator);
    resourceHeadersTable.body._overrideVerticalGridline(headerColumns.length - 2, O$.isExplorer6() ? "1px solid white" : "1px solid transparent");
  }

  dayTable._getLayoutCache = function() {
    if (!dayTable._cachedPositions)
      dayTable._cachedPositions = {};
    return dayTable._cachedPositions;
  };
  dayTable._getScrollingCache = function() {
    if (!dayTable._cachedScrollPositions)
      dayTable._cachedScrollPositions = {};
    return dayTable._cachedScrollPositions;
  };
  dayTable._resetScrollingCache = function() {
    dayTable._cachedScrollPositions = {};
  };

  function adjustRolloverPaddings() {
    var tempDiv = document.createElement("div");

    tempDiv.style.visibility = "hidden";
    tempDiv.style.position = "absolute";
    tempDiv.style.left = "0px";
    tempDiv.style.top = "0px";
    document.body.appendChild(tempDiv);

    setTimeout(function() {
      tempDiv.className = eventStyleClass;
      var eventStyleProperties = O$.getElementStyle(tempDiv, ["padding-left", "padding-right", "padding-top", "padding-bottom",
        "border-left-width", "border-top-width", "border-right-width", "border-bottom-width"]);
      tempDiv.className = rolloverEventClass;
      var rolloverEventStyleProperties = O$.getElementStyle(tempDiv, ["padding-left", "padding-right", "padding-top", "padding-bottom",
        "border-left-width", "border-top-width", "border-right-width", "border-bottom-width"]);
      document.body.removeChild(tempDiv);

      var userRolloverPaddings = O$.getStyleClassProperties(
              uiEvent.rolloverStyle, ["padding-left", "padding-right", "padding-top", "padding-bottom"]);

      var adjustedStyles = "";

      function adjustPaddingIfNotSpecified(paddingPropertyName, padding, border, rolloverPadding, rolloverBorder) {
        if (userRolloverPaddings[paddingPropertyName])
          return;
        rolloverPadding = O$.calculateNumericCSSValue(padding) + O$.calculateNumericCSSValue(border) - O$.calculateNumericCSSValue(rolloverBorder);
        adjustedStyles += paddingPropertyName + ": " + rolloverPadding + "px; ";
      }

      adjustPaddingIfNotSpecified("padding-left", eventStyleProperties.paddingLeft, eventStyleProperties.borderLeftWidth,
              rolloverEventStyleProperties.paddingLeft, rolloverEventStyleProperties.borderLeftWidth);
      adjustPaddingIfNotSpecified("padding-right", eventStyleProperties.paddingRight, eventStyleProperties.borderRightWidth,
              rolloverEventStyleProperties.paddingRight, rolloverEventStyleProperties.borderRightWidth);
      adjustPaddingIfNotSpecified("padding-top", eventStyleProperties.paddingTop, eventStyleProperties.borderTopWidth,
              rolloverEventStyleProperties.paddingTop, rolloverEventStyleProperties.borderTopWidth);
      adjustPaddingIfNotSpecified("padding-bottom", eventStyleProperties.paddingBottom, eventStyleProperties.borderBottomWidth,
              rolloverEventStyleProperties.paddingBottom, rolloverEventStyleProperties.borderBottomWidth);

      if (adjustedStyles) {
        var newClassName = O$.createCssClass(adjustedStyles);
        rolloverEventClass = O$.combineClassNames([rolloverEventClass, newClassName]);
      }
    }, 1);
  }

  adjustRolloverPaddings();

  var ieEventHandlerLayer = O$.isExplorer() ? O$.createAbsolutePositionedElement(dayTable._scroller) : null;
  var absoluteElementsParentNode = O$.createAbsolutePositionedElement(dayTable._scroller);
  dayTable._absoluteElementsParentNode = absoluteElementsParentNode;
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

  dayTable._getEventEditor = function() {
    if (!editable)
      return null;
    return dayTable._eventEditor;
  };


  function getNearestTimeslotForPosition(x, y) {
    var row = table.body._rowFromPoint(10, y, true, dayTable._getLayoutCache());
    if (!row)
      return {
        resource: undefined,
        time: y <= 0 ? dayTable._startTime : dayTable._endTime
      };

    var cell = row._cellFromPoint(x, y, true, dayTable._getLayoutCache());
    var resource;
    if (cell) {
      if (cell._cell) {
        cell = cell._cell;
        row = cell._row;
      }
      var nextCell = cell.nextSibling;
      if (!nextCell)
        resource = cell._resource;
      else {
        if (!cell._resource && nextCell._resource)
          resource = nextCell._resource;
        else {
          var x1 = O$.getElementPos(cell, true, dayTable._getLayoutCache()).x;
          var x2 = O$.getElementPos(nextCell, true, dayTable._getLayoutCache()).x;
          var nearestCell = Math.abs(x - x1) < Math.abs(x - x2) ? cell : nextCell;
          resource = nearestCell._resource;
        }
      }
    }

    if (row._row)
      row = row._row;
    var time;
    var rows = table.body._getRows();
    var rowIncrement = duplicatedRows ? 2 : 1;
    var nextRow = (row._index + rowIncrement < rows.length) ? rows[row._index + rowIncrement] : null;
    var timeAtPosition = new Date();
    if (!nextRow) {
      time = row._time;
      var rowRect = O$.getElementBorderRectangle(row, true, dayTable._getLayoutCache());
      timeAtPosition.setTime(row._time.getTime() + minutesPerRow * 60000 * (y - rowRect.y) / rowRect.height);
    } else {
      var y1 = O$.getElementPos(row, true, dayTable._getLayoutCache()).y;
      var y2 = O$.getElementPos(nextRow, true, dayTable._getLayoutCache()).y;
      var nearestRow = Math.abs(y - y1) < Math.abs(y - y2) ? row : nextRow;
      time = nearestRow._time;
      timeAtPosition.setTime(row._time.getTime() + minutesPerRow * 60000 * (y - y1) / (y2 - y1));
    }
    return {resource: resource, time: time, timeAtPosition: timeAtPosition};
  }

  function getVertOffsetByTime(time) {
    var hours = time.getHours();
    var minutes = time.getMinutes();
    var thisTime = time.getTime();
    var startTime = dayTable._startTime.getTime();
    if (thisTime < startTime)
      hours -= 24;
    var endTime = dayTable._endTime.getTime();
    if (thisTime == endTime && hours == 0)
      hours = 24;
    if (thisTime > endTime)
      hours += 24;
    var timeOffsetInMinutes = hours * 60 + minutes - startTimeInMinutes;
    var minutesPerRow = duplicatedRows ? minorTimeInterval / 2 : minorTimeInterval;
    var rowIndex = Math.floor(timeOffsetInMinutes / minutesPerRow);
    var relativePosInsideRow = (timeOffsetInMinutes % minutesPerRow) / minutesPerRow;
    var rows = table.body._getRows();
    var correctedRowIndex = rowIndex;
    if (correctedRowIndex < 0)
      correctedRowIndex = 0;
    if (correctedRowIndex >= rows.length)
      correctedRowIndex = rows.length - 1;
    var row = rows[correctedRowIndex];
    var rowRectangle = O$.getElementBorderRectangle(row, true);
    var result = {y: rowRectangle.y + rowRectangle.height * relativePosInsideRow};
    if (rowIndex < 0) {
      result.y += rowRectangle.height * rowIndex;
      result.topTruncated = true;
    }
    if (rowIndex >= rows.length) {
      result.y += rowRectangle.height;
      result.bottomTruncated = !(rowIndex == rows.length && minutes == 0);
    }
    return result;
  }

  function addEventElement(event) {
    var eventElement = document.createElement("div");
    eventElement._event = event;
    event.mainElement = eventElement;
    eventElement._dayTable = dayTable;

    var nameElement = document.createElement("span");
    nameElement.className = eventNameClass;
    eventElement.appendChild(nameElement);

    var descriptionElement = document.createElement("span");
    descriptionElement.className = eventDescriptionClass;
    eventElement.appendChild(descriptionElement);

    eventElement._attachAreas = function() {
      eventElement._areas = [];
      for (var areaIndex = 0, areaCount = eventAreaSettings.length; areaIndex < areaCount; areaIndex++) {
        var areaSettings = eventAreaSettings[areaIndex];
        var areaId = areaSettings.id;
        var areaClientId = dayTable.id + "::" + event.id + ":" + areaId;
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
    eventElement.style.zIndex = dayTable._baseZIndex + 5;

    eventElement._backgroundElement = document.createElement("div");
    eventElement._backgroundElement.className = eventBackgroundStyleClassName;
    eventElement._backgroundElement.style.zIndex = dayTable._baseZIndex + 4;
    event.backgroundElement = eventElement._backgroundElement;

    function canEventBeDropppedHere(event) {
      var startTime = event.start.getTime();
      var endTime = event.end.getTime();
      var resourceId = event.resourceId;
      for (var i = 0, count = dayTable._dayEvents.length; i < count; i++) {
        var currEvent = dayTable._dayEvents[i];
        if (currEvent == event)
          continue;
        if (currEvent.type != "reserved" && editingOptions.overlappedEventsAllowed)
          continue;
        if (currEvent.resourceId && resourceId && currEvent.resourceId != resourceId)
          continue;
        var timeSpansIntersect =
                currEvent.end.getTime() > startTime &&
                currEvent.start.getTime() < endTime;
        if (timeSpansIntersect)
          return false;
      }
      return true;
    }

    function setupDragAndDrop() {
      var eventPreview = getEventPreview();

      eventElement._updateDropAllowed = function() {
        var dropAllowed = canEventBeDropppedHere(event);
        if (dropAllowed) {
          eventElement._lastValidStart = event.start;
          eventElement._lastValidEnd = event.end;
          eventElement._lastValidResourceId = event.resourceId;
        }
        eventElement._setDropAllowed(dropAllowed);
      };
      eventElement._setDropAllowed = function(value) {
        if (this._dropAllowed == value)
          return;
        this._dropAllowed = value;

        O$.runTransitionEffect(eventElement, ["opacity"], [value ? 1.0 : 1.0 - undroppableEventTransparency], undroppableStateTransitionPeriod, undefined, {
          onupdate: function() {
            if (this.propertyValues.opacity !== undefined)
              O$.setOpacityLevel(eventElement._backgroundElement, this.propertyValues.opacity * (1 - eventBackgroundTransparency));
          }
        });
      };
      eventElement.onmousedown = function (e) {
        dayTable._resetScrollingCache();
        eventElement._bringToFront();
        O$.startDragging(e, this);
        eventElement._initialStart = eventElement._lastValidStart = event.start;
        eventElement._initialEnd = eventElement._lastValidEnd = event.end;
        eventElement._initialResourceId = eventElement._lastValidResourceId = event.resourceId;
        eventElement._originalCursor = O$.getElementStyle(eventElement, "cursor");
        eventElement._dropAllowed = true;
      };

      function hideExcessiveElementsWhileDragging() {
        if (eventPreview)
          setTimeout(function() {
            eventPreview.hide();
          }, 100);
      }

      eventElement.setPosition = function (left, top) {
        if (topResizeHandle)
          topResizeHandle.style.display = "none";
        if (bottomResizeHandle)
          bottomResizeHandle.style.display = "none";

        var nearestTimeslot = getNearestTimeslotForPosition(left, top);
        var newStartTime = nearestTimeslot.time;
        var newResource = editingOptions.eventResourceEditable ? nearestTimeslot.resource : undefined;
        var eventUpdated = false;
        if (event.resourceId && newResource !== undefined) {
          if (event.resourceId != newResource.id) {
            event.resourceId = newResource.id;
            eventUpdated = true;
          }
        }
        var timeIncrement = newStartTime.getTime() - event.start.getTime();
        if (timeIncrement != 0) {
          var newEndTime = O$.dateByTimeMillis(event.end.getTime() + timeIncrement);
          event.setStart(newStartTime);
          event.setEnd(newEndTime);
          eventUpdated = true;
        }
        if (eventUpdated) {
          eventElement._updateDropAllowed();
          eventElement.style.cursor = "move";//eventElement._setDropAllowed ? "move" : this._originalCursor;
          if (!event._draggingInProgress) {
            event._draggingInProgress = true;
            dayTable._draggingInProgress = true;
            hideExcessiveElementsWhileDragging();
          }
          event.mainElement._updatePos(true, dragAndDropTransitionPeriod, {
            onupdate: function() {
              event._scrollIntoView();
            }
          });

        }
      };
      eventElement.ondragend = function() {
        if (topResizeHandle)
          topResizeHandle.style.display = "";
        if (bottomResizeHandle)
          bottomResizeHandle.style.display = "";

        setTimeout(function() {
          if (event._draggingInProgress) {
            var draggingCanceled = false;
            var dropAllowed = canEventBeDropppedHere(event);
            if (!dropAllowed) {
              event.setStart(eventElement._initialStart);//eventElement._lastValidStart);
              event.setEnd(eventElement._initialEnd);//eventElement._lastValidEnd);
              event.resourceId = eventElement._initialResourceId;//eventElement._lastValidResourceId;
              draggingCanceled = true;
            }
            eventElement._setDropAllowed(true);
            eventElement.style.cursor = eventElement._originalCursor;

            if (event.start.getTime() >= dayTable._endTime.getTime() ||
                event.end.getTime() <= dayTable._startTime.getTime()) {
              dayTable._updateEventElements(true);
            } else {
              event.mainElement._updatePos(false, dragAndDropCancelingPeriod, {
                onupdate: function() {
                  event._scrollIntoView();
                }
              });
            }

            event._draggingInProgress = undefined;
            dayTable._draggingInProgress = undefined;

            if (!draggingCanceled) {
              putTimetableChanges(null, [event], null);
            } else {
              event._setMouseInside(false);
            }
          }

        }, 10);
      };
      eventElement._updateResizersPos = function(draggingInProgress) {
        if (!draggingInProgress) {
          var eventRect = event.mainElement._rect;
          if (topResizeHandle)
            O$.setElementBorderRectangle(topResizeHandle, new O$.Rectangle(eventRect.x, eventRect.y - eventResizeHandleHeight / 2, eventRect.width, eventResizeHandleHeight));
          if (bottomResizeHandle)
            O$.setElementBorderRectangle(bottomResizeHandle, new O$.Rectangle(eventRect.x, eventRect.getMaxY() - eventResizeHandleHeight / 2, eventRect.width, eventResizeHandleHeight));
        }
        this._updateZIndex();
      };
      eventElement._updateZIndex = function(eventZIndex) {
        if (eventZIndex === undefined)
          eventZIndex = O$.getNumericElementStyle(event.mainElement, "z-index");
        if (topResizeHandle)
          topResizeHandle.style.zIndex = eventZIndex + 2;
        if (bottomResizeHandle)
          bottomResizeHandle.style.zIndex = eventZIndex + 2;
        this._updateAreaZIndexes();
      };

      if (editingOptions.eventDurationEditable) {
        var topResizeHandle = editingOptions.eventDurationEditable ? document.createElement("div") : null;
        var bottomResizeHandle = editingOptions.eventDurationEditable ? document.createElement("div") : null;
        topResizeHandle.style.fontSize = bottomResizeHandle.style.fontSize = "0px";
        topResizeHandle.style.position = bottomResizeHandle.style.position = "absolute";
        topResizeHandle.style.cursor = "n-resize";
        bottomResizeHandle.style.cursor = "s-resize";

        O$.fixIEEventsForTransparentLayer(topResizeHandle);
        O$.fixIEEventsForTransparentLayer(bottomResizeHandle);

        topResizeHandle.onclick = bottomResizeHandle.onclick = function(e) {
          O$.breakEvent(e);
        };
        topResizeHandle.onmousedown = bottomResizeHandle.onmousedown = eventElement.onmousedown;
        topResizeHandle.setPosition = bottomResizeHandle.setPosition = function(left, top) {
          var nearestTimeslot = getNearestTimeslotForPosition(left, top + eventResizeHandleHeight / 2);
          var eventUpdated = false;
          if (this == topResizeHandle) {
            if (event.end.getTime() - nearestTimeslot.time < shortestEventTimeWhileResizing)
              nearestTimeslot.time = O$.dateByTimeMillis(event.end.getTime() - shortestEventTimeWhileResizing);
            if (event.start.getTime() != nearestTimeslot.time) {
              event.setStart(nearestTimeslot.time);
              eventUpdated = true;
            }
          } else {
            if (nearestTimeslot.time - event.start.getTime() < shortestEventTimeWhileResizing)
              nearestTimeslot.time = O$.dateByTimeMillis(event.start.getTime() + shortestEventTimeWhileResizing);
            if (event.end.getTime() != nearestTimeslot.time) {
              event.setEnd(nearestTimeslot.time);
              eventUpdated = true;
            }
          }
          if (eventUpdated) {
            event.mainElement._updatePos(true, dragAndDropTransitionPeriod, {
              onupdate: function() {
                event._scrollIntoView();
              }
            });
            eventElement._updateDropAllowed();
          }
          if (eventUpdated && !event._draggingInProgress) {
            event._draggingInProgress = true;
            dayTable._draggingInProgress = true;
            hideExcessiveElementsWhileDragging();
          }
          eventElement._updateResizersPos();
        };
        topResizeHandle.ondragend = bottomResizeHandle.ondragend = eventElement.ondragend;

        function setResizerHoverState(mouseInside, resizer) {
          resizer._mouseInside = mouseInside;
          O$.invokeFunctionAfterDelay(event._updateRolloverState, O$.EVENT_ROLLOVER_STATE_UPDATE_TIMEOUT);
        }

        O$.setupHoverStateFunction(topResizeHandle, setResizerHoverState);
        O$.setupHoverStateFunction(bottomResizeHandle, setResizerHoverState);
      }

      eventElement._topResizeHandle = topResizeHandle;
      eventElement._bottomResizeHandle = bottomResizeHandle;
      if (topResizeHandle)
        absoluteElementsParentNode.appendChild(topResizeHandle);
      if (bottomResizeHandle)
        absoluteElementsParentNode.appendChild(bottomResizeHandle);
      eventElement._removeNodes = function() {
        if (topResizeHandle)
          absoluteElementsParentNode.removeChild(topResizeHandle);
        if (bottomResizeHandle)
          absoluteElementsParentNode.removeChild(bottomResizeHandle);
        var areas = eventElement._areas;
        for (var i = 0, count = areas.length; i < count; i++) {
          var area = areas[i];
          if (area.parentNode == null)
            continue; // don't add an area back to document if it was already removed by Ajax 
          hiddenArea.appendChild(area);
        }
      };
    }

    if (event.type != "reserved" && editable) {
      setupDragAndDrop();
    }

    eventElement._update = function(transitionPeriod) {
      this._updatePos(false, transitionPeriod);
      if (event.type != "reserved") {
        O$.setInnerText(nameElement, event.name, escapeEventNames);
        O$.setInnerText(descriptionElement, event.description, escapeEventDescriptions);
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
    eventElement._updatePos = function(draggingInProgress, transitionPeriod, transitionEvents) {
      var resourceColIndex;
      if (event.resourceId) {
        var resource = dayTable._getResourceForEvent(event);
        if (!resource) {
          this.style.display = "none";
          return;
        }
        resourceColIndex = resource._colIndex;
      }
      this.style.display = "";
      var firstDataRow = table.body._getRows()[0];
      var leftColBoundaries = O$.getElementBorderRectangle(firstDataRow._cells[resourceColIndex != undefined ? resourceColIndex : 1], true, dayTable._getLayoutCache());
      var rightColBoundaries = O$.getElementBorderRectangle(firstDataRow._cells[resourceColIndex != undefined ? resourceColIndex : columns.length - 1], true, dayTable._getLayoutCache());
      var top = getVertOffsetByTime(event.start);
      var bottom = getVertOffsetByTime(event.end);
      var x1 = leftColBoundaries.getMinX() + (event.type != "reserved" ? eventsLeftOffset : reservedEventsLeftOffset);
      var x2 = rightColBoundaries.getMaxX() - (event.type != "reserved" ? eventsRightOffset : reservedEventsRightOffset);
      if (O$.isExplorer() && O$.isStrictMode() && (resourceColIndex === undefined || resourceColIndex == columns.length - 1)) {
        var scroller = O$(dayTable.id + "::scroller");
        var scrollerWidth = scroller.offsetWidth - scroller.clientWidth;
        x2 -= scrollerWidth;
      }
      var rect = new O$.Rectangle(Math.round(x1), Math.round(top.y),
              Math.round(x2 - x1), Math.round(bottom.y - top.y));
      this._rect = rect;
      if (!transitionPeriod)
        transitionPeriod = 0;
      var backgroundElement = this._backgroundElement;
      if (this._lastRectangleTransition && this._lastRectangleTransition.active)
        this._lastRectangleTransition.stop(1.0);
      var events = {
        onupdate: function() {
          var currentRect = this.propertyValues.rectangle;
          if (currentRect)
            O$.setElementBorderRectangle(backgroundElement, currentRect);
          if (transitionEvents && transitionEvents.onupdate)
            transitionEvents.onupdate();
          eventElement._currentRect = currentRect;
          eventElement._updateAreaPositions(false);
        }
      };
      if (transitionEvents)
        events.oncomplete = transitionEvents.oncomplete;
      this._lastRectangleTransition = O$.runTransitionEffect(this, ["rectangle"], [rect], transitionPeriod, 20, events);

      if (eventElement._updateResizersPos)
        eventElement._updateResizersPos(draggingInProgress);
      var eventZIndex = O$.getNumericElementStyle(this, "z-index");
      this._backgroundElement.style.zIndex = eventZIndex - 1;
      if (bottom.bottomTruncated)
        O$.appendClassNames(this, ["o_truncatedTimetableEvent"]);
      else
        O$.excludeClassNames(this, ["o_truncatedTimetableEvent"]);
      this._updateAreaPositions(true);
    };
    eventElement._bringToFront = function() {
      var index = O$.findValueInArray(this, dayTable._eventElements);
      O$.assert(index != -1, "eventElement._bringToFront. Can't find element in _eventElements array.");
      dayTable._eventElements.splice(index, 1);
      dayTable._eventElements.push(this);
      dayTable._updateEventZIndexes();
    };

    event._updateRolloverState = function() {
      var eventElement = event.mainElement;
      if (!eventElement) {
        // this can be the case because _updateRolloverState is invoked by time-out, so if mouseOver/mouseOut happens
        // just before element is replaced with Ajax, this call will be made when there's no original element anymore
        return;
      }
      var actionBar = getEventActionBar();
      var elementResizable = eventElement._topResizeHandle || eventElement._bottomResizeHandle;
      event._setMouseInside(eventElement._mouseInside ||
                            elementResizable && (eventElement._topResizeHandle._mouseInside || eventElement._bottomResizeHandle._mouseInside) ||
                            (actionBar._event == event && actionBar._actionsArea._mouseInside));
    };

    var eventPreview = getEventPreview();
    event._setMouseInside = function(value) {
      if (event._mouseInside == value)
        return;
      event._mouseInside = value;
      if (event._draggingInProgress)
        return;
      if (event.type == "reserved")
        return;
      if (value) {
        O$.setStyleMappings(eventElement, {_rolloverStyle: rolloverEventClass});
        O$.setElementBorderRectangle(eventElement, eventElement._rect);
        eventElement._updateAreaPositions(true);
        showEventActionBar(event);

        if (eventPreview) {
          setTimeout(function() {
            if (event._mouseInside && !event._draggingInProgress)
              eventPreview.showForEvent(event);
          }, eventPreview._showingDelay);
        }
        if (eventElement._onmouseover) {
          eventElement._onmouseover(O$.createEvent("mouseover"));
        }
      } else {
        O$.setStyleMappings(eventElement, {_rolloverStyle: null});
        O$.setElementBorderRectangle(eventElement, eventElement._currentRect);
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
      O$.invokeFunctionAfterDelay(event._updateRolloverState, O$.EVENT_ROLLOVER_STATE_UPDATE_TIMEOUT);
    });

    if (editable) {
      eventElement.onclick = function(e) {
        O$.breakEvent(e);
        if (event._draggingInProgress)
          return;
        if (event.type == "reserved")
          return;
        dayTable._getEventEditor().run(event, "update");
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
    if (!dayTable._eventActionBar) {
      dayTable._eventActionBar = O$(dayTable.id + ":_eventActionBar");
      if (!dayTable._eventActionBar)
        return null;
      dayTable._eventActionBar.style.position = "absolute";
      dayTable._eventActionBar.style.visibility = "hidden";

    }
    return dayTable._eventActionBar;
  }

  function getEventPreview() {
    if (!dayTable._eventPreview) {
      dayTable._eventPreview = O$(dayTable.id + ":_eventPreview");
    }
    return dayTable._eventPreview;
  }

  function showEventActionBar(event) {
    var eventElement = event.mainElement;
    var actionBar = getEventActionBar();
    actionBar._event = event;
    var userSpecifiedStyles = O$.getStyleClassProperties(actionBar._userSpecifiedClass, ["color", "background-color"]);
    actionBar.style.backgroundColor = userSpecifiedStyles.backgroundColor
            ? userSpecifiedStyles.backgroundColor
            : O$.blendColors(eventElement._color, "#ffffff", 1 - actionBar._backgroundIntensity);
    eventElement.appendChild(actionBar);
    actionBar.style.height = "";
    actionBar.style.width = "";
    var barHeight = actionBar.offsetHeight;
    var actionsAreaHeight = actionBar._actionsArea._getHeight();
    if (barHeight < actionsAreaHeight)
      barHeight = actionsAreaHeight;
    var borderLeftWidth = O$.getNumericElementStyle(eventElement, "border-left-width");
    var borderRightWidth = O$.getNumericElementStyle(eventElement, "border-right-width");
    O$.setElementSize(actionBar, {width: eventElement._rect.width - borderLeftWidth - borderRightWidth,
      height: barHeight});
    actionBar.style.left = "0px";
    actionBar.style.bottom = "0px";
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

  dayTable.cancelEventCreation = function(event) {
    event._creationInProgress = undefined;
    removeEventElement(event);
  };

  dayTable.addEvent = function(event) {
    event.setStart(event.start, event.startStr);
    event.setEnd(event.end, event.endStr);
    if (event._creationInProgress) {
      event._creationInProgress = undefined;
      removeEventElement(event);
    }

    eventProvider.addEvent(event);
    dayTable._updateEventElements(true);
    putTimetableChanges([event], null, null);
  };

  dayTable.deleteEvent = function(event) {
    eventProvider.deleteEvent(event);
    dayTable._updateEventElements(true);
    putTimetableChanges(null, null, [event.id]);
  };

  dayTable.getEventById = function(eventId) {
    return eventProvider.getEventById(eventId);
  };

  dayTable.updateEvent = function(event) {
    event.setStart(event.start, event.startStr);
    event.setEnd(event.end, event.endStr);

    event.updatePresentation();
    putTimetableChanges(null, [event], null);
  };

  dayTable.refreshEvents = function(serverAction) {
    this._saveChanges(true, serverAction, dayTable._startTime, dayTable._endTime);
  };

  dayTable.saveChanges = function() {
    this._saveChanges(false, null, dayTable._startTime, dayTable._endTime);
  };

  dayTable._saveChanges = function(reloadAllEvents, serverAction, reloadStartTime, reloadEndTime) {
    O$.requestComponentPortions(dayTable.id, ["saveEventChanges"], JSON.stringify(
    {reloadAllEvents: !!reloadAllEvents, startTime: O$.formatDateTime(reloadStartTime), endTime: O$.formatDateTime(reloadEndTime)},
            ["reloadAllEvents", "startTime", "endTime"]), function(
            component, portionName, portionHTML, portionScripts, portionData) {
      var remainingElements = O$.replaceDocumentElements(portionHTML, true);
      if (remainingElements.hasChildNodes())
        hiddenArea.appendChild(remainingElements);
      O$.executeScripts(portionScripts);

      if (portionData.reloadedEvents) {
        eventProvider.setEvents(portionData.reloadedEvents, reloadStartTime, reloadEndTime);
        dayTable._updateEventElements(true, true);
      } else {
        if (portionData.addedEvents) {
          var addedCount = portionData.addedEvents.length;
          O$.assertEquals(dayTable._addedEvents.length, addedCount, "addedEventCount should be same as dayTable._addedEvents.length");
          for (var addedIdx = 0; addedIdx < addedCount; addedIdx++) {
            var addedEvent = dayTable._addedEvents[addedIdx];
            addedEvent._copyFrom(portionData.addedEvents[addedIdx]);
            addedEvent.updatePresentation(dragAndDropCancelingPeriod);
            addedEvent.mainElement._attachAreas();
            addedEvent.mainElement._updateAreaPositions();
          }
        }
        if (portionData.editedEvents) {
          var editedCount = portionData.editedEvents.length;
          O$.assertEquals(dayTable._editedEvents.length, editedCount, "editedEventCount should be same as dayTable._editedEvents.length");
          for (var editedIdx = 0; editedIdx < editedCount; editedIdx++) {
            var editedEvent = dayTable._editedEvents[editedIdx];
            editedEvent._copyFrom(portionData.editedEvents[editedIdx]);
            editedEvent.mainElement._attachAreas();
            editedEvent.updatePresentation(dragAndDropCancelingPeriod);
          }
        }
      }

      dayTable._addedEvents = [];
      dayTable._editedEvents = [];
      dayTable._removedEventIds = [];
      updateTimetableChangesField();
    }, function () {
      alert('Error saving the last timetable change');
    }, serverAction);
  };

  dayTable._dayTextElement = O$(dayTable.id + "::dayText");

  dayTable.getDay = function() {
    return dayTable._day;
  };

  dayTable.setDay = function(day) {
    if (O$._datesEqual(dayTable._day, day))
      return;
    if (dayTable._onDayChange) {
      dayTable._onDayChange(day);
    }

    var dtf = O$.getDateTimeFormatObject(locale);
    O$.setHiddenField(dayTable, dayTable.id + "::day", dtf.format(day, "dd/MM/yyyy"));

    dayTable._day = day;
    dayTable._startTime = O$.parseTime(startTimeStr, O$.cloneDate(day));
    dayTable._endTime = O$.parseTime(endTimeStr, O$.cloneDate(day));
    dayTable._dayEvents = eventProvider._getEventsForPeriod(dayTable._startTime, dayTable._endTime, function() {
      dayTable._updateEventElements(true, true);
    });

    dayTable._updateEventElements();

    var rows = table.body._getRows();
    for (var rowIndex = 0, rowCount = rows.length; rowIndex < rowCount; rowIndex++) {
      var row = rows[rowIndex];
      row._updateTime(day);
    }
  };

  dayTable._updateEventElements = function(reacquireDayEvents, refreshAreasAfterReload) {
    dayTable._baseZIndex = O$.getElementZIndex(dayTable);
    if (dayTable._eventElements)
      for (var elementIndex = 0, elementCount = dayTable._eventElements.length; elementIndex < elementCount; elementIndex++) {
        var eventElement = dayTable._eventElements[elementIndex];
        if (refreshAreasAfterReload)
          eventElement._attachAreas();
        removeEventElement(eventElement._event);
      }

    dayTable._eventElements = [];
    if (reacquireDayEvents)
      dayTable._dayEvents = eventProvider._getEventsForPeriod(dayTable._startTime, dayTable._endTime, function() {
        dayTable._updateEventElements(true, true);
      });
    for (var eventIndex = 0, eventCount = dayTable._dayEvents.length; eventIndex < eventCount; eventIndex++) {
      var event = dayTable._dayEvents[eventIndex];
      dayTable._eventElements.push(addEventElement(event));
    }
    dayTable._updateEventZIndexes();
  };

  dayTable._baseZIndex = O$.getElementZIndex(dayTable);
  dayTable._maxZIndex = dayTable._baseZIndex + 10;
  dayTable._updateEventZIndexes = function() {
    if (!dayTable._eventElements)
      return;
    for (var elementIndex = 0, elementCount = dayTable._eventElements.length; elementIndex < elementCount; elementIndex++) {
      var eventElement = dayTable._eventElements[elementIndex];
      var eventZIndex = dayTable._baseZIndex + (elementIndex + 1) * 5;
      eventElement.style.zIndex = eventZIndex;
      eventElement._backgroundElement.style.zIndex = eventZIndex - 1;
      if (eventElement._updateZIndex)
        eventElement._updateZIndex(eventZIndex);
    }
    dayTable._maxZIndex = eventZIndex + 10;
  };

  dayTable.previousDay = function() {
    var prevDay = O$.incDay(dayTable._day, -1);
    dayTable.setDay(prevDay);
  };

  dayTable.nextDay = function() {
    var nextDay = O$.incDay(dayTable._day, 1);
    dayTable.setDay(nextDay);
  };

  dayTable.today = function() {
    var today = new Date();
    dayTable.setDay(today);
  };

  var dtf = O$.getDateTimeFormatObject(locale);
  dayTable.setDay(dtf.parse(day, "dd/MM/yyyy"));

  dayTable._addEvent = function(startTime, resourceId) {
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
    O$._initEvent(event);
    addEventElement(event);
    event._creationInProgress = true;
    dayTable._getEventEditor().run(event, "create");
  };

  dayTable.updateLayout = function() {
    dayTable._cachedPositions = {};
    dayTable._declaredDayTableHeight = O$.getStyleClassProperty(dayTable.className, "height");
    if (dayTable._declaredDayTableHeight === undefined) {
      dayTable._scroller.style.overflow = "visible";
      dayTable._scroller.style.overflowX = "visible";
      dayTable._scroller.style.overflowY = "hidden";
      dayTable._scroller.style.height = "auto";
    } else {
      if (O$.isOpera()) {
        dayTable._scroller.style.overflow = "scroll"; // Opera < 9.5 doesn't understand specifying just overflow-y
      }
    }

    updateHeightForFF();
    accountForScrollerWidth();
    absoluteElementsParentNode._updatePos();
    for (var eventIndex = 0, eventCount = dayTable._dayEvents.length; eventIndex < eventCount; eventIndex++) {
      var event = dayTable._dayEvents[eventIndex];
      event.updatePresentation();
    }

  };

  O$.addEventHandler(window, "resize", function() {
    dayTable.updateLayout();
  });
  O$.addInternalLoadEvent(function() {
    dayTable.updateLayout(); // update positions after layout changes that might have had place during loading

    var scrollOffset = getVertOffsetByTime(scrollTime).y;
    var maxScrollOffset = dayTable._scroller.scrollHeight - O$.getElementSize(dayTable._scroller).height;
    if (maxScrollOffset < 0)
      maxScrollOffset = 0;
    if (scrollOffset > maxScrollOffset)
      scrollOffset = maxScrollOffset;
    dayTable._scroller.scrollTop = scrollOffset;
    O$.addEventHandler(dayTable._scroller, "scroll", function() {
      var timeslot = getNearestTimeslotForPosition(10, dayTable._scroller.scrollTop);
      O$.setHiddenField(dayTable, dayTable.id + "::scrollPos", O$.formatTime(timeslot.timeAtPosition));
    });
  });
  if (O$._documentLoaded) {
    // update in case when DayTable was loaded with Ajax
    setTimeout(function() {dayTable.updateLayout()}, 1);
  }

  function updateHeightForFF() {
    // FireFox's scroller height includes the entire table without truncating it according to
    // user-specified height in day table, so we should truncate height ourselves
    if (!O$.isMozillaFF())
      return;

    if (dayTable._declaredDayTableHeight === undefined)
      return;
    var scrollerHeight = dayTable._declaredDayTableHeight;
    if (!O$.stringEndsWith(dayTable._declaredDayTableHeight, "%")) {
      var height = O$.calculateNumericCSSValue(dayTable._declaredDayTableHeight);
      if (height) {
        var offset = O$.getElementPos(dayTable._scroller).y - O$.getElementPos(dayTable).y;
        height -= offset;
        scrollerHeight = height + "px";
      }
    }
    dayTable._scroller.style.height = scrollerHeight;
  }

  function accountForScrollerWidth() {
    if (!useResourceSeparation)
      return;
    var firstHeaderRow = resourceHeadersTable.body._getRows()[0];
    var firstDataRow = table.body._getRows()[0];
    var visibleDataWidth = O$.getElementBorderRectangle(firstDataRow._cells[firstDataRow._cells.length - 1]).getMaxX() - O$.getElementPos(firstDataRow._cells[0]).x;
    var totalHeaderWidth = O$.getElementBorderRectangle(firstHeaderRow._cells[firstHeaderRow._cells.length - 1]).getMaxX() - O$.getElementPos(firstHeaderRow._cells[0]).x;
    var lastColWidth = totalHeaderWidth - visibleDataWidth;
    if (lastColWidth < 0) // can be the case under Mozilla3 because O$.getElementBorderRectangle returns non-rounded coordinates, which may result in values like -0.2499...
      lastColWidth = 0;
    if (lastColWidth == 0 && O$.isOpera())
      lastColWidth = 1;
    firstHeaderRow._cells[firstHeaderRow._cells.length - 1].style.width = lastColWidth + "px";
  }

  updateHeightForFF();

  function putTimetableChanges(addedEvents, changedEvents, removedEventIds, initialAssignment) {
    if (!dayTable._addedEvents) dayTable._addedEvents = [];
    if (!dayTable._editedEvents) dayTable._editedEvents = [];
    if (!dayTable._removedEventIds) dayTable._removedEventIds = [];
    if (addedEvents)
      dayTable._addedEvents = dayTable._addedEvents.concat(addedEvents);
    if (changedEvents) {
      for (var editedIdx = 0, editedCount = changedEvents.length; editedIdx < editedCount; editedIdx++) {
        var editedEvent = changedEvents[editedIdx];
        if (O$.findValueInArray(editedEvent, dayTable._addedEvents) != -1)
          continue;
        if (O$.findValueInArray(editedEvent, dayTable._editedEvents) == -1) {
          dayTable._editedEvents.push(editedEvent);
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
        var addedEventIndex = eventIndexById(dayTable._addedEvents, removedEventId);
        if (addedEventIndex != -1) {
          dayTable._addedEvents.splice(addedEventIndex, 1);
          continue;
        }
        var editedEventIndex = eventIndexById(dayTable._editedEvents, removedEventId);
        if (editedEventIndex != -1)
          dayTable._editedEvents.splice(editedEventIndex, 1);
        dayTable._removedEventIds.push(removedEventId);
      }
    }

    updateTimetableChangesField();

    if (dayTable.onchange) {
      var e = O$.createEvent("change");
      e.addedEvents = addedEvents ? addedEvents : {};
      e.changedEvents = changedEvents ? changedEvents : {};
      e.removedEventIds = removedEventIds ? removedEventIds : {};

      if (dayTable.onchange(e) === false)
        return;
    }

    if (editingOptions.autoSaveChanges && !initialAssignment)
      dayTable.saveChanges();
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
      arrayToJSON("addedEvents", dayTable._addedEvents, properties),
      arrayToJSON("editedEvents", dayTable._editedEvents, properties),
      arrayToJSON("removedEventIds", dayTable._removedEventIds, properties)
    ];
    var changesAsString = "{" + events.join(",") + "}";
    O$.setHiddenField(dayTable, dayTable.id + "::timetableChanges", changesAsString);
  }

  putTimetableChanges(null, null, null, true);
  O$.assignEvents(dayTable, {onchange: onchange}, true);
};

O$._findEventById = function(events, id) {
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

O$._LazyLoadedTimetableEvents = function(preloadedEvents, preloadedStartTime, preloadedEndTime) {
  O$._PreloadedTimetableEvents.call(this, []);

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
    O$.requestComponentPortions(this._dayTable.id, ["loadEvents"],
            JSON.stringify(
            {startTime: O$.formatDateTime(start), endTime: O$.formatDateTime(end)},
                    ["startTime", "endTime"]),
            function(component, portionName, portionHTML, portionScripts, portionData) {
              var remainingElements = O$.replaceDocumentElements(portionHTML, true);
              if (remainingElements.hasChildNodes())
                thisProvider._dayTable._hiddenArea.appendChild(remainingElements);
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

O$._PreloadedTimetableEvents = function(events) {

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
      O$._initEvent(event);
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
    O$._initEvent(event);
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

O$._initEventEditorDialog = function(dayTableId, dialogId, createEventCaption, editEventCaption, centered) {
  var dayTable = O$(dayTableId);
  var dialog = O$(dialogId);
  dayTable._eventEditor = dialog;
  dialog._dayTable = dayTable;

  dialog._nameField = O$.byIdOrName(dialog.id + "--nameField");
  dialog._resourceField = O$.byIdOrName(dialog.id + "--resourceField");
  dialog._startDateField = O$.byIdOrName(dialog.id + "--startDateField");
  dialog._endDateField = O$.byIdOrName(dialog.id + "--endDateField");
  dialog._startTimeField = O$.byIdOrName(dialog.id + "--startTimeField");
  dialog._endTimeField = O$.byIdOrName(dialog.id + "--endTimeField");
  dialog._colorField = O$.byIdOrName(dialog.id + "--colorField");
  dialog._color = "";
  dialog._descriptionArea = O$.byIdOrName(dialog.id + "--descriptionArea");
  dialog._okButton = O$.byIdOrName(dialog.id + "--okButton");
  dialog._cancelButton = O$.byIdOrName(dialog.id + "--cancelButton");
  dialog._deleteButton = O$.byIdOrName(dialog.id + "--deleteButton");

  var fixedDurationMode = !dialog._endDateField;

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

  dialog.run = function(event, mode) {
    this._event = event;
    setFieldText(this._nameField, event.name);
    var resource = dayTable._getResourceForEvent(event);
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
      O$.breakEvent(e);
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
        event.resourceId = dayTable._idsByResourceNames[dialog._resourceField.getValue()];
      event.color = dialog._color ? dialog._color : "";
      event.description = getFieldText(dialog._descriptionArea);
      dialog.hide();
      if (mode == "create")
        dayTable.addEvent(event);
      else
        dayTable.updateEvent(event);
    };

    this._cancelButton.onclick = function(e) {
      O$.breakEvent(e);
      dialog.hide();
      if (mode == "create")
        dayTable.cancelEventCreation(event);
    };

    this._deleteButton.onclick = function(e) {
      O$.breakEvent(e);
      dialog.hide();
      if (mode == "update")
        dayTable.deleteEvent(event);
    };

    this.onhide = function() {
      if (!this._okProcessed && mode == "create")
        dayTable.cancelEventCreation(event);
      if (dialog._textareaHeightUpdateInterval)
        clearInterval(dialog._textareaHeightUpdateInterval);
    };

    if (event.mainElement)
      O$.correctElementZIndex(this, event.mainElement, 5);
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

  };

};

O$._initEventEditorPage = function(dayTableId, thisComponentId, actionDeclared, url, modeParamName,
                                   eventIdParamName, eventStartParamName, eventEndParamName, resourceIdParamName) {
  var dayTable = O$(dayTableId);
  var thisComponent = O$(thisComponentId);
  dayTable._eventEditor = thisComponent;
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
      O$.submitFormWithAdditionalParams(dayTable, params);
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
};

O$._initCustomEventEditor = function(dayTableId, thisComponentId, oncreate, onedit) {
  var dayTable = O$(dayTableId);
  var thisComponent = O$(thisComponentId);
  dayTable._eventEditor = thisComponent;
  thisComponent.run = function(event, mode) {
    if (mode == "create")
      oncreate(dayTable, event);
    else
      onedit(dayTable, event);
  };

};

O$._initEvent = function(event) {
  event.setStart = function(asDate, asString) {
    if (asDate) {
      event.start = asDate;
      event.startStr = O$.formatDateTime(asDate);
    } else
      if (asString) {
        event.startStr = asString;
        event.start = O$.parseDateTime(asString);
      } else
        throw "event.setStart: either asDate parameter, or asTime parameter should be specified";
  };
  event.setEnd = function(asDate, asString) {
    if (asDate) {
      event.end = asDate;
      event.endStr = O$.formatDateTime(asDate);
    } else
      if (asString) {
        event.endStr = asString;
        event.end = O$.parseDateTime(asString);
      } else
        throw "event.setEnd: either asDate parameter, or asTime parameter should be specified";
  };
  event._copyFrom = function(otherEvent) {
    this.id = otherEvent.id;
    this.setStart(otherEvent.start, otherEvent.startStr);
    this.setEnd(otherEvent.end, otherEvent.endStr);
    this.name = otherEvent.name;
    this.description = otherEvent.description;
    this.resourceId = otherEvent.resourceId;
    this.color = otherEvent.color;
  };
  event.updatePresentation = function(transitionPeriod) {
    if (event.mainElement) {
      event.mainElement._update(transitionPeriod);
      event.mainElement._updateAreaPositions(true);
    }
  };
  event._scrollIntoView = function() {
    /*    return; //todo: finish auto-scrolling functionality
     var dayTable = event.mainElement._dayTable;
     var scrollingOccured = O$.scrollElementIntoView(event.mainElement, dayTable._getScrollingCache());
     if (scrollingOccured)
     dayTable._resetScrollingCache();*/
  };
  if (event.start || event.startStr)
    event.setStart(event.start, event.startStr);
  if (event.end || event.endStr)
    event.setEnd(event.end, event.endStr);
};

O$._initEventPreview = function(eventPreviewId, dayTableId, showingDelay, popupClass,
                                eventNameClass, eventDescriptionClass,
                                horizontalAlignment, verticalAlignment, horizontalDistance, verticalDistance) {
  var eventPreview = O$(eventPreviewId);
  var popupLayer = O$(eventPreviewId + "--popupLayer");
  O$.appendClassNames(popupLayer, [popupClass]);

  eventNameClass = O$.combineClassNames(["o_timetableEventName", eventNameClass]);
  eventDescriptionClass = O$.combineClassNames(["o_timetableEventDescription", eventDescriptionClass]);

  eventPreview._showingDelay = showingDelay;

  eventPreview.showForEvent = function(event) {
    var dayTable = O$(dayTableId);
    var popupParent = O$.getDefaultAbsolutePositionParent();
    if (popupLayer.parentNode != popupParent) {
      popupParent.appendChild(popupLayer);
    }
    O$.correctElementZIndex(popupLayer, dayTable);

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

    O$.setInnerText(nameElement, event.name, dayTable._escapeEventNames);
    O$.setInnerText(descriptionElement, event.description, dayTable._escapeEventDescriptions);

    if (!event.mainElement)
      popupLayer.showCentered();
    else
      popupLayer.showByElement(event.mainElement,
              horizontalAlignment, verticalAlignment, horizontalDistance, verticalDistance);
  };

  eventPreview.hide = function() {
    popupLayer.hide();
  };
};


O$._initEventActionBar = function(actionBarId, dayTableId, backgroundIntensity, userSpecifiedClass, actions,
                                  actionRolloverIntensity, actionPressedIntensity) {
  var actionBar = O$(actionBarId);
  if (!actionBar) {
    var initArgs = arguments;
    // postpone initialization to avoid FF2 failure of finding actionBar element in some cases
    setTimeout(function() {
      O$._initEventActionBar.apply(null, initArgs);
    }, 100);
    return;
  }

  actionBar._backgroundIntensity = backgroundIntensity;
  actionBar._userSpecifiedClass = userSpecifiedClass;
  actionBar.className = O$.combineClassNames(["o_eventActionBar", userSpecifiedClass]);

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
    action._cell = cell;
    cell._index = i;
    cell._action = action;
    cell._image = image;

    tr.appendChild(cell);
    cell.vAlign = "middle";
    cell.align = "center";
    cell.id = action.id;
    cell.className = action.style[0];
    var image = document.createElement("img");
    image.src = action.image[0];
    if (action.hint)
      image.title = action.hint;
    cell.appendChild(image);
    O$.assignEvents(cell, action, true);
    O$.preloadImage(action.image[0]);
    O$.preloadImage(action.image[1]);
    O$.preloadImage(action.image[2]);
    cell._userClickHandler = cell.onclick;
    cell.onmousedown = function() {
      this._timetableEvent = actionBar._event ? actionBar._event : actionBar._lastEditedEvent;
      this._dayTable = O$(dayTableId);
      this._timetableEvent.mainElement._bringToFront();
    };
    cell.onclick = function(e) {
      e = O$.getEvent(e);
      e._timetableEvent = this._timetableEvent;
      e._dayTable = this._dayTable;
      if (this._userClickHandler) {
        if (this._userClickHandler(e) === false || e.returnValue === false)
          return;
      }

      var eventId = this._timetableEvent.id;
      O$.setHiddenField(this._dayTable, actionBarId + "::" + this._index, eventId);
      O$.submitEnclosingForm(this._dayTable);
    };
    function setupStateHighlighting(cell) {
      cell._mouseState = O$.setupHoverAndPressStateFunction(cell, function(mouseInside, pressed) {
        cell._mouseInside = mouseInside;
        cell._pressed = pressed;
        cell._update();
      });
    }

    setupStateHighlighting(cell);
    cell._update = function() {
      var mouseInside = this._mouseInside;
      var pressed = this._pressed;
      O$.setStyleMappings(this, {
        _rolloverStyle: mouseInside ? this._action.style[1] : null,
        _pressedStyle: pressed ? this._action.style[2] : null});
      var userSpecifiedBackground = O$.getStyleClassProperty(this.className, "background-color");
      if (!userSpecifiedBackground) {
        var event = actionBar._event ? actionBar._event : actionBar._lastEditedEvent;
        var intensity = pressed ? actionPressedIntensity : mouseInside ? actionRolloverIntensity : backgroundIntensity;
        this.style.backgroundColor = O$.blendColors(event.mainElement._color, "#ffffff", 1 - intensity);
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
    };
  }
  actionsTable.onclick = function(e) {
    O$.breakEvent(e); // avoid passing event to the absoluteElementsParentNode
  };
  actionBar._actionsArea = actionsTable;
  actionBar._actionsArea.style.position = "absolute";
  actionBar._actionsArea.style.visibility = "hidden";
  actionsTable._getHeight = function() {
    if (!this._height) {
      this._height = O$.getElementSize(this).height;
    }
    return this._height;
  };
  actionBar.appendChild(actionsTable);

  actionBar._update = function() {
    actionBar._actionsArea._updatePos();
    for (var i = 0, count = actions.length; i < count; i++) {
      var action = actions[i];
      var cell = action._cell;
      cell._update();
    }
  };

  O$.setupHoverStateFunction(actionBar._actionsArea, function(mouseInside) {
    actionBar._actionsArea._mouseInside = mouseInside;
    if (actionBar._event)
      O$.invokeFunctionAfterDelay(actionBar._event._updateRolloverState, O$.EVENT_ROLLOVER_STATE_UPDATE_TIMEOUT);
  });


  actionBar._actionsArea._updatePos = function() {
    var dayTable = O$(dayTableId);
    var actionBarSize = O$.getElementSize(actionBar);
    var actionsAreaSize = O$.getElementSize(this);
    this.style.top = "0px";
    this.style.left = actionBarSize.width - actionsAreaSize.width + "px";
    this.style.height = actionBarSize.height + "px";

    O$.correctElementZIndex(this, dayTable);
  };
};

/*
 This function is invoked from the "delete event" action button. Don't invoke this function directly.
 */
O$._deleteCurrentTimetableEvent = function(event) {
  var e = O$.getEvent(event);
  var timetableEvent = e._timetableEvent;
  e._dayTable.deleteEvent(timetableEvent);
  e.returnValue = false;
};

O$._RangeMap = function() {
  this._disjointRanges = [];
};
O$._RangeMap.prototype._rangesIntersect = function(range1, range2) {
  // Returns true if the ranges intersect. End-point intersection is also not considered as an intersection.
  return range2.end >= range1.start && range2.start <= range1.end;
};
O$._RangeMap.prototype._rangeContainsRange = function(range1, range2) {
  // Returns true if range2 is entirely in range1. Equal ranges result in returning true as well.
  return range2.start >= range1.start && range2.end <= range1.end;
};
O$._RangeMap.prototype._mergeRanges = function(range1, range2) {
  if (!this._rangesIntersect(range1, range2))
    throw "An attempt to merge non-intersecting ranges";
  return {
    start: Math.min(range1.start, range2.start),
    end: Math.max(range1.end, range2.end)
  };
};
O$._RangeMap.prototype.addRange = function(start, end) {
  if (!start && !end) {
    this._infiniteRange = true;
    return;
  }
  if (start == end)
    return;
  if (start > end)
    throw "O$._RangeMap.prototype.addRange: start (" + start + ") can't be greater than end(" + end + ")";
  var addedRange = {start: start, end: end};
  var extendedRange = addedRange;
  for (var i = 0; i < this._disjointRanges.length;) {
    var range = this._disjointRanges[i];
    if (!this._rangesIntersect(range, addedRange)) {
      i++;
      continue;
    }
    if (!this._rangeContainsRange(addedRange, range)) {
      extendedRange = this._mergeRanges(extendedRange, range);
    }
    this._disjointRanges.splice(i, 1);
  }
  this._disjointRanges.push(extendedRange);
};
O$._RangeMap.prototype.isRangeFullyInMap = function(start, end) {
  if (this._infiniteRange)
    return true;
  var testedRange = {start: start, end: end};
  for (var i = 0, count = this._disjointRanges.length; i < count; i++) {
    var range = this._disjointRanges[i];
    if (this._rangeContainsRange(range, testedRange))
      return true;
  }
  return false;
};