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

function checkRequestKeys(evt) {
  var e = evt ? evt : window.event;
  if (e.altKey || e.ctrlKey || e.shiftKey) {
    if (!e.altKey && !e.shiftKey) {
      if (e.keyCode == 13) // Ctrl+Enter
        clickButton('form1:requestsTreeTable:saveChangesBtn');
    }
    return;
  }
  switch (e.keyCode) {
    case 83: // S
      clickButton('form1:requestsTreeTable:addSubrequestBtn');
      break;
    case 69: // E
      editRequest();
      break;
    case 65: // A
      clickButton('form1:requestsTreeTable:addRequestBtn');
      break;
    case 46: // Delete
      clickButton('form1:requestsTreeTable:deleteRequestBtn');
      break;
  }
}

function clickButton(btnId) {
  var btn = O$(btnId);
  if (btn)
    btn.onclick();
}

function editRequest() {
  clickButton('form1:requestsTreeTable:editRequestBtn');
}

function requestsSelectionChanged() {
  var inEditingState = refreshButtons();

  if (inEditingState)
    O$('form1:requestsTreeTable:saveChangesBtn').onclick();
}

function refreshButtons() {
  var saveChangesBtn = O$('form1:requestsTreeTable:saveChangesBtn');
  var inEditingState = saveChangesBtn != null;

  var selectedNodeCount = O$('form1:requestsTreeTable').getSelectedNodeCount();
  var deleteRequestBtn = O$('form1:requestsTreeTable:deleteRequestBtn');
  var editRequestBtn = O$('form1:requestsTreeTable:editRequestBtn');
  var addSubRequestBtn = O$('form1:requestsTreeTable:addSubrequestBtn');

  if (!inEditingState) {
    if (selectedNodeCount != 1) {
      editRequestBtn.className = 'HK_holder_inactive';
      editRequestBtn.childNodes[0].childNodes[0].childNodes[0].className = 'HK_icon_edit_req_inactive';
      addSubRequestBtn.className = 'HK_holder_inactive';
      addSubRequestBtn.childNodes[0].childNodes[0].childNodes[0].className = 'HK_icon_add_sub_req_inactive';
    }
    if (selectedNodeCount == 0) {
      deleteRequestBtn.className = 'HK_holder_inactive';
      deleteRequestBtn.childNodes[0].childNodes[0].childNodes[0].className = 'HK_icon_del_req_inactive';
    } else {
      deleteRequestBtn.className = 'HK_holder';
      deleteRequestBtn.childNodes[0].childNodes[0].childNodes[0].className = 'HK_icon_del_req';
    }
    if (selectedNodeCount == 1) {
      editRequestBtn.className = 'HK_holder';
      editRequestBtn.childNodes[0].childNodes[0].childNodes[0].className = 'HK_icon_edit_req';
      addSubRequestBtn.className = 'HK_holder';
      addSubRequestBtn.childNodes[0].childNodes[0].childNodes[0].className = 'HK_icon_add_sub_req';
    }
  }

  return inEditingState;
}

function initFields() {
  setTimeout(function() {
    var treeTable = O$("form1:requestsTreeTable");
    var inputs = treeTable.getElementsByTagName("input");
    for (var i = 0, count = inputs.length; i < count; i++) {
      var input = inputs[i];
      if (input.type != "text")
        continue;
      input.focus();
      break;
    }
  }, 100);
}

var optionsArray = [];
function columnsDialogShow() {
  optionsArray = O$('form1:requestsTreeTable:optionsTLS').getValue();
}

function cancelColumnsDialog() {
  O$('form1:requestsTreeTable:optionsTLS').setValue(optionsArray);
  O$('form1:requestsTreeTable:selectColumnsPopup').hide();
}