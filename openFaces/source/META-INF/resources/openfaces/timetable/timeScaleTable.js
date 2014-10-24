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

O$.TimeScaleTable = {
  RESOLVE_OVERLAPPING: false,

  _init: function(componentId,
                  day, locale, dateFormat, startTimeStr, endTimeStr, scrollTimeStr,
                  preloadedEventParams, resources, eventAreaSettings,
                  editable, onchange, editingOptions,
                  stylingParams,
                  uiEvent,
                  timePattern, timeSuffixPattern,
                  majorTimeInterval, minorTimeInterval, showTimeForMinorIntervals) {
    var timeScaleTable = O$(componentId);

    O$.TimeTableView._init(componentId, day, locale, dateFormat, preloadedEventParams, resources,
            eventAreaSettings, editable, onchange, editingOptions, stylingParams, uiEvent);

    if (!uiEvent)
      uiEvent = {};

    if (editingOptions.overlappedEventsAllowed === undefined) editingOptions.overlappedEventsAllowed = true;

    timeScaleTable._showTimeAgainstMark = stylingParams.timeTextPosition && stylingParams.timeTextPosition == "againstMark";
    var shortestEventTimeWhileResizing = 1000 * 60 * minorTimeInterval;

    var dateTimeFormat = O$.getDateTimeFormatObject(locale);
    if (!startTimeStr)
      startTimeStr = "00:00";
    timeScaleTable._startTimeStr = startTimeStr;
    if (!endTimeStr || endTimeStr == "00:00")
      endTimeStr = "24:00";
    timeScaleTable._endTimeStr = endTimeStr;
    var startTime = O$.parseTime(startTimeStr);
    timeScaleTable._startTime = startTime;
    var endTime = O$.parseTime(endTimeStr);
    timeScaleTable._endTime = endTime;
    if (!scrollTimeStr)
      scrollTimeStr = startTimeStr;
    var scrollTime = O$.parseTime(scrollTimeStr);
    timeScaleTable._scrollTime = scrollTime;
    var startTimeInMinutes = startTime.getHours() * 60 + startTime.getMinutes();
    timeScaleTable._startTimeInMinutes = startTimeInMinutes;
    var endTimeInMinutes = (endTime.getHours() == 0 ? 24 : endTime.getHours()) * 60 + endTime.getMinutes();
    timeScaleTable._endTimeInMinutes = endTimeInMinutes;

    var resourceHeadersRowClass = O$.combineClassNames(["o_resourceHeadersRow", stylingParams.resourceHeadersRowClass]);
    var timetableViewRowClass = O$.combineClassNames(["o_timetableViewRowClass", stylingParams.rowClass]);

    var timeColumnClass = O$.combineClassNames(["o_timeColumn", stylingParams.timeColumnClass]);
    timeScaleTable._timeColumnClass = timeColumnClass;
    var majorTimeStyle = O$.combineClassNames(["o_majorTimeText", stylingParams.majorTimeClass]);
    var minorTimeStyle = O$.combineClassNames(["o_minorTimeText", stylingParams.minorTimeClass]);
    var timeSuffixStyle = O$.combineClassNames(["o_timeSuffixText", stylingParams.timeSuffixClass]);

    var escapeEventNames = uiEvent.escapeName !== undefined ? uiEvent.escapeName : true;
    timeScaleTable._escapeEventNames = escapeEventNames;
    var escapeEventDescriptions = uiEvent.escapeDescription !== undefined ? uiEvent.escapeDescription : true;
    timeScaleTable._escapeEventDescriptions = escapeEventDescriptions;
    var escapeEventResources = uiEvent.escapeResource !== undefined ? uiEvent.escapeResource : true;
    timeScaleTable._escapeEventResources = escapeEventResources;

    var resourceHeadersRowSeparator = stylingParams.resourceHeadersRowSeparator ? stylingParams.resourceHeadersRowSeparator : "1px solid gray";
    var resourceColumnSeparator = stylingParams.resourceColumnSeparator ? stylingParams.resourceColumnSeparator : "1px dotted #b0b0b0";
    var timeColumnSeparator = stylingParams.timeColumnSeparator ? stylingParams.timeColumnSeparator : "2px solid gray";
    timeScaleTable._timeColumnSeparator = timeColumnSeparator;
    var primaryRowSeparator = stylingParams.primaryRowSeparator ? stylingParams.primaryRowSeparator : "1px solid #c4c4c4";
    timeScaleTable._primaryRowSeparator = primaryRowSeparator;
    var secondaryRowSeparator = stylingParams.secondaryRowSeparator ? stylingParams.secondaryRowSeparator : "1px solid #e8e8e8";
    var timeColumnPrimaryRowSeparator = stylingParams.timeColumnPrimaryRowSeparator ? stylingParams.timeColumnPrimaryRowSeparator : "1px solid #b0b0b0";
    var timeColumnSecondaryRowSeparator = stylingParams.timeColumnSecondaryRowSeparator ? stylingParams.timeColumnSecondaryRowSeparator : "1px solid #e4e4e4";

    var resourceHeadersTable = O$(componentId + "::resourceHeaders");
    timeScaleTable._resourceHeadersTable = resourceHeadersTable;

    if (timeScaleTable._useResourceSeparation) {
      timeScaleTable._resourcesByIds = {};
      timeScaleTable._idsByResourceNames = {};
      for (var resourceIndex = 0; resourceIndex < resources.length; resourceIndex++) {
        var resource = resources[resourceIndex];
        timeScaleTable._resourcesByIds[resource.id] = resource;
        timeScaleTable._idsByResourceNames[resource.name] = resource.id;
        resource._colIndex = resourceIndex + 1;
      }
    }

    var rowHeight = O$.getStyleClassProperty(timetableViewRowClass, "height");
    var duplicatedRows = timeScaleTable._showTimeAgainstMark;
    if (duplicatedRows && rowHeight) {
      var heightPx = O$.calculateNumericCSSValue(rowHeight);
      var additionalRowHeightClass = O$.createCssClass("height: " + Math.ceil(heightPx / 2) + "px ! important");
      timetableViewRowClass = O$.combineClassNames([timetableViewRowClass, additionalRowHeightClass]);
    }

    var forceUsingCellStyles = true; // allow such styles as text-align to be applied to row's cells

    timeScaleTable._initStylesForTables = function() {
      var columnStyles = this._getColumnStyles(timeColumnClass);
      var columns = columnStyles.columns;
      O$.Tables._init(this._table, {
                columns: columns,
                gridLines: [primaryRowSeparator, resourceColumnSeparator, null, null, null, null, null, null, null, null, null],
                body: {rowClassName: timetableViewRowClass},
                forceUsingCellStyles: forceUsingCellStyles,
                additionalParams: {}
              });

      var headerColumns = columnStyles.headerColumns;
      if (this._useResourceSeparation) {
        O$.Tables._init(resourceHeadersTable, {
                  columns: headerColumns,
                  gridLines: [primaryRowSeparator, resourceColumnSeparator, null, null, null, null, null, null, null, null, null],
                  body: {rowClassName: resourceHeadersRowClass},
                  rowStyles: {bodyRowClass: resourceHeadersRowClass},
                  forceUsingCellStyles: forceUsingCellStyles,
                  additionalParams: {}
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

      var intervalCount = (this._endTimeInMinutes - this._startTimeInMinutes) / minorTimeInterval;
      var roundedIntervalCount = Math.round(intervalCount);
      if (Math.abs(intervalCount - roundedIntervalCount) > 0) {
        // The entire time span from start time to end time should be divisible by a minor time interval.
        // The fallback here is to move end time to the nearest minor interval boundary before the specified end time.
        this._endTimeInMinutes = roundedIntervalCount * minorTimeInterval;
      }
      intervalCount = roundedIntervalCount;

      var now = new Date();
      var minutesPerRow = this._showTimeAgainstMark ? minorTimeInterval / 2 : minorTimeInterval;
      this._minutesPerRow = minutesPerRow;
      for (var timeInMinutes = this._startTimeInMinutes, intervalIndex = 0;
           timeInMinutes < this._endTimeInMinutes;
           timeInMinutes += minorTimeInterval,intervalIndex++) {
        var isMajorMark = (!this._showTimeAgainstMark ? intervalIndex : intervalIndex + 1) % minorPerMajorIntervals == 0;
        var row = this._table.body._createRow();
        var row2 = this._showTimeAgainstMark ? this._table.body._createRow() : null;
        if (row2) row2._row = row;
        row._intervalStartMinutes = timeInMinutes;
        if (row2) row2._intervalStartMinutes = timeInMinutes + minorTimeInterval / 2;
        row._updateTime = function(day) {
          this._time = O$.cloneDate(day);
          this._time.setHours(Math.floor(this._intervalStartMinutes / 60), this._intervalStartMinutes % 60, 0, 0);
        };
        if (row2) row2._updateTime = row._updateTime;
        row._updateTime(now);


        var timeHeader = this._showTimeAgainstMark ? row2.firstChild : row.firstChild;
        var headerCellSpan = 1;
        if (this._showTimeAgainstMark && intervalIndex != intervalCount - 1)
          headerCellSpan = 2;
        if (headerCellSpan != 1)
          timeHeader.rowSpan = headerCellSpan;
        if (intervalIndex == 0 && this._showTimeAgainstMark) {
          var emptyTimeHeader = row.firstChild;
          O$.appendClassNames(emptyTimeHeader, ["o_tinyText"]);
        }
        if (this._showTimeAgainstMark && intervalIndex == intervalCount - 1) {
          O$.appendClassNames(timeHeader, ["o_tinyText"]);
        }

        timeHeader._aHeaderCell = true;
        if (isMajorMark || showTimeForMinorIntervals) {
          if (this._showTimeAgainstMark)
            timeHeader.style.verticalAlign = "middle";
          var excludeTrailingMarkInscription = this._showTimeAgainstMark && headerCellSpan != 2;
          var markTime = row._time;
          if (this._showTimeAgainstMark) {
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
        this._table.body._addRows(newRows);

        var cells = row._cells;
        var cells2 = row2 ? row2._cells : null;
        for (var cellIndex = 1, cellCount = cells.length; cellIndex < cellCount; cellIndex++) {
          var cell = cells[cellIndex];
          var cell2 = cells2 ? cells2[cellIndex] : null;
          this._initTableCell(cell, cellIndex);

          if (cell2) {
            cell2._cell = cell;
            cell2.onclick = function() {
              this._cell.onclick();
            };
          }
        }

        if (this._showTimeAgainstMark && intervalIndex > 0)
          row.removeChild(row.firstChild);
      }

      this._table.body._getBorderBottomForCell = function(rowIndex, colIndex, cell) {
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

      if (this._useResourceSeparation) {
        resourceHeadersTable.body._overrideVerticalGridline(0, timeColumnSeparator);
        resourceHeadersTable.body._overrideVerticalGridline(headerColumns.length - 2, O$.isExplorer6() ? "1px solid white" : "1px solid transparent");
      }

    };

    timeScaleTable._getNearestTimeslotForPositionAndRow = function(x, y, row) {
      var firstRow = this._table.body._rowFromPoint(1, 1, true, this._getLayoutCache());
      var timeColumnCell = firstRow._cellFromPoint(1, 1, true, this._getLayoutCache());
      var minX = timeColumnCell.clientWidth;
      x = x < minX ? minX : x;
      var cell = row._cellFromPoint(x, y, true, this._getLayoutCache());
      if (cell == null) {
        //in case we are butt against cell border
        cell = row._cellFromPoint(x + 5, y, true, this._getLayoutCache());
      }
      if (cell == null) {
        //in case we are butt against cell border
        cell = row._cellFromPoint(x - 5, y, true, this._getLayoutCache());
      }
      var resource;

      if (cell._cell) {
        cell = cell._cell;
        row = cell._row;
      }
      var nextCell = cell.nextSibling;
      if (nextCell) {
        if (!cell._resource && nextCell._resource)
          cell = nextCell;
        else {
          var x1 = O$.getElementPos(cell, true, this._getLayoutCache()).x;
          var x2 = O$.getElementPos(nextCell, true, this._getLayoutCache()).x;
          cell = Math.abs(x - x1) < Math.abs(x - x2) ? cell : nextCell;
        }
      }
      resource = cell._resource;

      if (row._row)
        row = row._row;
      var time;
      var rows = this._table.body._getRows();
      var rowIncrement = this._showTimeAgainstMark ? 2 : 1;
      var nextRow = (row._index + rowIncrement < rows.length) ? rows[row._index + rowIncrement] : null;
      var timeAtPosition = new Date();
      if (!nextRow) {
        time = row._time;
        var rowRect = O$.getElementBorderRectangle(row, true, this._getLayoutCache());
        timeAtPosition.setTime(row._time.getTime() + this._minutesPerRow * 60000 * (y - rowRect.y) / rowRect.height);
      } else {
        var y1 = O$.getElementPos(row, true, this._getLayoutCache()).y;
        var y2 = O$.getElementPos(nextRow, true, this._getLayoutCache()).y;
        var nearestRow = Math.abs(y - y1) < Math.abs(y - y2) ? row : nextRow;
        time = nearestRow._time;
        timeAtPosition.setTime(row._time.getTime() + this._minutesPerRow * 60000 * (y - y1) / (y2 - y1));
      }


      return {resource: resource, time: time, timeAtPosition: timeAtPosition, cell: cell};
    };
    var eventResizeHandleHeight = O$.calculateNumericCSSValue("6px");

    var dragAndDropTransitionPeriod = stylingParams.dragAndDropTransitionPeriod !== undefined ? stylingParams.dragAndDropTransitionPeriod : 70;
    timeScaleTable._dragAndDropCancelingPeriod = stylingParams.dragAndDropCancelingPeriod !== undefined ? stylingParams.dragAndDropCancelingPeriod : 200;
    var undroppableStateTransitionPeriod = stylingParams.undroppableStateTransitionPeriod !== undefined ? stylingParams.undroppableStateTransitionPeriod : 250;
    var undroppableEventTransparency = stylingParams.undroppableEventTransparency !== undefined ? stylingParams.undroppableEventTransparency : 0.5;

    var eventsLeftOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(timeScaleTable._eventStyleClass, "marginLeft"));
    var eventsRightOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(timeScaleTable._eventStyleClass, "marginRight"));
    var reservedEventsLeftOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(timeScaleTable._reservedTimeEventClass, "marginLeft"));
    var reservedEventsRightOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(timeScaleTable._reservedTimeEventClass, "marginRight"));

    var addEvent = timeScaleTable._addEvent;
    var addEventElements = timeScaleTable._addEventElements;
    var addEventElement = timeScaleTable._addEventElement;

    O$.extend(timeScaleTable, {
              _getLayoutCache: function() {
                if (!timeScaleTable._cachedPositions)
                  timeScaleTable._cachedPositions = {};
                return timeScaleTable._cachedPositions;
              },

              _getScrollingCache: function() {
                if (!timeScaleTable._cachedScrollPositions)
                  timeScaleTable._cachedScrollPositions = {};
                return timeScaleTable._cachedScrollPositions;
              },

              _resetScrollingCache: function() {
                timeScaleTable._cachedScrollPositions = {};
              },

              _getEventEditor: function() {
                if (!editable)
                  return null;
                return this._eventEditor;
              },

              _getVertOffsetByHoursAndMinutes: function(hours, minutes) {
                var timeOffsetInMinutes = hours * 60 + minutes - startTimeInMinutes;
                //TODO: duplicatedRows
                //var minutesPerRow = duplicatedRows ? minorTimeInterval / 2 : minorTimeInterval;
                var minutesPerRow = this._minutesPerRow;
                var rowIndex = Math.floor(timeOffsetInMinutes / minutesPerRow);
                var relativePosInsideRow = (timeOffsetInMinutes % minutesPerRow) / minutesPerRow;
                var rows = this._table.body._getRows();
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
              },


              _updateEventElements: function(reacquireDayEvents, refreshAreasAfterReload) {
                if (!this._isActive()) return;
                this._baseZIndex = O$.getElementZIndex(this);
                this._removeEventElements();
                if (reacquireDayEvents)
                  this._events = this._eventProvider._getEventsForPeriod(this._startTime, this._endTime, function() {
                    this._updateEventElements(true, true);
                  });

                this._updateOverlappingPositions();

                var event;
                for (var eventIndex = 0, eventCount = this._events.length; eventIndex < eventCount; eventIndex++) {
                  event = this._events[eventIndex];
                  this._addEventElements(event);
                  if (refreshAreasAfterReload) {
                    event._attachAreas();
                  }

                }
                this._updateEventZIndexes();
              },


              _updateOverlappingPositions: function(callback) {
                if (O$.TimeScaleTable.RESOLVE_OVERLAPPING) {
                  if (timeScaleTable._useResourceSeparation) {

                    for (var resourceId in timeScaleTable._resourcesByIds) {
                      var eventsByResources = this._events.filter(
                              function(event) {
                                return event.resourceId !== undefined && event.resourceId == resourceId;
                              }).sort(O$.Timetable.compareEventsByStart);
                      this._updateOverlappingPositionsForEvents(eventsByResources, callback);
                    }
                    eventsByResources = this._events.filter(
                            function(event) {
                              return event.resourceId === undefined;
                            }).sort(O$.Timetable.compareEventsByStart);
                    this._updateOverlappingPositionsForEvents(eventsByResources, callback);
                  } else {
                    var events = this._events.slice();
                    events.sort(O$.Timetable.compareEventsByStart);
                    this._updateOverlappingPositionsForEvents(events, callback);
                  }
                }
              },

              /**
               * @param events events for specific resource sorted by start
               */
              _updateOverlappingPositionsForEvents: function(events, callback) {

                var updatedEvents = [];

                var positions = [];

                function reservePosition() {
                  var position = positions.length;
                  for (var i = 0; i < positions.length; i++) {
                    if (positions[i] != i) {
                      position = i;
                      break;
                    }
                  }
                  positions.push(position);
                  return position;
                }

                function releasePosition(position) {
                  for (var i = 0; i < positions.length; i++) {
                    if (positions[i] == position) {
                      positions = positions.slice(0, position).concat(positions.slice(position + 1));
                      break;
                    }
                  }
                }

                var divisor = 0;
                var pendingEvents = [];
                var openEvents = [];

                while (true) {

                  if (events.length == 0 && openEvents.length == 0) {
                    break;
                  }

                  while (events.length > 0 && (openEvents.length == 0 || events[0].start < openEvents[0].end)) {
                    var nextEvent = events.shift();
                    var positionForEvent;
                    if (nextEvent._dropAllowed === undefined || nextEvent._dropAllowed) {
                      positionForEvent = reservePosition();
                    } else {
                      positionForEvent = 0;
                    }
                    if (nextEvent._position === undefined || nextEvent._position != positionForEvent) {
                      nextEvent._position = positionForEvent;
                      updatedEvents.push(nextEvent);
                    }

                    divisor = divisor > positions.length ? divisor : positions.length;
                    openEvents.push(nextEvent);
                    openEvents.sort(O$.Timetable.compareEventsByEnd);
                  }

                  while (openEvents.length > 0 && (events.length == 0 || openEvents[0].end <= events[0].start)) {
                    var closingEvent = openEvents.shift();
                    pendingEvents.push(closingEvent);
                    if (closingEvent._dropAllowed === undefined || closingEvent._dropAllowed) {
                      releasePosition(closingEvent._position);
                    }

                    if (openEvents.length == 0) {
                      for (var i = 0; i < pendingEvents.length; i++) {
                        var pendingEvent = pendingEvents[i];
                        var divisorForEvent;
                        if (pendingEvent._dropAllowed === undefined || pendingEvent._dropAllowed) {
                          divisorForEvent = divisor;
                        } else {
                          divisorForEvent = 1;
                        }
                        if (pendingEvent._divisor === undefined || pendingEvent._divisor != divisorForEvent) {
                          pendingEvents[i]._divisor = divisorForEvent;
                          updatedEvents.push(pendingEvent);
                        }
                      }

                      divisor = 0;
                      pendingEvents = [];
                    }
                  }
                }
                if (callback !== undefined) {
                  for (i = 0; i < updatedEvents.length; i++) {
                    callback(updatedEvents[i]);
                  }
                }

              },

              _canEventBeDroppedHere: function(event) {
                var startTime = event.start.getTime();
                var endTime = event.end.getTime();
                var resourceId = event.resourceId;
                for (var i = 0, count = this._events.length; i < count; i++) {
                  var currEvent = this._events[i];
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
              },

              _addEvent: function(startTime, resourceId) {
                var event = addEvent(startTime, resourceId);
                this._addEventElements(event);
              },

              _updateEventsPresentation: function() {
                this._updateOverlappingPositions();
                for (var eventIndex = 0, eventCount = this._events.length; eventIndex < eventCount; eventIndex++) {
                  var event = this._events[eventIndex];
                  event._updatePresentation();
                }
              },

              _addEventElements: function(event) {
                addEventElements(event);

                O$.extend(event, {
                          _updatePresentation: event.updatePresentation,
                          updatePresentation: function(transitionPeriod) {
                            timeScaleTable._updateOverlappingPositions(function(updatedEvent) {
                              if (event !== updatedEvent) {
                                updatedEvent._updatePresentation(transitionPeriod);
                              }
                            });
                            event._updatePresentation(transitionPeriod)

                          },

                          _onresize: function(top) {
                            var draggableEventElement;
                            if (top) {
                              draggableEventElement = event.parts[0].mainElement;
                            } else {
                              draggableEventElement = event.parts[event.parts.length - 1].mainElement;
                            }

                            event._ondragend(draggableEventElement, top);
                          },

                          _ondragend: function(draggableEventElement, top) {
                            if (draggableEventElement._topResizeHandle) {
                              draggableEventElement._topResizeHandle.style.display = "";
                            }
                            if (draggableEventElement._bottomResizeHandle) {
                              draggableEventElement._bottomResizeHandle.style.display = "";
                            }
                            setTimeout(function() {
                              if (event._draggingInProgress) {
                                var draggingCanceled = false;
                                var dropAllowed = timeScaleTable._canEventBeDroppedHere(event);

                                if (!(top === undefined)) {
                                  if (top) {
                                    draggableEventElement = event.parts[0].mainElement;
                                  } else {
                                    draggableEventElement = event.parts[event.parts.length - 1].mainElement;
                                  }
                                }
                                if (!dropAllowed) {
                                  event.setStart(event._initialStart);//event._lastValidStart);
                                  event.setEnd(event._initialEnd);//event._lastValidEnd);
                                  event.resourceId = event._initialResourceId;//event._lastValidResourceId;
                                  draggingCanceled = true;
                                }

                                event._setDropAllowed(true);
                                if (!draggableEventElement) {
                                  return;
                                }
                                draggableEventElement.style.cursor = draggableEventElement._originalCursor;

                                event._draggingInProgress = undefined;
                                timeScaleTable._draggingInProgress = undefined;
                                draggableEventElement._draggingInProgress = undefined;
                                event._topResize = undefined;

                                if (event.start.getTime() >= timeScaleTable._endTime.getTime() ||
                                        event.end.getTime() <= timeScaleTable._startTime.getTime()) {
                                  timeScaleTable._updateEventElements(true);
                                } else {
                                  event.updatePresentation(dragAndDropTransitionPeriod);
                                  event._scrollIntoView();
                                }

                                if (!draggingCanceled) {
                                  timeScaleTable._putTimetableChanges(null, [event], null);
                                } else {
                                  event._setMouseInside(false);
                                }
                              }

                            }, 10);
                          },

                          _updateRolloverState: function() {
                            var mouseInsideEventElements = false;
                            for (var i = 0; i < event.parts.length; i++) {
                              var eventElement = event.parts[i].mainElement;
                              if (!eventElement) {
                                // this can be the case because _updateRolloverState is invoked by time-out, so if mouseOver/mouseOut happens
                                // just before element is replaced with Ajax, this call will be made when there's no original element anymore
                                return;
                              }
                              mouseInsideEventElements |= eventElement._mouseInside ||
                                      eventElement._topResizeHandle && eventElement._topResizeHandle._mouseInside ||
                                      eventElement._bottomResizeHandle && eventElement._bottomResizeHandle._mouseInside;

                            }

                            var actionBar = timeScaleTable._getEventActionBar();
                            event._setMouseInside(mouseInsideEventElements ||
                                    (actionBar._event == event && actionBar._actionsArea._mouseInside));
                          }

                        });
              },

              //TODO: move this to timetableScale because we don't need day spliting for month table
              //TODO: 2move this to weekTable because we don't need day spliting for day table
              _splitIntoParts: function(event) {
                var parts = [];
                var start = this._startTime < event.start ? event.start : this._startTime;
                var end = this._endTime < event.end ? this._endTime : event.end;
                var partStart = start;
                var partEnd = O$.incDay(new Date(partStart.getFullYear(), partStart.getMonth(), partStart.getDate()));
                var i = 0;
                do {
                  var part = {
                    start: partStart,
                    end: (partEnd < end) ? partEnd : end,
                    index: i++,
                    event: event
                  };
                  parts.push(part);
                  partStart = O$.cloneDate(partEnd);
                  partEnd = O$.incDay(O$.cloneDate(partEnd));
                } while (partStart < end);

                parts[0].first = true;
                parts[parts.length - 1].last = true;

                return parts;
              },
              _addEventElement: function(event, part) {
                var eventElement = addEventElement(event, part);

                O$.extend(eventElement, {
                          _updatePos: function(transitionPeriod, transitionEvents) {
                            var resourceColIndex;
                            if (event.resourceId) {
                              var resource = timeScaleTable._getResourceForEvent(event);
                              if (!resource) {
                                this.style.display = "none";
                                return;
                              }
                              resourceColIndex = resource._colIndex;
                            }
                            this.style.display = "";
                            var firstDataRow = timeScaleTable._table.body._getRows()[0];

                            var leftColBoundaries = this._getLeftColBoundaries(firstDataRow, resourceColIndex);
                            var rightColBoundaries = this._getRightColBoundaries(firstDataRow, resourceColIndex);
                            var top = this._getTop();
                            var bottom = this._getBottom();

                            var x1 = leftColBoundaries.getMinX() + (event.type != "reserved" ? eventsLeftOffset : reservedEventsLeftOffset);
                            var x2 = rightColBoundaries.getMaxX() - (event.type != "reserved" ? eventsRightOffset : reservedEventsRightOffset);
                            if (O$.isExplorer() && O$.isStrictMode() && (resourceColIndex === undefined || resourceColIndex == timeScaleTable._table._params.columns.length - 1)) {
                              var scroller = O$(timeScaleTable.id + "::scroller");
                              var scrollerWidth = scroller.offsetWidth - scroller.clientWidth;
                              x2 -= scrollerWidth;
                            }

                            var rect;
                            if (O$.TimeScaleTable.RESOLVE_OVERLAPPING) {

                              var intersectWidth = Math.round((x2 - x1) / event._divisor);
                              var intersectX = Math.round(Math.round(x1) + event._position * intersectWidth);

                              rect = new O$.Rectangle(intersectX, Math.round(top.y),
                                      intersectWidth, Math.round(bottom.y - top.y));

                            } else {

                              rect = new O$.Rectangle(Math.round(x1), Math.round(top.y),
                                      Math.round(x2 - x1), Math.round(bottom.y - top.y));
                            }
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
                                if (part.first || part.last) {
                                  event._updateAreaPositions(false);
                                }
                              }
                            };
                            if (transitionEvents)
                              events.oncomplete = transitionEvents.oncomplete;
                            this._lastRectangleTransition = O$.runTransitionEffect(this, ["rectangle"], [rect], transitionPeriod, 20, events);

                            if (eventElement._updateResizersPos)
                              eventElement._updateResizersPos();
                            var eventZIndex = O$.getNumericElementStyle(this, "z-index");
                            this._backgroundElement.style.zIndex = eventZIndex - 1;
                            if (bottom.bottomTruncated)
                              O$.appendClassNames(this, ["o_truncatedTimetableEvent"]);
                            else
                              O$.excludeClassNames(this, ["o_truncatedTimetableEvent"]);
                            event._updateAreaPositions(true);
                          },

                          _update: function(transitionPeriod) {
                            this._updatePos(transitionPeriod);
                            this._updateShape();
                          },

                          ondragend: function() {
                            event._ondragend(eventElement);
                          },

                          _setupDragAndDrop: function() {
                            O$.extend(event, {
                                      _updateDropAllowed: function() {
                                        var dropAllowed = timeScaleTable._canEventBeDroppedHere(event);
                                        if (dropAllowed) {
                                          event._lastValidStart = event.start;
                                          event._lastValidEnd = event.end;
                                          event._lastValidResourceId = event.resourceId;
                                        }
                                        event._setDropAllowed(dropAllowed);
                                      },

                                      _setDropAllowed: function(value) {
                                        if (this._dropAllowed == value)
                                          return;
                                        this._dropAllowed = value;
                                        for (var i = 0; i < event.parts.length; i++) {
                                          event.parts[i].mainElement._setDropAllowed(value);
                                        }
                                      },

                                      _getDraggablePartIndex: function() {
                                        if (!(event._topResize === undefined)) {
                                          if (event._topResize) {
                                            return 0;
                                          } else {
                                            return event.parts.length - 1;
                                          }
                                        }
                                        for (var i = 0; i < event.parts.length; i++) {
                                          part = event.parts[i];
                                          if (part.start < event._dragPositionTime && event._dragPositionTime < part.end) {
                                            return i;
                                          }
                                        }
                                      }

                                    });

                            var eventPreview = timeScaleTable._getEventPreview();

                            function hideExcessiveElementsWhileDragging() {
                              if (eventPreview)
                                setTimeout(function() {
                                  eventPreview.hide();
                                }, 100);
                            }

                            var containingBlock = O$.getContainingBlock(eventElement, true);
                            O$.extend(eventElement, {
                                      _setDropAllowed: function(value) {
                                        O$.runTransitionEffect(eventElement, ["opacity"], [value ? 1.0 : 1.0 - undroppableEventTransparency], undroppableStateTransitionPeriod, undefined, {
                                                  onupdate: function() {
                                                    if (this.propertyValues.opacity !== undefined)
                                                      O$.setOpacityLevel(eventElement._backgroundElement, this.propertyValues.opacity * (1 - timeScaleTable._eventBackgroundTransparency));
                                                  }
                                                });
                                      },

                                      onmousedown: function (e) {
                                        timeScaleTable._resetScrollingCache();
                                        eventElement._bringToFront();

                                        var pos = O$.getEventPoint(e, eventElement);
                                        event._dragPositionTop = pos.y;
                                        event._dragPositionTime = timeScaleTable._getNearestTimeslotForPosition(eventElement._rect.x, event._dragPositionTop).time;

                                        O$.startDragging(e, this);
                                        event._initialStart = event._lastValidStart = event.start;
                                        event._initialEnd = event._lastValidEnd = event.end;
                                        event._initialResourceId = event._lastValidResourceId = event.resourceId;
                                        eventElement._originalCursor = O$.getElementStyle(eventElement, "cursor");
                                        event._dropAllowed = true;
                                      },

                                      _getContainingBlock: function() {
                                        return containingBlock;
                                      },

                                      _getPositionTop: function() {
                                        return event._dragPositionTop;
                                      },

                                      /**
                                       * doesn't allows event to go beyond column bounds
                                       */
                                      _adjustTimeIncrement: function(timeIncrement) {
                                        var newPartStartTime = O$.dateByTimeMillis(part.start.getTime() + timeIncrement);
                                        var newPartEndTime = O$.dateByTimeMillis(part.end.getTime() + timeIncrement);

                                        var columnStartDate;
                                        var columnEndDate;
                                        //to make possible dnd between days
                                        var dayChangeTimeIncrement = 43200000; //12 hours
                                        if (part.start.getDate() != newPartStartTime.getDate() &&
                                                Math.abs(timeIncrement) > dayChangeTimeIncrement) {
                                          columnStartDate = newPartStartTime;
                                          columnEndDate = newPartEndTime;
                                        } else {
                                          columnStartDate = part.start;
                                          columnEndDate = part.end;
                                        }

                                        //TODO: introduce column constants
                                        var columnStartTime = new Date(columnStartDate.getFullYear(), columnStartDate.getMonth(), columnStartDate.getDate(), timeScaleTable._startTime.getHours(), timeScaleTable._startTime.getMinutes());
                                        columnStartTime = (columnStartTime.getTime() > timeScaleTable._startTime.getTime()) ? columnStartTime : timeScaleTable._startTime;
                                        var columnEndTime = new Date(columnEndDate.getFullYear(), columnEndDate.getMonth(), columnEndDate.getDate(), timeScaleTable._endTime.getHours(), timeScaleTable._endTime.getMinutes());
                                        columnEndTime = (columnEndTime.getTime() <= columnStartTime.getTime()) ? O$.incDay(columnEndTime) : columnEndTime;
                                        columnEndTime = (columnEndTime.getTime() < timeScaleTable._endTime.getTime()) ? columnEndTime : timeScaleTable._endTime;

                                        var minIntersection = 1800000; //30 minutes

                                        if (newPartEndTime.getTime() < columnStartTime.getTime() + minIntersection) {
                                          timeIncrement += columnStartTime.getTime() - newPartEndTime.getTime() + minIntersection;
                                        }
                                        if (newPartStartTime.getTime() > columnEndTime.getTime() - minIntersection) {
                                          timeIncrement -= newPartStartTime.getTime() - columnEndTime.getTime() + minIntersection;
                                        }

                                        return timeIncrement;
                                      },

                                      setPosition: function (left, top) {
                                        if (topResizeHandle)
                                          topResizeHandle.style.display = "none";
                                        if (bottomResizeHandle)
                                          bottomResizeHandle.style.display = "none";

                                        var rect = O$.getElementBorderRectangle(timeScaleTable._table, true);
                                        var maxTop = rect.height;
                                        var maxLeft = rect.width;
                                        left = left < 0 ? 0 : left > maxLeft ? maxLeft : left;
                                        top = top < 0 ? 0 : top > maxTop ? maxTop : top;

                                        var nearestTimeslot = timeScaleTable._getNearestTimeslotForPosition(left, top);
                                        var timeIncrement = nearestTimeslot.time.getTime() - event._dragPositionTime.getTime();

                                        var eventUpdated = false;
                                        if (timeIncrement != 0) {

                                          timeIncrement = this._adjustTimeIncrement(timeIncrement);
                                          event._dragPositionTime = O$.dateByTimeMillis(event._dragPositionTime.getTime() + timeIncrement);

                                          var newStartTime = O$.dateByTimeMillis(event.start.getTime() + timeIncrement);
                                          var newEndTime = O$.dateByTimeMillis(event.end.getTime() + timeIncrement);

                                          event.setStart(newStartTime);
                                          event.setEnd(newEndTime);

                                          eventUpdated = true;

                                        }

                                        var newResource = editingOptions.eventResourceEditable ? nearestTimeslot.resource : undefined;
                                        if (event.resourceId && newResource !== undefined) {
                                          if (event.resourceId != newResource.id) {
                                            event.resourceId = newResource.id;
                                            eventUpdated = true;
                                          }
                                        }
                                        if (eventUpdated) {
                                          event._updateDropAllowed();
                                          eventElement.style.cursor = "move";//eventElement._setDropAllowed ? "move" : this._originalCursor;
                                          if (!event._draggingInProgress) {
                                            event._draggingInProgress = true;
                                            eventElement._draggingInProgress = true;
                                            timeScaleTable._draggingInProgress = true;
                                            hideExcessiveElementsWhileDragging();
                                          }

                                          event._dragPositionTop -= eventElement._rect.y + eventElement._rect.height;

                                          if (timeIncrement < 0) {
                                            eventElement._elementPartIndex = part.index;
                                            eventElement._elementPartIndexFromTheStart = true;

                                          } else {
                                            eventElement._elementPartIndex = event.parts.length - part.index - 1;
                                            eventElement._elementPartIndexFromTheStart = false;
                                          }
                                          event.updatePresentation(dragAndDropTransitionPeriod);
                                          event._scrollIntoView();
                                          event._dragPositionTop += eventElement._rect.y + eventElement._rect.height;
                                        }
                                      },

                                      _updateResizersPos: function() {
                                        if (!event._draggingInProgress) {
                                          var eventRect = eventElement._rect;
                                          if (topResizeHandle)
                                            O$.setElementBorderRectangle(topResizeHandle, new O$.Rectangle(eventRect.x, eventRect.y - eventResizeHandleHeight / 2, eventRect.width, eventResizeHandleHeight));
                                          if (bottomResizeHandle)
                                            O$.setElementBorderRectangle(bottomResizeHandle, new O$.Rectangle(eventRect.x, eventRect.getMaxY() - eventResizeHandleHeight / 2, eventRect.width, eventResizeHandleHeight));
                                        }
                                        this._updateZIndex();
                                      },

                                      _updateZIndex: function(eventZIndex) {
                                        if (eventZIndex === undefined)
                                          eventZIndex = O$.getNumericElementStyle(eventElement, "z-index");
                                        if (topResizeHandle)
                                          topResizeHandle.style.zIndex = eventZIndex + 2;
                                        if (bottomResizeHandle)
                                          bottomResizeHandle.style.zIndex = eventZIndex + 2;
                                        if (part.first || part.last) {
                                          event._updateAreaZIndexes();
                                        }
                                      }
                                    });

                            if (editingOptions.eventDurationEditable) {

                              if (part.first || part.last) {

                                function setResizerHoverState(mouseInside, resizer) {
                                  resizer._mouseInside = mouseInside;
                                  O$.invokeFunctionAfterDelay(event._updateRolloverState, timeScaleTable.EVENT_ROLLOVER_STATE_UPDATE_TIMEOUT);
                                }

                                /**
                                 * @param topResize true for topResizeHandle, false for bottomResizeHandle
                                 */
                                function setResizerPosition(left, top, topResize) {
                                  var nearestTimeslot = timeScaleTable._getNearestTimeslotForPosition(left, top + eventResizeHandleHeight / 2, part);
                                  var eventUpdated = false;
                                  if (topResize) {
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
                                    event.updatePresentation(dragAndDropTransitionPeriod);
                                    event._scrollIntoView();
                                    event._updateDropAllowed();
                                  }
                                  if (eventUpdated && !event._draggingInProgress) {
                                    event._draggingInProgress = true;
                                    timeScaleTable._draggingInProgress = true;
                                    eventElement._draggingInProgress = true;
                                    event._topResize = topResize;
                                    hideExcessiveElementsWhileDragging();
                                  }

                                  if (part.mainElement) {
                                    eventElement._updateResizersPos();
                                  }
                                }

                                if (part.first) {
                                  var topResizeHandle = editingOptions.eventDurationEditable ? document.createElement("div") : null;
                                  topResizeHandle.style.fontSize = "0px";
                                  topResizeHandle.style.position = "absolute";
                                  topResizeHandle.style.cursor = "n-resize";
                                  O$.fixIEEventsForTransparentLayer(topResizeHandle);
                                  topResizeHandle.onclick = function(e) {
                                    O$.cancelEvent(e);
                                  };
                                  topResizeHandle.onmousedown = eventElement.onmousedown;
                                  topResizeHandle.ondragend = function() {
                                    event._onresize(true);
                                  };

                                  O$.setupHoverStateFunction(topResizeHandle, setResizerHoverState);
                                  eventElement._topResizeHandle = topResizeHandle;
                                  topResizeHandle.setPosition = function(left, top) {
                                    setResizerPosition(left, top, true);
                                  };
                                  timeScaleTable._absoluteElementsParentNode.appendChild(topResizeHandle);
                                }
                                if (part.last) {
                                  var bottomResizeHandle = editingOptions.eventDurationEditable ? document.createElement("div") : null;
                                  bottomResizeHandle.style.fontSize = "0px";
                                  bottomResizeHandle.style.position = "absolute";
                                  bottomResizeHandle.style.cursor = "s-resize";
                                  O$.fixIEEventsForTransparentLayer(bottomResizeHandle);
                                  bottomResizeHandle.onclick = function(e) {
                                    O$.cancelEvent(e);
                                  };
                                  bottomResizeHandle.onmousedown = eventElement.onmousedown;
                                  bottomResizeHandle.ondragend = function() {
                                    event._onresize(false);
                                  };

                                  O$.setupHoverStateFunction(bottomResizeHandle, setResizerHoverState);
                                  eventElement._bottomResizeHandle = bottomResizeHandle;
                                  bottomResizeHandle.setPosition = function(left, top) {
                                    setResizerPosition(left, top, false);
                                  };
                                  timeScaleTable._absoluteElementsParentNode.appendChild(bottomResizeHandle);
                                }

                              }
                            }

                            eventElement._removeNodes = function() {
                              if (topResizeHandle)
                                timeScaleTable._absoluteElementsParentNode.removeChild(topResizeHandle);
                              if (bottomResizeHandle)
                                timeScaleTable._absoluteElementsParentNode.removeChild(bottomResizeHandle);

                            };
                          },

                          _bringToFront: function() {
                            var index = timeScaleTable._eventElements.indexOf(eventElement);
                            O$.assert(index != -1, "eventElement._bringToFront. Can't find element in _eventElements array.");
                            timeScaleTable._eventElements.splice(index, 1);
                            timeScaleTable._eventElements.push(this);
                            timeScaleTable._updateEventZIndexes();
                          }

                        });

                if (event.type != "reserved" && editable) {
                  eventElement._setupDragAndDrop();
                }


                //TODO: move upper
                O$.extend(event, {
                          _updateAreaPositionsAndBorder: function() {
                            for (var i = 0; i < event.parts.length; i++) {
                              var eventElementI = event.parts[i].mainElement;
                              O$.setElementBorderRectangle(eventElementI, eventElementI._currentRect);
                            }
                            event._updateAreaPositions(true);
                          },

                          _isEventPreviewAllowed: function() {
                            return event._mouseInside && !event._draggingInProgress;
                          },

                          _isEventUpdateNotAllowed: function() {
                            return event.type == "reserved" || event._draggingInProgress;
                          },

                          _beforeUpdate: function() {
                          }

                        });

                return eventElement;

              },

              _layoutActionBar: function(actionBar, barHeight, eventElement) {
                var borderLeftWidth = O$.getNumericElementStyle(eventElement, "border-left-width");
                var borderRightWidth = O$.getNumericElementStyle(eventElement, "border-right-width");
                O$.setElementSize(actionBar, {width: eventElement._rect.width - borderLeftWidth - borderRightWidth,
                          height: barHeight});
                actionBar.style.top = "";
                actionBar.style.bottom = "";
                actionBar.style.left = "0px";
                actionBar.style.bottom = "0px";
              }

            });

    timeScaleTable._adjustRolloverPaddings();
  }
};

