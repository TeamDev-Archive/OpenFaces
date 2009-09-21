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

O$.Filter = {


  add: function(filterComponentId) {
    O$.requestComponentPortions(filterComponentId, ["newRow"], JSON.stringify({operation: 'add'}),
            O$.Filter._ajaxResponseProcessor);
  },

  clear: function(filterComponentId, index) {
    O$.requestComponentPortions(filterComponentId, ["removedFilter"], JSON.stringify({operation: 'clear'}),
            O$.Filter._ajaxResponseProcessor);
  },

  _remove: function(filterComponentId, index) {
    O$.requestComponentPortions(filterComponentId, ["removedRow:" + index], JSON.stringify({operation: 'remove', index: index}),
            O$.Filter._ajaxResponseProcessor);
  },

  _propertyChange: function(filterComponentId, index) {
    O$.requestComponentPortions(filterComponentId, ["operation:" + index], JSON.stringify({operation: 'propertyChange', index: index}),
            O$.Filter._ajaxResponseProcessor);
  },

  _operationChange: function(filterComponentId, index) {
    O$.requestComponentPortions(filterComponentId, ["parameters:" + index], JSON.stringify({operation: 'operationChange', index: index}),
            O$.Filter._ajaxResponseProcessor);
  },


  _rowContainer: function(component, index) {
    return O$.byIdOrName(component.id + "--row--" + index);
  },

  _noFilterRowContainer: function(component) {
    return O$.byIdOrName(component.id + "--no_filter");
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

  _removeAddButton: function(component) {
    var rowContainerSrc = component.lastChild;
    if (!rowContainerSrc) {
      return;
    }
    var addButton = O$.Filter._addButton(rowContainerSrc);
    rowContainerSrc.removeChild(addButton);
  },
  _moveAddButton: function(component) {
    var lastIndex = component.childNodes.length - 1, preLastIndex = lastIndex - 1;
    if (lastIndex < 1) {
      return;
    }
    var rowContainerSrc = component.childNodes[lastIndex];
    var rowContainerDest = component.childNodes[preLastIndex];
    var addButton = O$.Filter._addButton(rowContainerSrc);
    rowContainerSrc.removeChild(addButton);
    addButton.id = rowContainerDest.id + "--add";
    rowContainerDest.appendChild(addButton);
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
  },

  _ajaxResponseProcessor: function(component, portionName, portionHTML, portionScripts) {

    var newNode = O$.Filter._htmlToNode(portionHTML);
    if (portionName.match("newRow")) {

      O$.Filter._removeAddButton(component);
      component.appendChild(newNode);
      var noFilterRowContainer = O$.Filter._noFilterRowContainer(component);
      if (noFilterRowContainer) {
        component.removeChild(noFilterRowContainer);
      }
    } else if (portionName.match("removedRow")) {
      if (!newNode) {
        var index = portionName.split(":")[1];
        var rowContainer = O$.Filter._rowContainer(component, index);
        if (component.lastChild && rowContainer.id == component.lastChild.id) {
          O$.Filter._moveAddButton(component);
        }
        component.removeChild(rowContainer);
      } else {
        O$.removeAllChildNodes(component);
        component.appendChild(newNode);
      }

    } else if (portionName.match("removedFilter")) {
      O$.removeAllChildNodes(component);
      component.appendChild(newNode);
    } else if (portionName.match("operation")) {
      var newNodes = O$.Filter._htmlToNodes(portionHTML);
      var newNode2 = newNodes[1];
      var index = portionName.split(":")[1];
      var rowContainer = O$.Filter._rowContainer(component, index);
      var oldNode1 =
              O$.Filter._inverseContainer(rowContainer);
      var deleteButton = O$.Filter._deleteButton(rowContainer);

      if (oldNode1 != null) {
        rowContainer.replaceChild(newNode, oldNode1);
      } else {
        rowContainer.insertBefore(newNode, deleteButton);
      }

      var oldNode2 =
              O$.Filter._operationSelectorContainer(rowContainer);
      if (oldNode2 != null) {
        rowContainer.replaceChild(newNode2, oldNode2);
      } else {
        rowContainer.insertBefore(newNode2, deleteButton);
      }
      
      var parametersEditorContainer =
              O$.Filter._parametersEditorContainer(rowContainer);
      if (parametersEditorContainer) {
        rowContainer.removeChild(parametersEditorContainer);
      }

    } else if (portionName.match("parameters")) {

      var index = portionName.split(":")[1];
      var rowContainer = O$.Filter._rowContainer(component, index);
      var oldNode =
              O$.Filter._parametersEditorContainer(rowContainer);
      if (oldNode != null) {
        rowContainer.replaceChild(newNode, oldNode);
      } else {
        var deleteButton = O$.Filter._deleteButton(rowContainer);
        rowContainer.insertBefore(newNode, deleteButton);
      }

    }
    O$.executeScripts(portionScripts);
  }


}


