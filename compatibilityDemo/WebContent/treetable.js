function refreshButtons() {
    var saveChangesBtn = O$('form:saveChangesLink');
    var inEditingState = saveChangesBtn != null;
    return inEditingState;
}

function requestsSelectionChanged() {
    var inEditingState = refreshButtons();

    if (inEditingState)
        O$('form:saveChangesLink').onclick();
}

function clickButton(btnId) {
    var btn = O$(btnId);
    if (btn)
        btn.onclick();
}