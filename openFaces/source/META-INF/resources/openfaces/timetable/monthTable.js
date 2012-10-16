/*
 * OpenFaces - JSF Component Library 2.0
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

// ========== implementation

O$.MonthTable = {
  _init: function(componentId,
                  day, locale, dateFormat, scrollOffset,
                  preloadedEventParams, resources, eventAreaSettings,
                  editable, onchange, editingOptions,
                  stylingParams,
                  uiEvent,
                  calendarOptions,
                  timetableId) {

    var monthTable = O$.initComponent(componentId, null, {
              _viewType: "month"
            });

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

    monthTable.DEFAULT_EVENT_CLASS_NAME = "o_monthTableEvent";
    monthTable.DEFAULT_EVENT_NAME_CLASS_NAME = "o_monthTableEventName";
    monthTable.DEFAULT_EVENT_CONTENT = [
      {type: "time"},
      {type: "name"},
      {type: "resource"}
    ];
    monthTable.EVENT_ROLLOVER_STATE_UPDATE_TIMEOUT = 1;
    monthTable._timetable = O$(timetableId);

    O$.TimeTableView._init(componentId, day, locale, dateFormat, preloadedEventParams, resources,
            eventAreaSettings, editable, onchange, editingOptions, stylingParams, uiEvent);

    var moreLinkElementClass = O$.combineClassNames(["o_moreLinkElement", stylingParams.moreLinkElementClass]);
    var moreLinkClass = O$.combineClassNames(["o_moreLink", stylingParams.moreLinkClass]);
    var moreLinkText = stylingParams.moreLinkText ? stylingParams.moreLinkText : "More...";

    if (!uiEvent)
      uiEvent = {};

    var weekdayHeadersRowClass = O$.combineClassNames(["o_weekdayHeadersRow", stylingParams.weekdayHeadersRowClass]);
    var weekdayStyle = O$.combineClassNames(["o_weekdayText", stylingParams.weekdayClass]);
    var weekdayPattern = stylingParams.weekdayPattern ? stylingParams.weekdayPattern : "EEEE";


    monthTable._escapeEventNames = uiEvent.escapeName !== undefined ? uiEvent.escapeName : true;
    monthTable._escapeEventDescriptions = uiEvent.escapeDescription !== undefined ? uiEvent.escapeDescription : true;
    monthTable._escapeEventResources = uiEvent.escapeResource !== undefined ? uiEvent.escapeResource : true;

    var weekdayHeadersRowSeparator = stylingParams.weekdayHeadersRowSeparator ? stylingParams.weekdayHeadersRowSeparator : "1px solid gray";
    var weekdayColumnSeparator = stylingParams.weekdayColumnSeparator ? stylingParams.weekdayColumnSeparator : "1px solid silver";
    var rowSeparator = stylingParams.rowSeparator ? stylingParams.rowSeparator : "1px solid #b0b0b0";
    var secondaryRowSeparator = stylingParams.secondaryRowSeparator ? stylingParams.secondaryRowSeparator : "1px solid #e8e8e8";
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


    //TODO: add initializing of dayView styles
    setTimeout(function(){
      monthTable._expandedDayView = O$(componentId + "::expandedDayView");
      monthTable._expandedDayView.eventBlock = O$(componentId + "::expandedDayView::eventBlock");
      monthTable._expandedDayView.opened = false;
      //monthTable._expandedDayView.defaultRect = O$.getElementBorderRectangle(monthTable._expandedDayView, true);
      O$.extend(monthTable._expandedDayView, {
        scrollPos : 0,
        allEventHeight : 0,
        _scrollContent: function (delta){
          if (Math.abs(this.scrollPos) + O$.getElementSize(this).height + delta >  this.allEventHeight) return;
          this.scrollPos += delta;
          for (var i = 0; i<monthTable._expandedDayView.eventBlock.childNodes.length;i++){
            var position = O$.getNumericElementStyle(monthTable._expandedDayView.eventBlock.childNodes[i], "top");
            monthTable._expandedDayView.eventBlock.childNodes[i].style.top = (position + delta)+"px";
          }
        },
        _expandDayView: function (dayCell){
          monthTable._expandedDayView.style.display = "";
          var cellBoundaries = O$.getElementBorderRectangle(dayCell, true);
          // O$.setElementSize(monthTable._expandedDayView,{width: cellBoundaries.width, height: 200});
          O$.setElementPos(this, cellBoundaries);
          var oldExpandedDay = this.expandedDay;
          this.expandedDay = dayCell._cellDay;
          if (oldExpandedDay)
            monthTable._updateCellEventElements(oldExpandedDay);
          monthTable._updateCellEventElements(dayCell._cellDay);
          this.opened = true;
          var rect = O$.getElementBorderRectangle(this, true).clone();
          console.log("IMPORTANT!!!!!!!!!!!!!" + rect.toString());
          O$.setElementSize(this, {height:10});
          this._lastRectangleTransition = O$.runTransitionEffect(monthTable._expandedDayView, ["rectangle"], [rect], 200, 20, null);
        },
        _contractDayView : function (){
          this.style.display = "none";
          var oldExpandedDay = monthTable._expandedDayView.expandedDay;
          this.expandedDay = null;
          monthTable._updateCellEventElements(oldExpandedDay);
          this.opened = false;
        }
    });



      //TODO: temp soultion in future neeed to erase this collection
      monthTable._expandedDayView.parts = [];
    }, 200);




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

    //TODO: add activation of ExpandedDayView

    var eventElementHeight = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._eventStyleClass, "height"));
    var eventsLeftOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._eventStyleClass, "marginLeft"));
    var eventsRightOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._eventStyleClass, "marginRight"));
    var reservedEventsLeftOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._reservedTimeEventClass, "marginLeft"));
    var reservedEventsRightOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._reservedTimeEventClass, "marginRight"));
    var moreLinkElementHeight = O$.calculateNumericCSSValue(O$.getStyleClassProperty(moreLinkElementClass, "height"));

    var weekdayHeadersTable = O$(componentId + "::weekdayHeaders");

    var firstDayOfWeek = (calendarOptions && calendarOptions.firstDayOfWeek) ? calendarOptions.firstDayOfWeek : 0;

    var columns = [];
    var weekdayHeaderColumns = [];

    for (var weekDay = 0; weekDay < 7; weekDay++) {
      columns.push({});
      weekdayHeaderColumns.push({});
    }
    weekdayHeaderColumns.push({className: "o_defaultScrollBarWidth"});

    if (monthTable._useResourceSeparation) {
      monthTable._resourcesByIds = {};
      monthTable._idsByResourceNames = {};
      resources.forEach(function(resource) {
        monthTable._resourcesByIds[resource.id] = resource;
        monthTable._idsByResourceNames[resource.name] = resource.id;
      });
    }

    var forceUsingCellStyles = true; // allow such styles as text-align to be applied to row's cells

    O$.Tables._init(monthTable._table, {
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
      monthTable._table.body._newRow().className = dayHeaderRowClass; // day header row
      monthTable._table.body._newRow().className = timetableViewRowClass; // day content row
    }

    var rows = monthTable._table.body._getRows();
    rows.forEach(function(row) {
      var cells = row._cells;
      for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
        var cell = cells[cellIndex];
        if (!cell.reservedPlaces) {
          cell.reservedPlaces = [];
        }
        cell.onclick = function() {
          // onclick event can be fired on drag end under IE
          if (monthTable._expandedDayView.opened)
            monthTable._expandedDayView._contractDayView();
          else if (editable) {
            var newEventTime = this._cellDay;
            var event = monthTable._addEvent(newEventTime, this._resource ? this._resource.id : null);
            event._cell = this;
            monthTable._addEventElements(event);
          }
        };
      }
      cells[cells.length - 1]._last = true;
    });

    // workaround to show *horizontal* gridlines
    monthTable._table.body._overrideVerticalGridline(0, columnSeparator);

    var rowCount = monthTable._table.body._getRows().length;
    monthTable._table.body._getBorderBottomForCell = function(rowIndex, colIndex, cell) {
      if (rowIndex == rowCount - 1) return "none";
      if (rowIndex % 2 == 0)
        return secondaryRowSeparator;
      else
        return rowSeparator;
    };

    monthTable._table.body._updateVerticalGridlines();

    function clearAllCellEvents() {
      var rows = monthTable._table.body._getRows();
      for (var rowIndex = 1; rowIndex < rows.length; rowIndex += 2) {
        var row = rows[rowIndex];
        var cells = row._cells;
        for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
          var cell = cells[cellIndex];
          if (cell._cellEvents) {
            cell._cellEvents.forEach(function(oldCellEvent) {
              oldCellEvent._removeElements();
            });
          }
          cell._cellEvents = [];
        }
      }
    }


    monthTable._updateCellEventElements = function(day) {
      var cell = O$.MonthTable.getCellForDay(this, day);

      if (!cell) {
        return;
      }

      if (cell._cellEvents) {
        cell._cellEvents.forEach(function(oldCellEvent) {
          if (oldCellEvent._removeElements)
            oldCellEvent._removeElements();
        });
      }

      cell._cellEvents = [];

      var cellEvents = O$.MonthTable.getDayEvents(this._events, day);

      function getEventDaysDuration(event){

        return O$.getDayInterval(event.end, event.start)
      }

      //sorting events by theirs day interval TODO: доделать нормально
      for (var i = 0; i<cellEvents.length; i++){

        var max = i;
        for (var j = i+1; j<cellEvents.length; j++){
          if (getEventDaysDuration(cellEvents[max]) < getEventDaysDuration(cellEvents[j]))
            max = j;
        }
        var tmp = cellEvents[max];
        cellEvents[max] = cellEvents[i];
        cellEvents[i] = tmp
      }

      for (var cellEventIndex = 0; cellEventIndex < cellEvents.length; cellEventIndex++) {
        var cellEvent = cellEvents[cellEventIndex];
        cellEvent._cell = cell;
        cellEvent._cellEventIndex = cellEventIndex;
        cell._cellEvents.push(cellEvent);
      }


      cell._moreLinkData = null;

      cellEvents.forEach(function(cellEvent) {
        monthTable._addEventElements(cellEvent);
      });
      //monthTable._reCalcCells();
      cell._moreLinkElement._update();
      cell._moreLinkData = null;

    };

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

      link.onclick = function(e) {
        O$.stopEvent(e);
        monthTable._expandedDayView._expandDayView(cell);
      }
      //link.onmousedown = link.onclick;

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
      };

      monthTable._absoluteElementsParentNode.appendChild(moreLinkElement);
      return moreLinkElement;
    }

    for (var rowIndex = 1; rowIndex < rows.length; rowIndex += 2) {
      var row = rows[rowIndex];
      var cells = row._cells;
      for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
        var cell = cells[cellIndex];
        addMoreLink(cell);
      }
    }

    var super_addEventElement = monthTable._addEventElement;
    var super_removeEventElement = monthTable._removeEventElement;
    var super_removeEventElements = monthTable._removeEventElements;
    O$.extend(monthTable, {
              scrollContent: function(delta){
                this._expandedDayView._scrollContent(delta);
              },

              _checkDayInEvent: function(day,event){
                var incDay = O$.incDay(day,1);
                // TODO: varify if method correct
                /*console.log("event.start > day && event.start < incDay = " + (event.start > day && event.start < incDay));
                console.log("event.end > day && event.end < incDay = " + (event.end > day && event.end < incDay));
                console.log("day>event.start && day<event.end = " + (day>event.start && day<event.end));*/
                if ((event.start > day && event.start < incDay) ||
                    (event.end > day && event.end < incDay) ||
                    (day>event.start && day<event.end) ||
                    (incDay>event.start && incDay<event.end)) return true;
                return false;
              },
              //TODO: realization of splitting to parts for month table
              _splitIntoParts: function(event) {
                var parts = [];

                var start = this._startTime < event.start ? event.start : this._startTime;
                var end = this._endTime < event.end ? this._endTime : event.end;
                var partStart = start;
                var partEnd = O$.incDay(new Date(partStart.getFullYear(), partStart.getMonth(), partStart.getDate()),7 - partStart.getDay());
                var i = 0;

                do {
                  var part = {
                    start: partStart,
                    end: (partEnd < end) ? partEnd : end,
                    index: i++,
                    event: event,
                    expandedPart: false
                  };
                  parts.push(part);
                  partStart = O$.cloneDate(partEnd);

                  // adding delta to day need to end till the week end      \
                  partEnd = O$.incDay(O$.cloneDate(partEnd), 7);
                } while (partStart < end);


                if (monthTable._expandedDayView.expandedDay){
                  if (this._checkDayInEvent(monthTable._expandedDayView.expandedDay,event)){
                    var part = {
                      start: monthTable._expandedDayView.expandedDay,
                      end: monthTable._expandedDayView.expandedDay,
                      index: i++,
                      event: event,
                      expandedPart: true
                    };
                    parts.push(part);
                    //TODO:  temp solution in future need to erase this collection
                    monthTable._expandedDayView.parts.push(part);
                  }
                }

                parts[0].first = true;
                parts[parts.length - 1].last = true;
                return parts;
              },

              _getNearestTimeslotForPosition: function(x, y) {
                row = this._table.body._rowFromPoint(10, y, true, this._getLayoutCache());
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
                if (cell._cell) {
                  cell = cell._cell;
                  row = cell._row;
                }
                //TODO: do not return JSON object just for one value
                return { cell: cell};
              },

              _getLayoutCache: function() {
                if (!this._cachedPositions)
                  this._cachedPositions = {};
                return this._cachedPositions;
              },

              _getEventEditor: function() {
                if (!editable)
                  return null;
                return this._eventEditor;
              },

              _cellsToReCalc : [],

              _addCellForReCalc : function(cell){
                if (!O$.contains(this._cellsToReCalc, cell)){
                  this._cellsToReCalc.push(cell);
                }
              },

              _passedEvent : [],

              //TODO: maybe remove this method because it was added to avoid double event creation when parts wasn't correctly supported
              _addPassedEvent : function(event){
                if (!O$.contains(this._passedEvent, event)){
                  this._passedEvent.push(event);
                }
              },

              //TODO: maybe remove this method because it was added to avoid double event creation when parts wasn't correctly supported
              _validateEvent : function(event){
                return !O$.contains(this._passedEvent, event);
              },

              _reCalcCells : function(){
                if (this._cellsToReCalc.length>0){
                  monthTable._updateCellEventElements(this._cellsToReCalc[0]._cellDay);
                  this._cellsToReCalc.splice (0, 1);
                }
              },

              _addEventElement: function(event, part) {
                var eventElement = super_addEventElement.call(this, event, part);

//######################################################################################################################
                O$.extend(event, {
                  _getDraggablePartIndex: function() {
                    for (var i = 0; i < event.parts.length; i++) {
                      part = event.parts[i];
                      //TODO: rename this attribute and add some stuff
                      var rightBorder = O$.incDay(event._dragPositionTime,0.51);
                      //TODO: FINISH THIS
                      if ((part.start < event._dragPositionTime && event._dragPositionTime < part.end )||
                          (part.start < rightBorder && rightBorder < part.end ) ){
                        return i;
                      }
                    }
                  },

                  _setupDragAndDrop: function() {
                    var eventPreview = monthTable._getEventPreview();

                    function hideExcessiveElementsWhileDragging() {
                      if (eventPreview)
                        setTimeout(function() {
                          eventPreview.hide();
                      }, 100);
                    }
                    var containingBlock = O$.getContainingBlock(eventElement, true);
                    O$.extend(eventElement, {
                      onmousedown: function (e) {
                        //timeScaleTable._resetScrollingCache();

                       // eventElement._bringToFront();

                        var pos = O$.getEventPoint(e, eventElement);
                        event._dragPositionTop = pos.y;
                        event._dragPositionTime = monthTable._getNearestTimeslotForPosition(eventElement._rect.x, event._dragPositionTop).cell._cellDay;

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

                      setPosition: function (left, top) {
                        var rect = O$.getElementBorderRectangle(monthTable._table, true);
                        var maxTop = rect.height;
                        var maxLeft = rect.width;
                        left = left < 0 ? 0 : left > maxLeft ? maxLeft : left;
                        top = top < 0 ? 0 : top > maxTop ? maxTop : top;

                        var nearestTimeslot = monthTable._getNearestTimeslotForPosition(left, top);
                        var timeIncrement = nearestTimeslot.cell._cellDay.getTime() - event._dragPositionTime.getTime();

                        var eventUpdated = false;
                        if (timeIncrement != 0) {
                          // TODO: here in others tables we adjusting time so make sure that this correct without adjusting
                          event._dragPositionTime = O$.dateByTimeMillis(event._dragPositionTime.getTime() + timeIncrement);

                          var newStartTime = O$.dateByTimeMillis(event.start.getTime() + timeIncrement);
                          var newEndTime = O$.dateByTimeMillis(event.end.getTime() + timeIncrement);


                          event.setStart(newStartTime);
                          event.setEnd(newEndTime);

                          eventUpdated = true;
                        }

                        if (eventUpdated) {
                          eventElement.style.cursor = "move";
                          if (!event._draggingInProgress) {
                            event._draggingInProgress = true;
                            eventElement._draggingInProgress = true;
                            monthTable._draggingInProgress = true;
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
                          event.updatePresentation(70);
                          event._scrollIntoView();
                          event._dragPositionTop += eventElement._rect.y + eventElement._rect.height;
                        }
                      }
                    });
                  }
                });
//######################################################################################################################
                event._setupDragAndDrop();
                event._updateRolloverState = function() {

                  var mouseInsideEventElements = false;

                  for (var i = 0; i < event.parts.length; i++) {
                    var eventElement = event.parts[i].mainElement;
                    if (!eventElement) {
                      // this can be the case because _updateRolloverState is invoked by time-out, so if mouseOver/mouseOut happens
                      // just before element is replaced with Ajax, this call will be made when there's no original element anymore
                      return;
                    }
                    mouseInsideEventElements |= eventElement._mouseInside;
                  }

                  var actionBar = monthTable._getEventActionBar();
                  event._setMouseInside(mouseInsideEventElements
                          || (actionBar._event == event && actionBar._actionsArea._mouseInside)
                  );
                };

                eventElement._update = function() {
                  this._updatePos();
                  this._updateShape();
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
                  if (event._cell._cellDay.getDate() != part.start.getDate){
                    event._cell = O$.MonthTable.getCellForDay(monthTable, part.start);
                  }



                  var cell = O$.MonthTable.getCellForDay(monthTable,part.start);
                  var cellEventIndex = event._cellEventIndex;
                  //TODO: temporary we don't have expanded view  if (cell._cellDay != monthTable._expandedDayView.expandedDay){
                  if (part.expandedPart){
                    var changeEventParent = function(newParent){
                      newParent.appendChild(this);
                      newParent.appendChild(this._backgroundElement);
                    }
                    changeEventParent.call(this, monthTable._expandedDayView.eventBlock);
                    var cellBoundaries = O$.getElementBorderRectangle(monthTable._expandedDayView.eventBlock, true);
                    var topY = 6 + eventElementHeight * cellEventIndex;
                    var bottomY = topY + eventElementHeight;

                    monthTable._expandedDayView.allEventHeight = topY + eventElementHeight;

                    // TODO: maybe remove this vaiables
                    var rightBorderOverflow =  event.end.getDate() > part.end.getDate();
                    var leftBorderOverflow = event.start.getDate() < part.start.getDate();

                    var x1 = 0 + (event.type != "reserved" ? eventsLeftOffset : reservedEventsLeftOffset);
                    if (leftBorderOverflow){
                      x1 -= 50;
                    }
                    var x2 = cellBoundaries.getMaxX() - cellBoundaries.getMinX() - (event.type != "reserved" ? eventsRightOffset : reservedEventsRightOffset);
                    if (rightBorderOverflow){
                      x2 += 50;
                    }

                    var rect = new O$.Rectangle(Math.round(x1), Math.round(topY),
                            Math.round(x2 - x1), Math.round(bottomY - topY));
                    this._rect = rect;
                  }
                  if (!part.expandedPart){
                    //todo: remove this all it's just to remember fields name
                    // cell._row
                    /* row._cells;
                     for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
                    * */
                    var endDayCell = O$.MonthTable.getCellForDay(monthTable,part.end);

                    var endDayCellBoundaries = O$.getElementBorderRectangle(endDayCell, true);
                    var startDayCellBoundaries = O$.getElementBorderRectangle(cell, true);
                    //todo: temporary for future check here if we need some hidden parts for event layout
                    //if (!monthTable._expandedDayView || cell._cellDay != monthTable._expandedDayView.expandedDay) {
                    if (!part.expandedPart) {
                      // If we have multi cell event we need to copy event to several lines
                      //todo: maybe put this dirrectly to if

                      var rightBorderOverflow =  part.index < (part.event.parts.length - 1 - (part.event.parts[part.event.parts.length-1].expandedPart ? 1 : 0 ));
                      var leftBorderOverflow = part.index > 0;


                      endDayCell =  cell._row._cells[cell._row._cells.length-1];

                      //TODO: make refactoring of calculating x1,x2 right here
                      var rightDelta = 0;
                      var leftDelta = 0;
                      if (rightBorderOverflow)
                        rightDelta = 1;
                      if (leftBorderOverflow)
                        leftDelta = 1;
                      var x1 = startDayCellBoundaries.getMinX() + (event.type != "reserved" ? eventsLeftOffset : reservedEventsLeftOffset);
                      var x2 = - (event.type != "reserved" ? eventsRightOffset : reservedEventsRightOffset);
                      var cellWidth = startDayCellBoundaries.getMaxX() - startDayCellBoundaries.getMinX();

                      if (leftBorderOverflow){
                        x1 -= cellWidth;
                      }
                      if (rightBorderOverflow){
                        x2 += startDayCellBoundaries.getMaxX() + cellWidth*(7-part.start.getDay());
                      }else{
                        x2 += endDayCellBoundaries.getMaxX();
                      }
                    }

                    var placeIndex = cell.reservedPlaces.length;
                    for (i=0; i<cell.reservedPlaces.length; i++){
                      if (!cell.reservedPlaces[i] || cell.reservedPlaces[i]==event){
                        placeIndex = i;
                        break;
                      }
                    };


                    var topY = startDayCellBoundaries.getMinY() + eventElementHeight * placeIndex;
                    var bottomY = topY + eventElementHeight;
                    var lastForCell = (cellEventIndex == cell._cellEvents.length - 1);
                    var maxY = lastForCell ? startDayCellBoundaries.getMaxY() : startDayCellBoundaries.getMaxY() - moreLinkElementHeight;
                    if (cell._moreLinkData ) {
                      this.style.display = "none";
                      return;
                    }
                    if (bottomY > maxY ) {
                      //TODO: temporary commented need to uncomment for showing of more link
                      cell._moreLinkData = { topY: topY };
                      this.style.display = "none";
                      return;
                    }

                    // проверяем если ивент на 2+ дня резервируем ему место на след ячейки
                    if (!O$._datesEqual(part.end, part.start) /* && monthTable._validateEvent(event)*/){
                      var nextDayCell
                      for (day = O$.incDay(part.start,1); !O$._datesEqual(day, O$.incDay(part.end,1)); day=O$.incDay(day,1)){
                        nextDayCell = O$.MonthTable.getCellForDay(monthTable,day);
                        monthTable._addCellForReCalc(nextDayCell);
                        nextDayCell.reservedPlaces[placeIndex] = event;
                      }
                    }


                    if (O$.isExplorer() && O$.isStrictMode() && cell._last) {
                      var scroller = O$(monthTable.id + "::scroller");
                      var scrollerWidth = scroller.offsetWidth - scroller.clientWidth;
                      x2 -= scrollerWidth;
                    }
                    var rect = new O$.Rectangle(Math.round(x1), Math.round(topY),
                            Math.round(x2 - x1), Math.round(bottomY - topY));
                    cell.reservedPlaces[placeIndex] =  event;
                    this._rect = rect;
                  };
                  var backgroundElement = this._backgroundElement;
                  O$.setElementBorderRectangle(eventElement, eventElement._rect);
                  O$.setElementBorderRectangle(backgroundElement, eventElement._rect);
                };

                //TODO: move upper
                event._updateAreaPositionsAndBorder = function() {
                  event._updateAreaPositions(true);
                };

                event._isEventPreviewAllowed = function() {
                  return event._mouseInside;
                };

                event._isEventUpdateNotAllowed = function() {
                  return event.type == "reserved";
                };

                event._beforeUpdate = function() {
                  event._oldStart = event.start;
                };

                eventElement._update();
                return eventElement;
              },

              _removeEventElement: function(event, part) {
                if (!part.mainElement)
                  return;

                super_removeEventElement.call(this, event, part);
              },


              _layoutActionBar: function(actionBar, barHeight, eventElement) {
                var barWidth = actionBar._actionsArea._getWidth();
                O$.setElementSize(actionBar, {width: barWidth, height: barHeight});
                actionBar.style.left = "";
                actionBar.style.bottom = "";
                actionBar.style.right = "0px";
                actionBar.style.top = (eventElement._rect.height - barHeight) / 2 + "px";
              },


              _updateStartEndTime: function() {
                this._startTime = O$.MonthTable.getDay(this._day, firstDayOfWeek);
                this._endTime = O$.MonthTable.getFirstDayOut(this._day, firstDayOfWeek);

                O$.MonthTable.updateCellDays(this);

              },

              _updateTimeForCells: function() {
                var weekdayHeaderRow = weekdayHeadersTable.body._getRows()[0];

                var weekdayHeaderCellDay = this._startTime;
                for (var i = 0; i < weekdayHeaderRow._cells.length - 1; i++) {
                  var weekdayHeaderCell = weekdayHeaderRow._cells[i];
                  var weekdayHeaderCellClassName = this._weekdayHeaderCellClass;
                  if (weekdayHeaderCellDay.getDay() == 0 || weekdayHeaderCellDay.getDay() == 6) {
                    weekdayHeaderCellClassName = this._weekendWeekdayHeaderCellClass;
                  }
                  weekdayHeaderCell.className = weekdayHeaderCellClassName;
                  O$.removeAllChildNodes(weekdayHeaderCell);
                  var weekdayHeaderCellText = dtf.format(weekdayHeaderCellDay, weekdayPattern);
                  var weekdayHeaderCellStyledText = O$.createStyledText(weekdayHeaderCellText, weekdayStyle);
                  weekdayHeaderCell.appendChild(weekdayHeaderCellStyledText);
                  weekdayHeaderCellDay = O$.incDay(weekdayHeaderCellDay);
                }

              },

              _removeEventElements: function() {
                super_removeEventElements.call(this);
                clearAllCellEvents();
              },

              _updateEventElements: function(reacquireDayEvents, refreshAreasAfterReload) {
                if (!this._isActive()) return;
                this._baseZIndex = O$.getElementZIndex(this);
                this._removeEventElements();
                if (reacquireDayEvents)
                  this._events = this._eventProvider._getEventsForPeriod(this._startTime, this._endTime, function() {
                    this._updateEventElements(true, true);
                  });

                for (var cellDate = this._startTime; cellDate < this._endTime; cellDate = O$.MonthTable.__incDay(cellDate)) {
                  this._updateCellEventElements(cellDate);
                }

                if (refreshAreasAfterReload) {
                  this._events.forEach(function(event) {
                    event._attachAreas();
                  });
                }

                this._updateEventZIndexes();
              },

              previousMonth: function() {
                var prevDay = new Date(this._day.getFullYear(), this._day.getMonth() - 1, this._day.getDate());
                this.setDay(prevDay);
              },

              nextMonth: function() {
                var nextDay = new Date(this._day.getFullYear(), this._day.getMonth() + 1, this._day.getDate());
                if ((nextDay.getMonth() - this._day.getMonth()) % 12 != 1) {
                  nextDay = new Date(this._day.getFullYear(), this._day.getMonth() + 2, 0);
                }
                this.setDay(nextDay);
              },

              currentMonth: function() {
                var today = new Date();
                this.setDay(today);
              },

              _updateEventsPresentation: function() {
                var rows = monthTable._table.body._getRows();
                for (var rowIndex = 1; rowIndex < rows.length; rowIndex += 2) {
                  var row = rows[rowIndex];
                  var cells = row._cells;
                  for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
                    var cell = cells[cellIndex];
                    cell._moreLinkData = null;
                    cell._cellEvents.forEach(function(event) {
                      event.updatePresentation();
                    });
                    if (cell._moreLinkElement) {
                      cell._moreLinkElement._update();
                    }
                    cell._moreLinkData = null;
                  }
                }

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
              }

            });

    monthTable._adjustRolloverPaddings();

    var dtf = O$.getDateTimeFormatObject(locale);
    monthTable.setDay(dtf.parse(day, "dd/MM/yyyy"));

    monthTable._updateHeightForFF();

    monthTable._putTimetableChanges(null, null, null, true);
    O$.assignEvents(monthTable, {onchange: onchange}, true);

    O$.addInternalLoadEvent(function() {
      var maxScrollOffset = monthTable._scroller.scrollHeight - O$.getElementSize(monthTable._scroller).height;
      if (maxScrollOffset < 0)
        maxScrollOffset = 0;
      if (scrollOffset > maxScrollOffset)
        scrollOffset = maxScrollOffset;
      monthTable._scroller.scrollTop = scrollOffset;
      O$.addEventHandler(monthTable._scroller, "scroll", function() {
        O$.setHiddenField(monthTable, monthTable.id + "::scrollPos", monthTable._scroller.scrollTop);
      });

      monthTable.updateLayout(); // update positions after layout changes that might have had place during loading
    });

    O$.addEventHandler(window, "resize", function() {
      monthTable.updateLayout();
    });
  },

  getDay: function(day, firstDayOfWeek) {
    var firstDayOfMonth = O$.cloneDate(day);
    firstDayOfMonth.setDate(1);

    var dayOfWeek = firstDayOfMonth.getDay();
    var decrement = dayOfWeek - firstDayOfWeek;
    if (decrement < 0) {
      decrement += 7;
    }
    var result = O$.MonthTable.__incDay(firstDayOfMonth, -decrement);
    return result;
  },

  getFirstDayOut: function(day, firstDayOfWeek) {
    var lastDayOfMonth = new Date(day.getFullYear(), day.getMonth() + 1, 0);
    var lastDayOfMonthWeekday = lastDayOfMonth.getDay();
    var increment = firstDayOfWeek + 7 - lastDayOfMonthWeekday;
    var result = new Date(lastDayOfMonth.getFullYear(), lastDayOfMonth.getMonth(), lastDayOfMonth.getDate() + increment);
    return result;
  },

  getNextMonthStart: function(date) {
    return new Date(date.getFullYear(), date.getMonth() + 1, 1);
  },


  getDayEvents: function(allEvents, day) {
    var result = [];
    allEvents.forEach(function(currEvent) {
      if (currEvent.type != "reserved") {
        var start = currEvent.start;
        if (O$._datesEqual(start, day)) {
          result.push(currEvent);
        }
      }
    });
    result.sort(O$.Timetable.compareEventsByStart);
    return result;
  },

  updateCellDays: function(monthTable) {
    var numberOfWeeks = Math.round((monthTable._endTime - monthTable._startTime) / (7 * 24 * 60 * 60 * 1000));
    var visibleRowCount = 2 * numberOfWeeks;
    O$.MonthTable.setVisibleRowCount(monthTable._table, visibleRowCount);

    var cellDay = monthTable._startTime;
    var rows = monthTable._table.body._getRows();
    for (var rowIndex = 0; rowIndex < visibleRowCount; rowIndex += 2) {
      var dayHeaderRow = rows[rowIndex];
      var dayContentRow = rows[rowIndex + 1];
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
        var text = document.createElement("a");
        //TODO: LINK UNCLICKABLE
        text.style.zIndex = monthTable._baseZIndex + 150;
        text.innerHTML = cellDay.getDate();
        text.onclick = function(e) {
          O$.stopEvent(e);
          if (monthTable._timetable)
            monthTable._timetable.goViewDay(cellDay);
        }
        dayHeaderCell.appendChild(text);
        cellDay = O$.MonthTable.__incDay(cellDay);
      }
    }
  },

  getCellForDay: function(monthTable, day) {
    var rows = monthTable._table.body._getRows();
    for (var rowIndex = 1; rowIndex < rows.length; rowIndex += 2) {
      var row = rows[rowIndex];
      var cells = row._cells;
      for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
        var cell = cells[cellIndex];
        var cellDay = cell._cellDay;
        if (O$._datesEqual(cellDay, day)) {
          cell._rowIndex = rowIndex;
          return cell;
        }
      }
    }
    return null;
  },

  // increment the date without setting time to 12:00
  __incDay: function(date, increment) {
    var newDate = O$.cloneDateTime(date);
    if (increment === 0) {
      return newDate;
    }
    if (!increment) {
      increment = 1;
    }
    newDate.setDate(newDate.getDate() + increment);
    return newDate;
  },


  setVisibleRowCount: function(table, count) {
    var rows = table.body._getRows();
    var rowCount = Math.min(count, rows.length);
    for (var visibleRowIndex = 0; visibleRowIndex < rowCount; visibleRowIndex++) {
      rows[visibleRowIndex].style.display = "";
    }
    for (var hiddenRowIndex = rowCount; hiddenRowIndex < rows.length; hiddenRowIndex++) {
      rows[hiddenRowIndex].style.display = "none";
    }
  }

};

