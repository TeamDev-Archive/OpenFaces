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

  _init: function(filterComponentId){
    
    var filter = O$(filterComponentId);
    
    filter.add = function() {
      O$.requestComponentPortions(filterComponentId, ["newRow"], JSON.stringify({operation: 'add'}),
              this._ajaxResponseProcessor);
    },
  
    filter.clear = function() {
      O$.requestComponentPortions(filterComponentId, ["removedFilter"], JSON.stringify({operation: 'clear'}),
              this._ajaxResponseProcessor);
    };

    filter.remove = function(index) {
      O$.requestComponentPortions(filterComponentId, ["removedRow:" + index], JSON.stringify({operation: 'remove', index: index}),
              this._ajaxResponseProcessor);
    };
  
    filter._propertyChange = function(index) {
      O$.requestComponentPortions(filterComponentId, ["operation:" + index], JSON.stringify({operation: 'propertyChange', index: index}),
              this._ajaxResponseProcessor);
    };
  
    filter._operationChange = function(index) {
      O$.requestComponentPortions(filterComponentId, ["parameters:" + index], JSON.stringify({operation: 'operationChange', index: index}),
              this._ajaxResponseProcessor);
    };
  
    filter._rowContainer = function(index) {
      return O$.byIdOrName(filterComponentId + "--row--" + index);
    };
  
    filter._noFilterRowContainer = function() {
      return O$.byIdOrName(filterComponentId + "--no_filter");
    };
  
    filter._operationSelectorContainer = function(rowContainer) {
      return O$.byIdOrName(rowContainer.id + "--operationSelector");
    };

    filter._inverseContainer = function(rowContainer) {
      return O$.byIdOrName(rowContainer.id + "--inverse");
    };

    filter._parametersEditorContainer = function(rowContainer) {
      return O$.byIdOrName(rowContainer.id + "--parametersEditor");
    };

    filter._addButton = function(rowContainer) {
      return O$.byIdOrName(rowContainer.id + "--add");
    };

    filter._deleteButton = function(rowContainer) {
      return O$.byIdOrName(rowContainer.id + "--delete");
    };
  
    filter._removeAddButton = function() {
      var rowContainerSrc = filter.lastChild;
      if (!rowContainerSrc) {
        return;
      }
      var addButton = filter._addButton(rowContainerSrc);
      rowContainerSrc.removeChild(addButton);
    };

    filter._moveAddButton = function() {
      var lastIndex = filter.childNodes.length - 1, preLastIndex = lastIndex - 1;
      if (lastIndex < 1) {
        return;
      }
      var rowContainerSrc = filter.childNodes[lastIndex];
      var rowContainerDest = filter.childNodes[preLastIndex];
      var addButton = filter._addButton(rowContainerSrc);
      rowContainerSrc.removeChild(addButton);
      addButton.id = rowContainerDest.id + "--add";
      rowContainerDest.appendChild(addButton);
    };

    filter._ajaxResponseProcessor = function(filter, portionName, portionHTML, portionScripts) {
  
      var newNode = O$.Filter._htmlToNode(portionHTML);
      if (portionName.match("newRow")) {
  
        filter._removeAddButton();
        filter.appendChild(newNode);
        var noFilterRowContainer = filter._noFilterRowContainer();
        if (noFilterRowContainer) {
          filter.removeChild(noFilterRowContainer);
        }
      } else if (portionName.match("removedRow")) {
        if (!newNode) {
          var index = portionName.split(":")[1];
          var rowContainer = filter._rowContainer(index);
          if (filter.lastChild && rowContainer.id == filter.lastChild.id) {
            filter._moveAddButton();
          }
          filter.removeChild(rowContainer);
        } else {
          O$.removeAllChildNodes(filter);
          filter.appendChild(newNode);
        }
  
      } else if (portionName.match("removedFilter")) {
        O$.removeAllChildNodes(filter);
        filter.appendChild(newNode);

      } else if (portionName.match("operation")) {
        var newNodes = O$.Filter._htmlToNodes(portionHTML);
        var newNode2 = newNodes[1];
        var index = portionName.split(":")[1];
        var rowContainer = filter._rowContainer(index);
        var oldNode1 =
                filter._inverseContainer(rowContainer);
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
  
      } else if (portionName.match("parameters")) {
  
        var index = portionName.split(":")[1];
        var rowContainer = filter._rowContainer(index);
        var oldNode =
                filter._parametersEditorContainer(rowContainer);
        if (oldNode != null) {
          rowContainer.replaceChild(newNode, oldNode);
        } else {
          var deleteButton = filter._deleteButton(rowContainer);
          rowContainer.insertBefore(newNode, deleteButton);
        }
  
      }
      O$.executeScripts(portionScripts);
    };
  },

  _htmlToNode : function(html) {
      if (html == "") {
        return null;
      }
      var tempDiv = document.createElement("div");
      tempDiv.innerHTML = html;
      var result = tempDiv.childNodes[0];
      return result;
    },

    _htmlToNodes : function(html) {
      if (html == "") {
        return null;
      }
      var tempDiv = document.createElement("div");
      tempDiv.innerHTML = html;
      var result = tempDiv.childNodes;
      return result;
    }

}


