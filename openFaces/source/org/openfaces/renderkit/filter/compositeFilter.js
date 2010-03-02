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

O$.CompositeFilter = {

  _init: function(clientId) {
    O$.initComponent(clientId, null, {
      apply: function() {
        this._ajaxForApply().run();
      },

      add: function() {
        O$.requestComponentPortions(clientId, ["newRow"], JSON.stringify({operation: "add"}),
                this._ajaxResponseProcessor);
      },

      clear: function() {
        O$.requestComponentPortions(clientId, ["removedFilter"], JSON.stringify({operation: "clear"}),
                this._ajaxResponseProcessor);
      },

      remove: function(index) {
        O$.requestComponentPortions(clientId, ["removedRow:" + index], JSON.stringify({operation: "remove", index: index}),
                this._ajaxResponseProcessor);
      },

      _propertyChange: function(index) {
        O$.requestComponentPortions(clientId, ["operation:" + index], JSON.stringify({operation: "propertyChange", index: index}),
                this._ajaxResponseProcessor);
      },

      _operationChange: function(index) {
        O$.requestComponentPortions(clientId, ["parameters:" + index], JSON.stringify({operation: "operationChange", index: index}),
                this._ajaxResponseProcessor);
      },

      _rowContainer: function(index) {
        return O$.byIdOrName(clientId + "--row--" + index);
      },

      _noFilterRowContainer: function() {
        return O$.byIdOrName(clientId + "--no_filter");
      },

      _applyButton: function() {
        return O$.byIdOrName(clientId + "--applyButton");
      },

      _ajaxForApply: function() {
        return O$.byIdOrName(clientId + "--ajaxForApply");
      },

      _operationSelectorContainer: function(rowContainer) {
        return O$.byIdOrName(rowContainer.id + "--operationSelector");
      },

      _inverseContainer: function(rowContainer) {
        return O$.byIdOrName(rowContainer.id + "--inverse");
      },

      _parametersEditorContainer: function(rowContainer) {
        return O$.byIdOrName(rowContainer.id + "--parametersEditor");
      },

      _addButton: function(rowContainer) {
        return O$.byIdOrName(rowContainer.id + "--add");
      },

      _deleteButton: function(rowContainer) {
        return O$.byIdOrName(rowContainer.id + "--delete");
      },

      _removeAddButton: function() {
        var rowContainerSrc = this.lastChild;
        if (!rowContainerSrc) {
          return;
        }
        var addButton = this._addButton(rowContainerSrc);
        rowContainerSrc.removeChild(addButton);
      },

      _moveAddButton: function() {
        var lastIndex = this.childNodes.length - 1, preLastIndex = lastIndex - 1;
        if (lastIndex < 1) {
          return;
        }
        var rowContainerSrc = this.childNodes[lastIndex];
        var rowContainerDest = this.childNodes[preLastIndex];
        var addButton = this._addButton(rowContainerSrc);
        rowContainerSrc.removeChild(addButton);
        addButton.id = rowContainerDest.id + "--add";
        rowContainerDest.appendChild(addButton);
      },

      _ajaxResponseProcessor: function(filter, portionName, portionHTML, portionScripts) {
        function focusLastField(container) {
          var fc = O$.getLastFocusableControl(container, function(el) {
            // don't auto-focus add/remove buttons
            return !(el.nodeName.toLowerCase() == "input" && el.type == "button");
          });
          if (fc) {
            setTimeout(function() {
              fc.focus();
            }, 1);
          }
        }

        var index, rowContainer;
        var newNode = O$.CompositeFilter._htmlToNode(portionHTML);
        if (portionName.match("newRow")) {
          filter._removeAddButton();
          filter.appendChild(newNode);
          focusLastField(newNode);
          var noFilterRowContainer = filter._noFilterRowContainer();
          if (noFilterRowContainer) {
            filter.removeChild(noFilterRowContainer);
          }
        } else if (portionName.match("removedRow")) {
          if (!newNode) {
            index = portionName.split(":")[1];
            rowContainer = filter._rowContainer(index);
            if (filter.lastChild && rowContainer.id == filter.lastChild.id) {
              filter._moveAddButton();
            }
            filter.removeChild(rowContainer);
          } else {
            O$.removeAllChildNodes(filter);
            filter.appendChild(newNode);
          }
          focusLastField(filter);

        } else if (portionName.match("removedFilter")) {
          O$.removeAllChildNodes(filter);
          filter.appendChild(newNode);

        } else if (portionName.match("operation")) {
          var newNodes = O$.CompositeFilter._htmlToNodes(portionHTML);
          var newNode2 = newNodes[1];
          index = portionName.split(":")[1];
          rowContainer = filter._rowContainer(index);
          var oldNode1 = filter._inverseContainer(rowContainer);
          var deleteButton = filter._deleteButton(rowContainer);

          if (oldNode1 != null) {
            rowContainer.replaceChild(newNode, oldNode1);
          } else {
            rowContainer.insertBefore(newNode, deleteButton);
          }

          var oldNode2 =
                  filter._operationSelectorContainer(rowContainer);
          if (oldNode2 != null) {
            rowContainer.replaceChild(newNode2, oldNode2);
          } else {
            rowContainer.insertBefore(newNode2, deleteButton);
          }

          var parametersEditorContainer =
                  filter._parametersEditorContainer(rowContainer);
          if (parametersEditorContainer) {
            rowContainer.removeChild(parametersEditorContainer);
          }
          
          focusLastField(rowContainer);

        } else if (portionName.match("parameters")) {
          index = portionName.split(":")[1];
          rowContainer = filter._rowContainer(index);
          if (newNode) {
            var oldNode =
                    filter._parametersEditorContainer(rowContainer);
            if (oldNode != null) {
              rowContainer.replaceChild(newNode, oldNode);
            } else {
              deleteButton = filter._deleteButton(rowContainer);
              rowContainer.insertBefore(newNode, deleteButton);
            }
          }
          focusLastField(rowContainer);
        }
        O$.executeScripts(portionScripts);
      }
    });
  },

  _htmlToNode: function(html) {
    if (html == "") {
      return null;
    }
    var tempDiv = document.createElement("div");
    tempDiv.innerHTML = html;
    var result = tempDiv.childNodes[0];
    return result;
  },

  _htmlToNodes: function(html) {
    if (html == "") {
      return null;
    }
    var tempDiv = document.createElement("div");
    tempDiv.innerHTML = html;
    var result = tempDiv.childNodes;
    return result;
  }

};


