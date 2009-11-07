/*
 * OpenFaces - JSF Component Library 2.0
 * Copyright (C) 2007-2009, TeamDev Ltd.
 * licensing@openfaces.org
 * Unless agreed in writing the contents of this file are subject to
 * the GNU Lesser General Public License Version 2.1 (the "LGPL" License).
 * This library is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * Please visit http://openfaces.org/licensing/ for more details.
 */

O$._initTabSet = function(tabSetId, tabIds, selectedIndex, placement,
                          tabStylesParams, borderClassesParams, focusable, focusAreaClass, focusedClass,
                          onchange) {
  var tabset = O$(tabSetId);

  tabset._index = selectedIndex;
  tabset._indexField = O$(tabSetId + "::selected");
  tabset._placement = placement;
  tabset.style.emptyCells = "show"; // needed for JSFC-2713 to show cell borders on empty cells (inter-tab spacings)
  tabset._tabs = [];
  tabset._tabCount = tabIds.length;
  tabset.onchange = onchange;

  for (var i = 0, count = tabset._tabCount; i < count; i++) {
    var tabId = tabIds[i];
    var separatorIndex = tabId.lastIndexOf("::");
    var idxStr = tabId.substring(separatorIndex + "::".length);
    var absoluteIndex = parseInt(idxStr, 10);
    var tab = O$(tabId);
    tab._absoluteIndex = absoluteIndex;
    tabset._tabs[i] = tab;
    tab._index = i;

    O$.assignEventHandlerField(tab, "onmouseover", function (e) {
      if (this._tabbedPaneCall == null) {
        tabset._indexOver = this._index;
      }

      if (this._absoluteIndex == tabset._index) {
        if (this._tabbedPaneCall == null)
        {
          O$.setStyleMappings(this, {
            mouseover: tabset._rolloverTabClass
          });
        } else {
          O$.setStyleMappings(this, {
            mouseover_TP: tabset._rolloverTabClass
          });
        }
        O$.setStyleMappings(this, {
          mouseoverSelected: tabset._rolloverSelectTabClass
        });

        O$.setStyleMappings(this, {
          border:  tabset.frontBorderClass
        });
      }
      else {
        O$.setStyleMappings(this, {
          mouseover:  tabset._rolloverTabClass
        });
        O$.setStyleMappings(this, {
          border:  this._borderClass
        });
      }
      this._tabbedPaneCall = null;
      O$.cancelBubble(e);
    });

    O$.assignEventHandlerField(tab, "onmouseout", function (e) {
      if (this._tabbedPaneCall == null) {
        tabset._indexOver = null;
      }

      if (this._absoluteIndex == tabset._index) {
        O$.setStyleMappings(this, {
          mouseoverSelected: null
        });

        if (this._tabbedPaneCall == null)
        {
          O$.setStyleMappings(this, {
            mouseover: null
          });
        } else {
          O$.setStyleMappings(this, {
            mouseover_TP: null
          });
        }

        O$.setStyleMappings(this, {
          border:  tabset.frontBorderClass
        });
      }
      else {
        O$.setStyleMappings(this, {
          border: this._borderClass
        });
      }
      O$.setStyleMappings(this, {
        mouseover:  null
      });
      O$.setStyleMappings(this, {
        mouseover_TP: null
      });
      this._tabbedPaneCall = null;
      O$.cancelBubble(e);
    });

    O$.assignEventHandlerField(tab, "onclick", function () {
      tabset.setSelectedIndex(this._absoluteIndex, true);
      O$.setStyleMappings(this, {
        mouseoverSelected: tabset._rolloverSelectTabClass
      });
    });
  }

  tabset.getSelectedIndex = function () {
    return tabset._index;
  }

  tabset._getTabByAbsoluteIndex = function (absoluteTabIndex) {
    var tab = null;
    for (var i = 0, count = tabset._tabs.length; i < count; i++) {
      var currTab = tabset._tabs[i];
      if (currTab._absoluteIndex == absoluteTabIndex) {
        tab = currTab;
        break;
      }
    }
    return tab;
  }

  tabset.getTabCount = function() {
    return this._tabCount;
  }

  tabset.setSelectedIndex = function (index, cancellable) {
    if (focusable) {
      O$.setStyleMappings(tabset._tabs[tabset._index].childNodes[0], {
        focused:  null
      });
      O$.setStyleMappings(tabset._tabs[tabset._index], {
        focused: null
      });
      O$.setStyleMappings(tabset._tabs[index].childNodes[0], {
        focused:  focusAreaClass
      });
      O$.setStyleMappings(tabset._tabs[index], {
        focused: tabset._focusedTabClass
      });

    }
    var tab = this._getTabByAbsoluteIndex(index);
    if (tab == null)
      throw "An attempt to select non-rendered or non-existing tab has been made. index =  " + index + "; TabSet id = " + tabset.id;

    if (index == tabset._index)
      return;

    O$.setStyleMappings(tabset._tabs[tabset._index], {
      selected:  null
    });
    O$.setStyleMappings(tabset._tabs[tabset._index], {
      mouseoverSelected: null
    });
    O$.setStyleMappings(tabset._tabs[tabset._index], {
      mouseover_TP: null
    });

    var prevIndex = tabset._index;
    tabset._index = tab._absoluteIndex;
    tabset._indexField.value = tab._absoluteIndex;
    if (tabset.onchange) {
      var evt = O$.createEvent("change");
      evt._absoluteIndex = tab._absoluteIndex;
      if ((tabset.onchange(evt) === false || evt.returnValue === false) && cancellable) {
        tabset._index = prevIndex;
        tabset._indexField.value = prevIndex;
        return false;
      }
    }
    if (tabset._indexOver != null && tabset._indexOver == index) {
      O$.setStyleMappings(tabset._tabs[index], {
        mouseoverSelected: tabset._rolloverSelectTabClass
      });
    }
    tabset._refreshTabs();
  }


  tabset._setTabStyles = function (tabClass, selectTabClass, rolloverTabClass, rolloverSelectTabClass, focusedTabClass) {
    tabset._tabClass = tabClass;
    tabset._selectTabClass = selectTabClass;
    tabset._rolloverTabClass = rolloverTabClass;
    tabset._rolloverSelectTabClass = rolloverSelectTabClass;
    tabset._focusedTabClass = focusedTabClass;
  }

  tabset._setBorderClasses = function (frontBorderClass, backBorderClass1, backBorderClass2) {
    tabset.frontBorderClass = frontBorderClass;
    if (placement == 'top' || placement == 'right') {
      tabset.backBorderClass1 = backBorderClass1;
      tabset.backBorderClass2 = backBorderClass2;
    } else {
      tabset.backBorderClass1 = backBorderClass2;
      tabset.backBorderClass2 = backBorderClass1;
    }
    tabset._refreshTabs();
  }

  tabset._refreshTabs = function () {
    if (!tabset._tabs)
      return;

    function initTabStyles(tab, borderClass) {
      tab._borderClass = borderClass;
    }

    for (var i = 0, tabCount = tabset._tabs.length; i < tabCount; i++) {
      var tab = tabset._tabs[i];
      O$.setStyleMappings(tab, {
        main: tabset._tabClass
      });
      if (tab._absoluteIndex < tabset._index) {
        O$.setStyleMappings(tab, {
          border: tabset.backBorderClass1
        });
        initTabStyles(tab, tabset.backBorderClass1);
      }
      else if (tab._absoluteIndex == tabset._index) {
        O$.setStyleMappings(tab, {
          selected: tabset._selectTabClass
        });

        O$.setStyleMappings(tab, {
          border: tabset.frontBorderClass
        });
        initTabStyles(tab, tabset.frontBorderClass);
      }
      else {
        O$.setStyleMappings(tab, {
          border: tabset.backBorderClass2
        });
        initTabStyles(tab, tabset.backBorderClass2);
      }
    }
  };

  tabset._setTabStyles.apply(tabset, tabStylesParams);
  tabset._setBorderClasses.apply(tabset, borderClassesParams);

  if (focusable) {
    O$.setupArtificialFocus(tabset, focusedClass);
    var eventName = (O$.isSafariOnMac() || O$.isOpera() || O$.isMozillaFF()) ? "onkeypress" : "onkeydown";
    tabset._prevKeyHandler = tabset[eventName];
    tabset[eventName] = function (evt) {
      var e = evt ? evt : window.event;
      switch (e.keyCode) {
        case 38: // up
          O$._setNextIndex(tabset, -1);
          break;
        case 40: // down
          O$._setNextIndex(tabset, 1);
          break;
        case 37: // left
          O$._setNextIndex(tabset, -1);
          break;
        case 39: // right
          O$._setNextIndex(tabset, 1);
          break;
      }
      if (tabset._prevKeyHandler) {
        tabset._prevKeyHandler(e);
      }
      if ((e.keyCode == 38 || e.keyCode == 40 || e.keyCode == 37 || e.keyCode == 39) && O$.isSafari())
        return false;
    };

    tabset._oldfocus = tabset.onfocus;
    tabset.onfocus = function(e) {
      if (tabset._oldfocus)
        tabset._oldfocus(e);
      O$.setStyleMappings(tabset._tabs[tabset._index].childNodes[0], {
        focused:  focusAreaClass
      });
      O$.setStyleMappings(tabset._tabs[tabset._index], {
        focused: tabset._focusedTabClass
      });
    };

    tabset._oldblur = tabset.onblur;
    tabset.onblur = function(e) {
      if (tabset._oldblur)
        tabset._oldblur(e);
      O$.setStyleMappings(tabset._tabs[tabset._index].childNodes[0], {
        focused:  null
      });
    };
  }
}

O$._setNextIndex = function(tabset, inc) {
  var number = tabset._tabCount;
  var nextIndex = tabset._index + inc;

  if (nextIndex >= number) {
    nextIndex = nextIndex - number;
  }
  if (nextIndex < 0) {
    nextIndex = number + nextIndex;
  }
  tabset.setSelectedIndex(tabset._tabs[nextIndex]._absoluteIndex, true);
}
