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

O$.DayTable = {
  _init: function(componentId,
                  day, locale, dateFormat, startTimeStr, endTimeStr, scrollTimeStr,
                  preloadedEventParams, resources, eventAreaSettings,
                  editable, onchange, editingOptions,
                  stylingParams,
                  uiEvent,
                  timePattern, timeSuffixPattern,
                  majorTimeInterval, minorTimeInterval, showTimeForMinorIntervals,
                  dummyParam,
                  timetableId) {

    var dayTable = O$.initComponent(componentId, null, {
              _viewType: "day"
            });

    if (O$.isExplorer()) {
      if (!dayTable._initScheduled) {
        dayTable._initScheduled = true;
        var initArgs = arguments;
        // postpone initialization to avoid IE failure during page loading
        O$.addInternalLoadEvent(function() {
          O$.DayTable._init.apply(null, initArgs);
        });
        return;
      }
    }

    dayTable.DEFAULT_EVENT_CLASS_NAME = "";
    dayTable.DEFAULT_EVENT_NAME_CLASS_NAME = "o_timetableEventName";
    dayTable.DEFAULT_EVENT_CONTENT = [
      { type : "name"},
      { type : "description" }
    ];
    dayTable.EVENT_ROLLOVER_STATE_UPDATE_TIMEOUT = 1;
    dayTable._timetable = O$(timetableId);

    O$.TimeScaleTable._init(componentId, day, locale, dateFormat, startTimeStr, endTimeStr, scrollTimeStr,
            preloadedEventParams, resources, eventAreaSettings, editable, onchange, editingOptions, stylingParams,
            uiEvent, timePattern, timeSuffixPattern, majorTimeInterval, minorTimeInterval, showTimeForMinorIntervals);


    dayTable._getColumnStyles = function() {
      var columns = [
        {className: this._timeColumnClass}
      ];
      var headerColumns = [
        {className: this._timeColumnClass}
      ];
      if (this._useResourceSeparation) {
        for (var resourceIndex = 0; resourceIndex < resources.length; resourceIndex++) {
          columns.push({});
          headerColumns.push({});
        }
        headerColumns.push({className: "o_defaultScrollBarWidth"});
      } else {
        columns.push({});
      }
      return {
        columns:columns,
        headerColumns:headerColumns
      };
    };

    dayTable._initTableCell = function(cell, cellIndex) {
      cell._resource = resources[cellIndex - 1];
      cell.onclick = function() {
        // onclick event can be fired on drag end under IE
        if (dayTable._draggingInProgress)
          return;
        if (editable)
          dayTable._addEvent(this._row._time, this._resource ? this._resource.id : null);
      };
    };

    dayTable._initStylesForTables();
    dayTable._table.body._overrideVerticalGridline(0, dayTable._timeColumnSeparator);

    var super_addEventElement = dayTable._addEventElement;
    O$.extend(dayTable, {
              _getNearestTimeslotForPosition: function(x, y) {
                var row = this._table.body._rowFromPoint(10, y, true, this._getLayoutCache());
                if (!row)
                  return {
                    resource: undefined,
                    time: y <= 0 ? this._startTime : this._endTime
                  };

                return this._getNearestTimeslotForPositionAndRow(x, y, row);
              },

              _getVertOffsetByTime: function(time) {
                var hours = time.getHours();
                var minutes = time.getMinutes();
                var thisTime = time.getTime();
                var startTime = this._startTime.getTime();
                if (thisTime < startTime)
                  hours -= 24;
                var endTime = this._endTime.getTime();
                if (thisTime == endTime && hours == 0)
                  hours = 24;
                if (thisTime > endTime)
                  hours += 24;
                return this._getVertOffsetByHoursAndMinutes(hours, minutes);
              },

              _addEventElement: function(event, part) {
                var eventElement = super_addEventElement(event, part);

                O$.extend(eventElement, {
                          _getLeftColBoundaries: function(firstDataRow, resourceColIndex) {
                            return O$.getElementBorderRectangle(firstDataRow._cells[resourceColIndex != undefined ? resourceColIndex : 1], true, dayTable._getLayoutCache());
                          },

                          _getRightColBoundaries: function(firstDataRow, resourceColIndex) {
                            return O$.getElementBorderRectangle(
                                    firstDataRow._cells[resourceColIndex != undefined ? resourceColIndex : dayTable._table._params.columns.length - 1],
                                    true, dayTable._getLayoutCache())
                          },

                          _getTop: function() {
                            return dayTable._getVertOffsetByTime(event.start);
                          },

                          _getBottom: function() {
                            return dayTable._getVertOffsetByTime(event.end);
                          }
                        });

                eventElement._update();
                return eventElement;
              },

              _updateStartEndTime: function() {
                this._startTime = O$.parseTime(this._startTimeStr, O$.cloneDate(this._day));
                this._endTime = O$.parseTime(this._endTimeStr, O$.cloneDate(this._day));
              },

              _updateTimeForCells: function() {
                var rows = this._table.body._getRows();
                for (var rowIndex = 0, rowCount = rows.length; rowIndex < rowCount; rowIndex++) {
                  var row = rows[rowIndex];
                  row._updateTime(this._day);
                }
              },

              previousDay: function() {
                var prevDay = O$.incDay(this._day, -1);
                this.setDay(prevDay);
              },

              nextDay: function() {
                var nextDay = O$.incDay(this._day, 1);
                this.setDay(nextDay);
              },

              today: function() {
                var today = new Date();
                this.setDay(today);
              },

              _accountForScrollerWidth: function() {
                if (!this._useResourceSeparation)
                  return;
                var firstHeaderRow = this._resourceHeadersTable.body._getRows()[0];
                var firstDataRow = this._table.body._getRows()[0];
                var visibleDataWidth = O$.getElementBorderRectangle(firstDataRow._cells[firstDataRow._cells.length - 1]).getMaxX() - O$.getElementPos(firstDataRow._cells[0]).x;
                var totalHeaderWidth = O$.getElementBorderRectangle(firstHeaderRow._cells[firstHeaderRow._cells.length - 1]).getMaxX() - O$.getElementPos(firstHeaderRow._cells[0]).x;
                var lastColWidth = totalHeaderWidth - visibleDataWidth;
                if (lastColWidth < 0) // can be the case under Mozilla3 because O$.getElementBorderRectangle returns non-rounded coordinates, which may result in values like -0.2499...
                  lastColWidth = 0;
                if (lastColWidth == 0 && O$.isOpera())
                  lastColWidth = 1;
                O$.setElementWidth(firstHeaderRow._cells[firstHeaderRow._cells.length - 1], lastColWidth);
              }
            });

    var dtf = O$.getDateTimeFormatObject(locale);
    dayTable.setDay(dtf.parse(day, "dd/MM/yyyy"));

    if (O$._documentLoaded) {
      // update in case when DayTable was loaded with Ajax
      setTimeout(function() {
        dayTable.updateLayout()
      }, 1);
    }


    dayTable._updateHeightForFF();

    dayTable._putTimetableChanges(null, null, null, true);
    O$.assignEvents(dayTable, {onchange: onchange}, true);

    O$.addInternalLoadEvent(function() {
      var scrollOffset = dayTable._getVertOffsetByTime(dayTable._scrollTime).y;
      var maxScrollOffset = dayTable._scroller.scrollHeight - O$.getElementSize(dayTable._scroller).height;
      if (maxScrollOffset < 0)
        maxScrollOffset = 0;
      if (scrollOffset > maxScrollOffset)
        scrollOffset = maxScrollOffset;
      dayTable._scroller.scrollTop = scrollOffset;
      O$.addEventHandler(dayTable._scroller, "scroll", function() {
        //TODO: move null to the end
        var timeslot = dayTable._getNearestTimeslotForPosition(10, dayTable._scroller.scrollTop);
        O$.setHiddenField(dayTable, dayTable.id + "::scrollPos", O$.formatTime(timeslot.timeAtPosition));
      });

      dayTable.updateLayout(); // update positions after layout changes that might have had place during loading
    });

    O$.addEventHandler(window, "resize", function() {
      dayTable.updateLayout();
    });

  }

};


