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

O$.WeekTable = {
  _init: function(componentId,
                  day, locale, dateFormat, startTimeStr, endTimeStr, scrollTimeStr,
                  preloadedEventParams, resources, eventAreaSettings,
                  editable, onchange, editingOptions,
                  stylingParams,
                  uiEvent,
                  timePattern, timeSuffixPattern,
                  majorTimeInterval, minorTimeInterval, showTimeForMinorIntervals,
                  calendarOptions,
                  timetableId) {

    var weekTable = O$.initComponent(componentId, null, {
              _viewType: "week"
            });

    if (O$.isExplorer()) {
      if (!weekTable._initScheduled) {
        weekTable._initScheduled = true;
        var initArgs = arguments;
        // postpone initialization to avoid IE failure during page loading
        O$.addInternalLoadEvent(function() {
          O$.WeekTable._init.apply(null, initArgs);
        });
        return;
      }
    }
    weekTable.DEFAULT_EVENT_CLASS_NAME = "";
    weekTable.DEFAULT_EVENT_NAME_CLASS_NAME = "o_timetableEventName";
    weekTable.DEFAULT_EVENT_CONTENT = [ { type : "name"}, { type : "description" } ];
    weekTable.EVENT_ROLLOVER_STATE_UPDATE_TIMEOUT = 1;
    weekTable._timetable = O$(timetableId);

    O$.TimeScaleTable._init(componentId, day, locale, dateFormat, startTimeStr, endTimeStr, scrollTimeStr,
            preloadedEventParams, resources, eventAreaSettings, editable, onchange, editingOptions, stylingParams,
            uiEvent, timePattern, timeSuffixPattern, majorTimeInterval, minorTimeInterval, showTimeForMinorIntervals);


    var weekdayHeadersRowClass = O$.combineClassNames(["o_weekdayHeadersRow", stylingParams.weekdayHeadersRowClass]);

    var weekdayStyle = O$.combineClassNames(["o_weekdayText", stylingParams.weekdayClass]);
    var weekdayPattern = stylingParams.weekdayPattern ? stylingParams.weekdayPattern : "EEE MMM d";
    var weekdayHeadersRowSeparator = stylingParams.weekdayHeadersRowSeparator ? stylingParams.weekdayHeadersRowSeparator : "1px solid gray";
    var weekdayColumnSeparator = stylingParams.weekdayColumnSeparator ? stylingParams.weekdayColumnSeparator : "1px solid #c0c0c0";
    var weekdayHeadersTable = O$(componentId + "::weekdayHeaders");

    var firstDayOfWeek = (calendarOptions && calendarOptions.firstDayOfWeek) ? calendarOptions.firstDayOfWeek : 0;
    var columnsInWeekday = weekTable._useResourceSeparation ? resources.length : 1;

    weekTable._getColumnStyles = function() {
      var columns = [
        {className: this._timeColumnClass}
      ];
      var headerColumns = [
        {className: this._timeColumnClass}
      ];

      if (this._useResourceSeparation) {
        for (var resourceIndex = 0; resourceIndex < resources.length; resourceIndex++) {
          for (var weekDay = 0; weekDay < 7; weekDay++) {
            columns.push({});
            headerColumns.push({});
          }
        }
        headerColumns.push({className: "o_defaultScrollBarWidth"});
      } else {
        for (var weekDay = 0; weekDay < 7; weekDay++) {
          columns.push({});
        }
      }

      return {
        columns:columns,
        headerColumns:headerColumns
      };
    };

    weekTable._initTableCell = function(cell, cellIndex) {
      if (this._useResourceSeparation) {
        cell._resource = resources[(cellIndex - 1) % resources.length];
      }
      cell._weekday = Math.floor((cellIndex - 1) / columnsInWeekday);
      cell.onclick = function() {
        // onclick event can be fired on drag end under IE
        if (weekTable._draggingInProgress)
          return;
        if (editable) {
          var newEventTime = new Date(this._row._time.getTime() + 86400000 * this._weekday);
          weekTable._addEvent(newEventTime, this._resource ? this._resource.id : null);
        }
      };
    };

    weekTable._initStylesForTables();

    var weekdayHeaderColumns = [
      {
        className: weekTable._timeColumnClass
      }
    ];

    for (var weekDay = 0; weekDay < 7; weekDay++) {
      weekdayHeaderColumns.push({});
    }
    weekdayHeaderColumns.push({className: "o_defaultScrollBarWidth"});

    O$.Tables._init(weekdayHeadersTable, {
      columns: weekdayHeaderColumns,
      gridLines: [weekTable._primaryRowSeparator, weekdayColumnSeparator, null, null, null, null, null, null, null, null, null],
      body: {rowClassName: weekdayHeadersRowClass},
      forceUsingCellStyles: true,
      additionalParams: {}
    });
    weekdayHeadersTable.style.borderBottom = weekdayHeadersRowSeparator;

    weekTable._table.body._overrideVerticalGridline(0, weekTable._timeColumnSeparator);
    weekdayHeadersTable.body._overrideVerticalGridline(0, weekTable._timeColumnSeparator);
    weekdayHeadersTable.body._overrideVerticalGridline(weekdayHeaderColumns.length - 2, O$.isExplorer6() ? "1px solid white" : "1px solid transparent");

    for (var weekdayColumn = columnsInWeekday; weekdayColumn < weekTable._table._params.columns; weekdayColumn += columnsInWeekday) {
      weekTable._table.body._overrideVerticalGridline(weekdayColumn, weekdayColumnSeparator);
      if (weekTable._useResourceSeparation) {
        resourceHeadersTable.body._overrideVerticalGridline(weekdayColumn, weekdayColumnSeparator);
      }
    }

    var super_addEventElement = weekTable._addEventElement;
    O$.extend(weekTable, {
              _getNearestTimeslotForPosition: function(x, y) {
                var weekday = undefined;
                var row = this._table.body._rowFromPoint(10, y, true, this._getLayoutCache());
                if (!row) {
                  return null;
                }

                var result = this._getNearestTimeslotForPositionAndRow(x, y, row);
                weekday = result.cell._weekday;

                result.time = new Date(result.time.getTime() + weekday * 86400000);
                result.timeAtPosition = new Date(result.timeAtPosition.getTime() + weekday * 86400000);

                return result;
              },

              _getVertOffsetByTime: function(time, isEnd, isNextDay) {
                var hours = time.getHours();
                var minutes = time.getMinutes();

                if (!isEnd) {
                  var startTime = this._startTime;
                  var startHours = startTime.getHours();
                  var startMinutes = startTime.getMinutes();
                  if (hours < startHours || (hours == startHours && minutes < startMinutes)) {
                    hours -= 24;
                  }
                } else {
                  var endTime = this._endTime;
                  var endHours = endTime.getHours();
                  var endMinutes = endTime.getMinutes();
                  if (isNextDay) {
                    hours += 24;
                  } else {
                    if (endHours == 0 && endMinutes == 0) {
                      endHours = 24;
                    }
                    if (hours > endHours || (hours == endHours && minutes > endMinutes)) {
                      hours += 24;
                    }
                  }
                }
                return weekTable._getVertOffsetByHoursAndMinutes(hours, minutes);

              },

              _addEventElement: function(event, part) {
                var eventElement = super_addEventElement(event, part);

                O$.extend(eventElement, {
                          _getLeftColBoundaries: function(firstDataRow, resourceColIndex) {
                            var weekday = O$.WeekTable.getPartWeekday(weekTable, part);
                            var leftColIndex = weekday * columnsInWeekday + (resourceColIndex != undefined ? resourceColIndex : 1);
                            var leftColBoundaries = O$.getElementBorderRectangle(firstDataRow._cells[leftColIndex], true, weekTable._getLayoutCache());
                            return leftColBoundaries;
                          },

                          _getRightColBoundaries: function(firstDataRow, resourceColIndex) {
                            var weekday = O$.WeekTable.getPartWeekday(weekTable, part);
                            var rightColIndex = weekday * columnsInWeekday + (resourceColIndex != undefined ? resourceColIndex : columnsInWeekday);
                            var rightColBoundaries = O$.getElementBorderRectangle(firstDataRow._cells[rightColIndex], true, weekTable._getLayoutCache());
                            return rightColBoundaries;
                          },

                          _getTop: function() {
                            return weekTable._getVertOffsetByTime(part.start);
                          },

                          _getBottom: function() {
                            var isCrossDay = ! O$.WeekTable.isSameDay(part.start, part.end);
                            return weekTable._getVertOffsetByTime(part.end, true, isCrossDay);
                          }
                        });

                eventElement._update();
                return eventElement;
              },

              _updateStartEndTime: function() {
                var firstDay = O$.WeekTable.getDay(this._day, firstDayOfWeek);
                this._firstDay = firstDay;
                this._startTime = O$.parseTime(this._startTimeStr, O$.cloneDate(firstDay));
                this._endTime = O$.parseTime(this._endTimeStr, O$.incDay(O$.cloneDate(firstDay), 6));
              },

              _updateTimeForCells: function() {
                var rows = this._table.body._getRows();
                for (var rowIndex = 0, rowCount = rows.length; rowIndex < rowCount; rowIndex++) {
                  var row = rows[rowIndex];
                  row._updateTime(this._firstDay);
                }

                var weekdayHeaderRow = weekdayHeadersTable.body._getRows()[0];

                var cellDay = this._firstDay;
                for (var weekdayHeaderCellIndex = 1; weekdayHeaderCellIndex < weekdayHeaderRow._cells.length - 1; weekdayHeaderCellIndex++) {
                  var weekdayHeaderCell = weekdayHeaderRow._cells[weekdayHeaderCellIndex];
                  O$.removeAllChildNodes(weekdayHeaderCell);
                  var weekdayHeaderCellText = dtf.format(cellDay, weekdayPattern);
                  var weekdayHeaderCellStyledText = O$.createStyledText(weekdayHeaderCellText, weekdayStyle);
                  weekdayHeaderCell.appendChild(weekdayHeaderCellStyledText);
                  cellDay = O$.incDay(cellDay);
                }
              },

              previousWeek: function() {
                var prevDay = O$.incDay(this._day, -7);
                this.setDay(prevDay);
              },

              nextWeek: function() {
                var nextDay = O$.incDay(this._day, 7);
                this.setDay(nextDay);
              },

              currentWeek: function() {
                var today = new Date();
                this.setDay(today);
              },

              _accountForScrollerWidth: function() {
                var firstDataRow = this._table.body._getRows()[0];
                var visibleDataWidth = O$.getElementBorderRectangle(firstDataRow._cells[firstDataRow._cells.length - 1]).getMaxX() - O$.getElementPos(firstDataRow._cells[0]).x;

                var weekdayHeaderRow = weekdayHeadersTable.body._getRows()[0];
                var totalWeekdayHeaderWidth = O$.getElementBorderRectangle(weekdayHeaderRow._cells[weekdayHeaderRow._cells.length - 1]).getMaxX() - O$.getElementPos(weekdayHeaderRow._cells[0]).x;
                var lastHeaderColWidth = totalWeekdayHeaderWidth - visibleDataWidth;
                if (lastHeaderColWidth < 0) // can be the case under Mozilla3 because O$.getElementBorderRectangle returns non-rounded coordinates, which may result in values like -0.2499...
                  lastHeaderColWidth = 0;
                if (lastHeaderColWidth == 0 && O$.isOpera())
                  lastHeaderColWidth = 1;
                O$.setElementWidth(weekdayHeaderRow._cells[weekdayHeaderRow._cells.length - 1], lastHeaderColWidth);

                if (this._useResourceSeparation) {
                  var resourceHeaderRow = this._resourceHeadersTable.body._getRows()[0];
                  O$.setElementWidth(resourceHeaderRow._cells[resourceHeaderRow._cells.length - 1], lastHeaderColWidth);
                }
              }
            });

    var dtf = O$.getDateTimeFormatObject(locale);
    weekTable.setDay(dtf.parse(day, "dd/MM/yyyy"));


    weekTable._updateHeightForFF();

    weekTable._putTimetableChanges(null, null, null, true);
    O$.assignEvents(weekTable, {onchange: onchange}, true);

    O$.addInternalLoadEvent(function() {
       var scrollOffset = weekTable._getVertOffsetByTime(weekTable._scrollTime).y;
      var maxScrollOffset = weekTable._scroller.scrollHeight - O$.getElementSize(weekTable._scroller).height;
      if (maxScrollOffset < 0)
        maxScrollOffset = 0;
      if (scrollOffset > maxScrollOffset)
        scrollOffset = maxScrollOffset;
      weekTable._scroller.scrollTop = scrollOffset;
      O$.addEventHandler(weekTable._scroller, "scroll", function() {
        //TODO: move null to the end
        var timeslot = weekTable._getNearestTimeslotForPosition(10, weekTable._scroller.scrollTop);
        O$.setHiddenField(weekTable, weekTable.id + "::scrollPos", O$.formatTime(timeslot.timeAtPosition));
      });

      weekTable.updateLayout(); // update positions after layout changes that might have had place during loading
    });

     O$.addEventHandler(window, "resize", function() {
      weekTable.updateLayout();
    });
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

  getPartWeekday: function(weekTable, part) {
    var firstDay = weekTable._firstDay;
    var testDate = O$.cloneDate(firstDay);
    var eventDate = O$.dateByTimeMillis(part.start.getTime());

    for (var d = 0; d < 7; d++) {
      if (O$._datesEqual(eventDate, testDate)) {
        return d;
      }
      testDate = O$.incDay(testDate);
    }

    return null;
  },

  getDay: function(day, firstDayOfWeek) {
    var dayOfWeek = day.getDay();
    var decrement = dayOfWeek - firstDayOfWeek;
    if (decrement < 0) {
      decrement += 7;
    }
    var result = O$.incDay(day, -decrement);
    return result;
  },

  isSameDay: function(firstDate, secondDate) {
    var firstPureDate = O$.cloneDate(firstDate);
    var secondPureDate = O$.cloneDate(secondDate);
    var result = (firstPureDate.getTime() == secondPureDate.getTime());
    return result;
  }

};

