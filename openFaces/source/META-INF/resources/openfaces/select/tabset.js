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

O$.TabSet = {
  _init:function (tabSetId, tabIds, selectedIndex, placement, tabStylesParams, borderClassesParams, focusable, focusAreaClass, focusedClass, onchange) {
    var tabSet = O$.initComponent(tabSetId, null, {
      _index:selectedIndex,
      _indexField:O$(tabSetId + "::selected"),
      _placement:placement,
      _tabs:[],
      _tabCount:tabIds.length,
      onchange:onchange,

      _getTabByAbsoluteIndex:function (absoluteTabIndex) {
        var tab = null;
        for (var i = 0, count = tabSet._tabs.length; i < count; i++) {
          var currTab = tabSet._tabs[i];
          if (currTab._absoluteIndex == absoluteTabIndex) {
            tab = currTab;
            break;
          }
        }
        return tab;
      },

      getTabCount:function () {
        return this._tabCount;
      },

      setSelectedIndex:function (index, cancellable) {
        var tab = this._getTabByAbsoluteIndex(index);
        if (tab == null)
          throw "An attempt to select non-rendered or non-existing tab has been made. index =  " + index + "; TabSet id = " + tabSet.id;

        if (index == tabSet._index)
          return;


        var prevTab = this._getTabByAbsoluteIndex(tabSet._index);

        O$.setStyleMappings(prevTab, {
          mouseoverSelected:null
        });
        O$.setStyleMappings(prevTab, {
          mouseover_TP:null
        });

        var prevIndex = tabSet._index;
        tabSet._index = tab._absoluteIndex;
        tabSet._indexField.value = tab._absoluteIndex;
        if (tabSet.onchange) {
          var evt = O$.createEvent("change");
          evt._absoluteIndex = tab._absoluteIndex;
          if ((tabSet.onchange(evt) === false || evt.returnValue === false) && cancellable) {
            tabSet._index = prevIndex;
            tabSet._indexField.value = prevIndex;
            return false;
          }
        }
        if (tabSet._indexOver != null && tabSet._indexOver == index) {
          O$.setStyleMappings(tabSet._tabs[index], {
            mouseoverSelected:tabSet._rolloverSelectTabClass
          });
        }
        tabSet._refreshTabs();
      },


      _setTabStyles:function (tabClass, selectTabClass, rolloverTabClass, rolloverSelectTabClass, focusedTabClass) {
        tabSet._tabClass = tabClass;
        tabSet._selectTabClass = selectTabClass;
        tabSet._rolloverTabClass = rolloverTabClass;
        tabSet._rolloverSelectTabClass = rolloverSelectTabClass;
        tabSet._focusedTabClass = focusedTabClass;
      },

      _setBorderClasses:function (frontBorderClass, backBorderClass1, backBorderClass2) {
        tabSet.frontBorderClass = frontBorderClass;
        if (placement == 'top' || placement == 'right') {
          tabSet.backBorderClass1 = backBorderClass1;
          tabSet.backBorderClass2 = backBorderClass2;
        } else {
          tabSet.backBorderClass1 = backBorderClass2;
          tabSet.backBorderClass2 = backBorderClass1;
        }
        tabSet._refreshTabs();
      },

      _refreshTabs:function () {
        if (!tabSet._tabs)
          return;

        function initTabStyles(tab, borderClass) {
          tab._borderClass = borderClass;
        }

        for (var i = 0, tabCount = tabSet._tabs.length; i < tabCount; i++) {
          var tab = tabSet._tabs[i];
          O$.setStyleMappings(tab, {
            main:tabSet._tabClass
          });
          O$.setStyleMappings(tab, {
            selected:tab._absoluteIndex == tabSet._index ? tabSet._selectTabClass : null
          });
          if (tab._absoluteIndex < tabSet._index) {
            O$.setStyleMappings(tab, {
              border:tabSet.backBorderClass1
            });
            initTabStyles(tab, tabSet.backBorderClass1);
          } else if (tab._absoluteIndex == tabSet._index) {

            O$.setStyleMappings(tab, {
              border:tabSet.frontBorderClass
            });
            initTabStyles(tab, tabSet.frontBorderClass);
          } else {
            O$.setStyleMappings(tab, {
              border:tabSet.backBorderClass2
            });
            initTabStyles(tab, tabSet.backBorderClass2);
          }

        }
      },
      getSelectedIndex:function () {
        return tabSet._index;
      },

      _setNextIndex:function (inc) {
        var number = tabSet._tabCount;
        var tab = tabSet._getTabByAbsoluteIndex(tabSet._index);
        var nextIndex = tab._index + inc;

        if (nextIndex >= number) {
          nextIndex = nextIndex - number;
        }
        if (nextIndex < 0) {
          nextIndex = number + nextIndex;
        }
        tabSet._setTabFocused(tab._index, nextIndex);
        tabSet.setSelectedIndex(tabSet._tabs[nextIndex]._absoluteIndex, true);

      },

      _setTabFocused:function (prevIndex, nextIndex) {
        var prevTab = tabSet._getTabByAbsoluteIndex(prevIndex);
        var tab = tabSet._getTabByAbsoluteIndex(nextIndex);

        O$.setStyleMappings(prevTab.childNodes[0], {
          focused:null
        });
        O$.setStyleMappings(prevTab, {
          focused:null
        });
        O$.setStyleMappings(tab.childNodes[0], {
          focused:focusAreaClass
        });
        O$.setStyleMappings(tab, {
          focused:tabSet._focusedTabClass
        });
      }
    });
    tabSet.style.emptyCells = "show"; // needed for JSFC-2713 to show cell borders on empty cells (inter-tab spacings)


    for (var i = 0, count = tabSet._tabCount; i < count; i++) {
      var tabId = tabIds[i];
      var separatorIndex = tabId.lastIndexOf("::");
      var idxStr = tabId.substring(separatorIndex + "::".length);
      var absoluteIndex = parseInt(idxStr, 10);
      var tab = O$(tabId);
      tab._absoluteIndex = absoluteIndex;
      tabSet._tabs[i] = tab;
      tab._index = i;

      O$.assignEventHandlerField(tab, "onmouseover", function (e) {
        if (this._tabbedPaneCall == null) {
          tabSet._indexOver = this._index;
        }

        if (this._absoluteIndex == tabSet._index) {
          if (this._tabbedPaneCall == null)
            O$.setStyleMappings(this, {mouseover:tabSet._rolloverTabClass});
          else
            O$.setStyleMappings(this, {mouseover_TP:tabSet._rolloverTabClass});
          O$.setStyleMappings(this, {mouseoverSelected:tabSet._rolloverSelectTabClass});

          O$.setStyleMappings(this, {border:tabSet.frontBorderClass});
        } else {
          O$.setStyleMappings(this, {mouseover:tabSet._rolloverTabClass});
          O$.setStyleMappings(this, {border:this._borderClass});
        }
        this._tabbedPaneCall = null;
        O$.stopEvent(e);
      });

      O$.assignEventHandlerField(tab, "onmouseout", function (e) {
        if (this._tabbedPaneCall == null) {
          tabSet._indexOver = null;
        }

        if (this._absoluteIndex == tabSet._index) {
          O$.setStyleMappings(this, {mouseoverSelected:null});

          if (this._tabbedPaneCall == null)
            O$.setStyleMappings(this, {mouseover:null});
          else
            O$.setStyleMappings(this, {mouseover_TP:null});

          O$.setStyleMappings(this, {border:tabSet.frontBorderClass});
        } else {
          O$.setStyleMappings(this, {border:this._borderClass});
        }
        O$.setStyleMappings(this, {mouseover:null});
        O$.setStyleMappings(this, {mouseover_TP:null});
        this._tabbedPaneCall = null;
        O$.stopEvent(e);
      });

      O$.assignEventHandlerField(tab, "onclick", function () {
        tabSet.setSelectedIndex(this._absoluteIndex, true);
        O$.setStyleMappings(this, {mouseoverSelected:tabSet._rolloverSelectTabClass});
      });
    }

    tabSet._setTabStyles.apply(tabSet, tabStylesParams);
    tabSet._setBorderClasses.apply(tabSet, borderClassesParams);

    if (focusable) {
      O$.setupArtificialFocus(tabSet, focusedClass);
      var eventName = (O$.isSafariOnMac() || O$.isOpera() || O$.isMozillaFF()) ? "onkeypress" : "onkeydown";
      tabSet._prevKeyHandler = tabSet[eventName];
      tabSet[eventName] = function (evt) {
        var e = evt ? evt : window.event;
        switch (e.keyCode) {
          case 38: // up
            tabSet._setNextIndex(-1);
            break;
          case 40: // down
            tabSet._setNextIndex(1);
            break;
          case 37: // left
            tabSet._setNextIndex(-1);
            break;
          case 39: // right
            tabSet._setNextIndex(1);
            break;
        }
        if (tabSet._prevKeyHandler) {
          tabSet._prevKeyHandler(e);
        }
        if ((e.keyCode == 38 || e.keyCode == 40 || e.keyCode == 37 || e.keyCode == 39) && O$.isSafari())
          return false;
      };

      tabSet._oldfocus = tabSet.onfocus;
      tabSet.onfocus = function (e) {
        if (tabSet._oldfocus)
          tabSet._oldfocus(e);
        var tab = this._getTabByAbsoluteIndex(tabSet._index);
        O$.setStyleMappings(tab.childNodes[0], {focused:focusAreaClass});
        O$.setStyleMappings(tab, {focused:tabSet._focusedTabClass});
      };

      tabSet._oldblur = tabSet.onblur;
      tabSet.onblur = function (e) {
        if (tabSet._oldblur)
          tabSet._oldblur(e);
        var tab = this._getTabByAbsoluteIndex(tabSet._index);
        O$.setStyleMappings(tab.childNodes[0], {focused:null});
      };
    }
  }
};
