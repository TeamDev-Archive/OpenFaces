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

O$.MultiPage = {
  _init: function(clientId, rolloverClass, containerClass, loadingMode, pageCount, selectedIndex) {
    var tabbedPane = O$(clientId);
    O$.extend(tabbedPane, {
      _loadingMode: loadingMode,
      _rolloverClass: rolloverClass,
      _containerClass: containerClass,

      getPageCount: function() {
        return pageCount;
      },

      getSelectedIndex: function () {
        return selectedIndex;
      },

      setSelectedIndex: function(index) {
        if (selectedIndex == index) return;
        selectedIndex = index;
        tabbedPane.doSetSelectedIndex(index);
      },

      _getPageContainer: function(pageIndex) {
        var result = O$(this.id + "::pane" + pageIndex);
        return result;
      },

      _changeCurrentPageContainer: function(newPageContainer) {
        var prevPageContainer = this._currentPageContainer;
        O$.MultiPage._hideContainer(prevPageContainer);

        O$.MultiPage._getContianerStyleElement(newPageContainer).className = tabbedPane._containerClass;
        O$.MultiPage._showContainer(newPageContainer);
        tabbedPane._currentPageContainer = newPageContainer;
        O$.repaintAreaForOpera(tabbedPane);
      }
    });


    var currentPageContainer = tabbedPane._getPageContainer(selectedIndex);
    if (!currentPageContainer)
      return;
    currentPageContainer.parentNode.fontSize = "0px";
    tabbedPane._currentPageContainer = currentPageContainer;
    var pagesContainer = currentPageContainer.parentNode;
    tabbedPane._pagesContainer = pagesContainer;

    // setup methods
    O$.assignEventHandlerField(tabbedPane, "onmouseover", function () {
      O$.appendClassNames(this, [this._rolloverClass]);
      O$.repaintAreaForOpera(this, true);
    });
    O$.assignEventHandlerField(tabbedPane, "onmouseout", function () {
      O$.excludeClassNames(this, [this._rolloverClass]);
      O$.repaintAreaForOpera(this, true);
    });

    tabbedPane.doSetSelectedIndex = function(absoluteIndex) {
      O$.setHiddenField(tabbedPane, clientId + "::index", absoluteIndex);
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
    O$.MultiPage._showContainer(currentPageContainer);
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