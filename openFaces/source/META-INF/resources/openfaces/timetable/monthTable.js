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
//
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


    O$.initComponent(componentId, null, {
      _viewType: "month"
    });
    monthTable._expandedDayView = O$.initComponent(componentId + "::expandedDayView", null,{
        eventBlock  : O$(componentId + "::expandedDayView::eventBlock"),
        opened : false,
        scrollPos : 0,
        allEventHeight : 0,
        header : O$(componentId + "::expandedDayView::header"),
        footer : O$(componentId + "::expandedDayView::footer"),
        headerHeight : 0,
        footerHeight : 0,
        expandedEvents : [],
        transitionPeriod : stylingParams.expandTransitionPeriod
      },
      {
        _init: function(){
          this.style.display = "none";
          O$.invokeWhenVisible(monthTable._expandedDayView, function (){monthTable._expandedDayView._correctMarginsForButtonLayout()});
        },
        _scrollContent: function (delta){
          //TODO: cache O$.getElementSize(this).height value
          if (this.scrollPos + delta > this.headerHeight ||
              this.scrollPos + delta + this.footerHeight +this.allEventHeight  < O$.getElementSize(this).height){
            return;
          };
          this.scrollPos += delta;
          for (var i = 0; i<this.eventBlock.childNodes.length;i++){
            var position = O$.getNumericElementStyle(this.eventBlock.childNodes[i], "top");
           this.eventBlock.childNodes[i].style.top = (position + delta)+"px";
          }
        },

        _addExpandedEventElements : function (cell){
          var i = 0;
          cell.reservedPlaces.forEach(function(event){
            var part = {
              start: monthTable._expandedDayView.expandedDay,
              end: monthTable._expandedDayView.expandedDay,
              index: i++,
              event: event,
              expandedPart: true
            };
            i++;
            monthTable._addEventElement(event, part);
            monthTable._expandedDayView.expandedEvents.push({event: event, part: part});
          });
        },

        _removeExpandedEventElements : function () {
          this.expandedEvents.forEach(function(expandedEvent){
                monthTable._removeEventElement(expandedEvent.event, expandedEvent.part);
          });
          this.expandedEvents = [];
          this.eventCount = 0;
          this.scrollPos = 0;
        },

        _expandDayView: function (dayCell){
          if (this.opened){
            this._contractDayView();
          }
          this.style.display = "";
          var cellBoundaries = O$.getElementBorderRectangle(dayCell, true);
          O$.setElementPos(this, cellBoundaries);
          this.expandedDay = dayCell._cellDay;
          this._addExpandedEventElements(dayCell);
          monthTable._recalculatePositions();
          this.opened = true;
          var rect = O$.getElementBorderRectangle(this, true).clone();
          O$.setElementSize(this, {height:10});
          this._lastRectangleTransition = O$.runTransitionEffect(this, ["rectangle"], [rect], this.transitionPeriod, 20, null);
        },
        _contractDayView : function (){
          this.style.display = "none";
          this.expandedDay = null;
          this.opened = false;
          this._removeExpandedEventElements();
          monthTable._recalculatePositions();
        },
        _correctMarginsForButtonLayout : function (){
          this.headerHeight = O$.getElementSize(this.header).height;
          this.footerHeight = O$.getElementSize(this.footer).height;
          this.eventBlock.style.marginTop = -1 * this.headerHeight + "px"
          this.eventBlock.style.marginBottom = -1 * this.footerHeight + "px"
        },
        _correctExpandedDayViewZIndex : function (){
          this.style.zIndex  = monthTable._maxZIndex + 5;
          this.header.style.zIndex = monthTable._maxZIndex + 5;
          this.footer.style.zIndex = monthTable._maxZIndex + 5;
        }
    });
    monthTable._expandedDayView._init.call(monthTable._expandedDayView);




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

    var eventElementHeight = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._eventStyleClass, "height"));
    var eventsLeftOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._eventStyleClass, "marginLeft"));
    var eventsRightOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._eventStyleClass, "marginRight"));
    var reservedEventsLeftOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._reservedTimeEventClass, "marginLeft"));
    var reservedEventsRightOffset = O$.calculateNumericCSSValue(O$.getStyleClassProperty(monthTable._reservedTimeEventClass, "marginRight"));
    var moreLinkElementHeight = O$.calculateNumericCSSValue(O$.getStyleClassProperty(moreLinkElementClass, "height"));

    var weekdayHeadersTable = O$(componentId + "::weekdayHeaders");

    monthTable.firstDayOfWeek = (calendarOptions && calendarOptions.firstDayOfWeek) ? calendarOptions.firstDayOfWeek : 0;

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
              forceUsingCellStyles: forceUsingCellStyles,
              additionalParams: {}
            });

    O$.Tables._init(weekdayHeadersTable, {
              columns: weekdayHeaderColumns,
              gridLines: [rowSeparator, weekdayColumnSeparator, null, null, null, null, null, null, null, null, null],
              body: {rowClassName: weekdayHeadersRowClass},
              forceUsingCellStyles: forceUsingCellStyles,
              additionalParams: {}
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
          if (monthTable._expandedDayView.opened){
            monthTable._expandedDayView._contractDayView();
          }else if (editable) {
            var newEventTime = this._cellDay;
            var event = monthTable._addEvent(newEventTime, null);
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


    monthTable._appendEventElements = function(events) {
      events.forEach(function(event) {
        monthTable._addEventElements(event);
      });
    }


    var super_addEventElement = monthTable._addEventElement;
    var super_removeEventElement = monthTable._removeEventElement;
    var super_removeEventElements = monthTable._removeEventElements;
    O$.extend(monthTable, {
              scrollContent: function(delta){
                this._expandedDayView._scrollContent(delta);
              },

              //We are using +/- 1 ms for fixing an issue with different work of 00:00:00 time under different browsers
              _splitIntoParts: function(event) {
                var parts = [];
                var layoutEndTime = O$.incDay(this._endTime, -1 );
                var start = this._startTime > event.start ? this._startTime : event.start;
                var end = layoutEndTime < event.end ? layoutEndTime : event.end;
                var partStart = start;
                var partEnd = O$.incDay(
                        new Date(partStart.getFullYear(), partStart.getMonth(), partStart.getDate()),
                        partStart.getDay() + 1 != monthTable.firstDayOfWeek ? (7 - partStart.getDay() + monthTable.firstDayOfWeek) % 8 : 1,
                        true
                );
                end.setTime(end.getTime() - 1);
                partEnd.setTime(partEnd.getTime() - 1);
                var i = 0;

                do {
                  var part = {
                    start: partStart,
                    end: (partEnd < end) ? O$.cloneDateTime(partEnd) : O$.cloneDateTime(end),
                    index: i++,
                    event: event,
                    expandedPart: false
                  };
                  parts.push(part);
                  partStart = O$.cloneDateTime(partEnd);
                  partStart.setTime(partStart.getTime() + 1);

                  // adding delta to day need to end till the week end      \
                  partEnd.setTime(partEnd.getTime() + 1);
                  partEnd = O$.incDay(O$.cloneDate(partEnd), 7, true);
                  partEnd.setTime(partEnd.getTime() - 1);
                } while (partStart.getTime() < end.getTime()+2);


                return parts;
              },

              _getNearestTimeslotForPosition: function(x, y) {
                var cell = this._table._cellFromPoint(x, y, true, this._getLayoutCache());
                if (cell._cell) {
                  cell = cell._cell;
                }
                return cell;
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

              _addEventElement: function(event, part) {
                var eventElement = super_addEventElement.call(this, event, part);

                O$.extend(event, {
                  updatePresentation: function(){
                    monthTable._recalculatePositions();
                  },

                  _getDraggablePartIndex: function() {
                    for (var i = 0; i < event.parts.length; i++) {
                      part = event.parts[i];
                      var positionTime =  new Date(event._dragPositionTime.getFullYear(), event._dragPositionTime.getMonth(), event._dragPositionTime.getDate());
                      if (O$.MonthTable.checkDayInInterval(positionTime, part )){
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

                        event._dragPositionTime = monthTable._getNearestTimeslotForPosition(pos.x, pos.y)._cellDay;
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

                      ondragend: function (){
                        //TODO: resolve issue with 1 minute lost after dragging
                        monthTable.updateEvent(event);
                      },

                      setPosition: function (left, top, dx, dy) {
                        var rect = O$.getElementBorderRectangle(monthTable._table, true);
                        var maxTop = rect.height;
                        var maxLeft = rect.width;
                        left = left < 0 ? 0 : left > maxLeft ? maxLeft : left;
                        top = top < 0 ? 0 : top > maxTop ? maxTop : top;

                        var nearestTimeslot = monthTable._getNearestTimeslotForPosition(left, top);
                        var timeIncrement = nearestTimeslot._cellDay.getTime() - event._dragPositionTime.getTime();

                        if (timeIncrement != 0) {
                          event._dragPositionTime = nearestTimeslot._cellDay;

                          var newStartTime = O$.dateByTimeMillis(event.start.getTime() + timeIncrement);
                          var newEndTime = O$.dateByTimeMillis(event.end.getTime() + timeIncrement);

                          event.setStart(newStartTime);
                          event.setEnd(newEndTime);

                          eventElement.style.cursor = "move";
                          monthTable._recalculatePositions();
                        }
                      }
                    });
                  }
                });
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
                  if (!monthTable._layoutNeeded) return;

                  if (event.resourceId) {
                    var resource = monthTable._getResourceForEvent(event);
                    if (!resource) {
                      this.style.display = "none";
                      this._backgroundElement.style.display = "none";
                      return;
                    }
                  }
                  this.style.display = "";
                  this._backgroundElement.style.display = "";
                  var cell = O$.MonthTable.getCellForDay(monthTable,part.start);
                  var x1,y1,x2,y2;
                  if (part.expandedPart){
                    var changeEventParent = function(newParent){
                      newParent.appendChild(this);
                      newParent.appendChild(this._backgroundElement);
                    }
                    changeEventParent.call(this, monthTable._expandedDayView.eventBlock);
                    var cellBoundaries = O$.getElementBorderRectangle(monthTable._expandedDayView.eventBlock, true);

                    y1 = monthTable._expandedDayView.headerHeight + eventElementHeight * monthTable._expandedDayView.eventCount;
                    y2 = y1 + eventElementHeight;

                    monthTable._expandedDayView.eventCount = monthTable._expandedDayView.eventCount + 1;
                    monthTable._expandedDayView.allEventHeight = y1 + eventElementHeight;
                    x1 = 0 + (event.type != "reserved" ? eventsLeftOffset : reservedEventsLeftOffset);
                    if (event.start.getDate() < part.start.getDate()){
                      x1 -= 50;
                    };
                    x2 = cellBoundaries.getMaxX() - cellBoundaries.getMinX() - (event.type != "reserved" ? eventsRightOffset : reservedEventsRightOffset);
                    if (event.end.getDate() > part.end.getDate()){
                      x2 += 50;
                    };
                  }else{
                    var endDayCell = O$.MonthTable.getCellForDay(monthTable,part.end);
                    var endDayCellBoundaries = O$.getElementBorderRectangle(endDayCell, true);

                    var startDayCellBoundaries = O$.getElementBorderRectangle(cell, true);
                    x1 = startDayCellBoundaries.getMinX() + (event.type != "reserved" ? eventsLeftOffset : reservedEventsLeftOffset);
                    x2 = - (event.type != "reserved" ? eventsRightOffset : reservedEventsRightOffset);

                    //TODO: connect this with almost the same code for expanded day view
                    if (event.start.getTime() < part.start.getTime()){
                      x1 -= 50;
                    };
                    if (event.end.getTime() > part.end.getTime()){
                      x2 +=  endDayCellBoundaries.getMaxX() + 50;
                    } else {
                      x2 += endDayCellBoundaries.getMaxX();
                    }


                    var placeIndex = event.placeIndex;
                    y1 = startDayCellBoundaries.getMinY() + eventElementHeight * placeIndex;
                    y2 = y1 + eventElementHeight;

                    var maxY = startDayCellBoundaries.getMaxY() - moreLinkElementHeight;
                    //TODO: not actually sure that we need two ifs to hide element that is not displayed
                    if (y2 > maxY && ! cell._moreLinkData) {
                      cell._moreLinkData = { topY: y1 };
                    }
                    if (cell._moreLinkData ) {
                      this.style.display = "none";
                      this._backgroundElement.style.display = "none";
                      return;
                    }




                    if (O$.isExplorer() && O$.isStrictMode() && cell._last) {
                      var scroller = O$(monthTable.id + "::scroller");
                      var scrollerWidth = scroller.offsetWidth - scroller.clientWidth;
                      x2 -= scrollerWidth;
                    }
                  };

                  this._rect = new O$.Rectangle(Math.round(x1), Math.round(y1),
                          Math.round(x2 - x1), Math.round(y2 - y1));
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

                this._startTime = O$.MonthTable.getDay(this._day, monthTable.firstDayOfWeek);
                this._endTime = O$.MonthTable.getFirstDayOut(this._day, monthTable.firstDayOfWeek);

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
                this._clearReservedPlaces();
              },

              _clearReservedPlaces: function() {
                var rows = monthTable._table.body._getRows();
                for (var rowIndex = 1; rowIndex < rows.length; rowIndex += 2) {
                  var row = rows[rowIndex];
                  var cells = row._cells;
                  for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
                    var cell = cells[cellIndex];
                    cell.reservedPlaces = [];
                    cell._moreLinkData = false;
                  }
                }
              },

              _addMoreLink: function(cell) {
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
              },


              _recalculatePositions: function() {
                // sort events by start date if date equal the sort by length of event
                function getEventDaysDuration(event){
                  return O$.getDayInterval(event.end, event.start)
                }

                this._events.sort(function(row1, row2){
                  var value1 = row1.start, value2 = row2.start;
                  if (!O$._datesEqual(value1, value2)){
                    return (value1 > value2) ? 1 : -1;
                  }

                  value1 = getEventDaysDuration(row1);
                  value2 = getEventDaysDuration(row2);
                  if (Math.abs(value1) != Math.abs(value2)){
                    return (Math.abs(value1) > Math.abs(value2)) ? -1 :  1 ;
                  }

                  return (row1.id > row2.id) ? 1 : -1;
                });

                // fill reserved places for cells
                this._clearReservedPlaces();
                this._events.forEach(function(event) {
                  // reserving place inside first cell
                  var eventStart = monthTable._startTime > event.start ? monthTable._startTime : event.start;
                  var eventEnd = monthTable._endTime < event.end ? monthTable._endTime : event.end;
                  var startCell = O$.MonthTable.getCellForDay(monthTable, eventStart);
                  var placeIndex;
                  for (placeIndex=0; placeIndex<startCell.reservedPlaces.length; placeIndex++){
                    if (!startCell.reservedPlaces[placeIndex]){
                      break;
                    }
                  };
                  startCell.reservedPlaces[placeIndex] = event;
                  event.placeIndex = placeIndex;
                  // reserving place inside all continiously days
                  if (!O$._datesEqual(eventStart, eventEnd)){
                    var nextDayCell
                    for (day = O$.incDay(eventStart,1); !O$._datesEqual(day, O$.incDay(eventEnd,1)); day=O$.incDay(day,1)){
                      nextDayCell = O$.MonthTable.getCellForDay(monthTable,day);
                      if (!nextDayCell) break;
                      nextDayCell.reservedPlaces[placeIndex] = event;
                    }
                  }
                  // recalculate parts if needed
                  if (event.parts) {
                    var newParts = monthTable._splitIntoParts(event);
                    if (newParts.length > event.parts.length) {
                      event.parts.push(newParts[newParts.length-1]);
                      monthTable._addEventElement(event, event.parts[event.parts.length-1]);
                    };
                    if (newParts.length < event.parts.length) {
                      monthTable._removeEventElement(event, event.parts[event.parts.length-1]);
                      event.parts.splice(event.parts.length-1);
                    };
                    for (var i=0; i<event.parts.length;i++){
                      event.parts[i].start = newParts[i].start;
                      event.parts[i].end = newParts[i].end;
                    }
                  }
                });


                this._events.forEach(function(event) {
                  event.parts.forEach(function(part){
                    part.mainElement._updatePos();
                  })
                })

                for (var rowIndex = 1; rowIndex < rows.length; rowIndex += 2) {
                  var row = rows[rowIndex];
                  var cells = row._cells;
                  for (var cellIndex = 0; cellIndex < cells.length; cellIndex++) {
                    var cell = cells[cellIndex];
                    if (!cell._moreLinkElement){
                      this._addMoreLink(cell)._update();
                    }else{
                      cell._moreLinkElement._update();
                    }
                  }
                }
              },

              _updateEventElements: function(reacquireDayEvents, refreshAreasAfterReload) {
                if (!this._isActive()) return;
                this._baseZIndex = O$.getElementZIndex(this);
                monthTable._expandedDayView.eventCount = 0;
                this._removeEventElements();
                if (reacquireDayEvents)
                  this._events = this._eventProvider._getEventsForPeriod(this._startTime, this._endTime, function() {
                    this._updateEventElements(true, true);
                  });
                //TODO: refactor passing of _layoutNeeded inside update element code
                this._layoutNeeded = false;
                this._appendEventElements(this._events);
                this._layoutNeeded = true;

                if (refreshAreasAfterReload) {
                  this._events.forEach(function(event) {
                    event._attachAreas();
                  });
                }

                this._updateEventZIndexes();
                monthTable._expandedDayView._correctExpandedDayViewZIndex();
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
                this._recalculatePositions();
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

  checkDayInInterval: function(day, interval){
    day.setHours(0);
    day.setMinutes(0);

    var incDay = O$.incDay(day,1);
    if (!(interval.end < day || interval.start > incDay))
      return true;
    return false;
  },

  getDayEvents: function(allEvents, day, getPartEvents) {
    var result = [];
    allEvents.forEach(function(currEvent) {
      if (currEvent.type != "reserved") {
        var start = currEvent.start;
        if (getPartEvents){
          if (O$.MonthTable.checkDayInInterval(day, currEvent)) {
            result.push(currEvent);
          }
        } else {
          if (O$._datesEqual(start, day)) {
            result.push(currEvent);
          }
        }
      }
    });
    return result;
  },

  updateCellDays: function(monthTable) {
    var numberOfWeeks = Math.round((monthTable._endTime - monthTable._startTime) / (7 * 24 * 60 * 60 * 1000));
    var visibleRowCount = 2 * numberOfWeeks;
    O$.MonthTable.setVisibleRowCount(monthTable._table, visibleRowCount);

    var cellDay = O$.cloneDate(monthTable._startTime);
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


        if (monthTable._timetable){
          var dayLink = document.createElement("a");
          O$.correctElementZIndex(dayLink, monthTable, 20);
          dayLink.style.position = "relative";
          dayLink.style.cursor = "pointer";
          dayLink.innerHTML = cellDay.getDate();
          dayLink.linkedDay = O$.cloneDate(cellDay);
          dayLink.onclick = function(e) {
            O$.stopEvent(e);
            if (monthTable._timetable)
              monthTable._timetable.goViewDay(this.linkedDay);
          }
          dayHeaderCell.appendChild(dayLink);
        }else{
          var text = document.createElement("span");
          text.innerHTML = cellDay.getDate();
          dayHeaderCell.appendChild(text);
        };
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
        if (O$._datesEqual(cellDay, day))
          return cell;
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

