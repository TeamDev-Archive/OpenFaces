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

O$.TabbedPane = {
  _init: function(clientId, rolloverClass,
                  containerClass, rolloverContainerClass, borderClass, loadingMode, focusable, focusedClass,
                  onselectionchange) {
    var tabbedPane = O$(clientId);
    O$.extend(tabbedPane, {
      _loadingMode: loadingMode,
      _rolloverClass: rolloverClass,
      _containerClass: containerClass,
      _rolloverContainerClass: rolloverContainerClass,
      onselectionchange: onselectionchange,
      _tabSet: O$(clientId + "--tabSet"),

      getPageCount: function() {
        return tabbedPane._tabSet.getTabCount();
      },

      getSelectedIndex: function () {
        return tabbedPane._tabSet.getSelectedIndex();
      },

      setSelectedIndex: function(index) {
        tabbedPane._tabSet.setSelectedIndex(index);
      },

      _getPageContainer: function(pageIndex) {
        var result = O$(this.id + "::pane" + pageIndex);
        return result;
      },

      _changeCurrentPageContainer: function(newPageContainer) {
        var prevPageContainer = this._currentPageContainer;
        O$.TabbedPane._hideContainer(prevPageContainer);

        O$.TabbedPane._getContianerStyleElement(newPageContainer).className = tabbedPane._containerClass;
        O$.TabbedPane._showContainer(newPageContainer);
        tabbedPane._currentPageContainer = newPageContainer;
        O$.repaintAreaForOpera(tabbedPane);
      }
    });

    tabbedPane._tabSet._tabbedPane = tabbedPane;

    if (focusable) {
      tabbedPane.focus = function () {
        this._tabSet.focus();
      };
      tabbedPane.blur = function () {
        this._tabSet.blur();
      };
      O$.addEventHandler(tabbedPane, "click", function (evt) {
        if (tabbedPane._tabSet.onclick)
          tabbedPane._tabSet.onclick(evt);
        if (window.getSelection) {
          if (window.getSelection() != "")
            return; // don't switch focus to make text selection possible under FF (JSFC-1134)
        }
        var e = evt ? evt : event;
        if (tabbedPane._focused)
          return;

        var target = (e != null)
                ? (e.target ? e.target : e.srcElement)
                : null;
        if (O$.isControlFocusable(target))
          return;
        tabbedPane._tabSet._preventPageScrolling = true;
        tabbedPane._tabSet.focus();
        tabbedPane._tabSet._preventPageScrolling = false;
      });
      O$.addEventHandler(tabbedPane._tabSet, "focus", function () {
        O$.setStyleMappings(tabbedPane, {
          focused: focusedClass
        });
      });

      O$.addEventHandler(tabbedPane._tabSet, "blur", function () {
        O$.setStyleMappings(tabbedPane, {
          focused: null
        });
      });

    }

    var currentPageContainer = tabbedPane._getPageContainer(tabbedPane._tabSet._index);
    if (!currentPageContainer)
      return;
    currentPageContainer.parentNode.fontSize = "0px";
    tabbedPane._currentPageContainer = currentPageContainer;
    var pagesContainer = currentPageContainer.parentNode;
    tabbedPane._pagesContainer = pagesContainer;
    pagesContainer.className = borderClass;

    // setup methods
    O$.assignEventHandlerField(tabbedPane, "onmouseover", function (evt) {
      O$.appendClassNames(this, [this._rolloverClass]);
      O$.appendClassNames(O$.TabbedPane._getContianerStyleElement(this._currentPageContainer), [this._rolloverContainerClass]);
      O$.repaintAreaForOpera(this, true);

      if (this._tabSet._tabs) {
        var tab = this._tabSet._getTabByAbsoluteIndex(tabbedPane._tabSet._index);
        tab._tabbedPaneCall = true;
        tab.onmouseover(evt);
      }
    });
    O$.assignEventHandlerField(tabbedPane, "onmouseout", function (evt) {
      O$.excludeClassNames(this, [this._rolloverClass]);
      O$.excludeClassNames(O$.TabbedPane._getContianerStyleElement(this._currentPageContainer), [this._rolloverContainerClass]);
      if (this._tabSet._tabs) {
        var tab = this._tabSet._getTabByAbsoluteIndex(tabbedPane._tabSet._index);
        tab._tabbedPaneCall = true;
        tab.onmouseout(evt);
      }
      O$.repaintAreaForOpera(this, true);
    });

    tabbedPane._tabSet.onchange = function (evt) {
      var absoluteIndex = evt._absoluteIndex;
      var tabbedPane = this._tabbedPane;
      if (tabbedPane.onselectionchange) {
        if (tabbedPane.onselectionchange(evt) === false || evt.returnValue === false)
          return false;
      }
      var loadingMode = tabbedPane._loadingMode;
      var newPageContainer;
      if (loadingMode == "server") {
        O$.submitEnclosingForm(tabbedPane);
      } else if (loadingMode == "ajaxLazy" || loadingMode == "ajaxAlways") {
        newPageContainer = tabbedPane._getPageContainer(absoluteIndex);
        if (!newPageContainer || loadingMode == "ajaxAlways")
          O$.requestComponentPortions(tabbedPane.id, ["page:" + absoluteIndex], null, function(tabbedPane, portionName, portionHTML, portionScripts) {
            var tempDiv = document.createElement("div");
            tempDiv.innerHTML = portionHTML;
            var newPageContainer = tempDiv.childNodes[0];
            tabbedPane._pagesContainer.appendChild(newPageContainer);
            O$.executeScripts(portionScripts);

            O$.assert(portionName.substring(0, "page:".length) == "page:", "tabbedPanePageLoaded: illegal portion prefix:" + portionName);
            var pageNoStr = portionName.substring("page:".length);
            var pageNo = eval(pageNoStr);
            tabbedPane._changeCurrentPageContainer(newPageContainer);
            tabbedPane.setSelectedIndex(pageNo);
          });
        else
          tabbedPane._changeCurrentPageContainer(newPageContainer);
      } else if (loadingMode == "client") {
        newPageContainer = tabbedPane._getPageContainer(absoluteIndex);
        tabbedPane._changeCurrentPageContainer(newPageContainer);
      } else
        O$.assert(false, "tabSet.onchange - invalid loading mode: " + loadingMode);
    };

    // relayout pane container
    O$.TabbedPane._showContainer(currentPageContainer);
  },

  _hideContainer: function(paneContainer) {
    paneContainer.style.display = "none";
  },

  _showContainer: function(paneContainer) {
    paneContainer.style.display = "";
    paneContainer.parentNode.style.height = "100%";
    paneContainer.parentNode.style.width = "100%";
    paneContainer.style.width = "100%";
    paneContainer.style.height = "100%";
    O$.repaintAreaForOpera(paneContainer, true); // JSFC-2244
  },

  _getContianerStyleElement: function(container) {
    if (!container._styleElement)
      container._styleElement = container.getElementsByTagName("td")[0];
    return container._styleElement;
  },

  _getTargetComponentHasOwnKeyBehavior: function(evt) {
    var element = evt.target ? evt.target : evt.srcElement;
    var tagName = element ? element.tagName : null;
    if (tagName)
      tagName = tagName.toLowerCase();
    var elementHasItsOwnMouseBehavior =
            tagName == "input" ||
            tagName == "textarea";
    return  elementHasItsOwnMouseBehavior && element.className != "o_hiddenFocus";
  }
};